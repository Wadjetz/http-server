package fr.wadjetz.http.server;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HttpStaticFileHandler implements HttpHandler {

    private String rootPath;

    public HttpStaticFileHandler(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public HttpResponse apply(HttpRequest request, HttpResponse response) {

        System.out.println("HttpStaticFileHandler request = " + request);

        String requestPath = request.getAbsolutePath();
        String path = buildPath(this.rootPath, requestPath);

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

    private String buildPath(String root, String path) {
        return (root + path).replaceAll("'/'*", "/").replaceAll("'..'*", "");
    }

    private HttpResponse buildDirectory(HttpRequest request, HttpResponse response, File directory) {

        String f = Arrays.stream(directory.listFiles())
                .map(file -> "<div><a href=\"" + buildPath(request.getAbsolutePath(), "/" + file.getName()) + "\">" + file.getName() + "</a></div>")
                .collect(Collectors.toList())
                .stream()
                .reduce("", String::concat);

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
