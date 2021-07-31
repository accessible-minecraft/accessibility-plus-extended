package net.shoaibkhan.accessibiltyplusextended;


import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class FluidDetectorThread extends Thread{
  private boolean lava, water; 
  public static String[] volume = { "0", "0.05", "0.1", "0.15", "0.2", "0.25", "0.3", "0.35", "0.4", "0.45", "0.5", "0.55", "0.6", "0.65", "0.7", "0.75", "0.8", "0.85", "0.9", "0.95", "1" };
  public static String[] pitch = { "0", "0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5", "-0.5", "-1", "-1.5", "-2", "-2.5", "-3", "-3.5", "-4", "-4.5", "-5" };
  public static String[] range = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};

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
      rangeVal = Integer.parseInt(range[Config.getInt(Config.getFindfluidrange())] + "");
    } catch (Exception e) {
      rangeVal = 10;
    }

    BlockPos newBlockPos = new BlockPos(new Vec3d(posX, posY, posZ));
    BlockPos fluidPos = findFluid(client, newBlockPos, rangeVal, this.lava, this.water);
    if(fluidPos!=null){
      if(!Config.get(Config.getFindfluidtextkey())){
        try {
          Float vol, pit;
          try {
            vol = Float.parseFloat(volume[Config.getInt(Config.getFindfluidvolume())] + "");
          } catch (Exception e) {
            vol = 0.2f;
          }
          try {
            pit = Float.parseFloat(pitch[Config.getInt(Config.getFindfluidpitch())] + "");
          } catch (Exception e) {
            pit = 1f;
          }
          if (Config.get(Config.getOredetectorcustomsoundkey())) {
            client.world.playSound(fluidPos, modInit.oreSoundEvent, SoundCategory.BLOCKS, vol, pit, true);
          } else {
            client.world.playSound(fluidPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, vol, pit, true);
          }
        } catch (Exception e) {
        }
      }else{
        String posDifference = HudRenderCallBackClass.get_position_difference(fluidPos, client);
        MutableText blockMutableText = new LiteralText("").append(client.world.getBlockState(fluidPos).getBlock().getName());
        String name = blockMutableText.getString();
    
        client.player.sendMessage(new LiteralText(""+name+", "+posDifference), true);
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
