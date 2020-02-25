package net.kyori.text.title;

import java.time.Duration;
import java.util.function.Consumer;
import net.kyori.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TitleBuilder implements Title.Builder {
  @Nullable Component title;
  @Nullable Component subtitle;
  @Nullable Component actionbar;
  @Nullable TimesImpl times;
  boolean clear;
  boolean reset;

  @Override
  public Title.@NonNull Builder title(final @NonNull Component title) {
    this.title = title;
    return this;
  }

  @Override
  public Title.@NonNull Builder subtitle(final @NonNull Component subtitle) {
    this.subtitle = subtitle;
    return this;
  }

  @Override
  public Title.@NonNull Builder actionbar(final @NonNull Component actionbar) {
    this.actionbar = actionbar;
    return this;
  }

  @Override
  public Title.@NonNull Builder times(final @NonNull Consumer<Times> consumer) {
    final TimesImpl times = new TimesImpl();
    consumer.accept(times);
    this.times = times;
    return this;
  }

  @Override
  public Title.@NonNull Builder clear(final boolean clear) {
    this.clear = clear;
    return this;
  }

  @Override
  public Title.@NonNull Builder reset(final boolean reset) {
    this.reset = reset;
    return this;
  }

  @Override
  public @NonNull Title build() {
    return new TitleImpl(this);
  }

  static class TimesImpl implements Times {
    int fadeIn;
    int stay;
    int fadeOut;

    @Override
    public Times fadeIn(final @NonNull Duration duration) {
      return this.fadeIn((int) TitleImpl.ticks(duration));
    }

    @Override
    public Times stay(final @NonNull Duration duration) {
      return this.stay((int) TitleImpl.ticks(duration));
    }

    @Override
    public Times fadeOut(final @NonNull Duration duration) {
      return this.fadeOut((int) TitleImpl.ticks(duration));
    }

    @Override
    public Times fadeIn(final int duration) {
      this.fadeIn = duration;
      return this;
    }

    @Override
    public Times stay(final int duration) {
      this.stay = duration;
      return this;
    }

    @Override
    public Times fadeOut(final int duration) {
      this.fadeOut = duration;
      return this;
    }
  }
}
