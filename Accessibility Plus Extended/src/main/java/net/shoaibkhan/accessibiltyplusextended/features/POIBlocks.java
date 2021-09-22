package net.shoaibkhan.accessibiltyplusextended.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.modInit;

public class POIBlocks extends Thread {
    private MinecraftClient client;
    private TreeMap<Double, Vec3d> oreBlocks = new TreeMap<>();
    private TreeMap<Double, Vec3d> doorBlocks = new TreeMap<>();
    private TreeMap<Double, Vec3d> buttonBlocks = new TreeMap<>();
    private TreeMap<Double, Vec3d> blocks = new TreeMap<>();

    private List<String> blockList;
    public boolean running = false;

    public void run() {
        client = MinecraftClient.getInstance();
        running = true;

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
        blockList.add("furnace");
        blockList.add("blast furnace");
        blockList.add("smoker");
        blockList.add("bee nest");
        blockList.add("bee hive");
        blockList.add("lectern");
        blockList.add("item frame");
        blockList.add("glow item frame");
        blockList.add("composter");
        blockList.add("barrel");
        blockList.add("barrel");
        blockList.add("cartography table");
        blockList.add("fletching table");
        blockList.add("grindstone");
        blockList.add("bell");
        blockList.add("smithing table");
        blockList.add("respawn anchor");

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
            rangeVal = 6;
        } catch (Exception e) {
            rangeVal = 6;
        }
        checkBlock(new BlockPos(new Vec3d(posX, posY, posZ)), 0);
        checkBlock(new BlockPos(new Vec3d(posX, posY + 3, posZ)), 0);
        checkBlock(new BlockPos(new Vec3d(posX, posY + 1, posZ)), rangeVal);
        checkBlock(new BlockPos(new Vec3d(posX, posY + 2, posZ)), rangeVal);

        PointsOfInterestsHandler.oreBlocks = this.oreBlocks;
        PointsOfInterestsHandler.doorBlocks = this.doorBlocks;
        PointsOfInterestsHandler.buttonBlocks = this.buttonBlocks;
        PointsOfInterestsHandler.blocks = this.blocks;

        running = false;
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
        Vec3d blockVec3dPos = new Vec3d(posX + 0.5, posY + 0.5, posZ + 0.5);

        double diff = playerVec3dPos.distanceTo(blockVec3dPos);
        boolean playSound = false;

        if (name.contains("ore") || name.equalsIgnoreCase("ancient debris")) {
            oreBlocks.put(diff, blockVec3dPos);
            playSound = true;
        } else if (block instanceof AbstractButtonBlock) {
            buttonBlocks.put(diff, blockVec3dPos);
            playSound = true;
        } else if (block instanceof DoorBlock) {
            ImmutableSet<Entry<Property<?>, Comparable<?>>> entries = blockState.getEntries().entrySet();
            for (Entry<Property<?>, Comparable<?>> i : entries) {

                if (i.getKey().getName().equalsIgnoreCase("half")) {
                    if (i.getValue().toString().equalsIgnoreCase("upper")) {
                        doorBlocks.put(diff, blockVec3dPos);
                        playSound = true;
                    }
                    break;
                }

            }
        } else if (block instanceof LeavesBlock) {
            // System.out.println("\n\n\n\t\tLEAVES\t" +
            // client.world.getBlockState(blockPos).get(LeavesBlock.DISTANCE)
            // + "\t\t\n\n\n");
        } else if (blockList.contains(name) || name.contains("shulker box") || name.contains("fence gate")
                || name.contains("candle")) {
            blocks.put(diff, blockVec3dPos);
            playSound = true;
        } else if (name.equalsIgnoreCase("air") && val - 1 >= 0) {
            checkBlock(new BlockPos(new Vec3d(posX, posY, posZ - 1)), val - 1); // North Block
            checkBlock(new BlockPos(new Vec3d(posX, posY, posZ + 1)), val - 1); // South Block
            checkBlock(new BlockPos(new Vec3d(posX - 1, posY, posZ)), val - 1); // West Block
            checkBlock(new BlockPos(new Vec3d(posX + 1, posY, posZ)), val - 1); // East Block
            checkBlock(new BlockPos(new Vec3d(posX, posY + 1, posZ)), val - 1); // Top Block
            checkBlock(new BlockPos(new Vec3d(posX, posY - 1, posZ)), val - 1); // Bottom Block
        }

        if (playSound && !modInit.mainThreadMap.containsKey("sound+" + blockPos)) {
            client.world.playSound(new BlockPos(blockVec3dPos), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS,
                    0.05f, -5f, true);
            modInit.mainThreadMap.put("sound+" + blockPos, 3000);
        }
    }
}
