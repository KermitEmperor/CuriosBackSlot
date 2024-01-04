package com.kermitemperor.curiosbackslot.capability;

import net.minecraft.nbt.CompoundTag;


import static com.kermitemperor.curiosbackslot.CuriosBackSlot.LOGGER;

public class XYZPosAndRotation {
    private CompoundTag XYZPosAndRotationDATA;


    public void set(CompoundTag tag) {
        this.XYZPosAndRotationDATA = tag;
    }

    public void setXYZPosAndRotationDATA(double x, double y, double z, float xrot, float yrot, float zrot) {



        this.XYZPosAndRotationDATA.putDouble("X", x);
        this.XYZPosAndRotationDATA.putDouble("Y", y);
        this.XYZPosAndRotationDATA.putDouble("Z", z);
        this.XYZPosAndRotationDATA.putFloat("Xrot", xrot);
        this.XYZPosAndRotationDATA.putFloat("Yrot", yrot);
        this.XYZPosAndRotationDATA.putFloat("Zrot", zrot);


    }

    public void copyFrom(XYZPosAndRotation source) {
        this.XYZPosAndRotationDATA = source.XYZPosAndRotationDATA;
    }

    public void save(CompoundTag nbt) {
        nbt.put("XYZPosAndRotation", this.XYZPosAndRotationDATA);
    }

    public void load(CompoundTag nbt) {
        LOGGER.info(String.valueOf(nbt.getCompound("XYZPosAndRotation")));
        this.XYZPosAndRotationDATA = nbt.getCompound("XYZPosAndRotation");
    }


    public double getX() {
        return this.XYZPosAndRotationDATA.getDouble("X");
    }

    public double getY() {
        return this.XYZPosAndRotationDATA.getDouble("Y");
    }

    public double getZ() {
        return this.XYZPosAndRotationDATA.getDouble("Z");
    }

    public float getXrot() {
        return XYZPosAndRotationDATA.getFloat("Xrot");
    }

    public float getYrot() {
        return XYZPosAndRotationDATA.getFloat("Yrot");
    }

    public float getZrot() {
        return XYZPosAndRotationDATA.getFloat("Zrot");
    }

}
