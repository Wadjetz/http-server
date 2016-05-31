package fr.wadjetz.http.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRouter {
    private Map<String, HttpHandler> routes = new HashMap<>();

    public HttpRouter addRoute(String path, HttpHandler httpHandler) {
        routes.put(path, httpHandler);
        return this;
    }

    public Optional<HttpHandler> resolve(HttpRequest request) {
        String path = request.getAbsolutePath();
        HttpHandler httpHandler = routes.get(path);
        return Optional.ofNullable(httpHandler);
    }

    @Override
    public String toString() {
        return "HttpRouter{" +
                "routes=" + routes +
                '}';
    }
}
