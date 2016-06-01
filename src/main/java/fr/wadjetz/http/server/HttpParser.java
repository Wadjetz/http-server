package fr.wadjetz.http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpParser {

    public static String buildResponseHeader(HttpRequest httpRequest, HttpResponse httpResponse) {
        String protocolLine = httpRequest.getVersion() + " " + httpResponse.getStatus() + " " + httpResponse.getStatusText();
        String responseHeaders = "";
        for (Map.Entry<String, String> entry : httpResponse.getHeaders().entrySet()) {
            responseHeaders += entry.getKey() + ": " + entry.getValue() + "\r\n";
        }
        return protocolLine + "\r\n" + responseHeaders + "\r\n";
    }

    public static HttpRequest parseRequestLine(String line) throws HttpException, UnsupportedEncodingException {
        HttpRequest httpRequest = new HttpRequest();

        String[] splites = line.split(" ");

        if (splites.length != 3) {
            throw new HttpException(400, "First Line Malformed");
        }

        String method = URLDecoder.decode(splites[0].trim().toUpperCase(), "UTF-8");
        String absolutePath = URLDecoder.decode(splites[1].trim(), "UTF-8");
        String version = URLDecoder.decode(splites[2].trim(), "UTF-8");

        httpRequest.setMethod(method);
        httpRequest.setAbsolutePath(absolutePath);
        httpRequest.setVersion(version);

        return httpRequest;
    }

    public static byte[] parseBody(Map<String, String> headers, BufferedReader bufferedReader) throws IOException, HttpException {
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

    public static Map<String, String> parseHeaders(BufferedReader bufferedReader) throws IOException {
        Map<String, String> headers = new HashMap<>();

        while (true) {
            String inputLine = bufferedReader.readLine();
            //System.out.println(inputLine);
            if (inputLine.trim().isEmpty()) {
                break;
            }
            int index = inputLine.indexOf(":");
            if (index != -1) {
                String key = URLDecoder.decode(inputLine.substring(0, index), "UTF-8");
                String value = URLDecoder.decode(inputLine.substring(index+1), "UTF-8");
                headers.put(key.trim().toLowerCase(), value.trim());
            }
        }

        return headers;
    }

}
