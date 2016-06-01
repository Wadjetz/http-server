package fr.wadjetz.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

class Main {
    public static void main(String[] args) {
        System.out.println("Start Server localhost:8888");
        try {
            int port = 8888;
            HttpConfig httpConfig = new HttpConfig("conf.properties").load();

            LoadBalancer loadBalancer = new LoadBalancer(httpConfig.getGroups());
            //HttpVHost httpVHost = new HttpVHost().loadConfig(httpConfig);

            HttpRouter httpRouter = new HttpRouter().addRoute(new Route("ALL", Pattern.compile(".*"), loadBalancer));
            HttpVHost httpVHost = new HttpVHost().addVHost("site2.fr:" + port, httpRouter);

            new HttpServer().run(new InetSocketAddress("127.0.0.1", port), httpVHost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
