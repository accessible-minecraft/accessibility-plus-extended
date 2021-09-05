package net.shoaibkhan.accessibiltyplusextended.gui;

import io.github.cottonmc.cotton.gui.widget.WButton;
//import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.text.TranslatableText;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class ArrayButton extends WButton {
  private String translateKey;
  private String jsonKey;
  private String[] array;

  public ArrayButton(String translationKey, String jsonKey, String[] array) {
    super(new TranslatableText(translationKey, " :" + array[Config.getInt(jsonKey)]));
    this.translateKey = translationKey;
    this.jsonKey = jsonKey;
    this.array = array.clone();
  }

//  //1.17
//  @Override
//  public InputResult onClick(int x, int y, int button) {
//    super.onClick(x, y, button);
//    if (this.isEnabled()) {
//      int val = Config.getInt(jsonKey) + 1;
//      if (val == array.length)
//        val = 0;
//      Config.setString(jsonKey, val + "");
//      TranslatableText newButtonText = new TranslatableText(this.translateKey , " :" + array[Config.getInt(jsonKey)]);
//      this.setLabel(newButtonText);
//    }
//    return InputResult.PROCESSED;
//  }

  //1.16
   @Override
   public void onClick(int x, int y, int button) {
     super.onClick(x, y, button);
     if (this.isEnabled()) {
       int val = Config.getInt(jsonKey) + 1;
       if (val == array.length)
         val = 0;
       Config.setString(jsonKey, val + "");
       TranslatableText newButtonText = new TranslatableText(this.translateKey , " :" + array[Config.getInt(jsonKey)]);
       this.setLabel(newButtonText);
     }
   }
}
