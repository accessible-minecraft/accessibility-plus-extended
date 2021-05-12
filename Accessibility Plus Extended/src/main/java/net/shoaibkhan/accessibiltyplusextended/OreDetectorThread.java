package net.shoaibkhan.accessibiltyplusextended;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class OreDetectorThread extends Thread {
	public boolean finished = false, alive=false;
	private MinecraftClient client; 
	public void run() {
		alive=true;
		client = MinecraftClient.getInstance();
    	BlockPos pos = client.player.getBlockPos();
    	int posX = pos.getX();
    	int posY = pos.getY()-1;
    	int posZ = pos.getZ();
    	checkBlock(new BlockPos(new Vec3d(posX, posY, posZ)), 0);
    	checkBlock(new BlockPos(new Vec3d(posX, posY+3, posZ)), 0);
		checkBlock(new BlockPos(new Vec3d(posX, posY+1, posZ)), 4);
		checkBlock(new BlockPos(new Vec3d(posX, posY+2, posZ)), 4);
		finished = true;
    }
	

	private void checkBlock(BlockPos blockPos, int val) {
		Block block = client.world.getBlockState(blockPos).getBlock();
		String name = block.getTranslationKey();
		if(name.contains("void_air")) return;
		name = name.substring(name.lastIndexOf(".")+1);
		if(name.contains("_")) name = name.replace("_", " ");
		String fluidState = block.getFluidState(client.world.getBlockState(blockPos))+"";
		fluidState = fluidState.toLowerCase();
//		System.out.println(name+"\t"+fluidState+"\t"+blockPos);
		
		if(fluidState.contains("lavafluid") && !modInit.ores.containsKey("Warning Lava Ore Detector")) {
			if(!modInit.ores.containsKey("Warning Lava Ore Detector")) {
				client.player.sendMessage(new LiteralText("Warning Lava"), true);
				modInit.ores.put("Warning Lava Ore Detector", 5000);
			}
		}
		
		if( name.contains("ore") && !modInit.ores.containsKey(name+""+blockPos)) {
			try {
				if(Config.get(Config.getOredetectorcustomsoundkey())) {
					client.world.playSound(blockPos, modInit.oreSoundEvent, SoundCategory.BLOCKS, 0.5f, 1f, true);
				} else { 
					client.world.playSound(blockPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f, true);
				}
			} catch (Exception e) {
			}
			modInit.ores.put(name+""+blockPos, 10000);
		} else if( name.contains("air") && val-1>=0 ) {
	    	int posX = blockPos.getX();
	    	int posY = blockPos.getY();
	    	int posZ = blockPos.getZ();
			checkBlock(new BlockPos(new Vec3d(posX, posY, posZ-1)), val-1); // North Block
			checkBlock(new BlockPos(new Vec3d(posX, posY, posZ+1)), val-1); // South Block
			checkBlock(new BlockPos(new Vec3d(posX-1, posY, posZ)), val-1); // West Block
			checkBlock(new BlockPos(new Vec3d(posX+1, posY, posZ)), val-1); // East Block
			checkBlock(new BlockPos(new Vec3d(posX, posY+1, posZ)), val-1); // Top Block
			checkBlock(new BlockPos(new Vec3d(posX, posY-1, posZ)), val-1); // Bottom Block
		}		
	}

}
