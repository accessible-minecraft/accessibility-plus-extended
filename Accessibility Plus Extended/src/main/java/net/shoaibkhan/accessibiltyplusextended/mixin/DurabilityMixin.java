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
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

@Mixin(value=ItemStack.class,priority = 0)
public class DurabilityMixin {
	@Inject(at = @At("RETURN"), method = "getTooltip")
	private void getTooltipMixin(PlayerEntity player, TooltipContext context,CallbackInfoReturnable<List<Text>> info) throws Exception {
		if(MinecraftClient.getInstance().world == null) return;
		List<Text> list = info.getReturnValue();
		ItemStack itemStack = (ItemStack) ((Object) this);
		
//		if(HudRenderCallBackClass.isTradeScreenOpen) {
//			MutableText mutableText = new LiteralText("").append(list.get(0));
//			list.set(0, new LiteralText(itemStack.getCount() + " " + mutableText.getString()) );
//		}
		
		if (Config.get(Config.getDurabilitycheckerkey())) {
			if (itemStack.getItem().isDamageable()) {
				int totalDurability = itemStack.getItem().getMaxDamage();
				int currrRemainingDurability = totalDurability - (itemStack.getDamage());
				list.add(1, (new LiteralText("Durability: " + currrRemainingDurability + " of " + totalDurability)
						.formatted(Formatting.GREEN)));
			}
		}
	}
}
