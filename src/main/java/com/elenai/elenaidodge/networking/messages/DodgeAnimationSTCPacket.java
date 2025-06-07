package com.elenai.elenaidodge.networking.messages;

import com.elenai.elenaidodge.client.ElenaiDodgeClient;
import com.elenai.elenaidodge.util.DodgeDirection;
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
                ElenaiDodgeClient.animatePlayer(this.dodgeDirection, player);
            }
        });
        return true;
    }
}
