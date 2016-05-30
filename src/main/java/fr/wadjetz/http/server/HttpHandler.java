package fr.wadjetz.http.server;

@FunctionalInterface
public interface HttpHandler {
    HttpResponse apply(HttpRequest request, HttpResponse response);
}
