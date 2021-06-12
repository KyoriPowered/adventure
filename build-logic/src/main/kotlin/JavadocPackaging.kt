import org.gradle.api.Named
import org.gradle.api.attributes.Attribute

interface JavadocPackaging : Named {
  companion object {
    val ATTRIBUTE = Attribute.of("net.kyori.javadoc.packaging", JavadocPackaging::class.java)

    const val ARCHIVE = "archive"
    const val DIRECTORY = "directory"
  }
}
