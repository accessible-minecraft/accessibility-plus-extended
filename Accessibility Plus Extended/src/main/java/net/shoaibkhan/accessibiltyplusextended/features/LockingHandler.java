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
    public static boolean isLockedOnLadder = false;
    public static String lockedOnBlockEntries = "";
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

        if (isLockedOnLadder) {
            Vec3d playerPos = client.player.getPos();
            double distance = lockedOnBlock.distanceTo(playerPos);
            if (distance <= 0.5) {
                lockedOnBlock = null;
                isLockedOnLadder = false;
            }
        }

        if (lockedOnBlock != null) {
            String entries = client.world.getBlockState(new BlockPos(lockedOnBlock)).getEntries() + ""
                    + client.world.getBlockState(new BlockPos(lockedOnBlock)).getBlock().getName();
            if (entries.equalsIgnoreCase(lockedOnBlockEntries))
                client.player.lookAt(EntityAnchor.EYES, lockedOnBlock);
            else {
                lockedOnBlockEntries = "";
                isLockedOnLadder = false;
                lockedOnBlock = null;
            }
        }

        if (modInit.LockEntityKey.wasPressed()) {
            if (HudRenderCallBackClass.isAltPressed && (lockedOnEntity != null || lockedOnBlock != null)) {
                NarratorPlus.narrate("Unlocked");
                lockedOnEntity = null;
                lockedOnBlockEntries = "";
                lockedOnBlock = null;
                isLockedOnLadder = false;
            } else {

                if (!POIHandler.hostileEntity.isEmpty()) {
                    Entry<Double, Entity> entry = POIHandler.hostileEntity.firstEntry();
                    Entity entity = entry.getValue();

                    MutableText mutableText = (new LiteralText("")).append(entity.getName());
                    String name = mutableText.getString();
                    String text = name;
                    if (Config.get(ConfigKeys.ENTITY_NARRATOR_NARRATE_DISTANCE_KEY.getKey()))
                        text += " " + HudRenderCallBackClass.get_position_difference(toBeLocked.getBlockPos(), client);
                    NarratorPlus.narrate(text);

                    lockedOnEntity = entity;
                    lockedOnBlockEntries = "";

                    lockedOnBlock = null;
                    isLockedOnLadder = false;
                } else {
                    Double closest = -9999.0;

                    Entry<Double, Entity> closestPassiveEntityEntry = null;
                    Double closestPassiveEntityDouble = -9999.0;
                    if (!POIHandler.passiveEntity.isEmpty()) {
                        closestPassiveEntityEntry = POIHandler.passiveEntity.firstEntry();
                        closestPassiveEntityDouble = closestPassiveEntityEntry.getKey();
                        closest = closestPassiveEntityDouble;
                    }

                    Entry<Double, Vec3d> closestDoorBlockEntry = null;
                    Double closestDoorBlockDouble = -9999.0;
                    if (!POIHandler.doorBlocks.isEmpty()) {
                        closestDoorBlockEntry = POIHandler.doorBlocks.firstEntry();
                        closestDoorBlockDouble = closestDoorBlockEntry.getKey();
                        closest = closestDoorBlockDouble;
                    }

                    Entry<Double, Vec3d> closestButtonBlockEntry = null;
                    Double closestButtonBlockDouble = -9999.0;
                    if (!POIHandler.buttonBlocks.isEmpty()) {
                        closestButtonBlockEntry = POIHandler.buttonBlocks.firstEntry();
                        closestButtonBlockDouble = closestButtonBlockEntry.getKey();
                        closest = closestButtonBlockDouble;
                    }

                    Entry<Double, Vec3d> closestLadderBlockEntry = null;
                    Double closestLadderBlockDouble = -9999.0;
                    if (!POIHandler.ladderBlocks.isEmpty()) {
                        closestLadderBlockEntry = POIHandler.ladderBlocks.firstEntry();
                        closestLadderBlockDouble = closestLadderBlockEntry.getKey();
                        closest = closestLadderBlockDouble;
                    }

                    Entry<Double, Vec3d> closestLeverBlockEntry = null;
                    Double closestLeverBlockDouble = -9999.0;
                    if (!POIHandler.leverBlocks.isEmpty()) {
                        closestLeverBlockEntry = POIHandler.leverBlocks.firstEntry();
                        closestLeverBlockDouble = closestLeverBlockEntry.getKey();
                        closest = closestLeverBlockDouble;
                    }

                    Entry<Double, Vec3d> closestBlockEntry = null;
                    Double closestBlockDouble = -9999.0;
                    if (!POIHandler.blocks.isEmpty()) {
                        closestBlockEntry = POIHandler.blocks.firstEntry();
                        closestBlockDouble = closestBlockEntry.getKey();
                        closest = closestBlockDouble;
                    }

                    Entry<Double, Vec3d> closestOreBlockEntry = null;
                    Double closestOreBlockDouble = -9999.0;
                    if (!POIHandler.oreBlocks.isEmpty()) {
                        closestOreBlockEntry = POIHandler.oreBlocks.firstEntry();
                        closestOreBlockDouble = closestOreBlockEntry.getKey();
                        closest = closestOreBlockDouble;
                    }

                    if (closest != -9999.0) {
                        if (closestPassiveEntityDouble != -9999.0)
                            closest = Math.min(closest, closestPassiveEntityDouble);
                        if (closestDoorBlockDouble != -9999.0)
                            closest = Math.min(closest, closestDoorBlockDouble);
                        if (closestButtonBlockDouble != -9999.0)
                            closest = Math.min(closest, closestButtonBlockDouble);
                        if (closestLadderBlockDouble != -9999.0)
                            closest = Math.min(closest, closestLadderBlockDouble);
                        if (closestLeverBlockDouble != -9999.0)
                            closest = Math.min(closest, closestLeverBlockDouble);
                        if (closestOreBlockDouble != -9999.0)
                            closest = Math.min(closest, closestOreBlockDouble);
                        if (closestBlockDouble != -9999.0)
                            closest = Math.min(closest, closestBlockDouble);

                        if (closest.equals(closestPassiveEntityDouble) && closestPassiveEntityDouble != -9999.0) {
                            MutableText mutableText = (new LiteralText(""))
                                    .append(closestPassiveEntityEntry.getValue().getName());
                            String name = mutableText.getString();
                            String text = name;
                            if (Config.get(ConfigKeys.ENTITY_NARRATOR_NARRATE_DISTANCE_KEY.getKey()))
                                text += " " + HudRenderCallBackClass.get_position_difference(toBeLocked.getBlockPos(),
                                        client);
                            NarratorPlus.narrate(text);

                            lockedOnEntity = closestPassiveEntityEntry.getValue();
                            lockedOnBlockEntries = "";
                            lockedOnBlock = null;
                            isLockedOnLadder = false;
                        } else if (closest.equals(closestDoorBlockDouble) && closestDoorBlockDouble != -9999.0) {
                            Vec3d absolutePos = getDoorAbsolutePosition(client, closestDoorBlockEntry.getValue());
                            lockedOnBlockEntries = client.world
                                    .getBlockState(new BlockPos(closestDoorBlockEntry.getValue())).getEntries() + ""
                                    + client.world.getBlockState(new BlockPos(closestDoorBlockEntry.getValue()))
                                            .getBlock().getName();

                            lockedOnBlock = absolutePos;
                            lockedOnEntity = null;
                            isLockedOnLadder = false;
                        } else if (closest.equals(closestButtonBlockDouble) && closestButtonBlockDouble != -9999.0) {
                            Vec3d absolutePos = getButtonsAbsolutePosition(client, closestButtonBlockEntry.getValue());
                            lockedOnBlockEntries = client.world
                                    .getBlockState(new BlockPos(closestButtonBlockEntry.getValue())).getEntries() + ""
                                    + client.world.getBlockState(new BlockPos(closestButtonBlockEntry.getValue()))
                                            .getBlock().getName();

                            lockedOnBlock = absolutePos;
                            lockedOnEntity = null;
                            isLockedOnLadder = false;
                        } else if (closest.equals(closestLadderBlockDouble) && closestLadderBlockDouble != -9999.0) {
                            Vec3d absolutePos = getLaddersAbsolutePosition(client, closestLadderBlockEntry.getValue());
                            isLockedOnLadder = true;
                            lockedOnBlockEntries = client.world.getBlockState(new BlockPos(absolutePos)).getEntries()
                                    + "" + client.world.getBlockState(new BlockPos(absolutePos)).getBlock().getName();

                            lockedOnBlock = absolutePos;
                            lockedOnEntity = null;
                        } else if (closest.equals(closestLeverBlockDouble) && closestLeverBlockDouble != -9999.0) {
                            Vec3d absolutePos = getLeversAbsolutePosition(client, closestLeverBlockEntry.getValue());
                            lockedOnBlockEntries = client.world
                                    .getBlockState(new BlockPos(closestLeverBlockEntry.getValue())).getEntries() + ""
                                    + client.world.getBlockState(new BlockPos(closestLeverBlockEntry.getValue()))
                                            .getBlock().getName();

                            lockedOnBlock = absolutePos;
                            lockedOnEntity = null;
                            isLockedOnLadder = false;
                        } else if (closest.equals(closestBlockDouble) && closestBlockDouble != -9999.0) {
                            lockedOnBlock = closestBlockEntry.getValue();
                            lockedOnBlockEntries = client.world
                                    .getBlockState(new BlockPos(closestBlockEntry.getValue())).getEntries() + ""
                                    + client.world.getBlockState(new BlockPos(closestBlockEntry.getValue())).getBlock()
                                            .getName();
                            lockedOnEntity = null;
                            isLockedOnLadder = false;
                        } else if (closest.equals(closestOreBlockDouble) && closestOreBlockDouble != -9999.0) {
                            lockedOnBlock = closestOreBlockEntry.getValue();
                            lockedOnBlockEntries = client.world
                                    .getBlockState(new BlockPos(closestOreBlockEntry.getValue())).getEntries() + ""
                                    + client.world.getBlockState(new BlockPos(closestOreBlockEntry.getValue()))
                                            .getBlock().getName();
                            lockedOnEntity = null;
                            isLockedOnLadder = false;
                        }
                    }
                }
            }
        }
    }

    private Vec3d getLeversAbsolutePosition(MinecraftClient client, Vec3d blockPos) {
        BlockState blockState = client.world.getBlockState(new BlockPos(blockPos));
        ImmutableSet<Entry<Property<?>, Comparable<?>>> entries = blockState.getEntries().entrySet();

        String face = "", facing = "";

        for (Entry<Property<?>, Comparable<?>> i : entries) {

            System.out.println("Key:\t" + i.getKey().getName());
            System.out.println("Value:\t" + i.getValue());
            if (i.getKey().getName().equalsIgnoreCase("face")) {
                face = i.getValue().toString();

            } else if (i.getKey().getName().equalsIgnoreCase("facing")) {
                facing = i.getValue().toString();
            }

        }

        double x = blockPos.getX();
        double y = blockPos.getY();
        double z = blockPos.getZ();

        if (face.equalsIgnoreCase("floor")) {
            y -= 0.3;
        } else if (face.equalsIgnoreCase("ceiling")) {
            y += 0.3;
        } else if (face.equalsIgnoreCase("wall")) {
            if (facing.equalsIgnoreCase("north"))
                z += 0.3;
            else if (facing.equalsIgnoreCase("south"))
                z -= 0.3;
            else if (facing.equalsIgnoreCase("east"))
                x -= 0.3;
            else if (facing.equalsIgnoreCase("west"))
                x += 0.3;
        }

        return new Vec3d(x, y, z);
    }

    private Vec3d getLaddersAbsolutePosition(MinecraftClient client, Vec3d blockPos) {
        BlockState blockState = client.world.getBlockState(new BlockPos(blockPos));
        ImmutableSet<Entry<Property<?>, Comparable<?>>> entries = blockState.getEntries().entrySet();

        String facing = "";

        for (Entry<Property<?>, Comparable<?>> i : entries) {

            if (i.getKey().getName().equalsIgnoreCase("facing")) {
                facing = i.getValue().toString();
                break;
            }

        }

        double x = blockPos.getX();
        double y = blockPos.getY();
        double z = blockPos.getZ();

        if (facing.equalsIgnoreCase("north"))
            z += 0.35;
        else if (facing.equalsIgnoreCase("south"))
            z -= 0.35;
        else if (facing.equalsIgnoreCase("west"))
            x += 0.35;
        else if (facing.equalsIgnoreCase("east"))
            x -= 0.35;

        return new Vec3d(x, y, z);
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

        if (face.equalsIgnoreCase("floor")) {
            y -= 0.4;
        } else if (face.equalsIgnoreCase("ceiling")) {
            y += 0.4;
        } else if (face.equalsIgnoreCase("wall")) {
            if (facing.equalsIgnoreCase("north"))
                z += 0.4;
            else if (facing.equalsIgnoreCase("south"))
                z -= 0.4;
            else if (facing.equalsIgnoreCase("east"))
                x -= 0.4;
            else if (facing.equalsIgnoreCase("west"))
                x += 0.4;
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
                z += 0.35;
            else if (facing.equalsIgnoreCase("south"))
                z -= 0.35;
            else if (facing.equalsIgnoreCase("east"))
                x -= 0.35;
            else if (facing.equalsIgnoreCase("west"))
                x += 0.35;
        } else {
            if (hinge.equalsIgnoreCase("right")) {
                if (facing.equalsIgnoreCase("north"))
                    x += 0.35;
                else if (facing.equalsIgnoreCase("south"))
                    x -= 0.35;
                else if (facing.equalsIgnoreCase("east"))
                    z += 0.35;
                else if (facing.equalsIgnoreCase("west"))
                    z -= 0.35;
            } else {
                if (facing.equalsIgnoreCase("north"))
                    x -= 0.35;
                else if (facing.equalsIgnoreCase("south"))
                    x += 0.35;
                else if (facing.equalsIgnoreCase("east"))
                    z -= 0.35;
                else if (facing.equalsIgnoreCase("west"))
                    z += 0.35;
            }
        }

        return new Vec3d(x, y, z);
    }
}
