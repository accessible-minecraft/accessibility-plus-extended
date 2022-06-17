package net.shoaibkhan.accessibiltyplusextended.features.withThreads;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;

public class DurabilityThread extends Thread {
	private final MinecraftClient client;
	private double threshold;
	public static Integer[] thresholdArray = { 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60,
			65, 70, 75, 80, 85, 90, 95 };

	public DurabilityThread() {
		this.client = MinecraftClient.getInstance();
		threshold = 25;
	}

	public void run() {
		try {
			threshold = thresholdArray[Config.getInt(ConfigKeys.DURABILITY_THRESHOLD_KEY.getKey())];
		} catch (Exception e) {
			threshold = 25;
		}

		try {
			assert this.client.player != null;
			 PlayerInventory playerInventory = this.client.player.getInventory(); // post 1.17
//			PlayerInventory playerInventory = this.client.player.inventory;      // pre 1.17
			int size = playerInventory.size();
			for (int i = 0; i <= size; i++) {
				ItemStack itemStack = playerInventory.getStack(i);
				String name = itemStack.getName().getString();
				if (itemStack.isDamageable()) {
					String searchQuery = name + "\t" + itemStack;
					if (modInit.lowDurabilityItems.contains(searchQuery))
						break;
					double maxDamage = itemStack.getMaxDamage();
					double damage = itemStack.getDamage();
					double healthLeft = 100.00 - ((damage*100)/maxDamage);
					if (healthLeft <= threshold) {
//						this.client.player.sendMessage(Text.of(name + " durability is low"), true);
						NarratorPlus.narrate(I18n.translate("narrate.apextended.durability.warn", name));
						modInit.lowDurabilityItems.add(searchQuery);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
