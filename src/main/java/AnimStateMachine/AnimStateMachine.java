package AnimStateMachine;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.BlendableAction;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimStateMachine extends AbstractControl {

    private List<ActionState> states = new ArrayList<>();
    private Map<String, ActionState> defaultStates = new HashMap<>();
    private Map<String, ActionState> currentStates = new HashMap<>();
    private boolean initialized = false;

    /**
     * Set default state on default layer.
     */
    public void setDefaultState (ActionState s) {
        setDefaultState(AnimComposer.DEFAULT_LAYER, s);
    }

    /**
     * Set default state on layer.
     */
    public void setDefaultState (String layer, ActionState s) {
        defaultStates.put(layer, s);
    }

    /**
     * Add state to State Machine on default Layer
     */
    public ActionState addState (String name, Action action) {
        return addState(name, AnimComposer.DEFAULT_LAYER, action);
    }

    /**
     * Add state to State Machine on specified layer.
     * State name must be unique.
     */
    public ActionState addState (String name, String layer, Action action) {
        ActionState state = new ActionState(name, layer, action);
        AnimComposer animComposer = getAnimComposer();
        animComposer.addAction(name, action);
        this.states.add(state);
        return state;
    }

    public Link addLink(ActionState s1, ActionState s2) {
        if ( !s1.getLayer().equals(s2.getLayer()) ) {
            throw new UnsupportedOperationException("Linking states from different layers is not supported");
        }
        return s1.linkTo(s2);
    }

    @Override
    protected void controlUpdate(float tpf) {

        AnimComposer animComposer = this.getSpatial().getControl(AnimComposer.class);

        if (animComposer == null) {
            throw new IllegalStateException("no anim composer attached to this spatial");
        }

        if (!initialized) {
            for (String layer : defaultStates.keySet()) {
                if (!currentStates.containsKey(layer)) {
                    if (!defaultStates.containsKey(layer)) {
                        throw new IllegalStateException("No default state set for layer: " + layer);
                    }

                    currentStates.put(layer, defaultStates.get(layer));
                    animComposer.setCurrentAction(currentStates.get(layer).getName(), layer);
                }
            }

            initialized = true;
        }

        for (Map.Entry<String, ActionState> actionStateEntry: currentStates.entrySet()) {
            String layer = actionStateEntry.getKey();
            ActionState oldState = actionStateEntry.getValue();
            ActionState currentState = oldState.next();
            boolean hasChangedState = oldState != currentState;

            if (hasChangedState) {
                currentStates.put(layer, currentState);
                animComposer.setCurrentAction(currentState.getName(), layer);
            }
        }
    }

    public ActionState getState (String name) {
        return states.stream()
            .filter(s -> s.getName().equals(name))
            .findFirst()
            .orElseThrow();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    public void printStateMachine () {
        states.forEach(System.out::println);
    }

    private AnimComposer getAnimComposer () {
        return getSpatial().getControl(AnimComposer.class);
    }
}
