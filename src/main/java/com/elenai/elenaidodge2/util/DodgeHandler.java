package com.elenai.elenaidodge2.util;

import com.elenai.elenaidodge2.client.animation.DodgeDirection;
import com.elenai.elenaidodge2.config.ED2ServerConfig;
import com.elenai.elenaidodge2.networking.ED2Messages;
import com.elenai.elenaidodge2.networking.messages.DodgeAnimationSTCPacket;
import com.elenai.elenaidodge2.networking.messages.DodgeEffectsCTSPacket;
import com.elenai.feathers.api.FeathersHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class DodgeHandler {
    public static int dodgingCooldown = 0;

    public static void handleDodge(LocalPlayer player, DodgeDirection dodgeDirection) {
        if (dodgingCooldown == 0 && (player.isOnGround() || ED2ServerConfig.DODGE_WHILST_AIRBORNE.get())) {
            if (!player.isPassenger() && !player.isShiftKeyDown() && !player.isUnderWater() && !player.isUsingItem() &&
                    !player.isFallFlying() && !player.getAbilities().flying && !player.isSleeping() &&
                    !player.isAutoSpinAttack()) {
                if (FeathersHelper.spendFeathers(ED2ServerConfig.DODGE_COST.get())) {

                    BlockPos blockPos = getBlockPosBelowThatAffectsMyMovement(player);
                    float blockFriction = player.level.getBlockState(blockPos)
                            .getFriction(player.level, blockPos, player);
                    float frictionInfluencedSpeed = getFrictionInfluencedSpeed(player, blockFriction);

                    Vec3 vec3 = getInputVector(new Vec3(dodgeDirection.getLeftImpulse(),
                                    0.0,
                                    dodgeDirection.getForwardImpulse()),
                            frictionInfluencedSpeed * ED2ServerConfig.DODGE_STRENGTH.get().floatValue(),
                            player.getYRot());
                    player.setDeltaMovement(player.getDeltaMovement()
                            .add(vec3.x(), ED2ServerConfig.DODGE_HEIGHT.get(), vec3.z));

                    DodgeAnimationSTCPacket.animatePlayer(dodgeDirection, player);

                    dodgingCooldown = ED2ServerConfig.COOLDOWN_TIME.get();
                    ED2Messages.sendToServer(new DodgeEffectsCTSPacket(dodgeDirection));
                }
            }
        }
    }

    /**
     * @see LivingEntity#getBlockPosBelowThatAffectsMyMovement()
     */
    protected static BlockPos getBlockPosBelowThatAffectsMyMovement(Entity entity) {
        return new BlockPos(entity.position().x(), entity.getBoundingBox().minY - 0.5, entity.position().z);
    }

    /**
     * @see LivingEntity#getFrictionInfluencedSpeed(float)
     */
    private static float getFrictionInfluencedSpeed(LivingEntity entity, float speedValue) {
        return entity.isOnGround() ? entity.getSpeed() * (0.216F / (speedValue * speedValue * speedValue)) :
                entity.flyingSpeed;
    }

    /**
     * @see Entity#getInputVector(Vec3, float, float)
     */
    private static Vec3 getInputVector(Vec3 moveVector, float surfaceFriction, float yRot) {
        double d0 = moveVector.lengthSqr();
        if (d0 < 1.0E-7D) {
            return Vec3.ZERO;
        } else {
            Vec3 vec3 = (d0 > 1.0D ? moveVector.normalize() : moveVector).scale(surfaceFriction);
            float f = Mth.sin(yRot * ((float) Math.PI / 180F));
            float f1 = Mth.cos(yRot * ((float) Math.PI / 180F));
            return new Vec3(vec3.x * (double) f1 - vec3.z * (double) f,
                    vec3.y,
                    vec3.z * (double) f1 + vec3.x * (double) f);
        }
    }
}
