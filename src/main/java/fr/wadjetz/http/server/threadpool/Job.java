package fr.wadjetz.http.server.threadpool;

@FunctionalInterface
public interface Job {
    void apply();
}
