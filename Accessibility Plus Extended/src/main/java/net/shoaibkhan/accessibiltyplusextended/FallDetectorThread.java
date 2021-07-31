package net.shoaibkhan.accessibiltyplusextended;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class FallDetectorThread extends Thread {

	public boolean alive = false,finished = false;
	private MinecraftClient client;
  public static String[] range = {"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
  public static String[] depthArray = {"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
	
	public void run() {
		alive = true;
		client = MinecraftClient.getInstance();
    	BlockPos pos = client.player.getBlockPos();
    	int posX = pos.getX();
    	int posY = pos.getY()-1;
    	int posZ = pos.getZ();

      int rangeVal = 10;
      try {
        rangeVal = Integer.parseInt(range[Config.getInt(Config.getFalldetectorrange())] + "");
        rangeVal = rangeVal-1;
      } catch (Exception e) {
        rangeVal = 10;
      }

    	String dir = client.player.getHorizontalFacing().toString().toLowerCase().trim();
    	if(dir.equalsIgnoreCase("north")) {
    		checkBlock(new BlockPos(new Vec3d(posX-1,posY,posZ-1)), dir, rangeVal);
    		checkBlock(new BlockPos(new Vec3d(posX,posY,posZ-1)), dir, rangeVal);
    		checkBlock(new BlockPos(new Vec3d(posX+1,posY,posZ-1)), dir, rangeVal);
    	} else if(dir.equalsIgnoreCase("south")) {
    		checkBlock(new BlockPos(new Vec3d(posX-1,posY,posZ+1)), dir, rangeVal);
    		checkBlock(new BlockPos(new Vec3d(posX,posY,posZ+1)), dir, rangeVal);
    		checkBlock(new BlockPos(new Vec3d(posX+1,posY,posZ+1)), dir, rangeVal);
    	} else if(dir.equalsIgnoreCase("east")) {
    		checkBlock(new BlockPos(new Vec3d(posX+1,posY,posZ-1)), dir, rangeVal);
    		checkBlock(new BlockPos(new Vec3d(posX+1,posY,posZ)), dir, rangeVal);
    		checkBlock(new BlockPos(new Vec3d(posX+1,posY,posZ+1)), dir, rangeVal);
    	} else if(dir.equalsIgnoreCase("west")) {
    		checkBlock(new BlockPos(new Vec3d(posX-1,posY,posZ-1)), dir, rangeVal);
    		checkBlock(new BlockPos(new Vec3d(posX-1,posY,posZ)), dir, rangeVal);
    		checkBlock(new BlockPos(new Vec3d(posX-1,posY,posZ+1)), dir, rangeVal);
    	}
    	finished = true;
    }
	
	private void checkBlock(BlockPos blockPos,String direction,int limit) {
		if(!modInit.mainThreadMap.containsKey("fall_detector_key")){
			Block block = client.world.getBlockState(blockPos).getBlock();
			MutableText blockNameMutable = (new LiteralText("")).append(block.getName());
			String blockName = blockNameMutable.getString().toLowerCase();
			if(blockName.equalsIgnoreCase("void air")) return;
	    	int posX = blockPos.getX();
	    	int posY = blockPos.getY();
	    	int posZ = blockPos.getZ();
	    	
	    	
	    	if(blockName.contains("air") && !modInit.mainThreadMap.containsKey("lava_detector_key") && !modInit.mainThreadMap.containsKey("fall_detector_key")) {
		    	Block topBlock = client.world.getBlockState(new BlockPos(new Vec3d(posX,posY+1,posZ))).getBlock();
		    	MutableText topBlocklockNameMutable = (new LiteralText("")).append(topBlock.getName());
		    	String topBlocklockName = topBlocklockNameMutable.getString().toLowerCase();
		    	if(topBlocklockName.equalsIgnoreCase("void air")) return;
		    	if(!topBlocklockName.contains("air")) return;
		    	
		    	int depth = getDepth((new BlockPos(new Vec3d(posX,posY,posZ))),15);

          int depthVal;
          try {
            depthVal = Integer.parseInt(depthArray[Config.getInt(Config.getFalldetectordepth())] + "");
          } catch (Exception e) {
            depthVal = 5;
          }

		    	if(depth>=depthVal && !modInit.mainThreadMap.containsKey("fall_detector_key")) {
		    		modInit.mainThreadMap.put("fall_detector_key", 5000);
		    		client.player.sendMessage(new LiteralText("warning Fall Detected"), true);
		    	}
		    }

		  	if(direction.equalsIgnoreCase("north") && limit>0) {
	    		checkBlock(new BlockPos(new Vec3d(posX,posY,posZ-1)), direction, limit-1);
	    	} else if(direction.equalsIgnoreCase("south") && limit>0) {
	    		checkBlock(new BlockPos(new Vec3d(posX,posY,posZ+1)), direction, limit-1);
	    	} else if(direction.equalsIgnoreCase("east") && limit>0) {
	    		checkBlock(new BlockPos(new Vec3d(posX+1,posY,posZ)), direction, limit-1);
	    	} else if(direction.equalsIgnoreCase("west") && limit>0) {
	    		checkBlock(new BlockPos(new Vec3d(posX-1,posY,posZ)), direction, limit-1);
	    	}
		}
	}
	
	private int getDepth(BlockPos blockPos, int limit) {
		if(limit<=0) return 0;
		Block block = client.world.getBlockState(blockPos).getBlock();
		MutableText blockNameMutable = (new LiteralText("")).append(block.getName());
		String blockName = blockNameMutable.getString().toLowerCase();
    	int posX = blockPos.getX();
    	int posY = blockPos.getY();
    	int posZ = blockPos.getZ();
    	
    	// if(blockName.contains("lava") && !modInit.mainThreadMap.containsKey("lava_detector_key")) {
    	// 	if(!modInit.mainThreadMap.containsKey("lava_detector_key")) {
    	// 		client.player.sendMessage(new LiteralText("Warning Lava"), true);
    	// 		modInit.mainThreadMap.put("lava_detector_key", 5000);
    	// 		return -9999;
    	// 	}
    	// }
    	
    	// if(blockName.contains("water") && !modInit.mainThreadMap.containsKey("lava_detector_key") && !modInit.mainThreadMap.containsKey("water_detector_key")) {
    	// 	if(!modInit.mainThreadMap.containsKey("water_detector_key")) {
    	// 		client.player.sendMessage(new LiteralText("Warning Water"), true);
    	// 		modInit.mainThreadMap.put("water_detector_key", 5000);
    	// 		return -9999;
    	// 	}
    	// }
		
    	if(blockName.contains("air")) return 1+getDepth((new BlockPos(new Vec3d(posX,posY-1,posZ))),limit-1);
		else if(blockName.equalsIgnoreCase("void air")) return 0;
		return 0;
	}

}
