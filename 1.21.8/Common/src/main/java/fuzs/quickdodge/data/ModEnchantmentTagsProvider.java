package fuzs.quickdodge.data;

import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import fuzs.quickdodge.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantmentTagsProvider extends AbstractTagProvider<Enchantment> {

    public ModEnchantmentTagsProvider(DataProviderContext context) {
        super(Registries.ENCHANTMENT, context);
    }

    @Override
    public void addTags(HolderLookup.Provider registries) {
        this.tag(EnchantmentTags.NON_TREASURE)
                .addKey(ModRegistry.FLEETFOOT_ENCHANTMENT,
                        ModRegistry.AIRSTRIDE_ENCHANTMENT,
                        ModRegistry.SHOCKSTEP_ENCHANTMENT);
    }
}
