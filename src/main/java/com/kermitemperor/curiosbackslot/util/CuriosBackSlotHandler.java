package com.kermitemperor.curiosbackslot.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class CuriosBackSlotHandler {

    private static final Minecraft mc = Minecraft.getInstance();
    public static final String SLOT_ID = "back_weapon";

    private static ICurioStacksHandler getCurioStackHandler(LivingEntity player) {
        ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosInventory(player).orElseThrow(NullPointerException::new);
        return curiosItemHandler.getStacksHandler(SLOT_ID).orElseThrow(NullPointerException::new);
    }

    //this looks cursed
    private static ItemStack safelyGetCurioStack(LivingEntity player) {
        try {
            return getCurioStackHandler(player).getStacks().getStackInSlot(0);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }


    public static ItemStack getStackInSlotClient() {
        return safelyGetCurioStack(mc.player);
    }

    public static boolean renderItemInSlot(LivingEntity player) {
        boolean show;
        try {
            show = getCurioStackHandler(player).getRenders().get(0);
        } catch (Exception e) {
            show = false;
        }
        return show;
    }

    //FOR RENDER USE ONLY
    public static ItemStack getStackInSlot(LivingEntity player) {
        return safelyGetCurioStack(player);
    }

    //Must make an instance of this on serverside, otherwise null pointer exception will happen
    //Using CuriosBackSlotHandler on a server packet enqueue will produce errors
    public static class ServerCuriosBackSlotHandler {

        private final ICurioStacksHandler curioStacksHandler;

        public ServerCuriosBackSlotHandler(LivingEntity player) {
            ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosInventory(player).orElseThrow(NullPointerException::new);
            this.curioStacksHandler = curiosItemHandler.getStacksHandler(SLOT_ID).orElseThrow();
        }

        public ItemStack getStackInSlot() {
            return this.curioStacksHandler.getStacks().getStackInSlot(0);
        }

        public void setStackInSlot(ItemStack stack) {
            this.curioStacksHandler.getStacks().setStackInSlot(0, stack);
        }
    }
}
