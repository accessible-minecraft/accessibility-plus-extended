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

    public static Float[] volume = { 0f, 0.05f, 0.1f, 0.15f, 0.2f, 0.25f, 0.3f, 0.35f, 0.4f, 0.45f, 0.5f,
            0.55f, 0.6f, 0.65f, 0.7f, 0.75f, 0.8f, 0.85f, 0.9f, 0.95f, 1f };
    public static Integer[] range = { 5, 6, 7, 8};
    public static Integer[] delay = { 1 ,2,3,4 ,5, 6, 7, 8, 9, 10};

    public static void reInitialize(){
        oreBlocks = new TreeMap<>();
        doorBlocks = new TreeMap<>();
        buttonBlocks = new TreeMap<>();
        blocks = new TreeMap<>();
        ladderBlocks = new TreeMap<>();
        leverBlocks = new TreeMap<>();
        trapDoorBlocks = new TreeMap<>();
        passiveEntity = new TreeMap<>();
        hostileEntity = new TreeMap<>();
        eyeOfEnderEntity = new TreeMap<>();
    }

    public static int getRange(){
        try {
            int index = Config.getInt(ConfigKeys.POI_RANGE.getKey());
            return range[index];
        } catch (Exception e) {
            e.printStackTrace();
            return 6;
        }
    }

    public static int getDelay(){
        try {
            int index = Config.getInt(ConfigKeys.POI_DELAY.getKey());
            return delay[index] * 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return 3000;
        }
    }

    public static int getFluidDetectorDelay(){
        try {
            int index = Config.getInt(ConfigKeys.POI_FLUID_DETECTOR_DELAY.getKey());
            return delay[index] * 1000;
        } catch (Exception e) {
            e.printStackTrace();
            return 3000;
        }
    }

    public static float getVolume(){
        try {
            int index = Config.getInt(ConfigKeys.POI_VOLUME.getKey());
            return volume[index];
        } catch (Exception e) {
            e.printStackTrace();
            return 0.25f;
        }
    }

    public static float getUnlockingSoundVolume(){
        try {
            int index = Config.getInt(ConfigKeys.POI_UNLOCKING_SOUND_VOLUME.getKey());
            return volume[index];
        } catch (Exception e) {
            e.printStackTrace();
            return 0.4f;
        }
    }
}