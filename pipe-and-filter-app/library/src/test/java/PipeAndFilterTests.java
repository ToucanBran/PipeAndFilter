import com.google.common.collect.Lists;
import common.PipeInput;
import common.Stemmer;
import common.enums.Pills;
import filters.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pipes.Pipe;
import services.PipeAndFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.mockito.Mockito.mock;

public class PipeAndFilterTests {

    List<String> stopWords = new ArrayList<>();
    final static Logger logger = LogManager.getLogger(PipeAndFilterTests.class);

    @Before
    public void setUp() {
        if (stopWords.size() == 0) {
            File stopWordsFile = new File(getClass().getClassLoader()
                    .getResource("stopWords.txt")
                    .getFile());
            try {
                Scanner scanner = new Scanner(stopWordsFile);
                while (scanner.hasNext()) {
                    stopWords.add(scanner.next());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void pipeAndFilter_fullPipleline_expectedExtendsBeyondDefinedLimit() throws InterruptedException {
        final int WORD_LIMIT = 3;
        List<String> words = new ArrayList<String>() {
            {
                add("This");
                add("test's");
                add("2");
                add("test's");
                add("test's");
                add("case");
                add("case");
                add("case");
                add("case");
                add("case");
                add("swim");
                add("swims");
                add("jump");
                add("jum'ped");
                add("jumpi'ng");
                add("jumps");
                add("swimming");
            }
        };

        for (int i = 0; i < 10; i++) {
            String stopWord = "a";
            words.add(stopWord);
        }
        List<String> expectedOutput = new ArrayList<String>() {
            {
                add("swim");
                add("case");
                add("jump");
                add("test");
            }
        };

        Filter[] filters = new Filter[]{
                new RemoveWords(null, stopWords),
                new RemoveNonAlphabetical(),
                new Stem(),
                new Frequency(),
                new MostFrequent(1, WORD_LIMIT)};

        PipeAndFilter paf = new PipeAndFilter(filters);

        Pipe inputPipe = paf.startPipeline();
        inputPipe.write(words);
        Thread.sleep(1000);
        paf.stopPipeline();
        List<String> output = (List<String>) filters[4].getOutputPipe().read().getInput();
        int outputSize = output.size();
        output.removeAll(expectedOutput);

        Assert.assertTrue(output.isEmpty() && outputSize > WORD_LIMIT);
    }

    @Test
    public void pipeAndFilter_fullPipleline_expectedWitihinDefinedLimit() throws InterruptedException {
        final int WORD_LIMIT = 3;
        List<String> words = new ArrayList<String>() {
            {
                add("This");
                add("test's");
                add("2");
                add("test's");
                add("case");
                add("case");
                add("case");
                add("case");
                add("case");
                add("swim");
                add("swims");
                add("jump");
                add("jum'ped");
                add("jumpi'ng");
                add("jumps");
                add("swimming");
            }
        };

        for (int i = 0; i < 10; i++) {
            String stopWord = "a";
            words.add(stopWord);
        }
        List<String> expectedOutput = new ArrayList<String>() {
            {
                add("swim");
                add("case");
                add("jump");
            }
        };

        Filter[] filters = new Filter[]{
                new RemoveWords(null, stopWords),
                new RemoveNonAlphabetical(),
                new Stem(),
                new Frequency(),
                new MostFrequent(1, WORD_LIMIT)};

        PipeAndFilter paf = new PipeAndFilter(filters);

        Pipe inputPipe = paf.startPipeline();
        inputPipe.write(words);
        Thread.sleep(1000);
        paf.stopPipeline();
        List<String> output = (List<String>) filters[4].getOutputPipe().read().getInput();
        int outputSize = output.size();
        output.removeAll(expectedOutput);

        Assert.assertTrue(output.isEmpty() && outputSize == WORD_LIMIT);
    }

    @Test
    public void pipeAndFilter_poisonPill_end() throws InterruptedException {
        final int WORD_LIMIT = 3;
        List<String> words = new ArrayList<String>() {
            {
                add("This");
                add("test's");
                add("2");
                add("test's");
                add("case");
                add("case");
                add("case");
                add("case");
                add("case");
                add("swim");
                add("swims");
                add("jump");
                add("jum'ped");
                add("jumpi'ng");
                add("jumps");
                add("swimming");
            }
        };

        for (int i = 0; i < 10; i++) {
            String stopWord = "a";
            words.add(stopWord);
        }
        List<String> expectedOutput = new ArrayList<String>() {
            {
                add("swim");
                add("case");
                add("jump");
            }
        };

        Filter[] filters = new Filter[]{
                new RemoveWords(null, stopWords),
                new RemoveNonAlphabetical(),
                new Stem(),
                new Frequency(),
                new MostFrequent(1, WORD_LIMIT)};

        PipeAndFilter paf = new PipeAndFilter(filters);

        Pipe inputPipe = paf.startPipeline();
        inputPipe.write(words);
        inputPipe.write(Pills.POISON);
        Thread.sleep(1000);
        List<String> output = (List<String>) filters[4].getOutputPipe().read().getInput();
        int outputSize = output.size();
        output.removeAll(expectedOutput);

        Assert.assertTrue(output.isEmpty() && outputSize == WORD_LIMIT);
    }

    @Test
    public void pipeAndFilter_stringAggregator_success() throws InterruptedException {
        List<String> words = new ArrayList<String>() {
            {
                add("This");
                add("test's");
                add("2");
                add("test's");
                add("case");
                add("case");
                add("case");
                add("case");

            }
        };

        List<String> words2 = new ArrayList<String>() {
            {
                add("case");
                add("swim");
                add("swims");
                add("jump");
                add("jum'ped");
                add("jumpi'ng");
                add("jumps");
                add("swimming");
            }
        };


        Filter[] filters = new Filter[]{
                new RemoveWords(null, stopWords),
                new StringAggregator(2)
        };

        PipeAndFilter paf = new PipeAndFilter(filters);

        Pipe inputPipe = paf.startPipeline();
        inputPipe.write(words);
        inputPipe.write(words2);
        PipeInput outputPipe = filters[1].getOutputPipe().read();
        while (outputPipe == null || outputPipe.getInput() == null) {
            outputPipe = filters[1].getOutputPipe().read();
        }
        paf.stopPipeline();
        List<String> output = (List<String>) outputPipe.getInput();
        Assert.assertTrue(output.size() == (words.size() + words2.size()));
    }

    @Test
    public void DELETE() throws InterruptedException {

        List<String> kjvWords = getWords(PipeAndFilterTests.class.getResourceAsStream("/kjbible.txt"));
        List<List<String>> splitWords = Lists.partition(kjvWords, kjvWords.size() / 20);
        long start = System.currentTimeMillis();
        Filter[] filters = new Filter[]{
                new RemoveWords(splitWords.size(), stopWords),
                new StringAggregator(splitWords.size()),
                new RemoveNonAlphabetical(),
                new Stem(),
                new Frequency(),
                new MostFrequent()};

        PipeAndFilter paf = new PipeAndFilter(filters);
        Pipe inputPipe = paf.startPipeline();
        splitWords.forEach(list -> {
            inputPipe.write(list);
        });
        inputPipe.write(Pills.POISON);
        PipeInput outputPipe = paf.getFinalOutput().read();
        while (outputPipe == null || outputPipe.getInput() == null) {
            outputPipe = paf.getFinalOutput().read();
        }
        paf.stopPipeline();
        long finish = System.currentTimeMillis();
        ArrayList<String> expected = new ArrayList<String>() {
            {
                add("lord");
                add("god");
                add("thy");
                add("ye");
                add("will");
                add("thee");
                add("him");
                add("son");
                add("them");
                add("king");
            }
        };
        logger.info(String.format("Full pipeline took %d millis", finish - start));
        List<String> output = (ArrayList<String>)outputPipe.getInput();
        int outputSize = output.size();
        output.removeAll(expected);
        Assert.assertTrue(output.isEmpty() && outputSize == expected.size());
    }

    // Given a file, all strings are added to a list and returned to the caller
    private static List<String> getWords(InputStream file) {
        if (file == null) {
            return null;
        }
        List<String> output = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                output.add(scanner.next().toLowerCase());
            }
        } catch (Exception e) {
            System.out.println("Something went wrong when searching for the book. Rerun the program to try again.");
            System.exit(1);
        }
        return output;
    }


}
