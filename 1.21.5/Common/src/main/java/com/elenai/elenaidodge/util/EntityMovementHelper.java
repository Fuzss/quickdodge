package com.elenai.elenaidodge.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * TODO replace this with access wideners
 */
@Deprecated
public class EntityMovementHelper {

    /**
     * @see LivingEntity#getBlockPosBelowThatAffectsMyMovement()
     */
    public static BlockPos getBlockPosBelowThatAffectsMyMovement(Entity entity) {
        return new BlockPos(entity.position().x(), entity.getBoundingBox().minY - 0.5, entity.position().z);
    }

    /**
     * @see LivingEntity#getFrictionInfluencedSpeed(float)
     */
    public static float getFrictionInfluencedSpeed(LivingEntity entity, float speedValue) {
        return entity.isOnGround() ? entity.getSpeed() * (0.216F / (speedValue * speedValue * speedValue)) :
                entity.flyingSpeed;
    }

    /**
     * @see Entity#getInputVector(Vec3, float, float)
     */
    public static Vec3 getInputVector(Vec3 moveVector, float surfaceFriction, float yRot) {
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
