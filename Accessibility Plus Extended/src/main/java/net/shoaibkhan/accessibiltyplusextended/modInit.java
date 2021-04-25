package net.shoaibkhan.accessibiltyplusextended;

import net.fabricmc.api.ModInitializer;

public class modInit implements ModInitializer {
	HudRenderCallBackClass hudRenderCallBackClass;
	@Override
	public void onInitialize() {
		System.out.println("Accessibilty Plus Extended is initializing!");
		hudRenderCallBackClass = new HudRenderCallBackClass();
	}
}
