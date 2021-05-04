package net.shoaibkhan.accessibiltyplusextended.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.LiteralText;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.ELConfig;

public class ConfigGui extends LightweightGuiDescription {
    private ClientPlayerEntity player;

    public ConfigGui(ClientPlayerEntity player,MinecraftClient client) {
        this.player = player;
        WGridPanel root = new WGridPanel();

        setRootPanel(root);

        ConfigButton rcStatus = new ConfigButton("Read crosshair", ELConfig.getReadcrosshairkey());
        root.add(rcStatus,1, 2, 10 ,1);
        
        ConfigButton fdStatus = new ConfigButton("Fall Detector", ELConfig.getFalldetectorkey());
        root.add(fdStatus,12, 2, 10 ,1);
        
        ConfigButton dcStatus = new ConfigButton("Durability Checker", ELConfig.getDurabilitycheckerkey());
        root.add(dcStatus,1, 4, 10 ,1);

        WButton doneButton = new WButton(new LiteralText("Done"));
        doneButton.setOnClick(this::onDoneClick);
        root.add(doneButton, 7, 10, 7, 1);

        WLabel label = new WLabel(new LiteralText("Accessibility Plus Extended"), modInit.colors("red",100));
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(label, 0, 0, 21, 1);
        
        root.validate(this);
}

    private void onDoneClick() {
        this.player.closeScreen();
    }

    // private void hbClick(){
    //     this.player.closeScreen();
    //     this.client.openScreen(new ConfigScreen(new HBConfigGui(client.player,client)));
    // }

    @Override
    public void addPainters() {
        this.rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(modInit.colors("lightgrey",50)));
    }

}