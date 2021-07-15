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

public class SettingsGui extends LightweightGuiDescription {
  private ClientPlayerEntity player;
  private MinecraftClient client;

  public SettingsGui(ClientPlayerEntity player, MinecraftClient client) {
    this.player = player;
    this.client = client;
    WGridPanel root = new WGridPanel();
    setRootPanel(root);

    WButton oreDetectorButton = new WButton(new LiteralText("Ore Detector Settings"));
    oreDetectorButton.setOnClick(this::onOreDetectorClick);
    root.add(oreDetectorButton, 1, 3, 7, 1);

    WButton fallDetectorButton = new WButton(new LiteralText("Fall Detector Settings"));
    fallDetectorButton.setOnClick(this::onFallDetectorClick);
    root.add(fallDetectorButton, 12, 3, 7, 1);

    WButton backButton = new WButton(new LiteralText("Back"));
    backButton.setOnClick(this::onBackClick);
    root.add(backButton, 2, 5, 7, 1);

    WButton doneButton = new WButton(new LiteralText("Done"));
    doneButton.setOnClick(this::onDoneClick);
    root.add(doneButton, 12, 5, 7, 1);

    WLabel label = new WLabel(new LiteralText("Settings"), modInit.colors("red", 100));
    label.setHorizontalAlignment(HorizontalAlignment.CENTER);
    root.add(label, 0, 1, 21, 1);
    WLabel fakeLabel = new WLabel(new LiteralText(""), modInit.colors("red", 100));
    fakeLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
    root.add(fakeLabel, 0, 6, 21, 1);

    root.validate(this);
  }

  private void onDoneClick() {
    this.player.closeScreen();
  }

  private void onBackClick() {
    this.player.closeScreen();
    this.client.openScreen(new ConfigScreen(new ConfigGui(client.player, client), "AP Extended Configuration", player));
  }

  private void onOreDetectorClick() {
    this.player.closeScreen();
    this.client.openScreen(new ConfigScreen(new OreDetectorGui(player, client), "AP Extended Configuration", player));
  }

  private void onFallDetectorClick() {
    this.player.closeScreen();
    this.client.openScreen(new ConfigScreen(new ConfigGui(player, client), "AP Extended Configuration", player));
  }

  @Override
  public void addPainters() {
    this.rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(modInit.colors("lightgrey", 50)));
  }
}
