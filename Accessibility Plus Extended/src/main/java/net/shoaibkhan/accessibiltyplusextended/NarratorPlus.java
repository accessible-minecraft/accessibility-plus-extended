package net.shoaibkhan.accessibiltyplusextended;

import com.sun.jna.Library;
import com.sun.jna.Native;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class NarratorPlus {
  public static String chatOptions[] = {"on","off","default"};

  private interface NVDA extends Library {
    public int nvdaController_speakText(char[] text);

    public int nvdaController_cancelSpeech();
  }

  public String lastText = "";
  public String prefixAmount = "";
  public Block lastBlock = null;
  public BlockPos lastBlockPos = null;
  private static NarratorPlus instance;
  public static String previousToolTip = "";
  private NVDA nvda;

  public NarratorPlus() {
    instance = this;
    String osName = System.getProperty("os.name").toLowerCase(java.util.Locale.ROOT);
    if (osName.contains("win")) {
      try {
        this.nvda = (NVDA) Native.loadLibrary("nvdaControllerClient64", NVDA.class);
      } catch (java.lang.UnsatisfiedLinkError e64) {
        try {
          this.nvda = (NVDA) Native.loadLibrary("nvdaControllerClient32", NVDA.class);
        } catch (java.lang.UnsatisfiedLinkError e32) {
        } finally {
        }
      } finally {
      }
    }
  }

  public static boolean isNVDALoaded() {
    return instance.nvda != null;
  }

  public static void narrate(boolean bool) {
      narrate(I18n.translate("narrate.apextended." + bool));
  }

  public static void narrate(String text) {
    if (instance.nvda != null) {
      instance.nvda.nvdaController_cancelSpeech();
      char[] ch = new char[text.length() + 1]; // Last character must be null so NVDA decodes the text correctly
      for (int i = 0; i < text.length(); i++) {
        ch[i] = text.charAt(i);
      }

      instance.nvda.nvdaController_speakText(ch);
    } else {
      NarratorManager.INSTANCE.narrate(text);
    }
  }

  public static NarratorPlus getInstance() {
    return instance;
  }
}
