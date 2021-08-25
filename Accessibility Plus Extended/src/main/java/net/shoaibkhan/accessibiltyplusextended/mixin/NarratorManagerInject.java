package net.shoaibkhan.accessibiltyplusextended.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.util.NarratorManager;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;

@Mixin(NarratorManager.class)
public class NarratorManagerInject {

  @Inject(at = @At("HEAD"), method = "narrate(Ljava/lang/String;)V", cancellable = true)
  public void sayWithNVDA(String message, CallbackInfo ci) {
    if (NarratorPlus.isNVDALoaded()) {
      NarratorPlus.narrate(message);
      ci.cancel();
    }
  }
}
