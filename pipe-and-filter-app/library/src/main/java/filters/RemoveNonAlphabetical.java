package filters;

import pipes.Pipe;

import java.util.ArrayList;
import java.util.List;

public class RemoveNonAlphabetical extends Filter<List<String>, List<String>> {

    public RemoveNonAlphabetical() {
        super();
    }
    public RemoveNonAlphabetical(Pipe<List<String>> outputPipe) {
        super(outputPipe);
    }
    @Override
    public List<String> process(List<String> input) {
        List<String> processedList = new ArrayList<>();
        if (input != null) {
            input.forEach((word) -> {
                String strippedWord = word.replaceAll("[^a-zA-Z]", "").trim();
                if (!strippedWord.isEmpty()) {
                    processedList.add(strippedWord);
                }
            });
        }
        return processedList;
    }
}
