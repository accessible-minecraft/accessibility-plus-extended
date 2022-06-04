package net.shoaibkhan.accessibiltyplusextended.features.withThreads;


import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.HudRenderCallBackClass;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;

public class FluidDetectorThread extends Thread{
  private boolean lava, water; 
  public static Float[] volume = { 0f, 0.05f, 0.1f, 0.15f, 0.2f, 0.25f, 0.3f, 0.35f, 0.4f, 0.45f, 0.5f, 0.55f, 0.6f, 0.65f, 0.7f, 0.75f, 0.8f, 0.85f, 0.9f, 0.95f, 1f };
  public static Float[] pitch = { 0f, 0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 3.5f, 4f, 4.5f, 5f, -0.5f, -1f, -1.5f, -2f, -2.5f, -3f, -3.5f, -4f, -4.5f, -5f };
  public static Integer[] range = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

  public FluidDetectorThread(boolean lava, boolean water){
    this.lava = lava;
    this.water = water;
  }
  public void run() {
    MinecraftClient client = MinecraftClient.getInstance();
    BlockPos pos = client.player.getBlockPos();
    int posX = pos.getX();
    int posY = pos.getY();
    int posZ = pos.getZ();

    int rangeVal = 10;
    try {
      rangeVal = range[Config.getInt(ConfigKeys.FIND_FLUID_RANGE.getKey())];
    } catch (Exception e) {
      rangeVal = 10;
    }

    BlockPos newBlockPos = new BlockPos(new Vec3d(posX, posY, posZ));
    BlockPos fluidPos = findFluid(client, newBlockPos, rangeVal, this.lava, this.water);
    if(fluidPos!=null){
      if(!Config.get(ConfigKeys.FIND_FLUID_TEXT_KEY.getKey())){
        try {
          Float vol, pit;
          try {
            vol = volume[Config.getInt(ConfigKeys.FIND_FLUID_VOLUME.getKey())];
          } catch (Exception e) {
            vol = 0.2f;
          }
          try {
            pit = pitch[Config.getInt(ConfigKeys.FIND_FLUID_PITCH.getKey())];
          } catch (Exception e) {
            pit = 1f;
          }

          client.world.playSound(fluidPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, vol, pit, true);

        } catch (Exception e) {
        }
      }else{
        String posDifference = HudRenderCallBackClass.get_position_difference(fluidPos, client);
        MutableText blockMutableText = client.world.getBlockState(fluidPos).getBlock().getName();
        String name = blockMutableText.getString();
    
//        client.player.sendMessage(new LiteralText(""+name+", "+posDifference), true);
        NarratorPlus.narrate(name+", "+posDifference);
      }
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

    // System.out.println(name);

    FluidState fluidState = client.world.getFluidState(blockPos);
    if ((name.contains("lava") && lava) || (name.contains("water") && water) && fluidState.getLevel()==8) {
      return blockPos;
    } else if(range-1 >= 0 && name.contains("air")){
      int posX = blockPos.getX();
      int posY = blockPos.getY();
      int posZ = blockPos.getZ();
      int rangeVal = range-1;
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
