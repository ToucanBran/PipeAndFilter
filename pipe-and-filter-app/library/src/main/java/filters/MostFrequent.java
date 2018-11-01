package filters;

import pipes.Pipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MostFrequent extends Filter<Map<String, Integer>, List<String>> {

    private static final int DEFAULT_LIMIT = 10;
    private int limit = DEFAULT_LIMIT;

    public MostFrequent(Pipe<Map<String, Integer>> inputPipe,
                        Pipe<List<String>> outputPipe) {
        super(inputPipe, outputPipe);

    }

    public MostFrequent(Pipe<Map<String, Integer>> inputPipe,
                        Pipe<List<String>> outputPipe,
                        int limit) {
        super(inputPipe, outputPipe);
        if (limit > 0) {
            this.limit = limit;
        } else {
            throw new IllegalArgumentException("Limit must be greater than zero.");
        }
    }

    // Expects a map of objects keys to frequency count values
    @Override
    public List<String> process(Map<String, Integer> input) {
        List<String> output = new ArrayList<>();
        if (input == null || input.size() == 0) {
            return output;
        }
        output = input.entrySet().stream().
                sorted(Map.Entry.<String, Integer>comparingByValue()
                .reversed()
                .thenComparing(Map.Entry.comparingByKey()))
                .map((e) -> e.getKey())
                .limit(limit)
                .collect(Collectors.toList());
        return output;
    }
}
