package filters;

import pipes.Pipe;

import java.util.HashMap;
import java.util.List;

public class Frequency extends Filter<List<Object>, HashMap<Object, Integer>> {

    public Frequency(Pipe<List<Object>> inputPipe,
                     Pipe<HashMap<Object, Integer>> outputPipe) {
        super(inputPipe, outputPipe);
    }

    @Override
    public HashMap<Object, Integer> process(List<Object> input) {
        HashMap<Object, Integer> output = new HashMap<>();
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
