import filters.*;
import org.junit.Assert;
import org.junit.Test;
import pipes.Pipe;

import java.util.*;

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
        Filter<List<String>, List<String>> stopWordsFilter = new RemoveWords(mock(Pipe.class), stopWords);
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
        Filter<List<String>, List<String>> stopWordsFilter = new RemoveWords( mock(Pipe.class));
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
        Filter<List<String>, List<String>> removeNonAlphabeticalFilter = new RemoveNonAlphabetical( mock(Pipe.class));
        List<String> output = removeNonAlphabeticalFilter.process(wordsWithNonAlphas);
        output.removeAll(expectedOutput);
        Assert.assertTrue(output.isEmpty());
    }

    @Test
    public void removeNonAlphabetical_nullList_returnEmptyList() {

        Filter<List<String>, List<String>> removeNonAlphabeticalFilter = new RemoveNonAlphabetical( mock(Pipe.class));
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
        Filter<List<String>, List<String>> removeNonAlphabeticalFilter = new RemoveNonAlphabetical( mock(Pipe.class));
        List<String> output = removeNonAlphabeticalFilter.process(wordsWithNonAlphas);
        output.removeAll(expectedOutput);
        Assert.assertTrue(output.isEmpty());
    }

    @Test
    public void stem_normal_success() {
        List<String> words = new ArrayList<String>() {
            {
                add("jumps");
                add("jumping");
                add("jumped");
                add("jump");
            }
        };

        List<String> expectedOutput = new ArrayList<String>() {
            {
                add("jump");
                add("jump");
                add("jump");
                add("jump");
            }
        };
        Filter<List<String>, List<String>> stemFilter = new Stem( mock(Pipe.class));
        List<String> output = stemFilter.process(words);
        output.removeAll(expectedOutput);
        Assert.assertTrue(output.isEmpty());
    }

    @Test
    public void frequency_normal_success() {
        List<Object> words = new ArrayList<Object>() {
            {
                add("jump");
                add("jump");
                add("jump");
                add("jump");
            }
        };

        Filter<List<Object>, Map<Object, Integer>> frequencyFilter = new Frequency( mock(Pipe.class));
        Map<Object, Integer> output = frequencyFilter.process(words);
        Assert.assertTrue(output.size() == 1 && output.get("jump") == 4);
    }

    @Test
    public void frequency_differentObjects_success() {
        Set setOne = new HashSet();
        setOne.add("test");
        Set setTwo = new HashSet();
        setTwo.add("test2");
        List<Object> words = new ArrayList<Object>() {
            {
                add("jump");
                add("jump");
                add(1);
                add(1);
                add(setOne);
                add(setOne);
                add(setTwo);
            }
        };

        Filter<List<Object>, Map<Object, Integer>> frequencyFilter = new Frequency( mock(Pipe.class));
        Map<Object, Integer> output = frequencyFilter.process(words);
        Assert.assertTrue(output.size() == 4 && output.get("jump") == 2
                            && output.get(1) == 2 && output.get(setOne) == 2
                            && output.get(setTwo) == 1);
    }

    @Test(timeout=1000)
    public void frequency_stressTest_success() {
        Set setOne = new HashSet();
        setOne.add("test");
        List<Object> words = new ArrayList<Object>();
        for (int i = 0; i < 100000; i++) {
            words.add("jump");
            words.add(1);
            words.add(setOne);
        }

        Filter<List<Object>, Map<Object, Integer>> frequencyFilter = new Frequency( mock(Pipe.class));
        frequencyFilter.process(words);
    }

    @Test
    public void mostFrequent_defaultLimit_success() {
        HashMap<String, Integer> frequency = new HashMap<>();
        frequency.put("Hello", 10);
        frequency.put("Foo", 13);
        frequency.put("Bar", 13);
        frequency.put("jumps", 11);
        frequency.put("jumping",22);
        frequency.put("jumped", 6);
        frequency.put("jump", 2);
        frequency.put("world", 1);
        frequency.put("I",32);
        frequency.put("am", 40);
        frequency.put("testing", 12);

        String[] expectedOrder = new String[]{"am", "I", "jumping", "Bar", "Foo", "testing",
                "jumps", "Hello","jumped", "jump"};
        Filter<Map<String, Integer>, List<String>> mostFrequentWordsFilter = new MostFrequent( mock(Pipe.class));

        String[] output = mostFrequentWordsFilter.process(frequency).toArray(new String[0]);
        Assert.assertTrue(output.length == expectedOrder.length);
        for (int i = 0; i < output.length; i++) {
            Assert.assertTrue(output[i].equals(expectedOrder[i]));
        }
    }

    @Test
    public void mostFrequent_customLimit_success() {
        HashMap<String, Integer> frequency = new HashMap<>();
        frequency.put("Hello", 10);
        frequency.put("Foo", 13);
        frequency.put("Bar", 13);
        frequency.put("jumps", 11);
        frequency.put("jumping",22);
        frequency.put("jumped", 6);
        frequency.put("jump", 2);
        frequency.put("world", 1);
        frequency.put("I",32);
        frequency.put("am", 40);
        frequency.put("testing", 12);

        Filter<Map<String, Integer>, List<String>> mostFrequentWordsFilter =
                        new MostFrequent(mock(Pipe.class), 11);

        String[] output = mostFrequentWordsFilter.process(frequency).toArray(new String[0]);
        Assert.assertTrue(output.length == 11);
    }
}
