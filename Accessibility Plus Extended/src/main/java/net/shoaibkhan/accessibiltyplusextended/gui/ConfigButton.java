package net.shoaibkhan.accessibiltyplusextended.gui;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.text.TranslatableText;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class ConfigButton extends WButton {
  private String translateKey;
  private String jsonKey;

  public ConfigButton(String translationKey, String jsonKey) {
    super(new TranslatableText(translationKey, Config.get(jsonKey) ? "on" : "off"));
    this.translateKey = translationKey;
    this.jsonKey = jsonKey;

  }

  // 1.17
  @Override
  public InputResult onClick(int x, int y, int button) {
    super.onClick(x, y, button);
    if (this.isEnabled()) {
      boolean enabled = Config.toggle(this.jsonKey);
      TranslatableText newButtonText = new TranslatableText(this.translateKey , (enabled ? " : on" : " : off"));
      this.setLabel(newButtonText);
    }
    return InputResult.PROCESSED;
  }

//	// 1.16
//	@Override
//	public void onClick(int x, int y, int button) {
//		super.onClick(x, y, button);
//		if (this.isEnabled()) {
//			boolean enabled = Config.toggle(this.jsonKey);
//			TranslatableText newButtonText = new TranslatableText(this.translateKey , (enabled ? " : on" : " : off"));
//			this.setLabel(newButtonText);
//		}
//	}
}
