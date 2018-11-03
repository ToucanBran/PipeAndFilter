package pipes;

import java.util.concurrent.LinkedBlockingQueue;

public class Pipe<T> {

    private LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();

    public void write(T input) {
        queue.add(input);
    }

    public T read() {
        return queue.poll();
    }


}
