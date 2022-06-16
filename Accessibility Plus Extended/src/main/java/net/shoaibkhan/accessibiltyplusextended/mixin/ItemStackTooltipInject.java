package net.shoaibkhan.accessibiltyplusextended.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.keyboard.KeyboardController;

@Mixin(value = ItemStack.class, priority = 0)
public class ItemStackTooltipInject {
  @Inject(at = @At("RETURN"), method = "getTooltip")
  private void getTooltipMixin(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info)
      throws Exception {
    if (MinecraftClient.getInstance().world == null)
      return;
    ItemStack itemStack = (ItemStack) ((Object) this);
    List<Text> list = info.getReturnValue();

    addCount: {
      if (!itemStack.isStackable() || itemStack.getItem().isDamageable())
        break addCount;

      MutableText mutableText = Text.empty().append(list.get(0));
      list.set(0, Text.of(itemStack.getCount() + " " + mutableText.getString()));
    }

    narrateToolTip: {
      if (KeyboardController.hasControlOverMouse())
        break narrateToolTip;
      String message = "";

      for (Text text : list) {
        message += text.getString() + ", ";
      }

      if (!NarratorPlus.previousToolTip.equals(message)) {
        NarratorPlus.narrate(message);
        NarratorPlus.previousToolTip = message;
      }
    }

  }
}
