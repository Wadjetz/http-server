package fr.wadjetz.http.server;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpServer {
    public static void run(final int port, final HttpStaticFileHandler httpStaticFileHandler, final HttpHandler handler) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            final Socket socket = serverSocket.accept();

            new Thread(() -> {
                PrintWriter printWriter = null;
                BufferedReader bufferedReader = null;
                HttpRequest httpRequest = null;
                try {
                    printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    try {
                        httpRequest = parseRequest(bufferedReader);
                        HttpResponse httpResponse = null;
                        Optional<HttpResponse> staticFileHandler = httpStaticFileHandler.apply(httpRequest, new HttpResponse());

                        if (staticFileHandler.isPresent()) {
                            System.out.println("staticFileHandler");
                            httpResponse = staticFileHandler.get();
                        } else {
                            System.out.println("handler");
                            httpResponse = handler.apply(httpRequest, new HttpResponse());
                        }

                        if (httpResponse.getBody().isPresent()) {
                            String responseHeader = buildResponseHeader(httpRequest, httpResponse.withHeader("Content-Length", httpResponse.getBody().get().length() + ""));
                            printWriter.print(responseHeader);
                            printWriter.print(httpResponse.getBody().get() + "\r\n");
                        } else if (httpResponse.getFile().isPresent()) {
                            String responseHeader = buildResponseHeader(httpRequest, httpResponse);
                            printWriter.print(responseHeader);
                            IOUtils.copyLarge(new FileReader(httpResponse.getFile().get()),printWriter);
                        } else {
                            String responseHeader = buildResponseHeader(httpRequest, httpResponse);
                            printWriter.print(responseHeader);
                        }

                    } catch (HttpException e) {
                        e.printStackTrace();
                        String responseHeader = buildResponseHeader(httpRequest,  new HttpResponse().withStatus(e.getErrorCode()).withStatusText(e.getMessage()));
                        printWriter.print(responseHeader);
                    }

                    printWriter.flush();

                    bufferedReader.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (printWriter != null) printWriter.close();
                }
            }).start();

            if (false) break;
        }

        serverSocket.close();
    }

    public static String buildResponseHeader(HttpRequest httpRequest, HttpResponse httpResponse) {
        String protocolLine = httpRequest.getVersion() + " " + httpResponse.getStatus() + " " + httpResponse.getStatusText();
        String responseHeaders = "";
        for (Map.Entry<String, String> entry : httpResponse.getHeaders().entrySet()) {
            responseHeaders += entry.getKey() + ": " + entry.getValue() + "\r\n";
        }
        return protocolLine + "\r\n" + responseHeaders + "\r\n";
    }

    public static HttpRequest parseRequest(BufferedReader bufferedReader) throws IOException, HttpException {
        String inputLine;
        int lineCounter = 0;
        byte[] body = null;
        HttpRequest httpRequest = new HttpRequest();
        HashMap<String, String> headers = new HashMap<>();
        while (true) {
            inputLine = bufferedReader.readLine();
            System.out.println(inputLine);
            if (inputLine.trim().isEmpty()) {
                break;
            }
            if (lineCounter == 0) {
                String[] splites = inputLine.split(" ");
                httpRequest.setMethod(splites[0].trim().toUpperCase());
                httpRequest.setAbsolutePath(splites[1].trim());
                httpRequest.setVersion(splites[2].trim());
            } else {
                int index = inputLine.indexOf(":");
                if (index != -1) {
                    String key = inputLine.substring(0, index);
                    String value = inputLine.substring(index+1);
                    headers.put(key.trim().toLowerCase(), value.trim());
                }
            }
            lineCounter++;
        }

        String contentLength = headers.get("Content-Length".toLowerCase());

        if (contentLength != null) {
            int length = Integer.parseInt(contentLength);
            if(length < 0)
                throw new HttpException(400, "Malformed content-length header:" + headers.get("content-length"));

            body = new byte[length];
            int i = 0;

            while(i < length) {
                body[i++] = (byte)bufferedReader.read();
            }
        }

        httpRequest.setBody(body);
        httpRequest.setHeaders(headers);
        return httpRequest;
    }
}
