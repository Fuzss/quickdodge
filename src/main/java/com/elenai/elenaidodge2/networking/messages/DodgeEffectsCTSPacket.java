package com.elenai.elenaidodge2.networking.messages;

import com.elenai.elenaidodge2.client.animation.DodgeDirection;
import com.elenai.elenaidodge2.config.ED2ServerConfig;
import com.elenai.elenaidodge2.networking.ED2Messages;
import com.elenai.elenaidodge2.sound.ED2Sounds;
import net.minecraft.network.FriendlyByteBuf;
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
            context.getSender().level.playSound(null,
                    context.getSender(),
                    ED2Sounds.DODGE_SOUND.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    4.0F + context.getSender().level.random.nextFloat());

            context.getSender().invulnerableTime = ED2ServerConfig.INVINCIBILITY_TICKS.get();

            ED2Messages.sendToPlayersNearby(new DodgeAnimationSTCPacket(this.dodgeDirection,
                    context.getSender().getId()), context.getSender());

        });
        return true;
    }
}
