package com.kermitemperor.curiosbackslot.mixin;

import com.kermitemperor.curiosbackslot.client.KeyBinding;
import com.kermitemperor.curiosbackslot.util.CuriosBackSlotHandler;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.common.inventory.CurioSlot;

@Mixin(CurioSlot.class)
public abstract class CurioSlotMixin extends SlotItemHandler {
    @Shadow @Final private String identifier;

    public CurioSlotMixin(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @OnlyIn(Dist.CLIENT)
    @Inject(method = "getSlotName", at=@At("TAIL"), cancellable = true, remap = false)
    public void onGetSlotName(CallbackInfoReturnable<String> info) {
        if (this.identifier.equals(CuriosBackSlotHandler.SLOT_ID)) {
            info.setReturnValue(I18n.get("curios.identifier." + this.identifier, KeyBinding.SWITCHING_KEY.getKey().getDisplayName().getString().toUpperCase()));
        }
    }

}
