# jme-anim-state-machine
jMonkeyEngine Animation State Machine

State machine to make animation states consume from a character controller state.
This way Character controllers no longer need to think about animations.

```
ActionState idleTopState = animStateMachine.addState("idle", "top", new ClipAction(animComposer.getAnimClip("IdleTop")));
ActionState idleBaseState = animStateMachine.addState("idleBase", new ClipAction(animComposer.getAnimClip("IdleBase")));

OnFinishedEventAction onFinishJumpLoopAction = new OnFinishedEventAction(animComposer.getAnimClip("JumpLoop"));
BlendAction blendedStartToLoop = new BlendAction(
    new LinearBlendSpace(0, 1f),
    new ClipAction(animComposer.getAnimClip("JumpStart")),
    onFinishJumpLoopAction
);

blendedStartToLoop.setSpeed(2f);

ActionState blendedStartToLoopState = animStateMachine.addState("blendedStartToLoop", blendedStartToLoop);
ActionState inAirState = animStateMachine.addState("in air", new ClipAction(animComposer.getAnimClip("JumpLoop")));

ClipAction runAction = new ClipAction(animComposer.getAnimClip("RunBase"));
ActionState runningState = animStateMachine.addState("running", runAction);

Link idleToStartJumping = animStateMachine.addLink(idleBaseState, blendedStartToLoopState);
Link startJumpingToInAir = animStateMachine.addLink(blendedStartToLoopState, inAirState);
Link inAirToIdle = animStateMachine.addLink(inAirState, idleBaseState);
Link idleToRunning = animStateMachine.addLink(idleBaseState, runningState);
Link runningToIdle = animStateMachine.addLink(runningState, idleBaseState);
Link runningToStartJumping = animStateMachine.addLink(runningState, blendedStartToLoopState);

animStateMachine.setDefaultState("top", idleTopState);
animStateMachine.setDefaultState(idleBaseState);

BetterCharacterControl characterControl = this.getSpatial().getParent().getControl(BetterCharacterControl.class);

BooleanSupplier didJump = () -> !characterControl.isOnGround() && characterControl.getVelocity().y > 0;
idleToStartJumping.when(didJump);
runningToStartJumping.when(didJump);

startJumpingToInAir.when(new DelayedConsumer(onFinishJumpLoopAction));
inAirToIdle.when(characterControl::isOnGround);

Supplier<Float> getForwardVelocity = () -> characterControl.getSpatial()
    .getLocalRotation()
    .inverse()
    .mult(characterControl.getVelocity())
    .z;

idleToRunning.when(() -> {
    if (!characterControl.isOnGround()) return false;
    return getForwardVelocity.get() >= 0.01f;
});

runningToIdle.when(() -> {
    if (!characterControl.isOnGround()) return false;
    return getForwardVelocity.get() < 0.01f;
});
```
