package net.shoaibkhan.accessibiltyplusextended.gui;

import io.github.cottonmc.cotton.gui.widget.WButton;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class ConfigButton extends WButton {
  private String translateKey;
  private String jsonKey;

  public ConfigButton(String translationKey, String jsonKey) {
    super(generateTitle(translationKey, jsonKey, Config.get(jsonKey)));
    this.translateKey = translationKey;
    this.jsonKey = jsonKey;
  }

	// post 1.17
	@Override
	public io.github.cottonmc.cotton.gui.widget.data.InputResult onClick(int x, int y, int button) {
		super.onClick(x, y, button);
		if (this.isEnabled()) {
			this.setLabel(generateTitle(translateKey, jsonKey, Config.toggle(this.jsonKey)));
		}
		return io.github.cottonmc.cotton.gui.widget.data.InputResult.PROCESSED;
	}

	private static Text generateTitle(String translateKey, String jsonKey, boolean enabled) {
		String translatedOption = I18n.translate("gui.apextended.config.buttons." + (enabled ? "on" : "off"));
		return Text.of(I18n.translate(translateKey, translatedOption));
	}

	// pre 1.17
//	@Override
//	public void onClick(int x, int y, int button) {
//		super.onClick(x, y, button);
//		if (this.isEnabled()) {
//			boolean enabled = Config.toggle(this.jsonKey);
//			net.minecraft.text.TranslatableText newButtonText = new net.minecraft.text.TranslatableText(this.translateKey , (enabled ? "on" : "off"));
//			this.setLabel(newButtonText);
//		}
//	}
}
