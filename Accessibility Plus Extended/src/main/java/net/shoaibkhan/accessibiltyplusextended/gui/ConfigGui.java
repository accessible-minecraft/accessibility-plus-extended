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
import net.shoaibkhan.accessibiltyplusextended.config.Config;

public class ConfigGui extends LightweightGuiDescription {
  private ClientPlayerEntity player;
  private MinecraftClient client;

  public ConfigGui(ClientPlayerEntity player, MinecraftClient client) {
    this.player = player;
    this.client = client;
    WGridPanel root = new WGridPanel();

    setRootPanel(root);

    ConfigButton rcStatus = new ConfigButton("Read Blocks", Config.getReadblockskey());
    root.add(rcStatus, 1, 3, 10, 1);

    ConfigButton enStatus = new ConfigButton("Read Entity", Config.getEntitynarratorkey());
    root.add(enStatus, 12, 3, 10, 1);

    ConfigButton fdStatus = new ConfigButton("Fall Detector", Config.getFalldetectorkey());
    root.add(fdStatus, 1, 5, 10, 1);

    ConfigButton odStatus = new ConfigButton("Ore Detector", Config.getOredetectorkey());
    root.add(odStatus, 12, 5, 10, 1);

    ConfigButton ldStatus = new ConfigButton("Lava Detector", Config.getLavadetectorkey());
    root.add(ldStatus, 1, 7, 10, 1);

    ConfigButton wdStatus = new ConfigButton("Water Detector", Config.getWaterdetectorkey());
    root.add(wdStatus, 12, 7, 10, 1);

    ConfigButton dcStatus = new ConfigButton("ToolTip Durability", Config.getDurabilitycheckerkey());
    root.add(dcStatus, 7, 9, 10, 1);

    WButton settingsButton = new WButton(new LiteralText("Settings"));
    settingsButton.setOnClick(this::onSettingsClick);
    root.add(settingsButton, 2, 11, 7, 1);

    WButton doneButton = new WButton(new LiteralText("Done"));
    doneButton.setOnClick(this::onDoneClick);
    root.add(doneButton, 12, 11, 7, 1);

    WLabel label = new WLabel(new LiteralText("Accessibility Plus Extended"), modInit.colors("red", 100));
    label.setHorizontalAlignment(HorizontalAlignment.CENTER);
    root.add(label, 0, 1, 21, 1);
    WLabel fakeLabel = new WLabel(new LiteralText(""), modInit.colors("red", 100));
    fakeLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
    root.add(fakeLabel, 0, 12, 21, 1);

    root.validate(this);
  }

  private void onDoneClick() {
    this.player.closeScreen();
  }

  private void onSettingsClick() {
    this.player.closeScreen();
    this.client.openScreen(new ConfigScreen(new SettingsGui(client.player, client), "Settings", player));
  }

  // private void hbClick(){
  // this.player.closeScreen();
  // this.client.openScreen(new ConfigScreen(new
  // HBConfigGui(client.player,client)));
  // }

  @Override
  public void addPainters() {
    this.rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(modInit.colors("lightgrey", 50)));
  }

}
