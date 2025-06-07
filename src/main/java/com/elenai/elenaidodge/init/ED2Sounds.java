package com.elenai.elenaidodge.init;

import com.elenai.elenaidodge.ElenaiDodge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ED2Sounds {
    static final DeferredRegister<SoundEvent> REGISTRIES = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
            ElenaiDodge.MOD_ID);
    public static final RegistryObject<SoundEvent> DODGE_SOUND_EVENT = REGISTRIES.register("dodge",
            () -> new SoundEvent(new ResourceLocation(ElenaiDodge.MOD_ID, "dodge")));

    public static void register(IEventBus bus) {
        REGISTRIES.register(bus);
    }
}
