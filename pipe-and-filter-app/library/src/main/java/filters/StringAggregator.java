package filters;

import common.PipeInput;
import pipes.Pipe;

import java.util.ArrayList;
import java.util.List;

// Class is used to turn multiple string lists into a single one once
// all inputs are received
public class StringAggregator extends Filter<List<String>, List<String>> {

    private List<String> aggregate = new ArrayList<>();
    // Aggregate will return once poisonPillCount reaches
    // the goal
    private int poisonPillGoal = 1;
    private int poisonPillCount = 0;

    public StringAggregator(int poisonPillGoal) {
        this.poisonPillGoal = poisonPillGoal;
    }

    public StringAggregator() {
        super();
    }

    public StringAggregator(Pipe<List<String>> outputPipe) {
        super(outputPipe);

    }

    @Override
    public List<String> process(List<String> input) {
        // if input is null, no more lists are expected so return aggregate
        if (input == null) {
            return null;
        }
        poisonPillCount++;
        aggregate.addAll(input);
        if (poisonPillCount == poisonPillGoal) {
            return aggregate;
        }
        return null;
    }

    @Override
    public boolean processPoison() {
        return poisonPillCount < poisonPillGoal;
    }


    @Override
    public String toString() {
        return "SA";
    }
}
