package AnimStateMachine;

import com.jme3.anim.tween.action.BlendableAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActionState {

    private BlendableAction action;
    private String name;
    private String layer;
    private List<Link> links = new ArrayList<>();

    public ActionState(String name, String layer, BlendableAction action) {
        this.name = name;
        this.layer = layer;
        this.action = action;
    }

    public BlendableAction getBlendableAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    public String getLayer() {
        return layer;
    }

    public Link linkTo(ActionState s2) {
        Link link = new Link(this, s2);
        links.add(link);
        return link;
    }

    public ActionState next () {
        Optional<Link> positiveLink = this.links
            .stream()
            .filter(Link::test)
            .findFirst();

        if (positiveLink.isPresent()) {
            return positiveLink.get().getTo();
        }
        return this;
    }

    @Override
    public String toString() {
        return "ActionState{" +
            "action=" + action +
            ", name='" + name + '\'' +
            ", links=" + links +
            '}';
    }
}
