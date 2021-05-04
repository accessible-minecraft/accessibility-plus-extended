package net.shoaibkhan.accessibiltyplusextended.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.LiteralText;

public class ConfigScreen extends CottonClientScreen {
    public ConfigScreen(GuiDescription description, String title, ClientPlayerEntity playerEntity) {
        super(description);
        playerEntity.sendMessage(new LiteralText(title), true);
    }
}
