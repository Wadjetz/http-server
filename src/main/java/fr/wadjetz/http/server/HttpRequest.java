package fr.wadjetz.http.server;

import java.util.Arrays;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String relativePath;
    private String absolutePath;
    private String version;
    private Map<String, String> headers;
    private byte[] body;

    public void setMethod(String method) {
        this.method = method;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", relativePath='" + relativePath + '\'' +
                ", absolutePath='" + absolutePath + '\'' +
                ", version='" + version + '\'' +
                //", headers=" + headers +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
