package io.github.kurrycat.mpkmod.module.macros;

import io.github.kurrycat.mpkmod.compatibility.API;
import io.github.kurrycat.mpkmod.compatibility.MCClasses.Keyboard;
import io.github.kurrycat.mpkmod.events.EventAPI;
import io.github.kurrycat.mpkmod.module.macros.macro_gui.MacroGUI;
import io.github.kurrycat.mpkmod.module.macros.util.FileUtil;
import io.github.kurrycat.mpkmod.modules.MPKModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MPKMacros implements MPKModule {
    public static final String MODULE_NAME = "mpkmacros";
    public static final Logger LOGGER = LogManager.getLogger(MODULE_NAME);

    public static Macro.Runner currentMacro = null;

    public void init() {
        API.registerGUIScreen("macro_gui", new MacroGUI());
    }

    public void loaded() {
        FileUtil.init();
        API.registerGUIScreen("macro_gui", new MacroGUI());
        EventAPI.addListener(EventAPI.EventListener.onTickEnd(e -> {
            if (currentMacro == null) return;

            if (Keyboard.getPressedButtons().size() != 0 || !currentMacro.tick()) {
                currentMacro.stop();
                currentMacro = null;
            }
        }));
    }
}
