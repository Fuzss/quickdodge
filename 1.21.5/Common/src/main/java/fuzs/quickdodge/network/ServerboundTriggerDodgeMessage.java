package fuzs.quickdodge.network;

import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.puzzleslib.api.network.v4.PlayerSet;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.quickdodge.QuickDodge;
import fuzs.quickdodge.config.ServerConfig;
import fuzs.quickdodge.handler.DodgeEffectsHandler;
import fuzs.quickdodge.init.ModRegistry;
import fuzs.quickdodge.util.DodgeDirection;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityEvent;

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

                if (QuickDodge.CONFIG.get(ServerConfig.class).dodgeSound) {
                    context.level()
                            .playSound(null,
                                    player,
                                    ModRegistry.DODGE_SOUND_EVENT.value(),
                                    SoundSource.PLAYERS,
                                    1.0F,
                                    4.0F + context.level().random.nextFloat());
                }
                if (QuickDodge.CONFIG.get(ServerConfig.class).poofParticles) {
                    context.level().broadcastEntityEvent(player, EntityEvent.POOF);
                }

                player.invulnerableTime = Math.max(player.invulnerableTime,
                        QuickDodge.CONFIG.get(ServerConfig.class).invincibilityTicks);
                DodgeEffectsHandler.setDodging(player);
                MessageSender.broadcast(PlayerSet.nearPlayer(player),
                        new ClientboundPlayDodgeAnimationMessage(player.getId(),
                                ServerboundTriggerDodgeMessage.this.dodgeDirection));
            }
        };
    }
}
