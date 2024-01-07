package com.kermitemperor.curiosbackslot.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.kermitemperor.curiosbackslot.CuriosBackSlot.LOGGER;

public class XYZPosAndRotationProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<XYZPosAndRotation> PLAYER_BACK_WEAPON_XYZ = CapabilityManager.get(new CapabilityToken<>() {});
    private XYZPosAndRotation XYZPosAndRotationDATA;
    private final LazyOptional<XYZPosAndRotation> optional = LazyOptional.of(this::createPlayerXYZPosAndRotationDATA);
    private XYZPosAndRotation createPlayerXYZPosAndRotationDATA() {
        if (this.XYZPosAndRotationDATA == null) {
            this.XYZPosAndRotationDATA = new XYZPosAndRotation();
            this.XYZPosAndRotationDATA.set(new CompoundTag());
            this.XYZPosAndRotationDATA.setXYZPosAndRotationDATA(false,0,0,0,0,0,0);
            LOGGER.info("created data");
        }

        return this.XYZPosAndRotationDATA;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_BACK_WEAPON_XYZ) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerXYZPosAndRotationDATA().save(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerXYZPosAndRotationDATA().load(nbt);
    }
}
