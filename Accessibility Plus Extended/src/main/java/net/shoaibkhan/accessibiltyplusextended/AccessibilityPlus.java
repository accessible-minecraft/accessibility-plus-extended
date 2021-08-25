package net.shoaibkhan.accessibiltyplusextended;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.gui.AccessibilityPlusConfigGui;
import net.shoaibkhan.accessibiltyplusextended.gui.ConfigScreen;
import net.shoaibkhan.accessibiltyplusextended.keyboard.KeyboardController;
import net.shoaibkhan.accessibiltyplusextended.mixin.AccessorHandledScreen;

public class AccessibilityPlus {
	public static KeyBinding AP_CONFIG_KEY;
	public static NarratorPlus narrator;
	public static KeyboardController keyboardController;

	public static int currentColumn = 0;
	public static int currentRow = 0;
	public static boolean isDPressed, isAPressed, isWPressed, isSPressed, isRPressed, isFPressed, isCPressed,
			isVPressed, isTPressed, isEnterPressed;
	private HudScreenHandler hudScreenHandler;

	public void onInitialize() {
		AP_CONFIG_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.accessibilityplus.config",
				InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.categories.accessibilityplus.general"));

		narrator = new NarratorPlus();
		keyboardController = new KeyboardController();
		System.setProperty("java.awt.headless", "false");

		hudScreenHandler = new HudScreenHandler();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			if (Config.get(Config.getInvKeyboardControlKey())) {

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

			if (client.player == null)
				return;

			while (AP_CONFIG_KEY.wasPressed()) {
				client.openScreen(new ConfigScreen(new AccessibilityPlusConfigGui(client.player),
						"Accessibility Plus Configuration", client.player));
				return;
			}

			if (client.currentScreen == null || !(client.currentScreen instanceof AccessorHandledScreen)) {
				HitResult hit = client.crosshairTarget;
				switch (hit.getType()) {
				case MISS:
					// nothing near enough
					break;
				case BLOCK:
					try {
						BlockHitResult blockHit = (BlockHitResult) hit;
						BlockPos blockPos = blockHit.getBlockPos();
						BlockState blockState = client.world.getBlockState(blockPos);

						Block block = blockState.getBlock();

						if (!block.equals(narrator.lastBlock) || !blockPos.equals(narrator.lastBlockPos)) {
							narrator.lastBlock = block;
							narrator.lastBlockPos = blockPos;
							String output = "";
							if (Config.get(Config.getAPReadblocksKey())) {
								output += block.getName().getString();
							}
							if (blockState.toString().contains("sign") && Config.get(Config.getReadSignsContents())) {
								try {
									SignBlockEntity signentity = (SignBlockEntity) client.world
											.getBlockEntity(blockPos);
									output += " says: ";
									output += "1: " + signentity.getTextOnRow(0, false).getString() + ", ";
									output += "2: " + signentity.getTextOnRow(1, false).getString() + ", ";
									output += "3: " + signentity.getTextOnRow(2, false).getString() + ", ";
									output += "4: " + signentity.getTextOnRow(3, false).getString();
								} catch (Exception e) {

								} finally {
								}
							}
							if (!output.equals("")) {
								NarratorPlus.narrate(output);
							}
						}
					} finally {
					}
					break;
				case ENTITY:
					// Entity in range
					break;
				}
			}
		});
	}

}
