import org.junit.Assert;
import org.junit.Test;
import pipes.BasicPipe;

public class PipeTests {

    @Test
    public void basicPipe_normal_readAndWriteSuccess() {
        BasicPipe pipe = new BasicPipe();
        pipe.write("input");
        Assert.assertTrue(pipe.read().equals("input"));
    }

    @Test
    public void basicPipe_normal_readNoJobs() {
        BasicPipe pipe = new BasicPipe();
        Assert.assertTrue(pipe.read() == null);
    }
}
