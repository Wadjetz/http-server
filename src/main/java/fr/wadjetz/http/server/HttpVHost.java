package fr.wadjetz.http.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpVHost {

    Map<String, HttpRouter> vhosts = new HashMap<>();

    public HttpVHost addVHost(String host, HttpRouter router) {
        vhosts.put(host, router);
        return this;
    }

    public Optional<HttpHandler> resolve(HttpRequest request) {
        String host = request.getHeaders().get("host");

        System.out.println(host);

        if (host != null) {
            HttpRouter httpRouter = vhosts.get(host);

            System.out.println("vhosts=" + vhosts);

            return httpRouter.resolve(request);
        } else {
            return Optional.empty();
        }
    }

    public void loadConfig() {
        HttpStaticFileHandler httpStaticFileHandler = new HttpStaticFileHandler("/tmp");
        HttpConfig httpConfig = new HttpConfig("config").load();

        int port = Integer.parseInt(httpConfig.getString("port").orElse("8888"));
        String host = httpConfig.getString("host").orElse("0.0.0.0");
        List<VHost> vhosts = httpConfig.getVHosts();

        for (VHost vhost : vhosts) {

        }
    }

    @Override
    public String toString() {
        return "HttpVHost{" +
                "vhosts=" + vhosts +
                '}';
    }
}
