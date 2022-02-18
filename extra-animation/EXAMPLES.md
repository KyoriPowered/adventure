# Examples
### Hello World (actionbar animation)
```java
// Making frames
final TextComponent.Builder builder = Component.empty().toBuilder();

final ComponentReel reel = FrameReel.componentReelBuilder()
  .append(builder)
  .append(builder.append(Component.text("H").color(NamedTextColor.RED)))
  .append(builder.append(Component.text("e").color(NamedTextColor.GOLD)))
  .append(builder.append(Component.text("l").color(NamedTextColor.GOLD)))
  .append(builder.append(Component.text("l").color(NamedTextColor.YELLOW)))
  .append(builder.append(Component.text("o").color(NamedTextColor.GREEN)))
  .append(builder.append(Component.text(" ")))
  .append(builder.append(Component.text("W").color(NamedTextColor.GREEN)))
  .append(builder.append(Component.text("o").color(NamedTextColor.AQUA)))
  .append(builder.append(Component.text("r").color(NamedTextColor.DARK_PURPLE)))
  .append(builder.append(Component.text("l").color(NamedTextColor.DARK_PURPLE)))
  .append(builder.append(Component.text("d").color(NamedTextColor.DARK_BLUE)))
  .build();

// Setting up animation (frames from reel, displayed on actionbar, every half second, without extra listeners)
final Animation<Component, Audience> animation = Animation.animation(reel, FrameDisplayer.ACTION_BAR, Duration.ofMillis(500));

// Displaying the animation (We must have access to AnimationAudience or AnimationScheduler.)
final AnimationDisplay display = animationAudience.displayAnimation(animation);
```
### Frame Reel Mapping
```java
final FrameReel<Title> reel = FrameReel.reel(
  "H",
  "He",
  "Hel",
  "Hell",
  "Hello",
  "Hello ",
  "Hello W",
  "Hello Wo",
  "Hello Wor",
  "Hello Worl",
  "Hello World"
).map(s -> Title.title(Component.text(s).color(TextColor.color(new Random().nextInt())), Component.empty(), TitleMixer.DEFAULT_ANIMATION_TIMES), FrameReel.reelFactory());

final Animation<Title, Audience> animation = Animation.animation(reel, FrameDisplayer.TITLE, Duration.ofMillis(250));

final AnimationDisplay display = animationAudience.displayAnimation(animation);
```
### Message Bar
```java
final List<String> messages = Arrays.asList(
  "message1",
  "message2",
  "message3",
  "message4",
  "message5",
  "message6",
  "message7",
  "message8",
  "message9",
  "message10",
  "message11"
);

final FrameReel<Float> progressReel = FrameReelGenerator.gradient(FrameReel.reelFactory(), f -> f).createReel(50);

final AtomicReference<Component> name = new AtomicReference<>(Component.empty());

FrameSupplier<BossBarState> supplier = i -> {
  if (i % 50 == 0) {
    name.set(Component.text(messages.get(new Random().nextInt(messages.size()))).color(NamedTextColor.GOLD));
  }

  return BossBarState.state(name.get(), progressReel.frame(i % 50), null, null, (Collection<BossBar.Flag>) null);
};

Animation<BossBarState, BossBar> animation = Animation.animation(supplier, FrameDisplayer.BOSS_BAR, Duration.ofMillis(100),
DisplayListener.listener(DisplayListener.onStart(), (s, c) -> event.getPlayer().showBossBar(c.target())),
DisplayListener.listener(DisplayListener.onStop(), (s, c) -> event.getPlayer().hideBossBar(c.target())));

final BossBar bossBar = BossBar.bossBar(Component.empty(), 0f, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);

final AnimationDisplay display = scheduler.scheduleAnimation(animation, bossBar);
```
### Hello World with sounds
```java
final ComponentReel reel = FrameReel.reel(
  "H",
  "He",
  "Hel",
  "Hell",
  "Hello",
  "Hello ",
  "Hello W",
  "Hello Wo",
  "Hello Wor",
  "Hello Worl",
  "Hello World"
).map(s -> Component.text(s).color(TextColor.color(new Random().nextInt())), FrameReel.componentReelFactory());

final Animation<Component, Audience> animation = Animation.animation(reel, FrameDisplayer.ACTION_BAR, Duration.ofMillis(250));

final FrameSupplier<SoundInfo> soundSupplier = i -> {
if (i < reel.size())
  return SoundInfo.playSound(Sound.sound(Key.key("minecraft:entity.arrow.hit_player"), Sound.Source.MASTER, 10, 1));
else
   return null;
};

final Animation<SoundInfo, Audience> soundAnimation = Animation.animation(soundSupplier, FrameDisplayer.SOUND, Duration.ofMillis(250));

final AnimationDisplay display = scheduler.scheduleAnimations(animation.displayRequest(event.getPlayer()), soundAnimation.displayRequest(event.getPlayer()));
```
### Reel Mixing
```java
final ComponentReel titlePart = FrameReel.reel(
      "a",
      "b",
      "c"
    ).map(Component::text, FrameReel.componentReelFactory());

    final ComponentReel subtitlePart = FrameReel.reel(
      1,
      2,
      3
    ).map(Component::text, FrameReel.componentReelFactory());

    // Times are default (TitleMixer.DEFAULT_ANIMATION_TIMES)
    final FrameReel<Title> titleFrameReel = ReelMixer.titleMixerBuilder().titleReel(titlePart).subtitleReel(subtitlePart).build().asFrameReel();
```
