package fr.wadjetz.http.server;

import fr.wadjetz.http.threadpool.HttpParser;
import fr.wadjetz.http.threadpool.ThreadPool;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Optional;

public class HttpServer {

    private ThreadPool threadPool;

    @FunctionalInterface
    interface Resolver {
        Optional<HttpHandler> apply(HttpRequest request);
    }

    public HttpServer() {
        this.threadPool = new ThreadPool(8);
    }

    public void run(final InetSocketAddress address, HttpRouter httpRouter) throws IOException {
        serverLoop(address, httpRouter::resolve);
    }

    public void run(final InetSocketAddress address, HttpVHost httpVHost) throws IOException {
        serverLoop(address, httpVHost::resolve);
    }

    private void serverLoop(final InetSocketAddress address, Resolver resolver) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(address);

        while (true) {
            final Socket socket = serverSocket.accept();

            this.threadPool.submit(() -> {
                PrintWriter printWriter = null;
                BufferedReader bufferedReader = null;
                HttpRequest httpRequest = null;
                HttpResponse httpResponse = null;

                try {
                    printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    try {
                        httpRequest = parseRequest(bufferedReader);
                        httpRequest.setPort(address.getPort());
                        Optional<HttpHandler> httpHandlerOptional = resolver.apply(httpRequest);

                        if (httpHandlerOptional.isPresent()) {
                            httpResponse = httpHandlerOptional.get().apply(httpRequest, new HttpResponse());

                            System.out.println(httpResponse);
                        } else {
                            httpResponse = new HttpResponse().withStatus(404);
                        }

                        if (httpResponse.getBody().isPresent()) {
                            String responseHeader = HttpParser.buildResponseHeader(httpRequest, httpResponse.withHeader("Content-Length", httpResponse.getBody().get().length() + ""));
                            printWriter.print(responseHeader);
                            printWriter.print(httpResponse.getBody().get() + "\r\n");
                        } else if (httpResponse.getFile().isPresent()) {
                            String responseHeader = HttpParser.buildResponseHeader(httpRequest, httpResponse.withHeader("Content-Type", URLConnection.guessContentTypeFromName(httpResponse.getFile().get().getName())));
                            printWriter.print(responseHeader);
                            IOUtils.copyLarge(new FileReader(httpResponse.getFile().get()),printWriter);
                        } else {
                            String responseHeader = HttpParser.buildResponseHeader(httpRequest, httpResponse);
                            printWriter.print(responseHeader);
                        }

                    } catch (HttpException e) {
                        e.printStackTrace();
                        String responseHeader = HttpParser.buildResponseHeader(httpRequest,  new HttpResponse().withStatus(e.getErrorCode()).withStatusText(e.getMessage()));
                        printWriter.print(responseHeader);
                    }

                    printWriter.flush();
                    bufferedReader.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (printWriter != null) printWriter.close();
                }
            });

            if (false) break;
        }

        serverSocket.close();
    }

    private HttpRequest parseRequest(BufferedReader bufferedReader) throws IOException, HttpException {
        // Parse first line
        String firstLine = bufferedReader.readLine();
        HttpRequest httpRequest = HttpParser.parseRequestLine(firstLine);
        Map<String, String> headers = HttpParser.parseHeaders(bufferedReader);
        byte[] body = HttpParser.parseBody(headers, bufferedReader);
        httpRequest.setBody(body);
        httpRequest.setHeaders(headers);
        return httpRequest;
    }

}
