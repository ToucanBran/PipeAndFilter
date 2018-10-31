import filters.Filter;
import filters.RemoveNonAlphabetical;
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
        output.removeAll(expectedOutput);
        Assert.assertTrue(output.isEmpty());
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
                add("hello");
                add("world");
                add("I");
                add("am");
                add("testing");
            }
        };
        Filter<List<String>, List<String>> stopWordsFilter = new RemoveWords(mock(Pipe.class), mock(Pipe.class));
        List<String> output = stopWordsFilter.process(input);
        output.removeAll(expectedOutput);
        Assert.assertTrue(output.isEmpty());
    }

    @Test
    public void removeNonAlphabetical_normal_success() {
        List<String> wordsWithNonAlphas = new ArrayList<String>() {
            {
                add("This");
                add("test's");
                add("2");
            }
        };

        List<String> expectedOutput = new ArrayList<String>() {
            {
                add("This");
                add("tests");
            }
        };
        Filter<List<String>, List<String>> removeNonAlphabeticalFilter = new RemoveNonAlphabetical(mock(Pipe.class), mock(Pipe.class));
        List<String> output = removeNonAlphabeticalFilter.process(wordsWithNonAlphas);
        output.removeAll(expectedOutput);
        Assert.assertTrue(output.isEmpty());
    }

    @Test
    public void removeNonAlphabetical_nullList_returnEmptyList() {

        Filter<List<String>, List<String>> removeNonAlphabeticalFilter = new RemoveNonAlphabetical(mock(Pipe.class), mock(Pipe.class));
        List<String> output = removeNonAlphabeticalFilter.process(null);
        Assert.assertTrue(output != null && output.isEmpty());
    }

    @Test
    public void removeNonAlphabetical_nonAlphaIntensiveInput_success() {
        List<String> wordsWithNonAlphas = new ArrayList<String>() {
            {
                add("h3e11ll000o_! ");
                add("\\/\\/w00or1ld");
            }
        };

        List<String> expectedOutput = new ArrayList<String>() {
            {
                add("hello");
                add("world");
            }
        };
        Filter<List<String>, List<String>> removeNonAlphabeticalFilter = new RemoveNonAlphabetical(mock(Pipe.class), mock(Pipe.class));
        List<String> output = removeNonAlphabeticalFilter.process(wordsWithNonAlphas);
        output.removeAll(expectedOutput);
        Assert.assertTrue(output.isEmpty());
    }
}
