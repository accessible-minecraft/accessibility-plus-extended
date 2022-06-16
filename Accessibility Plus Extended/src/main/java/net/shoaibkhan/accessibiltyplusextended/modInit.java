package net.shoaibkhan.accessibiltyplusextended;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import io.github.cottonmc.cotton.gui.widget.data.Color;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
//import net.minecraft.client.options.KeyBinding;
 import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.shoaibkhan.accessibiltyplusextended.keyboard.KeyboardController;
import net.shoaibkhan.accessibiltyplusextended.util.KeyBinds;

public class modInit implements ClientModInitializer {
	HudRenderCallBackClass hudRenderCallBackClass;
	public static Map<String, Integer> mainThreadMap;
	private static CustomWait mainThreadCustomWait;
	public static List<String> lowDurabilityItems = new ArrayList<String>();

	// Accessibility Plus
	public static NarratorPlus narrator;
	public static KeyboardController keyboardController;

	@Override
	public void onInitializeClient() {
		keyboardController = new KeyboardController();

		narrator = new NarratorPlus();
		this.initializeKeyBinds();
		System.setProperty("java.awt.headless", "false");

		new customCommands(); // post 1.19


		mainThreadMap = new HashMap<String, Integer>();
		mainThreadCustomWait = new CustomWait();
		mainThreadCustomWait.startThread();
		hudRenderCallBackClass = new HudRenderCallBackClass();
	}

	private void initializeKeyBinds() {
		KeyBinds.CONFIG_KEY.setKeyBind(KeyBindingHelper.registerKeyBinding(new KeyBinding("gui.apextended.config.ext.title", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.categories.apextended.general")));
		KeyBinds.LockEntityKey.setKeyBind(KeyBindingHelper.registerKeyBinding(new KeyBinding("key.apextended.lockEntity", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Y, "key.categories.apextended.general")));
		KeyBinds.AP_CONFIG_KEY.setKeyBind(KeyBindingHelper.registerKeyBinding(new KeyBinding("key.apextended.config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.categories.apextended.general")));

		KeyBinds.LEFT_KEY.setKeyBind(KeyBindingHelper.registerKeyBinding(new KeyBinding("key.apextended.left", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT, "key.categories.apextended.inventorycontrol")));
		KeyBinds.RIGHT_KEY.setKeyBind(KeyBindingHelper.registerKeyBinding(new KeyBinding("key.apextended.right", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT, "key.categories.apextended.inventorycontrol")));
		KeyBinds.UP_KEY.setKeyBind(KeyBindingHelper.registerKeyBinding(new KeyBinding("key.apextended.up", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UP, "key.categories.apextended.inventorycontrol")));
		KeyBinds.DOWN_KEY.setKeyBind(KeyBindingHelper.registerKeyBinding(new KeyBinding("key.apextended.down", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_DOWN, "key.categories.apextended.inventorycontrol")));
		KeyBinds.GROUP_KEY.setKeyBind(KeyBindingHelper.registerKeyBinding(new KeyBinding("key.apextended.group", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_TAB, "key.categories.apextended.inventorycontrol")));
		KeyBinds.HOME_KEY.setKeyBind(KeyBindingHelper.registerKeyBinding(new KeyBinding("key.apextended.home", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_HOME, "key.categories.apextended.inventorycontrol")));
		KeyBinds.END_KEY.setKeyBind(KeyBindingHelper.registerKeyBinding(new KeyBinding("key.apextended.end", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_END, "key.categories.apextended.inventorycontrol")));
		KeyBinds.CLICK_KEY.setKeyBind(KeyBindingHelper.registerKeyBinding(new KeyBinding("key.apextended.mouseclick", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.categories.apextended.inventorycontrol")));
		KeyBinds.RIGHT_CLICK_KEY.setKeyBind(KeyBindingHelper.registerKeyBinding(new KeyBinding("key.apextended.mouserightclick", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "key.categories.apextended.inventorycontrol")));
	}
	public static int colors(String c, int o) {
		c = c.trim().toLowerCase();
		switch (c) {
			case "red":
				if (o > 95 && o <= 100)
					return 0xffdb0000;
				else if (o > 90 && o <= 95)
					return 0xfadb0000;
				else if (o > 85 && o <= 90)
					return 0xe6db0000;
				else if (o > 80 && o <= 85)
					return 0xd9db0000;
				else if (o > 75 && o <= 80)
					return 0xccdb0000;
				else if (o > 70 && o <= 75)
					return 0xbfdb0000;
				else if (o > 65 && o <= 70)
					return 0xb3db0000;
				else if (o > 60 && o <= 65)
					return 0xa6db0000;
				else if (o > 55 && o <= 60)
					return 0x99db0000;
				else if (o > 50 && o <= 55)
					return 0x8cdb0000;
				else if (o > 45 && o <= 50)
					return 0x80db0000;
				else if (o > 40 && o <= 45)
					return 0x73db0000;
				else if (o > 35 && o <= 40)
					return 0x66db0000;
				else if (o > 30 && o <= 35)
					return 0x59db0000;
				else if (o > 25 && o <= 30)
					return 0x4ddb0000;
				else if (o > 20 && o <= 25)
					return 0x40db0000;
				else if (o > 15 && o <= 20)
					return 0x33db0000;
				else if (o > 10 && o <= 15)
					return 0x26db0000;
				else if (o > 5 && o <= 10)
					return 0x1adb0000;
				else if (o > 0 && o <= 5)
					return 0x0ddb0000;
				else if (o <= 0)
					return 0x00db0000;
				return 0xffdb0000;
			case "grey":
				if (o > 95 && o <= 100)
					return 0xff808080;
				else if (o > 90 && o <= 95)
					return 0xfa808080;
				else if (o > 85 && o <= 90)
					return 0xe6808080;
				else if (o > 80 && o <= 85)
					return 0xd9808080;
				else if (o > 75 && o <= 80)
					return 0xcc808080;
				else if (o > 70 && o <= 75)
					return 0xbf808080;
				else if (o > 65 && o <= 70)
					return 0xb3808080;
				else if (o > 60 && o <= 65)
					return 0xa6808080;
				else if (o > 55 && o <= 60)
					return 0x99808080;
				else if (o > 50 && o <= 55)
					return 0x8c808080;
				else if (o > 45 && o <= 50)
					return 0x80808080;
				else if (o > 40 && o <= 45)
					return 0x73808080;
				else if (o > 35 && o <= 40)
					return 0x66808080;
				else if (o > 30 && o <= 35)
					return 0x59808080;
				else if (o > 25 && o <= 30)
					return 0x4d808080;
				else if (o > 20 && o <= 25)
					return 0x40808080;
				else if (o > 15 && o <= 20)
					return 0x33808080;
				else if (o > 10 && o <= 15)
					return 0x26808080;
				else if (o > 5 && o <= 10)
					return 0x1a808080;
				else if (o > 0 && o <= 5)
					return 0x0d808080;
				else if (o <= 0)
					return 0x00808080;
				return 0xff808080;
			case "purple":
				if (o > 95 && o <= 100)
					return 0xff800080;
				else if (o > 90 && o <= 95)
					return 0xfa800080;
				else if (o > 85 && o <= 90)
					return 0xe6800080;
				else if (o > 80 && o <= 85)
					return 0xd9800080;
				else if (o > 75 && o <= 80)
					return 0xcc800080;
				else if (o > 70 && o <= 75)
					return 0xbf800080;
				else if (o > 65 && o <= 70)
					return 0xb3800080;
				else if (o > 60 && o <= 65)
					return 0xa6800080;
				else if (o > 55 && o <= 60)
					return 0x99800080;
				else if (o > 50 && o <= 55)
					return 0x8c800080;
				else if (o > 45 && o <= 50)
					return 0x80800080;
				else if (o > 40 && o <= 45)
					return 0x73800080;
				else if (o > 35 && o <= 40)
					return 0x66800080;
				else if (o > 30 && o <= 35)
					return 0x59800080;
				else if (o > 25 && o <= 30)
					return 0x4d800080;
				else if (o > 20 && o <= 25)
					return 0x40800080;
				else if (o > 15 && o <= 20)
					return 0x33800080;
				else if (o > 10 && o <= 15)
					return 0x26800080;
				else if (o > 5 && o <= 10)
					return 0x1a800080;
				else if (o > 0 && o <= 5)
					return 0x0d800080;
				else if (o <= 0)
					return 0x00800080;
				return 0xff800080;
			case "white":
				if (o > 95 && o <= 100)
					return 0xfff0f0f0;
				else if (o > 90 && o <= 95)
					return 0xfaf0f0f0;
				else if (o > 85 && o <= 90)
					return 0xe6f0f0f0;
				else if (o > 80 && o <= 85)
					return 0xd9f0f0f0;
				else if (o > 75 && o <= 80)
					return 0xccf0f0f0;
				else if (o > 70 && o <= 75)
					return 0xbff0f0f0;
				else if (o > 65 && o <= 70)
					return 0xb3f0f0f0;
				else if (o > 60 && o <= 65)
					return 0xa6f0f0f0;
				else if (o > 55 && o <= 60)
					return 0x99f0f0f0;
				else if (o > 50 && o <= 55)
					return 0x8cf0f0f0;
				else if (o > 45 && o <= 50)
					return 0x80f0f0f0;
				else if (o > 40 && o <= 45)
					return 0x73f0f0f0;
				else if (o > 35 && o <= 40)
					return 0x66f0f0f0;
				else if (o > 30 && o <= 35)
					return 0x59f0f0f0;
				else if (o > 25 && o <= 30)
					return 0x4df0f0f0;
				else if (o > 20 && o <= 25)
					return 0x40f0f0f0;
				else if (o > 15 && o <= 20)
					return 0x33f0f0f0;
				else if (o > 10 && o <= 15)
					return 0x26f0f0f0;
				else if (o > 5 && o <= 10)
					return 0x1af0f0f0;
				else if (o > 0 && o <= 5)
					return 0x0df0f0f0;
				else if (o <= 0)
					return 0x00f0f0f0;
				return 0xfff0f0f0;
			case "black":
				if (o > 95 && o <= 100)
					return 0xff0f0f0f;
				else if (o > 90 && o <= 95)
					return 0xfa0f0f0f;
				else if (o > 85 && o <= 90)
					return 0xe60f0f0f;
				else if (o > 80 && o <= 85)
					return 0xd90f0f0f;
				else if (o > 75 && o <= 80)
					return 0xcc0f0f0f;
				else if (o > 70 && o <= 75)
					return 0xbf0f0f0f;
				else if (o > 65 && o <= 70)
					return 0xb30f0f0f;
				else if (o > 60 && o <= 65)
					return 0xa60f0f0f;
				else if (o > 55 && o <= 60)
					return 0x990f0f0f;
				else if (o > 50 && o <= 55)
					return 0x8c0f0f0f;
				else if (o > 45 && o <= 50)
					return 0x800f0f0f;
				else if (o > 40 && o <= 45)
					return 0x730f0f0f;
				else if (o > 35 && o <= 40)
					return 0x660f0f0f;
				else if (o > 30 && o <= 35)
					return 0x590f0f0f;
				else if (o > 25 && o <= 30)
					return 0x4d0f0f0f;
				else if (o > 20 && o <= 25)
					return 0x400f0f0f;
				else if (o > 15 && o <= 20)
					return 0x330f0f0f;
				else if (o > 10 && o <= 15)
					return 0x260f0f0f;
				else if (o > 5 && o <= 10)
					return 0x1a0f0f0f;
				else if (o > 0 && o <= 5)
					return 0x0d0f0f0f;
				else if (o <= 0)
					return 0x000f0f0f;
				return 0xff0f0f0f;
			case "pink":
				if (o > 95 && o <= 100)
					return 0xffff0f87;
				else if (o > 90 && o <= 95)
					return 0xfaff0f87;
				else if (o > 85 && o <= 90)
					return 0xe6ff0f87;
				else if (o > 80 && o <= 85)
					return 0xd9ff0f87;
				else if (o > 75 && o <= 80)
					return 0xccff0f87;
				else if (o > 70 && o <= 75)
					return 0xbfff0f87;
				else if (o > 65 && o <= 70)
					return 0xb3ff0f87;
				else if (o > 60 && o <= 65)
					return 0xa6ff0f87;
				else if (o > 55 && o <= 60)
					return 0x99ff0f87;
				else if (o > 50 && o <= 55)
					return 0x8cff0f87;
				else if (o > 45 && o <= 50)
					return 0x80ff0f87;
				else if (o > 40 && o <= 45)
					return 0x73ff0f87;
				else if (o > 35 && o <= 40)
					return 0x66ff0f87;
				else if (o > 30 && o <= 35)
					return 0x59ff0f87;
				else if (o > 25 && o <= 30)
					return 0x4dff0f87;
				else if (o > 20 && o <= 25)
					return 0x40ff0f87;
				else if (o > 15 && o <= 20)
					return 0x33ff0f87;
				else if (o > 10 && o <= 15)
					return 0x26ff0f87;
				else if (o > 5 && o <= 10)
					return 0x1aff0f87;
				else if (o > 0 && o <= 5)
					return 0x0dff0f87;
				else if (o <= 0)
					return 0x00ff0f87;
				return 0xffff0f87;
			case "blue":
				if (o > 95 && o <= 100)
					return 0xff1f1fff;
				else if (o > 90 && o <= 95)
					return 0xfa1f1fff;
				else if (o > 85 && o <= 90)
					return 0xe61f1fff;
				else if (o > 80 && o <= 85)
					return 0xd91f1fff;
				else if (o > 75 && o <= 80)
					return 0xcc1f1fff;
				else if (o > 70 && o <= 75)
					return 0xbf1f1fff;
				else if (o > 65 && o <= 70)
					return 0xb31f1fff;
				else if (o > 60 && o <= 65)
					return 0xa61f1fff;
				else if (o > 55 && o <= 60)
					return 0x991f1fff;
				else if (o > 50 && o <= 55)
					return 0x8c1f1fff;
				else if (o > 45 && o <= 50)
					return 0x801f1fff;
				else if (o > 40 && o <= 45)
					return 0x731f1fff;
				else if (o > 35 && o <= 40)
					return 0x661f1fff;
				else if (o > 30 && o <= 35)
					return 0x591f1fff;
				else if (o > 25 && o <= 30)
					return 0x4d1f1fff;
				else if (o > 20 && o <= 25)
					return 0x401f1fff;
				else if (o > 15 && o <= 20)
					return 0x331f1fff;
				else if (o > 10 && o <= 15)
					return 0x261f1fff;
				else if (o > 5 && o <= 10)
					return 0x1a1f1fff;
				else if (o > 0 && o <= 5)
					return 0x0d1f1fff;
				else if (o <= 0)
					return 0x001f1fff;
				return 0xff1f1fff;
			case "green":
				if (o > 95 && o <= 100)
					return 0xff00bd00;
				else if (o > 90 && o <= 95)
					return 0xfa00bd00;
				else if (o > 85 && o <= 90)
					return 0xe600bd00;
				else if (o > 80 && o <= 85)
					return 0xd900bd00;
				else if (o > 75 && o <= 80)
					return 0xcc00bd00;
				else if (o > 70 && o <= 75)
					return 0xbf00bd00;
				else if (o > 65 && o <= 70)
					return 0xb300bd00;
				else if (o > 60 && o <= 65)
					return 0xa600bd00;
				else if (o > 55 && o <= 60)
					return 0x9900bd00;
				else if (o > 50 && o <= 55)
					return 0x8c00bd00;
				else if (o > 45 && o <= 50)
					return 0x8000bd00;
				else if (o > 40 && o <= 45)
					return 0x7300bd00;
				else if (o > 35 && o <= 40)
					return 0x6600bd00;
				else if (o > 30 && o <= 35)
					return 0x5900bd00;
				else if (o > 25 && o <= 30)
					return 0x4d00bd00;
				else if (o > 20 && o <= 25)
					return 0x4000bd00;
				else if (o > 15 && o <= 20)
					return 0x3300bd00;
				else if (o > 10 && o <= 15)
					return 0x2600bd00;
				else if (o > 5 && o <= 10)
					return 0x1a00bd00;
				else if (o > 0 && o <= 5)
					return 0x0d00bd00;
				else if (o <= 0)
					return 0x0000bd00;
				return 0xff00bd00;
			case "yellow":
				if (o > 95 && o <= 100)
					return 0xffffff3d;
				else if (o > 90 && o <= 95)
					return 0xfaffff3d;
				else if (o > 85 && o <= 90)
					return 0xe6ffff3d;
				else if (o > 80 && o <= 85)
					return 0xd9ffff3d;
				else if (o > 75 && o <= 80)
					return 0xccffff3d;
				else if (o > 70 && o <= 75)
					return 0xbfffff3d;
				else if (o > 65 && o <= 70)
					return 0xb3ffff3d;
				else if (o > 60 && o <= 65)
					return 0xa6ffff3d;
				else if (o > 55 && o <= 60)
					return 0x99ffff3d;
				else if (o > 50 && o <= 55)
					return 0x8cffff3d;
				else if (o > 45 && o <= 50)
					return 0x80ffff3d;
				else if (o > 40 && o <= 45)
					return 0x73ffff3d;
				else if (o > 35 && o <= 40)
					return 0x66ffff3d;
				else if (o > 30 && o <= 35)
					return 0x59ffff3d;
				else if (o > 25 && o <= 30)
					return 0x4dffff3d;
				else if (o > 20 && o <= 25)
					return 0x40ffff3d;
				else if (o > 15 && o <= 20)
					return 0x33ffff3d;
				else if (o > 10 && o <= 15)
					return 0x26ffff3d;
				else if (o > 5 && o <= 10)
					return 0x1affff3d;
				else if (o > 0 && o <= 5)
					return 0x0dffff3d;
				else if (o <= 0)
					return 0x00ffff3d;
				return 0xffffff3d;
			case "orange":
				if (o > 95 && o <= 100)
					return 0xffe09200;
				else if (o > 90 && o <= 95)
					return 0xfae09200;
				else if (o > 85 && o <= 90)
					return 0xe6e09200;
				else if (o > 80 && o <= 85)
					return 0xd9e09200;
				else if (o > 75 && o <= 80)
					return 0xcce09200;
				else if (o > 70 && o <= 75)
					return 0xbfe09200;
				else if (o > 65 && o <= 70)
					return 0xb3e09200;
				else if (o > 60 && o <= 65)
					return 0xa6e09200;
				else if (o > 55 && o <= 60)
					return 0x99e09200;
				else if (o > 50 && o <= 55)
					return 0x8ce09200;
				else if (o > 45 && o <= 50)
					return 0x80e09200;
				else if (o > 40 && o <= 45)
					return 0x73e09200;
				else if (o > 35 && o <= 40)
					return 0x66e09200;
				else if (o > 30 && o <= 35)
					return 0x59e09200;
				else if (o > 25 && o <= 30)
					return 0x4de09200;
				else if (o > 20 && o <= 25)
					return 0x40e09200;
				else if (o > 15 && o <= 20)
					return 0x33e09200;
				else if (o > 10 && o <= 15)
					return 0x26e09200;
				else if (o > 5 && o <= 10)
					return 0x1ae09200;
				else if (o > 0 && o <= 5)
					return 0x0de09200;
				else if (o <= 0)
					return 0x00e09200;
				return 0xffe09200;
			case "brown":
				if (o > 95 && o <= 100)
					return 0xff610000;
				else if (o > 90 && o <= 95)
					return 0xfa610000;
				else if (o > 85 && o <= 90)
					return 0xe6610000;
				else if (o > 80 && o <= 85)
					return 0xd9610000;
				else if (o > 75 && o <= 80)
					return 0xcc610000;
				else if (o > 70 && o <= 75)
					return 0xbf610000;
				else if (o > 65 && o <= 70)
					return 0xb3610000;
				else if (o > 60 && o <= 65)
					return 0xa6610000;
				else if (o > 55 && o <= 60)
					return 0x99610000;
				else if (o > 50 && o <= 55)
					return 0x8c610000;
				else if (o > 45 && o <= 50)
					return 0x80610000;
				else if (o > 40 && o <= 45)
					return 0x73610000;
				else if (o > 35 && o <= 40)
					return 0x66610000;
				else if (o > 30 && o <= 35)
					return 0x59610000;
				else if (o > 25 && o <= 30)
					return 0x4d610000;
				else if (o > 20 && o <= 25)
					return 0x40610000;
				else if (o > 15 && o <= 20)
					return 0x33610000;
				else if (o > 10 && o <= 15)
					return 0x26610000;
				else if (o > 5 && o <= 10)
					return 0x1a610000;
				else if (o > 0 && o <= 5)
					return 0x0d610000;
				else if (o <= 0)
					return 0x00610000;
				return 0xff610000;
			case "lightgrey":
				if (o > 95 && o <= 100)
					return 0xffececec;
				else if (o > 90 && o <= 95)
					return 0xfaececec;
				else if (o > 85 && o <= 90)
					return 0xe6ececec;
				else if (o > 80 && o <= 85)
					return 0xd9ececec;
				else if (o > 75 && o <= 80)
					return 0xccececec;
				else if (o > 70 && o <= 75)
					return 0xbfececec;
				else if (o > 65 && o <= 70)
					return 0xb3ececec;
				else if (o > 60 && o <= 65)
					return 0xa6ececec;
				else if (o > 55 && o <= 60)
					return 0x99ececec;
				else if (o > 50 && o <= 55)
					return 0x8cececec;
				else if (o > 45 && o <= 50)
					return 0x80ececec;
				else if (o > 40 && o <= 45)
					return 0x73ececec;
				else if (o > 35 && o <= 40)
					return 0x66ececec;
				else if (o > 30 && o <= 35)
					return 0x59ececec;
				else if (o > 25 && o <= 30)
					return 0x4dececec;
				else if (o > 20 && o <= 25)
					return 0x40ececec;
				else if (o > 15 && o <= 20)
					return 0x33ececec;
				else if (o > 10 && o <= 15)
					return 0x26ececec;
				else if (o > 5 && o <= 10)
					return 0x1aececec;
				else if (o > 0 && o <= 5)
					return 0x0dececec;
				else if (o <= 0)
					return 0x00ececec;
				return 0xffececec;
			default:
				if (c.contains("#"))
					c = c.replace("#", "");
				if (c.contains("0x"))
					c = c.replace("0x", "");
				int hex = Integer.parseInt(c, 16);
				int r = (hex & 0xff0000) >> 16;
				int g = (hex & 0xff00) >> 8;
				int b = (hex & 0xff);
				Color color = Color.rgb((int) (o * 2.55), r, g, b);
				return color.toRgb();
		}
	}

}
