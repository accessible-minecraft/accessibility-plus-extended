package net.shoaibkhan.accessibiltyplusextended.basemod.gui.widgets;

import io.github.cottonmc.cotton.gui.widget.WButton;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class APButton extends WButton {
    private static APButton hoveredButton;
    private static APButton focusedButton;
    private String hiddenText = "";

    public APButton(Text label) {
        super(label);
    }

    public void setHiddenText(String text) {
        this.hiddenText = text;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        super.paint(matrices, x, y, mouseX, mouseY);
        // if (hoveredButton == this) {
        // return;
        // }
        // boolean hovered = (mouseX >= 0 && mouseY >= 0 && mouseX < getWidth() &&
        // mouseY < getHeight());
        // if (hovered) {
        // hoveredButton = this;
        // NarratorPlus.narrate(this.getLabel().getString());
        // } else if (this.isFocused()) {
        // hoveredButton = this;
        // NarratorPlus.narrate(this.getLabel().getString());
        // }

        // NarratorPlus narrator = NarratorPlus.getInstance();
        // String text = this.hiddenText != "" ? this.hiddenText :
        // this.getLabel().getString();
        // if (hovered) {
        // hoveredButton = this;
        // if (!text.equals(narrator.lastText)) {
        // narrator.lastText = text;
        // NarratorPlus.narrate(text);
        // }
        // } else if (!hovered && hoveredButton == this) {
        // hoveredButton = null;
        // narrator.lastText = "";
        // }
    }
}
