/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2025 KyoriPowered
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
package net.kyori.adventure.annotation.processing;

import com.google.auto.service.AutoService;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import org.jetbrains.annotations.ApiStatus;

/**
 * Validate that PlatformAPI annotations are used in tandem with the {@link ApiStatus.Internal} annotation.
 *
 * @since 4.12.0
 */
@ApiStatus.Internal
@AutoService(Processor.class)
@SupportedAnnotationTypes(PlatformAPIAnnotationProcessor.ADVENTURE_PLATFORMAPI_ANNOTATION)
public class PlatformAPIAnnotationProcessor extends AbstractProcessor {

  public static final String ADVENTURE_PLATFORMAPI_ANNOTATION = "net.kyori.adventure.util.PlatformAPI";

  @Override
  public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
    for (final TypeElement annotation : annotations) {
      for (final Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
        if (element.getAnnotation(ApiStatus.Internal.class) == null) {
          this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ADVENTURE_PLATFORMAPI_ANNOTATION + " needs to be used together with " + ApiStatus.Internal.class.getCanonicalName() + ", see PlatformAPI javadocs", element);
        }
      }
    }
    return false;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }
}
