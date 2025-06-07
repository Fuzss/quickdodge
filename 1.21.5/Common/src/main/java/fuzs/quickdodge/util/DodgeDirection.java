package fuzs.quickdodge.util;

import com.google.common.collect.ImmutableMap;
import fuzs.puzzleslib.api.network.v4.codec.ExtraStreamCodecs;
import fuzs.quickdodge.QuickDodge;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.Vec2;
import org.joml.Vector2i;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * Basically {@link net.minecraft.core.Direction8}.
 */
public enum DodgeDirection implements StringRepresentable {
    FORWARD(0, 1),
    BACKWARD(0, -1),
    LEFT(1, 0),
    RIGHT(-1, 0),
    FORWARD_LEFT(1, 1),
    FORWARD_RIGHT(-1, 1),
    BACKWARD_LEFT(1, -1),
    BACKWARD_RIGHT(-1, -1);

    public static final StringRepresentable.EnumCodec<DodgeDirection> CODEC = StringRepresentable.fromEnum(
            DodgeDirection::values);
    public static final StreamCodec<ByteBuf, DodgeDirection> STREAM_CODEC = ExtraStreamCodecs.fromEnum(DodgeDirection.class);
    static final Map<Vector2i, DodgeDirection> BY_MOVE_VECTOR = Arrays.stream(values())
            .collect(ImmutableMap.toImmutableMap(DodgeDirection::getMoveVector, Function.identity()));

    private final int leftImpulse;
    private final int forwardImpulse;
    public final ResourceLocation animationLocation;

    DodgeDirection(int leftImpulse, int forwardImpulse) {
        this.leftImpulse = leftImpulse;
        this.forwardImpulse = forwardImpulse;
        this.animationLocation = QuickDodge.id("dodge/" + this.getSerializedName());
    }

    public static DodgeDirection byMoveVector(Vec2 vec2, DodgeDirection fallback) {
        // we need a vector implementation that supports hashing
        return BY_MOVE_VECTOR.getOrDefault(new Vector2i(Mth.sign(vec2.x), Mth.sign(vec2.y)), fallback);
    }

    private Vector2i getMoveVector() {
        return new Vector2i(this.leftImpulse, this.forwardImpulse);
    }

    public double getLeftImpulse() {
        return this.leftImpulse;
    }

    public double getForwardImpulse() {
        return this.forwardImpulse;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
