package net.shoaibkhan.accessibiltyplusextended.threads;


import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class DetectorThread extends Thread {
  public boolean finished = false, alive = false;
  private MinecraftClient client;
  public static String[] volume = { "0", "0.05", "0.1", "0.15", "0.2", "0.25", "0.3", "0.35", "0.4", "0.45", "0.5", "0.55", "0.6", "0.65", "0.7", "0.75", "0.8", "0.85", "0.9", "0.95", "1" };
  public static String[] pitch = { "0", "0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5", "-0.5", "-1", "-1.5", "-2", "-2.5", "-3", "-3.5", "-4", "-4.5", "-5" };
  public static String[] range = {"3", "4", "5", "6", "7", "8", "9", "10"};
  public static String[] delay = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};

  public void run() {
    alive = true;
    client = MinecraftClient.getInstance();
    BlockPos pos = client.player.getBlockPos();
    int posX = pos.getX();
    int posY = pos.getY() - 1;
    int posZ = pos.getZ();
    int rangeVal;
    try {
      rangeVal = Integer.parseInt(range[Config.getInt(Config.getOredetectorrange())]);
    } catch (Exception e) {
      rangeVal = 4;
    }
    checkBlock(new BlockPos(new Vec3d(posX, posY, posZ)), 0, Config.get(Config.getLavadetectorkey()), Config.get(Config.getWaterdetectorkey()), Config.get(Config.getOredetectorkey()));
    checkBlock(new BlockPos(new Vec3d(posX, posY + 3, posZ)), 0, Config.get(Config.getLavadetectorkey()), Config.get(Config.getWaterdetectorkey()), Config.get(Config.getOredetectorkey()));
    checkBlock(new BlockPos(new Vec3d(posX, posY + 1, posZ)), rangeVal, Config.get(Config.getLavadetectorkey()), Config.get(Config.getWaterdetectorkey()), Config.get(Config.getOredetectorkey()));
    checkBlock(new BlockPos(new Vec3d(posX, posY + 2, posZ)), rangeVal, Config.get(Config.getLavadetectorkey()), Config.get(Config.getWaterdetectorkey()), Config.get(Config.getOredetectorkey()));
    finished = true;
  }

  private void checkBlock(BlockPos blockPos, int val, boolean lava, boolean water, boolean ore) {
    Block block = client.world.getBlockState(blockPos).getBlock();
    String name = block.getTranslationKey();
    if (name.contains("void_air"))
      return;
    name = name.substring(name.lastIndexOf(".") + 1);
    if (name.contains("_"))
      name = name.replace("_", " ");

    lavaDetector: {
      if (name.contains("lava") && !modInit.mainThreadMap.containsKey("lava_detector_key") && lava) {
        if (!modInit.mainThreadMap.containsKey("lava_detector_key")) {
          client.player.sendMessage(new LiteralText("Warning Lava"), true);
          modInit.mainThreadMap.put("lava_detector_key", 5000);
        }
      }
    }

    waterDetector: {
      if (name.contains("water") && !modInit.mainThreadMap.containsKey("water_detector_key") && water) {
        if (!modInit.mainThreadMap.containsKey("water_detector_key")) {
          client.player.sendMessage(new LiteralText("Warning Water"), true);
          modInit.mainThreadMap.put("water_detector_key", 5000);
        }
      }
    }

    oreDetector: {
      if (name.contains("ore") && !modInit.mainThreadMap.containsKey(name + "" + blockPos) && ore) {
        try {
          Float vol, pit;
          int del;
          try {
            vol = Float.parseFloat(volume[Config.getInt(Config.getOredetectorvolume())] + "");
          } catch (Exception e) {
            vol = 0.2f;
          }
          try {
            pit = Float.parseFloat(pitch[Config.getInt(Config.getOredetectorpitch())] + "");
          } catch (Exception e) {
            pit = 1f;
          }
          
          client.world.playSound(blockPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, vol, pit, true);

          try {
            del = Integer.parseInt(delay[Config.getInt(Config.getOredetectordelay())] + "");
          } catch (Exception e) {
            del = 10;
          }
          modInit.mainThreadMap.put(name + "" + blockPos, del*1000);
        } catch (Exception e) {
        }
      } else if (name.contains("air") && val - 1 >= 0) {
        int posX = blockPos.getX();
        int posY = blockPos.getY();
        int posZ = blockPos.getZ();
        checkBlock(new BlockPos(new Vec3d(posX, posY, posZ - 1)), val - 1, lava, water, ore); // North Block
        checkBlock(new BlockPos(new Vec3d(posX, posY, posZ + 1)), val - 1, lava, water, ore); // South Block
        checkBlock(new BlockPos(new Vec3d(posX - 1, posY, posZ)), val - 1, lava, water, ore); // West Block
        checkBlock(new BlockPos(new Vec3d(posX + 1, posY, posZ)), val - 1, lava, water, ore); // East Block
        checkBlock(new BlockPos(new Vec3d(posX, posY + 1, posZ)), val - 1, lava, water, ore); // Top Block
        checkBlock(new BlockPos(new Vec3d(posX, posY - 1, posZ)), val - 1, lava, water, ore); // Bottom Block
      }
    }
  }

  public static void checkFluid() {
    MinecraftClient client = MinecraftClient.getInstance();
    BlockPos pos = client.player.getBlockPos();
    int posX = pos.getX();
    int posY = pos.getY() - 1;
    int posZ = pos.getZ();
    int rangeVal = 4;
    BlockPos newBlockPos = new BlockPos(new Vec3d(posX, posY, posZ));
    BlockPos fluidPos = findFluid(client, newBlockPos, rangeVal, false, true);
    if(fluidPos!=null){
      FluidState fluidState = client.world.getFluidState(fluidPos);
//      System.out.println("Fluid:" + client.world.getBlockState(fluidPos).getBlock().getName() + "\t Level:" + fluidState.getLevel());
    }
  }

  private static BlockPos findFluid(MinecraftClient client, BlockPos blockPos, int range, boolean lava, boolean water){
    Block block = client.world.getBlockState(blockPos).getBlock();
    String name = block.getTranslationKey();
    if (name.contains("void_air"))
      return null;
    name = name.substring(name.lastIndexOf(".") + 1);
    if (name.contains("_"))
      name = name.replace("_", " ");

//    System.out.println(name);

    FluidState fluidState = client.world.getFluidState(blockPos);
    if ((name.contains("lava") && lava) || (name.contains("water") && water) && fluidState.getLevel()==8) {
      return blockPos;
    } else if(range-1 >= 0){
      int posX = blockPos.getX();
      int posY = blockPos.getY();
      int posZ = blockPos.getZ();
      int rangeVal = 10;
      BlockPos bp1 = findFluid(client, new BlockPos(new Vec3d(posX, posY, posZ - 1)), rangeVal, lava, water);
      BlockPos bp2 = findFluid(client, new BlockPos(new Vec3d(posX, posY, posZ + 1)), rangeVal, lava, water);
      BlockPos bp3 = findFluid(client, new BlockPos(new Vec3d(posX - 1, posY, posZ)), rangeVal, lava, water);
      BlockPos bp4 = findFluid(client, new BlockPos(new Vec3d(posX + 1, posY, posZ)), rangeVal, lava, water);
      BlockPos bp5 = findFluid(client, new BlockPos(new Vec3d(posX, posY - 1, posZ)), rangeVal, lava, water);
      BlockPos bp6 = findFluid(client, new BlockPos(new Vec3d(posX, posY + 1, posZ)), rangeVal, lava, water);

      if(bp1 != null ) return bp1;
      if(bp2 != null ) return bp2;
      if(bp3 != null ) return bp3;
      if(bp4 != null ) return bp4;
      if(bp5 != null ) return bp5;
      if(bp6 != null ) return bp6;
    }

    return null;
  }
}