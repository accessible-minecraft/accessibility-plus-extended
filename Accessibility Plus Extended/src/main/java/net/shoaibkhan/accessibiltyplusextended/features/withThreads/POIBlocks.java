package net.shoaibkhan.accessibiltyplusextended.features.withThreads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.collect.ImmutableSet;
import com.mojang.text2speech.Narrator;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;
import net.shoaibkhan.accessibiltyplusextended.features.POIHandler;

public class POIBlocks extends Thread {
    private MinecraftClient client;
    private TreeMap<Double, Vec3d> oreBlocks = new TreeMap<>();
    private TreeMap<Double, Vec3d> doorBlocks = new TreeMap<>();
    private TreeMap<Double, Vec3d> buttonBlocks = new TreeMap<>();
    private TreeMap<Double, Vec3d> ladderBlocks = new TreeMap<>();
    private TreeMap<Double, Vec3d> leverBlocks = new TreeMap<>();
    private TreeMap<Double, Vec3d> trapDoorBlocks = new TreeMap<>();
    private TreeMap<Double, Vec3d> blocks = new TreeMap<>();
    private float volume;
    private int delay;

    private List<String> blockListWithInterface;
    private List<String> blockList;
    public boolean running = false;

    public void run() {
        client = MinecraftClient.getInstance();
        running = true;
        volume = POIHandler.getVolume();
        delay = POIHandler.getDelay();

        blockListWithInterface = new ArrayList<String>();
        blockList = new ArrayList<String>();
        blockListWithInterface.add("chest");
        blockListWithInterface.add("large chest");
        blockListWithInterface.add("ender chest");
        blockListWithInterface.add("crafting table");
        blockListWithInterface.add("enchanting table");
        blockListWithInterface.add("stonecutter");
        blockListWithInterface.add("trapped chest");
        blockListWithInterface.add("hopper");
        blockListWithInterface.add("dispenser");
        blockListWithInterface.add("dropper");
        blockListWithInterface.add("loom");
        blockListWithInterface.add("furnace");
        blockListWithInterface.add("blast furnace");
        blockListWithInterface.add("smoker");
        blockListWithInterface.add("lectern");
        blockListWithInterface.add("barrel");
        blockListWithInterface.add("cartography table");
        blockListWithInterface.add("fletching table");
        blockListWithInterface.add("grindstone");
        blockListWithInterface.add("smithing table");

        blockList.add("piston");
        blockList.add("respawn anchor");
        blockList.add("bell");
        blockList.add("sticky piston");
        blockList.add("observer");
        blockList.add("daylight detector");
        blockList.add("jukebox");
        blockList.add("bee nest");
        blockList.add("bee hive");
        blockList.add("item frame");
        blockList.add("glow item frame");
        blockList.add("composter");
        blockList.add("lodestone");

        main();
    }

    private void main() {
        client = MinecraftClient.getInstance();
        assert client.player != null;

        BlockPos pos = client.player.getBlockPos();

        int posX = pos.getX();
        int posY = pos.getY() - 1;
        int posZ = pos.getZ();
        int rangeVal = POIHandler.getRange();

        checkBlock(new BlockPos(new Vec3d(posX, posY, posZ)), 0);
        checkBlock(new BlockPos(new Vec3d(posX, posY + 3, posZ)), 0);
        checkBlock(new BlockPos(new Vec3d(posX, posY + 1, posZ)), rangeVal);
        checkBlock(new BlockPos(new Vec3d(posX, posY + 2, posZ)), rangeVal);

        POIHandler.oreBlocks = this.oreBlocks;
        POIHandler.doorBlocks = this.doorBlocks;
        POIHandler.buttonBlocks = this.buttonBlocks;
        POIHandler.ladderBlocks = this.ladderBlocks;
        POIHandler.leverBlocks = this.leverBlocks;
        POIHandler.trapDoorBlocks = this.trapDoorBlocks;
        POIHandler.blocks = this.blocks;

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        String soundType = "";

        FluidState fluidState = client.world.getFluidState(blockPos);

        if ((name.contains("lava") || name.contains("water"))) {
            if (fluidState.getLevel() == 8) {
                blocks.put(diff, blockVec3dPos);
                playSound = true;
                soundType = "blocks";
            }

            if (Config.get(ConfigKeys.POI_FLUID_DETECTOR_Key.getKey())
                    && !modInit.mainThreadMap.containsKey("fluid_detector_key")) {
                int delay = POIHandler.getFluidDetectorDelay();
                NarratorPlus.narrate("Warning " + name);
                modInit.mainThreadMap.put("fluid_detector_key", delay);
            }
        } else if (name.contains("ore") || name.equalsIgnoreCase("ancient debris")) {
            oreBlocks.put(diff, blockVec3dPos);
            playSound = true;
            soundType = "ore";
        } else if (block instanceof AbstractButtonBlock) {
            buttonBlocks.put(diff, blockVec3dPos);
            playSound = true;
            soundType = "blocks";
        } else if (block instanceof LadderBlock) {
            ladderBlocks.put(diff, blockVec3dPos);
            playSound = true;
            soundType = "blocks";
        } else if (block instanceof TrapdoorBlock) {
            trapDoorBlocks.put(diff, blockVec3dPos);
            playSound = true;
            soundType = "blocks";
        } else if (block instanceof LeverBlock) {
            leverBlocks.put(diff, blockVec3dPos);
            playSound = true;
            soundType = "blocks";
        } else if (block instanceof DoorBlock) {
            ImmutableSet<Entry<Property<?>, Comparable<?>>> entries = blockState.getEntries().entrySet();
            for (Entry<Property<?>, Comparable<?>> i : entries) {

                if (i.getKey().getName().equalsIgnoreCase("half")) {
                    if (i.getValue().toString().equalsIgnoreCase("upper")) {
                        doorBlocks.put(diff, blockVec3dPos);
                        playSound = true;
                        soundType = "blocks";
                    }
                    break;
                }

            }
        } else if (blockList.contains(name) || name.contains("fence gate")) {
            blocks.put(diff, blockVec3dPos);
            playSound = true;
            soundType = "blocks";
        } else if (blockListWithInterface.contains(name) || name.contains("shulker box")) {
            blocks.put(diff, blockVec3dPos);
            playSound = true;
            soundType = "blocksWithInterface";
        } else if (name.equalsIgnoreCase("air") && val - 1 >= 0) {
            checkBlock(new BlockPos(new Vec3d(posX, posY, posZ - 1)), val - 1); // North Block
            checkBlock(new BlockPos(new Vec3d(posX, posY, posZ + 1)), val - 1); // South Block
            checkBlock(new BlockPos(new Vec3d(posX - 1, posY, posZ)), val - 1); // West Block
            checkBlock(new BlockPos(new Vec3d(posX + 1, posY, posZ)), val - 1); // East Block
            checkBlock(new BlockPos(new Vec3d(posX, posY + 1, posZ)), val - 1); // Top Block
            checkBlock(new BlockPos(new Vec3d(posX, posY - 1, posZ)), val - 1); // Bottom Block
        }

        if (playSound && !modInit.mainThreadMap.containsKey("sound+" + blockPos)) {

            if (soundType.equalsIgnoreCase("ore"))
                client.world.playSound(new BlockPos(blockVec3dPos), SoundEvents.ENTITY_ITEM_PICKUP,
                        SoundCategory.BLOCKS, volume, -5f, true);
            else if (soundType.equalsIgnoreCase("blocks"))
                client.world.playSound(new BlockPos(blockVec3dPos), SoundEvents.BLOCK_NOTE_BLOCK_BIT,
                        SoundCategory.BLOCKS, volume, 2f, true);
            else if (soundType.equalsIgnoreCase("blocksWithInterface"))
                client.world.playSound(new BlockPos(blockVec3dPos), SoundEvents.BLOCK_NOTE_BLOCK_BANJO,
                        SoundCategory.BLOCKS, volume, 0f, true);

            modInit.mainThreadMap.put("sound+" + blockPos, delay);
        }
    }
}
