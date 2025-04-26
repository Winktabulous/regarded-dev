package com.zenyte.game.content.well;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zenyte.game.content.serverevent.WorldBoost;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WellPersistence {

    private static final String SAVE_FILE = "./data/well.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public static void save(ObjectArrayList<WellSession> sessions) {
        try(FileOutputStream fos = new FileOutputStream(SAVE_FILE)) {
            fos.write(gson.toJson(sessions).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectArrayList<WellSession> load() {
        ObjectArrayList<WellSession> sessions = new ObjectArrayList<>();

        try {
            if(new File(SAVE_FILE).exists()) {
                String text = Files.readString(Paths.get(SAVE_FILE));
                sessions = new ObjectArrayList<>(gson.fromJson(text, WellSession[].class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sessions.removeIf(next -> next.getPerk() == null);//incase perks ever get removed/name changed

        for (WellPerk value : WellPerk.VALUES) {
            boolean missing = true;
            for (WellSession session : sessions) {
                if (session.getPerk() == value) {
                    missing = false;
                    break;
                }
            }
            if(missing)
                sessions.add(new WellSession(value, 0, new ObjectArrayList<>(), 0));//automatically add it if we add new perks
        }

        for (WellSession session : sessions) {
            if(session.isActived()) {
                long remaining =  System.currentTimeMillis() + TimeUnit.TICKS.toMillis(session.getCycle());
                new WorldBoost(session.getPerk(), remaining, TimeUnit.DAYS.toHours(1)).activate(false);
            }
        }

        return sessions;
    }
}
