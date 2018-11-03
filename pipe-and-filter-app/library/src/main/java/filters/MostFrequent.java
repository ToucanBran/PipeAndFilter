package filters;

import pipes.Pipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MostFrequent extends Filter<Map<String, Integer>, List<String>> {

    private static final int DEFAULT_LIMIT = 10;
    private int limit = DEFAULT_LIMIT;

    public MostFrequent() {
        super();
    }
    public MostFrequent(int limit) {
        this.limit = limit;
    }
    public MostFrequent(Pipe<List<String>> outputPipe) {
        super(outputPipe);

    }

    public MostFrequent(Pipe<List<String>> outputPipe, int limit) {
        super(outputPipe);
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
        List<String> ordered = input.entrySet().stream().
                sorted(Map.Entry.<String, Integer>comparingByValue()
                .reversed()
                .thenComparing(Map.Entry.comparingByKey()))
                .map((e) -> e.getKey())
                .collect(Collectors.toList());

        output = ordered.stream().limit(limit).collect(Collectors.toList());

        // Handles cases when we have ties that go beyond the limit. Continues to
        // add words until tie is over or we add the rest of the items in the ordered
        // list
        if (limit < input.size())
        {
            int lastWordFreq = input.get(ordered.get(limit - 1));
            int i = limit;
            while (i < ordered.size() && input.get(ordered.get(i)) == lastWordFreq)
            {
                output.add(ordered.get(i));
                i++;
            }
        }

        return output;
    }
}
