package com.kermitemperor.curiosbackslot.capability;

import net.minecraft.nbt.CompoundTag;


import static com.kermitemperor.curiosbackslot.CuriosBackSlot.LOGGER;

public class XYZPosAndRotation {

    //TODO fix this being null
    private CompoundTag XYZPosAndRotationDATA;

    public void setXYZPosAndRotationDATA(double x, double y, double z, float xrot, float yrot, float zrot) {
        this.XYZPosAndRotationDATA = new CompoundTag();



        this.XYZPosAndRotationDATA.putDouble("X", x);
        this.XYZPosAndRotationDATA.putDouble("Y", y);
        this.XYZPosAndRotationDATA.putDouble("Z", z);
        this.XYZPosAndRotationDATA.putFloat("Xrot", xrot);
        this.XYZPosAndRotationDATA.putFloat("Yrot", yrot);
        this.XYZPosAndRotationDATA.putFloat("Zrot", zrot);


    }

    public void addXYZPosAndRotationDATA(double x, double y, double z, float xrot, float yrot, float zrot) {

        addX(x);
        addY(y);
        addZ(z);
        addXrot(xrot);
        addYrot(yrot);
        addZrot(zrot);


    }

    public CompoundTag getXYZPosAndRotationDATA() {
        return this.XYZPosAndRotationDATA;
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



    public void addX(double x) {
        this.XYZPosAndRotationDATA.putDouble("X", getX() + x);
    }

    public void addY(double y) {
        this.XYZPosAndRotationDATA.putDouble("Y", getY() + y);
    }

    public void addZ(double z) {
        this.XYZPosAndRotationDATA.putDouble("Z", getZ() + z);
    }

    public void addXrot(float xrot) {
        this.XYZPosAndRotationDATA.putFloat("Xrot", getXrot() + xrot);
    }

    public void addYrot(float yrot) {
        this.XYZPosAndRotationDATA.putFloat("Yrot", getYrot() + yrot);
    }

    public void addZrot(float zrot) {
        this.XYZPosAndRotationDATA.putFloat("Zrot", getZrot() + zrot);
    }

    public void setX(double x) {
        this.XYZPosAndRotationDATA.putDouble("X", x);
    }

    public void setY(double y) {
        this.XYZPosAndRotationDATA.putDouble("Y", y);
    }

    public void setZ(double z) {
        this.XYZPosAndRotationDATA.putDouble("Z", z);
    }

    public void setXrot(float xrot) {
        this.XYZPosAndRotationDATA.putFloat("Xrot", xrot);
    }

    public void setYrot(float yrot) {
        this.XYZPosAndRotationDATA.putFloat("Yrot", yrot);
    }

    public void setZrot(float zrot) {
        this.XYZPosAndRotationDATA.putFloat("Zrot", zrot);
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
