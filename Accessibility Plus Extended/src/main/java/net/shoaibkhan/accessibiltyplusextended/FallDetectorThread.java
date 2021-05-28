package net.shoaibkhan.accessibiltyplusextended;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FallDetectorThread extends Thread {
	public boolean alive = false,finished = false;
	private MinecraftClient client;
	
	public void run() {
		alive = true;
		client = MinecraftClient.getInstance();
    	BlockPos pos = client.player.getBlockPos();
    	int posX = pos.getX();
    	int posY = pos.getY()-1;
    	int posZ = pos.getZ();
    	String dir = client.player.getHorizontalFacing().toString().toLowerCase().trim();
    	if(dir.equalsIgnoreCase("north")) {
    		checkBlock(new BlockPos(new Vec3d(posX-1,posY,posZ-1)), dir, 10);
    		checkBlock(new BlockPos(new Vec3d(posX,posY,posZ-1)), dir, 10);
    		checkBlock(new BlockPos(new Vec3d(posX+1,posY,posZ-1)), dir, 10);
    		
    		checkFluid(new BlockPos(new Vec3d(posX-1,posY+1,posZ-1)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX,posY+1,posZ-1)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX+1,posY+1,posZ-1)), dir, 10);
    		
    		checkFluid(new BlockPos(new Vec3d(posX-1,posY+2,posZ-1)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX,posY+2,posZ-1)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX+1,posY+2,posZ-1)), dir, 10);
    	} else if(dir.equalsIgnoreCase("south")) {
    		checkBlock(new BlockPos(new Vec3d(posX-1,posY,posZ+1)), dir, 10);
    		checkBlock(new BlockPos(new Vec3d(posX,posY,posZ+1)), dir, 10);
    		checkBlock(new BlockPos(new Vec3d(posX+1,posY,posZ+1)), dir, 10);

    		checkFluid(new BlockPos(new Vec3d(posX-1,posY+1,posZ+1)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX,posY+1,posZ+1)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX+1,posY+1,posZ+1)), dir, 10);

    		checkFluid(new BlockPos(new Vec3d(posX-1,posY+2,posZ+1)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX,posY+2,posZ+1)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX+1,posY+2,posZ+1)), dir, 10);
    	} else if(dir.equalsIgnoreCase("east")) {
    		checkBlock(new BlockPos(new Vec3d(posX+1,posY,posZ-1)), dir, 10);
    		checkBlock(new BlockPos(new Vec3d(posX+1,posY,posZ)), dir, 10);
    		checkBlock(new BlockPos(new Vec3d(posX+1,posY,posZ+1)), dir, 10);
    		
    		checkFluid(new BlockPos(new Vec3d(posX+1,posY+1,posZ-1)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX+1,posY+1,posZ)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX+1,posY+1,posZ+1)), dir, 10);

    		checkFluid(new BlockPos(new Vec3d(posX+1,posY+2,posZ-1)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX+1,posY+2,posZ)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX+1,posY+2,posZ+1)), dir, 10);
    	} else if(dir.equalsIgnoreCase("west")) {
    		checkBlock(new BlockPos(new Vec3d(posX-1,posY,posZ-1)), dir, 10);
    		checkBlock(new BlockPos(new Vec3d(posX-1,posY,posZ)), dir, 10);
    		checkBlock(new BlockPos(new Vec3d(posX-1,posY,posZ+1)), dir, 10);

    		checkFluid(new BlockPos(new Vec3d(posX-1,posY+1,posZ-1)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX-1,posY+1,posZ)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX-1,posY+1,posZ+1)), dir, 10);

    		checkFluid(new BlockPos(new Vec3d(posX-1,posY+2,posZ-1)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX-1,posY+2,posZ)), dir, 10);
    		checkFluid(new BlockPos(new Vec3d(posX-1,posY+2,posZ+1)), dir, 10);
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
	    	
	    	if(blockName.contains("lava") && !modInit.mainThreadMap.containsKey("lava_detector_key")) {
				if(!modInit.mainThreadMap.containsKey("lava_detector_key")) {
					client.player.sendMessage(new LiteralText("Warning Lava"), true);
					modInit.mainThreadMap.put("lava_detector_key", 5000);
				}
			}
	    	
	    	if(blockName.contains("air") && !modInit.mainThreadMap.containsKey("lava_detector_key") && !modInit.mainThreadMap.containsKey("fall_detector_key")) {
				Block topBlock = client.world.getBlockState(new BlockPos(new Vec3d(posX,posY+1,posZ))).getBlock();
				MutableText topBlocklockNameMutable = (new LiteralText("")).append(topBlock.getName());
				String topBlocklockName = topBlocklockNameMutable.getString().toLowerCase();
				if(topBlocklockName.equalsIgnoreCase("void air")) return;
				if(!topBlocklockName.contains("air")) return;
				
				int depth = getDepth((new BlockPos(new Vec3d(posX,posY,posZ))),15);
				if(depth>=5 && !modInit.mainThreadMap.containsKey("fall_detector_key")) {
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
    	
    	if(blockName.contains("lava") && !modInit.mainThreadMap.containsKey("lava_detector_key")) {
			if(!modInit.mainThreadMap.containsKey("lava_detector_key")) {
				client.player.sendMessage(new LiteralText("Warning Lava"), true);
				modInit.mainThreadMap.put("lava_detector_key", 5000);
				return -9999;
			}
		}
    	
    	if(blockName.contains("water") && !modInit.mainThreadMap.containsKey("lava_detector_key") && !modInit.mainThreadMap.containsKey("water_detector_key")) {
			if(!modInit.mainThreadMap.containsKey("water_detector_key")) {
				client.player.sendMessage(new LiteralText("Warning Water"), true);
				modInit.mainThreadMap.put("water_detector_key", 5000);
				return -9999;
			}
		}
		
    	if(blockName.contains("air")) return 1+getDepth((new BlockPos(new Vec3d(posX,posY-1,posZ))),limit-1);
		else if(blockName.equalsIgnoreCase("void air")) return 0;
		return 0;
	}

	private void checkFluid(BlockPos blockPos,String direction,int limit) {
		Block block = client.world.getBlockState(blockPos).getBlock();
		MutableText blockNameMutable = (new LiteralText("")).append(block.getName());
		String blockName = blockNameMutable.getString().toLowerCase();
		if(blockName.equalsIgnoreCase("void air")) return;
    	int posX = blockPos.getX();
    	int posY = blockPos.getY();
    	int posZ = blockPos.getZ();
    	
    	if(blockName.contains("lava") && !modInit.mainThreadMap.containsKey("lava_detector_key")) {
			if(!modInit.mainThreadMap.containsKey("lava_detector_key")) {
				client.player.sendMessage(new LiteralText("Warning Lava"), true);
				modInit.mainThreadMap.put("lava_detector_key", 5000);
				return;
			}
		}
    	
    	if(blockName.contains("water") && !modInit.mainThreadMap.containsKey("lava_detector_key") && !modInit.mainThreadMap.containsKey("water_detector_key")) {
			if(!modInit.mainThreadMap.containsKey("water_detector_key")) {
				client.player.sendMessage(new LiteralText("Warning Water"), true);
				modInit.mainThreadMap.put("water_detector_key", 5000);
				return;
			}
		}
    	
    	if(blockName.contains("air")) {
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
}
