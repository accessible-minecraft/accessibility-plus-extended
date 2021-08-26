package net.shoaibkhan.accessibiltyplusextended.features;

import net.minecraft.client.MinecraftClient;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.features.withThreads.DurabilityThread;
import net.shoaibkhan.accessibiltyplusextended.features.withThreads.FallDetectorThread;
import net.shoaibkhan.accessibiltyplusextended.features.withThreads.OreDetectorThread;

public class FeaturesWithThreadHandler {
	private static FallDetectorThread[] fallDetectorThreads = { new FallDetectorThread(), new FallDetectorThread(),
			new FallDetectorThread() };
	private static OreDetectorThread[] oreDetectorThreads = { new OreDetectorThread(), new OreDetectorThread(),
			new OreDetectorThread() };
	private static DurabilityThread durabilityThread = new DurabilityThread();
	public static int fallDetectorFlag = 0;
	private MinecraftClient client;

	public FeaturesWithThreadHandler(MinecraftClient client) {
		this.client = client;
		this.main();
	}

	private void main() {
		if (!client.isPaused() && (client.currentScreen == null)) {

			// Read Crosshair
			if (10000 - fallDetectorFlag >= 3000 && (Config.get(Config.getReadblocksKey()) || Config.get(Config.getEntitynarratorkey()))) {
				new CrosshairTarget(client);
			}

			// Fall Detector
			if (Config.get(Config.getFalldetectorkey())) {
				for (int i = 0; i < fallDetectorThreads.length; i++) {
					if (!fallDetectorThreads[i].alive) {
						fallDetectorThreads[i].start();
					} else if (fallDetectorThreads[i].alive && fallDetectorThreads[i].finished) {
						fallDetectorThreads[i].interrupt();
						fallDetectorThreads[i] = new FallDetectorThread();
						fallDetectorThreads[i].start();
					}
				}
			}

			// Ore Detector
			if (Config.get(Config.getOredetectorkey()) || Config.get(Config.getLavadetectorkey())
					|| Config.get(Config.getWaterdetectorkey())) {
				for (int i = 0; i < oreDetectorThreads.length; i++) {
					if (!oreDetectorThreads[i].alive) {
						oreDetectorThreads[i].start();
					} else if (oreDetectorThreads[i].alive && oreDetectorThreads[i].finished) {
						oreDetectorThreads[i].interrupt();
						oreDetectorThreads[i] = new OreDetectorThread();
						oreDetectorThreads[i].start();
					}
				}
			}

			// Durability Checker
			if (!modInit.mainThreadMap.containsKey("durablity_thread_key")
					&& Config.get(Config.getDurabilitycheckerkey())) {
				modInit.mainThreadMap.put("durablity_thread_key", 5000);
				if (durabilityThread.isAlive() && durabilityThread != null)
					durabilityThread.interrupt();
				durabilityThread = new DurabilityThread();
				durabilityThread.start();
			}
		}
	}
}
