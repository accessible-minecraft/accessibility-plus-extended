package net.shoaibkhan.accessibiltyplusextended.features;

import java.util.TreeMap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class PointsOfInterestsHandler extends Thread {
    public static TreeMap<Double, Vec3d> oreBlocks    = new TreeMap<>();
    public static TreeMap<Double, Vec3d> doorBlocks   = new TreeMap<>();
    public static TreeMap<Double, Vec3d> buttonBlocks = new TreeMap<>();
    public static TreeMap<Double, Vec3d> blocks       = new TreeMap<>();
    public static TreeMap<Double, Entity> passiveEntity  = new TreeMap<>();
    public static TreeMap<Double, Entity> hostileEntity  = new TreeMap<>();
    private MinecraftClient client;

    public void run() {
        client = MinecraftClient.getInstance();
        while (true) {
            if(client==null) continue;
            if(client.player==null) continue;
            if(client.isPaused()) continue;
            try {
                Thread.sleep(1000);

                new POIBlocks();
                new POIEntities();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}