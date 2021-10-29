/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2021 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.animation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

/**
 * A frame container used to store information that enable animation of displayable media.
 *
 * <p>Animation stores frames and creates displays.</p>
 *
 * <p>Display role is to show frames in order.</p>
 *
 * @param <F> type of media used as frames
 * @param <D> type of generated displays used for animation show
 * @since 4.9.2
 */
public interface Animation<F, D extends AnimationDisplay<? extends F>> {

  /**
   * Creates a component animation of given frames.
   *
   * @param frames frames for the animation
   * @return new component animation with specified frames
   * @since 4.9.2
   */
  static ComponentAnimation component(Component... frames) {
    return new ComponentAnimationImpl(frames);
  }

  /**
   * Creates a component animation of given frames.
   *
   * @param frames frames for the animation
   * @return new component animation with specified frames
   * @since 4.9.2
   */
  static ComponentAnimation component(List<Component> frames) {
    return new ComponentAnimationImpl(frames);
  }

  /**
   * Creates a component animation which is result of merging given ones (in the order).
   *
   * @param subAnimations parts for the new animation
   * @return result of merging sub-animations.
   * @since 4.9.2
   */
  static ComponentAnimation component(ComponentAnimation... subAnimations) {
    return new ComponentAnimationImpl(subAnimations);
  }

  /**
   * Creates a title animation of given frames.
   *
   * @param frames frames for the animation
   * @return new title animation with specified frames
   * @since 4.9.2
   */
  static TitleAnimation title(List<Title> frames) {
    return new TitleAnimationImpl(frames);
  }

  /**
   * Creates a title animation of given frames.
   *
   * @param frames frames for the animation
   * @return new title animation with specified frames
   * @since 4.9.2
   */
  static TitleAnimation title(Title... frames) {
    return new TitleAnimationImpl(frames);
  }

  /**
   * Creates a title animation of two component animations representing frame streams for title and subtitle.
   *
   * <p>Title animation and subtitle animation are merged the way that <b>title</b> frames are present
   * from the beginning, but subtitle is firstly preceded by empty component frames in amount equal to offset.</p>
   *
   * @param titleAnimation an animation representing title part
   * @param subtitleOffset an offset of subtitle frames
   * @param subtitleAnimation an animation representing subtitle part
   * @param times times for all frames
   * @return new title animation from parameters
   * @see Component#empty()
   *
   * @since 4.9.2
   */
  static TitleAnimation title(ComponentAnimation titleAnimation, int subtitleOffset, ComponentAnimation subtitleAnimation, Title.Times times) {
    final List<Title> frames = new ArrayList<>();

    final Iterator<Component> titleIterator = titleAnimation.frameIterator();
    final Iterator<Component> subtitleIterator = subtitleAnimation.frameIterator();

    while (titleIterator.hasNext() || subtitleOffset > 0 || subtitleIterator.hasNext()) {
      final Component title;

      if (titleIterator.hasNext())
        title = titleIterator.next();
      else
        title = Component.empty();

      final Component subtitle;

      if (subtitleOffset <= 0) {
        if (subtitleIterator.hasNext())
          subtitle = subtitleIterator.next();
        else
          subtitle = Component.empty();
      } else {
        subtitle = Component.empty();
        subtitleOffset--;
      }

      frames.add(Title.title(title, subtitle, times));
    }

    return new TitleAnimationImpl(frames);
  }

  /**
   * Does this same as @link{TitleAnimation#title(ComponentAnimation, int, ComponentAnimation, Title.Times)}, but with default times.
   *
   * @param subtitleOffset an offset of title frames
   * @param titleAnimation an animation representing title part
   * @param subtitleAnimation an animation representing subtitle part
   * @return new title animation from parameters
   * @since 4.9.2
   */
  static TitleAnimation title(ComponentAnimation titleAnimation, int subtitleOffset, ComponentAnimation subtitleAnimation) {
    return Animation.title(titleAnimation, subtitleOffset, subtitleAnimation, Title.DEFAULT_TIMES);
  }

  /**
   * Creates a title animation of two component animations representing frame streams for title and subtitle.
   *
   * <p>Title animation and subtitle animation are merged the way that <b>subtitle</b> frames are present
   * from the beginning, but title is firstly preceded by empty component frames in amount equal to offset.</p>
   *
   *
   * @param titleOffset an offset of title frames
   * @param titleAnimation an animation representing title part
   * @param subtitleAnimation an animation representing subtitle part
   * @param times times for all frames
   * @return new title animation from parameters
   * @see Component#empty()
   *
   * @since 4.9.2
   */
  static TitleAnimation title(int titleOffset, ComponentAnimation titleAnimation, ComponentAnimation subtitleAnimation, Title.Times times) {
    final List<Title> frames = new ArrayList<>();

    final Iterator<Component> titleIterator = titleAnimation.frameIterator();
    final Iterator<Component> subtitleIterator = subtitleAnimation.frameIterator();

    while (titleIterator.hasNext() || titleOffset > 0 || subtitleIterator.hasNext()) {
      final Component subtitle;

      if (subtitleIterator.hasNext())
        subtitle = subtitleIterator.next();
      else
        subtitle = Component.empty();

      final Component title;

      if (titleOffset <= 0) {
        if (titleIterator.hasNext())
          title = titleIterator.next();
        else
          title = Component.empty();
      } else {
        title = Component.empty();
        titleOffset--;
      }

      frames.add(Title.title(title, subtitle, times));
    }

    return new TitleAnimationImpl(frames);
  }

  /**
   * Does this same as @link{TitleAnimation#title(int, ComponentAnimation, ComponentAnimation, Title.Times)}, but with default times.
   *
   * @param titleOffset an offset of title frames
   * @param titleAnimation an animation representing title part
   * @param subtitleAnimation an animation representing subtitle part
   * @return new title animation from parameters
   * @since 4.9.2
   */
  static TitleAnimation title(int titleOffset, ComponentAnimation titleAnimation, ComponentAnimation subtitleAnimation) {
    return Animation.title(titleOffset, titleAnimation, subtitleAnimation, Title.DEFAULT_TIMES);
  }

  /**
   * Creates a title animation of two component animations representing frame streams for title and subtitle.
   *
   * <p>Both title and subtitle animations start from this same point.</p>
   *
   * @param titleAnimation an animation representing title part
   * @param subtitleAnimation an animation representing subtitle part
   * @param times times for all frames
   * @return new title animation from parameters
   *
   * @since 4.9.2
   */
  static TitleAnimation title(ComponentAnimation titleAnimation, ComponentAnimation subtitleAnimation, Title.Times times) {
    return Animation.title(titleAnimation, 0, subtitleAnimation, times);
  }

  /**
   * Does this same as @link{TitleAnimation#title(ComponentAnimation, ComponentAnimation, Title.Times)}, but with default times.
   *
   * @param titleAnimation an animation representing title part
   * @param subtitleAnimation an animation representing subtitle part
   * @return new title animation from parameters
   * @since 4.9.2
   */
  static TitleAnimation title(ComponentAnimation titleAnimation, ComponentAnimation subtitleAnimation) {
    return Animation.title(titleAnimation, 0, subtitleAnimation, Title.DEFAULT_TIMES);
  }

  /**
   * Returns a list of frames contained by this animation.
   *
   * @return a list of stored frames
   * @since 4.9.2
   */
  List<F> frames();

  /**
   * Creates a new display for this animation.
   *
   * @param audience the viewer of the display
   * @param schedulerTask task for scheduling frames
   * @return created display
   * @since 4.9.2
   */
  D createDisplay(Audience audience, AnimationTask schedulerTask);

  /**
   * Returns iterator that specifies the way of getting frames in correct order for this animation.
   *
   * @return iterator of contained frames
   * @since 4.9.2
   */
  Iterator<F> frameIterator();

}
