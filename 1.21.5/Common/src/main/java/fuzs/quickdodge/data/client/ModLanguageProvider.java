package fuzs.quickdodge.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.quickdodge.QuickDodge;
import fuzs.quickdodge.client.QuickDodgeClient;
import fuzs.quickdodge.client.gui.components.toasts.DodgingToast;
import fuzs.quickdodge.init.ModRegistry;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.addKeyCategory(QuickDodge.MOD_ID, QuickDodge.MOD_NAME);
        translationBuilder.add(QuickDodgeClient.DODGE_KEY_MAPPING, "Dodge");
        translationBuilder.add(ModRegistry.DODGE_STRENGTH_ATTRIBUTE.value(), "Dodge Strength");
        translationBuilder.add(ModRegistry.DODGE_SOUND_EVENT.value(), "Player dodges");
        translationBuilder.addEnchantment(ModRegistry.FLEETFOOT_ENCHANTMENT, "Fleetfoot");
        translationBuilder.addEnchantment(ModRegistry.FLEETFOOT_ENCHANTMENT,
                "desc",
                "Increases the distance traveled when dodging.");
        translationBuilder.addEnchantment(ModRegistry.AIRSTRIDE_ENCHANTMENT, "Airstride");
        translationBuilder.addEnchantment(ModRegistry.AIRSTRIDE_ENCHANTMENT,
                "desc",
                "Allows dodging while in mid-air.");
        translationBuilder.addEnchantment(ModRegistry.SHOCKSTEP_ENCHANTMENT, "Shockstep");
        translationBuilder.addEnchantment(ModRegistry.SHOCKSTEP_ENCHANTMENT,
                "desc",
                "Damages mobs when dashing through them.");
        translationBuilder.add(DodgingToast.SINGLE_TAP_TITLE_COMPONENT, "Move through the world");
        translationBuilder.add(DodgingToast.SINGLE_TAP_DESCRIPTION_COMPONENT, "Dodge with %s");
        translationBuilder.add(DodgingToast.DOUBLE_TAP_TITLE_COMPONENT, "Get ready to dodge");
        translationBuilder.add(DodgingToast.DOUBLE_TAP_DESCRIPTION_COMPONENT, "Double tap %s, %s, %s or %s");
    }
}
