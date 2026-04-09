package com.njackal;

import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static com.njackal.persistence.ComponentInit.PLAYER_SPEC;

public class SurvivalSpectator implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	public static final String PERM_C = "survival-spectator.c";

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

		CommandRegistrationCallback.EVENT.register((
				(dispatcher, registryAccess, environment) ->
						dispatcher.register(
								Commands.literal("c")
										.requires(CommandSourceStack::isPlayer)
										.requires(source -> {
											MinecraftServer server = source.getServer();
											if (server != null && server.isDedicatedServer()){ //multiplayer
												LOGGER.debug("Dedicated server command called");
												return Permissions.check(source, PERM_C, true);
											} else {//singleplayer
												return true;
											}
										})
										.executes(this::onCCommand)
						)
				)
		);
	}

	private int onCCommand(CommandContext<CommandSourceStack> context) {
		var source = context.getSource();
		if(!source.isPlayer()) {
			source.sendSystemMessage(Component.literal("This command can only be executed by a player."));
			return 0;// exit early if the source isn't a player
		}

		var player = source.getPlayer();
		assert player != null;

		var specData = PLAYER_SPEC.get(player);
		if(!player.isSpectator()) {
			//get player details
			Vec3 pos =  player.position();
			GameType gamemode = player.gameMode();
			Identifier dim = source.getLevel().dimension().identifier();
			LOGGER.info(dim.toString());

			//save details, and switch mode
			Vec2 rot = player.getRotationVector();
			specData.setData(pos, rot.x, rot.y, gamemode, dim);
			player.setGameMode(GameType.SPECTATOR);
		} else {
			//set gamemode to pre spectator mode
			player.setGameMode(specData.getGameMode());

			//teleport back to start pos

			//dimension
			ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, specData.getDim());// ... half hour to find this code KEKW
			ServerLevel serverLevel = source.getServer().getLevel(dimension);
			if(serverLevel != null) {
				TeleportTransition transition = new TeleportTransition(
						serverLevel,
						specData.getPosition(),
						Vec3.ZERO,
						specData.getYaw(),
						specData.getPitch(),
						TeleportTransition.DO_NOTHING
				);
				player.teleport(transition);
			} // default to just not teleporting if the dimension is somehow not found
		}

		return 1;
	}
}