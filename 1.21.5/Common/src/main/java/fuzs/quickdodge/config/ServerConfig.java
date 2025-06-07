package fuzs.quickdodge.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.quickdodge.handler.DodgeDurationHandler;

public class ServerConfig implements ConfigCore {
    @Config(description = "How many ticks it takes after dodging before you can dodge again.")
    @Config.IntRange(min = 0)
    public int cooldownTime = 20;
    @Config(description = "How many feathers it costs to dodge.")
    @Config.IntRange(min = 0)
    public int dodgeCost = 2;
    @Config(description = "How many ticks the player is invincible for after dodging.")
    public int invincibilityTicks = DodgeDurationHandler.DODGE_DURATION_TICKS;
    @Config(description = "Shrink the player size, allowing to pass through one block tall gaps via dodging.")
    public boolean shrinkSizeWhilstDodging = true;
    @Config(description = "How far the player moves when dodging.")
    @Config.DoubleRange(min = 0.0, max = 100.0)
    public double dodgeStrength = 10.0;
    @Config(description = "How high the player moves when dodging.")
    @Config.DoubleRange(min = 0.0, max = 10.0)
    public double dodgeHeight = 0.2;
    @Config(description = "Whether the player can dodge in mid-air.")
    public boolean dodgeWhilstAirborne = false;
    @Config(description = "How many feathers it costs to dodge while airborne")
    @Config.IntRange(min = 0)
    public int dodgeCostWhilstAirborne = 4;

    public int getDodgeCost(boolean onGround) {
        return onGround ? this.dodgeCost : this.dodgeCostWhilstAirborne;
    }
}
