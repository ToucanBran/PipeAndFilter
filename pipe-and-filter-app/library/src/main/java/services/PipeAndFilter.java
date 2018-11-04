package services;

import filters.Filter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import pipes.Pipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PipeAndFilter {

    private ExecutorService executor;
    private ArrayList<Filter> filters;
    private HashMap<Filter, Integer> filterThreads = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(PipeAndFilter.class);
    public PipeAndFilter() { }
    public PipeAndFilter(Filter[] filters) {
        long start = System.currentTimeMillis();
        this.filters = new ArrayList<>();
        Pipe inputPipe = new Pipe();
        for (Filter filter : filters) {
            inputPipe = registerFilter(filter, inputPipe);
        }
        long end = System.currentTimeMillis();
        logger.info(String.format("Took %d to register filters", end-start));
    }

    public Pipe startPipeline() {
        long start = System.currentTimeMillis();
        int totalThreads = filterThreads.entrySet()
                .stream()
                .mapToInt(e -> e.getValue())
                .sum();
        ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
        filters.forEach(filter -> {
            for (int i = 0; i < filterThreads.get(filter); i++)
            {
                executor.execute(filter);
            }
        });
        long end = System.currentTimeMillis();
        logger.info(String.format("Took %d to actually start the pipeline", end-start));
        return filters.get(0).getInputPipe();
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
