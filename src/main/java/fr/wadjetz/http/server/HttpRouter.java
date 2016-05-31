package fr.wadjetz.http.server;

import java.util.*;

public class HttpRouter {
    private Map<String, HttpHandler> routes = new HashMap<>();

    private List<Route> routesList = new ArrayList<>();

    public HttpRouter addRoute(Route route) {
        routesList.add(route);
        return this;
    }

    public Optional<HttpHandler> resolve(HttpRequest httpRequest) {
        return routesList.stream().filter(route -> {
            String path = httpRequest.getAbsolutePath();
            return route.getPattern().matcher(path).find();
        }).map(Route::getHttpHandler).findFirst();
    }

    @Override
    public String toString() {
        return "HttpRouter{" +
                "routes=" + routes +
                '}';
    }
}
