package com.zenyte.game.model.ui.testinterfaces.advancedsettings;

import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 10/06/2022
 */
public final class SettingCategory {
    private static final int SETTINGS_CATEGORY_ID_PARAM = 743;
    private static final int SETTINGS_CATEGORY_NAME_PARAM = 744;
    private static final int SETTINGS_CATEGORY_SETTINGS_LIST_PARAM = 745;
    private final int id;
    private final String name;

    private final List<Setting> settings;

    SettingCategory(StructDefinitions struct) {
        this.id = struct.getParamAsInt(SETTINGS_CATEGORY_ID_PARAM);
        this.name = struct.getParamAsString(SETTINGS_CATEGORY_NAME_PARAM);
        final IntEnum settings = EnumDefinitions.getIntEnum(struct.getParamAsInt(SETTINGS_CATEGORY_SETTINGS_LIST_PARAM));
        this.settings = getSettings(settings);
    }

    private List<Setting> getSettings(IntEnum settingsEnum) {
        final List<Setting> list = new ArrayList<>(settingsEnum.getSize());
        for (final int element : settingsEnum.getValues().values()) {
            final StructDefinitions struct = StructDefinitions.get(element);
            list.add(new Setting(struct));
        }
        return list;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SettingCategory that = (SettingCategory) o;

        if (id != that.id) return false;
        if (!name.equals(that.name)) return false;
        return settings.equals(that.settings);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + settings.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SettingCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", settings=" + settings +
                '}';
    }
}
