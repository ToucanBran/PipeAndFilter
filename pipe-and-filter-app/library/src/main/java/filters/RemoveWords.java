package filters;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pipes.Pipe;

import java.util.ArrayList;
import java.util.List;

public class RemoveWords extends Filter<List<String>, List<String>> {

    private List<String> stopWords;
    final static Logger logger = LogManager.getLogger(RemoveWords.class);

    public RemoveWords(int threadCount, List<String> stopWords) {
        super(threadCount);
        this.stopWords = stopWords;
    }
    public RemoveWords(List<String> stopWords) {
        super();
        this.stopWords = stopWords;
    }
    public RemoveWords() {
        super();
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
        if (input == null) {
            return null;
        }
        if (stopWords == null || stopWords.isEmpty()) {
            return input;
        }
        ArrayList<String> newArrayList = Lists.newArrayList(input);
        newArrayList.removeAll(stopWords);

        return newArrayList;
    }

    @Override
    public boolean processPoison() {
        return false;
    }


    @Override
    public String toString() {
        return "RW";
    }
}
