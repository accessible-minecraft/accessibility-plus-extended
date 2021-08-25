package net.shoaibkhan.accessibiltyplusextended.basemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import net.shoaibkhan.accessibiltyplusextended.basemod.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.basemod.config.Config;

@Mixin(InGameHud.class)
public class ActionbarInject {
    @Inject(at = @At("HEAD"), method = "setOverlayMessage(Lnet/minecraft/text/Text;Z)V")
    public void speakActionbar(Text message, boolean tinted, CallbackInfo ci) {
        if(Config.get(Config.ATION_BAR_KEY)){
            NarratorPlus.narrate(message.getString());
        }
    }
}
