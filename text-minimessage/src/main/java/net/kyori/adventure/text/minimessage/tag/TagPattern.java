package net.kyori.adventure.text.minimessage.tag;

import net.kyori.adventure.text.minimessage.internal.TagInternals;
import org.intellij.lang.annotations.Pattern;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

@Documented
@Pattern(TagInternals.TAG_NAME_REGEX)
@Retention(RetentionPolicy.CLASS)
@Target({ METHOD, FIELD, PARAMETER, LOCAL_VARIABLE }) //Unsure of what is needed here
// TODO: documentation
/**
 * @since 4.14.0
 */
public @interface TagPattern {
}
