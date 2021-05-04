package net.shoaibkhan.accessibiltyplusextended;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.shoaibkhan.accessibiltyplusextended.config.ELConfig;
import net.shoaibkhan.accessibiltyplusextended.gui.ConfigGui;
import net.shoaibkhan.accessibiltyplusextended.gui.ConfigScreen;
import net.shoaibkhan.accessibiltyplusextended.mixin.AccessorHandledScreen;

public class HudRenderCallBackClass {
    private MinecraftClient client;
    private String tempBlock="", tempBlockPos="";
    private String tempEntity="",tempEntityPos="";
    public static int fallDetectorFlag = 0, dPressedCooldownFlag = 0;
    public static CustomWait fDObjCustomWait,dPressedCooldown;
    private static FallDetectorThread[] fallDetectorThreads = {new FallDetectorThread(),new FallDetectorThread(),new FallDetectorThread()};
    private static int fallDetectorThreadsFlag = 0;
    
    public  HudRenderCallBackClass(KeyBinding CONFIG_KEY){
        fDObjCustomWait = new CustomWait();
        dPressedCooldown = new CustomWait();
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
        	this.client = client;
        	if(client.player == null) return;
            try {
            	boolean isDPressed = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.d").getCode());
            	if ( isDPressed && client.currentScreen != null && client.currentScreen instanceof AccessorHandledScreen &&  dPressedCooldownFlag <= 0 && ELConfig.get(ELConfig.getDurabilitycheckerkey())) {
                    Slot hovered = ((AccessorHandledScreen) client.currentScreen).getFocusedSlot();
                    if(hovered!=null && hovered.getStack().isDamageable()) {
                    	client.player.sendMessage(new LiteralText(hovered.getStack().getMaxDamage()-hovered.getStack().getDamage()+" durability"), true);
                    	if(dPressedCooldown.isAlive()) {
                    		dPressedCooldown.stopThread();
                    		dPressedCooldownFlag = 0;
                    	}
                    	dPressedCooldown = new CustomWait();
                    	dPressedCooldown.setWait(1000, 2, client);
                    	dPressedCooldown.startThread();
                    }
            	}
            	
            	while(CONFIG_KEY.wasPressed()){
	            	Screen screen = new ConfigScreen(new ConfigGui(client.player,client), "AP Extended Configuration", client.player);
	                client.openScreen(screen);
	                return;
	            }
            	// ||!(client.currentScreen instanceof AccessorHandledScreen)
                if ( !client.isPaused() && (client.currentScreen==null))  {
                	if(10000-fallDetectorFlag>=3000 && ELConfig.get(ELConfig.getReadcrosshairkey())){
                		crosshairTarget();
                	}
                	if(fallDetectorFlag<=0&&ELConfig.get(ELConfig.getFalldetectorkey())){
                		for(int i=0; i<fallDetectorThreads.length; i++) {
                			if(!fallDetectorThreads[i].alive) {
                				fallDetectorThreads[i].start();
                			} else if(i==fallDetectorThreads.length-1) {
                				if(fallDetectorThreads[fallDetectorThreadsFlag].alive) {
                					fallDetectorThreads[fallDetectorThreadsFlag].interrupt();
                					fallDetectorFlag = 0;
                				}
                				fallDetectorThreads[fallDetectorThreadsFlag] = new FallDetectorThread();
                				fallDetectorThreads[fallDetectorThreadsFlag].start();
                				fallDetectorThreadsFlag++;
                				if(fallDetectorThreadsFlag==fallDetectorThreads.length) fallDetectorThreadsFlag = 0;
                			}
                		}
                	}
                }
            } catch (Exception e) {
            }
            
        });
    }

    private void crosshairTarget() {
        HitResult hit = client.crosshairTarget;
        String text = "";
        switch (hit.getType()) {
            case MISS:
                break;
            case BLOCK:
                BlockHitResult blockHitResult = (BlockHitResult) hit;
                BlockState blockState = client.world.getBlockState(blockHitResult.getBlockPos());
                Block block = blockState.getBlock();
                if ((!tempBlock.equalsIgnoreCase(block+"")||!(tempBlockPos.equalsIgnoreCase(blockHitResult.getBlockPos()+""))) && !(blockState+"").toLowerCase().contains("sign")){
                    tempBlock = block+"";
                    tempBlockPos = blockHitResult.getBlockPos()+"";
                    tempEntityPos = "";
                    tempEntity = "";
                    String side = blockHitResult.getSide().asString();
                    String name = block.getTranslationKey();
                    name = name.substring(name.lastIndexOf('.')+1);
                    if (name.contains("_")) name = name.replace("_"," ");
                    text = name + ", " + side + " face";
                    narrate(text);
                }
                break;
            case ENTITY:
                try{
                    EntityHitResult entityHitResult = (EntityHitResult) hit;
                    if (!(((EntityHitResult) hit).getEntity().getDisplayName() + "").equalsIgnoreCase(tempEntity) || !(((EntityHitResult) hit).getEntity().getBlockPos() + "").equalsIgnoreCase(tempEntityPos)) {
                        tempEntity = ((EntityHitResult) hit).getEntity().getType() + "";
                        tempEntityPos = ((EntityHitResult) hit).getEntity().getBlockPos() + "";
                        tempBlockPos = "";
                        tempBlock = "";
                        text = entityHitResult.getEntity().getType() + "";
                        text = text.substring(text.lastIndexOf('.') + 1);
                        if (text.contains("_")) text = text.replace("_", " ");
                        System.out.println(text);
                        narrate(text);
                    }
                } catch (Exception e) {
                    try{
                        BlockHitResult blockHitResult1 = (BlockHitResult) hit;
                        BlockState blockState1 = client.world.getBlockState(blockHitResult1.getBlockPos());
                        Block block1 = blockState1.getBlock();
                        if ((!tempBlock.equalsIgnoreCase(block1 + "") || !(tempBlockPos.equalsIgnoreCase(blockHitResult1.getBlockPos() + ""))) && !(blockState1 + "").toLowerCase().contains("sign")) {
                            tempBlock = block1 + "";
                            tempBlockPos = blockHitResult1.getBlockPos() + "";
                            tempEntityPos = "";
                            tempEntity = "";
                            String side = blockHitResult1.getSide().asString();
                            String name = block1.getTranslationKey();
                            name = name.substring(name.lastIndexOf('.') + 1);
                            if (name.contains("_")) name = name.replace("_", " ");
                            text = name + ", " + side + " face";
                            narrate(text);
                        }
                    } catch (Exception e1){
                        System.out.println(e1);
                    }
                }
                break;
        }
    }

    private void narrate(String st){
        client.player.sendMessage(new LiteralText(st), true);
    }
    
}
