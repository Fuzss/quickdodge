package fuzs.quickdodge.fabric;

import fuzs.quickdodge.QuickDodge;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class QuickDodgeFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(QuickDodge.MOD_ID, QuickDodge::new);
    }
}
