package net.shoaibkhan.accessibiltyplusextended.config;

import java.io.File;
import java.io.FileWriter; // Import the FileWriter class
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner; // Import the Scanner class to read text files

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class ELConfig {
    private static JsonObject data;
    private static String CONFIG_PATH = Paths.get("config", "apextended", "config.json").toString();
    public static final String FallDetectorKey = "fall_detector_key";
    public static final String ReadCrosshairKey = "read_crosshair_key";
    public static final String DurabilityCheckKey = "durability_check_key";
    public static final String EntityNarratorKey = "entity_narrator_key";

    public static String getEntitynarratorkey() {
		return EntityNarratorKey;
	}


	public static String getDurabilitycheckerkey() {
		return DurabilityCheckKey;
	}


	public static String getFalldetectorkey() {
		return FallDetectorKey;
	}


	public static String getReadcrosshairkey() {
		return ReadCrosshairKey;
	}


	public ELConfig() {
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
        data.add(getReadcrosshairkey(), new JsonPrimitive(false));
        data.add(getDurabilitycheckerkey(), new JsonPrimitive(true));
        data.add(getEntitynarratorkey(), new JsonPrimitive(true));
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