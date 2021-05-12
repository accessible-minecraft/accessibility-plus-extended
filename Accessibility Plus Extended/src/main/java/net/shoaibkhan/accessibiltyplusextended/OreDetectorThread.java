package net.shoaibkhan.accessibiltyplusextended;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class OreDetectorThread extends Thread {
	public boolean alive = false;
	private MinecraftClient client; 
	public void run() {
		alive = true;
		client = MinecraftClient.getInstance();
    	BlockPos pos = client.player.getBlockPos();
    	int posX = pos.getX();
    	int posY = pos.getY();
    	int posZ = pos.getZ();
		checkBlock(new BlockPos(new Vec3d(posX, posY+1, posZ)), 3);
		checkBlock(new BlockPos(new Vec3d(posX, posY+2, posZ)), 3);
    }
	

	private void checkBlock(BlockPos blockPos, int val) {
		if(HudRenderCallBackClass.fallDetectorFlag <= 0) {
			Block block = client.world.getBlockState(blockPos).getBlock();
			String name = block.getTranslationKey();
			if(name.contains("void_air")) return;
			name = name.substring(name.lastIndexOf(".")+1);
			if(name.contains("_")) name = name.replace("_", " ");
			
	//		System.out.println("fluid\t"+block.getFluidState(client.world.getBlockState(blockPos)));
			String fluidState = block.getFluidState(client.world.getBlockState(blockPos))+"";
			fluidState = fluidState.toLowerCase();
			if(name.contains("ore")) {
	//			client.world.playSound(pos, sound, category, volume, pitch, useDistance);
				System.out.println("ore\t"+name);
			} else if(fluidState.contains("lavafluid") && HudRenderCallBackClass.fallDetectorFlag <= 0) {
				client.player.sendMessage(new LiteralText("Warning Lava"), true);
				if(HudRenderCallBackClass.fDObjCustomWait.isAlive()) { 
					HudRenderCallBackClass.fDObjCustomWait.stopThread();
					HudRenderCallBackClass.fallDetectorFlag = 0;
				}
				HudRenderCallBackClass.fDObjCustomWait = new CustomWait();
				HudRenderCallBackClass.fDObjCustomWait.setWait(5000, 1, client);
				HudRenderCallBackClass.fDObjCustomWait.startThread();
			} else if(fluidState.contains("waterfluid") && HudRenderCallBackClass.fallDetectorFlag <= 0 && false ) {
				client.player.sendMessage(new LiteralText("Warning Water"), true);
				if(HudRenderCallBackClass.fDObjCustomWait.isAlive()) { 
					HudRenderCallBackClass.fDObjCustomWait.stopThread();
					HudRenderCallBackClass.fallDetectorFlag = 0;
				}
				HudRenderCallBackClass.fDObjCustomWait = new CustomWait();
				HudRenderCallBackClass.fDObjCustomWait.setWait(5000, 1, client);
				HudRenderCallBackClass.fDObjCustomWait.startThread();
			} else if(name.equalsIgnoreCase("air") && val-1>=0 ) {
	//	    	BlockPos pos = block.
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

}
