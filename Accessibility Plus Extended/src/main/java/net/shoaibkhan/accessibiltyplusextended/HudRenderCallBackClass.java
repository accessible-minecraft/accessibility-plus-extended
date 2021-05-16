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
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
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
    private PlayerEntity player;
    private String tempBlock="", tempBlockPos="";
    private String tempEntity="",tempEntityPos="";
    public static int fallDetectorFlag = 0, entityNarratorFlag = 0,oreDetectorFlag = 0;
    public static CustomWait fDObjCustomWait,ODObjCustomWait,entityNarrator, oreThread;
    private static FallDetectorThread[] fallDetectorThreads = {new FallDetectorThread(),new FallDetectorThread(),new FallDetectorThread()};
    private static int fallDetectorThreadFlag = 0;
    private static OreDetectorThread[] oreDetectorThreads = {new OreDetectorThread(),new OreDetectorThread(),new OreDetectorThread()};
    public static int oreDetectorThreadFlag = 0;
    private static Entity lockedOnEntity = null;
    
    public  HudRenderCallBackClass(KeyBinding CONFIG_KEY,KeyBinding LockEntityKey){
        fDObjCustomWait = new CustomWait();
        ODObjCustomWait = new CustomWait();
        entityNarrator = new CustomWait();
        
        
        
        HudRenderCallback.EVENT.register((__, ___) -> {
        	this.client = MinecraftClient.getInstance();
        	if(client.player == null) return;
        	player = client.player;
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
            		Entity toBeLocked = entityLocking();
            		if(toBeLocked!=null) {
            			MutableText mutableText = (new LiteralText("")).append(toBeLocked.getName());
            			player.sendMessage(new LiteralText(mutableText.getString()+" "+get_position_difference(toBeLocked.getBlockPos())), true);
            			lockedOnEntity = toBeLocked;
            		}
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
                	if(Config.get(Config.getOredetectorkey())){
                		for(int i=0; i<oreDetectorThreads.length; i++) {
                			if(!oreDetectorThreads[i].alive) {
                				oreDetectorThreads[i].start();
                			} else if(oreDetectorThreads[i].alive  && oreDetectorThreads[i].finished) {
                				oreDetectorThreads[i].interrupt();
                				oreDetectorThreads[i] = new OreDetectorThread();
                				oreDetectorThreads[i].start();
                			}
                		}
                	}
                }
            } catch (Exception e) {
            }
            
        });
    }
    
    private String get_position_difference(BlockPos blockPos) {
    	String dir = client.player.getHorizontalFacing().asString();
        dir = dir.toLowerCase().trim();
        
        String diffXBlockPos = ((double)player.getBlockPos().getX() - blockPos.getX()) + "";
        String diffYBlockPos = ((double)(player.getBlockPos().getY()+1) - blockPos.getY())+ "";
        String diffZBlockPos = ((double)player.getBlockPos().getZ() - blockPos.getZ()) + "";
        
        diffXBlockPos = diffXBlockPos.substring(0, diffXBlockPos.indexOf("."));
        diffYBlockPos = diffYBlockPos.substring(0, diffYBlockPos.indexOf("."));
        diffZBlockPos = diffZBlockPos.substring(0, diffZBlockPos.indexOf("."));
        
        if(!diffXBlockPos.equalsIgnoreCase("0")) {
        	if(dir.contains("east")||dir.contains("west")) {
        		if(diffXBlockPos.contains("-")) diffXBlockPos = diffXBlockPos.replace("-", "");
        		diffXBlockPos += " blocks away";
        	} else if(dir.contains("north")) {
        		if(diffXBlockPos.contains("-")) diffXBlockPos += " blocks to left";
        		else diffXBlockPos += " blocks to right";
        		if(diffXBlockPos.contains("-")) diffXBlockPos = diffXBlockPos.replace("-", "");
        	} else if(dir.contains("south")) {
        		if(diffXBlockPos.contains("-")) diffXBlockPos += " blocks to right";
        		else diffXBlockPos += " blocks to left";
        		if(diffXBlockPos.contains("-")) diffXBlockPos = diffXBlockPos.replace("-", "");
        	}
        } else {
        	diffXBlockPos = "";
        }
        
        if(!diffYBlockPos.equalsIgnoreCase("0")) {
        	if(diffYBlockPos.contains("-")) {
        		diffYBlockPos = diffYBlockPos.replace("-", "");
        		diffYBlockPos += " blocks up";
        	} else {
        		diffYBlockPos += " blocks down";
        	}
        } else {
        	diffYBlockPos = "";
        }
        
        if(!diffZBlockPos.equalsIgnoreCase("0")) {
        	if(dir.contains("north")||dir.contains("south")) {
        		if(diffZBlockPos.contains("-")) diffZBlockPos = diffZBlockPos.replace("-", "");
        		diffZBlockPos += " blocks away";
        	} else if(dir.contains("east")) {
        		if(diffZBlockPos.contains("-")) diffZBlockPos += " blocks to right";
        		else diffZBlockPos += " blocks to left";
        		if(diffZBlockPos.contains("-")) diffZBlockPos = diffZBlockPos.replace("-", "");
        	} else if(dir.contains("west")) {
        		if(diffZBlockPos.contains("-")) diffZBlockPos += " blocks to left";
        		else diffZBlockPos += " blocks to right";
        		if(diffZBlockPos.contains("-")) diffZBlockPos = diffZBlockPos.replace("-", "");
        	}
        } else {
        	diffZBlockPos = "";
        }
        
        String text = "";
        if(dir.contains("north")||dir.contains("south")) text = String.format("%s  %s  %s", diffZBlockPos, diffYBlockPos, diffXBlockPos);
        else text = String.format("%s  %s  %s", diffXBlockPos, diffYBlockPos, diffZBlockPos);
        return text;
    }
    
    private Entity entityLocking() {
    	{
    		double closestDouble=-99999;
    		Entity closestEntity = null;
    		try {
				for (Entity i : client.world.getEntities()) {
					if (!(i instanceof MobEntity)) continue;
					BlockPos blockPos = i.getBlockPos();
					
					Vec3d entityVec3d = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
					Vec3d playerVec3d = new Vec3d(client.player.getBlockPos().getX(), client.player.getBlockPos().getY(), client.player.getBlockPos().getZ());
					if(closestDouble==-99999 || closestDouble>entityVec3d.distanceTo(playerVec3d)) {
						closestDouble = entityVec3d.distanceTo(playerVec3d);
						closestEntity = i;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    		if(closestDouble>10.0) closestEntity = null;
			return closestEntity;
    	}
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
					if(((EntityHitResult) hit).getEntity()==lockedOnEntity) break;
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
