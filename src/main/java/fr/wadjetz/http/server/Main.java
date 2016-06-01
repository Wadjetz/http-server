package fr.wadjetz.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;

class Main {
    public static void main(String[] args) {
        System.out.println("Start Server localhost:8888");
        int port = 8888;

        HttpVHost httpVHost = new HttpVHost().loadConfig();

        try {
            new HttpServer().run(new InetSocketAddress("127.0.0.1", port), httpVHost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
