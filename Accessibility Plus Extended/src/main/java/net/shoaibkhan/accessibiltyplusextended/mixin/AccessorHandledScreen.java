package net.shoaibkhan.accessibiltyplusextended.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

@Mixin(HandledScreen.class)
public interface AccessorHandledScreen {
    //
    @Accessor("playerInventoryTitleX")
    int getPlayerInventoryTitleX();

    @Accessor("playerInventoryTitleY")
    int getPlayerInventoryTitleY();

    @Accessor("x")
    int getX();

    @Accessor("y")
    int getY();

    @Accessor("handler")
    ScreenHandler getHandler();

    @Accessor("focusedSlot")
    Slot getFocusedSlot();

    @Accessor("focusedSlot")
    public void setFocusedSlot(Slot slot);
}
