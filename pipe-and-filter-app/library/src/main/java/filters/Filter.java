package filters;

import pipes.Pipe;

import java.util.List;

public abstract class Filter<T, K> implements Runnable{

    private Pipe<T> inputPipe;
    private Pipe<K> outputPipe;
    public Filter(Pipe<T> inputPipe, Pipe<K> outputPipe) {
        this.outputPipe = outputPipe;
        this.inputPipe = inputPipe;
    }

    public abstract K process(T input);

    public Pipe<T> getInputPipe() {
        return inputPipe;
    }

    public Pipe<K> getOutputPipe() {
        return outputPipe;
    }

    @Override
    public void run() {
        while (true) {
            T input = inputPipe.read();
            if (input != null) {
                K transformedInput = process(input);
                outputPipe.write(transformedInput);
            }
        }
    }
}
