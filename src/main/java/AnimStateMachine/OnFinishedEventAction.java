package AnimStateMachine;

import com.jme3.anim.AnimClip;
import com.jme3.anim.tween.action.ClipAction;

import java.util.LinkedList;
import java.util.List;

public class OnFinishedEventAction extends ClipAction {

    private List<ActionDoneEventListener> subscribers = new LinkedList<>();
    private boolean isInitialized = false;

    public OnFinishedEventAction(AnimClip clip) {
        super(clip);
    }

    public void subscribe (ActionDoneEventListener subscriber) {
        this.subscribers.add(subscriber);
    }

    @Override
    public boolean interpolate(double t) {
        boolean running = super.interpolate(t);
        if (!isInitialized && t < this.getLength()) {
            isInitialized = true;
        }

        if (isInitialized && !running) {
            subscribers.forEach(s -> s.onComplete(this));
            isInitialized = false;
        }
        return running;
    }

    @Override
    public String toString() {
        return "EventAction";
    }
}
