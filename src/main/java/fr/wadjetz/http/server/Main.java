package fr.wadjetz.http.server;

import fr.wadjetz.http.load.balancer.LoadBalancing;

import java.io.IOException;
import java.net.InetSocketAddress;

class Main {
    public static void main(String[] args) {
        try {
            HttpConfig httpConfig = new HttpConfig("conf.properties").load();
            int port = Integer.parseInt(httpConfig.getString("port").orElse("8888"));
            String host = httpConfig.getString("host").orElse("0.0.0.0");

            LoadBalancing loadBalancing = new LoadBalancing(httpConfig.getGroups());
            HttpVHost httpVHostFromConfig = new HttpVHost().loadConfig(httpConfig);

            HttpRouter httpRouter = new HttpRouter().addRoute(new Route("ALL", ".*", loadBalancing));
            HttpVHost httpVHost = new HttpVHost().addVHost("site2.fr:" + port, httpRouter);

            System.out.println("Start Server " + host + ":" + port);

            new HttpServer().run(new InetSocketAddress(host, port), httpVHost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
