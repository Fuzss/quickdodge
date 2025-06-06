package com.elenai.elenaidodge2.client.animation;

import com.elenai.elenaidodge2.ElenaiDodge2;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.Vec2;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public enum DodgeDirection implements StringRepresentable {
    LEFT(1, 0),
    RIGHT(-1, 0),
    FORWARD(0, 1),
    BACKWARD(0, -1),
    FORWARD_LEFT(1, 1),
    FORWARD_RIGHT(-1, 1),
    BACKWARD_LEFT(1, -1),
    BACKWARD_RIGHT(-1, -1);

    static final Map<MoveVector, DodgeDirection> BY_MOVE_VECTOR = Arrays.stream(values())
            .collect(ImmutableMap.toImmutableMap((DodgeDirection dodgeDirection) -> {
                return new MoveVector(dodgeDirection.moveVector);
            }, Function.identity()));

    private final Vec2 moveVector;
    public final ResourceLocation animationLocation;

    DodgeDirection(int leftImpulse, int forwardImpulse) {
        this.moveVector = new Vec2(leftImpulse, forwardImpulse);
        this.animationLocation = ElenaiDodge2.id("dodge/" + this.getSerializedName());
    }

    public static DodgeDirection byMoveVector(Vec2 moveVector, DodgeDirection fallback) {
        return BY_MOVE_VECTOR.getOrDefault(new MoveVector(moveVector), fallback);
    }

    public double getLeftImpulse() {
        return this.moveVector.x;
    }

    public double getForwardImpulse() {
        return this.moveVector.y;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    /**
     * TODO remove this in newer Minecraft versions where vanilla uses a vector implementation that supports hashing
     */
    private record MoveVector(double leftImpulse, double forwardImpulse) {

        public MoveVector(Vec2 moveVector) {
            this(Math.signum(moveVector.x), Math.signum(moveVector.y));
        }
    }
}
