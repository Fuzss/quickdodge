package com.elenai.elenaidodge.client.handler;

import com.elenai.elenaidodge.client.ElenaiDodgeClient;
import com.elenai.elenaidodge.config.ClientConfig;
import com.elenai.elenaidodge.config.ServerConfig;
import com.elenai.elenaidodge.handler.CommonEventHandler;
import com.elenai.elenaidodge.networking.ED2Messages;
import com.elenai.elenaidodge.networking.messages.DodgeEffectsCTSPacket;
import com.elenai.elenaidodge.util.DodgeDirection;
import com.elenai.elenaidodge.util.EntityMovementHelper;
import com.elenai.feathers.api.FeathersHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class InputHandlers {
    private static final double MINIMUM_DODGING_IMPULSE_SQUARE = Math.pow(0.8, 2.0);

    @Nullable
    private static DodgeDirection lastDodgeDirection;
    private static int dodgeTriggerTime;
    private static boolean hasEnoughImpulseToStartDodging;
    private static int dodgingCooldown = 0;

    public static void singleTapHandler(LocalPlayer player) {
        DodgeDirection dodgeDirection = DodgeDirection.byMoveVector(player.input.getMoveVector(),
                DodgeDirection.FORWARD);
        handleDodge(player, dodgeDirection);
    }

    public static void doubleTapInputHandler(LocalPlayer player) {
        if (dodgeTriggerTime > 0) {
            dodgeTriggerTime--;
        }
        Vec2 moveVector = player.input.getMoveVector();
        if (moveVector.lengthSquared() >= MINIMUM_DODGING_IMPULSE_SQUARE) {
            if (!hasEnoughImpulseToStartDodging) {
                DodgeDirection dodgeDirection = DodgeDirection.byMoveVector(moveVector, null);
                if (dodgeDirection != null && lastDodgeDirection == dodgeDirection && dodgeTriggerTime > 0) {
                    handleDodge(player, dodgeDirection);
                } else {
                    lastDodgeDirection = dodgeDirection;
                    dodgeTriggerTime = ClientConfig.DOUBLE_TAP_TICKS.get();
                }
            }
            hasEnoughImpulseToStartDodging = true;
        } else {
            hasEnoughImpulseToStartDodging = false;
        }
        if (ClientConfig.PREVENT_DOUBLE_TAP_SPRINTING.get()) {
            // TODO access widener
//            player.sprintTriggerTime = 0;
        }
    }

    public static void tick() {
        if (dodgingCooldown > 0) {
            dodgingCooldown--;
        }
    }

    private static void handleDodge(LocalPlayer player, DodgeDirection dodgeDirection) {
        if (dodgingCooldown == 0 && isAbleToDodge(player)) {
            int dodgeCost =
                    player.isOnGround() ? ServerConfig.DODGE_COST.get() : ServerConfig.DODGE_COST_WHILST_AIRBORNE.get();
            if (FeathersHelper.spendFeathers(dodgeCost)) {

                Vec3 vec3 = getMoveVector(player, dodgeDirection);
                player.setDeltaMovement(player.getDeltaMovement()
                        .add(vec3.x(), ServerConfig.DODGE_HEIGHT.get(), vec3.z));

                dodgingCooldown = ServerConfig.COOLDOWN_TIME.get();
                CommonEventHandler.setDodgePose(player);

                ElenaiDodgeClient.animatePlayer(dodgeDirection, player);
                ED2Messages.sendToServer(new DodgeEffectsCTSPacket(dodgeDirection));
            }
        }
    }

    private static Vec3 getMoveVector(Player player, DodgeDirection dodgeDirection) {
        BlockPos blockPos = EntityMovementHelper.getBlockPosBelowThatAffectsMyMovement(player);
        float blockFriction = player.level.getBlockState(blockPos).getFriction(player.level, blockPos, player);
        float frictionInfluencedSpeed = EntityMovementHelper.getFrictionInfluencedSpeed(player, blockFriction);
        return EntityMovementHelper.getInputVector(new Vec3(dodgeDirection.getLeftImpulse(),
                        0.0,
                        dodgeDirection.getForwardImpulse()),
                frictionInfluencedSpeed * ServerConfig.DODGE_STRENGTH.get().floatValue(),
                player.getYRot());
    }

    private static boolean isAbleToDodge(Player player) {
        return (player.isOnGround() || ServerConfig.DODGE_WHILST_AIRBORNE.get()) && !player.isPassenger() &&
                !player.isShiftKeyDown() && !player.isUnderWater() && !player.isUsingItem() && !player.isFallFlying() &&
                !player.getAbilities().flying && !player.isSleeping() && !player.isAutoSpinAttack() &&
                !player.hasEffect(MobEffects.BLINDNESS);
    }
}
