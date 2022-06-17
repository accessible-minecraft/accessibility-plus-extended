package net.shoaibkhan.accessibiltyplusextended.gui;

import io.github.cottonmc.cotton.gui.widget.WButton;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class ArrayButton extends WButton {
  private String translateKey;
  private String jsonKey;
  private Object[] array;

  public ArrayButton(String translationKey, String jsonKey, Object[] array) {
    super(generateTitle(translationKey, jsonKey, array));
    this.translateKey = translationKey;
    this.jsonKey = jsonKey;
    this.array = array.clone();
  }

	// post 1.17
	@Override
	public io.github.cottonmc.cotton.gui.widget.data.InputResult onClick(int x, int y, int button) {
		super.onClick(x, y, button);
		if (this.isEnabled()) {
			int val = Config.getInt(jsonKey) + 1;
			if (val == array.length)
				val = 0;
			Config.setString(jsonKey, val + "");
			this.setLabel(generateTitle(translateKey, jsonKey, array));
		}
		return io.github.cottonmc.cotton.gui.widget.data.InputResult.PROCESSED;
	}

	private static Text generateTitle(String translateKey, String jsonKey, Object[] array) {
		Object o = array[Config.getInt(jsonKey)];
		String translatedOption;
		if (o instanceof CharSequence) {
			translatedOption = I18n.translate("gui.apextended.config.buttons." + o);
		} else {
			translatedOption = "" + o;
		}
		return Text.of(I18n.translate(translateKey, translatedOption));
	}

 // pre 1.17
//  @Override
//  public void onClick(int x, int y, int button) {
//    super.onClick(x, y, button);
//    if (this.isEnabled()) {
//      int val = Config.getInt(jsonKey) + 1;
//      if (val == array.length)
//        val = 0;
//      Config.setString(jsonKey, val + "");
//      net.minecraft.text.TranslatableText newButtonText = new net.minecraft.text.TranslatableText(this.translateKey , array[Config.getInt(jsonKey)]);
//      this.setLabel(newButtonText);
//    }
//  }
}
