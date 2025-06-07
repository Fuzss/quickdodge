package fuzs.quickdodge.client.handler;

import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.quickdodge.QuickDodge;
import fuzs.quickdodge.config.ClientConfig;
import fuzs.quickdodge.config.ServerConfig;
import fuzs.quickdodge.handler.DodgeDurationHandler;
import fuzs.quickdodge.network.ServerboundTriggerDodgeMessage;
import fuzs.quickdodge.util.DodgeDirection;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class MovementInputHandler {
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

    public static void onMovementInputUpdate(LocalPlayer player, ClientInput clientInput) {
        if (dodgingCooldown > 0) {
            dodgingCooldown--;
        }
        if (QuickDodge.CONFIG.get(ClientConfig.class).doubleTapMode) {
            doubleTapInputHandler(player);
        }
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
                    dodgeTriggerTime = QuickDodge.CONFIG.get(ClientConfig.class).doubleTapTicks;
                }
            }
            hasEnoughImpulseToStartDodging = true;
        } else {
            hasEnoughImpulseToStartDodging = false;
        }
        if (QuickDodge.CONFIG.get(ClientConfig.class).preventDoubleTapSprinting) {
            player.sprintTriggerTime = 0;
        }
    }

    private static void handleDodge(LocalPlayer player, DodgeDirection dodgeDirection) {
        if (dodgingCooldown == 0 && isAbleToDodge(player)) {
            // TODO add back feathers and do this on the server
            int dodgeCost = QuickDodge.CONFIG.get(ServerConfig.class).getDodgeCost(player.onGround());

            Vec3 vec3 = getMoveVector(player, dodgeDirection);
            player.setDeltaMovement(player.getDeltaMovement()
                    .add(vec3.x(), QuickDodge.CONFIG.get(ServerConfig.class).dodgeHeight, vec3.z));

            dodgingCooldown = QuickDodge.CONFIG.get(ServerConfig.class).cooldownTime;
            DodgeDurationHandler.setDodging(player);

            PlayerAnimationHandler.animatePlayer(dodgeDirection, player);
            MessageSender.broadcast(new ServerboundTriggerDodgeMessage(dodgeDirection));
        }
    }

    private static Vec3 getMoveVector(LivingEntity livingEntity, DodgeDirection dodgeDirection) {
        BlockPos blockPos = livingEntity.getBlockPosBelowThatAffectsMyMovement();
        float blockFriction = livingEntity.level().getBlockState(blockPos).getBlock().getFriction();
        float frictionInfluencedSpeed = livingEntity.getFrictionInfluencedSpeed(blockFriction);
        return Entity.getInputVector(new Vec3(dodgeDirection.getLeftImpulse(), 0.0, dodgeDirection.getForwardImpulse()),
                frictionInfluencedSpeed * (float) QuickDodge.CONFIG.get(ServerConfig.class).dodgeStrength,
                livingEntity.getYRot());
    }

    private static boolean isAbleToDodge(Player player) {
        return (player.onGround() || QuickDodge.CONFIG.get(ServerConfig.class).dodgeWhilstAirborne) &&
                !player.isPassenger() && !player.isShiftKeyDown() && !player.isUnderWater() && !player.isUsingItem() &&
                !player.isFallFlying() && !player.getAbilities().flying && !player.isSleeping() &&
                !player.isAutoSpinAttack() && !player.hasEffect(MobEffects.BLINDNESS);
    }
}
