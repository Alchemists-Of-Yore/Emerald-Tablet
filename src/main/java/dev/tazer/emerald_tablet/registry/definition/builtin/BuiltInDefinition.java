package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.definition.Definition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public abstract class BuiltInDefinition<T extends B, B> extends Definition<T, B> {
    private final List<String> aliases = new ArrayList<>();

    public BuiltInDefinition(ResourceKey<Registry<B>> registry, String id, Supplier<T> supplier) {
        super(registry, id, supplier);
    }

    protected abstract Registry<B> builtInRegistry();

    @Override
    public void bind() {
        super.bind();
        setHolder(builtInRegistry().getHolder(key()).orElseThrow());
    }

    public List<String> aliases() {
        return aliases;
    }

    @SuppressWarnings("unchecked")
    public <S extends BuiltInDefinition<T, B>> S withAliases(String... aliases) {
        requireEditable();
        this.aliases.addAll(Set.of(aliases));
        return (S) this;
    }
}
