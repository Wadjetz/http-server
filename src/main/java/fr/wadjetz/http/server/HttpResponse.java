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

    public HttpResponse withStatus(int status) {
        switch (status) {
            case 200:
                this.statusText = "OK";
            case 404:
                this.statusText = "Not Found";
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

    public HttpResponse setStatus(String status) {
        this.status = status;
        return this;
    }
}
