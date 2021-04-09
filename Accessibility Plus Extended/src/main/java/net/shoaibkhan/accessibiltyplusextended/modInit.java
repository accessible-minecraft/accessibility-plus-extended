package net.shoaibkhan.accessibiltyplusextended;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class modInit implements ModInitializer {
	HudRenderCallBackClass hudRenderCallBackClass;
	@Override
	public void onInitialize() {
		System.out.println("Accessibilty Plus Extended is initializing!");
		hudRenderCallBackClass = new HudRenderCallBackClass();
	}
}
