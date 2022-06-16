package net.shoaibkhan.accessibiltyplusextended.features;

import java.util.Map.Entry;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.HudRenderCallBackClass;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;
import net.shoaibkhan.accessibiltyplusextended.util.KeyBinds;

public class LockingHandler {
    public static Entity lockedOnEntity = null;
    public static Vec3d lockedOnBlock = null;
    public static Vec3d prevEntityPos = null;
    public static boolean isLockedOntoLadder = false;
    public static boolean isLockedOntoEyeOfEnderTarget = false;
    public static String lockedOnBlockEntries = "";

    public LockingHandler() {
        main();
    }

    public void main() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (lockedOnEntity != null) {

            if (!lockedOnEntity.isAlive()) {
                lockedOnEntity = null;
                playUnlockingSound(client);
            } else {
                double posX = lockedOnEntity.getX() - 0.5;
                double posY = lockedOnEntity.getY() - 0.5;
                double posZ = lockedOnEntity.getZ() - 0.5;
                if (lockedOnEntity instanceof EyeOfEnderEntity)
                    prevEntityPos = new Vec3d(posX, posY, posZ);

                Vec3d vec3d = new Vec3d(lockedOnEntity.getX(),
                        lockedOnEntity.getY() + lockedOnEntity.getHeight() - 0.25, lockedOnEntity.getZ());
                client.player.lookAt(EntityAnchor.EYES, vec3d);
            }
        } else {
            if (prevEntityPos != null) {
                lockedOnBlock = prevEntityPos;
                isLockedOntoEyeOfEnderTarget = true;
                prevEntityPos = null;
            }
        }

        if (isLockedOntoLadder) {
            Vec3d playerPos = client.player.getPos();
            double distance = lockedOnBlock.distanceTo(playerPos);
            if (distance <= 0.5) {
                lockedOnBlock = null;
                isLockedOntoLadder = false;
                playUnlockingSound(client);
            }
        }

        if (lockedOnBlock != null) {
            BlockState blockState = client.world.getBlockState(new BlockPos(lockedOnBlock));
            String entries = blockState.getEntries() + "" + blockState.getBlock() + "" + (new BlockPos(lockedOnBlock));
            if (entries.equalsIgnoreCase(lockedOnBlockEntries) || isLockedOntoEyeOfEnderTarget)
                client.player.lookAt(EntityAnchor.EYES, lockedOnBlock);
            else {
                lockedOnBlockEntries = "";
                isLockedOntoLadder = false;
                lockedOnBlock = null;
                playUnlockingSound(client);
            }
        }

        if (KeyBinds.LockEntityKey.getKeyBind().wasPressed()) {
            if (HudRenderCallBackClass.isAltPressed && (lockedOnEntity != null || lockedOnBlock != null)) {
                NarratorPlus.narrate("narrate.apextended.locking.unlocked");
                lockedOnEntity = null;
                lockedOnBlockEntries = "";
                lockedOnBlock = null;
                isLockedOntoLadder = false;
                isLockedOntoEyeOfEnderTarget = false;
                playUnlockingSound(client);
            } else {

                if (!POIHandler.eyeOfEnderEntity.isEmpty()) {
                    Entry<Double, Entity> entry = POIHandler.eyeOfEnderEntity.firstEntry();
                    Entity entity = entry.getValue();

                    String text = I18n.translate("narrate.apextended.locking.trackingEyeOfEnder");
                    lockedOnEntity = entity;
                    lockedOnBlockEntries = "";

                    lockedOnBlock = null;
                    isLockedOntoLadder = false;

                    NarratorPlus.narrate(text);
                } else if (!POIHandler.hostileEntity.isEmpty()) {
                    Entry<Double, Entity> entry = POIHandler.hostileEntity.firstEntry();
                    Entity entity = entry.getValue();

                    String name = entity.getName().getString();
                    String text = name;
                    lockedOnEntity = entity;
                    lockedOnBlockEntries = "";

                    lockedOnBlock = null;
                    isLockedOntoLadder = false;

                    if (Config.get(ConfigKeys.POI_ENTITY_LOCKING_NARRATE_DISTANCE_KEY.getKey())) {
                        text += " " + HudRenderCallBackClass.get_position_difference(entity.getBlockPos(), client);
                    }
                    NarratorPlus.narrate(text);

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

                    Entry<Double, Vec3d> closestTrapDoorBlockEntry = null;
                    Double closestTrapDoorBlockDouble = -9999.0;
                    if (!POIHandler.trapDoorBlocks.isEmpty()) {
                        closestTrapDoorBlockEntry = POIHandler.trapDoorBlocks.firstEntry();
                        closestTrapDoorBlockDouble = closestTrapDoorBlockEntry.getKey();
                        closest = closestTrapDoorBlockDouble;
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
                        if (closestTrapDoorBlockDouble != -9999.0)
                            closest = Math.min(closest, closestTrapDoorBlockDouble);
                        if (closestOreBlockDouble != -9999.0)
                            closest = Math.min(closest, closestOreBlockDouble);
                        if (closestBlockDouble != -9999.0)
                            closest = Math.min(closest, closestBlockDouble);

                        lockOntoBlocksorPassiveEntity(client, closest, closestPassiveEntityEntry,
                                closestPassiveEntityDouble, closestDoorBlockEntry, closestDoorBlockDouble,
                                closestButtonBlockEntry, closestButtonBlockDouble, closestLadderBlockEntry,
                                closestLadderBlockDouble, closestLeverBlockEntry, closestLeverBlockDouble,
                                closestTrapDoorBlockEntry, closestTrapDoorBlockDouble, closestBlockEntry,
                                closestBlockDouble, closestOreBlockEntry, closestOreBlockDouble);

                        narrateBlockPosAndSetBlockEntries(client);

                    }
                }
            }

        }

    }

    private void lockOntoBlocksorPassiveEntity(MinecraftClient client, Double closest,
            Entry<Double, Entity> closestPassiveEntityEntry, Double closestPassiveEntityDouble,
            Entry<Double, Vec3d> closestDoorBlockEntry, Double closestDoorBlockDouble,
            Entry<Double, Vec3d> closestButtonBlockEntry, Double closestButtonBlockDouble,
            Entry<Double, Vec3d> closestLadderBlockEntry, Double closestLadderBlockDouble,
            Entry<Double, Vec3d> closestLeverBlockEntry, Double closestLeverBlockDouble,
            Entry<Double, Vec3d> closestTrapDoorBlockEntry, Double closestTrapDoorBlockDouble,
            Entry<Double, Vec3d> closestBlockEntry, Double closestBlockDouble,
            Entry<Double, Vec3d> closestOreBlockEntry, Double closestOreBlockDouble) {

        if (closest.equals(closestPassiveEntityDouble) && closestPassiveEntityDouble != -9999.0) {
            MutableText mutableText = (Text.empty()).append(closestPassiveEntityEntry.getValue().getName());
            String name = mutableText.getString();
            String text = name;

            lockedOnEntity = closestPassiveEntityEntry.getValue();
            lockedOnBlockEntries = "";
            lockedOnBlock = null;
            isLockedOntoLadder = false;

            if (Config.get(ConfigKeys.POI_ENTITY_LOCKING_NARRATE_DISTANCE_KEY.getKey())) {
                text += " " + HudRenderCallBackClass.get_position_difference(lockedOnEntity.getBlockPos(), client);
            }
            NarratorPlus.narrate(text);

        } else if (closest.equals(closestDoorBlockDouble) && closestDoorBlockDouble != -9999.0) {
            Vec3d absolutePos = getDoorAbsolutePosition(client, closestDoorBlockEntry.getValue());
            lockedOnBlock = absolutePos;
            lockedOnEntity = null;
            isLockedOntoLadder = false;
        } else if (closest.equals(closestButtonBlockDouble) && closestButtonBlockDouble != -9999.0) {
            Vec3d absolutePos = getButtonsAbsolutePosition(client, closestButtonBlockEntry.getValue());
            lockedOnBlock = absolutePos;
            lockedOnEntity = null;
            isLockedOntoLadder = false;
        } else if (closest.equals(closestLadderBlockDouble) && closestLadderBlockDouble != -9999.0) {
            Vec3d absolutePos = getLaddersAbsolutePosition(client, closestLadderBlockEntry.getValue());
            isLockedOntoLadder = true;
            lockedOnBlock = absolutePos;
            lockedOnEntity = null;
        } else if (closest.equals(closestLeverBlockDouble) && closestLeverBlockDouble != -9999.0) {
            Vec3d absolutePos = getLeversAbsolutePosition(client, closestLeverBlockEntry.getValue());
            lockedOnBlock = absolutePos;
            lockedOnEntity = null;
            isLockedOntoLadder = false;
        } else if (closest.equals(closestTrapDoorBlockDouble) && closestTrapDoorBlockDouble != -9999.0) {
            Vec3d absolutePos = getTrapDoorAbsolutePosition(client, closestTrapDoorBlockEntry.getValue());
            lockedOnBlock = absolutePos;
            lockedOnEntity = null;
            isLockedOntoLadder = false;
        } else if (closest.equals(closestBlockDouble) && closestBlockDouble != -9999.0) {
            lockedOnBlock = closestBlockEntry.getValue();
            lockedOnEntity = null;
            isLockedOntoLadder = false;
        } else if (closest.equals(closestOreBlockDouble) && closestOreBlockDouble != -9999.0) {
            lockedOnBlock = closestOreBlockEntry.getValue();
            lockedOnEntity = null;
            isLockedOntoLadder = false;
        }
    }

    private void narrateBlockPosAndSetBlockEntries(MinecraftClient client) {
        if (lockedOnBlock != null) {
            BlockState blockState = client.world.getBlockState(new BlockPos(lockedOnBlock));
            lockedOnBlockEntries = blockState.getEntries() + "" + blockState.getBlock() + ""
                    + (new BlockPos(lockedOnBlock));

            Block closestBlock = blockState.getBlock();

            MutableText mutableText = (Text.empty()).append(closestBlock.getName());
            String name = mutableText.getString();
            String text = name;
            if (Config.get(ConfigKeys.POI_BLOCKS_LOCKING_NARRATE_DISTANCE_KEY.getKey())) {
                text += " " + HudRenderCallBackClass.get_position_difference(new BlockPos(lockedOnBlock), client);
            }
            NarratorPlus.narrate(text);
        }
    }

    private void playUnlockingSound(MinecraftClient client) {
        if (Config.get(ConfigKeys.POI_UNLOCKING_SOUND_KEY.getKey())) {
            Float volume = POIHandler.getUnlockingSoundVolume();
            client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, volume, 2f);
        }
    }

    private Vec3d getTrapDoorAbsolutePosition(MinecraftClient client, Vec3d blockPos) {
        BlockState blockState = client.world.getBlockState(new BlockPos(blockPos));
        ImmutableSet<Entry<Property<?>, Comparable<?>>> entries = blockState.getEntries().entrySet();

        String half = "", facing = "", open = "";

        for (Entry<Property<?>, Comparable<?>> i : entries) {

            System.out.println("Key:\t" + i.getKey().getName());
            System.out.println("Value:\t" + i.getValue());
            if (i.getKey().getName().equalsIgnoreCase("half")) {
                half = i.getValue().toString();
            } else if (i.getKey().getName().equalsIgnoreCase("facing")) {
                facing = i.getValue().toString();
            } else if (i.getKey().getName().equalsIgnoreCase("open")) {
                open = i.getValue().toString();
            }

        }

        double x = blockPos.getX();
        double y = blockPos.getY();
        double z = blockPos.getZ();

        if (open.equalsIgnoreCase("true")) {
            if (facing.equalsIgnoreCase("north"))
                z += 0.4;
            else if (facing.equalsIgnoreCase("south"))
                z -= 0.4;
            else if (facing.equalsIgnoreCase("west"))
                x += 0.4;
            else if (facing.equalsIgnoreCase("east"))
                x -= 0.4;
        } else if (open.equalsIgnoreCase("false")) {
            if (half.equalsIgnoreCase("bottom"))
                y -= 0.4;
            else if (half.equalsIgnoreCase("top"))
                y += 0.4;
        }

        return new Vec3d(x, y, z);
    }

    private Vec3d getLeversAbsolutePosition(MinecraftClient client, Vec3d blockPos) {
        BlockState blockState = client.world.getBlockState(new BlockPos(blockPos));
        ImmutableSet<Entry<Property<?>, Comparable<?>>> entries = blockState.getEntries().entrySet();

        String face = "", facing = "";

        for (Entry<Property<?>, Comparable<?>> i : entries) {

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
