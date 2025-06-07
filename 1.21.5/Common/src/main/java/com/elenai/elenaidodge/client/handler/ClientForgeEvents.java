package com.elenai.elenaidodge.client.handler;

import com.elenai.elenaidodge.ElenaiDodge;
import com.elenai.elenaidodge.client.ElenaiDodgeClient;
import com.elenai.elenaidodge.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ElenaiDodge.MOD_ID, value = Dist.CLIENT)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void clientTickEvents(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft minecraft = Minecraft.getInstance();
            if (!minecraft.isPaused() && minecraft.player != null) {
                while (ElenaiDodgeClient.DODGE_KEY.consumeClick()) {
                    if (!ClientConfig.DOUBLE_TAP_MODE.get()) {
                        InputHandlers.singleTapHandler(minecraft.player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        InputHandlers.tick();
        if (ClientConfig.DOUBLE_TAP_MODE.get()) {
            InputHandlers.doubleTapInputHandler((LocalPlayer) event.getEntity());
        }
    }
}
