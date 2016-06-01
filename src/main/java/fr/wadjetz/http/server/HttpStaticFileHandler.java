package fr.wadjetz.http.server;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HttpStaticFileHandler implements HttpHandler {

    private String rootPath;

    public HttpStaticFileHandler(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public HttpResponse apply(HttpRequest request, HttpResponse response) {
        String path = null;
        try {
            path = URLDecoder.decode(request.getAbsolutePath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("Path = " + path);

        File file = new File(path);

        if (file.exists()) {
            if (file.isDirectory()) {
                return buildDirectory(request, response, file);
            } else {
                return response.withFile(file);
            }
        } else {
            return response.withStatus(404);
        }
    }

    private HttpResponse buildDirectory(HttpRequest request, HttpResponse response, File directory) {

        String f = Arrays.stream(directory.listFiles()).map(file -> {
            return "<div><a href=\"/" + file.getName() +"\">" + file.getName() + "</a></div>";
        }).collect(Collectors.toList()).stream().reduce("", String::concat);

        String html = "<!doctype html>" +
                "<html>" +
                    "<head>" +
                        "<title>" +
                            "Files" +
                        "</title>" +
                    "</head>" +
                    "<body>" +
                        f +
                    "</body>" +
                "</html>";

        return response.html(html);
    }
}
