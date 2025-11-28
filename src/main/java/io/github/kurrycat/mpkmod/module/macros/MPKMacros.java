package io.github.kurrycat.mpkmod.module.macros;

import io.github.kurrycat.mpkmod.compatibility.API;
import io.github.kurrycat.mpkmod.compatibility.MCClasses.FontRenderer;
import io.github.kurrycat.mpkmod.compatibility.MCClasses.InputConstants;
import io.github.kurrycat.mpkmod.compatibility.MCClasses.Keyboard;
import io.github.kurrycat.mpkmod.compatibility.MCClasses.Renderer2D;
import io.github.kurrycat.mpkmod.events.Event;
import io.github.kurrycat.mpkmod.events.EventAPI;
import io.github.kurrycat.mpkmod.events.OnRenderOverlayEvent;
import io.github.kurrycat.mpkmod.module.macros.macro_gui.MacroGUI;
import io.github.kurrycat.mpkmod.module.macros.util.FileUtil;
import io.github.kurrycat.mpkmod.modules.MPKModule;
import io.github.kurrycat.mpkmod.util.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.List;


public class MPKMacros implements MPKModule {
    public static final String MODULE_NAME = "mpkmacros";
    public static final Logger LOGGER = LogManager.getLogger(MODULE_NAME);

    public static Macro.Runner currentMacro = null;
    public static MacroGUI macroGUI = new MacroGUI();

    public void init() {
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
    }
}
