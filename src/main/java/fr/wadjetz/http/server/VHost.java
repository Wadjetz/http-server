package fr.wadjetz.http.server;

public class VHost {
    public String domain;
    public String root;

    public VHost(String domain, String root) {
        this.domain = domain;
        this.root = root;
    }
}
