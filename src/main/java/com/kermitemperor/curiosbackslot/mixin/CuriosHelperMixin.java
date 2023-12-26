package com.kermitemperor.curiosbackslot.mixin;


import com.kermitemperor.curiosbackslot.util.CuriosBackSlotHandler;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;
import top.theillusivec4.curios.common.CuriosHelper;

@Mixin(CuriosHelper.class)
public abstract class CuriosHelperMixin implements ICuriosHelper {

    @Inject(method = "isStackValid", at = @At("RETURN"), cancellable = true, remap = false)
    public void onIsStackValid(SlotContext slotContext, ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (slotContext.identifier().equals(CuriosBackSlotHandler.SLOT_ID)) {
            info.setReturnValue(true);
        }
    }

}
