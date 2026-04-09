package com.njackal.persistence;

import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.GameType;
import org.ladysnake.cca.api.v3.component.Component;

public interface IPlayerSpectatorComponent extends Component {
    /**
     * set the data of for the component
     * @param pos Position of the player
     */
    void setData(Vec3 pos, float pitch, float yaw, GameType gameMode, Identifier dim);

    /**
     * get the stored position
     */
    Vec3 getPosition();

    /**
     * get the stored pitch
     */
    float getPitch();

    /**
     * get the stored yaw
     */
    float getYaw();

    /**
     * get the stored game mode
     */
    GameType getGameMode();

    /**
     * get the stored dimension
     */
    Identifier getDim();
}
