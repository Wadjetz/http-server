package fr.wadjetz.http.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class HttpVHost {

    Map<String, HttpRouter> vhosts = new HashMap<>();

    public HttpVHost addVHost(String host, HttpRouter router) {
        vhosts.put(host, router);
        return this;
    }

    public Optional<HttpHandler> resolve(HttpRequest request) {
        String host = request.getHeaders().get("host");
        if (host != null) {
            HttpRouter httpRouter = vhosts.get(host);
            return httpRouter.resolve(request);
        } else {
            return Optional.empty();
        }
    }

    public HttpVHost loadConfig(HttpConfig httpConfig) throws IOException {
        int port = Integer.parseInt(httpConfig.getString("port").orElse("8888"));
        String host = httpConfig.getString("host").orElse("0.0.0.0");
        List<VHost> vhosts = httpConfig.getVHosts();

        for (VHost vhost : vhosts) {
            this.vhosts.put(vhost.domain + ":" + port, new HttpRouter().addRoute(new Route("GET", Pattern.compile("/.*"), new HttpStaticFileHandler(vhost.root))));
        }

        return this;
    }

    @Override
    public String toString() {
        return "HttpVHost{" +
                "vhosts=" + vhosts +
                '}';
    }
}
