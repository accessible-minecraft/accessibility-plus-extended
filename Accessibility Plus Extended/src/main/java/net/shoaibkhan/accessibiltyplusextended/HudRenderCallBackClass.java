package net.shoaibkhan.accessibiltyplusextended;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
// import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;
import net.shoaibkhan.accessibiltyplusextended.features.EntityLocking;
import net.shoaibkhan.accessibiltyplusextended.features.FeaturesWithThreadHandler;
import net.shoaibkhan.accessibiltyplusextended.gui.AccessibilityPlusConfigGui;
import net.shoaibkhan.accessibiltyplusextended.gui.ConfigGui;
import net.shoaibkhan.accessibiltyplusextended.gui.ConfigScreen;

public class HudRenderCallBackClass {
	private MinecraftClient client;
	public static int entityNarratorFlag = 0, oreDetectorFlag = 0;
	public static boolean isTradeScreenOpen = false;
	public static boolean isAltPressed, isControlPressed, isDPressed, isAPressed, isWPressed, isSPressed, isRPressed,
			isFPressed, isCPressed, isVPressed, isTPressed, isEnterPressed;
	private KeyBinding CONFIG_KEY, LockEntityKey, AP_CONFIG_KEY;
	public static int currentColumn = 0;
	public static int currentRow = 0;
	private HudScreenHandler hudScreenHandler;

	public HudRenderCallBackClass(KeyBinding CONFIG_KEY, KeyBinding LockEntityKey, KeyBinding AP_CONFIG_KEY) {
		this.CONFIG_KEY = CONFIG_KEY;
		this.AP_CONFIG_KEY = AP_CONFIG_KEY;
		this.LockEntityKey = LockEntityKey;
		hudScreenHandler = new HudScreenHandler();
		HudRenderCallback.EVENT.register(this::hudRenderCallbackEventMethod);
	}

	private void hudRenderCallbackEventMethod(MatrixStack matixStack, float f) {
		this.client = MinecraftClient.getInstance();
		if (client.player == null)
			return;

		try {

			keyPresses(CONFIG_KEY);

			new EntityLocking(client, LockEntityKey);

			new FeaturesWithThreadHandler(client);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (Config.get(ConfigKeys.INV_KEYBOARD_CONTROL_KEY.getKey())) {

			isDPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
					InputUtil.fromTranslationKey("key.keyboard.d").getCode()));
			isAPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
					InputUtil.fromTranslationKey("key.keyboard.a").getCode()));
			isWPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
					InputUtil.fromTranslationKey("key.keyboard.w").getCode()));
			isSPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
					InputUtil.fromTranslationKey("key.keyboard.s").getCode()));
			isRPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
					InputUtil.fromTranslationKey("key.keyboard.r").getCode()));
			isFPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
					InputUtil.fromTranslationKey("key.keyboard.f").getCode()));
			isCPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
					InputUtil.fromTranslationKey("key.keyboard.c").getCode()));
			isVPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
					InputUtil.fromTranslationKey("key.keyboard.v").getCode()));
			isTPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
					InputUtil.fromTranslationKey("key.keyboard.t").getCode()));
			isEnterPressed = (InputUtil.isKeyPressed(client.getWindow().getHandle(),
					InputUtil.fromTranslationKey("key.keyboard.enter").getCode()));

			if (client.currentScreen == null) {
				currentColumn = 0;
				currentRow = 0;
				HudScreenHandler.isSearchingRecipies = false;
				HudScreenHandler.bookPageIndex = 0;
			} else {
				Screen screen = client.currentScreen;
				hudScreenHandler.screenHandler(screen);
			}
		}
	}

	private void keyPresses(KeyBinding CONFIG_KEY) {
		isAltPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),
				InputUtil.fromTranslationKey("key.keyboard.left.alt").getCode())
				|| InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),
				InputUtil.fromTranslationKey("key.keyboard.right.alt").getCode()));
		isControlPressed = (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),
				InputUtil.fromTranslationKey("key.keyboard.left.control").getCode())
				|| InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),
				InputUtil.fromTranslationKey("key.keyboard.right.control").getCode()));

		while (CONFIG_KEY.wasPressed()) {
			if (!isControlPressed) {
				Screen screen = new ConfigScreen(new ConfigGui(client.player, client), "AP Extended Configuration",
						client.player);
				client.openScreen(screen);
				return;
			}
		}

		while (AP_CONFIG_KEY.wasPressed()) {
			client.openScreen(new ConfigScreen(new AccessibilityPlusConfigGui(client.player),
					"Accessibility Plus Configuration", client.player));
			return;
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
}
