package dev.tazer.emerald_tablet.registry.definition.dynamic;

import dev.tazer.emerald_tablet.registry.definition.HasName;
import dev.tazer.emerald_tablet.registry.definition.Translations;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class DamageTypeDefinition extends DynamicDefinition<DamageType> implements HasName {
    private final Translations translations;
    @Nullable
    private String deathMessage;
    @Nullable
    private String playerDeathMessage;

    public DamageTypeDefinition(String id, DamageType damageType) {
        super(Registries.DAMAGE_TYPE, id, damageType);
        this.translations = new Translations(Translations.createName(id));
    }

    public DamageTypeDefinition deathMessage(String message) {
        this.deathMessage = message;
        return this;
    }

    public DamageTypeDefinition playerDeathMessage(String message) {
        this.playerDeathMessage = message;
        return this;
    }

    @Override
    public Translations translations() {
        return translations;
    }

    @Override
    public Map<String, String> translationEntries() {
        Map<String, String> map = new LinkedHashMap<>();
        if (deathMessage != null) {
            map.put("death.attack." + id(), deathMessage);
        }
        if (playerDeathMessage != null) {
            map.put("death.attack." + id() + ".player", playerDeathMessage);
        }
        return map;
    }

    @Override
    public String translationPrefix() {
        return "damage_type";
    }
}
