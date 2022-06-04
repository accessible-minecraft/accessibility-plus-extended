package net.shoaibkhan.accessibiltyplusextended.gui;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class ConfigButton extends WButton {
  private String translateKey;
  private String jsonKey;

  public ConfigButton(String translationKey, String jsonKey) {
    super(generateTitle(translationKey, jsonKey, Config.get(jsonKey)));
    this.translateKey = translationKey;
    this.jsonKey = jsonKey;
  }

	// 1.17
	@Override
	public InputResult onClick(int x, int y, int button) {
		super.onClick(x, y, button);
		if (this.isEnabled()) {
			this.setLabel(generateTitle(translateKey, jsonKey, Config.toggle(this.jsonKey)));
		}
		return InputResult.PROCESSED;
	}

	private static MutableText generateTitle(String translateKey, String jsonKey, boolean enabled) {
		String translatedOption = I18n.translate("gui.apextended.config.buttons." + (enabled ? "on" : "off"));
		return new TranslatableText(translateKey, translatedOption);
	}

	// 1.16
//	@Override
//	public void onClick(int x, int y, int button) {
//		super.onClick(x, y, button);
//		if (this.isEnabled()) {
//			boolean enabled = Config.toggle(this.jsonKey);
//			TranslatableText newButtonText = new TranslatableText(this.translateKey , (enabled ? "on" : "off"));
//			this.setLabel(newButtonText);
//		}
//	}
}
