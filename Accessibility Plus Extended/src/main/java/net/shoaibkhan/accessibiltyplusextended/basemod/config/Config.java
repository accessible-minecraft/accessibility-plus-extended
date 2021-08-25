package net.shoaibkhan.accessibiltyplusextended.basemod.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.FileWriter; // Import the FileWriter class

public class Config {
    private static SerializableConfig config;
    private static JsonObject data;
    private static String CONFIG_PATH = Paths.get("config", "accessibilityplus", "config.json").toString();

    public static final String READ_BLOCKS_KEY = "read_blocks";
    public static final String READ_TOOLTIPS_KEY = "read_tooltip";
    public static final String READ_SIGNS_CONTENTS = "read_signs_contents";
    public static final String INV_KEYBOARD_CONTROL_KEY = "inventory_keyboard_control";
    public static final String ATION_BAR_KEY = "action_bar_key";

    public Config() {
    }

    public static boolean readBlocksEnabled() {
        return get(READ_BLOCKS_KEY);
    }

    public static boolean readTooltipsEnabled() {
        return get(READ_TOOLTIPS_KEY);
    }

    public static boolean readSignsContentsEnabled() {
        return get(READ_SIGNS_CONTENTS);
    }

    public static boolean inventoryKeyboardControlEnabled() {
        return get(INV_KEYBOARD_CONTROL_KEY);
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

    public static boolean toggle(String key) {
        boolean newValue = !get(key);
        set(key, newValue);
        return newValue;
    }

    public static void set(String key, boolean value) {
        data.addProperty(key, value);
        saveConfig(data);
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
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();

            }
            data = new Gson().fromJson(jsonString, JsonObject.class);
            return data;
        } else {
            return resetData();
        }
    }

    public static JsonObject resetData(){
            data = new JsonObject();
            data.add(READ_BLOCKS_KEY, new JsonPrimitive(true));
            data.add(READ_TOOLTIPS_KEY, new JsonPrimitive(true));
            data.add(READ_SIGNS_CONTENTS, new JsonPrimitive(true));
            data.add(INV_KEYBOARD_CONTROL_KEY, new JsonPrimitive(true));
            data.add(ATION_BAR_KEY, new JsonPrimitive(true));

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
