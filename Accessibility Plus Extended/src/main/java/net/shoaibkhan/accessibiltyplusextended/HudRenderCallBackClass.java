package net.shoaibkhan.accessibiltyplusextended;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.Locale;

public class HudRenderCallBackClass {
    private MinecraftClient client;
    private String tempBlock, tempBlockPos;
    private String tempEntity="",tempEntityPos="";
    public  HudRenderCallBackClass(){
        client = MinecraftClient.getInstance();
        HudRenderCallback.EVENT.register((__,___) -> {
            if (!client.isPaused())crosshairTarget();
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
                EntityHitResult entityHitResult = (EntityHitResult) hit;
                if(!(((EntityHitResult) hit).getEntity().getType()+"").equalsIgnoreCase(tempEntity) || !(((EntityHitResult) hit).getEntity().getBlockPos()+"").equalsIgnoreCase(tempEntityPos)) {
                    tempEntity = ((EntityHitResult) hit).getEntity().getType()+"";
                    tempEntityPos = ((EntityHitResult) hit).getEntity().getBlockPos()+"";
                    tempBlockPos = "";
                    tempBlock = "";
                    text = entityHitResult.getEntity().getType() + "";
                    text = text.substring(text.lastIndexOf('.') + 1);
                    if (text.contains("_")) text = text.replace("_", " ");
                    System.out.println(text);
                    narrate(text);
                }
                break;
        }
    }

    private void narrate(String st){
        client.player.sendMessage(new LiteralText(st), true);
    }
}
