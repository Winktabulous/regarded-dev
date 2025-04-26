package com.zenyte.game.records;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.MessageType;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-20
 */
public class RecordManager {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(RecordManager.class);
    private static final List<SpeedRecord> serverSpeedRecords = new ArrayList<>();
    private static final String DATA_PATH = "data/records/globalRecords.json";

    private static RecordManager instance;

    public static RecordManager getInstance() {
        if (instance == null) instance = new RecordManager();
        return instance;
    }

    public RecordManager() {
        clearAndReloadFromFile();
    }

    // public in the case we want to be able to reload on the fly via a command for ex.
    public void clearAndReloadFromFile() {
        serverSpeedRecords.clear();
        loadFromFile();
    }

    public boolean isEventRecorded(String eventName) {
        return serverSpeedRecords.stream()
            .anyMatch(record -> record.eventName().equals(eventName));
    }

    public Optional<SpeedRecord> getRecordByEventName(String eventName) {
        return serverSpeedRecords.stream()
            .filter(record -> record.eventName().equals(eventName))
            .findFirst();
    }

    public void addRecord(SpeedRecord speedRecord) {
        // check if the event we're passing in is already recorded
        if (isEventRecorded(speedRecord.eventName())) {
            // now we get the recorded event and replace it
            getRecordByEventName(speedRecord.eventName())
                .ifPresentOrElse(
                    r -> replaceRecord(r, speedRecord),
                    // If for some reason we got here and there is no record, make a new one
                    () -> addNewRecord(speedRecord));
        }
        // we do not have a recorded event
        else addNewRecord(speedRecord);
    }

    private void addNewRecord(SpeedRecord speedRecord) {
        serverSpeedRecords.add(speedRecord);
        saveToFile(serverSpeedRecords);
        World.getPlayers().forEach(player -> player.sendMessage(speedRecord.toString()));
    }
    private void replaceRecord(SpeedRecord oldRecord, SpeedRecord newRecord) {
        serverSpeedRecords.remove(oldRecord);
        serverSpeedRecords.add(newRecord);
        saveToFile(serverSpeedRecords);
        World.getPlayers().forEach(player -> player.sendMessage("[New Record!] ".concat(newRecord.toString())));
    }

    public static void saveToFile(List<SpeedRecord> speedRecordsList) {
        var file = new File(DATA_PATH);
        if (!file.exists())
            if (!file.mkdirs()) file.mkdir();
        try {
            Files.write(
                Paths.get(DATA_PATH),
                new Gson().toJson(speedRecordsList).getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void loadFromFile() {
        try {
            var file = new File(DATA_PATH);
            if (!file.exists())
                if (!file.mkdirs()) file.mkdir();

            var json = new String(Files.readAllBytes(Paths.get(DATA_PATH)));
            if (!json.isEmpty())
                serverSpeedRecords.addAll(new Gson().fromJson(json, new TypeToken<List<SpeedRecord>>() {}.getType()));
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

}
