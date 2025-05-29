package com.njackal.persistence;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

import java.util.NoSuchElementException;

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
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) throws NoSuchElementException {
        try {
            //position
            double x = tag.getDouble("x").get();
            double y = tag.getDouble("y").get();
            double z = tag.getDouble("z").get();
            this.position = new Vec3d(x, y, z);
            //rotation
            this.pitch = tag.getFloat("pitch").get();
            this.yaw = tag.getFloat("yaw").get();
            //gamemode
            this.gameMode = GameMode.byId(tag.getString("gameMode").get());
            //dimension
            this.dim = Identifier.tryParse(tag.getString("dim").get());
        } catch (NoSuchElementException e) { // this is to allow the schema to change (may have some weird side effects if someone logs out in spectator before the update)
            return;
        }
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
        tag.putString("mode", this.gameMode.getId());
        //dimension
        tag.putString("dim", this.dim.toString());
    }
}
