package fr.wadjetz.http.server;

import java.util.List;

public class LoadBalancer implements HttpHandler {

    private List<Group> groups;

    public LoadBalancer(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public HttpResponse apply(HttpRequest request, HttpResponse response) {
        return response.withStatus(200).withBody("LoadBalancer");
    }
}
