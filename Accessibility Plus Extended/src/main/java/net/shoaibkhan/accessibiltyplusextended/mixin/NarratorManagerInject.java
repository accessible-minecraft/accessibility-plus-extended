package net.shoaibkhan.accessibiltyplusextended.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.NarratorMode;
import net.minecraft.network.message.MessageSender;
import net.minecraft.network.message.MessageType;
import net.minecraft.text.Text;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;
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

  // pre 1.19
  /*
  @Inject(at = @At("HEAD"), method = "onChatMessage", cancellable = true)
  public void onChatMessage(MessageType type, Text message, UUID sender, CallbackInfo ci) {
    String option = NarratorPlus.chatOptions[Config.getInt(ConfigKeys.CHAT_NARRATION.getKey())];

    switch (option) {
      case "on": {
        if (NarratorPlus.isNVDALoaded()) {
          Text text2;
          if (message instanceof net.minecraft.text.TranslatableText && "chat.type.text".equals(((net.minecraft.text.TranslatableText) message).getKey())) {
            text2 = new net.minecraft.text.TranslatableText("chat.type.text.narrate", ((net.minecraft.text.TranslatableText) message).getArgs());
          } else {
            text2 = message;
          }

          String string = text2.getString();
          NarratorPlus.narrate(string);
          ci.cancel();
          break;
        }
      }
      case "off" : {
        ci.cancel();
        break;
      }
      case "default" : {
        break;
      }
    }
  }
   */

  // post 1.19
  @Inject(at = @At("HEAD"), method = "onChatMessage", cancellable = true)
  public void onChatMessage(MessageType type, Text message, MessageSender sender, CallbackInfo ci) {
    String option = NarratorPlus.chatOptions[Config.getInt(ConfigKeys.CHAT_NARRATION.getKey())];

    switch (option) {
      case "on" -> {
        if (NarratorPlus.isNVDALoaded()) {
          NarratorMode narratorMode = MinecraftClient.getInstance().options.getNarrator().getValue();
          type.narration().ifPresent((narrationRule) -> {
            if (narratorMode.shouldNarrate(narrationRule.kind())) {
              Text text2 = narrationRule.apply(message, sender);
              String string = text2.getString();
              NarratorPlus.narrate(string);
              ci.cancel();
            }

          });
        }
      }
      case "off" -> {
        ci.cancel();
      }
      case "default" -> {
      }
    }
  }
}
