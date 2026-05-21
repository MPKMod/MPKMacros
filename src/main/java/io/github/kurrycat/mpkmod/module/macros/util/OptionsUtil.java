package io.github.kurrycat.mpkmod.module.macros.util;

import io.github.kurrycat.mpkmod.compatibility.API;
import io.github.kurrycat.mpkmod.gui.screens.options_gui.Option;
import io.github.kurrycat.mpkmod.module.macros.MPKMacros;

import java.awt.*;
import java.lang.reflect.Field;

public class OptionsUtil {
    public static void registerOption(Field field, String displayName, String description) {
        String id = field.getName();
        String defaultValue;
        try {
            defaultValue = field.get((Object)null).toString();
        } catch (IllegalAccessException var10) {
            API.LOGGER.debug(API.CONFIG_MARKER, "OptionMap: IllegalAccessException while trying to access field {} from {}", new Object[]{id, field.getDeclaringClass().getName()});
            return;
        } catch (NullPointerException var11) {
            API.LOGGER.debug(API.CONFIG_MARKER, "OptionMap: Option field for Option with id: {} in the class {} is not static", new Object[]{id, field.getDeclaringClass().getName()});
            return;
        }

        Option.ValueType type = getValueTypeForField(field);
        Option option = (new Option(id, defaultValue, defaultValue, type))
                .setCategory(MPKMacros.MODULE_NAME)
                .setDisplayName(displayName)
                .setDescription(description)
                .setShowInOptionList(true)
                .link(field);

        API.optionsMap.put(id, option);
    }

    private static Option.ValueType getValueTypeForField(Field field) {
        Option.ValueType type = Option.ValueType.STRING;
        if (field.getType() == Boolean.class || field.getType() == Boolean.TYPE) {
            type = Option.ValueType.BOOLEAN;
        }

        if (field.getType() == Double.class || field.getType() == Double.TYPE) {
            type = Option.ValueType.DOUBLE;
        }

        if (field.getType() == Integer.class || field.getType() == Integer.TYPE) {
            type = Option.ValueType.INTEGER;
        }

        if (field.getType() == Color.class) {
            type = Option.ValueType.COLOR;
        }
        return type;
    }
}
