package fr.wadjetz.http.server;

import java.io.File;

public class HttpStaticFileHandler implements HttpHandler {

    private String rootPath;

    public HttpStaticFileHandler(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public HttpResponse apply(HttpRequest request, HttpResponse response) {
        String path = request.getAbsolutePath();

        File file = new File(path);


        if (file.isDirectory()) {
            file.listFiles();
        }

        System.out.println(file.isDirectory());

        return null;
    }

    private HttpResponse buildDirectory(HttpRequest request, HttpResponse response, File directory) {

        String html = "<!doctype html>" +
                "<html>" +
                    "<head>" +
                        "<title>" +
                            "Files" +
                        "</title>" +
                    "</head>" +
                    "<body>" +
                    "</body>" +
                "</html>";

        return response.html(html);
    }
}
