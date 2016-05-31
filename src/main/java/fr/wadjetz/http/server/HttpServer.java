package fr.wadjetz.http.server;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpServer {

    @FunctionalInterface
    interface Resolver {
        Optional<HttpHandler> apply(HttpRequest request);
    }

    public static void run(final InetSocketAddress address, HttpRouter httpRouter) throws IOException {
        serverLoop(address, httpRouter::resolve);
    }

    public static void run(final InetSocketAddress address, HttpVHost httpVHost) throws IOException {
        serverLoop(address, httpVHost::resolve);
    }

    private static void serverLoop(final InetSocketAddress address, Resolver resolver) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(address);

        while (true) {
            final Socket socket = serverSocket.accept();

            new Thread(() -> {

                PrintWriter printWriter = null;
                BufferedReader bufferedReader = null;
                HttpRequest httpRequest = null;
                HttpResponse httpResponse = null;

                try {
                    printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    try {
                        httpRequest = parseRequest(bufferedReader);
                        Optional<HttpHandler> httpHandlerOptional = resolver.apply(httpRequest);

                        if (httpHandlerOptional.isPresent()) {
                            httpResponse = httpHandlerOptional.get().apply(httpRequest, new HttpResponse());

                            System.out.println(httpResponse);
                        } else {
                            httpResponse = new HttpResponse().withStatus(404);
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

    private static String buildResponseHeader(HttpRequest httpRequest, HttpResponse httpResponse) {
        String protocolLine = httpRequest.getVersion() + " " + httpResponse.getStatus() + " " + httpResponse.getStatusText();
        String responseHeaders = "";
        for (Map.Entry<String, String> entry : httpResponse.getHeaders().entrySet()) {
            responseHeaders += entry.getKey() + ": " + entry.getValue() + "\r\n";
        }
        return protocolLine + "\r\n" + responseHeaders + "\r\n";
    }

    private static HttpRequest parseRequest(BufferedReader bufferedReader) throws IOException, HttpException {
        // Parse first line
        String firstLine = bufferedReader.readLine();
        HttpRequest httpRequest = parseProtocolLine(firstLine);
        Map<String, String> headers = parseHeaders(bufferedReader);
        byte[] body = parseBody(headers, bufferedReader);
        httpRequest.setBody(body);
        httpRequest.setHeaders(headers);
        return httpRequest;
    }

    private static byte[] parseBody(Map<String, String> headers, BufferedReader bufferedReader) throws IOException, HttpException {
        String contentLength = headers.get("Content-Length".toLowerCase());
        byte[] body = null;
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

        return body;
    }

    private static Map<String, String> parseHeaders(BufferedReader bufferedReader) throws IOException {
        Map<String, String> headers = new HashMap<>();

        while (true) {
            String inputLine = bufferedReader.readLine();
            System.out.println(inputLine);
            if (inputLine.trim().isEmpty()) {
                break;
            }
            int index = inputLine.indexOf(":");
            if (index != -1) {
                String key = inputLine.substring(0, index);
                String value = inputLine.substring(index+1);
                headers.put(key.trim().toLowerCase(), value.trim());
            }
        }

        return headers;
    }

    private static HttpRequest parseProtocolLine(String line) throws HttpException {
        HttpRequest httpRequest = new HttpRequest();

        String[] splites = line.split(" ");

        if (splites.length != 3) {
            throw new HttpException(400, "First Line Malformed");
        }

        httpRequest.setMethod(splites[0].trim().toUpperCase());
        httpRequest.setAbsolutePath(splites[1].trim());
        httpRequest.setVersion(splites[2].trim());

        return httpRequest;
    }
}
