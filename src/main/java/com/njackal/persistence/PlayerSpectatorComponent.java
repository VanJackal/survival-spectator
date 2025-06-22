package com.njackal.persistence;

import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
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
    public void readData(ReadView readView) throws NoSuchElementException {
        try {
            //position
            double x = readView.getDouble("x",0);
            double y = readView.getDouble("y",500);// kinda just hoping this is a safe fallback, generally these shouldn't be used though
            double z = readView.getDouble("z",0);
            this.position = new Vec3d(x, y, z);
            //rotation
            this.pitch = readView.getFloat("pitch",0);
            this.yaw = readView.getFloat("yaw",0);
            //gamemode
            this.gameMode = GameMode.byId(readView.getString("gameMode",GameMode.SURVIVAL.asString()));
            //dimension
            this.dim = Identifier.tryParse(readView.getString("dim","minecraft:overworld"));
        } catch (NoSuchElementException e) { // this is to allow the schema to change (may have some weird side effects if someone logs out in spectator before the update)
            return;
        }
    }

    @Override
    public void writeData(WriteView writeView) {
        //put pos
        writeView.putDouble("x", this.position.x);
        writeView.putDouble("y", this.position.y);
        writeView.putDouble("z", this.position.z);
        //rotation
        writeView.putFloat("pitch", this.pitch);
        writeView.putFloat("yaw", this.yaw);
        //gamemode
        writeView.putString("mode", this.gameMode.getId());
        //dimension
        writeView.putString("dim", this.dim.toString());
    }
}
