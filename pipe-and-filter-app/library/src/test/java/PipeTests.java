import org.junit.Assert;
import org.junit.Test;
import pipes.Pipe;

public class PipeTests {

    @Test
    public void Pipe_normal_readAndWriteSuccess() {
        Pipe pipe = new Pipe();
        pipe.write("input");
        Assert.assertTrue(pipe.read().equals("input"));
    }

    @Test
    public void Pipe_normal_readNoJobs() {
        Pipe pipe = new Pipe();
        Assert.assertTrue(pipe.read() == null);
    }
}
