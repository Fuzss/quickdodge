package fuzs.quickdodge.init;

import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.quickdodge.QuickDodge;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.apache.commons.lang3.mutable.MutableInt;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(QuickDodge.MOD_ID);
    public static final Holder.Reference<SoundEvent> DODGE_SOUND_EVENT = REGISTRIES.registerSoundEvent("dodge");

    public static final DataAttachmentType<Entity, MutableInt> REMAINING_DODGE_TICKS_ATTACHMENT_TYPE = DataAttachmentRegistry.<MutableInt>entityBuilder()
            .defaultValue((Entity entity) -> entity.getType() == EntityType.PLAYER,
                    (RegistryAccess registries) -> new MutableInt())
            .build(QuickDodge.id("remaining_dodge_ticks"));

    public static void bootstrap() {
        // NO-OP
    }
}
