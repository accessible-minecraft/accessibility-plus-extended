package net.shoaibkhan.accessibiltyplusextended;

import net.fabricmc.api.ModInitializer;

public class modInit implements ModInitializer {
	HudRenderCallBackClass hudRenderCallBackClass;
	@Override
	public void onInitialize() {
		System.out.println("Hello Fabric world!");
		hudRenderCallBackClass = new HudRenderCallBackClass();
	}
}
