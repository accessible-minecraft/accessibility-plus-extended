package net.shoaibkhan.accessibiltyplusextended.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.util.NarratorManager;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

@Mixin(value = NarratorManager.class, priority = 0)
public class ChatNarrationMixin {

	@Inject(at = @At("HEAD"), method = "onChatMessage", cancellable = true)
	public void onChatMessage(MessageType messageType, Text message, UUID sender, CallbackInfo info) {
		if (!Config.get(Config.getChatnarration())) {
			info.cancel();
		}
	}
}
