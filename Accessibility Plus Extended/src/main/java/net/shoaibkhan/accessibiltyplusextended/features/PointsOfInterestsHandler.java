package net.shoaibkhan.accessibiltyplusextended.features;

import net.minecraft.block.DoorBlock;
import net.minecraft.util.math.Vec3d;

import java.util.Map;

public class PointsOfInterestsHandler extends Thread {
    public static Map<Double, String> oreBlocks;
    public static Map<Double, Vec3d> doorBlocks;
    public static Map<Double, Vec3d> buttonBlocks;

    public void run(){
        while(true){
            try {
                new POIBlocks();

                System.out.println(oreBlocks);
                System.out.println(doorBlocks);
                System.out.println(buttonBlocks);

                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
