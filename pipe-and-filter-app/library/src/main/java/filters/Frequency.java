package filters;

import pipes.Pipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frequency<T> extends Filter<List<T>, Map<T, Integer>> {

    public Frequency() {
        super();
    }
    public Frequency(Pipe<Map<T, Integer>> outputPipe) {
        super(outputPipe);
    }

    @Override
    public Map<T, Integer> process(List<T> input) {
        HashMap<T, Integer> output = new HashMap<>();
        if (input != null) {
            input.forEach(item -> {
                if (output.containsKey(item)) {
                    output.put(item, output.get(item) + 1);
                }
                else {
                    output.put(item, 1);
                }
            });
        }

        return output;
    }

}
