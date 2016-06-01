package fr.wadjetz.http.threadpool;

@FunctionalInterface
public interface Job {
    void apply();
}
