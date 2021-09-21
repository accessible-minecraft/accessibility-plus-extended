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
import net.minecraft.util.math.BlockPos;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;
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
				assert client.world != null;
				if (Config.get(ConfigKeys.READ_BLOCKS_KEY.getKey())
						|| Config.get(ConfigKeys.READ_SIGNS_CONTENTS.getKey())) {
					BlockHitResult blockHitResult = (BlockHitResult) hit;
					BlockState blockState = client.world.getBlockState(blockHitResult.getBlockPos());
					Block block = blockState.getBlock();

					String name;
					MutableText blockMutableText = new LiteralText("").append(block.getName());
					name = blockMutableText.getString();

					String searchQuery = name + blockHitResult.getBlockPos();

					if (LockingHandler.lockedOnBlock != null)
						if (block == client.world.getBlockState(new BlockPos(LockingHandler.lockedOnBlock)).getBlock())
							if (blockState == client.world.getBlockState(new BlockPos(LockingHandler.lockedOnBlock)))
								break;

					if (!name.toLowerCase().contains("sign") && Config.get(ConfigKeys.READ_BLOCKS_KEY.getKey())) {
						if (!modInit.mainThreadMap.containsKey(searchQuery)) {
							text += name;

							String side;
							if (Config.get(ConfigKeys.NARRATE_BLOCK_SIDE_KEY.getKey())) {
								side = blockHitResult.getSide().asString();
								if (side.equalsIgnoreCase("up"))
									side = "top";
								if (side.equalsIgnoreCase("down"))
									side = "bottom";
								text += " " + side;
							}

							NarratorPlus.narrate(text);
							modInit.mainThreadMap.put(searchQuery, 5000);
						}
					}
					if (name.toLowerCase().contains("sign") && Config.get(ConfigKeys.READ_SIGNS_CONTENTS.getKey())) {
						if (!modInit.mainThreadMap.containsKey(searchQuery)) {
							String output = "";
							try {
								SignBlockEntity signentity = (SignBlockEntity) client.world
										.getBlockEntity(blockHitResult.getBlockPos());
								output += " says: ";

								// 1.17
								output += "1: " + signentity.getTextOnRow(0, false).getString() + ", ";
								output += "2: " + signentity.getTextOnRow(1, false).getString() + ", ";
								output += "3: " + signentity.getTextOnRow(2, false).getString() + ", ";
								output += "4: " + signentity.getTextOnRow(3, false).getString();

								// // 1.16
								// output += "1: " + signentity.getTextOnRow(0).getString() + ", ";
								// output += "2: " + signentity.getTextOnRow(1).getString() + ", ";
								// output += "3: " + signentity.getTextOnRow(2).getString() + ", ";
								// output += "4: " + signentity.getTextOnRow(3).getString();
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

				if (Config.get(ConfigKeys.ENTITY_NARRATOR_KEY.getKey())) {
					try {
						EntityHitResult entityHitResult = (EntityHitResult) hit;

						if (((EntityHitResult) hit).getEntity() == LockingHandler.lockedOnEntity)
							break;

						if (!modInit.mainThreadMap.containsKey("entity_narrator_key")) {
							MutableText entityMutableText = new LiteralText("")
									.append(entityHitResult.getEntity().getName());
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
