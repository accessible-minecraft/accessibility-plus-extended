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
import net.shoaibkhan.accessibiltyplusextended.OreDetectorThread;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class OreDetectorGui extends LightweightGuiDescription {
    private ClientPlayerEntity player;
    private MinecraftClient client;

    public OreDetectorGui(ClientPlayerEntity player,MinecraftClient client) {
        this.player = player;
        this.client = client;
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        
        ConfigButton odcsStatus = new ConfigButton("Custom Ore Sound", Config.getOredetectorcustomsoundkey());
        root.add(odcsStatus,7, 2, 10 ,1);
        
        ArrayButton odvButton = new ArrayButton("Volume", Config.getOredetectorvolume(), OreDetectorThread.volume);
        root.add(odvButton,1, 4, 10 ,1);
        
        ArrayButton odpButton = new ArrayButton("Pitch", Config.getOredetectorpitch(), OreDetectorThread.pitch);
        root.add(odpButton,12, 4, 10 ,1);
        
        WButton backButton = new WButton(new LiteralText("Back"));
        backButton.setOnClick(this::onBackClick);
        root.add(backButton, 2, 6, 7, 1);
        
        WButton doneButton = new WButton(new LiteralText("Done"));
        doneButton.setOnClick(this::onDoneClick);
        root.add(doneButton, 12, 6, 7, 1);
        
        WLabel label = new WLabel(new LiteralText("Ore Detector Settings"), modInit.colors("red",100));
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(label, 0, 0, 21, 1);
        
        root.validate(this);
    }
    
    private void onDoneClick() {
        this.player.closeScreen();
    }

    private void onBackClick() {
        this.player.closeScreen();
        this.client.openScreen(new ConfigScreen(new SettingsGui(player,client), "settings", player));
    }

    @Override
    public void addPainters() {
        this.rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(modInit.colors("lightgrey",50)));
    }
}
