package com.elenai.elenaidodge2.client.animation;

import com.elenai.elenaidodge2.ElenaiDodge2;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = ElenaiDodge2.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DodgeAnimator {
    static final ResourceLocation PLAYER_ASSOCIATED_DATA_LOCATION = ElenaiDodge2.id("animation");

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register(DodgeAnimator::registerPlayerAnimation));
    }

    private static void registerPlayerAnimation(AbstractClientPlayer player, AnimationStack stack) {
        ModifierLayer<IAnimation> layer = new ModifierLayer<>();
        stack.addAnimLayer(1024, layer);
        PlayerAnimationAccess.getPlayerAssociatedData(player).set(PLAYER_ASSOCIATED_DATA_LOCATION, layer);
    }

    @Nullable
    public static ModifierLayer<IAnimation> getAnimation(AbstractClientPlayer player) {
        return (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player)
                .get(PLAYER_ASSOCIATED_DATA_LOCATION);
    }
}
