import filters.Filter;
import filters.Frequency;
import filters.RemoveNonAlphabetical;
import org.junit.Test;
import pipes.Pipe;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class PipeAndFilterTests {

    @Test
    public void removeStopWords_normal_removeWordsSuccess() {
        List<String> wordsWithNonAlphas = new ArrayList<String>() {
            {
                add("This");
                add("test's");
                add("2");
            }
        };

        Filter<List<String>, List<String>> removeNonAlphabeticalFilter =
                new RemoveNonAlphabetical(new Pipe<List<String>>());

        removeNonAlphabeticalFilter.pipeTo(new Frequency<>());



    }
}
