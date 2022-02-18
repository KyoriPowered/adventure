## Animations
Animation is a set of frames displayed periodically in certain order.

Animation class represent a recipe that makes able to create an animation display.
This recipe has its own properties that answers particular question we can ask to
prepare it.

### What to display?
**Frame Supplier** - it gives us frames in some order and tells us when there is no more
frames. It not gives information about how to display frames. It only gives frames and
only frames.

### How to display?
**Frame Displayer** - it takes one frame and displays it. Takes and displays all the time.
It provides a way to display particular frames. That is only one simple job it does.

### How frequent?
**Frame interval** - a time duration between frames. That is all.

**Animation also provides listeners, but they do not play main role in animation. They are
made to inform about a change of state of a display of this animation.**

## Animation Scheduling

Animation scheduling is done by AnimationScheduler. This object is designed to be
implemented by platform. To simplify scheduling process there is also Animation Audience
which may be implemented in platforms as well. It helps with scheduling single animation
task with Audience as target.

### Displays
Display is simply play of animation or animation that is playing. Display can consist of
one or more animations. It can be scheduled or cancelled.

**Creating a display (Displaying an animation):**
```java
// Single
final AnimationDisplay display1 = animationAudience.displayAnimation(animation);
final AnimationDisplay display2 = animationScheduler.scheduleAnimation(animation, audience);
final AnimationDisplay display3 = animationScheduler.scheduleAnimation(animation, target);
//Multiple (a vars - animations, t vars targets (e.g. Audience))
final AnimationDisplay display4 = animationScheduler.scheduleAnimations(a1.displayRequest(t1), a2.displayRequest(t2));
```
**Canceling a display**
```java
display.stop();
```

## Frame Displayers
There are a plenty of builtin frame displayers. They are mostly equivalents to Adventure's
api methods.

###ActionBar frame displayer
**Frames:** Component<br>
**Target:** Audience<br>
<br>
frame, target -> target.sendActionBar(frame)

###Title frame displayer
**Frames:** Title<br>
**Target:** Audience<br>
<br>
frame, target -> target.showTitle(frame)

###Sound frame displayer
**Frames:** SoundInfo (animation api utility class)<br>
**Target:** Audience<br>
<br>
frame, target -> frame.apply(target)

###BossBar frame displayer
**Frames:** BossBarState (animation api utility class)<br>
**Target:** BossBar<br>
<br>
frame, target -> frame.apply(target)<br><br>


**There is also an option to create custom displayers**

## Frame Suppliers and Frame Containers
### Suppliers
Frame Suppliers provides frames to animations. Every animation has one frame supplier.
These objects are intended to be immutable. Animation display has internal counter that
increments every frame. Current value of counter is passed to the supplier which gives
frame for index corresponding to counter value or returns null when it is out of frames.

Built in supplier can be found in javadoc in ContainerFrameSupplier class.

### Containers
Frame containers store frames. They are also a linear suppliers. They have fix size
and behave a bit like a list. FrameContainer class defines some basic features of containers.
There are also frame reel classes (FrameReel and ComponentReel) that provides extra
methods to deal with frames.

**Creating a reel**
```java
final FrameReel<Integer> f1 = FrameReel.reel(1, 2, 3);
```
**Mapping a reel**
```java
final ComponentReel c1 = FrameReel.reel("abc", "def", "ghi").map(str -> Component.text(str), FrameReel.componentReelFactory());
```
**Generating a reel**
```java
final ComponentReel grayscale = FrameReelGenerator.gradient(v -> Component.text("GRAYSCALE!!!").color(TextColor.color(HSVLike.of(0f, 0f, v)))).createReel(frameNum);
```
**Mixing reels**
```java
// These reels should have values assigned
final ComponentReel t;
final ComponentReel st;
final FrameReel<Title.Times> ts;
final FrameReel<Title> = ReelMixer.title(t, st, ts).asFrameReel();
```
