package fuzs.quickdodge.data.client;

import fuzs.quickdodge.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.quickdodge.QuickDodge;
import fuzs.quickdodge.client.QuickDodgeClient;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.addKeyCategory(QuickDodge.MOD_ID, QuickDodge.MOD_NAME);
        translationBuilder.add(QuickDodgeClient.DODGE_KEY_MAPPING, "Dodge");
        translationBuilder.add(ModRegistry.DODGE_SOUND_EVENT.value(), "Player dodges");
    }
}
