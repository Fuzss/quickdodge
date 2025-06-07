package com.elenai.elenaidodge;

import com.elenai.elenaidodge.config.ClientConfig;
import com.elenai.elenaidodge.config.ServerConfig;
import com.elenai.elenaidodge.handler.CommonEventHandler;
import com.elenai.elenaidodge.init.ED2Sounds;
import com.elenai.elenaidodge.networking.ED2Messages;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ElenaiDodge.MOD_ID)
@Mod.EventBusSubscriber
public class ElenaiDodge {
    public static final String MOD_ID = "elenaidodge2";

    public ElenaiDodge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        // server configs are synced automatically to clients
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);

        ED2Sounds.register(modEventBus);
    }

    @SubscribeEvent
    public static void tickPlayer(TickEvent.PlayerTickEvent evt) {
        if (evt.phase == TickEvent.Phase.END) {
            CommonEventHandler.onPlayerTickEnd(evt.player);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ED2Messages::register);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
