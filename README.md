# jme-anim-state-machine
jMonkeyEngine Animation State Machine

State machine to make animation states consume from a character controller state.
This way Character controllers no longer need to think about animations.
This Anim State Machine works with two entities, states and links.

## State

Create a state in default layer with name "idleBase", and set it as the default state.
```
ActionState idleBaseState = animStateMachine.addState(
    "idleBase", 
    new ClipAction(animComposer.getAnimClip("IdleBase"))
);
animStateMachine.setDefaultState(idleBaseState);
```

Links between states can not cross a layer.
State in different layer "top" (assumes this layer exists in the AnimationComposer).
```
ActionState idleTopState = animStateMachine.addState(
    "idle",
    "top", 
    new ClipAction(animComposer.getAnimClip("IdleTop"))
);
```

## Link
Creat links between states, and specify when the state should move to the next state  
```
Link idleToRunning = animStateMachine.addLink(idleBaseState, runningState);
idleToRunning.when(() -> getForwardVelocity.get() >= 0.01f);
```
