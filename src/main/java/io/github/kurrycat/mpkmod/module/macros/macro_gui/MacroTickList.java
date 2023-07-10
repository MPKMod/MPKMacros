package io.github.kurrycat.mpkmod.module.macros.macro_gui;

import io.github.kurrycat.mpkmod.gui.Theme;
import io.github.kurrycat.mpkmod.gui.components.Anchor;
import io.github.kurrycat.mpkmod.gui.components.InputField;
import io.github.kurrycat.mpkmod.gui.components.ScrollableList;
import io.github.kurrycat.mpkmod.gui.components.TextRectangle;
import io.github.kurrycat.mpkmod.module.macros.Macro;
import io.github.kurrycat.mpkmod.module.macros.util.LinkedList;
import io.github.kurrycat.mpkmod.util.Vector2D;

import java.io.File;

public class MacroTickList extends ScrollableList<MacroTick> {
    public Macro currentMacro;
    public InputField inputField;

    protected LinkedList<MacroTick> items = new LinkedList<>();

    public MacroTickList(Vector2D pos, Vector2D size) {
        super();
        setPos(pos);
        setSize(size);
        setTitle("Macro Tick List");

        bottomCover.setHeight(29, false);
        inputField = new InputField(
                new Vector2D(0, 2),
                -10
        );
        inputField.setFilter(InputField.FILTER_FILENAME);
        bottomCover.addChild(inputField, PERCENT.NONE, Anchor.BOTTOM_CENTER);
        bottomCover.addChild(new TextRectangle(
                new Vector2D(0, 3),
                new Vector2D(-10, 10),
                "Filename",
                null,
                Theme.defaultText
        ), PERCENT.NONE, Anchor.TOP_CENTER);

        items.addFirst(new MacroTick.Empty(this));
    }

    @Override
    public void render(Vector2D mouse) {
        super.render(mouse);
    }

    public void save() {
        if (currentMacro == null) return;
        currentMacro.setName(inputField.content);
        currentMacro.save();
    }

    public void load(File file) {
        setMacro(new Macro(file));
    }

    @Override
    public LinkedList<MacroTick> getItems() {
        return items;
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
