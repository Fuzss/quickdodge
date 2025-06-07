package com.elenai.elenaidodge.networking.messages;

import com.elenai.elenaidodge.config.ServerConfig;
import com.elenai.elenaidodge.handler.CommonEventHandler;
import com.elenai.elenaidodge.init.ED2Sounds;
import com.elenai.elenaidodge.networking.ED2Messages;
import com.elenai.elenaidodge.util.DodgeDirection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DodgeEffectsCTSPacket {
    private final DodgeDirection dodgeDirection;

    public DodgeEffectsCTSPacket(DodgeDirection dodgeDirection) {
        this.dodgeDirection = dodgeDirection;
    }

    public DodgeEffectsCTSPacket(FriendlyByteBuf buf) {
        this.dodgeDirection = buf.readEnum(DodgeDirection.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(this.dodgeDirection);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            ServerPlayer player = context.getSender();
            CommonEventHandler.setDodgePose(player);

            player.invulnerableTime = Math.max(player.invulnerableTime, ServerConfig.INVINCIBILITY_TICKS.get());
            // TODO just use custom sword sweeping sound event, they sound the same
            player.level.playSound(null,
                    player,
                    ED2Sounds.DODGE_SOUND_EVENT.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    4.0F + player.level.random.nextFloat());

            ED2Messages.sendToPlayersNearby(new DodgeAnimationSTCPacket(this.dodgeDirection, player.getId()), player);

        });
        return true;
    }
}
