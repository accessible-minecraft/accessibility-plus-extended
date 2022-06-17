package net.shoaibkhan.accessibiltyplusextended;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;
import net.shoaibkhan.accessibiltyplusextended.features.FeaturesWithThreadHandler;
import net.shoaibkhan.accessibiltyplusextended.features.LockingHandler;
import net.shoaibkhan.accessibiltyplusextended.gui.AccessibilityPlusConfigGui;
import net.shoaibkhan.accessibiltyplusextended.gui.ConfigGui;
import net.shoaibkhan.accessibiltyplusextended.gui.ConfigScreen;
import net.shoaibkhan.accessibiltyplusextended.util.KeyBinds;

public class HudRenderCallBackClass {
	private MinecraftClient client;
	public static int entityNarratorFlag = 0, oreDetectorFlag = 0;
	public static boolean isTradeScreenOpen = false;
	public static boolean isAltPressed, isControlPressed, isDPressed, isAPressed, isWPressed, isSPressed, isRPressed,
			isFPressed, isCPressed, isVPressed, isTPressed, isEnterPressed, isShiftPressed;
	public static int currentColumn = 0;
	public static int currentRow = 0;
	private final HudScreenHandler hudScreenHandler;

	public HudRenderCallBackClass() {
		hudScreenHandler = new HudScreenHandler();
		HudRenderCallback.EVENT.register(this::hudRenderCallbackEventMethod);
	}

	private void hudRenderCallbackEventMethod(MatrixStack matixStack, float f) {
		this.client = MinecraftClient.getInstance();
		if (client.player == null)
			return;

		try {

			keyPresses();

			if (Config.get(ConfigKeys.POI_KEY.getKey()))
				new LockingHandler();

			new FeaturesWithThreadHandler(client);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (Config.get(ConfigKeys.INV_KEYBOARD_CONTROL_KEY.getKey())) {

			isDPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.d").getCode()));
			isAPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.a").getCode()));
			isWPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.w").getCode()));
			isSPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.s").getCode()));
			isRPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.r").getCode()));
			isFPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.f").getCode()));
			isCPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.c").getCode()));
			isVPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.v").getCode()));
			isTPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.t").getCode()));
			isEnterPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.enter").getCode()));

			if (client.currentScreen == null) {
				currentColumn = 0;
				currentRow = 0;
				HudScreenHandler.isSearchingRecipies = false;
				HudScreenHandler.bookPageIndex = 0;
			} else {
				Screen screen = client.currentScreen;
				hudScreenHandler.screenHandler(screen);

				// Reset lockOnBlock
				LockingHandler.lockedOnBlockEntries = "";
				LockingHandler.lockedOnBlock = null;
				LockingHandler.isLockedOntoLadder = false;
			}
		}
	}

	private void keyPresses() {
		isAltPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.left.alt").getCode()) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.right.alt").getCode()));
		isControlPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.left.control").getCode()) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.right.control").getCode()));
		isShiftPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.left.shift").getCode()) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.fromTranslationKey("key.keyboard.right.shift").getCode()));

		while (KeyBinds.CONFIG_KEY.getKeyBind().wasPressed()) {
			if (!isControlPressed) {
				Screen screen = new ConfigScreen(new ConfigGui(client.player, client), "ext.title");
//				client.openScreen(screen); // pre 1.18
				client.setScreen(screen); // post 1.18
				return;
			}
		}

		while (KeyBinds.AP_CONFIG_KEY.getKeyBind().wasPressed()) {
			client.setScreen(new ConfigScreen(new AccessibilityPlusConfigGui(client.player), "title")); // post 1.18
//			client.openScreen(new ConfigScreen(new AccessibilityPlusConfigGui(client.player), "title")); // pre 1.18
			return;
		}

	}

	public static String get_position_difference(BlockPos blockPos, MinecraftClient client) {
		ClientPlayerEntity player = client.player;
		Direction dir = client.player.getHorizontalFacing();

		Vec3d diff = player.getEyePos().subtract(Vec3d.ofCenter(blockPos)); // post 1.17
//		Vec3d diff = (new Vec3d(client.player.getX(), client.player.getEyeY(), client.player.getZ())).subtract(Vec3d.ofCenter(blockPos)); // pre 1.17
		BlockPos diffBlockPos = new BlockPos(Math.round(diff.x), Math.round(diff.y), Math.round(diff.z));

		String diffXBlockPos = "";
		String diffYBlockPos = "";
		String diffZBlockPos = "";

		if (diffBlockPos.getX() != 0) {
			if (dir == Direction.NORTH) {
				diffXBlockPos = diff(diffBlockPos.getX(), "right", "left");
			} else if (dir == Direction.SOUTH) {
				diffXBlockPos = diff(diffBlockPos.getX(), "left", "right");
			} else if (dir == Direction.EAST) {
				diffXBlockPos = diff(diffBlockPos.getX(), "away", "behind");
			} else if (dir == Direction.WEST) {
				diffXBlockPos = diff(diffBlockPos.getX(), "behind", "away");
			}
		}

		if (diffBlockPos.getY() != 0) {
			diffYBlockPos = diff(diffBlockPos.getY(), "up", "down");
		}

		if (diffBlockPos.getZ() != 0) {
			if (dir == Direction.SOUTH) {
				diffZBlockPos = diff(diffBlockPos.getZ(), "away", "behind");
			} else if (dir == Direction.NORTH) {
				diffZBlockPos = diff(diffBlockPos.getZ(), "behind", "away");
			} else if (dir == Direction.EAST) {
				diffZBlockPos = diff(diffBlockPos.getZ(), "right", "left");
			} else if (dir == Direction.WEST) {
				diffZBlockPos = diff(diffBlockPos.getZ(), "left", "right");
			}
		}

		String text = "";
		if (dir == Direction.NORTH || dir == Direction.SOUTH)
			text = String.format("%s  %s  %s", diffZBlockPos, diffYBlockPos, diffXBlockPos);
		else
			text = String.format("%s  %s  %s", diffXBlockPos, diffYBlockPos, diffZBlockPos);
		return text;
	}

	private static String diff(int blocks, String key1, String key2) {
		return I18n.translate("narrate.apextended.posDiff." + (blocks < 0 ? key1 : key2), Math.abs(blocks));
	}
}
