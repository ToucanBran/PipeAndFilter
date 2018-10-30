package filters;

import pipes.Pipe;

import java.util.List;

public class RemoveWords extends Filter<List<String>, List<String>> {

    private List<String> stopWords;

    public RemoveWords(Pipe<List<String>> inputPipe,
                       Pipe<List<String>> outputPipe) {
        super(inputPipe, outputPipe);
    }
    public RemoveWords(Pipe<List<String>> inputPipe,
                       Pipe<List<String>> outputPipe,
                       List<String> stopWords) {
        super(inputPipe, outputPipe);
        this.stopWords = stopWords;
    }

    @Override
    public List<String> process(List<String> input) {
        if (stopWords == null || stopWords.isEmpty()){
            return input;
        }
        else {
            input.removeAll(stopWords);
        }
        return input;
    }
}
