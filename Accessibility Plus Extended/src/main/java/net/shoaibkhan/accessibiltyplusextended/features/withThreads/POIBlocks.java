package net.shoaibkhan.accessibiltyplusextended.features.withThreads;

import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.tag.BlockTags;
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

    private static final List<Predicate<BlockState>> blockList = Lists.newArrayList();
    public boolean running = false;

	static {
		blockList.add(state -> state.isOf(Blocks.PISTON));
		blockList.add(state -> state.isOf(Blocks.STICKY_PISTON));
		blockList.add(state -> state.isOf(Blocks.RESPAWN_ANCHOR));
		blockList.add(state -> state.isOf(Blocks.BELL));
		blockList.add(state -> state.isOf(Blocks.OBSERVER));
		blockList.add(state -> state.isOf(Blocks.DAYLIGHT_DETECTOR));
		blockList.add(state -> state.isOf(Blocks.JUKEBOX));
		blockList.add(state -> state.isOf(Blocks.LODESTONE));
		blockList.add(state -> state.getBlock() instanceof BeehiveBlock);
		blockList.add(state -> state.getBlock() instanceof ComposterBlock);
		blockList.add(state -> state.isOf(Blocks.OBSERVER));
		blockList.add(state -> state.isIn(BlockTags.FENCE_GATES));
	}

    public void run() {
        client = MinecraftClient.getInstance();
        running = true;
        volume = POIHandler.getVolume();
        delay = POIHandler.getDelay();
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

        running = false;
    }

    private void checkBlock(BlockPos blockPos, int val) {
        BlockState blockState = client.world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        Vec3d playerVec3dPos = client.player.getEyePos();
        double posX = blockPos.getX();
        double posY = blockPos.getY();
        double posZ = blockPos.getZ();
        Vec3d blockVec3dPos = Vec3d.ofCenter(blockPos);

        double diff = playerVec3dPos.distanceTo(blockVec3dPos);
        boolean playSound = false;
        String soundType = "";

        FluidState fluidState = client.world.getFluidState(blockPos);

        if (block instanceof FluidBlock && Config.get(ConfigKeys.POI_FLUID_DETECTOR_Key.getKey())) {
            if (fluidState.getLevel() == 8) {
                blocks.put(diff, blockVec3dPos);
                playSound = true;
                soundType = "blocks";
            }

            if (Config.get(ConfigKeys.POI_FLUID_DETECTOR_Key.getKey())
                    && !modInit.mainThreadMap.containsKey("fluid_detector_key")) {
                int delay = POIHandler.getFluidDetectorDelay();
                NarratorPlus.narrate(I18n.translate("narrate.apextended.poiblock.warn"));
                modInit.mainThreadMap.put("fluid_detector_key", delay);
            }
        } else if (block instanceof OreBlock) {
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

                if (i.getKey().getName().equals("half")) {
                    if (i.getValue().toString().equals("upper")) {
                        doorBlocks.put(diff, blockVec3dPos);
                        playSound = true;
                        soundType = "blocks";
                    }
                    break;
                }

            }
        } else if (blockList.stream().anyMatch($ -> $.test(blockState))) {
            blocks.put(diff, blockVec3dPos);
            playSound = true;
            soundType = "blocks";
        } else if (blockState.createScreenHandlerFactory(client.world, blockPos) != null) {
            blocks.put(diff, blockVec3dPos);
            playSound = true;
            soundType = "blocksWithInterface";
        } else if (blockState.isAir() && val - 1 >= 0) {
            checkBlock(new BlockPos(new Vec3d(posX, posY, posZ - 1)), val - 1); // North Block
            checkBlock(new BlockPos(new Vec3d(posX, posY, posZ + 1)), val - 1); // South Block
            checkBlock(new BlockPos(new Vec3d(posX - 1, posY, posZ)), val - 1); // West Block
            checkBlock(new BlockPos(new Vec3d(posX + 1, posY, posZ)), val - 1); // East Block
            checkBlock(new BlockPos(new Vec3d(posX, posY + 1, posZ)), val - 1); // Top Block
            checkBlock(new BlockPos(new Vec3d(posX, posY - 1, posZ)), val - 1); // Bottom Block
        }

        if (playSound && !modInit.mainThreadMap.containsKey("sound+" + blockPos) && volume>0) {

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
