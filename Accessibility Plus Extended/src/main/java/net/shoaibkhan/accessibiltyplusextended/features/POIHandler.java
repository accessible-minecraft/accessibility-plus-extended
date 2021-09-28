package net.shoaibkhan.accessibiltyplusextended.features;

import java.util.TreeMap;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;

public class POIHandler {
    public static TreeMap<Double, Vec3d> oreBlocks = new TreeMap<>();
    public static TreeMap<Double, Vec3d> doorBlocks = new TreeMap<>();
    public static TreeMap<Double, Vec3d> buttonBlocks = new TreeMap<>();
    public static TreeMap<Double, Vec3d> blocks = new TreeMap<>();
    public static TreeMap<Double, Vec3d> ladderBlocks = new TreeMap<>();
    public static TreeMap<Double, Vec3d> leverBlocks = new TreeMap<>();
    public static TreeMap<Double, Vec3d> trapDoorBlocks = new TreeMap<>();
    public static TreeMap<Double, Entity> passiveEntity = new TreeMap<>();
    public static TreeMap<Double, Entity> hostileEntity = new TreeMap<>();
    public static TreeMap<Double, Entity> eyeOfEnderEntity = new TreeMap<>();

    public static String[] volume = { "0", "0.05", "0.1", "0.15", "0.2", "0.25", "0.3", "0.35", "0.4", "0.45", "0.5",
            "0.55", "0.6", "0.65", "0.7", "0.75", "0.8", "0.85", "0.9", "0.95", "1" };
    public static String[] range = { "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" };
    public static String[] delay = { "1" ,"2","3","4" ,"5", "6", "7", "8", "9", "10"};

    public static int getRange(){
        try {
            int index = Config.getInt(ConfigKeys.POI_RANGE.getKey());
            return Integer.parseInt(range[index]);
        } catch (Exception e) {
            e.printStackTrace();
            return 6;
        }
    }

    public static int getDelay(){
        try {
            int index = Config.getInt(ConfigKeys.POI_DELAY.getKey());
            return Integer.parseInt(delay[index]) * 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return 3000;
        }
    }

    public static int getFluidDetectorDelay(){
        try {
            int index = Config.getInt(ConfigKeys.POI_FLUID_DETECTOR_DELAY.getKey());
            return Integer.parseInt(delay[index]) * 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return 3000;
        }
    }

    public static float getVolume(){
        try {
            int index = Config.getInt(ConfigKeys.POI_VOLUME.getKey());
            return Float.parseFloat(volume[index]);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.25f;
        }
    }

    public static float getUnlockingSoundVolume(){
        try {
            int index = Config.getInt(ConfigKeys.POI_UNLOCKING_SOUND_VOLUME.getKey());
            return Float.parseFloat(volume[index]);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.4f;
        }
    }
}