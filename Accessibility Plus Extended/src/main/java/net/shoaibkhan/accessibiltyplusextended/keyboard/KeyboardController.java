package net.shoaibkhan.accessibiltyplusextended.keyboard;

import blue.endless.jankson.annotation.Nullable;
import me.shedaniel.cloth.api.client.events.v0.ClothClientHooks;
import me.shedaniel.cloth.api.client.events.v0.ScreenHooks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.shoaibkhan.accessibiltyplusextended.HudScreenHandler;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;
import net.shoaibkhan.accessibiltyplusextended.mixin.AccessorHandledScreen;
import net.shoaibkhan.accessibiltyplusextended.util.KeyBinds;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.List;

public class KeyboardController {
    private static MinecraftClient client;
    @Nullable
    private static List<SlotsGroup> groups;
    @Nullable
    private static SlotsGroup currentGroup;
    @Nullable
    private static Slot currentSlot;
    @Nullable
    private static AccessorHandledScreen screen;
    private static boolean narrateCursorStack = false;
    private static double lastMouseX = 0;
    private static double lastMouseY = 0;
    private static final boolean hasControlOverMouse = false;
    private Robot robot;

    public KeyboardController() {
        System.setProperty("java.awt.headless", "false");
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            robot = null;
        }
        if (Config.get(ConfigKeys.INV_KEYBOARD_CONTROL_KEY.getKey())) {
            ClothClientHooks.SCREEN_INIT_POST.register(this::onScreenOpen);
            ClothClientHooks.SCREEN_KEY_PRESSED.register(this::onKeyPress);
        }
    }

    public static boolean hasControlOverMouse() {
        if (screen == null) {
            return false;
        } else {
            return hasControlOverMouse;
        }
    }

    private ActionResult onScreenOpen(MinecraftClient mc, Screen currentScreen, ScreenHooks screenHooks) {
        client = mc;
        groups = null;
        screen = null;
        currentGroup = null;
        currentSlot = null;

        if (currentScreen != null && currentScreen instanceof AccessorHandledScreen) {
            screen = (AccessorHandledScreen) currentScreen;
            groups = SlotsGroup.generateGroupsFromSlots(screen.getHandler().slots);
            System.out.println("\n\n\n\n" + currentScreen.getNarratedTitle());
            moveMouseToHome();
        }
        return ActionResult.PASS;
    }

    private ActionResult onKeyPress(MinecraftClient mc, Screen currentScreen, int keyCode, int scanCode,
                                    int modifiers) {
        if (screen != null && Config.get(ConfigKeys.INV_KEYBOARD_CONTROL_KEY.getKey()) && !HudScreenHandler.isSearchingRecipies) {
            if (KeyBinds.LEFT_KEY.getKeyBind().matchesKey(keyCode, scanCode)) {
                focusSlotAt(FocusDirection.LEFT);
            } else if (KeyBinds.RIGHT_KEY.getKeyBind().matchesKey(keyCode, scanCode)) {
                focusSlotAt(FocusDirection.RIGHT);
            } else if (KeyBinds.UP_KEY.getKeyBind().matchesKey(keyCode, scanCode)) {
                focusSlotAt(FocusDirection.UP);
            } else if (KeyBinds.DOWN_KEY.getKeyBind().matchesKey(keyCode, scanCode)) {
                focusSlotAt(FocusDirection.DOWN);
            } else if (KeyBinds.GROUP_KEY.getKeyBind().matchesKey(keyCode, scanCode)) {
                focusGroupVertically(modifiers != GLFW.GLFW_MOD_SHIFT);
                return ActionResult.SUCCESS;
            } else if (KeyBinds.HOME_KEY.getKeyBind().matchesKey(keyCode, scanCode)) {
                if (modifiers == GLFW.GLFW_MOD_SHIFT) {
                    focusEdgeGroup(false);
                } else {
                    focusEdgeSlot(false);
                }
            } else if (KeyBinds.END_KEY.getKeyBind().matchesKey(keyCode, scanCode)) {
                if (modifiers == GLFW.GLFW_MOD_SHIFT) {
                    focusEdgeGroup(true);
                } else {
                    focusEdgeSlot(true);
                }
            } else if (KeyBinds.CLICK_KEY.getKeyBind().matchesKey(keyCode, scanCode)) {
                click(false);
            } else if (KeyBinds.RIGHT_CLICK_KEY.getKeyBind().matchesKey(keyCode, scanCode)) {
                click(true);
            }
        }
        return ActionResult.PASS;
    }

    private void focusSlotAt(FocusDirection direction) {
        if (currentGroup == null) {
            focusGroupVertically(true);
            return;
        }
        focusSlot(currentGroup.getSlot(currentSlot));
    }

    private void focusSlot(Slot slot) {
        currentSlot = slot;
        moveToSlot(currentSlot);
        String message = "";
        if (currentGroup.getSlotName(currentSlot).length() > 0) {
            message += currentGroup.getSlotName(currentSlot) + ". ";
        }
        if (!currentSlot.hasStack()) {
            message += " Empty";
        } else {
            List<Text> lines = currentSlot.getStack().getTooltip(client.player, TooltipContext.Default.NORMAL);
            for (Text line : lines) {
                message += line.getString() + ", ";
            }
        }
        if (message != null && message.length() > 0) {
            NarratorPlus.narrate(message);
        }
    }

    private void focusEdgeSlot(boolean end) {
        if (currentGroup == null) {
            focusGroupVertically(true);
            return;
        }
        if (currentGroup.slots.size() == 1 && currentSlot != null) {
            NarratorPlus.narrate("This group only has one slot!");
            return;
        }
        focusSlot(end ? currentGroup.getLastSlot() : currentGroup.getFirstSlot());
    }

    private void focusEdgeGroup(boolean last) {
        focusGroup(groups.get(last ? groups.size() - 1 : 0));
    }

    private void focusGroupVertically(boolean goBelow) {
        if (currentGroup == null) {
            focusGroup(groups.get(0));
        } else {
            int currentGroupIndex = groups.indexOf(currentGroup);
            int nextGroupIndex = currentGroupIndex + (goBelow ? 1 : -1);
            if (nextGroupIndex < 0) {
                NarratorPlus.narrate("Reached the top group");
                return;
            } else if (nextGroupIndex > groups.size() - 1) {
                NarratorPlus.narrate("Reached the bottom group");
                return;
            } else {
                focusGroup(groups.get(nextGroupIndex));
            }
        }
    }

    private void focusGroup(SlotsGroup group) {
        currentGroup = group;
        currentSlot = null;
        moveMouseToHome();
        NarratorPlus.narrate(currentGroup.name);
    }

    private void moveMouseToHome() {
        SlotsGroup lastGroup = groups.get(groups.size() - 1);
        Slot lastSlot = lastGroup.getLastSlot();
        moveMouseToScreenCoords(lastSlot.x + 19, lastSlot.y + 19);
    }

    private void click(boolean rightClick) {
        if (robot == null)
            return;

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        narrateCursorStack = true;
    }

    private void moveMouseTo(int x, int y) {
        if (robot == null)
            return;

        robot.mouseMove(x, y);
        lastMouseX = x;
        lastMouseY = y;
    }

    private void moveMouseToScreenCoords(int x, int y) {
        double targetX = (screen.getX() + x) * client.getWindow().getScaleFactor() + client.getWindow().getX();
        double targetY = (screen.getY() + y) * client.getWindow().getScaleFactor() + client.getWindow().getY();
        moveMouseTo((int) targetX, (int) targetY);
    }

    private void moveToSlot(Slot slot) {
        if (slot == null) {
            return;
        }
        moveMouseToScreenCoords(slot.x + 9, slot.y + 9);
    }

    private enum FocusDirection {
        UP, DOWN, LEFT, RIGHT
    }
}
