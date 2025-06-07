package com.elenai.elenaidodge.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DOUBLE_TAP_MODE;
    public static final ForgeConfigSpec.ConfigValue<Integer> DOUBLE_TAP_TICKS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PREVENT_DOUBLE_TAP_SPRINTING;

    static {

        DOUBLE_TAP_MODE = BUILDER.comment("Enable this to allow for dodging by double tapping a movement key.")
                .define("Double Tap Mode", false);

        DOUBLE_TAP_TICKS = BUILDER.comment("How many ticks you have between double taps for them to register.")
                .define("Double Tap Ticks", 7);

        PREVENT_DOUBLE_TAP_SPRINTING = BUILDER.comment(
                        "Disable sprinting by double tapping the forward key. Sprinting can only be activated via the dedicated key.")
                .define("Prevent Double Tap Sprinting", false);

        SPEC = BUILDER.build();
    }
}
