package fuzs.quickdodge.client.handler;

import fuzs.quickdodge.util.DodgeDirection;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import fuzs.quickdodge.QuickDodge;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

public class PlayerAnimationHandler {
    private static final ResourceLocation PLAYER_ASSOCIATED_DATA_LOCATION = QuickDodge.id("animation");

    public static void registerAnimation() {
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register((AbstractClientPlayer player, AnimationStack animationStack) -> {
            ModifierLayer<IAnimation> layer = new ModifierLayer<>();
            animationStack.addAnimLayer(1024, layer);
            PlayerAnimationAccess.getPlayerAssociatedData(player).set(PLAYER_ASSOCIATED_DATA_LOCATION, layer);
        });
    }

    @SuppressWarnings("unchecked")
    public static void animatePlayer(DodgeDirection dodgeDirection, AbstractClientPlayer player) {
        ModifierLayer<IAnimation> layer = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(
                player).get(PLAYER_ASSOCIATED_DATA_LOCATION);
        if (layer != null) {
            KeyframeAnimation animation = (KeyframeAnimation) PlayerAnimationRegistry.getAnimation(dodgeDirection.animationLocation);
            if (animation != null) {
                layer.setAnimation(new KeyframeAnimationPlayer(animation));
            }
        }
    }
}
