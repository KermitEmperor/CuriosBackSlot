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
        ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosHelper().getCuriosHandler(player).orElseThrow(NullPointerException::new);
        return curiosItemHandler.getStacksHandler(SLOT_ID).orElseThrow(NullPointerException::new);
    }

    //TODO AVOID TRY CATCH AND DO A PROPER HANDLER OF ILLEGAL PLAYER ENTITIES

    public static ItemStack getStackInSlotClient() {
        ItemStack stack;
        try {
            ICurioStacksHandler curioStacksHandler = getCurioStackHandler(mc.player);
            stack = curioStacksHandler.getStacks().getStackInSlot(0);
        } catch (Exception e) {
            stack = ItemStack.EMPTY;
        }
        return stack;
    }

    public static boolean renderItemInSlot(LivingEntity player) {
        boolean hide;
        try {
            hide = getCurioStackHandler(player).getRenders().get(0);
        } catch (Exception e) {
            hide = true;
        }
        return hide;
    }

    //FOR RENDER USE ONLY
    public static ItemStack getStackInSlot(LivingEntity player) {
        ItemStack stack;
        try {
            stack = getCurioStackHandler(player).getStacks().getStackInSlot(0);
        } catch (Exception e) {
            stack = ItemStack.EMPTY;
        }
        return stack;
    }

    //Must make an instance of this on serverside, otherwise null pointer exception will happen
    //Using CuriosBackSlotHandler on a server packet enqueue will produce errors
    public static class ServerCuriosBackSlotHandler {

        private final ICurioStacksHandler curioStacksHandler;

        public ServerCuriosBackSlotHandler(LivingEntity player) {
            ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosHelper().getCuriosHandler(player).orElseThrow(NullPointerException::new);
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
