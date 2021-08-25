package net.shoaibkhan.accessibiltyplusextended.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class CrosshairTarget {
	private MinecraftClient client;
	private String tempBlock = "", tempBlockPos = "";
	private String tempEntity = "", tempEntityPos = "";
	
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
			if (Config.get(Config.getReadblockskey())) {
				BlockHitResult blockHitResult = (BlockHitResult) hit;
				BlockState blockState = client.world.getBlockState(blockHitResult.getBlockPos());
				Block block = blockState.getBlock();
				if ((!tempBlock.equalsIgnoreCase(block + "")
						|| !(tempBlockPos.equalsIgnoreCase(blockHitResult.getBlockPos() + "")))
						&& !(blockState + "").toLowerCase().contains("sign")) {
					tempBlock = block + "";
					tempBlockPos = blockHitResult.getBlockPos() + "";
					tempEntityPos = "";
					tempEntity = "";
					String side = blockHitResult.getSide().asString();
					String name = "";
					MutableText blockMutableText = new LiteralText("").append(block.getName());
					name = blockMutableText.getString();
					if (side.equalsIgnoreCase("up"))
						side = "top";
					if (side.equalsIgnoreCase("down"))
						side = "bottom";
					text = name + " " + side;
					narrate(text);
				}
			}
			break;
		case ENTITY:

			if (Config.get(Config.getEntitynarratorkey())) {
				try {
					EntityHitResult entityHitResult = (EntityHitResult) hit;

					if (((EntityHitResult) hit).getEntity() == EntityLocking.lockedOnEntity)
						break;

					if ((!(((EntityHitResult) hit).getEntity().getDisplayName() + "").equalsIgnoreCase(tempEntity)
							|| !(((EntityHitResult) hit).hashCode() + "").equalsIgnoreCase(tempEntityPos))
							&& !modInit.mainThreadMap.containsKey("entity_narrator_key")) {

						tempEntity = ((EntityHitResult) hit).getEntity().getType() + "";
						tempEntityPos = ((EntityHitResult) hit).hashCode() + "";
						tempBlockPos = "";
						tempBlock = "";
						MutableText entityMutableText = new LiteralText("")
								.append(entityHitResult.getEntity().getName());
						text = entityMutableText.getString();
						narrate(text);
						modInit.mainThreadMap.put("entity_narrator_key", 5000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}

	private void narrate(String st) {
		client.player.sendMessage(new LiteralText(st), true);
	}

}
