package fr.wadjetz.http.server;

import java.util.*;

public class HttpRouter {
    private Map<String, HttpHandler> routes = new HashMap<>();

    private List<Route> routesList = new ArrayList<>();

    public HttpRouter addRoute(String path, HttpHandler httpHandler) {
        routes.put(path, httpHandler);
        return this;
    }

    public HttpRouter _addRoute(Route route) {
        routesList.add(route);
        return this;
    }

    public Optional<HttpHandler> _resolve(HttpRequest httpRequest) {
        return routesList.stream().filter(route -> {
            String path = httpRequest.getAbsolutePath();

            return false;
        }).map(Route::getHttpHandler).findFirst();
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
