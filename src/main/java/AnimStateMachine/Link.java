package AnimStateMachine;

import java.util.function.BooleanSupplier;

public class Link {

    private ActionState from;
    private ActionState to;
    private BooleanSupplier predicate;

    public Link(ActionState from, ActionState to) {
        this.from = from;
        this.to = to;
    }

    public Link when(BooleanSupplier predicate) {
        this.predicate = predicate;
        return this;
    }

    public boolean test() {
        return this.predicate.getAsBoolean();
    }

    public ActionState getFrom() {
        return from;
    }

    public ActionState getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Link{" +
            "from=" + from.getName() +
            ", to=" + to.getName() +
            '}';
    }
}
