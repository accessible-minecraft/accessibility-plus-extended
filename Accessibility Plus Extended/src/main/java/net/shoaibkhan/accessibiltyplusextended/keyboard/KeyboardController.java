package net.shoaibkhan.accessibiltyplusextended.keyboard;

import blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.shoaibkhan.accessibiltyplusextended.HudRenderCallBackClass;
import net.shoaibkhan.accessibiltyplusextended.HudScreenHandler;
import net.shoaibkhan.accessibiltyplusextended.NarratorPlus;
import net.shoaibkhan.accessibiltyplusextended.config.Config;
import net.shoaibkhan.accessibiltyplusextended.config.ConfigKeys;
import net.shoaibkhan.accessibiltyplusextended.mixin.AccessorHandledScreen;
import net.shoaibkhan.accessibiltyplusextended.util.KeyBinds;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
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
  private static boolean hasControlOverMouse = false;

  private enum FocusDirection {
    UP, DOWN, LEFT, RIGHT
  }

  public KeyboardController() {
      client = MinecraftClient.getInstance();

      main();
//    if(Config.get(ConfigKeys.INV_KEYBOARD_CONTROL_KEY.getKey())) {
//      ClothClientHooks.SCREEN_INIT_POST.register(KeyboardController::onScreenOpen);
//      ClothClientHooks.SCREEN_KEY_PRESSED.register(KeyboardController::onKeyPress);
//    }
  }

  private void main(){

    groups = null;
    screen = null;
    currentGroup = null;
    currentSlot = null;

    if (client.currentScreen != null && client.currentScreen instanceof AccessorHandledScreen) {
      screen = (AccessorHandledScreen) client.currentScreen;
      groups = SlotsGroup.generateGroupsFromSlots(screen.getHandler().slots);
      moveMouseToHome();
    }

    if (screen != null && Config.get(ConfigKeys.INV_KEYBOARD_CONTROL_KEY.getKey()) && !HudScreenHandler.isSearchingRecipies) {
      if (KeyBinds.LEFT_KEY.getKeyBind().wasPressed()) {
        focusSlotAt(FocusDirection.LEFT);
      } else if (KeyBinds.RIGHT_KEY.getKeyBind().wasPressed()) {
        focusSlotAt(FocusDirection.RIGHT);
      } else if (KeyBinds.UP_KEY.getKeyBind().wasPressed()) {
        focusSlotAt(FocusDirection.UP);
      } else if (KeyBinds.DOWN_KEY.getKeyBind().wasPressed()) {
        focusSlotAt(FocusDirection.DOWN);
      } else if (KeyBinds.GROUP_KEY.getKeyBind().wasPressed()) {
        if (HudRenderCallBackClass.isShiftPressed) {
          focusGroupVertically(false);
        } else {
          focusGroupVertically(true);
        }
      } else if (KeyBinds.HOME_KEY.getKeyBind().wasPressed()) {
        if (HudRenderCallBackClass.isShiftPressed) {
          focusEdgeGroup(false);
        } else {
          focusEdgeSlot(false);
        }
      } else if (KeyBinds.END_KEY.getKeyBind().wasPressed()) {
        if (HudRenderCallBackClass.isShiftPressed) {
          focusEdgeGroup(true);
        } else {
          focusEdgeSlot(true);
        }
      } else if (KeyBinds.CLICK_KEY.getKeyBind().wasPressed()) {
        click(false);
      } else if (KeyBinds.RIGHT_CLICK_KEY.getKeyBind().wasPressed()) {
        click(true);
      }
    }
  }

  public static boolean hasControlOverMouse() {
    if (screen == null) {
      return false;
    } else {
      return hasControlOverMouse;
    }
  }

  private static void focusSlotAt(FocusDirection direction) {
    if (currentGroup == null) {
      focusGroupVertically(true);
      return;
    }
    if (currentSlot == null) {
      focusSlot(currentGroup.getFirstSlot());
      return;
    }
    int targetDeltaX = 0;
    int targetDeltaY = 0;
    switch (direction) {
      case UP:
        if (!currentGroup.hasSlotAbove(currentSlot)) {
          NarratorPlus.narrate("No more slots above");
          return;
        }
        targetDeltaY = -18;
        break;
      case DOWN:
        if (!currentGroup.hasSlotBelow(currentSlot)) {
          NarratorPlus.narrate("No more slots below");
          return;
        }
        targetDeltaY = 18;
        break;
      case LEFT:
        if (!currentGroup.hasSlotLeft(currentSlot)) {
          NarratorPlus.narrate("No more slots to the left");
          return;
        }
        targetDeltaX = -18;
        break;
      case RIGHT:
        if (!currentGroup.hasSlotRight(currentSlot)) {
          NarratorPlus.narrate("No more slots to the right");
          return;
        }
        targetDeltaX = 18;
        break;
    }
    int targetX = currentSlot.x + targetDeltaX;
    int targetY = currentSlot.y + targetDeltaY;
    for (Slot s : currentGroup.slots) {
      if (s.x == targetX && s.y == targetY) {
        focusSlot(s);
        break;
      }
    }
  }

  private static void focusSlot(Slot slot) {
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

  private static void focusEdgeSlot(boolean end) {
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

  private static void focusEdgeGroup(boolean last) {
    focusGroup(groups.get(last ? groups.size() - 1 : 0));
  }

  private static void focusGroupVertically(boolean goBelow) {
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
        ;
      }
    }
  }

  private static void focusGroup(SlotsGroup group) {
    currentGroup = group;
    currentSlot = null;
    moveMouseToHome();
    NarratorPlus.narrate(currentGroup.name);
  }

  private static void moveMouseToHome() {
    SlotsGroup lastGroup = groups.get(groups.size() - 1);
    Slot lastSlot = lastGroup.getLastSlot();
    moveMouseToScreenCoords(lastSlot.x + 19, lastSlot.y + 19);
  }

  private static void click(boolean rightClick) {
    double scale = client.getWindow().getScaleFactor();
    double x = client.mouse.getX() / scale;
    double y = client.mouse.getY() / scale;
    int button = rightClick ? GLFW.GLFW_MOUSE_BUTTON_RIGHT : GLFW.GLFW_MOUSE_BUTTON_LEFT;
    client.currentScreen.mouseClicked(x, y, button);
    client.currentScreen.mouseReleased(x, y, button);
    narrateCursorStack = true;
  }

  private static void moveMouseTo(int x, int y) {
    try {
      Robot robot = new Robot();
      robot.mouseMove(x, y);
      lastMouseX = (double) x;
      lastMouseY = (double) y;
    } catch (AWTException e) {
      e.printStackTrace();
    } finally {
    }
  }

  private static void moveMouseToScreenCoords(int x, int y) {
    double targetX = (screen.getX() + x) * client.getWindow().getScaleFactor() + client.getWindow().getX();
    double targetY = (screen.getY() + y) * client.getWindow().getScaleFactor() + client.getWindow().getY();
    moveMouseTo((int) targetX, (int) targetY);
  }

  private static void moveToSlot(Slot slot) {
    if (slot == null) {
      return;
    }
    moveMouseToScreenCoords(slot.x + 9, slot.y + 9);
  }
}
