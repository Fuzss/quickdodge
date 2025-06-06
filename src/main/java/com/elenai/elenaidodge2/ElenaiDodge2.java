package com.elenai.elenaidodge2;

import com.elenai.elenaidodge2.config.ED2ClientConfig;
import com.elenai.elenaidodge2.config.ED2ServerConfig;
import com.elenai.elenaidodge2.networking.ED2Messages;
import com.elenai.elenaidodge2.sound.ED2Sounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ElenaiDodge2.MOD_ID)
public class ElenaiDodge2 {
    public static final String MOD_ID = "elenaidodge2";

    public ElenaiDodge2() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ED2ClientConfig.SPEC);
        // server configs are synced automatically to clients
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ED2ServerConfig.SPEC);

        ED2Sounds.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ED2Messages::register);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
