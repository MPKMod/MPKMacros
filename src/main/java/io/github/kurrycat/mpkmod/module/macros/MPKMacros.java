package io.github.kurrycat.mpkmod.module.macros;

import io.github.kurrycat.mpkmod.compatibility.API;
import io.github.kurrycat.mpkmod.compatibility.MCClasses.*;
import io.github.kurrycat.mpkmod.events.Event;
import io.github.kurrycat.mpkmod.events.EventAPI;
import io.github.kurrycat.mpkmod.events.OnRenderOverlayEvent;
import io.github.kurrycat.mpkmod.events.OnRenderWorldOverlayEvent;
import io.github.kurrycat.mpkmod.module.macros.macro_gui.MacroGUI;
import io.github.kurrycat.mpkmod.module.macros.util.FileUtil;
import io.github.kurrycat.mpkmod.module.macros.util.OptionsUtil;
import io.github.kurrycat.mpkmod.module.macros.util.MacroRecorder;
import io.github.kurrycat.mpkmod.modules.MPKModule;
import io.github.kurrycat.mpkmod.util.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;


public class MPKMacros implements MPKModule {
    public static final String MODULE_NAME = "mpkmacros";
    public static final Logger LOGGER = LogManager.getLogger(MODULE_NAME);

    public static Macro.Runner currentMacro = null;
    public static MacroGUI macroGUI = new MacroGUI();

    public static boolean smoothCameraEnabled = true;

    public void init() {
        try {
            // This will need to change in MPK2 to properly detect annotations in module classes
            Field smoothCameraField = MPKMacros.class.getDeclaredField("smoothCameraEnabled");
            OptionsUtil.registerOption(
                    smoothCameraField,
                    "Enable macro smooth camera",
                    "When enabled, macro turns will progress smoothly throughout the tick instead of snapping instantly"
            );
        } catch (NoSuchFieldException e) { throw new RuntimeException(e); }

        API.registerGUIScreen("macro_gui", macroGUI);

        // TODO: update api for proper names
        API.registerKeyBinding("macros.run", () -> {
            if (macroGUI.getCurrentMacro() != null) {
                macroGUI.getCurrentMacro().run();
            }
        });
        API.registerKeyBinding("macros.record_start", MacroRecorder::startRecording);
        API.registerKeyBinding("macros.record_end", MacroRecorder::endRecording);
    }

    public void loaded() {
        init();
        FileUtil.init();

        EventAPI.addListener(EventAPI.EventListener.onTickStart(e -> {
            if (currentMacro != null) {

                List<Integer> buttons = Keyboard.getPressedButtons();
                boolean cancel = buttons.contains(InputConstants.KEY_ESCAPE) ||
                        buttons.contains(InputConstants.KEY_W) ||
                        buttons.contains(InputConstants.KEY_A) ||
                        buttons.contains(InputConstants.KEY_S) ||
                        buttons.contains(InputConstants.KEY_D) ||
                        buttons.contains(InputConstants.KEY_SPACE);

                if (cancel || !currentMacro.tick()) {
                    currentMacro.stop();
                    currentMacro = null;
                }
            }

            if (MacroRecorder.recording) {
                MacroRecorder.tick();
            }
        }));

        // TODO: Make this a movable info string
        final String MACRO_STRING = "MACRO";
        final String RECORDING_STRING = "RECORDING";
        final double FONT_SIZE = 18;
        final Vector2D PERCENT_POS = new Vector2D(0.5, 0.4);
        EventAPI.addListener(new EventAPI.EventListener<OnRenderOverlayEvent>(
                e -> {
                    if (currentMacro != null) {
                        Vector2D STRING_SIZE = FontRenderer.getStringSize(MACRO_STRING, FONT_SIZE);
                        FontRenderer.drawString(
                                MACRO_STRING,
                                Renderer2D.getScaledSize()
                                        .mult(PERCENT_POS)
                                        .sub(STRING_SIZE.div(2)),
                                new Color(255, 75, 75), FONT_SIZE, true
                        );
                    }
                    if (MacroRecorder.recording) {
                        Vector2D STRING_SIZE = FontRenderer.getStringSize(RECORDING_STRING, FONT_SIZE);
                        FontRenderer.drawString(
                                RECORDING_STRING,
                                Renderer2D.getScaledSize()
                                        .mult(PERCENT_POS)
                                        .sub(STRING_SIZE.div(2)),
                                new Color(255, 75, 75), FONT_SIZE, true
                        );
                    }
                },
                Event.EventType.RENDER_OVERLAY
        ));

        EventAPI.addListener(new EventAPI.EventListener<OnRenderWorldOverlayEvent>(
                e -> {
                    if (!smoothCameraEnabled || currentMacro == null) return;
                    float t = e.partialTicks;

                    float lerpedYaw = currentMacro.getLerpedYaw(t);
                    float lerpedPitch = currentMacro.getLerpedPitch(t);

                    int pressedInputs = currentMacro.getInput().getKeyInputs();

                    // TODO: Add a proper way to modify inputs individually in MPK2
                    Minecraft.setInputs(lerpedYaw, false, lerpedPitch, false, pressedInputs, ~pressedInputs, 0, 0);
                },
                Event.EventType.RENDER_WORLD_OVERLAY
        ));
    }
}
