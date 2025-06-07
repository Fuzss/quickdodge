package com.elenai.elenaidodge.handler;

import com.elenai.elenaidodge.config.ServerConfig;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

public class CommonEventHandler {
    /**
     * Don't set this for too long; so that the eye height changes back exactly when the dodge momentum stops. This is
     * shorter than the client animation (which is 15 ticks long), but this is arguably more important.
     */
    public static final int DODGE_DURATION_TICKS = 8;
    /**
     * TODO make this a per-player attachment, not messing with capabilities on Forge lol
     */
    public static ThreadLocal<Integer> dodgeProgressTicks = ThreadLocal.withInitial(() -> 0);

    public static void onPlayerTickEnd(Player player) {
        if (CommonEventHandler.dodgeProgressTicks.get() > 0) {
            CommonEventHandler.dodgeProgressTicks.set(CommonEventHandler.dodgeProgressTicks.get() - 1);
            if (CommonEventHandler.dodgeProgressTicks.get() == 0) {
                player.setForcedPose(null);
            }
        }
    }

    public static void setDodgePose(Player player) {
        if (ServerConfig.SHRINK_SIZE_WHILST_DODGING.get()) {
            // we just need a pose that has a size of a 0.6-block cube,
            // but swimming and fall flying also trigger the swimming animation,
            // while we want to keep the standing model for our custom animation
            player.setForcedPose(Pose.SPIN_ATTACK);
            dodgeProgressTicks.set(DODGE_DURATION_TICKS);
        }
    }
}
