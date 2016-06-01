package fr.wadjetz.http.server.threadpool;

import java.util.*;

public class ThreadPool {
    final Object lock = new Object();

    boolean shouldStop = false;

    PriorityQueue<PrioritizedAction> actions = new PriorityQueue<>();
    List<Worker> workers = new ArrayList<>();

    public ThreadPool(int nb) {
        for(int i = 0; i < nb; i++) {
            Worker w = new Worker(this);
            Thread t = new Thread(w);
            workers.add(w);
            t.start();
        }
    }

    public void submit(int priority, Job action) {
        submit(new PrioritizedAction(priority, action));
    }

    public void submit(Job action) {
        submit(new PrioritizedAction(1, action));
    }

    private void submit(PrioritizedAction action) {
        synchronized (lock) {
            actions.add(action);
            lock.notify();
        }
    }

    public Job consume() {
        synchronized (lock) {
            while(actions.isEmpty() && !shouldStop) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {}
            }

            PrioritizedAction pAction = actions.poll();
            return pAction != null ? pAction.action : null;
        }
    }

    public void stop() {
        synchronized (lock) {
            shouldStop = true;

            lock.notifyAll();
        }
    }

    private class PrioritizedAction implements Comparable {
        private int priority;
        private Job action;

        public PrioritizedAction(int priority, Job action) {
            this.priority = priority;
            this.action = action;
        }

        @Override
        public int compareTo(Object o) {
            if(o instanceof PrioritizedAction)
                return priority - ((PrioritizedAction) o).priority;
            else
                return 1;
        }
    }

    private class Worker implements Runnable {
        ThreadPool threadPool;

        public Worker(ThreadPool threadPool) {
            this.threadPool = threadPool;
        }

        @Override
        public void run() {
            while (!threadPool.shouldStop) {
                Job action = threadPool.consume();
                if (action != null) {
                    action.apply();
                }
            }
        }
    }
}
