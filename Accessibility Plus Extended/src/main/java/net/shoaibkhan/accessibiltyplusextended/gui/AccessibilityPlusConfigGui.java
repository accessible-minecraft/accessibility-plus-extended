package net.shoaibkhan.accessibiltyplusextended.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;

public class AccessibilityPlusConfigGui extends LightweightGuiDescription {
    private ClientPlayerEntity player;

    public AccessibilityPlusConfigGui(ClientPlayerEntity player) {
        this.player = player;
        WGridPanel root = new WGridPanel();

        setRootPanel(root);
        root.setSize(240, 240);

        ConfigButton readBlocksButton = new ConfigButton("gui.apextended.config.buttons.readblocks", ConfigKeys.READ_BLOCKS_KEY.getKey());
        root.add(readBlocksButton, 0, 1, 10, 1);

        ConfigButton readTooltipsButton = new ConfigButton("gui.apextended.config.buttons.readtooltips", ConfigKeys.READ_TOOLTIPS_KEY.getKey());
        root.add(readTooltipsButton, 11, 1, 10, 1);

        ConfigButton readSignsButton = new ConfigButton("gui.apextended.config.buttons.readsignscontents", ConfigKeys.READ_SIGNS_CONTENTS.getKey());
        root.add(readSignsButton, 0, 2, 10, 1);

        ConfigButton inventoryKeyboardControllButton = new ConfigButton("gui.apextended.config.buttons.inventorykeyboardcontrol", ConfigKeys.INV_KEYBOARD_CONTROL_KEY.getKey());
        root.add(inventoryKeyboardControllButton, 11, 2, 10, 1);

        ConfigButton actionBarButton = new ConfigButton("gui.apextended.config.buttons.actionbar", ConfigKeys.ATION_BAR_KEY.getKey());
        root.add(actionBarButton, 0, 3, 10, 1);

        WButton doneButton = new WButton(new TranslatableText("gui.apextended.config.buttons.done"));
        doneButton.setOnClick(this::onDoneClick);
        root.add(doneButton, 7, 8, 7, 1);

        WLabel label = new WLabel(new TranslatableText("gui.apextended.config.title"), 0xFFFFFF);
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
