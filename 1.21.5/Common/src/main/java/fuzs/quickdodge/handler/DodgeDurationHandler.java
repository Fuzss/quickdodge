package fuzs.quickdodge.handler;

import fuzs.quickdodge.config.ServerConfig;
import fuzs.quickdodge.init.ModRegistry;
import fuzs.quickdodge.QuickDodge;
import net.minecraft.world.entity.player.Player;

public class DodgeDurationHandler {
    /**
     * Don't set this for too long; so that the eye height changes back exactly when the dodge momentum stops. This is
     * shorter than the client animation (which is 15 ticks long), but this is arguably more important.
     */
    public static final int DODGE_DURATION_TICKS = 8;

    public static void onPlayerTickEnd(Player player) {
        if (ModRegistry.REMAINING_DODGE_TICKS_ATTACHMENT_TYPE.get(player).intValue() > 0) {
            ModRegistry.REMAINING_DODGE_TICKS_ATTACHMENT_TYPE.get(player).decrement();
        }
    }

    public static void setDodging(Player player) {
        if (QuickDodge.CONFIG.get(ServerConfig.class).shrinkSizeWhilstDodging) {
            ModRegistry.REMAINING_DODGE_TICKS_ATTACHMENT_TYPE.get(player).setValue(DODGE_DURATION_TICKS);
        }
    }

    public static boolean isDodging(Player player) {
        return ModRegistry.REMAINING_DODGE_TICKS_ATTACHMENT_TYPE.get(player).intValue() > 0;
    }
}
