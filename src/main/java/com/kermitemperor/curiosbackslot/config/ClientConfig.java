package com.kermitemperor.curiosbackslot.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> X_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<Integer> Y_OFFSET;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISALLOW_CURIO_ITEMS;

    static {
        BUILDER.push("Configs for Curios Back Weapon Slot");


        SHOW = BUILDER
                .comment("Whetever to show the weapon back slot or not beside the hotbar")
                .define("show_back_weapon", true);

        X_OFFSET = BUILDER
                .comment("Move the weapon back slot (the one that is near the hotbar) along the X axis")
                .define("x_offset", 0);

        Y_OFFSET = BUILDER
                .comment("Move the weapon back slot (the one that is near the hotbar) along the Y axis")
                .define("Y_offset", 0);
        DISALLOW_CURIO_ITEMS = BUILDER
                .comment("Disallow items with designated curio slots to be placed in here")
                .define("disallow_curio_items", true);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
