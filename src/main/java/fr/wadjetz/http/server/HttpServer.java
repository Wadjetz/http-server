package fr.wadjetz.http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
    public static void run(int port, HttpHandler handler) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();

        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        HttpRequest httpRequest = parseRequest(bufferedReader);

        HttpResponse httpResponse = handler.apply(httpRequest, new HttpResponse());

        String response = buildResponse(httpRequest, httpResponse.withHeader("Content-Length", httpResponse.getBody().length() + ""));

        printWriter.print(response);
        printWriter.flush();
        printWriter.close();
        bufferedReader.close();
        socket.close();
        serverSocket.close();
    }

    public static String buildResponse(HttpRequest httpRequest, HttpResponse httpResponse) {
        String protocolLine = httpRequest.getVersion() + " " + httpResponse.getStatus() + " " + httpResponse.getStatusText();
        String responseHeaders = "";
        for (Map.Entry<String, String> entry : httpResponse.getHeaders().entrySet()) {
            responseHeaders += entry.getKey() + ": " + entry.getValue() + "\n";
        }
        return protocolLine + "\n" + responseHeaders + "\n" + httpResponse.getBody();
    }

    public static HttpRequest parseRequest(BufferedReader bufferedReader) throws IOException {
        String inputLine;
        int lineCounter = 0;
        HttpRequest httpRequest = new HttpRequest();
        HashMap<String, String> headers = new HashMap<>();
        while (true) {
            inputLine = bufferedReader.readLine();
            if (inputLine.trim().isEmpty() || inputLine == null) {
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
        httpRequest.setHeaders(headers);
        return httpRequest;
    }
}
