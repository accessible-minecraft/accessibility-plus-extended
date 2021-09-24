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


	public Config() {
	}

	public static boolean get(String key) {
		if (data == null) {
			loadConfig();
		}
		boolean val;
		try {
			val = data.get(key).getAsBoolean();
		} catch (Exception e) {
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
		int val = 100;
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

	public static void setString(String key, String value) {
		data.addProperty(key, value);
		saveConfig(data);
	}

	public static boolean setInt(String key, String value) {
		try {
			Integer.parseInt(value);
			data.addProperty(key, value);
			saveConfig(data);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean setDouble(String key, String value) {
		try {
			Double.parseDouble(value);
			data.addProperty(key, value);
			saveConfig(data);
			return true;
		} catch (Exception e) {
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

	private static JsonObject resetData() {
		data = new JsonObject();
		data.add(ConfigKeys.FALL_DETECTOR_KEY.getKey(), new JsonPrimitive(false));
		data.add(ConfigKeys.FALL_DETECTOR_RANGE_KEY.getKey(), new JsonPrimitive("5"));
		data.add(ConfigKeys.FALL_DETECTOR_DEPTH.getKey(), new JsonPrimitive("2"));

		data.add(ConfigKeys.NARRATE_BLOCK_SIDE_KEY.getKey(), new JsonPrimitive(false));

		data.add(ConfigKeys.DURABILITY_CHECK_KEY.getKey(), new JsonPrimitive(true));
		data.add(ConfigKeys.DURABILITY_TOOL_TIP_KEY.getKey(), new JsonPrimitive(true));
		data.add(ConfigKeys.DURABILITY_THRESHOLD_KEY.getKey(), new JsonPrimitive("4"));

		data.add(ConfigKeys.ENTITY_NARRATOR_KEY.getKey(), new JsonPrimitive(true));

		data.add(ConfigKeys.FIND_FLUID_TEXT_KEY.getKey(), new JsonPrimitive(true));
		data.add(ConfigKeys.FIND_FLUID_VOLUME.getKey(), new JsonPrimitive("5"));
		data.add(ConfigKeys.FIND_FLUID_PITCH.getKey(), new JsonPrimitive("5"));
		data.add(ConfigKeys.FIND_FLUID_RANGE.getKey(), new JsonPrimitive("5"));

		data.add(ConfigKeys.CHAT_NARRATION.getKey(), new JsonPrimitive("0"));

		// Point of Interest
		data.add(ConfigKeys.POI_KEY.getKey(), new JsonPrimitive(true));
		data.add(ConfigKeys.POI_ENTITY_LOCKING_NARRATE_DISTANCE_KEY.getKey(), new JsonPrimitive(false));
		data.add(ConfigKeys.POI_BLOCKS_LOCKING_NARRATE_DISTANCE_KEY.getKey(), new JsonPrimitive(false));
		data.add(ConfigKeys.POI_VOLUME.getKey(), new JsonPrimitive("5"));
		data.add(ConfigKeys.POI_RANGE.getKey(), new JsonPrimitive("1"));
		data.add(ConfigKeys.POI_DELAY.getKey(), new JsonPrimitive("1"));

		// Accessibility Plus
		data.add(ConfigKeys.READ_BLOCKS_KEY.getKey(), new JsonPrimitive(true));
		data.add(ConfigKeys.READ_TOOLTIPS_KEY.getKey(), new JsonPrimitive(true));
		data.add(ConfigKeys.READ_SIGNS_CONTENTS.getKey(), new JsonPrimitive(true));
		data.add(ConfigKeys.INV_KEYBOARD_CONTROL_KEY.getKey(), new JsonPrimitive(true));
		data.add(ConfigKeys.ATION_BAR_KEY.getKey(), new JsonPrimitive(true));

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