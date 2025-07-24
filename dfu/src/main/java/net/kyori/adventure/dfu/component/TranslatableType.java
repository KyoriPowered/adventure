package net.kyori.adventure.dfu.component;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.kyori.adventure.dfu.AdventureCodecs;
import net.kyori.adventure.dfu.Codecs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import org.jetbrains.annotations.Nullable;

public class TranslatableType extends ComponentType<TranslatableComponent> {
  public static final TranslationArgument[] EMPTY_ARGUMENTS = new TranslationArgument[0];
  private static final Codec<Object> OBJECT_ARGUMENT_CODEC = Codecs.BASIC_OBJECT.validate(TranslatableType::validate);
  private static final Codec<Object> ARGUMENT_CODEC = Codec.either(OBJECT_ARGUMENT_CODEC, AdventureCodecs.COMPONENT)
    .xmap(either ->
        either.map(object -> object, text -> Objects.requireNonNullElse(text.toString(), text)),
      argument -> {
        Either<Object, Component> either;
        if (argument instanceof Component) {
          Component text = (Component) argument;
          either = Either.right(text);
        } else {
          either = Either.left(argument);
        }
        return either;
      });
  public static final MapCodec<TranslatableComponent> CODEC = RecordCodecBuilder.mapCodec(instance ->
    instance.group(
      Codec.STRING.fieldOf("translate")
        .forGetter(TranslatableComponent::key),
      Codec.STRING.lenientOptionalFieldOf("fallback")
        .forGetter(content -> Optional.ofNullable(content.fallback())),
      ARGUMENT_CODEC.listOf().optionalFieldOf("with")
        .forGetter(content -> toOptionalList(content.arguments()))
    ).apply(instance, TranslatableType::of));

  public static final TranslatableType INSTANCE = new TranslatableType(CODEC);

  private TranslatableType(final MapCodec<TranslatableComponent> codec) {
    super("translatable", codec);
  }

  private static boolean isPrimitive(@Nullable Object object) {
    return object instanceof String || object instanceof Number || object instanceof Boolean;
  }

  private static DataResult<Object> validate(@Nullable Object object) {
    if (!isPrimitive(object)) {
      return DataResult.error(() -> "This value needs to be parsed as component");
    }
    return DataResult.success(object);
  }

  private static Optional<List<Object>> toOptionalList(List<?> args) {
    return args.isEmpty() ? Optional.empty() : Optional.of(new ArrayList<>(args));
  }

  private static TranslationArgument[] toArray(Optional<List<Object>> args) {
    if (args.isEmpty()) {
      return EMPTY_ARGUMENTS;
    }
    final List<Object> objects = args.get();
    final TranslationArgument[] array = new TranslationArgument[objects.size()];
    for (int i = 0; i < objects.size(); i++) {
      final Object object = objects.get(i);
      if (object instanceof TranslationArgument)
        array[i] = (TranslationArgument) object;
      else if (isPrimitive(object)) {
        if (object instanceof String) {
          array[i] = TranslationArgument.component(Component.text(object.toString()));
        } else if (object instanceof Number) {
          array[i] = TranslationArgument.numeric((Number) object);
        } else if (object instanceof Boolean) {
          array[i] = TranslationArgument.bool((Boolean) object);
        } else if (object instanceof Component) {
          array[i] = TranslationArgument.component((Component) object);
        }
      }
    }
    return array;
  }


  private static TranslatableComponent of(String key, Optional<String> fallback, Optional<List<Object>> args) {
    return Component.translatable(key, fallback.orElse(null), toArray(args));
  }
}
