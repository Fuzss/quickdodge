package fuzs.quickdodge.util;

import com.google.common.collect.ImmutableMap;
import fuzs.quickdodge.QuickDodge;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.Vec2;
import org.joml.Vector2i;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;

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
    public static final IntFunction<DodgeDirection> BY_ID = ByIdMap.continuous(Enum::ordinal,
            values(),
            ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, DodgeDirection> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID,
            Enum::ordinal);
    static final Map<Vector2i, DodgeDirection> BY_MOVE_VECTOR = Arrays.stream(values())
            .collect(ImmutableMap.toImmutableMap(DodgeDirection::getMoveVector, Function.identity()));

    private final Vector2i moveVector;
    public final ResourceLocation animationLocation;

    DodgeDirection(int leftImpulse, int forwardImpulse) {
        this.moveVector = new Vector2i(leftImpulse, forwardImpulse);
        this.animationLocation = QuickDodge.id("dodge/" + this.getSerializedName());
    }

    public static DodgeDirection byMoveVector(Vec2 vec2, DodgeDirection fallback) {
        return BY_MOVE_VECTOR.getOrDefault(new Vector2i(Mth.sign(vec2.x), Mth.sign(vec2.y)), fallback);
    }

    private Vector2i getMoveVector() {
        return this.moveVector;
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
}
