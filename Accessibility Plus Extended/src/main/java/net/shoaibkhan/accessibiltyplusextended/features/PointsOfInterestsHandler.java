package net.shoaibkhan.accessibiltyplusextended.features;

import java.util.Map;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class PointsOfInterestsHandler extends Thread {
    public static Map<Double, BlockPos> oreBlocks;
    public static Map<Double, BlockPos> doorBlocks;
    public static Map<Double, BlockPos> buttonBlocks;
    public static Map<Double, BlockPos> blocks;
    public static Map<Double, Entity> passiveEntity;
    public static Map<Double, Entity> hostileEntity;
    private MinecraftClient client;

    public void run() {
        client = MinecraftClient.getInstance();
        while (true) {
            if(client==null) continue;
            if(client.player==null) continue;
            try {
                new POIBlocks();
                new POIEntities();

                System.out.println(passiveEntity);
                System.out.println(hostileEntity);
                System.out.println(doorBlocks);

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}