package filters;

import pipes.Pipe;

import java.util.ArrayList;
import java.util.List;

public class RemoveNonAlphabetical extends Filter<List<String>, List<String>> {

    public RemoveNonAlphabetical() {
        super();
    }
    public RemoveNonAlphabetical(int threadCount) {
        super(threadCount);
    }
    public RemoveNonAlphabetical(Pipe<List<String>> outputPipe) {

        super(outputPipe);
    }

    @Override
    public List<String> process(List<String> input) {
        if (input == null) {
            return null;
        }
        List<String> processedList = new ArrayList<>();
        input.forEach((word) -> {
            String strippedWord = word.replaceAll("[^a-zA-Z]", "").trim();
            if (!strippedWord.isEmpty()) {
                processedList.add(strippedWord);
            }
        });

        return processedList;
    }

    @Override
    public boolean processPoison() {
        return false;
    }


    @Override
    public String toString() {
        return "RNA";
    }
}
