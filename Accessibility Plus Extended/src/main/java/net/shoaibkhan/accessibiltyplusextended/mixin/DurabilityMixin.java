package net.shoaibkhan.accessibiltyplusextended.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.shoaibkhan.accessibiltyplusextended.config.ELConfig;

@Mixin(ItemStack.class)
public abstract class DurabilityMixin {
	@Inject(at = @At("RETURN"), method = "getTooltip")
	private void getTooltipMixin(PlayerEntity player, TooltipContext context,CallbackInfoReturnable<List<Text>> info) {
		if (ELConfig.get(ELConfig.getDurabilitycheckerkey())) {
			ItemStack itemStack = (ItemStack) ((Object) this);
			List<Text> list = info.getReturnValue();
			try {
				if (itemStack.isDamageable()) {
					int totalDurability = itemStack.getMaxDamage();
					int currrRemainingDurability = totalDurability - (itemStack.getDamage());
					list.add(1, (new LiteralText("Durability: " + currrRemainingDurability + " of " + totalDurability)
							.formatted(Formatting.DARK_GRAY)));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
}
