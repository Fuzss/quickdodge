package fuzs.quickdodge.network;

import fuzs.quickdodge.config.ServerConfig;
import fuzs.quickdodge.handler.DodgeDurationHandler;
import fuzs.quickdodge.init.ModRegistry;
import fuzs.quickdodge.util.DodgeDirection;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.puzzleslib.api.network.v4.PlayerSet;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.quickdodge.QuickDodge;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

public record ServerboundTriggerDodgeMessage(DodgeDirection dodgeDirection) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundTriggerDodgeMessage> STREAM_CODEC = StreamCodec.composite(
            DodgeDirection.STREAM_CODEC,
            ServerboundTriggerDodgeMessage::dodgeDirection,
            ServerboundTriggerDodgeMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                ServerPlayer player = context.player();
                DodgeDurationHandler.setDodging(player);

                player.invulnerableTime = Math.max(player.invulnerableTime,
                        QuickDodge.CONFIG.get(ServerConfig.class).invincibilityTicks);
                player.level()
                        .playSound(null,
                                player,
                                ModRegistry.DODGE_SOUND_EVENT.value(),
                                SoundSource.PLAYERS,
                                1.0F,
                                4.0F + player.level().random.nextFloat());

                MessageSender.broadcast(PlayerSet.nearPlayer(player),
                        new ClientboundPlayDodgeAnimationMessage(player.getId(),
                                ServerboundTriggerDodgeMessage.this.dodgeDirection));
            }
        };
    }
}
