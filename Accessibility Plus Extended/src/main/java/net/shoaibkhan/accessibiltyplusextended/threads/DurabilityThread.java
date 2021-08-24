package net.shoaibkhan.accessibiltyplusextended.threads;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class DurabilityThread extends Thread {
	private MinecraftClient client;
	private double threshold;
	public static String[] thresholdArray = { "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60",
			"65", "70", "75", "80", "85", "90", "95" };

	public DurabilityThread() {
		this.client = MinecraftClient.getInstance();
		threshold = 25;
	}

	public void run() {
		try {
			threshold = Integer.parseInt(thresholdArray[Config.getInt(Config.getDurabilitythresholdkey())] + "");
		} catch (Exception e) {
			threshold = 25;
		}

		try {
			PlayerInventory playerInventory = this.client.player.getInventory();
			int size = playerInventory.size();
			for (int i = 0; i <= size; i++) {
				ItemStack itemStack = playerInventory.getStack(i);
				MutableText mutableText = (new LiteralText("")).append(itemStack.getName());
				String name = mutableText.getString();
				if (!name.equalsIgnoreCase("air") && itemStack.isDamageable()) {
					String searchQuery = name + "\t" + itemStack;
					if (modInit.lowDurabilityItems.contains(searchQuery))
						break;
					double maxDamage = itemStack.getMaxDamage();
					double damage = itemStack.getDamage();
					double healthLeft = 100.00 - ((damage*100)/maxDamage);
					if (healthLeft <= threshold) {
						this.client.player.sendMessage(new LiteralText(name + " durability is low"), true);
						modInit.lowDurabilityItems.add(searchQuery);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
