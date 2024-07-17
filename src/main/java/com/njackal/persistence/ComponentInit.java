package com.njackal.persistence;


import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ComponentInit implements EntityComponentInitializer {

    public static final ComponentKey<IPlayerSpectatorComponent> PLAYER_SPEC =
            ComponentRegistry.getOrCreate(Identifier.of("survival-spectator","player_spectator"), IPlayerSpectatorComponent.class);

    public ComponentInit(){
        //no constructor since this is just a file for registry components
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PLAYER_SPEC, it -> new PlayerSpectatorComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }
}
