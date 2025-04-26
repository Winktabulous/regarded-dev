package com.zenyte.game.content.achievementdiary;

import com.google.gson.annotations.Expose;
import com.zenyte.game.world.DefaultGson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiaryInfo {

    @Expose
    private final boolean autoCompleted;

    @Expose
    private final DiaryComplexity type;

    @Expose
    private final DiaryArea area;

    public DiaryInfo(boolean autoCompleted, DiaryComplexity type, DiaryArea area) {
        this.autoCompleted = autoCompleted;
        this.type = type;
        this.area = area;
    }

    public static DiaryInfo[][] load(Path path) {
        if (path == null)
            path = Paths.get("assets/diary_info.json");
        DiaryInfo[][] diaryInfo = null;
        try {
            final FileReader reader = new FileReader(path.toFile());
            diaryInfo = DefaultGson.getGson().fromJson(reader, DiaryInfo[][].class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return diaryInfo;
    }

    public boolean isAutoCompleted() {
        return autoCompleted;
    }

    public DiaryComplexity getType() {
        return type;
    }

    public DiaryArea getArea() {
        return area;
    }
}
