package net.shoaibkhan.accessibiltyplusextended.basemod.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.shoaibkhan.accessibiltyplusextended.basemod.config.Config;
import net.shoaibkhan.accessibiltyplusextended.basemod.config.SerializableConfig;
import net.shoaibkhan.accessibiltyplusextended.basemod.gui.widgets.ConfigButton;

public class ConfigGui extends LightweightGuiDescription {
    private SerializableConfig tempConfig;
    private ClientPlayerEntity player;

    public ConfigGui(ClientPlayerEntity player) {
        this.player = player;
        WGridPanel root = new WGridPanel();

        setRootPanel(root);
        root.setSize(240, 240);

        ConfigButton readBlocksButton = new ConfigButton("gui.accessibilityplus.config.buttons.readblocks",
                Config.READ_BLOCKS_KEY);
        root.add(readBlocksButton, 0, 1, 10, 1);

        ConfigButton readTooltipsButton = new ConfigButton("gui.accessibilityplus.config.buttons.readtooltips",
                Config.READ_TOOLTIPS_KEY);
        root.add(readTooltipsButton, 11, 1, 10, 1);

        ConfigButton readSignsButton = new ConfigButton("gui.accessibilityplus.config.buttons.readsignscontents",
                Config.READ_SIGNS_CONTENTS);
        root.add(readSignsButton, 0, 2, 10, 1);

        ConfigButton inventoryKeyboardControllButton = new ConfigButton(
                "gui.accessibilityplus.config.buttons.inventorykeyboardcontrol", Config.INV_KEYBOARD_CONTROL_KEY);
        root.add(inventoryKeyboardControllButton, 11, 2, 10, 1);

        ConfigButton actionBarButton = new ConfigButton("gui.accessibilityplus.config.buttons.actionbar",
                Config.ATION_BAR_KEY);
        root.add(actionBarButton, 0, 3, 10, 1);

        WButton doneButton = new WButton(new TranslatableText("gui.accessibilityplus.config.buttons.done"));
        doneButton.setOnClick(this::onDoneClick);
        root.add(doneButton, 7, 8, 7, 1);

        WLabel label = new WLabel(new TranslatableText("gui.accessibilityplus.config.title"), 0xFFFFFF);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(label, 0, 0, 21, 1);

        root.validate(this);
    }

    private void onDoneClick() {
        this.player.closeScreen();

    }

    @Override
    public void addPainters() {
        this.rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(0x242424));
    }

}
