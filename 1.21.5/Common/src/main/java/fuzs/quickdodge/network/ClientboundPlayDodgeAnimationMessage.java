package fuzs.quickdodge.network;

import fuzs.quickdodge.client.handler.PlayerAnimationHandler;
import fuzs.quickdodge.util.DodgeDirection;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ClientboundPlayMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClientboundPlayDodgeAnimationMessage(int entityId,
                                                   DodgeDirection dodgeDirection) implements ClientboundPlayMessage {
    public static final StreamCodec<ByteBuf, ClientboundPlayDodgeAnimationMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ClientboundPlayDodgeAnimationMessage::entityId,
            DodgeDirection.STREAM_CODEC,
            ClientboundPlayDodgeAnimationMessage::dodgeDirection,
            ClientboundPlayDodgeAnimationMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.level()
                        .getEntity(ClientboundPlayDodgeAnimationMessage.this.entityId) instanceof AbstractClientPlayer player) {
                    PlayerAnimationHandler.animatePlayer(ClientboundPlayDodgeAnimationMessage.this.dodgeDirection,
                            player);
                }
            }
        };
    }
}
