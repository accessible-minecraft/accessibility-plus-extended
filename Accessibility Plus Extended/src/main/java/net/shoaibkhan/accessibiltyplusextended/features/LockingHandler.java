package net.shoaibkhan.accessibiltyplusextended.features;

import java.util.Map.Entry;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
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

public class LockingHandler {
    public static Entity lockedOnEntity = null;
    public static Vec3d lockedOnBlock = null;
    private BlockState lockedOnBlockState = null;
    private Entity toBeLocked = null;

    public LockingHandler() {
        main();
    }

    public void main() {
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

                if (!PointsOfInterestsHandler.hostileEntity.isEmpty()) {
                    Entry<Double, Entity> entry = PointsOfInterestsHandler.hostileEntity.entrySet().iterator().next();
                    Entity entity = entry.getValue();

                    MutableText mutableText = (new LiteralText("")).append(entity.getName());
                    String name = mutableText.getString();
                    String text = name;
                    if (Config.get(ConfigKeys.ENTITY_NARRATOR_NARRATE_DISTANCE_KEY.getKey()))
                        text += " " + HudRenderCallBackClass.get_position_difference(toBeLocked.getBlockPos(), client);
                    NarratorPlus.narrate(text);

                    lockedOnEntity = entity;
                    lockedOnBlock = null;
                } else {
                    Double closest = -9999.0;

                    Entry<Double, Entity> closestHostileEntityEntry = null;
                    Double closestHostileEntityDouble = -9999.0;
                    if (!PointsOfInterestsHandler.hostileEntity.isEmpty()) {
                        closestHostileEntityEntry = PointsOfInterestsHandler.hostileEntity.entrySet().iterator().next();
                        closestHostileEntityDouble = closestHostileEntityEntry.getKey();
                        closest = closestHostileEntityDouble;
                    }

                    Entry<Double, Entity> closestPassiveEntityEntry = null;
                    Double closestPassiveEntityDouble = -9999.0;
                    if (!PointsOfInterestsHandler.passiveEntity.isEmpty()) {
                        closestPassiveEntityEntry = PointsOfInterestsHandler.passiveEntity.entrySet().iterator().next();
                        closestPassiveEntityDouble = closestPassiveEntityEntry.getKey();
                        closest = closestPassiveEntityDouble;
                    }

                    Entry<Double, BlockPos> closestDoorBlockEntry = null;
                    Double closestDoorBlockDouble = -9999.0;
                    if (!PointsOfInterestsHandler.doorBlocks.isEmpty()) {
                        closestDoorBlockEntry = PointsOfInterestsHandler.doorBlocks.entrySet().iterator().next();
                        closestDoorBlockDouble = closestDoorBlockEntry.getKey();
                        closest = closestDoorBlockDouble;
                    }

                    Entry<Double, BlockPos> closestButtonBlockEntry = null;
                    Double closestButtonBlockDouble = -9999.0;
                    if (!PointsOfInterestsHandler.buttonBlocks.isEmpty()) {
                        closestButtonBlockEntry = PointsOfInterestsHandler.buttonBlocks.entrySet().iterator().next();
                        closestButtonBlockDouble = closestButtonBlockEntry.getKey();
                        closest = closestButtonBlockDouble;
                    }

                    Entry<Double, BlockPos> closestBlockEntry = null;
                    Double closestBlockDouble = -9999.0;
                    if (!PointsOfInterestsHandler.blocks.isEmpty()) {
                        closestBlockEntry = PointsOfInterestsHandler.blocks.entrySet().iterator().next();
                        closestBlockDouble = closestBlockEntry.getKey();
                        closest = closestBlockDouble;
                    }

                    Entry<Double, BlockPos> closestOreBlockEntry = null;
                    Double closestOreBlockDouble = -9999.0;
                    if (!PointsOfInterestsHandler.oreBlocks.isEmpty()) {
                        closestOreBlockEntry = PointsOfInterestsHandler.oreBlocks.entrySet().iterator().next();
                        closestOreBlockDouble = closestOreBlockEntry.getKey();
                        closest = closestOreBlockDouble;
                    }

                    if (closest != -9999.0) {
                        if(closestHostileEntityDouble!=-9999.0) closest = Math.min(closest, closestHostileEntityDouble);
                        if(closestPassiveEntityDouble!=-9999.0) closest = Math.min(closest, closestPassiveEntityDouble);
                        if(closestDoorBlockDouble!=-9999.0) closest = Math.min(closest, closestDoorBlockDouble);
                        if(closestButtonBlockDouble!=-9999.0) closest = Math.min(closest, closestButtonBlockDouble);
                        if(closestOreBlockDouble!=-9999.0) closest = Math.min(closest, closestOreBlockDouble);
                        if(closestBlockDouble!=-9999.0) closest = Math.min(closest, closestBlockDouble);

                        if (closest == closestHostileEntityDouble && closestHostileEntityDouble!=-9999.0) {
                            MutableText mutableText = (new LiteralText(""))
                                    .append(closestHostileEntityEntry.getValue().getName());
                            String name = mutableText.getString();
                            String text = name;
                            if (Config.get(ConfigKeys.ENTITY_NARRATOR_NARRATE_DISTANCE_KEY.getKey()))
                                text += " " + HudRenderCallBackClass.get_position_difference(toBeLocked.getBlockPos(),
                                        client);
                            NarratorPlus.narrate(text);

                            lockedOnEntity = closestHostileEntityEntry.getValue();
                            lockedOnBlock = null;
                        } else if (closest == closestPassiveEntityDouble && closestPassiveEntityDouble!=-9999.0) {
                            MutableText mutableText = (new LiteralText(""))
                                    .append(closestPassiveEntityEntry.getValue().getName());
                            String name = mutableText.getString();
                            String text = name;
                            if (Config.get(ConfigKeys.ENTITY_NARRATOR_NARRATE_DISTANCE_KEY.getKey()))
                                text += " " + HudRenderCallBackClass.get_position_difference(toBeLocked.getBlockPos(),
                                        client);
                            NarratorPlus.narrate(text);

                            lockedOnEntity = closestPassiveEntityEntry.getValue();
                            lockedOnBlock = null;
                        } else if (closest == closestDoorBlockDouble && closestDoorBlockDouble!=-9999.0) {
                            lockedOnBlock = getDoorAbsolutePosition(client, closestDoorBlockEntry.getValue());
                            lockedOnEntity = null;
                        } else if (closest == closestButtonBlockDouble && closestButtonBlockDouble!=-9999.0) {
                            lockedOnBlock = getButtonsAbsolutePosition(client, closestButtonBlockEntry.getValue());
                            lockedOnEntity = null;
                        } else if (closest == closestBlockDouble && closestBlockDouble!=-9999.0) {
                            double x = (int) closestBlockEntry.getValue().getX();
                            double y = (int) closestBlockEntry.getValue().getY();
                            double z = (int) closestBlockEntry.getValue().getZ();

                            if (x < 0)
                                x += 0.5;
                            else
                                x -= 0.5;

                            if (z < 0)
                                z += 0.5;
                            else
                                z -= 0.5;

                            y += 0.5;

                            lockedOnBlock = new Vec3d(x, y, z);
                            lockedOnEntity = null;
                        } else if (closest == closestOreBlockDouble && closestOreBlockDouble!=-9999.0) {
                            double x = (int) closestOreBlockEntry.getValue().getX();
                            double y = (int) closestOreBlockEntry.getValue().getY();
                            double z = (int) closestOreBlockEntry.getValue().getZ();

                            if (x < 0)
                                x += 0.5;
                            else
                                x -= 0.5;

                            if (z < 0)
                                z += 0.5;
                            else
                                z -= 0.5;

                            y += 0.5;

                            lockedOnBlock = new Vec3d(x, y, z);
                            lockedOnEntity = null;
                        }
                    }
                }
            }
        }
    }

    private Vec3d getButtonsAbsolutePosition(MinecraftClient client, BlockPos blockPos) {
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

    private Vec3d getDoorAbsolutePosition(MinecraftClient client, BlockPos blockPos) {
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

        return new Vec3d(x, y, z);
    }
}