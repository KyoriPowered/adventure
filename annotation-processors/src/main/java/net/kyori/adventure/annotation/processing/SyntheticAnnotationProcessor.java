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
 * Validate that Synthetic annotations are used in tandem with the {@link Deprecated} annotation.
 *
 * @since 5.0.0
 */
@ApiStatus.Internal
@AutoService(Processor.class)
@SupportedAnnotationTypes(SyntheticAnnotationProcessor.ADVENTURE_SYNTHETIC_ANNOTATION)
public class SyntheticAnnotationProcessor extends AbstractProcessor {

  public static final String ADVENTURE_SYNTHETIC_ANNOTATION = "net.kyori.adventure.internal.Synthetic";

  @Override
  public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
    for (final TypeElement annotation : annotations) {
      for (final Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
        if (element.getAnnotation(Deprecated.class) == null) {
          this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ADVENTURE_SYNTHETIC_ANNOTATION + " needs to be used together with " + Deprecated.class.getCanonicalName() + ", see Synthetic javadocs", element);
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
