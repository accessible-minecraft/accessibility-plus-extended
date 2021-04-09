package net.shoaibkhan.accessibiltyplusextended;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class HudRenderCallBackClass {
    private MinecraftClient client;
    private String tempBlock="", tempBlockPos="";
    private String tempEntity="",tempEntityPos="";
    public  HudRenderCallBackClass(){
        client = MinecraftClient.getInstance();
        HudRenderCallback.EVENT.register((__,___) -> {
            try {
                if (!client.isPaused()) {
                	crosshairTarget();
                	fallDetector();
                }
            } catch (Exception e) {
                System.out.println(e);
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
    
    private void fallDetector() {
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
    		jFac = posZ - 10;
	    	
	    	for(int i = posX; i <= iFac; i++) {
	    		posY = pos.getY();
	    		for(int j = posZ; j >= jFac; j--) {
	    			BlockState blockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,posY,j)));
	    			String name = blockEntity.getBlock().getTranslationKey();
	    			name = name.substring(name.lastIndexOf(".")+1);
	    			if(name.contains("_")) name = name.replace("_", "");
	    			if(name.equals("air")) {
	    				int tempY = posY-1;
	    				while(tempY>1) {
	    					BlockState tempblockEntity = client.world.getBlockState(new BlockPos(new Vec3d(i,posY,j)));
	    	    			String tempname = tempblockEntity.getBlock().getTranslationKey();
	    	    			tempname = tempname.substring(tempname.lastIndexOf(".")+1);
	    	    			if(tempname.contains("_")) tempname = tempname.replace("_", "");
	    	    			
	    				}
	    			}
	    		}
	    		System.out.println();
	    	}
    	}
    }
}
