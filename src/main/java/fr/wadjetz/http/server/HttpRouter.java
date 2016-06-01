package fr.wadjetz.http.server;

import java.util.*;

public class HttpRouter {
    private List<Route> routes = new ArrayList<>();

    public HttpRouter addRoute(Route route) {
        routes.add(route);
        return this;
    }

    public Optional<HttpHandler> resolve(HttpRequest httpRequest) {
        return routes.stream().filter(route -> {
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
