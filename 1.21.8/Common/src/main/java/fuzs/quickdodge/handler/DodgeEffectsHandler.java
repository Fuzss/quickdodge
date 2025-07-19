package fuzs.quickdodge.handler;

import fuzs.puzzleslib.api.item.v2.EnchantingHelper;
import fuzs.quickdodge.QuickDodge;
import fuzs.quickdodge.attachment.DodgeData;
import fuzs.quickdodge.config.ServerConfig;
import fuzs.quickdodge.init.ModRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DodgeEffectsHandler {
    /**
     * This is for how long the speed boost from dodging lasts; make it sync up with the pose change.
     */
    public static final int DODGE_MOMENTUM_TICKS = 8;
    /**
     * This is for how long the animation takes to play, used for invulnerability ticks.
     */
    public static final int DODGE_ANIMATION_TICKS = 15;

    @Nullable
    private static AABB originalBoundingBox;

    public static void onStartPlayerTick(Player player) {
        originalBoundingBox = player.getBoundingBox();
    }

    public static void onPlayerTickEnd(Player player) {
        DodgeData dodgeData = ModRegistry.DODGE_DATA_ATTACHMENT_TYPE.get(player);
        if (dodgeData.remainingDodgeTicks().intValue() > 0) {
            dodgeData.remainingDodgeTicks().decrement();
            if (originalBoundingBox != null) {
                checkBashAttack(player, originalBoundingBox, dodgeData);
            }
            if (dodgeData.remainingDodgeTicks().intValue() == 0) {
                dodgeData.bashedEntityIds().clear();
            }
        }
        originalBoundingBox = null;
    }

    /**
     * @see LivingEntity#checkAutoSpinAttack(AABB, AABB)
     */
    private static void checkBashAttack(Player player, AABB boundingBoxBeforeBash, DodgeData dodgeData) {
        AABB aABB = boundingBoxBeforeBash.minmax(player.getBoundingBox());
        // we need this on the client, so it has to be an unfiltered value
        int entityBashingBonus = (int) EnchantingHelper.getUnfilteredValueEffectBonus(player,
                ModRegistry.ENTITY_BASHING_ENCHANTMENT_EFFECT_COMPONENT_TYPE.value());
        if (dodgeData.bashedEntityIds().size() < entityBashingBonus) {
            List<Entity> list = player.level().getEntities(player, aABB);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity instanceof LivingEntity && !dodgeData.bashedEntityIds().contains(entity.getId())) {
                        if (player.level() instanceof ServerLevel serverLevel) {
                            int bashingDamageBonus = (int) EnchantingHelper.getEntityFilteredValueEffectBonus(
                                    serverLevel,
                                    player,
                                    ModRegistry.BASHING_DAMAGE_ENCHANTMENT_EFFECT_COMPONENT_TYPE.value());
                            attackEntityWithDamage(player, entity, bashingDamageBonus);
                        }
                        dodgeData.bashedEntityIds().add(entity.getId());
                        if (dodgeData.bashedEntityIds().size() == entityBashingBonus) {
                            player.setDeltaMovement(player.getDeltaMovement().scale(-0.2));
                            break;
                        }
                    }
                }
                return;
            }
        }
        if (player.horizontalCollision) {
            dodgeData.remainingDodgeTicks().setValue(0);
        }
    }

    private static void attackEntityWithDamage(Player player, Entity entity, int damageAmount) {
        boolean autoSpinAttack = player.isAutoSpinAttack();
        float autoSpinAttackDmg = player.autoSpinAttackDmg;
        ItemStack autoSpinAttackItemStack = player.autoSpinAttackItemStack;
        player.autoSpinAttackDmg = damageAmount;
        player.autoSpinAttackItemStack = ItemStack.EMPTY;
        player.setLivingEntityFlag(4, true);
        player.attack(entity);
        player.autoSpinAttackDmg = autoSpinAttackDmg;
        player.autoSpinAttackItemStack = autoSpinAttackItemStack;
        player.setLivingEntityFlag(4, autoSpinAttack);
    }

    public static void setDodging(Player player) {
        if (QuickDodge.CONFIG.get(ServerConfig.class).shrinkSizeWhilstDodging) {
            DodgeData dodgeData = ModRegistry.DODGE_DATA_ATTACHMENT_TYPE.get(player);
            dodgeData.remainingDodgeTicks().setValue(DODGE_MOMENTUM_TICKS);
        }
    }

    public static boolean isDodging(Player player) {
        DodgeData dodgeData = ModRegistry.DODGE_DATA_ATTACHMENT_TYPE.get(player);
        return dodgeData.remainingDodgeTicks().intValue() > 0;
    }
}
