package fr.wadjetz.http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class Main {
    public static void main(String[] args) {
        System.out.println("Start Server localhost:8888");
        try {
            HttpServer.run(8888, ((request, response) -> response.withStatus(HttpStatus.Ok).withBody("Hello World")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
