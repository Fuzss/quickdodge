package fuzs.quickdodge.fabric.client;

import fuzs.quickdodge.QuickDodge;
import fuzs.quickdodge.client.QuickDodgeClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class QuickDodgeFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(QuickDodge.MOD_ID, QuickDodgeClient::new);
    }
}
