package net.shoaibkhan.accessibiltyplusextended;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FallDetectorThread extends Thread {
	public boolean alive = false;
	
	public void run() {
		alive = true;
		MinecraftClient client = MinecraftClient.getInstance();
    	BlockPos pos = client.player.getBlockPos();
    	int posX = pos.getX();
    	int posY = pos.getY();
    	int posZ = pos.getZ();
    	int iFac = 0, jFac = 0;
    	String dir = client.player.getHorizontalFacing().toString().toLowerCase().trim();
    	if(dir.equalsIgnoreCase("north")) {
    		iFac = posX + 1;
    		posX--;
    		posZ--;
    		jFac = posZ - 5;
	    	
	    	for(int i = posX; i <= iFac; i++) {
	    		posY = pos.getY();
	    		for(int j = posZ; j >= jFac; j--) {
	    			BlockState blockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,posY,j)));
	    			String name = blockEntity.getBlock().getTranslationKey();
	    			if(name.contains("void_air")) return;
	    			name = name.substring(name.lastIndexOf(".")+1);
	    			if(name.contains("_")) name = name.replace("_", "");
	    			if(name.equals("air")) {
	    				int tempY = posY-1;
	    				while(tempY>1) {
	    					BlockState tempblockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,tempY,j)));
	    	    			String tempname = tempblockEntity.getBlock().getTranslationKey();
	    	    			if(tempname.contains("void_air")) return;
	    	    			tempname = tempname.substring(tempname.lastIndexOf(".")+1);
	    	    			if(tempname.contains("_")) tempname = tempname.replace("_", "");
	    	    			if (tempname.contains("air")) {
								tempY--;
							} else {
								if(posY-tempY >= 5) {
									client.player.sendMessage(new LiteralText("Warning Fall Detected"), true);
									if(HudRenderCallBackClass.fDObjCustomWait.isAlive()) HudRenderCallBackClass.fDObjCustomWait.stopThread();
									HudRenderCallBackClass.fDObjCustomWait = new CustomWait();
									HudRenderCallBackClass.fDObjCustomWait.setWait(5000, 1, client);
									HudRenderCallBackClass.fDObjCustomWait.startThread();
									return;
								} else {
									break;
								}
							}
	    				}
	    			} else {
	    				int tempY = posY+1;
	    				while(tempY<posY+4) {
	    					BlockState tempblockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,tempY,j)));
	    	    			String tempname = tempblockEntity.getBlock().getTranslationKey();
	    	    			if(tempname.contains("void_air")) return;
	    	    			tempname = tempname.substring(tempname.lastIndexOf(".")+1);
	    	    			if(tempname.contains("_")) tempname = tempname.replace("_", "");
	    	    			if (!tempname.contains("air")) {
								tempY++;
							} else {
								break;
							}
	    	    			if(tempY-posY>=3) break;
	    				}
	    			}
	    		}
	    	}
    	} else if(dir.equalsIgnoreCase("south")) {
    		iFac = posX + 1;
    		posX--;
    		posZ++;
    		jFac = posZ + 5;
	    	
	    	for(int i = posX; i <= iFac; i++) {
	    		posY = pos.getY();
	    		for(int j = posZ; j <= jFac; j++) {
	    			BlockState blockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,posY,j)));
	    			String name = blockEntity.getBlock().getTranslationKey();
	    			if(name.contains("void_air")) return;
	    			name = name.substring(name.lastIndexOf(".")+1);
	    			if(name.contains("_")) name = name.replace("_", "");
	    			if(name.equals("air")) {
	    				int tempY = posY-1;
	    				while(tempY>1) {
	    					BlockState tempblockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,tempY,j)));
	    	    			String tempname = tempblockEntity.getBlock().getTranslationKey();
	    	    			if(tempname.contains("void_air")) return;
	    	    			tempname = tempname.substring(tempname.lastIndexOf(".")+1);
	    	    			if(tempname.contains("_")) tempname = tempname.replace("_", "");
	    	    			if (tempname.contains("air")) {
								tempY--;
							} else {
								if(posY-tempY >= 5) {
									client.player.sendMessage(new LiteralText("Warning Fall Detected"), true);
									if(HudRenderCallBackClass.fDObjCustomWait.isAlive()) HudRenderCallBackClass.fDObjCustomWait.stopThread();
									HudRenderCallBackClass.fDObjCustomWait = new CustomWait();
									HudRenderCallBackClass.fDObjCustomWait.setWait(5000, 1, client);
									HudRenderCallBackClass.fDObjCustomWait.startThread();
									return;
								} else {
									break;
								}
							}
	    				}
	    			} else {
	    				int tempY = posY+1;
	    				while(tempY<posY+4) {
	    					BlockState tempblockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,tempY,j)));
	    	    			String tempname = tempblockEntity.getBlock().getTranslationKey();
	    	    			if(tempname.contains("void_air")) return;
	    	    			tempname = tempname.substring(tempname.lastIndexOf(".")+1);
	    	    			if(tempname.contains("_")) tempname = tempname.replace("_", "");
	    	    			if (!tempname.contains("air")) {
								tempY++;
							} else {
								break;
							}
	    	    			if(tempY-posY>=3) break;
	    				}
	    			}
	    		}
	    	}
    	} else if(dir.equalsIgnoreCase("east")) {
    		jFac = posZ + 1;
    		posX++;
    		posZ--;
    		iFac = posX + 5;
	    	
	    	for(int j = posZ; j <= jFac; j++) {
	    		posY = pos.getY();
	    		for(int i = posX; i <= iFac; i++) {
	    			BlockState blockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,posY,j)));
	    			String name = blockEntity.getBlock().getTranslationKey();
	    			if(name.contains("void_air")) return;
	    			name = name.substring(name.lastIndexOf(".")+1);
	    			if(name.contains("_")) name = name.replace("_", "");
	    			if(name.equals("air")) {
	    				int tempY = posY-1;
	    				while(tempY>1) {
	    					BlockState tempblockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,tempY,j)));
	    	    			String tempname = tempblockEntity.getBlock().getTranslationKey();
	    	    			if(tempname.contains("void_air")) return;
	    	    			tempname = tempname.substring(tempname.lastIndexOf(".")+1);
	    	    			if(tempname.contains("_")) tempname = tempname.replace("_", "");
	    	    			if (tempname.contains("air")) {
								tempY--;
							} else {
								if(posY-tempY >= 5) {
									client.player.sendMessage(new LiteralText("Warning Fall Detected"), true);
									if(HudRenderCallBackClass.fDObjCustomWait.isAlive()) HudRenderCallBackClass.fDObjCustomWait.stopThread();
									HudRenderCallBackClass.fDObjCustomWait = new CustomWait();
									HudRenderCallBackClass.fDObjCustomWait.setWait(5000, 1, client);
									HudRenderCallBackClass.fDObjCustomWait.startThread();
									return;
								} else {
									break;
								}
							}
	    				}
	    			} else {
	    				int tempY = posY+1;
	    				while(tempY<posY+4) {
	    					BlockState tempblockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,tempY,j)));
	    	    			String tempname = tempblockEntity.getBlock().getTranslationKey();
	    	    			if(tempname.contains("void_air")) return;
	    	    			tempname = tempname.substring(tempname.lastIndexOf(".")+1);
	    	    			if(tempname.contains("_")) tempname = tempname.replace("_", "");
	    	    			if (!tempname.contains("air")) {
								tempY++;
							} else {
								break;
							}
	    	    			if(tempY-posY>=3) break;
	    				}
	    			}
	    		}
	    	}
    	} else if(dir.equalsIgnoreCase("west")) {
    		jFac = posZ + 1;
    		posX--;
    		posZ--;
    		iFac = posX - 5;
	    	
	    	for(int j = posZ; j <= jFac; j++) {
	    		posY = pos.getY();
	    		for(int i = posX; i >= iFac; i--) {
	    			BlockState blockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,posY,j)));
	    			String name = blockEntity.getBlock().getTranslationKey();
	    			if(name.contains("void_air")) return;
	    			name = name.substring(name.lastIndexOf(".")+1);
	    			if(name.contains("_")) name = name.replace("_", "");
	    			if(name.equals("air")) {
	    				int tempY = posY-1;
	    				while(tempY>1) {
	    					BlockState tempblockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,tempY,j)));
	    	    			String tempname = tempblockEntity.getBlock().getTranslationKey();
	    	    			if(tempname.contains("void_air")) return;
	    	    			tempname = tempname.substring(tempname.lastIndexOf(".")+1);
	    	    			if(tempname.contains("_")) tempname = tempname.replace("_", "");
	    	    			if (tempname.contains("air")) {
								tempY--;
							} else {
								if(posY-tempY >= 5) {
									client.player.sendMessage(new LiteralText("Warning Fall Detected"), true);
									if(HudRenderCallBackClass.fDObjCustomWait.isAlive()) HudRenderCallBackClass.fDObjCustomWait.stopThread();
									HudRenderCallBackClass.fDObjCustomWait = new CustomWait();
									HudRenderCallBackClass.fDObjCustomWait.setWait(5000, 1, client);
									HudRenderCallBackClass.fDObjCustomWait.startThread();
									return;
								} else {
									break;
								}
							}
	    				}
	    			} else {
	    				int tempY = posY+1;
	    				while(tempY<posY+4) {
	    					BlockState tempblockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,tempY,j)));
	    	    			String tempname = tempblockEntity.getBlock().getTranslationKey();
	    	    			if(tempname.contains("void_air")) return;
	    	    			tempname = tempname.substring(tempname.lastIndexOf(".")+1);
	    	    			if(tempname.contains("_")) tempname = tempname.replace("_", "");
	    	    			if (!tempname.contains("air")) {
								tempY++;
							} else {
								break;
							}
	    	    			if(tempY-posY>=3) break;
	    				}
	    			}
	    		}
	    	}
    	}
    }

}
