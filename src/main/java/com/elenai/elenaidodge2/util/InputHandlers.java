package com.elenai.elenaidodge2.util;

import com.elenai.elenaidodge2.client.animation.DodgeDirection;
import com.elenai.elenaidodge2.config.ED2ClientConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;

public class InputHandlers {
    private static final double DODGING_IMPULSE_SQUARE = Math.pow(0.8, 2.0);
    @Nullable
    private static DodgeDirection lastDodgeDirection;
    private static int dodgeTriggerTime;
    private static boolean hasEnoughImpulseToStartDodging;

    public static void singleTapHandler(LocalPlayer player) {
        DodgeDirection dodgeDirection = DodgeDirection.byMoveVector(player.input.getMoveVector(),
                DodgeDirection.FORWARD);
        DodgeHandler.handleDodge(player, dodgeDirection);
    }

    public static void doubleTapInputHandler(LocalPlayer player) {
        if (dodgeTriggerTime > 0) {
            dodgeTriggerTime--;
        }
        Vec2 moveVector = player.input.getMoveVector();
        DodgeDirection dodgeDirection = DodgeDirection.byMoveVector(moveVector, null);
        if (moveVector.lengthSquared() >= DODGING_IMPULSE_SQUARE) {
            if (!hasEnoughImpulseToStartDodging) {
                if (dodgeDirection != null && lastDodgeDirection == dodgeDirection && dodgeTriggerTime > 0) {
                    DodgeHandler.handleDodge(player, dodgeDirection);
                } else {
                    lastDodgeDirection = dodgeDirection;
                    dodgeTriggerTime = ED2ClientConfig.DOUBLE_TAP_TICKS.get();
                }
            }
            hasEnoughImpulseToStartDodging = true;
        } else {
            hasEnoughImpulseToStartDodging = false;
        }
    }
}
