package net.shoaibkhan.accessibiltyplusextended;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.gui.ConfigGui;
import net.shoaibkhan.accessibiltyplusextended.gui.ConfigScreen;

public class HudRenderCallBackClass {
    private MinecraftClient client;
    private String tempBlock="", tempBlockPos="";
    private String tempEntity="",tempEntityPos="";
    public static int fallDetectorFlag = 0, entityNarratorFlag = 0,oreDetectorFlag = 0;
    public static CustomWait fDObjCustomWait,entityNarrator, oreThread;
    private static FallDetectorThread[] fallDetectorThreads = {new FallDetectorThread(),new FallDetectorThread(),new FallDetectorThread()};
    private static int fallDetectorThreadFlag = 0;
    private static OreDetectorThread[] oreDetectorThreads = {new OreDetectorThread(),new OreDetectorThread(),new OreDetectorThread()};
    private static int oreDetectorThreadFlag = 0;
    private static Entity lockedOnEntity = null;
    
    public  HudRenderCallBackClass(KeyBinding CONFIG_KEY,KeyBinding LockEntityKey){
        fDObjCustomWait = new CustomWait();
        entityNarrator = new CustomWait();
        
        
        
        HudRenderCallback.EVENT.register((__, ___) -> {
        	this.client = MinecraftClient.getInstance();
        	if(client.player == null) return;
        	boolean isAltPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.left.alt").getCode())||InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.right.alt").getCode()));
            try {
            	
            	if(lockedOnEntity != null) {
            		if(!lockedOnEntity.isAlive()) lockedOnEntity = null;
	            	Vec3d vec3d = new Vec3d(lockedOnEntity.getX(),lockedOnEntity.getY()+lockedOnEntity.getHeight()-0.25,lockedOnEntity.getZ());
	            	client.player.lookAt(EntityAnchor.EYES, vec3d);
	            	
            	}
            	
            	while(CONFIG_KEY.wasPressed()){
	            	Screen screen = new ConfigScreen(new ConfigGui(client.player,client), "AP Extended Configuration", client.player);
	                client.openScreen(screen);
	                return;
	            }
            	
            	if(isAltPressed) {
            		while(LockEntityKey.wasPressed()){
                		lockedOnEntity = null;
    	            }
            	}
            	
            	while(LockEntityKey.wasPressed()){
            		lockedOnEntity = entityLocking();
	            }

                if ( !client.isPaused() && (client.currentScreen==null))  {
                	
                	// Read Crosshair
                	if(10000-fallDetectorFlag>=3000 && (Config.get(Config.getReadblockskey())||Config.get(Config.getEntitynarratorkey()))){
                		crosshairTarget();
                	}
                	
                	// Fall Detector
                	if(fallDetectorFlag<=0&&Config.get(Config.getFalldetectorkey())){
                		for(int i=0; i<fallDetectorThreads.length; i++) {
                			if(!fallDetectorThreads[i].alive) {
                				fallDetectorThreads[i].start();
                			} else if(i==fallDetectorThreads.length-1) {
                				if(fallDetectorThreads[fallDetectorThreadFlag].alive) {
                					fallDetectorThreads[fallDetectorThreadFlag].interrupt();
                					fallDetectorFlag = 0;
                				}
                				fallDetectorThreads[fallDetectorThreadFlag] = new FallDetectorThread();
                				fallDetectorThreads[fallDetectorThreadFlag].start();
                				fallDetectorThreadFlag++;
                				if(fallDetectorThreadFlag==fallDetectorThreads.length) fallDetectorThreadFlag = 0;
                			}
                		}
                	}
                	
                	// Ore Detector
                	if(fallDetectorFlag<=0&&Config.get(Config.getOredetectorkey())){
                		for(int i=0; i<oreDetectorThreads.length; i++) {
                			if(!oreDetectorThreads[i].alive) {
                				oreDetectorThreads[i].start();
                			} else if(i==oreDetectorThreads.length-1) {
                				if(oreDetectorThreads[oreDetectorThreadFlag].alive) {
                					oreDetectorThreads[oreDetectorThreadFlag].interrupt();
                					fallDetectorFlag = 0;
                				}
                				oreDetectorThreads[oreDetectorThreadFlag] = new OreDetectorThread();
                				oreDetectorThreads[oreDetectorThreadFlag].start();
                				oreDetectorThreadFlag++;
                				if(oreDetectorThreadFlag==oreDetectorThreads.length) oreDetectorThreadFlag = 0;
                			}
                		}
                	}
                }
            } catch (Exception e) {
            }
            
        });
    }

    private Entity entityLocking() {
    	HitResult hit = client.crosshairTarget;
        switch (hit.getType()) {
            case MISS:
            	return null;
            case BLOCK:
            	return null;
            case ENTITY:
				try {
					EntityHitResult entityHitResult = (EntityHitResult) hit;
	            	BlockPos blockPos = ((EntityHitResult) hit).getEntity().getBlockPos();
	            	Vec3d vec3d = new Vec3d(blockPos.getX(),blockPos.getY(),blockPos.getZ());
	            	client.player.lookAt(EntityAnchor.EYES, vec3d);
	            	return entityHitResult.getEntity();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
        }
		return null;
    }
    
    private void crosshairTarget() {
        HitResult hit = client.crosshairTarget;
        String text = "";
        switch (hit.getType()) {
            case MISS:
                break;
            case BLOCK:
			if (Config.get(Config.getReadblockskey())) {
				BlockHitResult blockHitResult = (BlockHitResult) hit;
				BlockState blockState = client.world.getBlockState(blockHitResult.getBlockPos());
				Block block = blockState.getBlock();
				if ((!tempBlock.equalsIgnoreCase(block + "")
						|| !(tempBlockPos.equalsIgnoreCase(blockHitResult.getBlockPos() + "")))
						&& !(blockState + "").toLowerCase().contains("sign")) {
					tempBlock = block + "";
					tempBlockPos = blockHitResult.getBlockPos() + "";
					tempEntityPos = "";
					tempEntity = "";
					String side = blockHitResult.getSide().asString();
					String name = block.getTranslationKey();
					name = name.substring(name.lastIndexOf('.') + 1);
					if (name.contains("_"))
						name = name.replace("_", " ");
					if (side.equalsIgnoreCase("up"))
						side = "top";
					if (side.equalsIgnoreCase("down"))
						side = "bottom";
					text = name + " " + side;
					narrate(text);
				} 
			}
			break;
            case ENTITY:
            	
			if (Config.get(Config.getEntitynarratorkey())) {
				try {
					EntityHitResult entityHitResult = (EntityHitResult) hit;
					if ((!(((EntityHitResult) hit).getEntity().getDisplayName() + "").equalsIgnoreCase(tempEntity)
							|| !(((EntityHitResult) hit).hashCode() + "").equalsIgnoreCase(tempEntityPos))
							&& entityNarratorFlag <= 0) {

						tempEntity = ((EntityHitResult) hit).getEntity().getType() + "";
						tempEntityPos = ((EntityHitResult) hit).hashCode() + "";
						tempBlockPos = "";
						tempBlock = "";
						text = entityHitResult.getEntity().getType() + "";
						text = text.substring(text.lastIndexOf('.') + 1);
						if (text.contains("_"))
							text = text.replace("_", " ");
						String customNameString = "" + ((EntityHitResult) hit).getEntity().getCustomName();
						if (!customNameString.equalsIgnoreCase("null")) {
							int indexOfText = customNameString.indexOf("text='");
							int index = customNameString.indexOf("'", indexOfText + 6);
							System.out.println(indexOfText + "\t" + index + "\t" + customNameString.length());
							customNameString = customNameString.substring(indexOfText + 6, index);
							text = customNameString;
						}
						narrate(text);
						if (entityNarrator.isAlive()) {
							entityNarrator.stopThread();
							entityNarratorFlag = 0;
						}
						entityNarrator = new CustomWait();
						entityNarrator.setWait(5000, 2, client);
						entityNarrator.startThread();
					}
				} catch (Exception e) {
					try {
						BlockHitResult blockHitResult1 = (BlockHitResult) hit;
						BlockState blockState1 = client.world.getBlockState(blockHitResult1.getBlockPos());
						Block block1 = blockState1.getBlock();
						if ((!tempBlock.equalsIgnoreCase(block1 + "")
								|| !(tempBlockPos.equalsIgnoreCase(blockHitResult1.getBlockPos() + "")))
								&& !(blockState1 + "").toLowerCase().contains("sign")) {
							tempBlock = block1 + "";
							tempBlockPos = blockHitResult1.getBlockPos() + "";
							tempEntityPos = "";
							tempEntity = "";
							String side = blockHitResult1.getSide().asString();
							String name = block1.getTranslationKey();
							name = name.substring(name.lastIndexOf('.') + 1);
							if (name.contains("_"))
								name = name.replace("_", " ");
							text = name + ", " + side + " face";
							narrate(text);
						}
					} catch (Exception e1) {
						System.out.println(e1);
					}
				} 
			}
			break;
        }
    }

    private void narrate(String st){
        client.player.sendMessage(new LiteralText(st), true);
    }
    
}
