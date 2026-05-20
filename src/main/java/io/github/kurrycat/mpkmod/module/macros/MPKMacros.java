package io.github.kurrycat.mpkmod.module.macros;

import io.github.kurrycat.mpkmod.Main;
import io.github.kurrycat.mpkmod.compatibility.API;
import io.github.kurrycat.mpkmod.compatibility.MCClasses.*;
import io.github.kurrycat.mpkmod.events.Event;
import io.github.kurrycat.mpkmod.events.EventAPI;
import io.github.kurrycat.mpkmod.events.OnRenderOverlayEvent;
import io.github.kurrycat.mpkmod.events.OnRenderWorldOverlayEvent;
import io.github.kurrycat.mpkmod.gui.infovars.InfoString;
import io.github.kurrycat.mpkmod.gui.screens.options_gui.Option;
import io.github.kurrycat.mpkmod.module.macros.macro_gui.MacroGUI;
import io.github.kurrycat.mpkmod.module.macros.util.FileUtil;
import io.github.kurrycat.mpkmod.modules.MPKModule;
import io.github.kurrycat.mpkmod.util.ClassUtil;
import io.github.kurrycat.mpkmod.util.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class MPKMacros implements MPKModule {
    public static final String MODULE_NAME = "mpkmacros";
    public static final Logger LOGGER = LogManager.getLogger(MODULE_NAME);

    public static Macro.Runner currentMacro = null;
    public static MacroGUI macroGUI = new MacroGUI();

    @Option.Field(
            category = "mpkmacros",
            displayName = "Enable smooth camera",
            description = "When enabled, macro turns will progress smoothly throughout the tick instead of snapping instantly"
    )
    public static boolean smoothCameraEnabled = true;

    public void init() {
        addClassesToClassesTxt(new Class[] {
                MPKMacros.class
        });
        API.optionsMap = Option.createOptionMap();

        API.registerGUIScreen("macro_gui", macroGUI);
    }

    public void loaded() {
        init();
        FileUtil.init();

        EventAPI.addListener(EventAPI.EventListener.onTickStart(e -> {
            if (currentMacro == null) return;

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
        }));

        final String MACRO_STRING = "MACRO";
        final double FONT_SIZE = 18;
        final Vector2D PERCENT_POS = new Vector2D(0.5, 0.4);
        EventAPI.addListener(new EventAPI.EventListener<OnRenderOverlayEvent>(
                e -> {
                    if (currentMacro == null) return;

                    Vector2D STRING_SIZE = FontRenderer.getStringSize(MACRO_STRING, FONT_SIZE);
                    FontRenderer.drawString(
                            MACRO_STRING,
                            Renderer2D.getScaledSize()
                                    .mult(PERCENT_POS)
                                    .sub( STRING_SIZE.div(2) ),
                            new Color(255, 75, 75), FONT_SIZE, true
                    );
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

    private void addClassesToClassesTxt(Class<?>[] classes) {
        try {
            Field classesField = ClassUtil.class.getDeclaredField("classes");
            classesField.setAccessible(true);

            @SuppressWarnings("unchecked")
            final Set<Class<?>> classesTxt = (Set<Class<?>>) classesField.get(null);
            classesTxt.addAll(Arrays.asList(classes));

            Main.infoTree = InfoString.createInfoTree();
        } catch (ReflectiveOperationException e) { e.printStackTrace(); }
    }
}
