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
	public static String[] volume = {"0", "0.05", "0.1", "0.15", "0.2", "0.25", "0.3", "0.35", "0.4", "0.45", "0.5", "0.55", "0.6", "0.65", "0.7", "0.75", "0.8", "0.85", "0.9", "0.95", "1"};
	public static String[] pitch = {"0","0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5","-0.5", "-1", "-1.5", "-2", "-2.5", "-3", "-3.5", "-4", "-4.5", "-5"};
	
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
		
		if(name.contains("lava") && !modInit.ores.containsKey("Warning Lava Ore Detector")) {
			if(!modInit.ores.containsKey("Warning Lava Ore Detector")) {
				client.player.sendMessage(new LiteralText("Warning Lava"), true);
				modInit.ores.put("Warning Lava Ore Detector", 5000);
			}
		}
		
		if( name.contains("ore") && !modInit.ores.containsKey(name+""+blockPos)) {
			try {
				Float vol,pit;
				try {
					vol = Float.parseFloat(volume[Config.getInt(Config.getOredetectorvolume())]+"");
				} catch (Exception e) {
					vol = 0.2f;
				}
				try {
					pit = Float.parseFloat(pitch[Config.getInt(Config.getOredetectorpitch())]+"");
				} catch (Exception e) {
					pit = 1f;
				}
				if(Config.get(Config.getOredetectorcustomsoundkey())) {
					client.world.playSound(blockPos, modInit.oreSoundEvent, SoundCategory.BLOCKS, vol, pit, true);
				} else { 
					client.world.playSound(blockPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, vol, pit, true);
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
