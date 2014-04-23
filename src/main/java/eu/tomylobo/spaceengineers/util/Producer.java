package eu.tomylobo.spaceengineers.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class Producer<T> extends Thread {
    @Override
    public abstract void run();

    private final BlockingQueue<T> queue = new ArrayBlockingQueue<>(1024);

    protected void produce(T what) throws InterruptedException {
        queue.put(what);
    }

    public T consume() throws InterruptedException {
        return queue.poll();
    }
}
