package io.github.kurrycat.mpkmod.module.macros.macro_gui;

import io.github.kurrycat.mpkmod.compatibility.MCClasses.Minecraft;
import io.github.kurrycat.mpkmod.gui.Theme;
import io.github.kurrycat.mpkmod.gui.components.*;
import io.github.kurrycat.mpkmod.module.macros.MPKMacros;
import io.github.kurrycat.mpkmod.module.macros.Macro;
import io.github.kurrycat.mpkmod.module.macros.util.LinkedList;
import io.github.kurrycat.mpkmod.util.Vector2D;

import java.io.File;

public class MacroTickList extends ScrollableList<MacroTick> {
    public Macro currentMacro;
    public InputField inputField;
    public Button runButton;

    protected LinkedList<MacroTick> items = new LinkedList<>();

    public MacroTickList(Vector2D pos, Vector2D size) {
        super();
        setPos(pos);
        setSize(size);
        setTitle("Macro Tick List");

        bottomCover.setHeight(29, false);

        Div filenameDiv = new Div(new Vector2D(0, 0), new Vector2D(0.7D, 1));
        bottomCover.addChild(filenameDiv, PERCENT.SIZE);

        inputField = new InputField(new Vector2D(0, 2), -5);
        inputField.setFilter(InputField.FILTER_FILENAME);
        filenameDiv.addChild(inputField, PERCENT.NONE, Anchor.BOTTOM_RIGHT);

        filenameDiv.addChild(new TextRectangle(
                new Vector2D(0, 3),
                new Vector2D(-5, 10),
                "Filename",
                null,
                Theme.defaultText
        ), PERCENT.NONE, Anchor.TOP_RIGHT);

        Div runDiv = new Div(new Vector2D(0, 0), new Vector2D(0.3D, 1));
        bottomCover.addChild(runDiv, PERCENT.SIZE, Anchor.TOP_RIGHT);

        runButton = new Button("Run Macro",
                new Vector2D(0, 0),
                new Vector2D(-10, 20),
                mouseButton -> {
                    ((MacroGUI) parent).close();
                    if (currentMacro != null)
                        currentMacro.run();
                }
        );
        runDiv.addChild(runButton, PERCENT.NONE, Anchor.CENTER);

        items.addFirst(new MacroTick.Empty(this));
    }

    @Override
    public void render(Vector2D mouse) {
        runButton.enabled = currentMacro != null;
        super.render(mouse);
    }

    @Override
    public LinkedList<MacroTick> getItems() {
        return items;
    }

    public void load(File file) {
        save();
        setMacro(new Macro(file));
    }

    public void save() {
        if (currentMacro == null) return;
        currentMacro.setName(inputField.content);
        currentMacro.save();
    }

    public void setMacro(Macro macro) {
        currentMacro = macro;
        inputField.content = macro.name;
        items.clear();
        if (currentMacro.getSize() == 0) currentMacro.addFirst(new Macro.Tick());

        for (Macro.NodeItr it = currentMacro.nodeIterator(); it.hasNext(); ) {
            Macro.Node n = it.next();
            MacroTick t = new MacroTick(this, n);
            t.setNode(items.addLast(t));
        }
    }
}
