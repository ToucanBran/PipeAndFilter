package services;

import filters.Filter;
import pipes.Pipe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PipeAndFilter {

    ExecutorService executor;
    Filter[] filters;
    public PipeAndFilter() { }
    public PipeAndFilter(Filter[] filters) {
        this.filters = filters;
    }

    public Pipe startPipeline() {
       return startPipeline(filters);
    }

    public Pipe startPipeline(Filter[] filters) {
        Pipe inputPipe = new Pipe();
        executor = Executors.newFixedThreadPool(10);
        for(Filter filter : filters) {
            Pipe outputPipe = new Pipe();
            filter.setPipes(inputPipe, outputPipe);
            inputPipe = outputPipe;
            executor.execute(filter);
        }
        return filters[0].getInputPipe();
    }

    public void stopPipeline() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    public Pipe getFinalOutput() {
        return filters[filters.length - 1].getOutputPipe();
    }
}
