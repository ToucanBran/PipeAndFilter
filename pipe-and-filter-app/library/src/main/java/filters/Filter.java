package filters;

import pipes.Pipe;

import java.util.List;

public abstract class Filter<T, K> implements Runnable{

    private Pipe<T> outputPipe;
    private Pipe<K> inputPipe;
    public Filter(Pipe<K> inputPipe, Pipe<T> outputPipe) {
        this.outputPipe = outputPipe;
        this.inputPipe = inputPipe;
    }

    public abstract T process(K input);

    public Pipe<K> getInputPipe() {
        return inputPipe;
    }

    public Pipe<T> getOutputPipe() {
        return outputPipe;
    }

    @Override
    public void run() {
        while (true) {
            K input = inputPipe.read();
            if (input != null) {
                T transformedInput = process(input);
                outputPipe.write(transformedInput);
            }
        }
    }
}
