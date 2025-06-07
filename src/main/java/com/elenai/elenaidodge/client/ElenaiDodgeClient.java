package com.elenai.elenaidodge.client;

import com.elenai.elenaidodge.ElenaiDodge;
import com.elenai.elenaidodge.util.DodgeDirection;
import com.mojang.blaze3d.platform.InputConstants;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ElenaiDodge.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ElenaiDodgeClient {
    public static final String KEY_CATEGORY = "key.category." + ElenaiDodge.MOD_ID + "." + ElenaiDodge.MOD_ID;
    public static final String KEY_DODGE = "key." + ElenaiDodge.MOD_ID + ".dodge";
    public static final KeyMapping DODGE_KEY = new KeyMapping(KEY_DODGE,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_LALT,
            KEY_CATEGORY);
    private static final ResourceLocation PLAYER_ASSOCIATED_DATA_LOCATION = ElenaiDodge.id("animation");

    @SubscribeEvent
    public static void onKeyRegistry(RegisterKeyMappingsEvent event) {
        event.register(DODGE_KEY);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register(ElenaiDodgeClient::registerPlayerAnimation));
    }

    private static void registerPlayerAnimation(AbstractClientPlayer player, AnimationStack stack) {
        ModifierLayer<IAnimation> layer = new ModifierLayer<>();
        stack.addAnimLayer(1024, layer);
        PlayerAnimationAccess.getPlayerAssociatedData(player).set(PLAYER_ASSOCIATED_DATA_LOCATION, layer);
    }

    @SuppressWarnings("unchecked")
    public static void animatePlayer(DodgeDirection dodgeDirection, AbstractClientPlayer player) {
        ModifierLayer<IAnimation> layer = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(
                player).get(PLAYER_ASSOCIATED_DATA_LOCATION);
        if (layer != null) {
            KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(dodgeDirection.animationLocation);
            if (animation != null) {
                layer.setAnimation(new KeyframeAnimationPlayer(animation));
            }
        }
    }
}
