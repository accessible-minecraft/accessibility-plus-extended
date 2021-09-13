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
import net.minecraft.text.TranslatableText;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.features.withThreads.OreDetectorThread;

public class OreDetectorGui extends LightweightGuiDescription {
  private ClientPlayerEntity player;
  private MinecraftClient client;

  public OreDetectorGui(ClientPlayerEntity player, MinecraftClient client) {
    this.player = player;
    this.client = client;
    WGridPanel root = new WGridPanel();
    setRootPanel(root);

    ArrayButton odvButton = new ArrayButton("gui.apextended.config.buttons.volume", ConfigKeys.ORE_DETECTOR_VOLUME.getKey(), OreDetectorThread.volume);
    root.add(odvButton, 1, 3, 10, 1);

    ArrayButton odpButton = new ArrayButton("gui.apextended.config.buttons.pitch", ConfigKeys.ORE_DETECTOR_PITCH.getKey(), OreDetectorThread.pitch);
    root.add(odpButton, 12, 3, 10, 1);

    ArrayButton odrButton = new ArrayButton("gui.apextended.config.buttons.range", ConfigKeys.ORE_DETECTOR_RANGE.getKey(), OreDetectorThread.range);
    root.add(odrButton, 12, 5, 10, 1);

    ArrayButton odcButton = new ArrayButton("gui.apextended.config.buttons.delay", ConfigKeys.ORE_DETECTOR_DELAY.getKey(), OreDetectorThread.delay);
    root.add(odcButton, 1, 5, 10, 1);

    WButton backButton = new WButton(new TranslatableText("gui.apextended.config.buttons.back"));
    backButton.setOnClick(this::onBackClick);
    root.add(backButton, 2, 7, 7, 1);

    WButton doneButton = new WButton(new TranslatableText("gui.apextended.config.buttons.done"));
    doneButton.setOnClick(this::onDoneClick);
    root.add(doneButton, 12, 7, 7, 1);

    WLabel label = new WLabel(new LiteralText("Ore Detector Settings"), modInit.colors("red", 100));
    label.setHorizontalAlignment(HorizontalAlignment.CENTER);
    root.add(label, 0, 1, 21, 1);
    WLabel fakeLabel = new WLabel(new LiteralText(""), modInit.colors("red", 100));
    fakeLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
    root.add(fakeLabel, 0, 8, 21, 1);

    root.validate(this);
  }

  private void onDoneClick() {
    this.player.closeScreen();
  }

  private void onBackClick() {
    this.player.closeScreen();
    this.client.openScreen(new ConfigScreen(new SettingsGui(player, client), "settings", player));
  }

  @Override
  public void addPainters() {
    this.rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(modInit.colors("lightgrey", 50)));
  }
}
