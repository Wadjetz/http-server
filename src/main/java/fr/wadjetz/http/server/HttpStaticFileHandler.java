package fr.wadjetz.http.server;

import java.nio.file.Path;
import java.util.Optional;

public class HttpStaticFileHandler {

    private Path rootPath;

    public HttpStaticFileHandler(Path rootPath) {
        this.rootPath = rootPath;
    }

    public Optional<HttpResponse> apply(HttpRequest request, HttpResponse response) {
        String path = request.getAbsolutePath();

        Path file = rootPath.resolve(path);

        System.out.println(file.toFile().isDirectory());

        return Optional.empty();
    }
}
