package filters;

import common.Stemmer;
import opennlp.tools.stemmer.PorterStemmer;
import pipes.Pipe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Stem extends Filter<List<String>, List<String>> {

    private PorterStemmer stemmer;

    public Stem() {
        super();
     //   stemmer = new PorterStemmer();
    }

    public Stem(int threadCount) {
        super(threadCount);
   //     stemmer = new PorterStemmer();
    }

    public Stem(Pipe<List<String>> outputPipe) {
        super(outputPipe);
   //     stemmer = new PorterStemmer();
    }

    @Override
    public List<String> process(List<String> input) {
        List<String> output = new ArrayList<>();
        if (input == null) {
            return null;
        }
        output = input.stream().map(word -> {
//            stemmer.add(word.toCharArray(), word.length());
//            stemmer.stem();
            //return new String(stemmer.getResultBuffer(), 0, stemmer.getResultLength());

                String s = new PorterStemmer().stem(word);

            return s;
        }).collect(Collectors.toList());
        return output;
    }

    @Override
    public boolean processPoison() {
        return false;
    }


    @Override
    public String toString() {
        return "Stem";
    }
}
