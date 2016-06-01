package fr.wadjetz.http.server;

import java.util.regex.Pattern;

public class Route {
    private String method;
    private Pattern pattern;
    private HttpHandler httpHandler;

    public Route(String method, String pattern, HttpHandler httpHandler) {
        this.method = method;
        this.pattern = Pattern.compile(pattern);
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

    @Override
    public String toString() {
        return "Route{" +
                "method='" + method + '\'' +
                ", pattern=" + pattern +
                ", httpHandler=" + httpHandler +
                '}';
    }
}
