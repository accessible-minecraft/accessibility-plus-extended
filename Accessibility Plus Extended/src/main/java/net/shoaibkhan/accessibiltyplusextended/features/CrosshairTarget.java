package net.shoaibkhan.accessibiltyplusextended.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class CrosshairTarget {
	private final MinecraftClient client;

	public CrosshairTarget(MinecraftClient client) {
		this.client = client;
		this.main();
	}

	private void main() {
		HitResult hit = client.crosshairTarget;
		String text = "";
		switch (hit.getType()) {
		case MISS:
			break;
		case BLOCK:
			if (Config.get(Config.getNarrateblocksidekey()) || Config.get(Config.getReadSignsContents())) {
				BlockHitResult blockHitResult = (BlockHitResult) hit;
				BlockState blockState = client.world.getBlockState(blockHitResult.getBlockPos());
				Block block = blockState.getBlock();

				String name = "";
				MutableText blockMutableText = new LiteralText("").append(block.getName());
				name = blockMutableText.getString();

				String searchQuery = name + blockHitResult.getBlockPos();

				if(!(blockState + "").toLowerCase().contains("sign") && Config.get(Config.getReadblocksKey())) {
					if (!modInit.mainThreadMap.containsKey(searchQuery)) {
						text += name;

						String side = "";
						if(Config.get(Config.getNarrateblocksidekey())) {
							side = blockHitResult.getSide().asString();
							if (side.equalsIgnoreCase("up"))
								side = "top";
							if (side.equalsIgnoreCase("down"))
								side = "bottom";
							text += side;
						}

						NarratorPlus.narrate(text);
						modInit.mainThreadMap.put(searchQuery, 5000);
					}
				}
				if (blockState.toString().contains("sign") && Config.get(Config.getReadSignsContents())) {
						if (!modInit.mainThreadMap.containsKey(searchQuery)) {
							String output = "";
							try {
								SignBlockEntity signentity = (SignBlockEntity) client.world
										.getBlockEntity(blockHitResult.getBlockPos());
								output += " says: ";
								output += "1: " + signentity.getTextOnRow(0, false).getString() + ", ";
								output += "2: " + signentity.getTextOnRow(1, false).getString() + ", ";
								output += "3: " + signentity.getTextOnRow(2, false).getString() + ", ";
								output += "4: " + signentity.getTextOnRow(3, false).getString();
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								NarratorPlus.narrate(output);
								modInit.mainThreadMap.put(searchQuery, 10000);
							}
						}
				}
			}
			break;
		case ENTITY:

			if (Config.get(Config.getEntitynarratorkey())) {
				try {
					EntityHitResult entityHitResult = (EntityHitResult) hit;

					if (((EntityHitResult) hit).getEntity() == EntityLocking.lockedOnEntity) break;

					if (!modInit.mainThreadMap.containsKey("entity_narrator_key")) {
						MutableText entityMutableText = new LiteralText("") .append(entityHitResult.getEntity().getName());
						text = entityMutableText.getString();

						NarratorPlus.narrate(text);
						modInit.mainThreadMap.put("entity_narrator_key", 5000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}
}
