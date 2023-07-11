package io.github.kurrycat.mpkmod.module.macros.macro_gui;

import io.github.kurrycat.mpkmod.gui.ComponentScreen;
import io.github.kurrycat.mpkmod.gui.components.Anchor;
import io.github.kurrycat.mpkmod.gui.components.Button;
import io.github.kurrycat.mpkmod.util.Vector2D;

public class MacroGUI extends ComponentScreen {
    private MacroTickList macroTickList;

    public boolean shouldCreateKeyBind() {
        return true;
    }

    public boolean resetOnOpen() {
        return false;
    }

    public void onGuiInit() {
        super.onGuiInit();

        macroTickList = new MacroTickList(
                new Vector2D(0.05D, 0.05D),
                new Vector2D(0.6D, 0.9D)
        );

        addChild(macroTickList, PERCENT.ALL, Anchor.TOP_LEFT);
        macroTickList.topCover.addChild(
                new Button(
                        "x",
                        new Vector2D(5, 1),
                        new Vector2D(11, 11),
                        mouseButton -> close()
                ),
                PERCENT.NONE, Anchor.CENTER_RIGHT
        );

        addChild(new MacroFileList(
                macroTickList,
                new Vector2D(0.05D, 0.05D),
                new Vector2D(0.25D, 0.9D)
        ), PERCENT.ALL, Anchor.TOP_RIGHT);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        macroTickList.save();
    }

    @Override
    public void render(Vector2D mouse, float partialTicks) {
        super.render(mouse, partialTicks);
    }
}
