package net.shoaibkhan.accessibiltyplusextended.features.withThreads;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;

public class FallDetectorThread extends Thread {

	public boolean alive = false, finished = false;
	private MinecraftClient client;
	public static Integer[] range = { 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
	public static Integer[] depthArray = { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };

	public void run() {
		alive = true;
		client = MinecraftClient.getInstance();
		BlockPos pos = client.player.getBlockPos();
		int posX = pos.getX();
		int posY = pos.getY() - 1;
		int posZ = pos.getZ();

		int rangeVal = 10;
		try {
			rangeVal = range[Config.getInt(ConfigKeys.FALL_DETECTOR_RANGE_KEY.getKey())];
			rangeVal = rangeVal - 1;
		} catch (Exception e) {
			rangeVal = 10;
		}

		Direction dir = client.player.getHorizontalFacing();
		if (dir == Direction.NORTH) {
			checkBlock(new BlockPos(new Vec3d(posX - 1, posY, posZ - 1)), dir, rangeVal);
			checkBlock(new BlockPos(new Vec3d(posX, posY, posZ - 1)), dir, rangeVal);
			checkBlock(new BlockPos(new Vec3d(posX + 1, posY, posZ - 1)), dir, rangeVal);
		} else if (dir == Direction.SOUTH) {
			checkBlock(new BlockPos(new Vec3d(posX - 1, posY, posZ + 1)), dir, rangeVal);
			checkBlock(new BlockPos(new Vec3d(posX, posY, posZ + 1)), dir, rangeVal);
			checkBlock(new BlockPos(new Vec3d(posX + 1, posY, posZ + 1)), dir, rangeVal);
		} else if (dir == Direction.EAST) {
			checkBlock(new BlockPos(new Vec3d(posX + 1, posY, posZ - 1)), dir, rangeVal);
			checkBlock(new BlockPos(new Vec3d(posX + 1, posY, posZ)), dir, rangeVal);
			checkBlock(new BlockPos(new Vec3d(posX + 1, posY, posZ + 1)), dir, rangeVal);
		} else if (dir == Direction.WEST) {
			checkBlock(new BlockPos(new Vec3d(posX - 1, posY, posZ - 1)), dir, rangeVal);
			checkBlock(new BlockPos(new Vec3d(posX - 1, posY, posZ)), dir, rangeVal);
			checkBlock(new BlockPos(new Vec3d(posX - 1, posY, posZ + 1)), dir, rangeVal);
		}
		finished = true;
	}

	private void checkBlock(BlockPos blockPos, Direction direction, int limit) {
		if (client.player.isFallFlying())
			return;
		if (!modInit.mainThreadMap.containsKey("fall_detector_key")) {
			BlockState block = client.world.getBlockState(blockPos);
			if (block.isOf(Blocks.VOID_AIR))
				return;
			int posX = blockPos.getX();
			int posY = blockPos.getY();
			int posZ = blockPos.getZ();

			if (block.isAir() && !modInit.mainThreadMap.containsKey("fluid_detector_key")
					&& !modInit.mainThreadMap.containsKey("fall_detector_key")) {
				BlockState topBlock = client.world.getBlockState(new BlockPos(new Vec3d(posX, posY + 1, posZ)));
				if (topBlock.isOf(Blocks.VOID_AIR))
					return;
				if (!topBlock.isAir())
					return;

				int depth = getDepth((new BlockPos(new Vec3d(posX, posY, posZ))), 15);

				int depthVal;
				try {
					depthVal = depthArray[Config.getInt(ConfigKeys.FALL_DETECTOR_DEPTH.getKey())];
				} catch (Exception e) {
					depthVal = 5;
				}

				if (depth >= depthVal && !modInit.mainThreadMap.containsKey("fall_detector_key")) {
					modInit.mainThreadMap.put("fall_detector_key", 5000);
//					client.player.sendMessage(Text.of("warning Fall Detected"), true);
					NarratorPlus.narrate(I18n.translate("narrate.apextended.falldetector"));
				}
			}

			if (direction == Direction.NORTH && limit > 0) {
				checkBlock(new BlockPos(new Vec3d(posX, posY, posZ - 1)), direction, limit - 1);
			} else if (direction == Direction.SOUTH && limit > 0) {
				checkBlock(new BlockPos(new Vec3d(posX, posY, posZ + 1)), direction, limit - 1);
			} else if (direction == Direction.EAST && limit > 0) {
				checkBlock(new BlockPos(new Vec3d(posX + 1, posY, posZ)), direction, limit - 1);
			} else if (direction == Direction.WEST && limit > 0) {
				checkBlock(new BlockPos(new Vec3d(posX - 1, posY, posZ)), direction, limit - 1);
			}
		}
	}

	private int getDepth(BlockPos blockPos, int limit) {
		if (limit <= 0) return 0;

		BlockState block = client.world.getBlockState(blockPos);
		int posX = blockPos.getX();
		int posY = blockPos.getY();
		int posZ = blockPos.getZ();

		if (block.isAir() && !block.isOf(Blocks.VOID_AIR)) {
			return 1 + getDepth((new BlockPos(new Vec3d(posX, posY - 1, posZ))), limit - 1);
		}
		return 0;
	}

}
