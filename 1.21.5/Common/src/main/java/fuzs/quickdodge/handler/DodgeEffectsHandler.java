package fuzs.quickdodge.handler;

import fuzs.quickdodge.QuickDodge;
import fuzs.quickdodge.attachment.DodgeData;
import fuzs.quickdodge.config.ServerConfig;
import fuzs.quickdodge.init.ModRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.mutable.MutableFloat;
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
    private static AABB boundingBoxBeforeBash;

    public static void onStartPlayerTick(Player player) {
        boundingBoxBeforeBash = player.getBoundingBox();
    }

    public static void onPlayerTickEnd(Player player) {
        DodgeData dodgeData = ModRegistry.DODGE_DATA_ATTACHMENT_TYPE.get(player);
        if (dodgeData.remainingDodgeTicks().intValue() > 0) {
            dodgeData.remainingDodgeTicks().decrement();
            if (boundingBoxBeforeBash != null) {
                checkBashAttack(player, boundingBoxBeforeBash, dodgeData);
            }
            if (dodgeData.remainingDodgeTicks().intValue() == 0) {
                dodgeData.bashedEntityIds().clear();
            }
        }
        boundingBoxBeforeBash = null;
    }

    /**
     * @see LivingEntity#checkAutoSpinAttack(AABB, AABB)
     */
    private static void checkBashAttack(Player player, AABB boundingBoxBeforeBash, DodgeData dodgeData) {
        AABB aABB = boundingBoxBeforeBash.minmax(player.getBoundingBox());
        // we need this on the client, so it has to be an unfiltered value
        int entityBashingBonus = (int) getEnchantmentValueEffectBonus(player,
                ModRegistry.ENTITY_BASHING_ENCHANTMENT_EFFECT_COMPONENT_TYPE.value());
        if (dodgeData.bashedEntityIds().size() < entityBashingBonus) {
            List<Entity> list = player.level().getEntities(player, aABB);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity instanceof LivingEntity && !dodgeData.bashedEntityIds().contains(entity.getId())) {
                        if (player.level() instanceof ServerLevel serverLevel) {
                            int bashingDamageBonus = (int) getEnchantmentValueEffectBonus(serverLevel,
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

    public static float getEnchantmentValueEffectBonus(ServerLevel serverLevel, LivingEntity livingEntity, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> componentType) {
        MutableFloat mutableFloat = new MutableFloat(0.0F);
        EnchantmentHelper.runIterationOnEquipment(livingEntity,
                (Holder<Enchantment> holder, int enchantmentLevel, EnchantedItemInUse enchantedItemInUse) -> {
                    Enchantment enchantment = holder.value();
                    enchantment.modifyEntityFilteredValue(componentType,
                            serverLevel,
                            enchantmentLevel,
                            enchantedItemInUse.itemStack(),
                            livingEntity,
                            mutableFloat);
                });
        return Math.max(0.0F, mutableFloat.floatValue());
    }

    public static float getEnchantmentValueEffectBonus(LivingEntity livingEntity, DataComponentType<EnchantmentValueEffect> componentType) {
        MutableFloat mutableFloat = new MutableFloat(0.0F);
        EnchantmentHelper.runIterationOnEquipment(livingEntity,
                (Holder<Enchantment> holder, int enchantmentLevel, EnchantedItemInUse enchantedItemInUse) -> {
                    Enchantment enchantment = holder.value();
                    enchantment.modifyUnfilteredValue(componentType,
                            livingEntity.getRandom(),
                            enchantmentLevel,
                            mutableFloat);
                });
        return Math.max(0.0F, mutableFloat.floatValue());
    }
}
