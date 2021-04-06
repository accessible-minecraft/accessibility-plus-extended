package net.shoaibkhan.accessibiltyplusextended;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class modInit implements ModInitializer {
	HudRenderCallBackClass hudRenderCallBackClass;
	KeyBinding num_5;
	@Override
	public void onInitialize() {
		System.out.println("Hello Fabric world!");
		num_5 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"Center Camera",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_5,
				"Accessibility Plus Extended"
		));
		hudRenderCallBackClass = new HudRenderCallBackClass(num_5);
	}
}
