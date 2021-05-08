package net.shoaibkhan.accessibiltyplusextended.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

@Mixin(HandledScreen.class)
public interface AccessorHandledScreen {
    //
    @Accessor(value = "playerInventoryTitleX")
    int getPlayerInventoryTitleX();

    @Accessor(value = "playerInventoryTitleY")
    int getPlayerInventoryTitleY();

    @Accessor(value = "x")
    int getX();

    @Accessor(value = "y")
    int getY();

    @Accessor(value = "handler")
    ScreenHandler getHandler();

    @Accessor(value = "focusedSlot")
    Slot getFocusedSlot();

    @Accessor(value = "focusedSlot")
    public void setFocusedSlot(Slot slot);
}
