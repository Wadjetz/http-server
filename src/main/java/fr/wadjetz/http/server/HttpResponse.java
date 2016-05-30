package fr.wadjetz.http.server;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String status;
    private String statusText;
    private String body;
    private Map<String, String> headers;

    public HttpResponse(String status, String statusText, String body) {
        this.status = status;
        this.statusText = statusText;
        this.body = body;
        this.headers = new HashMap<>();
        headers.put("Date", new Date().toString());
        headers.put("Server", "My Http Server");
        headers.put("Content-Type", "text/html");
        headers.put("Connection", "Closed");
    }

    public void setCookie(String name, String value) {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
