package com.njackal.persistence;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.GameType;

import java.util.NoSuchElementException;

public class PlayerSpectatorComponent implements IPlayerSpectatorComponent{
    private Vec3 position;
    private float pitch;
    private float yaw;
    private GameType gameMode;
    private Identifier dim;

    public PlayerSpectatorComponent() {
        position = Vec3.ZERO; // init pos to 0
        this.pitch = 0f;
        this.yaw = 0f;
        this.gameMode = GameType.SURVIVAL;
        this.dim = Identifier.tryParse("minecraft:overworld");
    }

    @Override
    public void setData(Vec3 pos, float pitch, float yaw, GameType gameMode, Identifier dim) {
        this.position = pos;
        this.pitch = pitch;
        this.yaw = yaw;
        this.gameMode = gameMode;
        this.dim = dim;
    }

    @Override
    public Vec3 getPosition() {
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
    public GameType getGameMode() {
        return this.gameMode;
    }

    @Override
    public Identifier getDim() {
        return this.dim;
    }

    @Override
    public void readData(ValueInput readView) throws NoSuchElementException {
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
            this.gameMode = GameType.byId(readView.getString("gameMode", GameType.SURVIVAL.asString()));
            //dimension
            this.dim = Identifier.tryParse(readView.getString("dim","minecraft:overworld"));
        } catch (NoSuchElementException e) { // this is to allow the schema to change (may have some weird side effects if someone logs out in spectator before the update)
            return;
        }
    }

    @Override
    public void writeData(ValueOutput writeView) {
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
