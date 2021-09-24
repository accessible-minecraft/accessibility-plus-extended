package net.shoaibkhan.accessibiltyplusextended.features;

import java.util.TreeMap;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class POIHandler{
    public static TreeMap<Double, Vec3d> oreBlocks = new TreeMap<>();
    public static TreeMap<Double, Vec3d> doorBlocks = new TreeMap<>();
    public static TreeMap<Double, Vec3d> buttonBlocks = new TreeMap<>();
    public static TreeMap<Double, Vec3d> blocks = new TreeMap<>();
    public static TreeMap<Double, Entity> passiveEntity = new TreeMap<>();
    public static TreeMap<Double, Entity> hostileEntity = new TreeMap<>();
    public static TreeMap<Double, Vec3d> ladderBlocks = new TreeMap<>();
    public static TreeMap<Double, Vec3d> leverBlocks = new TreeMap<>();
}