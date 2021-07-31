package net.shoaibkhan.accessibiltyplusextended.config;

import java.io.File;
import java.io.FileWriter; // Import the FileWriter class
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner; // Import the Scanner class to read text files

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Config {
  private static JsonObject data;
  private static String CONFIG_PATH = Paths.get("config", "apextended", "config.json").toString();

  public static final String FallDetectorKey = "fall_detector_key";
  public static final String FallDetectorRange = "fall_detector_range";
  public static final String FallDetectorDepth = "fall_detector_depth";
  public static final String OreDetectorKey = "ore_detector_key";
  public static final String LavaDetectorKey = "lava_detector_key";
  public static final String WaterDetectorKey = "water_detector_key";
  public static final String ReadBlocksKey = "read_blocks_key";
  public static final String DurabilityCheckKey = "durability_check_key";
  public static final String EntityNarratorKey = "entity_narrator_key";
  public static final String OreDetectorCustomSoundKey = "ore_detector_custom_sound_key";
  public static final String OreDetectorVolume = "ore_detector_volume";
  public static final String OreDetectorPitch = "ore_detector_pitch";
  public static final String OreDetectorRange = "ore_detector_range";
  public static final String OreDetectorDelay = "ore_detector_delay";
  public static final String FindFluidTextKey = "find_fluid_text_key";
  public static final String FindFluidVolume  = "find_fluid_volume";
  public static final String FindFluidPitch   = "find_fluid_pitch";
  public static final String FindFluidRange   = "find_fluid_range";
  
  public static String getFalldetectordepth() {
    return FallDetectorDepth;
}

  public static String getFalldetectorrange() {
    return FallDetectorRange;
}

  public static String getOredetectordelay() {
    return OreDetectorDelay;
}

public static String getFindfluidtextkey() {
    return FindFluidTextKey;
}

public static String getFindfluidvolume() {
    return FindFluidVolume;
}

public static String getFindfluidpitch() {
    return FindFluidPitch;
}

public static String getFindfluidrange() {
    return FindFluidRange;
}

  public static String getOredetectorrange() {
    return OreDetectorRange;
  }

  public static String getOredetectorvolume() {
    return OreDetectorVolume;
  }

  public static String getOredetectorpitch() {
    return OreDetectorPitch;
  }

  public static String getLavadetectorkey() {
    return LavaDetectorKey;
  }

  public static String getWaterdetectorkey() {
    return WaterDetectorKey;
  }

  public static String getDurabilitycheckkey() {
    return DurabilityCheckKey;
  }

  public static String getOredetectorcustomsoundkey() {
    return OreDetectorCustomSoundKey;
  }

  public static String getOredetectorkey() {
    return OreDetectorKey;
  }

  public static String getEntitynarratorkey() {
    return EntityNarratorKey;
  }


  public static String getDurabilitycheckerkey() {
    return DurabilityCheckKey;
  }


  public static String getFalldetectorkey() {
    return FallDetectorKey;
  }


  public static String getReadblockskey() {
    return ReadBlocksKey;
  }


  public Config() {
  }


  public static boolean get(String key) {
    if (data == null) {
      loadConfig();
    }
    boolean val;
    try {
      val = data.get(key).getAsBoolean();
    } catch(Exception e) {
      resetData();
      val = data.get(key).getAsBoolean();
    }
    return val;
  }

  public static String getString(String key) {
    if (data == null) {
      loadConfig();
    }
    String val;
    try {
      val = data.get(key).getAsString();
    } catch (Exception e) {
      resetData();
      val = data.get(key).getAsString();
    }
    return val;
  }

  public static Integer getInt(String key) {
    if (data == null) {
      loadConfig();
    }
    String val;
    try {
      val = data.get(key).getAsString();
      return Integer.parseInt(val);
    } catch (Exception e) {
      resetData();
      val = data.get(key).getAsString();
      return Integer.parseInt(val);
    }
  }

  public static int getOpacity(String key) {
    if (data == null) {
      loadConfig();
    }
    String v;
    int val=100;
    try {
      v = data.get(key).getAsString();
    } catch (Exception e) {
      resetData();
      v = data.get(key).getAsString();
    }
    v = v.toLowerCase().trim();
    try {
      val = Integer.parseInt(v);
    } catch (Exception e) {
      val = 100;
    }
    return val;
  }

  public static boolean toggle(String key) {
    boolean newValue = !get(key);
    set(key, newValue);
    return newValue;
  }

  public static void set(String key, boolean value) {
    data.addProperty(key, value);
    saveConfig(data);
  }

  public static void setString(String key, String value){
    data.addProperty(key, value);
    saveConfig(data);
  }

  public static boolean setInt(String key, String value){
    try{
      Integer.parseInt(value);
      data.addProperty(key, value);
      saveConfig(data);
      return true;
    }catch (Exception e) {
      return false;
    }
  }

  public static boolean setDouble(String key, String value){
    try{
      Double.parseDouble(value);
      data.addProperty(key, value);
      saveConfig(data);
      return true;
    }catch (Exception e) {
      return false;
    }
  }

  private static JsonObject loadConfig() {
    File configFile = new File(CONFIG_PATH);
    if (configFile.exists()) {
      String jsonString = "";
      try {
        Scanner configReader = new Scanner(configFile);
        while (configReader.hasNextLine()) {
          jsonString += configReader.nextLine();
        }
        configReader.close();
      } catch (Exception e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
        return resetData();
      }
      data = new Gson().fromJson(jsonString, JsonObject.class);
      return data;
    } else {
      return resetData();
    }
  }

  private static JsonObject resetData(){
    data = new JsonObject();
    data.add(getFalldetectorkey(), new JsonPrimitive(false));
    data.add(getFalldetectorrange(), new JsonPrimitive("5"));
    data.add(getFalldetectordepth(), new JsonPrimitive("2"));

    data.add(getOredetectorkey(), new JsonPrimitive(false));
    data.add(getLavadetectorkey(), new JsonPrimitive(false));
    data.add(getWaterdetectorkey(), new JsonPrimitive(false));

    data.add(getReadblockskey(), new JsonPrimitive(false));

    data.add(getDurabilitycheckerkey(), new JsonPrimitive(true));
    data.add(getEntitynarratorkey(), new JsonPrimitive(true));
    
    data.add(getOredetectorcustomsoundkey(), new JsonPrimitive(false));
    data.add(getOredetectorvolume(), new JsonPrimitive("5"));
    data.add(getOredetectorpitch(), new JsonPrimitive("20"));
    data.add(getOredetectorrange(), new JsonPrimitive("1"));
    data.add(getOredetectordelay(), new JsonPrimitive("10"));

    data.add(getFindfluidtextkey(), new JsonPrimitive(true));
    data.add(getFindfluidvolume(), new JsonPrimitive("5"));
    data.add(getFindfluidpitch(), new JsonPrimitive("20"));
    data.add(getFindfluidrange(), new JsonPrimitive("7"));
    saveConfig(data);
    return data;
  }

  public static void saveConfig(JsonObject newConfig) {
    // Save config to file

    String jsonString = new Gson().toJson(data);
    try {
      File configFile = new File(CONFIG_PATH);
      configFile.getParentFile().mkdirs();
      FileWriter configWriter = new FileWriter(configFile);
      configWriter.write(jsonString);
      configWriter.close();
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    data = newConfig;
  }

}
