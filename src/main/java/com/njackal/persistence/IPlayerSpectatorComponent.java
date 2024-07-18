package com.njackal.persistence;

import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.ladysnake.cca.api.v3.component.Component;

public interface IPlayerSpectatorComponent extends Component {
    /**
     * set the data of for the component
     * @param pos Position of the player
     */
    void setData(Vec3d pos, float pitch, float yaw, GameMode gameMode);

    /**
     * get the stored position
     */
    Vec3d getPosition();

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
    GameMode getGameMode();
}// todo add more components
