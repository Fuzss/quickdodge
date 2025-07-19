package fuzs.quickdodge.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.quickdodge.handler.DodgeEffectsHandler;

public class ServerConfig implements ConfigCore {
    @Config(description = "How many ticks it takes after dodging before you can dodge again.")
    @Config.IntRange(min = 0)
    public int cooldownTime = 20;
    @Config(description = "How many feathers it costs to dodge.")
    @Config.IntRange(min = 0)
    public int dodgeCost = 2;
    @Config(description = "How many feathers it costs to dodge while airborne")
    @Config.IntRange(min = 0)
    public int dodgeCostWhilstAirborne = 4;
    @Config(description = "How many ticks the player is invincible for after dodging.")
    public int invincibilityTicks = DodgeEffectsHandler.DODGE_ANIMATION_TICKS;
    @Config(description = "Shrink the player size, allowing to pass through one block tall gaps via dodging.")
    public boolean shrinkSizeWhilstDodging = true;
    @Config(description = "How high the player moves when dodging.")
    @Config.DoubleRange(min = 0.0, max = 16.0)
    public double dodgeHeight = 0.2;
    @Config(description = "Play a sweeping sound at the player position when dodging.")
    public boolean dodgeSound = true;
    @Config(description = "Spawn white particles at the player position when dodging.")
    public boolean poofParticles = true;
}
