package fr.wadjetz.http.server;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpResponse {
    private String status = "200";
    private String statusText = "Ok";
    private Optional<String> body = Optional.empty();
    private Optional<File> file = Optional.empty();
    private Map<String, String> headers;

    public HttpResponse() {
        this.headers = new HashMap<>();
        headers.put("Date", new Date().toString());
        headers.put("Server", "My Http Server");
        headers.put("Content-Type", "text/html");
        headers.put("Connection", "Closed");
    }

    public HttpResponse withStatus(int status) {
        switch (status) {
            case 200:
                this.statusText = "OK";
                break;
            case 404:
                this.statusText = "Not Found";
                break;
        }
        this.status = status + "";
        return this;
    }

    public HttpResponse withStatusText(String statusText) {
        this.statusText = statusText;
        return this;
    }

    public HttpResponse withHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpResponse withBody(String body) {
        this.body = Optional.ofNullable(body);
        return this;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusText() {
        return statusText;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpResponse setStatus(String status) {
        this.status = status;
        return this;
    }

    public HttpResponse withFile(File file) {
        this.file = Optional.ofNullable(file);
        return this;
    }

    public Optional<String> getBody() {
        return body;
    }

    public Optional<File> getFile() {
        return file;
    }
}
