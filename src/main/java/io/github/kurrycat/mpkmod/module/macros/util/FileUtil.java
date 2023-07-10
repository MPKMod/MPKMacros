package io.github.kurrycat.mpkmod.module.macros.util;

import java.io.File;

public class FileUtil {
    public static final String MACRO_FOLDER_PATH = "config/mpk/macros";
    public static File MACRO_FOLDER;

    public static void init() {
        MACRO_FOLDER = new File(MACRO_FOLDER_PATH);
        //noinspection ResultOfMethodCallIgnored
        MACRO_FOLDER.mkdir();
    }
}
