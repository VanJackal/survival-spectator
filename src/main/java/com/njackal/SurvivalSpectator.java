package com.njackal;

import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
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
								literal("c")
										.requires(CommandSourceStack::isExecutedByPlayer)
										.requires(source -> {
											MinecraftServer server = source.getServer();
											if (server != null && server.isDedicated()){ //multiplayer
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
		if(!source.isExecutedByPlayer()) {
			source.sendFeedback(() -> Component.literal("This command can only be executed by a player."),false);
			return 0;// exit early if the source isn't a player
		}

		var player = source.getPlayer();
		assert player != null;

		var specData = PLAYER_SPEC.get(player);
		if(!player.isSpectator()) {
			//get player details
			Vec3 pos =  player.getEntityPos();
			GameType gamemode = player.interactionManager.getGameMode();
			Identifier dim = source.getWorld().getRegistryKey().getValue();
			LOGGER.info(dim.toString());

			//save details, and switch mode
			specData.setData(pos, player.getPitch(), player.getYaw(), gamemode, dim);
			player.changeGameMode(GameType.SPECTATOR);
		} else {
			//set gamemode to pre spectator mode
			player.changeGameMode(specData.getGameMode());

			//teleport back to start pos
			Vec3 targetPos = specData.getPosition();

			//dimension
			ResourceKey<Level> dimension = ResourceKey.of(Registries.WORLD, specData.getDim());// ... half hour to find this code KEKW
			if(dimension != null) {
				player.teleport(source.getServer().getWorld(dimension), targetPos.x, targetPos.y, targetPos.z, Set.of(), specData.getYaw(),specData.getPitch(),false);
			} // default to just not teleporting if the dimension is somehow not found
		}

		return 1;
	}
}