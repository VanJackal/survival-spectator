package com.njackal.persistence;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class PlayerSpectatorComponent implements IPlayerSpectatorComponent{
    private Vec3d position;
    private float pitch;
    private float yaw;
    private GameMode gameMode;
    private Identifier dim;

    public PlayerSpectatorComponent() {
        position = Vec3d.ZERO; // init pos to 0
        this.pitch = 0f;
        this.yaw = 0f;
        this.gameMode = GameMode.SURVIVAL;
        this.dim = Identifier.tryParse("minecraft:overworld");
    }

    @Override
    public void setData(Vec3d pos, float pitch, float yaw, GameMode gameMode, Identifier dim) {
        this.position = pos;
        this.pitch = pitch;
        this.yaw = yaw;
        this.gameMode = gameMode;
        this.dim = dim;
    }

    @Override
    public Vec3d getPosition() {
        return this.position;
    }

    @Override
    public float getPitch() {
        return this.pitch;
    }

    @Override
    public float getYaw() {
        return this.yaw;
    }

    @Override
    public GameMode getGameMode() {
        return this.gameMode;
    }

    @Override
    public Identifier getDim() {
        return this.dim;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        //position
        double x = tag.getDouble("x");
        double y = tag.getDouble("y");
        double z = tag.getDouble("z");
        this.position = new Vec3d(x, y, z);
        //rotation
        this.pitch = tag.getFloat("pitch");
        this.yaw = tag.getFloat("yaw");
        //gamemode
        this.gameMode = GameMode.byId(tag.getInt("gameMode"));
        //dimension
        this.dim = Identifier.tryParse(tag.getString("dim"));
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        //put pos
        tag.putDouble("x", this.position.x);
        tag.putDouble("y", this.position.y);
        tag.putDouble("z", this.position.z);
        //rotation
        tag.putFloat("pitch", this.pitch);
        tag.putFloat("yaw", this.yaw);
        //gamemode
        tag.putInt("mode", this.gameMode.getId());
        //dimension
        tag.putString("dim", this.dim.toString());
    }
}
