package net.shoaibkhan.accessibiltyplusextended.features;

import java.util.Map.Entry;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.state.property.Property;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.HudRenderCallBackClass;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;

//import net.minecraft.client.options.KeyBinding;

public class EntityLocking {
	private MinecraftClient client;
	private KeyBinding LockEntityKey;
	public static Entity lockedOnEntity = null;
	public static Vec3d lockedOnBlock = null;

	public EntityLocking(MinecraftClient client, KeyBinding LockEntityKey) {
		this.client = client;
		this.LockEntityKey = LockEntityKey;
		this.main();
	}

	private void main() {
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

		while (LockEntityKey.wasPressed()) {
			if (HudRenderCallBackClass.isAltPressed && (lockedOnEntity != null || lockedOnBlock != null)) {
				lockedOnEntity = null;
				lockedOnBlock = null;
				NarratorPlus.narrate("Unlocked");
			} else {
				Entity toBeLocked = entityLocking();
				if (toBeLocked != null) {
					MutableText mutableText = (new LiteralText("")).append(toBeLocked.getName());
					String name = mutableText.getString();
					String text = "Locked on to " + name;
					if (Config.get(ConfigKeys.ENTITY_NARRATOR_NARRATE_DISTANCE_KEY.getKey()))
						text += " " + HudRenderCallBackClass.get_position_difference(toBeLocked.getBlockPos(), client);
					NarratorPlus.narrate(text);
					lockedOnEntity = toBeLocked;
					lockedOnBlock = null;
				} else if (PointsOfInterestsHandler.buttonBlocks.entrySet().iterator().hasNext()) {
					Vec3d temp;

					Entry<Double, BlockPos> entry = PointsOfInterestsHandler.buttonBlocks.entrySet().iterator().next();
					BlockPos blockPos = entry.getValue();
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

					temp = new Vec3d(x, y, z);
					lockedOnBlock = temp;

				} else if (PointsOfInterestsHandler.doorBlocks.entrySet().iterator().hasNext()) {
					Entry<Double, BlockPos> entry = PointsOfInterestsHandler.doorBlocks.entrySet().iterator().next();
					BlockPos blockPos = entry.getValue();
					BlockState blockState = client.world.getBlockState(blockPos);
					ImmutableSet<Entry<Property<?>, Comparable<?>>> entries = blockState.getEntries().entrySet();

					for (Entry<Property<?>, Comparable<?>> i : entries) {

						System.out.println("Key" + i.getKey().getName());
						System.out.println("Value" + i.getValue());

					}

					double x = (int) blockPos.getX();
					double y = (int) blockPos.getY();
					double z = (int) blockPos.getZ();
					Vec3d temp = new Vec3d(x, y, z);

					lockedOnBlock = temp;
				}
			}
		}
	}

	private Entity entityLocking() {
		double closestDouble = -99999;
		Entity closestEntity = null;
		try {
			for (Entity i : client.world.getEntities()) {
				if (!(i instanceof MobEntity))
					continue;

				BlockPos blockPos = i.getBlockPos();

				Vec3d entityVec3d = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
				Vec3d playerVec3d = new Vec3d(client.player.getBlockPos().getX(), client.player.getBlockPos().getY(),
						client.player.getBlockPos().getZ());
				if (closestDouble == -99999 || closestDouble > entityVec3d.distanceTo(playerVec3d)) {
					closestDouble = entityVec3d.distanceTo(playerVec3d);
					closestEntity = i;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (closestDouble > 10.0)
			closestEntity = null;
		return closestEntity;
	}

}
