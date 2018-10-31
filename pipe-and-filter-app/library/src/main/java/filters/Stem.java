package filters;

import common.Stemmer;
import pipes.Pipe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Stem extends Filter<List<String>, List<String>> {

    private Stemmer stemmer;
    public Stem(Pipe<List<String>> inputPipe,
                Pipe<List<String>> outputPipe) {
        super(inputPipe, outputPipe);
        stemmer = new Stemmer();
    }

    @Override
    public List<String> process(List<String> input) {
        List<String> output = new ArrayList<>();
        if (input != null) {
            output = input.stream().map(word -> {
                stemmer.add(word.toCharArray(), word.length());
                stemmer.stem();
                return stemmer.toString();
            }).collect(Collectors.toList());
        }
        return output;
    }
}
