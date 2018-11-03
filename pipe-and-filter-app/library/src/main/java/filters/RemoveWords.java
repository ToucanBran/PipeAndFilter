package filters;

import pipes.Pipe;

import java.util.List;

public class RemoveWords extends Filter<List<String>, List<String>> {

    private List<String> stopWords;


    public RemoveWords() {
        super();
        this.stopWords = null;
    }

    public RemoveWords(Pipe<List<String>> outputPipe) {
        this(outputPipe, null);
    }
    public RemoveWords(Pipe<List<String>> outputPipe,
                       List<String> stopWords) {
        super(outputPipe);
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
