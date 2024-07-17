package com.njackal.persistence;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Vec3d;

public class PlayerSpectatorComponent implements IPlayerSpectatorComponent{
    private Vec3d position;

    public PlayerSpectatorComponent() {
        position = Vec3d.ZERO; // init pos to 0
    }

    @Override
    public void setData(Vec3d pos) {
        this.position = pos;
    }

    @Override
    public Vec3d getPosition() {
        return this.position;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        double x = tag.getDouble("x");
        double y = tag.getDouble("y");
        double z = tag.getDouble("z");
        this.position = new Vec3d(x, y, z);
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        //put pos
        tag.putDouble("x", this.position.x);
        tag.putDouble("y", this.position.y);
        tag.putDouble("z", this.position.z);
    }
}
