package services;

import filters.Filter;
import pipes.Pipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PipeAndFilter {

    private ExecutorService executor;
    private ArrayList<Filter> filters;
    private HashMap<Filter, Integer> filterThreads = new HashMap<>();
    public PipeAndFilter() { }
    public PipeAndFilter(Filter[] filters) {
        this.filters = new ArrayList<>();
        Pipe inputPipe = new Pipe();
        for (Filter filter : filters) {
            inputPipe = registerFilter(filter, inputPipe);
        }
    }

    public Pipe startPipeline() {
        int totalThreads = filterThreads.entrySet()
                .stream()
                .mapToInt(e -> e.getValue())
                .sum();
        executor = Executors.newFixedThreadPool(totalThreads);
        filters.forEach(filter -> {
            for (int i = 0; i < filterThreads.get(filter); i++)
            {
                executor.execute(filter);
            }
        });
        return filters.get(0).getInputPipe();
    }

    public Pipe startPipeline(Filter[] filters) {
        Pipe inputPipe = new Pipe();
        executor = Executors.newFixedThreadPool(10);
        Pipe outputPipe = new Pipe();
        filters[0].setPipes(inputPipe, outputPipe);
        inputPipe = outputPipe;
        executor.execute(filters[0]);
        executor.execute(filters[0]);
        executor.execute(filters[0]);
        for (int i = 1; i < filters.length; i++) {
            outputPipe = new Pipe();
            filters[i].setPipes(inputPipe, outputPipe);
            inputPipe = outputPipe;
             executor.execute(filters[i]);
        }

        return filters[0].getInputPipe();
    }

    public Pipe registerFilter(Filter filter, Pipe inputPipe) {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        filters.add(filter);
        Pipe outputPipe = new Pipe();
        filter.setPipes(inputPipe, outputPipe);
        filterThreads.put(filter, filter.getThreadCount());
        return outputPipe;
    }
    public void stopPipeline() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    public Pipe getFinalOutput() {
        return filters.get(filters.size() - 1).getOutputPipe();
    }
}
