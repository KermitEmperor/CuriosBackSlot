package com.kermitemperor.curiosbackslot.client;

import com.kermitemperor.curiosbackslot.CuriosBackSlot;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;


public final class KeyBinding {
    private static final String KEY_CATEGORY = "key.category." + CuriosBackSlot.MOD_ID + ".main";
    private static final String KEY_SWITCH = "key." + CuriosBackSlot.MOD_ID + ".switch";
    public static final KeyMapping SWITCHING_KEY = new KeyMapping(KEY_SWITCH, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY);

}
