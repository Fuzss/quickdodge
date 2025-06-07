package fuzs.quickdodge.neoforge.data.client;

import fuzs.quickdodge.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.neoforge.api.client.data.v2.AbstractSoundProvider;
import net.minecraft.sounds.SoundEvents;

public class ModSoundProvider extends AbstractSoundProvider {

    public ModSoundProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addSounds() {
        this.add(ModRegistry.DODGE_SOUND_EVENT.value(), SoundEvents.PLAYER_ATTACK_SWEEP);
    }
}
