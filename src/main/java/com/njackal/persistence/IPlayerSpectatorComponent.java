package com.njackal.persistence;

import net.minecraft.util.math.Vec3d;
import org.ladysnake.cca.api.v3.component.Component;

public interface IPlayerSpectatorComponent extends Component {
    /**
     * set the data of for the component
     * @param pos Position of the player
     */
    void setData(Vec3d pos);

    /**
     * get the stored position
     */
    Vec3d getPosition();
}// todo add more components
