package io.github.kurrycat.mpkmod.module.macros;

import io.github.kurrycat.mpkmod.module.macros.util.FileUtil;
import io.github.kurrycat.mpkmod.module.macros.util.LinkedList;

import java.io.File;
import java.io.IOException;

public class Macro extends LinkedList<Macro.Tick> {
    public String name;
    public File macroFile;

    public Macro() {
        setName(null);
    }

    public void setName(String content) {
        if (content == null || content.isEmpty()) {
            name = getDefaultName();
        } else name = content;
        macroFile = new File(FileUtil.MACRO_FOLDER, name + ".csv");
    }

    protected String getDefaultName() {
        File[] files = FileUtil.MACRO_FOLDER.listFiles();
        int num = files == null ? 1 : files.length + 1;
        return "Macro_" + num;
    }

    public Macro(File file) {
        name = file.getName();
        if (name.endsWith(".csv")) name = name.substring(0, name.length() - ".csv".length());
        macroFile = file;

        load();
    }

    public void load() {

    }

    public void save() {
        try {
            //noinspection ResultOfMethodCallIgnored
            macroFile.createNewFile();
        } catch (IOException e) {
            MPKMacros.LOGGER.info("Failed to create file: " + macroFile.getAbsolutePath());
            return;
        }


    }

    public static class Tick {
        public TickInput tickInput;

        protected static Tick lastJump = null;

        protected Node target = null;
        protected int jumpCount;
        protected int counter;
        protected boolean resetCounterOnDifferentJump;

        public Tick(TickInput tickInput) {
            super();
            this.tickInput = tickInput;
        }

        public Tick() {
            this.tickInput = new TickInput();
        }

        @Override
        public String toString() {
            return tickInput == null ? "null" : tickInput.toString();
        }
    }

    @Override
    public It iterator() {
        return new It();
    }

    public class It extends Itr {
        public boolean hasNext() {
            return super.hasNext();
        }

        public Tick next() {
            if (curr.item.target == null)
                return super.next();

            if (curr.item.resetCounterOnDifferentJump && Tick.lastJump != curr.item) {
                curr.item.counter = curr.item.jumpCount;
                Tick.lastJump = curr.item;
            }
            if (curr.item.counter == 0) {
                curr = curr.next;
                return super.next();
            }
            curr.item.counter--;
            curr = curr.item.target;

            return super.next();
        }
    }
}