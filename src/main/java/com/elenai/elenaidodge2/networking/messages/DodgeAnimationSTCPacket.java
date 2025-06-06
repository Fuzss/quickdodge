package com.elenai.elenaidodge2.networking.messages;

import com.elenai.elenaidodge2.client.animation.DodgeDirection;
import com.elenai.elenaidodge2.client.animation.DodgeAnimator;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DodgeAnimationSTCPacket {
    private final DodgeDirection dodgeDirection;
    private final int entityId;

    public DodgeAnimationSTCPacket(DodgeDirection dodgeDirection, int entityId) {
        this.dodgeDirection = dodgeDirection;
        this.entityId = entityId;
    }

    public DodgeAnimationSTCPacket(FriendlyByteBuf buf) {
        this.dodgeDirection = buf.readEnum(DodgeDirection.class);
        this.entityId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(this.dodgeDirection);
        buf.writeInt(this.entityId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getEntity(this.entityId) instanceof AbstractClientPlayer player) {
                animatePlayer(this.dodgeDirection, player);
            }
        });
        return true;
    }

    public static void animatePlayer(DodgeDirection dodgeDirection, AbstractClientPlayer player) {
        ModifierLayer<IAnimation> layer = DodgeAnimator.getAnimation(player);
        if (layer != null) {
            KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(dodgeDirection.animationLocation);
            if (animation != null) {
                layer.setAnimation(new KeyframeAnimationPlayer(animation));
            }
        }
    }
}
