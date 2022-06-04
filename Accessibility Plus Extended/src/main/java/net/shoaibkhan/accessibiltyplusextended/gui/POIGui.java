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
import net.shoaibkhan.accessibiltyplusextended.modInit;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;
import net.shoaibkhan.accessibiltyplusextended.features.POIHandler;

public class POIGui extends LightweightGuiDescription {
  private ClientPlayerEntity player;
  private MinecraftClient client;

  public POIGui(ClientPlayerEntity player, MinecraftClient client) {
    this.player = player;
    this.client = client;
    WGridPanel root = new WGridPanel();
    setRootPanel(root);

    ConfigButton poiELKey = new ConfigButton("gui.apextended.config.buttons.poientitykey", ConfigKeys.POI_ENTITY_KEY.getKey());
    root.add(poiELKey, 1, 3, 10, 1);

    ConfigButton poiELNarrateDistance = new ConfigButton("gui.apextended.config.buttons.poienarratedistance", ConfigKeys.POI_ENTITY_LOCKING_NARRATE_DISTANCE_KEY.getKey());
    root.add(poiELNarrateDistance, 12, 3, 10, 1);

    ConfigButton poiBLKey = new ConfigButton("gui.apextended.config.buttons.poiblockskey", ConfigKeys.POI_BLOCK_KEY.getKey());
    root.add(poiBLKey, 1, 5, 10, 1);

    ConfigButton poiBLNarrateDIstance = new ConfigButton("gui.apextended.config.buttons.poibnarratedistance", ConfigKeys.POI_BLOCKS_LOCKING_NARRATE_DISTANCE_KEY.getKey());
    root.add(poiBLNarrateDIstance, 12, 5, 10, 1);

    ArrayButton poiVolume = new ArrayButton("gui.apextended.config.buttons.volume", ConfigKeys.POI_VOLUME.getKey(), POIHandler.volume);
    root.add(poiVolume, 1, 7, 10, 1);

    ArrayButton poiRange = new ArrayButton("gui.apextended.config.buttons.range", ConfigKeys.POI_RANGE.getKey(), POIHandler.range);
    root.add(poiRange, 12, 7, 10, 1);

    ArrayButton poiDelay = new ArrayButton("gui.apextended.config.buttons.delay", ConfigKeys.POI_DELAY.getKey(), POIHandler.delay);
    root.add(poiDelay, 1, 9, 10, 1);

    ConfigButton poiUnlockingSound = new ConfigButton("gui.apextended.config.buttons.poiunlockingsound", ConfigKeys.POI_UNLOCKING_SOUND_KEY.getKey());
    root.add(poiUnlockingSound, 1, 11, 10, 1);

    ArrayButton poiUnlockingSoundVolume = new ArrayButton("gui.apextended.config.buttons.poiunlockingsoundvolume", ConfigKeys.POI_UNLOCKING_SOUND_VOLUME.getKey(), POIHandler.volume);
    root.add(poiUnlockingSoundVolume, 12, 10, 11, 1);

    ConfigButton poiFluidDetector = new ConfigButton("gui.apextended.config.buttons.poifluiddetector", ConfigKeys.POI_FLUID_DETECTOR_Key.getKey());
    root.add(poiFluidDetector, 1, 13, 10, 1);

    ArrayButton poiFluidDetectorDelay = new ArrayButton("gui.apextended.config.buttons.poifluiddetectordelay", ConfigKeys.POI_FLUID_DETECTOR_DELAY.getKey(), POIHandler.delay);
    root.add(poiFluidDetectorDelay, 12, 13, 10, 1);

    WButton back = new WButton(new TranslatableText("gui.apextended.config.buttons.back"));
    back.setOnClick(this::onBackClick);
    root.add(back, 2, 15, 7, 1);

    WButton done = new WButton(new TranslatableText("gui.apextended.config.buttons.done"));
    done.setOnClick(this::onDoneClick);
    root.add(done, 12, 15, 7, 1);

    WLabel label = new WLabel(new TranslatableText("gui.apextended.config.buttons.poisettings"), modInit.colors("red", 100));
    label.setHorizontalAlignment(HorizontalAlignment.CENTER);
    root.add(label, 0, 1, 21, 1);

    WLabel fakeLabel = new WLabel(LiteralText.EMPTY, modInit.colors("red", 100));
    fakeLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
    root.add(fakeLabel, 0, 16, 21, 1);

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
