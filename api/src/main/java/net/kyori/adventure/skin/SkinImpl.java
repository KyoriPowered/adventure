package net.kyori.adventure.skin;

import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

/* package */ final class SkinImpl implements Skin, Examinable {
    /* package */ final static Skin EMPTY = new SkinImpl(null, null);
    private final String data;
    private final String signature;

    /* package */ SkinImpl(@Nullable String data, @Nullable String signature) {
        this.data = data;
        this.signature = signature;
    }

    @Override
    public @Nullable String data() {
        return data;
    }

    @Override
    public @Nullable String signature() {
        return signature;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(
                ExaminableProperty.of("data", data),
                ExaminableProperty.of("signature", signature)
        );
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) return true;
        if(!(other instanceof Skin)) return false;
        final Skin that = (Skin) other;
        return Objects.equals(data, that.data()) && Objects.equals(signature, that.signature());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(data);
        result = (31 * result) + Objects.hashCode(signature);
        return result;
    }
}
