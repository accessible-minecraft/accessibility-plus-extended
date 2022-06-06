package net.shoaibkhan.accessibiltyplusextended.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;

public class CrosshairTarget {
	private final MinecraftClient client;

	public CrosshairTarget(MinecraftClient client) {
		this.client = client;
		this.main();
	}

	private void main() {
		HitResult hit = client.crosshairTarget;
		if (hit == null)
		    return;
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

					String name = block.getName().getString();

					// Class name in production environment can be different
					String blockPos = blockHitResult.getBlockPos().toImmutable().toString();

					String searchQuery = name + blockPos;

					String blockEntries = blockState.getEntries() + "" + blockState.getBlock() + "" + blockPos;
					boolean isSign = blockState.isIn(BlockTags.SIGNS);

					if (!isSign && Config.get(ConfigKeys.READ_BLOCKS_KEY.getKey())) {
						if (!modInit.mainThreadMap.containsKey(searchQuery) && !blockEntries.equalsIgnoreCase(LockingHandler.lockedOnBlockEntries)) {
							text += name;

							if (Config.get(ConfigKeys.NARRATE_BLOCK_SIDE_KEY.getKey())) {
	                            Direction side = blockHitResult.getSide();
								text += " " + I18n.translate("narrate.apextended." + side.asString());
							}

							NarratorPlus.narrate(text);
							modInit.mainThreadMap.put(searchQuery, 5000);
						}
					}
					if (isSign && Config.get(ConfigKeys.READ_SIGNS_CONTENTS.getKey())) {
						if (!modInit.mainThreadMap.containsKey(searchQuery)) {
							String output = "";
							try {
								SignBlockEntity signentity = (SignBlockEntity) client.world
										.getBlockEntity(blockHitResult.getBlockPos());

								 // 1.17
								 output += "1: " + signentity.getTextOnRow(0, false).getString() + ", ";
								 output += "2: " + signentity.getTextOnRow(1, false).getString() + ", ";
								 output += "3: " + signentity.getTextOnRow(2, false).getString() + ", ";
								 output += "4: " + signentity.getTextOnRow(3, false).getString();

								// 1.16
//								output += "1: " + signentity.getTextOnRow(0).getString() + ", ";
//								output += "2: " + signentity.getTextOnRow(1).getString() + ", ";
//								output += "3: " + signentity.getTextOnRow(2).getString() + ", ";
//								output += "4: " + signentity.getTextOnRow(3).getString();
								 
								 output = I18n.translate("narrate.apextended.sign.says", output);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
                                if (!output.isEmpty())
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
							NarratorPlus.narrate(entityHitResult.getEntity().getName().getString());
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
