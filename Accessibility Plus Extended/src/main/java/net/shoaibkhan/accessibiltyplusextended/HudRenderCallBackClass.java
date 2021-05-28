package net.shoaibkhan.accessibiltyplusextended;

import java.awt.AWTException;
import java.awt.Robot;

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
    private static FallDetectorThread[] fallDetectorThreads = {new FallDetectorThread(),new FallDetectorThread(),new FallDetectorThread()};
    private static OreDetectorThread[] oreDetectorThreads = {new OreDetectorThread(),new OreDetectorThread(),new OreDetectorThread()};
    private static Entity lockedOnEntity = null;
    private int minColumn = 0;
    private int maxColumn = 0;
    private int currentColumn = 0;
    private int differenceColumn = 0;
    private int minRow = 0;
    private int maxRow = 0;
    private int currentRow = 0;
    private int differenceRow = 0;
    private boolean isDPressed, isAPressed, isWPressed, isSPressed;
    public static boolean isTradeScreenOpen = false;
    
    public  HudRenderCallBackClass(KeyBinding CONFIG_KEY,KeyBinding LockEntityKey){
        
        
        
        HudRenderCallback.EVENT.register((__, ___) -> {
        	this.client = MinecraftClient.getInstance();
        	if(client.player == null) return;
        	player = client.player;
        	boolean isAltPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.left.alt").getCode())||InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.right.alt").getCode()));
        	
        	isDPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.d").getCode()));
			isAPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.a").getCode()));
			isWPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.w").getCode()));
			isSPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.s").getCode()));
			
			if (client.currentScreen == null) {
				currentColumn = 0;
				currentRow = 0;
				isTradeScreenOpen = false;
            } else {
            	Screen screen =  client.currentScreen;
            	screenHandler(screen);	            	
            }
        	
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
                	if(Config.get(Config.getFalldetectorkey())){
                		for(int i=0; i<fallDetectorThreads.length; i++) {
                			if(!fallDetectorThreads[i].alive) {
                				fallDetectorThreads[i].start();
                			} else if(fallDetectorThreads[i].alive && fallDetectorThreads[i].finished) {
            					fallDetectorThreads[i].interrupt();
                				fallDetectorThreads[i] = new FallDetectorThread();
                				fallDetectorThreads[i].start();
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
        
    private void screenHandler(Screen screen) {
    	MutableText titleMutableText = new LiteralText("").append(screen.getTitle());
    	String titleString = titleMutableText.getString().toLowerCase();
    	{
    		if(!modInit.mainThreadMap.containsKey("stonecutter_result_slot") && titleString.contains("stonecutter")) stonecutterScreen();
			if(titleString.contains("armorer")||titleString.contains("butcher")||titleString.contains("cartographer")||titleString.contains("cleric")||titleString.contains("farmer")||titleString.contains("fisherman")||titleString.contains("fletcher")||titleString.contains("leatherworker")||titleString.contains("librarian")||titleString.contains("mason")||titleString.contains("shepherd")||titleString.contains("toolsmith")||titleString.contains("weaponsmith")){
				try {
					isTradeScreenOpen = true;
					System.out.println("XX:"+client.getWindow().getScaledWidth()+"\tYY:"+client.getWindow().getScaledHeight());
					System.out.println("X:"+client.mouse.getX()+"\tY:"+client.mouse.getY());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
    	}
    }

	private void stonecutterScreen() {
		try {
			minColumn = 635;
			maxColumn = 785;
			minRow = 270;
			maxRow = 390;
			differenceColumn = 50;
			differenceRow = 60;
			System.out.println("XX:"+client.getWindow().getScaledWidth()+"\tYY:"+client.getWindow().getScaledHeight());
			Robot robot;
			robot = new Robot();
			
			if(isDPressed) {
				if(currentColumn==0 && currentRow==0) {
					currentColumn = minColumn;
					currentRow = minRow;
				} else if(currentColumn == maxColumn) {
					currentColumn += 0;
				} else {
					currentColumn += differenceColumn; 
				}
				robot.mouseMove(currentColumn, currentRow);
				modInit.mainThreadMap.put("stonecutter_result_slot", 200);
			} else if(isAPressed) {
				if(currentColumn==0 && currentRow==0) {
					currentColumn = minColumn;
					currentRow = minRow;
				} else if(currentColumn == minColumn) {
					currentColumn -= 0;
				} else {
					currentColumn -= differenceColumn; 
				}
				robot.mouseMove(currentColumn, currentRow);
				modInit.mainThreadMap.put("stonecutter_result_slot", 200);
			} else if(isSPressed) {
				if(currentColumn==0 && currentRow==0) {
					currentColumn = minColumn;
					currentRow = minRow;
				} else if(currentRow == maxRow) {
					currentRow += 0;
				} else {
					currentRow += differenceRow; 
				}
				robot.mouseMove(currentColumn, currentRow);
				modInit.mainThreadMap.put("stonecutter_result_slot", 200);
			} else if(isWPressed) {
				if(currentColumn==0 && currentRow==0) {
					currentColumn = minColumn;
					currentRow = minRow;
				} else if(currentRow == minRow) {
					currentRow -= 0;
				} else {
					currentRow -= differenceRow; 
				}
				robot.mouseMove(currentColumn, currentRow);
				modInit.mainThreadMap.put("stonecutter_result_slot", 200);
			}
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
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
					String name = "";
					MutableText blockMutableText = new LiteralText("").append(block.getName());
					name = blockMutableText.getString();
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
							&& !modInit.mainThreadMap.containsKey("entity_narrator_key")) {

						tempEntity = ((EntityHitResult) hit).getEntity().getType() + "";
						tempEntityPos = ((EntityHitResult) hit).hashCode() + "";
						tempBlockPos = "";
						tempBlock = "";
						MutableText entityMutableText = new LiteralText("").append(entityHitResult.getEntity().getName());
						text = entityMutableText.getString();
						narrate(text);
						modInit.mainThreadMap.put("entity_narrator_key", 5000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			break;
        }
    }

    private void narrate(String st){
        client.player.sendMessage(new LiteralText(st), true);
    }
    
}
