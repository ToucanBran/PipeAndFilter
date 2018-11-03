package filters;

import pipes.Pipe;

public abstract class Filter<T, K> implements Runnable{

    private Pipe<T> inputPipe;
    private Pipe<K> outputPipe;
    public Filter() {
        this(new Pipe<K>());
    }
    public Filter(Pipe<K> outputPipe) {
        this.outputPipe = outputPipe;
    }

    public abstract K process(T input);

    public Pipe<T> getInputPipe() {
        return inputPipe;
    }

    public Pipe<K> getOutputPipe() {
        return outputPipe;
    }

    public void setInputPipe(Pipe<T> pipe) {
        this.inputPipe = pipe;
    }
    public void setOutputPipe(Pipe<K> pipe) {
        this.outputPipe = pipe;
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

    public <B> Filter pipeTo(Filter<K, B> nextFilter) {
        nextFilter.setInputPipe(outputPipe);
        return nextFilter;
    }
}
