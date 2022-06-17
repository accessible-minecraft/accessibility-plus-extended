package net.shoaibkhan.accessibiltyplusextended.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.features.withThreads.FallDetectorThread;

public class FallDetectorGui extends LightweightGuiDescription {
  private ClientPlayerEntity player;
  private MinecraftClient client;

  public FallDetectorGui(ClientPlayerEntity player, MinecraftClient client) {
    this.player = player;
    this.client = client;
    WGridPanel root = new WGridPanel();
    setRootPanel(root);

    ArrayButton fdrButton = new ArrayButton("gui.apextended.config.buttons.range", ConfigKeys.FALL_DETECTOR_RANGE_KEY.getKey(), FallDetectorThread.range);
    root.add(fdrButton, 1, 3, 10, 1);

    ArrayButton fdcButton = new ArrayButton("gui.apextended.config.buttons.depth", ConfigKeys.FALL_DETECTOR_DEPTH.getKey(), FallDetectorThread.depthArray);
    root.add(fdcButton, 12, 3, 10, 1);

    WButton backButton = new WButton(Text.of(I18n.translate("gui.apextended.config.buttons.back")));
    backButton.setOnClick(this::onBackClick);
    root.add(backButton, 2, 5, 7, 1);

    WButton doneButton = new WButton(Text.of(I18n.translate("gui.apextended.config.buttons.done")));
    doneButton.setOnClick(this::onDoneClick);
    root.add(doneButton, 12, 5, 7, 1);

    WLabel label = new WLabel(Text.of(I18n.translate("gui.apextended.config.buttons.falldetectorsettings")), modInit.colors("red", 100));
    label.setHorizontalAlignment(HorizontalAlignment.CENTER);
    root.add(label, 0, 1, 21, 1);
    WLabel fakeLabel = new WLabel(Text.of(""), modInit.colors("red", 100));
    fakeLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
    root.add(fakeLabel, 0, 6, 21, 1);

    root.validate(this);
  }

  private void onDoneClick() {
    this.player.closeScreen();
  }

  private void onBackClick() {
    this.player.closeScreen();
    this.client.setScreen(new ConfigScreen(new SettingsGui(player, client), "buttons.settings"));
//    this.client.openScreen(new ConfigScreen(new SettingsGui(player, client), "buttons.settings"));
  }

  @Override
  public void addPainters() {
    this.rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(modInit.colors("lightgrey", 50)));
  }
}
