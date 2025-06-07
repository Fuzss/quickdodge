package fuzs.quickdodge;

import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.EntityAttributesContext;
import fuzs.puzzleslib.api.core.v1.context.PayloadTypesContext;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerTickEvents;
import fuzs.quickdodge.config.ClientConfig;
import fuzs.quickdodge.config.CommonConfig;
import fuzs.quickdodge.config.ServerConfig;
import fuzs.quickdodge.handler.DodgeEffectsHandler;
import fuzs.quickdodge.init.ModRegistry;
import fuzs.quickdodge.network.ClientboundPlayDodgeAnimationMessage;
import fuzs.quickdodge.network.ServerboundTriggerDodgeMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuickDodge implements ModConstructor {
    public static final String MOD_ID = "quickdodge";
    public static final String MOD_NAME = "Quick Dodge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID)
            .client(ClientConfig.class)
            .common(CommonConfig.class)
            .server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        PlayerTickEvents.START.register(DodgeEffectsHandler::onStartPlayerTick);
        PlayerTickEvents.END.register(DodgeEffectsHandler::onPlayerTickEnd);
    }

    @Override
    public void onRegisterPayloadTypes(PayloadTypesContext context) {
        context.playToClient(ClientboundPlayDodgeAnimationMessage.class,
                ClientboundPlayDodgeAnimationMessage.STREAM_CODEC);
        context.playToServer(ServerboundTriggerDodgeMessage.class, ServerboundTriggerDodgeMessage.STREAM_CODEC);
    }

    @Override
    public void onRegisterEntityAttributes(EntityAttributesContext context) {
        context.registerAttribute(EntityType.PLAYER,
                ModRegistry.DODGE_STRENGTH_ATTRIBUTE,
                QuickDodge.CONFIG.get(CommonConfig.class).dodgeStrength);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
