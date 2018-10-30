import filters.Filter;
import filters.RemoveWords;
import org.junit.Assert;
import org.junit.Test;
import pipes.BasicPipe;
import pipes.Pipe;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class PipeAndFilterTests {

//    @Test
//    public void removeStopWords_normal_removeWordsSuccess() {
//        BasicPipe<List<String>> inputPipe = new BasicPipe<>();
//        BasicPipe<List<String>> outputPipe = new BasicPipe<>();
//
//        List<String> stopWords = new ArrayList<String>() {
//            {
//                add("hello");
//                add("world");
//                add("testing");
//            }
//        };
//
//        List<String> inputOne = new ArrayList<String>() {
//            {
//                add("hello");
//                add("world");
//                add("I");
//                add("am");
//                add("testing");
//            }
//        };
//
//        List<String> inputTwo = new ArrayList<String>() {
//            {
//                add("hello");
//                add("world");
//                add("I");
//                add("am");
//                add("testing");
//                add("again");
//            }
//        };
//
//        List<String> expectedOutput = new ArrayList<String>() {
//            {
//                add("I");
//                add("am");
//            }
//        };
//
//        inputPipe.write(inputOne);
//        inputPipe.write(inputTwo);
//
//        Filter<List<String>, List<String>> stopWordsFilter = new RemoveWords(inputPipe, inputPipe, stopWords);
//        stopWordsFilter.run();
//        expectedOutput.removeAll(output);
//        Assert.assertTrue(expectedOutput.isEmpty());
}
