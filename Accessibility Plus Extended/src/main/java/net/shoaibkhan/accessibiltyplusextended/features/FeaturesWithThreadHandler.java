package net.shoaibkhan.accessibiltyplusextended.features;

import net.minecraft.client.MinecraftClient;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;
import net.shoaibkhan.accessibiltyplusextended.features.withThreads.DurabilityThread;
import net.shoaibkhan.accessibiltyplusextended.features.withThreads.FallDetectorThread;
import net.shoaibkhan.accessibiltyplusextended.features.withThreads.POIBlocks;
import net.shoaibkhan.accessibiltyplusextended.features.withThreads.POIEntities;

public class FeaturesWithThreadHandler {
	private static FallDetectorThread[] fallDetectorThreads = { new FallDetectorThread(), new FallDetectorThread(),
			new FallDetectorThread() };
	private static DurabilityThread durabilityThread = new DurabilityThread();
	private static POIEntities poiEntities = new POIEntities();
	private static POIBlocks poiBlocks = new POIBlocks();
	public static int fallDetectorFlag = 0;
	private final MinecraftClient client;

	public FeaturesWithThreadHandler(MinecraftClient client) {
		this.client = client;
		this.main();
	}

	private void main() {
		if (!client.isPaused() && (client.currentScreen == null)) {

			// Read Crosshair
			if (10000 - fallDetectorFlag >= 3000 && (Config.get(ConfigKeys.READ_BLOCKS_KEY.getKey()) || Config.get(ConfigKeys.ENTITY_NARRATOR_KEY.getKey()))) {
				new CrosshairTarget(client);
			}

			// Fall Detector
			if (Config.get(ConfigKeys.FALL_DETECTOR_KEY.getKey())) {
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


			// Durability Checker
			if (!modInit.mainThreadMap.containsKey("durablity_thread_key")
					&& Config.get(ConfigKeys.DURABILITY_CHECK_KEY.getKey())) {
				modInit.mainThreadMap.put("durablity_thread_key", 5000);
				if (durabilityThread.isAlive() && durabilityThread != null)
					durabilityThread.interrupt();
				durabilityThread = new DurabilityThread();
				durabilityThread.start();
			}

			// Point Of Interest
			if(Config.get(ConfigKeys.POI_KEY.getKey())){
				POIHandler.reInitialize();

				if(!poiEntities.running && Config.get(ConfigKeys.POI_ENTITY_KEY.getKey())){
					poiEntities = new POIEntities();
					poiEntities.start();
				}


				if(!poiBlocks.running && Config.get(ConfigKeys.POI_BLOCK_KEY.getKey())){
					poiBlocks = new POIBlocks();
					poiBlocks.start();
				}
			}
		}
	}
}
