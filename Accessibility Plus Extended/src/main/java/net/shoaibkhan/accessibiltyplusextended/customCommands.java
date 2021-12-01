package net.shoaibkhan.accessibiltyplusextended;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.shoaibkhan.accessibiltyplusextended.features.withThreads.FluidDetectorThread;

public class customCommands {
  MinecraftClient client;

  public customCommands() {
      ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("getxp").executes(source -> {
      client = MinecraftClient.getInstance();
      NarratorPlus.narrate(""+client.player.experienceLevel);
      return 1;
    }));

      ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("isfullscreen").executes(source -> {
      client = MinecraftClient.getInstance();
      NarratorPlus.narrate(""+client.options.fullscreen);
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
  }

}
