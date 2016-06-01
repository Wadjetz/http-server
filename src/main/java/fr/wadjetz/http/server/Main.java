package fr.wadjetz.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

class Main {
    public static void main(String[] args) {
        try {
            HttpConfig httpConfig = new HttpConfig("conf.properties").load();
            int port = Integer.parseInt(httpConfig.getString("port").orElse("8888"));
            String host = httpConfig.getString("host").orElse("0.0.0.0");

            LoadBalancer loadBalancer = new LoadBalancer(httpConfig.getGroups());
            HttpVHost httpVHostFromConfig = new HttpVHost().loadConfig(httpConfig);

            HttpRouter httpRouter = new HttpRouter().addRoute(new Route("ALL", Pattern.compile(".*"), loadBalancer));
            HttpVHost httpVHost = new HttpVHost().addVHost("site2.fr:" + port, httpRouter);

            System.out.println("Start Server " + host + ":" + port);

            new HttpServer().run(new InetSocketAddress(host, port), httpVHost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
