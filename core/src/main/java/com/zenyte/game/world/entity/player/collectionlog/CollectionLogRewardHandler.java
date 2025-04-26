package com.zenyte.game.world.entity.player.collectionlog;

import com.near_reality.game.content.CollectionLogRewardSet;
import com.near_reality.game.content.CollectionLogRewards;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;

import java.util.Objects;

import static com.zenyte.game.world.entity.player.collectionlog.CollectionLogInterface.*;

public class CollectionLogRewardHandler {
    public static void evaluateAndSend(int struct, Player player, ObjectSet<Int2IntMap.Entry> logItems) {
        boolean completed = true;
        for(Int2IntMap.Entry entry: logItems) {
            if(!player.getCollectionLog().getContainer().contains(entry.getIntValue(), 1)) {
                completed = false;
                break;
            }
        }
        player.addTemporaryAttribute(validation_select, struct);
        CollectionLogRewardSet rewards = CollectionLogRewards.getRewardSet(struct);
        boolean hasClaimed = player.getCollectionLogRewardManager().hasClaimed(struct);
        if(hasClaimed)
            player.addTemporaryAttribute(validation_key, 3);
        else if(completed)
            player.addTemporaryAttribute(validation_key, 2);
        else
            player.addTemporaryAttribute(validation_key, 1);
        player.getPacketDispatcher().sendClientScript(34021, completed ? 1 : 0, hasClaimed ? 1 : 0, rewards.getItem0(), rewards.getQuantity0(), rewards.getItem1(), rewards.getQuantity1(), rewards.getItem2(), rewards.getQuantity2(), rewards.getItem3(), rewards.getQuantity3());
    }

    public static void forceComplete(int struct, Player player) {
        final StructDefinitions sub = Objects.requireNonNull(StructDefinitions.get(struct));
        final int subEnumId = Integer.parseInt(sub.getValue(STRUCT_POINTER_SUB_ENUM_CAT).orElseThrow(RuntimeException::new).toString());
        final IntEnum subEnum = EnumDefinitions.getIntEnum(subEnumId);
        final ObjectSet<Int2IntMap.Entry> logItems = subEnum.getValues().int2IntEntrySet();
        for(Int2IntMap.Entry entry: logItems) {
            if(!player.getCollectionLog().getContainer().contains(entry.getIntValue(), 1)) {
                player.getCollectionLog().add(new Item(entry.getIntValue()));
            }
        }
    }
}
