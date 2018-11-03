import common.Stemmer;
import filters.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pipes.Pipe;
import services.PipeAndFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.mockito.Mockito.mock;

public class PipeAndFilterTests {

    List<String> stopWords = new ArrayList<>();

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
    public void removeStopWords_normal_removeWordsSuccess() throws InterruptedException {
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
            }
        };

        Filter[] filters = new Filter[]{
                new RemoveWords(null, stopWords),
                new RemoveNonAlphabetical(),
                new Stem(),
                new Frequency(),
                new MostFrequent(3)};

        PipeAndFilter paf = new PipeAndFilter(filters);

        Pipe inputPipe = paf.startPipeline();
        inputPipe.write(words);
        Thread.sleep(1000);
        paf.stopPipeline();
        List<String> output = (List<String>) filters[4].getOutputPipe().read();
        output.removeAll(expectedOutput);

        Assert.assertTrue(output.isEmpty());
    }
}
