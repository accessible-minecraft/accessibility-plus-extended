package net.shoaibkhan.accessibiltyplusextended.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.HudRenderCallBackClass;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;

import java.util.Map;

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
			if (!lockedOnEntity.isAlive()) lockedOnEntity = null;
			Vec3d vec3d = new Vec3d(lockedOnEntity.getX(), lockedOnEntity.getY() + lockedOnEntity.getHeight() - 0.25, lockedOnEntity.getZ());
			client.player.lookAt(EntityAnchor.EYES, vec3d);
		}

		if (lockedOnBlock != null) {
			client.player.lookAt(EntityAnchor.EYES, lockedOnBlock);
		}

		while (LockEntityKey.wasPressed()) {
			if (HudRenderCallBackClass.isAltPressed && (lockedOnEntity!=null || lockedOnBlock!=null)) {
				lockedOnEntity = null;
				lockedOnBlock = null;
				NarratorPlus.narrate("Unlocked");
			}
			else {
				Entity toBeLocked = entityLocking();
				if (toBeLocked != null) {
					MutableText mutableText = (new LiteralText("")).append(toBeLocked.getName());
					String name = mutableText.getString();
					String text = "Locked on to " + name;
					if(Config.get(ConfigKeys.ENTITY_NARRATOR_NARRATE_DISTANCE_KEY.getKey())) text += " " + HudRenderCallBackClass.get_position_difference(toBeLocked.getBlockPos(), client);
					NarratorPlus.narrate(text);
					lockedOnEntity = toBeLocked;
					lockedOnBlock = null;
				} else if(PointsOfInterestsHandler.buttonBlocks.entrySet().iterator().hasNext()){
					Map.Entry<Double, Vec3d> entry = PointsOfInterestsHandler.buttonBlocks.entrySet().iterator().next();
					Vec3d temp = entry.getValue();
					double x = (int) temp.x;
					double y = (int) temp.y;
					double z = (int) temp.z;

					if(x<0) x += 0.5;
					else x -= 0.5;

					if(z<0) z += 0.5;
					else z -= 0.5;

					temp = new Vec3d(x, y, z);

					lockedOnBlock = temp;

				} else if(PointsOfInterestsHandler.doorBlocks.entrySet().iterator().hasNext()){
					Map.Entry<Double, Vec3d> entry = PointsOfInterestsHandler.doorBlocks.entrySet().iterator().next();
					lockedOnBlock = entry.getValue();
				}
			}
		}
	}

	private Entity entityLocking() {
		double closestDouble = -99999;
		Entity closestEntity = null;
		try {
			for (Entity i : client.world.getEntities()) {
				if (!(i instanceof MobEntity)) continue;
				BlockPos blockPos = i.getBlockPos();

				Vec3d entityVec3d = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
				Vec3d playerVec3d = new Vec3d(client.player.getBlockPos().getX(), client.player.getBlockPos().getY(), client.player.getBlockPos().getZ());
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
