package fuzs.quickdodge.init;

import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.data.v2.AbstractDatapackRegistriesProvider;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.quickdodge.QuickDodge;
import fuzs.quickdodge.attachment.DodgeData;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;

public class ModRegistry {
    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder().add(Registries.ENCHANTMENT,
            ModRegistry::bootstrapEnchantments);
    static final RegistryManager REGISTRIES = RegistryManager.from(QuickDodge.MOD_ID);
    public static final Holder.Reference<Attribute> DODGE_STRENGTH_ATTRIBUTE = REGISTRIES.registerAttribute(
            "dodge_strength",
            0.0,
            0.0,
            1024.0);
    public static final Holder.Reference<SoundEvent> DODGE_SOUND_EVENT = REGISTRIES.registerSoundEvent("dodge");
    public static final Holder.Reference<DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>>> BASHING_DAMAGE_ENCHANTMENT_EFFECT_COMPONENT_TYPE = REGISTRIES.register(
            Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            "bashing_damage",
            () -> DataComponentType.<List<ConditionalEffect<EnchantmentValueEffect>>>builder()
                    .persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC,
                            LootContextParamSets.ENCHANTED_ENTITY).listOf())
                    .build());
    public static final Holder.Reference<DataComponentType<EnchantmentValueEffect>> ENTITY_BASHING_ENCHANTMENT_EFFECT_COMPONENT_TYPE = REGISTRIES.register(
            Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            "entity_bashing",
            () -> DataComponentType.<EnchantmentValueEffect>builder().persistent(EnchantmentValueEffect.CODEC).build());
    public static final Holder.Reference<DataComponentType<Unit>> DODGE_WHILST_AIRBORNE_ENCHANTMENT_EFFECT_COMPONENT_TYPE = REGISTRIES.register(
            Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            "dodge_whilst_airborne",
            () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).build());
    public static final ResourceKey<Enchantment> FLEETFOOT_ENCHANTMENT = REGISTRIES.registerEnchantment("fleetfoot");
    public static final ResourceKey<Enchantment> AIRSTRIDE_ENCHANTMENT = REGISTRIES.registerEnchantment("airstride");
    public static final ResourceKey<Enchantment> SHOCKSTEP_ENCHANTMENT = REGISTRIES.registerEnchantment("shockstep");

    public static final DataAttachmentType<Entity, DodgeData> DODGE_DATA_ATTACHMENT_TYPE = DataAttachmentRegistry.<DodgeData>entityBuilder()
            .defaultValue((Entity entity) -> entity.getType() == EntityType.PLAYER,
                    (RegistryAccess registries) -> new DodgeData())
            .build(QuickDodge.id("dodge_data"));

    public static void bootstrap() {
        // NO-OP
    }

    public static void bootstrapEnchantments(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> itemLookup = context.lookup(Registries.ITEM);
        AbstractDatapackRegistriesProvider.registerEnchantment(context,
                FLEETFOOT_ENCHANTMENT,
                Enchantment.enchantment(Enchantment.definition(itemLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                2,
                                3,
                                Enchantment.dynamicCost(5, 9),
                                Enchantment.dynamicCost(20, 9),
                                4,
                                EquipmentSlotGroup.FEET))
                        .withEffect(EnchantmentEffectComponents.ATTRIBUTES,
                                new EnchantmentAttributeEffect(getResourceLocation(FLEETFOOT_ENCHANTMENT),
                                        DODGE_STRENGTH_ATTRIBUTE,
                                        LevelBasedValue.perLevel(0.3F),
                                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));
        AbstractDatapackRegistriesProvider.registerEnchantment(context,
                AIRSTRIDE_ENCHANTMENT,
                Enchantment.enchantment(Enchantment.definition(itemLookup.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                                1,
                                1,
                                Enchantment.dynamicCost(25, 25),
                                Enchantment.dynamicCost(75, 25),
                                8,
                                EquipmentSlotGroup.LEGS))
                        .withEffect(DODGE_WHILST_AIRBORNE_ENCHANTMENT_EFFECT_COMPONENT_TYPE.value()));
        AbstractDatapackRegistriesProvider.registerEnchantment(context,
                SHOCKSTEP_ENCHANTMENT,
                Enchantment.enchantment(Enchantment.definition(itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                                10,
                                4,
                                Enchantment.dynamicCost(1, 10),
                                Enchantment.constantCost(50),
                                1,
                                EquipmentSlotGroup.CHEST))
                        .withSpecialEffect(ENTITY_BASHING_ENCHANTMENT_EFFECT_COMPONENT_TYPE.value(),
                                new AddValue(LevelBasedValue.perLevel(1.0F)))
                        .withEffect(BASHING_DAMAGE_ENCHANTMENT_EFFECT_COMPONENT_TYPE.value(),
                                new AddValue(LevelBasedValue.perLevel(4.0F, 2.0F))));
    }

    @Deprecated
    public static ResourceLocation getResourceLocation(ResourceKey<?> resourceKey) {
        return resourceKey.location().withPrefix(Registries.elementsDirPath(resourceKey.registryKey()) + ".");
    }
}
