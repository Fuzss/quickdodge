package fuzs.quickdodge.mixin;

import fuzs.quickdodge.handler.DodgeDurationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "getDesiredPose", at = @At("HEAD"), cancellable = true)
    private void getDesiredPose(CallbackInfoReturnable<Pose> callback) {
        if (DodgeDurationHandler.isDodging(Player.class.cast(this))) {
            // we just need a pose that has a size of a 0.6-block cube,
            // but swimming and fall flying also trigger the swimming animation,
            // while we want to keep the standing model for our custom animation
            callback.setReturnValue(Pose.SPIN_ATTACK);
        }
    }
}
