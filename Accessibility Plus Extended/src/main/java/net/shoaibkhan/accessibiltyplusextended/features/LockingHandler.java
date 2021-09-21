package net.shoaibkhan.accessibiltyplusextended.features;

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
                NarratorPlus.narrate("Unlocked");
                lockedOnEntity = null;
                lockedOnBlock = null;
            } else {

                if (!PointsOfInterestsHandler.hostileEntity.isEmpty()) {
                    Entry<Double, Entity> entry = PointsOfInterestsHandler.hostileEntity.firstEntry();
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

                    Entry<Double, Entity> closestPassiveEntityEntry = null;
                    Double closestPassiveEntityDouble = -9999.0;
                    if (!PointsOfInterestsHandler.passiveEntity.isEmpty()) {
                        closestPassiveEntityEntry = PointsOfInterestsHandler.passiveEntity.firstEntry();
                        closestPassiveEntityDouble = closestPassiveEntityEntry.getKey();
                        closest = closestPassiveEntityDouble;
                    }

                    Entry<Double, Vec3d> closestDoorBlockEntry = null;
                    Double closestDoorBlockDouble = -9999.0;
                    if (!PointsOfInterestsHandler.doorBlocks.isEmpty()) {
                        closestDoorBlockEntry = PointsOfInterestsHandler.doorBlocks.firstEntry();
                        closestDoorBlockDouble = closestDoorBlockEntry.getKey();
                        closest = closestDoorBlockDouble;
                    }

                    Entry<Double, Vec3d> closestButtonBlockEntry = null;
                    Double closestButtonBlockDouble = -9999.0;
                    if (!PointsOfInterestsHandler.buttonBlocks.isEmpty()) {
                        closestButtonBlockEntry = PointsOfInterestsHandler.buttonBlocks.firstEntry();
                        closestButtonBlockDouble = closestButtonBlockEntry.getKey();
                        closest = closestButtonBlockDouble;
                    }

                    Entry<Double, Vec3d> closestBlockEntry = null;
                    Double closestBlockDouble = -9999.0;
                    if (!PointsOfInterestsHandler.blocks.isEmpty()) {
                        closestBlockEntry = PointsOfInterestsHandler.blocks.firstEntry();
                        closestBlockDouble = closestBlockEntry.getKey();
                        closest = closestBlockDouble;
                    }

                    Entry<Double, Vec3d> closestOreBlockEntry = null;
                    Double closestOreBlockDouble = -9999.0;
                    if (!PointsOfInterestsHandler.oreBlocks.isEmpty()) {
                        closestOreBlockEntry = PointsOfInterestsHandler.oreBlocks.firstEntry();
                        closestOreBlockDouble = closestOreBlockEntry.getKey();
                        closest = closestOreBlockDouble;
                    }


                    if (closest != -9999.0) {
                        if(closestPassiveEntityDouble!=-9999.0) closest = Math.min(closest, closestPassiveEntityDouble);
                        if(closestDoorBlockDouble!=-9999.0) closest = Math.min(closest, closestDoorBlockDouble);
                        if(closestButtonBlockDouble!=-9999.0) closest = Math.min(closest, closestButtonBlockDouble);
                        if(closestOreBlockDouble!=-9999.0) closest = Math.min(closest, closestOreBlockDouble);
                        if(closestBlockDouble!=-9999.0) closest = Math.min(closest, closestBlockDouble);

                        if (closest.equals(closestPassiveEntityDouble) && closestPassiveEntityDouble!=-9999.0) {
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
                        } else if (closest.equals(closestDoorBlockDouble) && closestDoorBlockDouble!=-9999.0) {
                            lockedOnBlock = getDoorAbsolutePosition(client, closestDoorBlockEntry.getValue());
                            lockedOnEntity = null;
                        } else if (closest.equals(closestButtonBlockDouble) && closestButtonBlockDouble!=-9999.0) {
                            lockedOnBlock = getButtonsAbsolutePosition(client, closestDoorBlockEntry.getValue());
                            lockedOnEntity = null;
                        } else if (closest.equals(closestBlockDouble) && closestBlockDouble!=-9999.0) {
                            lockedOnBlock = closestBlockEntry.getValue();
                            lockedOnEntity = null;
                        } else if (closest.equals(closestOreBlockDouble) && closestOreBlockDouble!=-9999.0) {
                            lockedOnBlock = closestOreBlockEntry.getValue();
                            lockedOnEntity = null;
                        }
                    }
                }
            }
        }
    }

    private Vec3d getButtonsAbsolutePosition(MinecraftClient client, Vec3d blockPos) {
        BlockState blockState = client.world.getBlockState(new BlockPos(blockPos));
        ImmutableSet<Entry<Property<?>, Comparable<?>>> entries = blockState.getEntries().entrySet();

        double x = blockPos.getX();
        double y = blockPos.getY();
        double z = blockPos.getZ();

        String face = "", facing = "";

        for (Entry<Property<?>, Comparable<?>> i : entries) {

            if (i.getKey().getName().equalsIgnoreCase("face")) {
                face = i.getValue().toString();

            } else if (i.getKey().getName().equalsIgnoreCase("facing")) {
                facing = i.getValue().toString();
            }
        }

        if (face.equalsIgnoreCase("ceiling")) {
            y += 0.5;

        } else if (face.equalsIgnoreCase("wall")) {
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

    private Vec3d getDoorAbsolutePosition(MinecraftClient client, Vec3d blockPos) {
        BlockState blockState = client.world.getBlockState(new BlockPos(blockPos));
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

        double x = blockPos.getX();
        double y = blockPos.getY();
        double z = blockPos.getZ();

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
