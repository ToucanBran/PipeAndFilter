package filters;

import pipes.Pipe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Filter<T, K> implements Runnable{

    private Pipe<T> inputPipe;
    private Pipe<K> outputPipe;
    private ExecutorService executorService;
    boolean running = false;
    public Filter() {
        this(new Pipe<K>());
    }
    public Filter(Pipe<K> outputPipe) {
        this.outputPipe = outputPipe;
    }

    public void processAndWrite(T input) {
        K transformedInput = process(input);
        outputPipe.write(transformedInput);
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

    public void setPipes(Pipe<T> inputPipe, Pipe<K> outputPipe) {
        this.inputPipe = inputPipe;
        this.outputPipe = outputPipe;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            T input = inputPipe.read();
            if (input != null) {
                processAndWrite(input);
            }
        }
    }

    // This method is used to quickly pipe this filter to another.
    // Because it returns the next filter, you can call pipeTo again
    // and just keep connecting filters. What I'm not sure about is how to
    // shutdown all the executorServices or if this is even efficient.
    // TODO: possibly utilize this in PipeAndFilter service
    public <B> Filter pipeTo(Filter<K, B> nextFilter) {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        nextFilter.setInputPipe(outputPipe);
        executorService.execute(nextFilter);
        return nextFilter;
    }

    public void stop() {
        running = false;
        executorService.shutdown();
    }
}
