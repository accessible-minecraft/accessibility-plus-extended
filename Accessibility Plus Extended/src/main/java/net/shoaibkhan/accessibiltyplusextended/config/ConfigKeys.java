package net.shoaibkhan.accessibiltyplusextended.config;

public enum ConfigKeys {

    // Fall Detector Key
    FALL_DETECTOR_KEY("fall_detector_key"),
    FALL_DETECTOR_RANGE_KEY("fall_detector_range"),
    FALL_DETECTOR_DEPTH("fall_detector_depth"),

    // Misc
    NARRATE_BLOCK_SIDE_KEY("narrate_blocks_side_key"),

    // Durability Checker Key
    DURABILITY_CHECK_KEY("durability_check_key"),
    DURABILITY_TOOL_TIP_KEY("durability_tool_tip_key"),
    DURABILITY_THRESHOLD_KEY("durability_threshold_key"),

    // Entity Narration key
    ENTITY_NARRATOR_KEY("entity_narrator_key"),

    // Find Fluid Keys
    FIND_FLUID_TEXT_KEY("find_fluid_text_key"),
    FIND_FLUID_VOLUME("find_fluid_volume"),
    FIND_FLUID_PITCH("find_fluid_pitch"),
    FIND_FLUID_RANGE("find_fluid_range"),

    // Chat Narration Key
    CHAT_NARRATION("chat_narration"),

    // Accessibility Plus Keys
    READ_BLOCKS_KEY("ap_read_blocks"),
    READ_TOOLTIPS_KEY("ap_read_tooltip"),
    READ_SIGNS_CONTENTS("ap_read_signs_contents"),
    INV_KEYBOARD_CONTROL_KEY("ap_inventory_keyboard_control"),
    ATION_BAR_KEY("ap_action_bar_key"),

    // Point Of Interest
    POI_ENTITY_LOCKING_NARRATE_DISTANCE_KEY("poi_entity_locking_narrate_distance_key"),
    POI_BLOCKS_LOCKING_NARRATE_DISTANCE_KEY("poi_blocks_locking_narrate_distance_key"),
    POI_VOLUME("poi_volume"),
    POI_RANGE("poi_range"),
    POI_DELAY("poi_delay"),
    POI_KEY("point_of_interest_key");

    private final String key;

    ConfigKeys(String key) {this.key = key;}

    public String getKey(){return this.key;}
}
