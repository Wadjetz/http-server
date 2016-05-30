package fr.wadjetz.http.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String status;
    private String statusText;
    private String body;
    private Map<String, String> headers;

    public HttpResponse() {
        this.headers = new HashMap<>();
        headers.put("Date", new Date().toString());
        headers.put("Server", "My Http Server");
        headers.put("Content-Type", "text/html");
        headers.put("Connection", "Closed");
    }

    public HttpResponse withStatus(HttpStatus status) {
        switch (status) {
            case Ok:
                this.status = "200";
                this.statusText = "OK";
            case NotFound:
                this.status = "400";
                this.statusText = "Not Found";
        }
        return this;
    }

    public HttpResponse withHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpResponse withBody(String body) {
        this.body = body;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
