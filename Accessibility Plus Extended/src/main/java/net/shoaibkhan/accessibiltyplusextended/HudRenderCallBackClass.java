package net.shoaibkhan.accessibiltyplusextended;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
// import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.gui.ConfigGui;
import net.shoaibkhan.accessibiltyplusextended.gui.ConfigScreen;
import net.shoaibkhan.accessibiltyplusextended.threads.DetectorThread;
import net.shoaibkhan.accessibiltyplusextended.threads.DurabilityThread;
import net.shoaibkhan.accessibiltyplusextended.threads.FallDetectorThread;

public class HudRenderCallBackClass {
	private MinecraftClient client;
	private PlayerEntity player;
	private String tempBlock = "", tempBlockPos = "";
	private String tempEntity = "", tempEntityPos = "";
	public static int fallDetectorFlag = 0, entityNarratorFlag = 0, oreDetectorFlag = 0;
	private static FallDetectorThread[] fallDetectorThreads = { new FallDetectorThread(), new FallDetectorThread(),
			new FallDetectorThread() };
	private static DetectorThread[] oreDetectorThreads = { new DetectorThread(), new DetectorThread(),
			new DetectorThread() };
	private static DurabilityThread durabilityThread = new DurabilityThread();
	private static Entity lockedOnEntity = null;
	public static boolean isTradeScreenOpen = false;

	public HudRenderCallBackClass(KeyBinding CONFIG_KEY, KeyBinding LockEntityKey) {

		HudRenderCallback.EVENT.register((__, ___) -> {
			this.client = MinecraftClient.getInstance();
			if (client.player == null)
				return;
			player = client.player;

			durabilityThread();

			boolean isAltPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),
					InputUtil.fromTranslationKey("key.keyboard.left.alt").getCode())
					|| InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),
							InputUtil.fromTranslationKey("key.keyboard.right.alt").getCode()));
			boolean isControlPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),
					InputUtil.fromTranslationKey("key.keyboard.left.control").getCode())
					|| InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),
							InputUtil.fromTranslationKey("key.keyboard.right.control").getCode()));

			try {

				if (lockedOnEntity != null) {
					if (!lockedOnEntity.isAlive())
						lockedOnEntity = null;
					Vec3d vec3d = new Vec3d(lockedOnEntity.getX(),
							lockedOnEntity.getY() + lockedOnEntity.getHeight() - 0.25, lockedOnEntity.getZ());
					client.player.lookAt(EntityAnchor.EYES, vec3d);

				}

				while (CONFIG_KEY.wasPressed()) {
					if (!isControlPressed) {
						Screen screen = new ConfigScreen(new ConfigGui(client.player, client),
								"AP Extended Configuration", client.player);
						client.openScreen(screen);
						return;
					}
				}

				if (isAltPressed) {
					while (LockEntityKey.wasPressed()) {
						lockedOnEntity = null;
					}
				}

				while (LockEntityKey.wasPressed()) {
					Entity toBeLocked = entityLocking();
					if (toBeLocked != null) {
						MutableText mutableText = (new LiteralText("")).append(toBeLocked.getName());
						player.sendMessage(
								new LiteralText(
										mutableText.getString() + " "
												+ HudRenderCallBackClass
														.get_position_difference(toBeLocked.getBlockPos(), client)),
								true);
						lockedOnEntity = toBeLocked;
					}
				}

				if (!client.isPaused() && (client.currentScreen == null)) {

					// Read Crosshair
					if (10000 - fallDetectorFlag >= 3000
							&& (Config.get(Config.getReadblockskey()) || Config.get(Config.getEntitynarratorkey()))) {
						crosshairTarget();
					}

					// Fall Detector
					if (Config.get(Config.getFalldetectorkey())) {
						for (int i = 0; i < fallDetectorThreads.length; i++) {
							if (!fallDetectorThreads[i].alive) {
								fallDetectorThreads[i].start();
							} else if (fallDetectorThreads[i].alive && fallDetectorThreads[i].finished) {
								fallDetectorThreads[i].interrupt();
								fallDetectorThreads[i] = new FallDetectorThread();
								fallDetectorThreads[i].start();
							}
						}
					}

					// Detectors
					if (Config.get(Config.getOredetectorkey()) || Config.get(Config.getLavadetectorkey())
							|| Config.get(Config.getWaterdetectorkey())) {
						for (int i = 0; i < oreDetectorThreads.length; i++) {
							if (!oreDetectorThreads[i].alive) {
								oreDetectorThreads[i].start();
							} else if (oreDetectorThreads[i].alive && oreDetectorThreads[i].finished) {
								oreDetectorThreads[i].interrupt();
								oreDetectorThreads[i] = new DetectorThread();
								oreDetectorThreads[i].start();
							}
						}
					}
				}
			} catch (Exception e) {
			}

		});
	}

	private void durabilityThread() {
		if (!modInit.mainThreadMap.containsKey("durablity_thread_key")
				&& Config.get(Config.getDurabilitycheckerkey())) {
			modInit.mainThreadMap.put("durablity_thread_key", 5000);
			if (durabilityThread.isAlive() && durabilityThread != null)
				durabilityThread.interrupt();
			durabilityThread = new DurabilityThread();
			durabilityThread.start();
		}
	}

	public static String get_position_difference(BlockPos blockPos, MinecraftClient client) {
		ClientPlayerEntity player = client.player;
		String dir = client.player.getHorizontalFacing().asString();
		dir = dir.toLowerCase().trim();

		String diffXBlockPos = ((double) player.getBlockPos().getX() - blockPos.getX()) + "";
		String diffYBlockPos = ((double) (player.getBlockPos().getY() + 1) - blockPos.getY()) + "";
		String diffZBlockPos = ((double) player.getBlockPos().getZ() - blockPos.getZ()) + "";

		diffXBlockPos = diffXBlockPos.substring(0, diffXBlockPos.indexOf("."));
		diffYBlockPos = diffYBlockPos.substring(0, diffYBlockPos.indexOf("."));
		diffZBlockPos = diffZBlockPos.substring(0, diffZBlockPos.indexOf("."));

		if (!diffXBlockPos.equalsIgnoreCase("0")) {
			if (dir.contains("east") || dir.contains("west")) {
				if (diffXBlockPos.contains("-") && dir.contains("east")) {
					diffXBlockPos += " blocks away";
				} else if (!diffXBlockPos.contains("-") && dir.contains("west")) {
					diffXBlockPos += " blocks away";
				} else
					diffXBlockPos += " blocks behind";
				diffXBlockPos = diffXBlockPos.replace("-", "");
			} else if (dir.contains("north")) {
				if (diffXBlockPos.contains("-"))
					diffXBlockPos += " blocks to left";
				else
					diffXBlockPos += " blocks to right";
				if (diffXBlockPos.contains("-"))
					diffXBlockPos = diffXBlockPos.replace("-", "");
			} else if (dir.contains("south")) {
				if (diffXBlockPos.contains("-"))
					diffXBlockPos += " blocks to right";
				else
					diffXBlockPos += " blocks to left";
				if (diffXBlockPos.contains("-"))
					diffXBlockPos = diffXBlockPos.replace("-", "");
			}
		} else {
			diffXBlockPos = "";
		}

		if (!diffYBlockPos.equalsIgnoreCase("0")) {
			if (diffYBlockPos.contains("-")) {
				diffYBlockPos = diffYBlockPos.replace("-", "");
				diffYBlockPos += " blocks up";
			} else {
				diffYBlockPos += " blocks down";
			}
		} else {
			diffYBlockPos = "";
		}

		if (!diffZBlockPos.equalsIgnoreCase("0")) {
			if (dir.contains("north") || dir.contains("south")) {
				if (diffZBlockPos.contains("-") && dir.contains("south")) {
					diffZBlockPos += " blocks away";
				} else if (!diffZBlockPos.contains("-") && dir.contains("north")) {
					diffZBlockPos += " blocks away";
				} else
					diffZBlockPos += " blocks behind";
				diffZBlockPos = diffZBlockPos.replace("-", "");
			} else if (dir.contains("east")) {
				if (diffZBlockPos.contains("-"))
					diffZBlockPos += " blocks to right";
				else
					diffZBlockPos += " blocks to left";
				if (diffZBlockPos.contains("-"))
					diffZBlockPos = diffZBlockPos.replace("-", "");
			} else if (dir.contains("west")) {
				if (diffZBlockPos.contains("-"))
					diffZBlockPos += " blocks to left";
				else
					diffZBlockPos += " blocks to right";
				if (diffZBlockPos.contains("-"))
					diffZBlockPos = diffZBlockPos.replace("-", "");
			}
		} else {
			diffZBlockPos = "";
		}

		String text = "";
		if (dir.contains("north") || dir.contains("south"))
			text = String.format("%s  %s  %s", diffZBlockPos, diffYBlockPos, diffXBlockPos);
		else
			text = String.format("%s  %s  %s", diffXBlockPos, diffYBlockPos, diffZBlockPos);
		return text;
	}

	private Entity entityLocking() {
		{
			double closestDouble = -99999;
			Entity closestEntity = null;
			try {
				for (Entity i : client.world.getEntities()) {
					if (!(i instanceof MobEntity))
						continue;
					BlockPos blockPos = i.getBlockPos();

					Vec3d entityVec3d = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
					Vec3d playerVec3d = new Vec3d(client.player.getBlockPos().getX(),
							client.player.getBlockPos().getY(), client.player.getBlockPos().getZ());
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

	private void crosshairTarget() {
		HitResult hit = client.crosshairTarget;
		String text = "";
		switch (hit.getType()) {
		case MISS:
			break;
		case BLOCK:
			if (Config.get(Config.getReadblockskey())) {
				BlockHitResult blockHitResult = (BlockHitResult) hit;
				BlockState blockState = client.world.getBlockState(blockHitResult.getBlockPos());
				Block block = blockState.getBlock();
				if ((!tempBlock.equalsIgnoreCase(block + "")
						|| !(tempBlockPos.equalsIgnoreCase(blockHitResult.getBlockPos() + "")))
						&& !(blockState + "").toLowerCase().contains("sign")) {
					tempBlock = block + "";
					tempBlockPos = blockHitResult.getBlockPos() + "";
					tempEntityPos = "";
					tempEntity = "";
					String side = blockHitResult.getSide().asString();
					String name = "";
					MutableText blockMutableText = new LiteralText("").append(block.getName());
					name = blockMutableText.getString();
					if (side.equalsIgnoreCase("up"))
						side = "top";
					if (side.equalsIgnoreCase("down"))
						side = "bottom";
					text = name + " " + side;
					narrate(text);
				}
			}
			break;
		case ENTITY:

			if (Config.get(Config.getEntitynarratorkey())) {
				try {
					EntityHitResult entityHitResult = (EntityHitResult) hit;

					if (((EntityHitResult) hit).getEntity() == lockedOnEntity)
						break;

					if ((!(((EntityHitResult) hit).getEntity().getDisplayName() + "").equalsIgnoreCase(tempEntity)
							|| !(((EntityHitResult) hit).hashCode() + "").equalsIgnoreCase(tempEntityPos))
							&& !modInit.mainThreadMap.containsKey("entity_narrator_key")) {

						tempEntity = ((EntityHitResult) hit).getEntity().getType() + "";
						tempEntityPos = ((EntityHitResult) hit).hashCode() + "";
						tempBlockPos = "";
						tempBlock = "";
						MutableText entityMutableText = new LiteralText("")
								.append(entityHitResult.getEntity().getName());
						text = entityMutableText.getString();
						narrate(text);
						modInit.mainThreadMap.put("entity_narrator_key", 5000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}

	private void narrate(String st) {
		client.player.sendMessage(new LiteralText(st), true);
	}

}
