package net.shoaibkhan.accessibiltyplusextended.basemod.config;

public class SerializableConfig {
    public boolean readBlocks = true;
    public boolean readTooltips = true;
    public boolean readSignsContents = true;
    public boolean inventoryKeyboardControl = true;

    public SerializableConfig() {

    }

    public SerializableConfig clone() {
        SerializableConfig cloneConfig = new SerializableConfig();
        cloneConfig.readBlocks = this.readBlocks;
        cloneConfig.readTooltips = this.readTooltips;
        cloneConfig.readSignsContents = this.readSignsContents;
        cloneConfig.inventoryKeyboardControl = this.inventoryKeyboardControl;
        return cloneConfig;
    }
}
