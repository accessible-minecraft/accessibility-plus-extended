package net.shoaibkhan.accessibiltyplusextended.features;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POIBlocks {
    private MinecraftClient client;
    private Map<Double, String> oreBlocks = new HashMap<>();
    private Map<Double, BlockPos> doorBlocks = new HashMap<>();
    private Map<Double, BlockPos> buttonBlocks = new HashMap<>();
    private Map<Double, BlockPos> blocks = new HashMap<>();

    private List<String> blockList;

    public POIBlocks() {
        client = MinecraftClient.getInstance();

        blockList = new ArrayList<String>();
        blockList.add("chest");
        blockList.add("large chest");
        blockList.add("ender chest");
        blockList.add("crafting table");
        blockList.add("enchanting table");
        blockList.add("stonecutter");
        blockList.add("trapped chest");
        blockList.add("piston");
        blockList.add("sticky piston");
        blockList.add("observer");
        blockList.add("hopper");
        blockList.add("dispenser");
        blockList.add("dropper");
        blockList.add("daylight detector");
        blockList.add("jukebox");
        blockList.add("loom");

        main();
    }

    private void main() {
        client = MinecraftClient.getInstance();
        assert client.player != null;

        BlockPos pos = client.player.getBlockPos();

        int posX = pos.getX();
        int posY = pos.getY() - 1;
        int posZ = pos.getZ();
        int rangeVal;
        try {
            rangeVal = 4;
        } catch (Exception e) {
            rangeVal = 4;
        }
        checkBlock(new BlockPos(new Vec3d(posX, posY, posZ)), 0);
        checkBlock(new BlockPos(new Vec3d(posX, posY + 3, posZ)), 0);
        checkBlock(new BlockPos(new Vec3d(posX, posY + 1, posZ)), rangeVal);
        checkBlock(new BlockPos(new Vec3d(posX, posY + 2, posZ)), rangeVal);

        PointsOfInterestsHandler.oreBlocks = this.oreBlocks;
        PointsOfInterestsHandler.doorBlocks = this.doorBlocks;
        PointsOfInterestsHandler.buttonBlocks = this.buttonBlocks;
        PointsOfInterestsHandler.blocks = this.blocks;
    }

    private void checkBlock(BlockPos blockPos, int val) {
        BlockState blockState = client.world.getBlockState(blockPos);
        Block block = blockState.getBlock();

        TranslatableText translatableText = new TranslatableText(block.getTranslationKey());
        String name = translatableText.getString();
        name = name.toLowerCase();

        Vec3d playerVec3dPos = client.player.getPos();
        double posX = blockPos.getX();
        double posY = blockPos.getY();
        double posZ = blockPos.getZ();
        Vec3d blockVec3dPos = new Vec3d(posX, posY, posZ);

        double diff = playerVec3dPos.distanceTo(blockVec3dPos);

        if (name.contains("ore")) {
            oreBlocks.put(diff, name);
        } else if (block instanceof AbstractButtonBlock) {
            buttonBlocks.put(diff, blockPos);
        } else if (block instanceof DoorBlock) {
            doorBlocks.put(diff, blockPos);
        } else if (block instanceof LeavesBlock) {
            System.out.println("\n\n\n\t\tLEAVES\t" + client.world.getBlockState(blockPos).get(LeavesBlock.DISTANCE)
                    + "\t\t\n\n\n");
        } else if (blockList.contains(name)) {
            blocks.put(diff, blockPos);
            client.world.playSound(blockPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f, true);
        } else if (name.equalsIgnoreCase("air") && val - 1 >= 0) {
            checkBlock(new BlockPos(new Vec3d(posX, posY, posZ - 1)), val - 1); // North Block
            checkBlock(new BlockPos(new Vec3d(posX, posY, posZ + 1)), val - 1); // South Block
            checkBlock(new BlockPos(new Vec3d(posX - 1, posY, posZ)), val - 1); // West Block
            checkBlock(new BlockPos(new Vec3d(posX + 1, posY, posZ)), val - 1); // East Block
            checkBlock(new BlockPos(new Vec3d(posX, posY + 1, posZ)), val - 1); // Top Block
            checkBlock(new BlockPos(new Vec3d(posX, posY - 1, posZ)), val - 1); // Bottom Block
        }
    }
}
