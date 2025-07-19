package fuzs.quickdodge.client.gui.components.toasts;

import fuzs.quickdodge.QuickDodge;
import fuzs.quickdodge.client.QuickDodgeClient;
import fuzs.quickdodge.config.ClientConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.neoforged.neoforge.common.ModConfigSpec;

public class DodgingToast extends TutorialToast {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/tutorial");
    private static final ResourceLocation DODGE_SPRITE = QuickDodge.id("toast/dodge");
    public static final Component SINGLE_TAP_TITLE_COMPONENT = Component.translatable("tutorial.single_tap_dodge.title");
    public static final Component SINGLE_TAP_DESCRIPTION_COMPONENT = Component.translatable(
            "tutorial.single_tap_dodge.description",
            Component.keybind(QuickDodgeClient.DODGE_KEY_MAPPING.getName()).withStyle(ChatFormatting.BOLD));
    public static final Component DOUBLE_TAP_TITLE_COMPONENT = Component.translatable("tutorial.double_tap_dodge.title");
    public static final Component DOUBLE_TAP_DESCRIPTION_COMPONENT = Component.translatable(
            "tutorial.double_tap_dodge.description",
            Tutorial.key("forward"),
            Tutorial.key("left"),
            Tutorial.key("back"),
            Tutorial.key("right"));

    private static int timeWaiting;

    public DodgingToast(Font font) {
        super(font, Icons.MOVEMENT_KEYS, getTitleComponent(), getDescriptionComponent(), false, 8000);
    }

    public static void onEndClientTick(Minecraft minecraft) {
        if (minecraft.level != null && !minecraft.isPaused()) {
            if (timeWaiting < 100 && ++timeWaiting == 100) {
                ModConfigSpec.BooleanValue displayTutorial = QuickDodge.CONFIG.get(ClientConfig.class).displayTutorial;
                if (displayTutorial.get()) {
                    minecraft.getToastManager().addToast(new DodgingToast(minecraft.font));
                    displayTutorial.set(false);
                    displayTutorial.save();
                }
            }
        }
    }

    public static void onLoggedIn(LocalPlayer player, MultiPlayerGameMode multiPlayerGameMode, Connection connection) {
        timeWaiting = 0;
    }

    private static Component getTitleComponent() {
        return QuickDodge.CONFIG.get(ClientConfig.class).doubleTapMode ? DOUBLE_TAP_TITLE_COMPONENT :
                SINGLE_TAP_TITLE_COMPONENT;
    }

    private static Component getDescriptionComponent() {
        return QuickDodge.CONFIG.get(ClientConfig.class).doubleTapMode ? DOUBLE_TAP_DESCRIPTION_COMPONENT :
                SINGLE_TAP_DESCRIPTION_COMPONENT;
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, long visibilityTime) {
        // copied from super method
        int i = this.height();
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, this.width(), i);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, DODGE_SPRITE, 6, 6, 20, 20);
        int j = this.lines.size() * 11;
        int k = 7 + (this.contentHeight() - j) / 2;

        for (int l = 0; l < this.lines.size(); l++) {
            guiGraphics.drawString(font, this.lines.get(l), 30, k + l * 11, ARGB.opaque(0), false);
        }
    }
}
