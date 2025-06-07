package fuzs.quickdodge.client;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.entity.player.ClientPlayerNetworkEvents;
import fuzs.puzzleslib.api.client.event.v1.entity.player.MovementInputUpdateCallback;
import fuzs.puzzleslib.api.client.key.v1.KeyActivationHandler;
import fuzs.puzzleslib.api.client.key.v1.KeyMappingHelper;
import fuzs.quickdodge.QuickDodge;
import fuzs.quickdodge.client.gui.components.toasts.DodgingToast;
import fuzs.quickdodge.client.handler.MovementInputHandler;
import fuzs.quickdodge.client.handler.PlayerAnimationHandler;
import fuzs.quickdodge.config.ClientConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class QuickDodgeClient implements ClientModConstructor {
    public static final KeyMapping DODGE_KEY_MAPPING = KeyMappingHelper.registerKeyMapping(QuickDodge.id("dodge"),
            InputConstants.KEY_LALT);

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ClientTickEvents.END.register(DodgingToast::onEndClientTick);
        ClientPlayerNetworkEvents.LOGGED_IN.register(DodgingToast::onLoggedIn);
        MovementInputUpdateCallback.EVENT.register(MovementInputHandler::onMovementInputUpdate);
    }

    @Override
    public void onClientSetup() {
        PlayerAnimationHandler.registerAnimation();
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMapping(DODGE_KEY_MAPPING, KeyActivationHandler.forGame((Minecraft minecraft) -> {
            if (!QuickDodge.CONFIG.get(ClientConfig.class).doubleTapMode) {
                MovementInputHandler.singleTapHandler(minecraft.player);
            }
        }));
    }
}
