package edu.byu.minecraft;

import edu.byu.minecraft.shopkeepers.command.Commands;
import edu.byu.minecraft.shopkeepers.data.SaveData;
import edu.byu.minecraft.shopkeepers.lock.InteractionLocks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shopkeepers implements ModInitializer {
	public static final String MOD_ID = "shopkeepers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static SaveData data;

	private static final InteractionLocks interactionLocks = new InteractionLocks();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		CommandRegistrationCallback.EVENT.register(Commands::register);
		ServerLifecycleEvents.SERVER_STARTED.register(this::serverStarted);
	}

	private void serverStarted(MinecraftServer server) {
		data = SaveData.getServerState(server);
	}

	public static SaveData getData() {
		return data;
	}

	public static InteractionLocks getInteractionLocks() {
		return interactionLocks;
	}
}