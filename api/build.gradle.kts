import net.bytebuddy.asm.ModifierAdjustment
import net.bytebuddy.build.gradle.Adjustment
import net.bytebuddy.description.method.MethodDescription
import net.bytebuddy.description.modifier.SyntheticState
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.ClassFileLocator
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.matcher.ElementMatcher

plugins {
  id("adventure.common-conventions")
  alias(libs.plugins.jmh)
  alias(libs.plugins.byteBuddy)
}

configurations {
  testCompileOnly {
    extendsFrom(compileOnlyApi.get())
  }
}

dependencies {
  api(projects.adventureKey)
  compileOnlyApi(libs.jetbrainsAnnotations)
  compileOnlyApi(libs.jspecify)
  testImplementation(libs.guava)
  annotationProcessor(projects.adventureAnnotationProcessors)
  testCompileOnly(libs.autoService.annotations)
  testAnnotationProcessor(libs.autoService)
}

applyJarMetadata("net.kyori.adventure")

class SyntheticPlugin : net.bytebuddy.build.Plugin {
  private val name = "net.kyori.adventure.internal.Synthetic"

  override fun apply(
    builder: DynamicType.Builder<*>,
    typeDescription: TypeDescription,
    classFileLocator: ClassFileLocator,
  ): DynamicType.Builder<*> {
    return builder.visit(
      ModifierAdjustment()
        .withMethodModifiers(
          ElementMatcher { methodDescription: MethodDescription -> methodDescription.declaredAnnotations.any { it.annotationType.name == name } },
          SyntheticState.SYNTHETIC
        )
    )
  }

  override fun matches(target: TypeDescription): Boolean {
    return target.declaredMethods.any { method ->
      method.declaredAnnotations.any { annotation ->
        annotation.annotationType.name == name
      }
    }
  }

  override fun close() {
    // Nothing to close!
  }
}

byteBuddy {
  adjustment = Adjustment.SELF
  transformation {
    plugin = SyntheticPlugin::class.java
  }
}
