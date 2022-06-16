package net.shoaibkhan.accessibiltyplusextended;

import net.minecraft.client.MinecraftClient;
import net.shoaibkhan.accessibiltyplusextended.features.withThreads.FluidDetectorThread;

public class customCommands {
  MinecraftClient client;

  public customCommands() {
    // pre 1.19
    /*
      ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("getxp").executes(source -> {
      client = MinecraftClient.getInstance();
      NarratorPlus.narrate(""+client.player.experienceLevel);
      return 1;
    }));

      ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("isfullscreen").executes(source -> {
      client = MinecraftClient.getInstance();
      NarratorPlus.narrate(client.options.fullscreen);
      return 1;
    }));

      ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("findlava").executes(source -> {
      try {
        FluidDetectorThread fluidDetectorThread = new FluidDetectorThread(true, false);
        fluidDetectorThread.start();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return 1;
    }));

      ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("findwater").executes(source -> {
      try {
        FluidDetectorThread fluidDetectorThread = new FluidDetectorThread(false, true);
        fluidDetectorThread.start();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return 1;
    }));
     */

    // post 1.19
    net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

      dispatcher.register(net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal("getxp").executes(source -> {
        client = MinecraftClient.getInstance();
        NarratorPlus.narrate("" + client.player.experienceLevel);
        return 1;
      }));

      dispatcher.register(net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal("isfullscreen").executes(source -> {
        client = MinecraftClient.getInstance();
        NarratorPlus.narrate(String.valueOf(client.options.getFullscreen()));
        return 1;
      }));

      dispatcher.register(net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal("findlava").executes(source -> {
        try {
          FluidDetectorThread fluidDetectorThread = new FluidDetectorThread(true, false);
          fluidDetectorThread.start();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return 1;
      }));

      dispatcher.register(net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal("findwater").executes(source -> {
        try {
          FluidDetectorThread fluidDetectorThread = new FluidDetectorThread(false, true);
          fluidDetectorThread.start();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return 1;
      }));
    });
  }

}
