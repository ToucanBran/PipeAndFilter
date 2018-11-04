package pipes;

import common.PipeInput;
import common.enums.Pills;

import java.util.concurrent.LinkedBlockingQueue;

public class Pipe<T> {

    private LinkedBlockingQueue<PipeInput<T>> queue = new LinkedBlockingQueue<>();

    public void write(T input) {

        queue.add(new PipeInput<>(input));
    }
    public void write(Pills pill) {

        queue.add(new PipeInput<>(pill));
    }
    public PipeInput<T> read() {
        return queue.poll();
    }
}
