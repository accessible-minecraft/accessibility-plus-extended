package net.shoaibkhan.accessibiltyplusextended.features;

import java.util.Map;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class PointsOfInterestsHandler extends Thread {
    public static Map<Double, String> oreBlocks;
    public static Map<Double, BlockPos> doorBlocks;
    public static Map<Double, BlockPos> buttonBlocks;
    public static Map<Double, BlockPos> blocks;

    public void run(){
        while(true){
            assert MinecraftClient.getInstance().player != null;
            try {
                new POIBlocks();

                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
