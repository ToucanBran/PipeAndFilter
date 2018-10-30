import filters.Filter;
import filters.RemoveWords;
import org.junit.Assert;
import org.junit.Test;
import pipes.Pipe;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class FilterTests {

    @Test
    public void removeStopWords_normal_removeWordsSuccess() {
        List<String> stopWords = new ArrayList<String>() {
            {
                add("hello");
                add("world");
                add("testing");
            }
        };

        List<String> input = new ArrayList<String>() {
            {
                add("hello");
                add("world");
                add("I");
                add("am");
                add("testing");
            }
        };

        List<String> expectedOutput = new ArrayList<String>() {
            {
                add("I");
                add("am");
            }
        };
        Filter<List<String>, List<String>> stopWordsFilter = new RemoveWords(mock(Pipe.class), mock(Pipe.class), stopWords);
        List<String> output = stopWordsFilter.process(input);
        expectedOutput.removeAll(output);
        Assert.assertTrue(expectedOutput.isEmpty());
    }

    @Test
    public void removeStopWords_noStopWords_removeWordsSuccess() {

        List<String> input = new ArrayList<String>() {
            {
                add("hello");
                add("world");
                add("I");
                add("am");
                add("testing");
            }
        };

        List<String> expectedOutput = new ArrayList<String>() {
            {
                add("I");
                add("am");
            }
        };
        Filter<List<String>, List<String>> stopWordsFilter = new RemoveWords(mock(Pipe.class), mock(Pipe.class));
        List<String> output = stopWordsFilter.process(input);
        expectedOutput.removeAll(output);
        Assert.assertTrue(expectedOutput.isEmpty());
    }
}
