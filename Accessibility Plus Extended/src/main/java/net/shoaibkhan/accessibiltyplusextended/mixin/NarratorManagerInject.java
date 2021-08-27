package net.shoaibkhan.accessibiltyplusextended.mixin;

import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.util.NarratorManager;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;

import java.util.UUID;

@Mixin(NarratorManager.class)
public class NarratorManagerInject {

  @Inject(at = @At("HEAD"), method = "narrate(Ljava/lang/String;)V", cancellable = true)
  public void sayWithNVDA(String message, CallbackInfo ci) {
    if (NarratorPlus.isNVDALoaded()) {
      NarratorPlus.narrate(message);
      ci.cancel();
    }
  }

  @Inject(at = @At("HEAD"), method = "onChatMessage", cancellable = true)
  public void onChatMessage(MessageType messageType, Text message, UUID sender, CallbackInfo ci) {
    String option = NarratorPlus.chatOptions[Config.getInt(Config.getChatnarration())];

    switch (option){
      case "on": {
        if (NarratorPlus.isNVDALoaded()) {
          Object text2;
          if (message instanceof TranslatableText && "chat.type.text".equals(((TranslatableText) message).getKey())) {
            text2 = new TranslatableText("chat.type.text.narrate", ((TranslatableText) message).getArgs());
          } else {
            text2 = message;
          }

          String string = ((Text) text2).getString();
          NarratorPlus.narrate(string);
          ci.cancel();
        }
        break;
      }
      case "off": {
        ci.cancel();
        break;
      }
      case "default":{
        break;
      }
    }
  }
}
