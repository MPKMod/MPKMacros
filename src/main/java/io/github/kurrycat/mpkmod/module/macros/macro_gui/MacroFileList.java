package io.github.kurrycat.mpkmod.module.macros.macro_gui;

import io.github.kurrycat.mpkmod.gui.Theme;
import io.github.kurrycat.mpkmod.gui.components.*;
import io.github.kurrycat.mpkmod.module.macros.util.FileUtil;
import io.github.kurrycat.mpkmod.util.Vector2D;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MacroFileList extends ScrollableList<MacroFileList.MacroFile> {
    private final MacroTickList macroTickList;
    private final List<MacroFile> allItems = new ArrayList<>();

    private final InputField inputField;

    public MacroFileList(MacroTickList macroTickList, Vector2D pos, Vector2D size) {
        super();
        setPos(pos);
        setSize(size);
        setTitle("Macro Files");
        this.macroTickList = macroTickList;
        bottomCover.setHeight(29, false);
        bottomCover.addChild(new Button(
                "Reload",
                new Vector2D(1 / 4D, 0),
                new Vector2D(40, 20),
                mouseButton -> reloadFiles()
        ), PERCENT.POS_X, Anchor.CENTER, Anchor.CENTER_RIGHT);
        inputField = new InputField(
                new Vector2D(2 / 7D, 2),
                1 / 2D
        );
        inputField.setFilter(InputField.FILTER_FILENAME);
        inputField.setOnContentChange(content -> filterFiles(content.getContent()));
        bottomCover.addChild(inputField,
                PERCENT.X, Anchor.BOTTOM_CENTER, Anchor.BOTTOM_LEFT);
        bottomCover.addChild(new TextRectangle(
                new Vector2D(2 / 7D, 3),
                new Vector2D(1 / 2D, 10),
                "Filter",
                null,
                Theme.defaultText
        ), PERCENT.X, Anchor.TOP_CENTER, Anchor.TOP_LEFT);
        reloadFiles();
    }

    public void reloadFiles() {
        File[] files = FileUtil.MACRO_FOLDER.listFiles();
        if (files == null) return;

        allItems.clear();
        for (File file : files) {
            if (!file.getName().endsWith(".csv")) continue;
            allItems.add(new MacroFile(this, file));
        }
        filterFiles(inputField.content);
    }

    public void filterFiles(String filter) {
        items.clear();
        for (MacroFile m : allItems)
            if (m.file.getName().toLowerCase().contains(filter.toLowerCase()))
                items.add(m);
    }

    public static class MacroFile extends ScrollableListItem<MacroFile> {
        public File file;

        public MacroFile(MacroFileList parent, File file) {
            super(parent);
            setHeight(20);
            this.file = file;
            String name = file.getName();
            if (name.endsWith(".csv")) name = name.substring(0, name.length() - ".csv".length());
            addChild(new Button(
                    name,
                    new Vector2D(0, 0),
                    new Vector2D(-2, -2),
                    mouseButton -> parent.macroTickList.load(file)
            ), PERCENT.NONE, Anchor.CENTER);
        }

        public void render(int index, Vector2D pos, Vector2D size, Vector2D mouse) {
            renderDefaultBorder(pos, size);
            renderComponents(mouse);
        }
    }
}
