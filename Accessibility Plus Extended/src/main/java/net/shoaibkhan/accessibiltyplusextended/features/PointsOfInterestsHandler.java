package net.shoaibkhan.accessibiltyplusextended.features;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.Property;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.HudRenderCallBackClass;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;

public class PointsOfInterestsHandler extends Thread {
    public static Map<Double, String> oreBlocks;
    public static Map<Double, BlockPos> doorBlocks;
    public static Map<Double, BlockPos> buttonBlocks;
    public static Map<Double, BlockPos> blocks;
    public static Map<Double, Entity> passiveEntity;
    public static Map<Double, Entity> hostileEntity;
    private MinecraftClient client;
    public static Entity lockedOnEntity = null;
    public static Vec3d lockedOnBlock = null;
    public static BlockState lockedOnBlockState = null;
    public static Entity toBeLocked = null;
    public static boolean hostileEntityInRange = false;

    public void run() {
        client = MinecraftClient.getInstance();
        while (true) {
            assert client.player != null;
            try {
                hostileEntityInRange = false;
                new POIBlocks();
                new POIEntities();

                System.out.println(passiveEntity);
                System.out.println(hostileEntity);
                System.out.println(doorBlocks);

                lockingHandlerMethod();

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Vec3d getButtonsAbsolutePosition(MinecraftClient client, BlockPos blockPos) {
        BlockState blockState = client.world.getBlockState(blockPos);
        ImmutableSet<Entry<Property<?>, Comparable<?>>> entries = blockState.getEntries().entrySet();

        double x = (int) blockPos.getX();
        double y = (int) blockPos.getY();
        double z = (int) blockPos.getZ();

        String face = "", facing = "";

        for (Entry<Property<?>, Comparable<?>> i : entries) {

            if (i.getKey().getName().equalsIgnoreCase("face")) {
                face = i.getValue().toString();

            } else if (i.getKey().getName().equalsIgnoreCase("facing")) {
                facing = i.getValue().toString();
            }
        }

        if (face.equalsIgnoreCase("floor")) {
            if (x < 0)
                x += 0.5;
            else
                x -= 0.5;

            if (z < 0)
                z += 0.5;
            else
                z -= 0.5;

        } else if (face.equalsIgnoreCase("ceiling")) {
            if (x < 0)
                x += 0.5;
            else
                x -= 0.5;

            if (z < 0)
                z += 0.5;
            else
                z -= 0.5;

            y += 1;

        } else if (face.equalsIgnoreCase("wall")) {
            if (x < 0)
                x += 0.5;
            else
                x -= 0.5;

            if (z < 0)
                z += 0.5;
            else
                z -= 0.5;

            y += 0.5;

            if (facing.equalsIgnoreCase("north"))
                z += 0.5;
            else if (facing.equalsIgnoreCase("south"))
                z -= 0.5;
            else if (facing.equalsIgnoreCase("east"))
                x -= 0.5;
            else if (facing.equalsIgnoreCase("west"))
                x += 0.5;

        }

        return new Vec3d(x, y, z);
    }

    public static void lockingHandlerMethod() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (lockedOnEntity != null) {
            if (!lockedOnEntity.isAlive())
                lockedOnEntity = null;
            Vec3d vec3d = new Vec3d(lockedOnEntity.getX(), lockedOnEntity.getY() + lockedOnEntity.getHeight() - 0.25,
                    lockedOnEntity.getZ());
            client.player.lookAt(EntityAnchor.EYES, vec3d);
        }

        if (lockedOnBlock != null) {
            client.player.lookAt(EntityAnchor.EYES, lockedOnBlock);
        }

        while (modInit.LockEntityKey.wasPressed()) {
            if (HudRenderCallBackClass.isAltPressed && (lockedOnEntity != null || lockedOnBlock != null)) {
                lockedOnEntity = null;
                lockedOnBlock = null;
                NarratorPlus.narrate("Unlocked");
            } else {
                if (toBeLocked != null) {
                    MutableText mutableText = (new LiteralText("")).append(toBeLocked.getName());
                    String name = mutableText.getString();
                    String text = name;
                    if (Config.get(ConfigKeys.ENTITY_NARRATOR_NARRATE_DISTANCE_KEY.getKey()))
                        text += " " + HudRenderCallBackClass.get_position_difference(toBeLocked.getBlockPos(), client);
                    NarratorPlus.narrate(text);
                    lockedOnEntity = toBeLocked;
                    lockedOnBlock = null;
                } else if (PointsOfInterestsHandler.buttonBlocks.entrySet().iterator().hasNext()) {
                    Entry<Double, BlockPos> entry = PointsOfInterestsHandler.buttonBlocks.entrySet().iterator().next();
                    BlockPos blockPos = entry.getValue();

                    lockedOnBlock = new PointsOfInterestsHandler().getButtonsAbsolutePosition(client, blockPos);

                } else if (PointsOfInterestsHandler.doorBlocks.entrySet().iterator().hasNext()) {
                    Entry<Double, BlockPos> entry = PointsOfInterestsHandler.doorBlocks.entrySet().iterator().next();
                    BlockPos blockPos = entry.getValue();
                    BlockState blockState = client.world.getBlockState(blockPos);
                    ImmutableSet<Entry<Property<?>, Comparable<?>>> entries = blockState.getEntries().entrySet();

                    String facing = "", hinge = "", open = "";

                    for (Entry<Property<?>, Comparable<?>> i : entries) {

                        if (i.getKey().getName().equalsIgnoreCase("facing"))
                            facing = i.getValue().toString();
                        else if (i.getKey().getName().equalsIgnoreCase("hinge"))
                            hinge = i.getValue().toString();
                        else if (i.getKey().getName().equalsIgnoreCase("open"))
                            open = i.getValue().toString();

                    }

                    double x = (int) blockPos.getX();
                    double y = (int) blockPos.getY();
                    double z = (int) blockPos.getZ();

                    if (x < 0)
                        x += 0.5;
                    else
                        x -= 0.5;

                    if (z < 0)
                        z += 0.5;
                    else
                        z -= 0.5;

                    y += 0.5;

                    if (open.equalsIgnoreCase("false")) {
                        if (facing.equalsIgnoreCase("north"))
                            z += 0.5;
                        else if (facing.equalsIgnoreCase("south"))
                            z -= 0.5;
                        else if (facing.equalsIgnoreCase("east"))
                            x -= 0.5;
                        else if (facing.equalsIgnoreCase("west"))
                            x += 0.5;
                    } else {
                        if (hinge.equalsIgnoreCase("right")) {
                            if (facing.equalsIgnoreCase("north"))
                                x += 0.5;
                            else if (facing.equalsIgnoreCase("south"))
                                x -= 0.5;
                            else if (facing.equalsIgnoreCase("east"))
                                z += 0.5;
                            else if (facing.equalsIgnoreCase("west"))
                                z -= 0.5;
                        } else {
                            if (facing.equalsIgnoreCase("north"))
                                x -= 0.5;
                            else if (facing.equalsIgnoreCase("south"))
                                x += 0.5;
                            else if (facing.equalsIgnoreCase("east"))
                                z -= 0.5;
                            else if (facing.equalsIgnoreCase("west"))
                                z += 0.5;
                        }
                    }

                    Vec3d temp = new Vec3d(x, y, z);

                    lockedOnBlock = temp;
                }
            }
        }
    }
}
