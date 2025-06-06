package com.elenai.elenaidodge2.event;

import com.elenai.elenaidodge2.ElenaiDodge2;
import com.elenai.elenaidodge2.client.KeyBinding;
import com.elenai.elenaidodge2.config.ED2ClientConfig;
import com.elenai.elenaidodge2.util.DodgeHandler;
import com.elenai.elenaidodge2.util.InputHandlers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = ElenaiDodge2.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void clientTickEvents(ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                Minecraft minecraft = Minecraft.getInstance();
                if (!minecraft.isPaused() && minecraft.player != null) {
                    while (KeyBinding.DODGE_KEY.consumeClick()) {
                        if (!ED2ClientConfig.DOUBLE_TAP_MODE.get()) {
                            InputHandlers.singleTapHandler(minecraft.player);
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
            if (DodgeHandler.dodgingCooldown > 0) {
                DodgeHandler.dodgingCooldown--;
            }
            if (ED2ClientConfig.DOUBLE_TAP_MODE.get()) {
                InputHandlers.doubleTapInputHandler((LocalPlayer) event.getEntity());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = ElenaiDodge2.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onKeyRegistry(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.DODGE_KEY);
        }
    }
}
