package net.kyori.text;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.List;

public class TranslatableComponent extends BaseComponent {

    private final String key;
    private List<Component> args;

    public TranslatableComponent(final String key) {
        this.key = key;
        this.args = EMPTY_COMPONENT_LIST;
    }

    public TranslatableComponent(final String key, final Object... args) {
        this.key = key;

        final List<Component> builder = new ArrayList<>(args.length);
        for(int i = 0; i < args.length; i++) {
            final Object arg = args[i];
            if(arg instanceof Component) {
                builder.add((Component) arg);
            } else {
                builder.add(new TextComponent(String.valueOf(arg)));
            }
        }
        this.args = builder;
    }

    public String getKey() {
        return this.key;
    }

    public List<Component> getArgs() {
        return this.args;
    }

    public TranslatableComponent addArg(final Component arg) {
        if(this.args == EMPTY_COMPONENT_LIST) this.args = new ArrayList<>();
        this.args.add(arg);
        return this;
    }

    @Override
    public Component copy() {
        final TranslatableComponent that;
        if(this.args == EMPTY_COMPONENT_LIST || this.args.isEmpty()) {
            that = new TranslatableComponent(this.key);
        } else {
            final Component[] args = new Component[this.args.size()];
            for(int i = 0; i < this.args.size(); i++) {
                args[i] = this.args.get(i).copy();
            }
            that = new TranslatableComponent(this.key, args);
        }
        that.mergeStyle(this);
        for(final Component child : this.getChildren()) {
            that.append(child);
        }
        return that;
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) return true;
        if(other == null || !(other instanceof TranslatableComponent)) return false;
        if(!super.equals(other)) return false;
        final TranslatableComponent that = (TranslatableComponent) other;
        return Objects.equal(this.key, that.key) && Objects.equal(this.args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), this.key, this.args);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("key", this.key)
            .add("args", this.args)
            .toString();
    }
}
