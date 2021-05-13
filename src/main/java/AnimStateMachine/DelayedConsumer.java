package AnimStateMachine;

import com.jme3.anim.tween.action.Action;

import java.util.function.BooleanSupplier;

public class DelayedConsumer implements ActionDoneEventListener, BooleanSupplier {

    private boolean isCompleted = false;

    public DelayedConsumer(OnFinishedEventAction finishedEventAction) {
        finishedEventAction.subscribe(this);
    }

    @Override
    public void onComplete(Action a) {
        this.isCompleted = true;
    }

    @Override
    public boolean getAsBoolean() {
        if (isCompleted) {
            isCompleted = false;
            return true;
        }
        return false;
    }
}
