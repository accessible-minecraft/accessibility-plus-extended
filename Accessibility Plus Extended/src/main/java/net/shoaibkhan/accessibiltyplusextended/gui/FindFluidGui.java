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
import net.shoaibkhan.accessibiltyplusextended.features.withThreads.FluidDetectorThread;

public class FindFluidGui extends LightweightGuiDescription {
  private ClientPlayerEntity player;
  private MinecraftClient client;

  public FindFluidGui(ClientPlayerEntity player, MinecraftClient client) {
    this.player = player;
    this.client = client;
    WGridPanel root = new WGridPanel();
    setRootPanel(root);

    ConfigButton fftStatus = new ConfigButton("gui.apextended.config.buttons.text", ConfigKeys.FIND_FLUID_TEXT_KEY.getKey());
    root.add(fftStatus, 1, 3, 10, 1);

    ArrayButton ffrButton = new ArrayButton("gui.apextended.config.buttons.range", ConfigKeys.FIND_FLUID_RANGE.getKey(), FluidDetectorThread.range);
    root.add(ffrButton, 12, 3, 10, 1);

    ArrayButton ffvButton = new ArrayButton("gui.apextended.config.buttons.volume", ConfigKeys.FIND_FLUID_VOLUME.getKey(), FluidDetectorThread.volume);
    root.add(ffvButton, 1, 5, 10, 1);

    ArrayButton ffpButton = new ArrayButton("gui.apextended.config.buttons.pitch", ConfigKeys.FIND_FLUID_PITCH.getKey(), FluidDetectorThread.pitch);
    root.add(ffpButton, 12, 5, 10, 1);

    WButton backButton = new WButton(new TranslatableText("gui.apextended.config.buttons.back"));
    backButton.setOnClick(this::onBackClick);
    root.add(backButton, 2, 9, 7, 1);

    WButton doneButton = new WButton(new TranslatableText("gui.apextended.config.buttons.done"));
    doneButton.setOnClick(this::onDoneClick);
    root.add(doneButton, 12, 9, 7, 1);

    WLabel label = new WLabel(new TranslatableText("gui.apextended.config.buttons.finddluidsettings"), modInit.colors("red", 100));
    label.setHorizontalAlignment(HorizontalAlignment.CENTER);
    root.add(label, 0, 1, 21, 1);
    WLabel fakeLabel = new WLabel(LiteralText.EMPTY, modInit.colors("red", 100));
    fakeLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
    root.add(fakeLabel, 0, 10, 21, 1);

    root.validate(this);
  }

  private void onDoneClick() {
    this.player.closeScreen();
  }

  private void onBackClick() {
    this.player.closeScreen();
    this.client.setScreen(new ConfigScreen(new SettingsGui(player, client), "buttons.settings"));
//    this.client.openScreen(new ConfigScreen(new SettingsGui(player, client), "settings", player));
  }

  @Override
  public void addPainters() {
    this.rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(modInit.colors("lightgrey", 50)));
  }
}
