package fr.wadjetz.http.server;

import java.io.IOException;

class Main {
    public static void main(String[] args) {
        System.out.println("Start Server localhost:8888");
        try {
            HttpServer.run(8888, ((request, response) -> {
                System.out.println(request);
                return response.withStatus(200).withBody(new String(request.getBody()));
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
