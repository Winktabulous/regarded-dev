package com.zenyte.game.content.rewards;

import com.zenyte.game.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Rewards {

    private static final Logger logger = LoggerFactory.getLogger(Rewards.class);
    private final static Path DIRECTORY = Paths.get("data", "rewards");

    /**
     * A list of all the rewards
     */
    public static Map<String, List<RewardEntrySet>> rewardsMap = new HashMap<>();

    public static List<Item> generateRewards(String name, int rolls) {
        if (rewardsMap.get(name) == null) {
            return new ArrayList<>();
        }
        return generateRewards(rewardsMap.get(name), rolls);
    }

    public static List<Item> generateRewards(String name) {
        return generateRewards(name, 1);
    }


    public static List<Item> generateRewards(List<RewardEntrySet> entrySets, int rolls) {
        List<Item> newRewards = new ArrayList<>();

        for (int i = 0; i < rolls; i++) {
            setLoop:
            for (RewardEntrySet set : entrySets) {
                if (set.getType() == RewardSetRollType.ALWAYS_ROLL) {
                    if (set.roll()) {
                        newRewards.addAll(set.getReward());
                    }
                } else {
                    if (set.roll()) {
                        newRewards.addAll(set.getReward());
                        break setLoop;
                    }
                }
            }
        }
        return newRewards;
    }

    /**
     * Loads all the rewards from file
     */
    public static void load() {
        rewardsMap = new HashMap<>();
        try {
            Files.walkFileTree(DIRECTORY, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.getFileName().toString().endsWith(".json")) {
                        RewardJsonLoader loader = new RewardJsonLoader(file);
                        loader.load();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Loaded " + Rewards.rewardsMap.size() + " reward sets");
    }


}