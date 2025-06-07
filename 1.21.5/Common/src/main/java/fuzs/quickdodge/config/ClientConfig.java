package fuzs.quickdodge.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ClientConfig implements ConfigCore {
    @Config(description = "Enable this to allow for dodging by double tapping a movement key.")
    public boolean doubleTapMode = false;
    @Config(description = "How many ticks you have between double taps for them to register.")
    @Config.IntRange(min = 0)
    public int doubleTapTicks = 7;
    @Config(description = "Disable sprinting by double tapping the forward key. Sprinting can only be activated via the dedicated key.")
    public boolean preventDoubleTapSprinting = false;
}
