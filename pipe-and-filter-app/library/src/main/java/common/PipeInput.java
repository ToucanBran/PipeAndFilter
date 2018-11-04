package common;

import common.enums.Pills;

public class PipeInput<T> {

    private T input;
    private boolean poisoned = false;
    public PipeInput(T input) {
        this.input = input;
    }

    public PipeInput(Pills pill) {
        poisoned = pill == Pills.POISON;
    }

    public T getInput() {
        return input;
    }

    public boolean isPoisoned() {
        return poisoned;
    }
    public void poison() {
        poisoned = true;
    }
}
