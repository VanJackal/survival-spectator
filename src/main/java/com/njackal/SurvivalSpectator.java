package com.njackal;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.njackal.persistence.ComponentInit.PLAYER_SPEC;
import static net.minecraft.server.command.CommandManager.*;

public class SurvivalSpectator implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("SurvivalSpectator initializing");
		commandInit();
	}

	private void commandInit() {
		LOGGER.info("Initializing Commands");

		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> dispatcher.register(literal("c").executes(this::onCCommand))));
	}

	private int onCCommand(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		if(!source.isExecutedByPlayer()) {
			source.sendFeedback(() -> Text.literal("This command can only be executed by a player."),false);
			return 0;// exit early if the source isn't a player
		}

		var player = source.getPlayer();
		assert player != null;

		var specData = PLAYER_SPEC.get(player);
		if(!player.isSpectator()) {
			//get player details
			Vec3d pos =  player.getPos();
			GameMode gamemode = player.interactionManager.getGameMode();
			Identifier dim = source.getWorld().getRegistryKey().getValue();
			LOGGER.info(dim.toString());

			//save details, and switch mode
			specData.setData(pos, player.getPitch(), player.getYaw(), gamemode, dim);
			player.changeGameMode(GameMode.SPECTATOR);
		} else {
			//set gamemode to pre spectator mode
			player.changeGameMode(specData.getGameMode());

			//teleport back to start pos
			Vec3d targetPos = specData.getPosition();

			//dimension
			RegistryKey<World> dimension = RegistryKey.of(RegistryKeys.WORLD, specData.getDim());// ... half hour to find this code KEKW
			if(dimension != null) {
				player.teleport(source.getServer().getWorld(dimension), targetPos.x, targetPos.y, targetPos.z, Set.of(), specData.getYaw(),specData.getPitch(),false);
			} // default to just not teleporting if the dimension is somehow not found
		}

		return 1;
	}
}