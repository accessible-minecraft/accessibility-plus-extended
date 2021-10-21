package net.shoaibkhan.accessibiltyplusextended.util;

//import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.option.KeyBinding;

public enum KeyBinds {
    CONFIG_KEY(null),
    AP_CONFIG_KEY(null),
    LockEntityKey(null),
    LEFT_KEY(null),
    RIGHT_KEY(null),
    UP_KEY(null),
    DOWN_KEY(null),
    GROUP_KEY(null),
    HOME_KEY(null),
    END_KEY(null),
    CLICK_KEY(null),
    RIGHT_CLICK_KEY(null);

    private KeyBinding keyBind;


    KeyBinds(KeyBinding keyBind) {this.keyBind = keyBind;}

    public KeyBinding getKeyBind(){return this.keyBind;}

    public void setKeyBind(KeyBinding newKeyBind){this.keyBind = newKeyBind;}
}
