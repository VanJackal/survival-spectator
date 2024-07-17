package com.njackal;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		if(!player.isSpectator()) {
			//todo save position (xyz, rotation, dim)
			//todo save gamemode
			GameMode gamemode = player.interactionManager.getGameMode();
			Vec3d pos =  player.getPos();

			player.changeGameMode(GameMode.SPECTATOR);
		} else {
			//todo put player back at old position
			//todo set to original gamemode
			Vec3d pos = PLAYER_SPEC.get(player).getPosition();
			LOGGER.info(pos.toString());
			player.changeGameMode(GameMode.SURVIVAL);
		}

		return 1;
	}
}