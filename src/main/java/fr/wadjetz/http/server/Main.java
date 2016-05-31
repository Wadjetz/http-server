package fr.wadjetz.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;

class Main {
    public static void main(String[] args) {
        System.out.println("Start Server localhost:8888");

        int port = 8888;

        HttpRouter router = new HttpRouter();
        router.addRoute("/", (request, response) -> {
            System.out.println(request);
            //return response.withFile(new File("/tmp/test.txt"));
            //return response.withStatus(200).withBody(new String(request.getBody()));
            return response.text(new String(request.getBody()));
        });
        //router.addRoute("/test", (request, response) -> response.text(new String(request.getBody())));

        //router.addRoute("/assets", new HttpStaticFileHandler("/tmp"));


        try {
            HttpServer.run(new InetSocketAddress("127.0.0.1", port), router);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
