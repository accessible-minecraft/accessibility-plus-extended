package net.shoaibkhan.accessibiltyplusextended.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.resource.language.I18n;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;

public class ConfigScreen extends CottonClientScreen {
    public ConfigScreen(GuiDescription description, String titleKey) {
        super(description);
        NarratorPlus.narrate(I18n.translate("gui.apextended.config." + titleKey));
    }
}
