package net.shoaibkhan.accessibiltyplusextended.features;

import java.util.TreeMap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.modInit;

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
				if (!(i instanceof MobEntity || i instanceof ItemEntity))
					continue;

				BlockPos blockPos = i.getBlockPos();

				Vec3d entityVec3d = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
				Vec3d playerVec3d = new Vec3d(client.player.getBlockPos().getX(), client.player.getBlockPos().getY(),
						client.player.getBlockPos().getZ());
				Double distance = entityVec3d.distanceTo(playerVec3d);

				if (distance <= 6.0) {
					String entityString = i + "";
					int z = entityString.indexOf("/");
					int y = entityString.indexOf(",", z);
					entityString = entityString.substring(z, y);

					if (i instanceof PassiveEntity) {
						passiveEntity.put(distance, i);
						if (!modInit.mainThreadMap.containsKey("passiveentity+" + entityString)) {
							client.world.playSound(blockPos, SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.BLOCKS,
									0.15f, 0f, true);
							modInit.mainThreadMap.put("passiveentity+" + entityString, 3000);
						}
					} else if (i instanceof HostileEntity) {
						hostileEntity.put(distance, i);
						if (!modInit.mainThreadMap.containsKey("hostileentity+" + entityString)) {
							client.world.playSound(blockPos, SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.BLOCKS,
									0.15f, 2f, true);
							modInit.mainThreadMap.put("hostileentity+" + entityString, 3000);
						}
					} else if (i instanceof ItemEntity) {
						if (((ItemEntity) i).isOnGround()) {
							if (!modInit.mainThreadMap.containsKey("itementity+" + i)) {
								client.world.playSound(blockPos, SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON,
										SoundCategory.BLOCKS, 0.15f, 2f, true);
								modInit.mainThreadMap.put("itementity+" + i, 3000);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		POIHandler.passiveEntity = passiveEntity;
		POIHandler.hostileEntity = hostileEntity;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		running = false;
	}

}