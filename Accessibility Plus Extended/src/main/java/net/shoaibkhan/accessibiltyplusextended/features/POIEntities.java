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

public class POIEntities extends Thread {
	private MinecraftClient client;
	private TreeMap<Double, Entity> passiveEntity = new TreeMap<>();
	private TreeMap<Double, Entity> hostileEntity = new TreeMap<>();
	public boolean running = false;

	public void run() {
		this.client = MinecraftClient.getInstance();
		running = true;
		this.main();
	}

	private void main() {
		try {
			for (Entity i : client.world.getEntities()) {
				if (!(i instanceof MobEntity))
					continue;

				BlockPos blockPos = i.getBlockPos();

				Vec3d entityVec3d = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
				Vec3d playerVec3d = new Vec3d(client.player.getBlockPos().getX(), client.player.getBlockPos().getY(),
						client.player.getBlockPos().getZ());
				Double distance = entityVec3d.distanceTo(playerVec3d);

				if (distance <= 6.0) {
					if (i instanceof PassiveEntity) {
						passiveEntity.put(distance, i);
					} else if (i instanceof HostileEntity) {
						hostileEntity.put(distance, i);
					}
					client.world.playSound(blockPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.25f, -5f,
							true);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		PointsOfInterestsHandler.passiveEntity = passiveEntity;
		PointsOfInterestsHandler.hostileEntity = hostileEntity;
		running = false;
	}

}
