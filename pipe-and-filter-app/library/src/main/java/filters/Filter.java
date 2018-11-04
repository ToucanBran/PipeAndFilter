package filters;

import common.PipeInput;
import common.enums.Pills;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pipes.Pipe;


public abstract class Filter<T,K> implements Runnable{

    private Pipe<PipeInput<T>> inputPipe;
    private Pipe<K> outputPipe;
    private int threadCount = 1;
    boolean running = false;
    final static Logger logger = LogManager.getLogger(Filter.class);
    public Filter() {
        this(new Pipe<K>());
    }
    public Filter(int threadCount) {
        this();
        this.threadCount = threadCount;
    }
    public Filter(Pipe<K> outputPipe) {
        this.outputPipe = outputPipe;
    }

    public void processAndWrite(PipeInput<T> input) {
        K transformedInput = process(input.getInput());
        outputPipe.write(transformedInput, input.isPoisoned());
    }
    public abstract K process(T input);

    public abstract boolean processPoison();
    public Pipe<PipeInput<T>> getInputPipe() {
        return inputPipe;
    }

    public Pipe<K> getOutputPipe() {
        return outputPipe;
    }

    public void setInputPipe(Pipe<PipeInput<T>> pipe) {
        this.inputPipe = pipe;
    }
    public void setOutputPipe(Pipe<K> pipe) {
        this.outputPipe = pipe;
    }

    public void setPipes(Pipe<PipeInput<T>> inputPipe, Pipe<K> outputPipe) {
        this.inputPipe = inputPipe;
        this.outputPipe = outputPipe;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            PipeInput input = inputPipe.read();
            if (input != null) {
                if (!input.isPoisoned()){
                    long start = System.currentTimeMillis();
                    processAndWrite(input);
                    long finish = System.currentTimeMillis();
                    logger.info(String.format("Filter took %d milliseconds", finish - start));
                }
                else {
                    running = processPoison();
                    if (!running) {
                        processAndWrite(input);
                        inputPipe.write(Pills.POISON);
                        outputPipe.write(Pills.POISON);
                    }
                }
            }
        }
        logger.info(String.format("%s no longer running", this.toString()));
    }


    public void stop() {
        running = false;
    }

    public void setThreads(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
