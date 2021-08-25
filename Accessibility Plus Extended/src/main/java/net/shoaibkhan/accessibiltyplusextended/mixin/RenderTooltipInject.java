package net.shoaibkhan.accessibiltyplusextended.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.keyboard.KeyboardController;

@Mixin(Screen.class)
public class RenderTooltipInject {

    @Inject(at = @At("HEAD"), method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;II)V")
    private void readOneLineTooltips(MatrixStack matrices, Text text, int x, int y, CallbackInfo callback) {
        if (!Config.get(Config.getReadTooltipsKey()) || KeyboardController.hasControlOverMouse()) {
            return;
        }
        String nextText = text.getString();
        if (!NarratorPlus.getInstance().lastText.equals(nextText)) {
            NarratorPlus.getInstance().lastText = nextText;
            NarratorPlus.narrate(nextText);
        }
    }

    @Inject(at = @At("HEAD"), method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V")
    private void renderTooltip(MatrixStack matrices, List<Text> lines, int x, int y, CallbackInfo callback) {
        if (!Config.get(Config.getReadTooltipsKey()) || KeyboardController.hasControlOverMouse()) {
            return;
        }
        if (lines.size() > 0) {
            String nextText = NarratorPlus.getInstance().prefixAmount;
            for (int i = 0; i < lines.size(); i++) {
                nextText += lines.get(i).getString() + ", ";
            }
            if (!NarratorPlus.getInstance().lastText.equals(nextText)) {
                NarratorPlus.getInstance().lastText = nextText;
                NarratorPlus.narrate(nextText);
            }
        }
    }
}