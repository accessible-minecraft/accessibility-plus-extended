package net.shoaibkhan.accessibiltyplusextended.features;

import java.util.Map;
import java.util.TreeMap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class POIEntities {
	private MinecraftClient client;
	private TreeMap<Double, Entity> passiveEntity = new TreeMap<>();
	private TreeMap<Double, Entity> hostileEntity = new TreeMap<>();

	public POIEntities() {
		this.client = MinecraftClient.getInstance();
		this.main();
	}

	private void main() {
		double closestDouble = -99999;
		try {
			for (Entity i : client.world.getEntities()) {
				if (!(i instanceof MobEntity))
					continue;

				BlockPos blockPos = i.getBlockPos();

				Vec3d entityVec3d = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
				Vec3d playerVec3d = new Vec3d(client.player.getBlockPos().getX(), client.player.getBlockPos().getY(),
						client.player.getBlockPos().getZ());
				Double distance = entityVec3d.distanceTo(playerVec3d);

				if (distance <= 4.0) {
					if (i instanceof PassiveEntity) {
						passiveEntity.put(distance, i);
					} else if (i instanceof HostileEntity) {
						hostileEntity.put(distance, i);
					}
					client.world.playSound(blockPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.25f, -5f,
							true);
				}

				if (closestDouble == -99999 || closestDouble > distance) {
					closestDouble = entityVec3d.distanceTo(playerVec3d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		PointsOfInterestsHandler.passiveEntity = passiveEntity;
		PointsOfInterestsHandler.hostileEntity = hostileEntity;
	}

}
