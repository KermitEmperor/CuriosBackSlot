package com.kermitemperor.curiosbackslot.capability;

import net.minecraft.nbt.CompoundTag;


import static com.kermitemperor.curiosbackslot.CuriosBackSlot.LOGGER;

public class XYZPosAndRotation {
    private CompoundTag XYZPosAndRotationDATA;


    public void set(CompoundTag tag) {
        this.XYZPosAndRotationDATA = tag;
    }

    public void setXYZPosAndRotationDATA(boolean third_p_render, float x, float y, float z, float xrot, float yrot, float zrot) {


        this.XYZPosAndRotationDATA.putBoolean("ThirdPersonRender", third_p_render);
        this.XYZPosAndRotationDATA.putFloat("X", x);
        this.XYZPosAndRotationDATA.putFloat("Y", y);
        this.XYZPosAndRotationDATA.putFloat("Z", z);
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


    public boolean isThirdPersonRender() {
        return this.XYZPosAndRotationDATA.getBoolean("ThirdPersonRender");
    }

    public float getX() {
        return this.XYZPosAndRotationDATA.getFloat("X");
    }

    public float getY() {
        return this.XYZPosAndRotationDATA.getFloat("Y");
    }

    public float getZ() {
        return this.XYZPosAndRotationDATA.getFloat("Z");
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
