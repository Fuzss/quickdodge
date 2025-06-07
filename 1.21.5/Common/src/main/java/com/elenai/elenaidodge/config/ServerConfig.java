package com.elenai.elenaidodge.config;

import com.elenai.elenaidodge.handler.CommonEventHandler;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> COOLDOWN_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> DODGE_COST;
    public static final ForgeConfigSpec.ConfigValue<Integer> INVINCIBILITY_TICKS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHRINK_SIZE_WHILST_DODGING;

    public static final ForgeConfigSpec.ConfigValue<Double> DODGE_STRENGTH;
    public static final ForgeConfigSpec.ConfigValue<Double> DODGE_HEIGHT;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DODGE_WHILST_AIRBORNE;
    public static final ForgeConfigSpec.ConfigValue<Integer> DODGE_COST_WHILST_AIRBORNE;

    //TODO: Add the tutorial back!
    static {

        COOLDOWN_TIME = BUILDER.comment("How many ticks it takes after dodging before you can dodge again.")
                .define("Dodge Cooldown", 20);

        INVINCIBILITY_TICKS = BUILDER.comment("How many ticks the player is invincible for after dodging.")
                .define("Invincibility Ticks", CommonEventHandler.DODGE_DURATION_TICKS);
        SHRINK_SIZE_WHILST_DODGING = BUILDER.comment(
                        "Shrink the player size, allowing to pass through one block tall gaps via dodging.")
                .define("Shrink Size Whilst Dodging", true);

        DODGE_COST = BUILDER.comment("How many half feathers it costs to dodge.").define("Dodge Cost", 2);

        DODGE_STRENGTH = BUILDER.comment("How far the player moves when dodging.").define("Dodge Strength", 10.0);
        DODGE_HEIGHT = BUILDER.comment("How high the player moves when dodging.").define("Dodge Height", 0.2);


        DODGE_WHILST_AIRBORNE = BUILDER.comment("Whether the player can dodge in mid-air.")
                .define("Dodge Whilst Airborne", false);
        DODGE_COST_WHILST_AIRBORNE = BUILDER.comment("How many half feathers it costs to dodge while airborne")
                .define("Dodge Cost Air", 4);

        SPEC = BUILDER.build();
    }
}
