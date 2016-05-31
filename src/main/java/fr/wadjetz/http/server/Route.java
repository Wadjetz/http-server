package fr.wadjetz.http.server;

import scala.util.matching.Regex;

public class Route {
    private String method;
    private Regex regex;
    private HttpHandler httpHandler;

    public Route(String method, Regex regex, HttpHandler httpHandler) {
        this.method = method;
        this.regex = regex;
        this.httpHandler = httpHandler;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Regex getRegex() {
        return regex;
    }

    public void setRegex(Regex regex) {
        this.regex = regex;
    }

    public HttpHandler getHttpHandler() {
        return httpHandler;
    }

    public void setHttpHandler(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }
}
