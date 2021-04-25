package net.shoaibkhan.accessibiltyplusextended;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class HudRenderCallBackClass {
    private MinecraftClient client;
    private String tempBlock="", tempBlockPos="";
    private String tempEntity="",tempEntityPos="";
    public static int fallDetectorFlag = 0;
    public static CustomWait fDObjCustomWait;
    private static FallDetectorThread[] fallDetectorThreads = {new FallDetectorThread(),new FallDetectorThread(),new FallDetectorThread()};
    private static int fallDetectorThreadsFlag = 0;
    
    public  HudRenderCallBackClass(){
        client = MinecraftClient.getInstance();
        fDObjCustomWait = new CustomWait();
        HudRenderCallback.EVENT.register((__,___) -> {
            try {
                if (!client.isPaused()) {
                	if(10000-fallDetectorFlag>=3000){
                		crosshairTarget();
                	}
                	if(fallDetectorFlag<=0){
                		for(int i=0; i<fallDetectorThreads.length; i++) {
                			if(!fallDetectorThreads[i].alive) {
                				fallDetectorThreads[i].start();
                			} else if(i==fallDetectorThreads.length-1) {
                				if(fallDetectorThreads[fallDetectorThreadsFlag].alive) fallDetectorThreads[fallDetectorThreadsFlag].interrupt();
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
                    if (!(((EntityHitResult) hit).getEntity().getType() + "").equalsIgnoreCase(tempEntity) || !(((EntityHitResult) hit).getEntity().getBlockPos() + "").equalsIgnoreCase(tempEntityPos)) {
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
