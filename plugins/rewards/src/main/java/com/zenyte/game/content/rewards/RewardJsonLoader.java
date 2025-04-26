package com.zenyte.game.content.rewards;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zenyte.utils.JsonLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RewardJsonLoader extends JsonLoader {

    private static final Logger log = LoggerFactory.getLogger(RewardJsonLoader.class);
    private final Path path;

    public RewardJsonLoader(Path path) {
        this.path = path;
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        try {
            final String name = reader.get("name").getAsString();
            final List<RewardEntrySet> rewardList = new ArrayList<>();

            JsonArray rewardsArray = reader.getAsJsonArray("rewards");
            for (int i = 0; i < rewardsArray.size(); i++) {
                JsonObject element = rewardsArray.get(i).getAsJsonObject();
                RewardEntrySet set = new RewardEntrySet();
                List<RewardEntry> setRewards = new ArrayList<>();

                float percentage = 0.0F;

                try {
                    percentage = element.get("percent").getAsFloat();
                } catch (NumberFormatException ex) {
                    String percentAsString = element.get("percent").getAsString().replace(" ", "");
                    String[] sections = percentAsString.split("/");
                    float firstSection = (float) Integer.parseInt(sections[0]);
                    float secondSection = (float) Integer.parseInt(sections[1]);
                    percentage = (firstSection / secondSection) * 100;
                }

                RewardSetRollType rollType = RewardSetRollType.valueOf(element.get("type").getAsString());
                RewardSelectType selectType = RewardSelectType.RANDOM;
                RewardPercentageStackType stackType = RewardPercentageStackType.NEVER_CHANGE;

                if (element.has("selectType")) {
                    selectType = RewardSelectType.valueOf(element.get("selectType").getAsString());
                }

                if (element.has("percentStack")) {
                    stackType = RewardPercentageStackType.valueOf(element.get("percentStack").getAsString());
                }

                JsonArray itemEntries = element.getAsJsonArray("entries");
                for (int x = 0; x < itemEntries.size(); x++) {
                    JsonObject rewardEntryJson = itemEntries.get(x).getAsJsonObject();

                    int itemId = rewardEntryJson.get("itemId").getAsInt();
                    int minAmount = rewardEntryJson.get("minAmount").getAsInt();
                    int maxAmount = rewardEntryJson.get("maxAmount").getAsInt();

                    RewardEntry rewardEntry = new RewardEntry(itemId, minAmount, maxAmount);
                    setRewards.add(rewardEntry);
                }

                if (stackType == RewardPercentageStackType.STACKED) {
                    percentage *= itemEntries.size();
                }

                if (percentage >= 100) percentage = 100;

                set.setEntries(setRewards);
                set.setSelectType(selectType);
                set.setStackType(stackType);
                set.setType(rollType);
                set.setPercent(percentage);

                rewardList.add(set);
            }
            // Will sort by percentages
            rewardList.sort(new RewardSortEnum()
                    .reversed()
                    .thenComparing(
                            (o1, o2) -> Float.compare(o1.getPercent(), o2.getPercent())));

            Rewards.rewardsMap.put(name, rewardList);
        } catch (Exception ex) {
            log.error("error parsing " + path.toString() + ", " + ex.getMessage());
        }
    }

    @Override
    public String filePath() {
        return path.toString();
    }
}