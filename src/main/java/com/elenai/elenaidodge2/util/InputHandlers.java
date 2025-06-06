package com.elenai.elenaidodge2.util;

import com.elenai.elenaidodge2.client.animation.DodgeDirection;
import com.elenai.elenaidodge2.config.ED2ClientConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.HashMap;
import java.util.Map;

public class InputHandlers {
    private static final HashMap<KeyMapping, MutableInt> KEY_LAST_STATE = new HashMap<>();
    private static final HashMap<KeyMapping, DodgeDirection> LOOKUP_KEY_TO_DIRECTION = new HashMap<>();

    public static void init() {
        Options options = Minecraft.getInstance().options;
        KEY_LAST_STATE.put(options.keyUp, new MutableInt());
        KEY_LAST_STATE.put(options.keyDown, new MutableInt());
        KEY_LAST_STATE.put(options.keyLeft, new MutableInt());
        KEY_LAST_STATE.put(options.keyRight, new MutableInt());
        LOOKUP_KEY_TO_DIRECTION.put(options.keyUp, DodgeDirection.FORWARD);
        LOOKUP_KEY_TO_DIRECTION.put(options.keyDown, DodgeDirection.BACKWARD);
        LOOKUP_KEY_TO_DIRECTION.put(options.keyLeft, DodgeDirection.LEFT);
        LOOKUP_KEY_TO_DIRECTION.put(options.keyRight, DodgeDirection.RIGHT);
    }

    public static void singleTapHandler(LocalPlayer player) {
        DodgeDirection dodgeDirection = DodgeDirection.byMoveVector(player.input.getMoveVector());
        DodgeHandler.handleDodge(player, dodgeDirection);
    }

    public static void doubleTapInputHandler(LocalPlayer player) {
        for (Map.Entry<KeyMapping, MutableInt> entry : KEY_LAST_STATE.entrySet()) {
            if (entry.getValue().intValue() > 0) {
                entry.getValue().decrement();
            }
            if (entry.getKey().isDown()) {
                if (entry.getValue().intValue() > 0) {
                    DodgeHandler.handleDodge(player,
                            LOOKUP_KEY_TO_DIRECTION.getOrDefault(entry.getKey(), DodgeDirection.FORWARD));
                    entry.getValue().setValue(-1);
                } else {
                    entry.getValue().setValue(ED2ClientConfig.DOUBLE_TAP_TICKS.get());
                }
            } else if (entry.getValue().intValue() == -1) {
                entry.getValue().setValue(0);
            }
        }
    }
}
