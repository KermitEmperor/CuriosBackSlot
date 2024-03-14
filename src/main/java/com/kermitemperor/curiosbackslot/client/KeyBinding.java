package com.kermitemperor.curiosbackslot.client;

import com.kermitemperor.curiosbackslot.CuriosBackSlot;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.client.KeyRegistry;


public final class KeyBinding {
    private static final String KEY_SWITCH = "key." + CuriosBackSlot.MOD_ID + ".switch";
    public static final KeyMapping SWITCHING_KEY = new KeyMapping(KEY_SWITCH, GLFW.GLFW_KEY_V, KeyRegistry.openCurios.getCategory());

}
