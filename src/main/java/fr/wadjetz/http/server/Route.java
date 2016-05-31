package fr.wadjetz.http.server;

import scala.util.matching.Regex;

import java.util.regex.Pattern;

public class Route {
    private String method;
    private Pattern pattern;
    private HttpHandler httpHandler;

    public Route(String method, Pattern pattern, HttpHandler httpHandler) {
        this.method = method;
        this.pattern = pattern;
        this.httpHandler = httpHandler;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HttpHandler getHttpHandler() {
        return httpHandler;
    }

    public void setHttpHandler(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
