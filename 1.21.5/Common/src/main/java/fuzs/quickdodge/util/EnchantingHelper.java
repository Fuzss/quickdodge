package fuzs.quickdodge.util;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;

@Deprecated
public final class EnchantingHelper {

    private EnchantingHelper() {
        // NO-OP
    }

    /**
     * @param itemStack     the item stack used as the enchantment holder
     * @param entity        the entity used as the enchantment holder
     * @param componentType the enchantment effect component type
     * @return the enchantment value effect bonus
     *
     * @see EnchantmentHelper#runIterationOnItem(ItemStack, EnchantmentHelper.EnchantmentVisitor)
     * @see Enchantment#modifyUnfilteredValue(DataComponentType, RandomSource, int, MutableFloat)
     */
    public static float getUnfilteredValueEffectBonus(ItemStack itemStack, Entity entity, DataComponentType<EnchantmentValueEffect> componentType) {
        MutableFloat mutableFloat = new MutableFloat(0.0F);
        EnchantmentHelper.runIterationOnItem(itemStack, (Holder<Enchantment> holder, int enchantmentLevel) -> {
            holder.value().modifyUnfilteredValue(componentType, entity.getRandom(), enchantmentLevel, mutableFloat);
        });
        return Math.max(0.0F, mutableFloat.floatValue());
    }

    /**
     * @param livingEntity  the entity used as the enchantment holder
     * @param componentType the enchantment effect component type
     * @return the enchantment value effect bonus
     *
     * @see EnchantmentHelper#runIterationOnEquipment(LivingEntity, EnchantmentHelper.EnchantmentInSlotVisitor)
     * @see Enchantment#modifyUnfilteredValue(DataComponentType, RandomSource, int, MutableFloat)
     */
    public static float getUnfilteredValueEffectBonus(LivingEntity livingEntity, DataComponentType<EnchantmentValueEffect> componentType) {
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

    /**
     * @param serverLevel   the server level for creating the loot context
     * @param itemStack     the item stack used as the enchantment holder
     * @param entity        the entity used as the enchantment holder
     * @param componentType the enchantment effect component type
     * @return the enchantment value effect bonus
     *
     * @see EnchantmentHelper#runIterationOnItem(ItemStack, EnchantmentHelper.EnchantmentVisitor)
     * @see Enchantment#modifyItemFilteredCount(DataComponentType, ServerLevel, int, ItemStack, MutableFloat)
     */
    public static float getItemFilteredValueEffectBonus(ServerLevel serverLevel, ItemStack itemStack, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> componentType) {
        MutableFloat mutableFloat = new MutableFloat(0.0F);
        EnchantmentHelper.runIterationOnItem(itemStack, (Holder<Enchantment> holder, int enchantmentLevel) -> {
            holder.value()
                    .modifyItemFilteredCount(componentType, serverLevel, enchantmentLevel, itemStack, mutableFloat);
        });
        return Math.max(0.0F, mutableFloat.floatValue());
    }

    /**
     * @param serverLevel   the server level for creating the loot context
     * @param livingEntity  the entity used as the enchantment holder
     * @param componentType the enchantment effect component type
     * @return the enchantment value effect bonus
     *
     * @see EnchantmentHelper#runIterationOnEquipment(LivingEntity, EnchantmentHelper.EnchantmentInSlotVisitor)
     * @see Enchantment#modifyItemFilteredCount(DataComponentType, ServerLevel, int, ItemStack, MutableFloat)
     */
    public static float getItemFilteredValueEffectBonus(ServerLevel serverLevel, LivingEntity livingEntity, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> componentType) {
        MutableFloat mutableFloat = new MutableFloat(0.0F);
        EnchantmentHelper.runIterationOnEquipment(livingEntity,
                (Holder<Enchantment> holder, int enchantmentLevel, EnchantedItemInUse enchantedItemInUse) -> {
                    holder.value()
                            .modifyItemFilteredCount(componentType,
                                    serverLevel,
                                    enchantmentLevel,
                                    enchantedItemInUse.itemStack(),
                                    mutableFloat);
                });
        return Math.max(0.0F, mutableFloat.floatValue());
    }

    /**
     * @param serverLevel   the server level for creating the loot context
     * @param itemStack     the item stack used as the enchantment holder
     * @param entity        the entity used as the enchantment holder
     * @param componentType the enchantment effect component type
     * @return the enchantment value effect bonus
     *
     * @see EnchantmentHelper#runIterationOnItem(ItemStack, EnchantmentHelper.EnchantmentVisitor)
     * @see Enchantment#modifyEntityFilteredValue(DataComponentType, ServerLevel, int, ItemStack, Entity,
     *         MutableFloat)
     */
    public static float getEntityFilteredValueEffectBonus(ServerLevel serverLevel, ItemStack itemStack, Entity entity, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> componentType) {
        MutableFloat mutableFloat = new MutableFloat(0.0F);
        EnchantmentHelper.runIterationOnItem(itemStack, (Holder<Enchantment> holder, int enchantmentLevel) -> {
            holder.value()
                    .modifyEntityFilteredValue(componentType,
                            serverLevel,
                            enchantmentLevel,
                            itemStack,
                            entity,
                            mutableFloat);
        });
        return Math.max(0.0F, mutableFloat.floatValue());
    }

    /**
     * @param serverLevel   the server level for creating the loot context
     * @param livingEntity  the entity used as the enchantment holder
     * @param componentType the enchantment effect component type
     * @return the enchantment value effect bonus
     *
     * @see EnchantmentHelper#runIterationOnEquipment(LivingEntity, EnchantmentHelper.EnchantmentInSlotVisitor)
     * @see Enchantment#modifyEntityFilteredValue(DataComponentType, ServerLevel, int, ItemStack, Entity,
     *         MutableFloat)
     */
    public static float getEntityFilteredValueEffectBonus(ServerLevel serverLevel, LivingEntity livingEntity, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> componentType) {
        MutableFloat mutableFloat = new MutableFloat(0.0F);
        EnchantmentHelper.runIterationOnEquipment(livingEntity,
                (Holder<Enchantment> holder, int enchantmentLevel, EnchantedItemInUse enchantedItemInUse) -> {
                    holder.value()
                            .modifyEntityFilteredValue(componentType,
                                    serverLevel,
                                    enchantmentLevel,
                                    enchantedItemInUse.itemStack(),
                                    livingEntity,
                                    mutableFloat);
                });
        return Math.max(0.0F, mutableFloat.floatValue());
    }

    /**
     * @param serverLevel   the server level for creating the loot context
     * @param itemStack     the item stack used as the enchantment holder
     * @param entity        the entity used as the enchantment holder
     * @param damageSource  the damage source
     * @param componentType the enchantment effect component type
     * @return the enchantment value effect bonus
     *
     * @see EnchantmentHelper#runIterationOnItem(ItemStack, EnchantmentHelper.EnchantmentVisitor)
     * @see Enchantment#modifyDamageFilteredValue(DataComponentType, ServerLevel, int, ItemStack, Entity,
     *         DamageSource, MutableFloat)
     */
    public static float getDamageFilteredValueEffectBonus(ServerLevel serverLevel, ItemStack itemStack, Entity entity, DamageSource damageSource, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> componentType) {
        MutableFloat mutableFloat = new MutableFloat(0.0F);
        EnchantmentHelper.runIterationOnItem(itemStack, (Holder<Enchantment> holder, int enchantmentLevel) -> {
            holder.value()
                    .modifyDamageFilteredValue(componentType,
                            serverLevel,
                            enchantmentLevel,
                            itemStack,
                            entity,
                            damageSource,
                            mutableFloat);
        });
        return Math.max(0.0F, mutableFloat.floatValue());
    }

    /**
     * @param serverLevel   the server level for creating the loot context
     * @param livingEntity  the entity used as the enchantment holder
     * @param damageSource  the damage source
     * @param componentType the enchantment effect component type
     * @return the enchantment value effect bonus
     *
     * @see EnchantmentHelper#runIterationOnEquipment(LivingEntity, EnchantmentHelper.EnchantmentInSlotVisitor)
     * @see Enchantment#modifyDamageFilteredValue(DataComponentType, ServerLevel, int, ItemStack, Entity,
     *         DamageSource, MutableFloat)
     */
    public static float getDamageFilteredValueEffectBonus(ServerLevel serverLevel, LivingEntity livingEntity, DamageSource damageSource, DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> componentType) {
        MutableFloat mutableFloat = new MutableFloat(0.0F);
        EnchantmentHelper.runIterationOnEquipment(livingEntity,
                (Holder<Enchantment> holder, int enchantmentLevel, EnchantedItemInUse enchantedItemInUse) -> {
                    holder.value()
                            .modifyDamageFilteredValue(componentType,
                                    serverLevel,
                                    enchantmentLevel,
                                    enchantedItemInUse.itemStack(),
                                    livingEntity,
                                    damageSource,
                                    mutableFloat);
                });
        return Math.max(0.0F, mutableFloat.floatValue());
    }
}
