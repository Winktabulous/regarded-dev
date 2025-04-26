package com.zenyte.game.content.larranskey;

import com.near_reality.game.content.commands.DeveloperCommands;
import com.zenyte.CacheManager;
import com.zenyte.game.GameConstants;
import com.zenyte.game.content.rewards.Rewards;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.near_reality.game.world.info.WorldProfile;
import com.zenyte.game.world.object.WorldObject;
import mgi.tools.jagcached.cache.Cache;
import mgi.types.Definitions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class LarransKey {

    private static final String LARGE_CHEST_REWARDS_KEY = "larrans-large-chest-reward";
    private static final String SMALL_CHEST_REWARDS_KEY = "larrans-small-chest-reward";

    public static final String SMALL_CHEST_ATTRIBUTE = "larrans-chest-small";
    public static final String LARGE_CHEST_ATTRIBUTE = "larrans-chest-large";

    public static final int LARRANS_CHEST_SMALL_OBJECT_ID = 34831;
    public static final int LARRANS_CHEST_LARGE_OBJECT_ID = 34832;
    public static final int LARRANS_CHEST_SMALL_OBJECT_ID_OPEN = 34828;
    public static final int LARRANS_CHEST_LARGE_OBJECT_ID_OPEN = 34830;

    public static final Item LARRANS_KEY = new Item(ItemId.LARRANS_KEY, 1);

    public static void sendCheckMessage(Player player, boolean bigChest) {
        if (bigChest) {
            int bigChestCount = player.getNumericAttribute(LARGE_CHEST_ATTRIBUTE).intValue();
            player.sendMessage("You have opened Larran's big chest " + bigChestCount + " times.");
        } else {
            int smallChest = player.getNumericAttribute(SMALL_CHEST_ATTRIBUTE).intValue();
            player.sendMessage("You have opened Larran's small chest " + smallChest + " times.");
        }
    }

    public static void openChest(Player player, WorldObject chest) {

        if (!DeveloperCommands.INSTANCE.getEnabledLarranKeys()){
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    plain("Larran's key is currently disabled.");
                }
            });
            return;
        }

        if(!player.getInventory().containsItem(LARRANS_KEY.getId())) {
            player.sendMessage("You must have a Larran's Key to open this chest!");
            return;
        }

        player.lock(3);
        player.setAnimation(new Animation(827));

        String keyToUse = chest.getId() == LARRANS_CHEST_SMALL_OBJECT_ID ?
                SMALL_CHEST_REWARDS_KEY : LARGE_CHEST_REWARDS_KEY;

        if(chest.getId() == LARRANS_CHEST_SMALL_OBJECT_ID) {
            int current = player.getNumericAttribute(SMALL_CHEST_ATTRIBUTE).intValue();
            current++;
            player.getAttributes().put(SMALL_CHEST_ATTRIBUTE, current);
            sendCheckMessage(player, false);
        } else {
            int current = player.getNumericAttribute(LARGE_CHEST_ATTRIBUTE).intValue();
            current++;
            player.getAttributes().put(LARGE_CHEST_ATTRIBUTE, current);
            sendCheckMessage(player, true);
        }


        player.getActionManager().setAction(new Action() {
            int ticks = 0;

            @Override
            public boolean start() {
                return true;
            }

            @Override
            public boolean process() {
                if (ticks == 1) {

                    if(chest.getId() == LARRANS_CHEST_SMALL_OBJECT_ID) {
                        final WorldObject smallLarensChest = new WorldObject(LarransKey.LARRANS_CHEST_SMALL_OBJECT_ID_OPEN, 10, 1, 3281, 3659, 0);
                        World.spawnObject(smallLarensChest);
                    }

                    if (chest.getId() == LARRANS_CHEST_LARGE_OBJECT_ID) {
                        final WorldObject largeChest = new WorldObject(LarransKey.LARRANS_CHEST_LARGE_OBJECT_ID_OPEN, 10, 2, 3018, 3955, 1);
                        World.spawnObject(largeChest);
                    }
                    player.getInventory().deleteItem(LARRANS_KEY);
                    // Open object
                }
                if (ticks == 2) {
                    this.stop();
                    List<Item> rewards;
                    if (chest.getId() == LARRANS_CHEST_LARGE_OBJECT_ID) {
                        if (Utils.randomBoolean(105)) {
                            rewards = new ArrayList<>();
                            int rate = Utils.random(2);
                            switch (rate) {
                                case 0:
                                    rewards.add(new Item(ItemId.DAGONHAI_HAT));
                                    break;
                                case 1:
                                    rewards.add(new Item(ItemId.DAGONHAI_ROBE_TOP));
                                    break;
                                case 2:
                                    rewards.add(new Item(ItemId.DAGONHAI_ROBE_BOTTOM));
                                    break;
                            }
                            WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, rewards.get(0), "Larran's big chest");
                        } else if (Utils.randomBoolean(900)) {
                            rewards = new ArrayList<>();
                            int rate = Utils.random(3);
                            switch (rate) {
                                case 0:
                                    rewards.add(new Item(ItemId.VESTAS_LONGSWORD));
                                    break;
                                case 1:
                                    rewards.add(new Item(ItemId.STATIUSS_WARHAMMER));
                                    break;
                                case 2:
                                    rewards.add(new Item(ItemId.ZURIELS_STAFF));
                                    break;
                                case 3:
                                    rewards.add(new Item(ItemId.VESTAS_SPEAR));
                                    break;
                            }
                            WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, rewards.get(0), "Larran's big chest");
                        } else {
                            rewards = Rewards.generateRewards(keyToUse, 1);
                        }
                    } else {
                        rewards = Rewards.generateRewards(keyToUse, 1);
                    }

                    if(World.hasBoost(XamphurBoost.LARRANS_KEY_DROPS_X2)) {
                        player.sendMessage("You get double the loot because of the Xamphur boost!");
                        for (Item reward : rewards) {
                            reward.setAmount(reward.getAmount() * 2);
                        }
                    }

                    for (Item item : rewards) {
                        player.getInventory().addOrDrop(item);
                        player.getCollectionLog().add(item);
                    }
                    player.getInventory().addOrDrop(ItemId.BLOOD_MONEY, Utils.random(5, 25));
                    player.getInventory().addOrDrop(new Item(995, Utils.random(50_000, 250_000)));

                    if(chest.getId() == LARRANS_CHEST_SMALL_OBJECT_ID) {
                        final WorldObject smallLarensChest = new WorldObject(LarransKey.LARRANS_CHEST_SMALL_OBJECT_ID, 10, 1, 3281, 3659, 0);
                        World.spawnObject(smallLarensChest);
                    }
                    if (chest.getId() == LARRANS_CHEST_LARGE_OBJECT_ID) {
                        final WorldObject largeChest = new WorldObject(LarransKey.LARRANS_CHEST_LARGE_OBJECT_ID, 10, 2, 3018, 3955, 1);
                        World.spawnObject(largeChest);
                    }

                    return false;
                }
                ticks++;
                return true;
            }

            @Override
            public int processWithDelay() {
                return 0;
            }
        });
    }
//
//    public static void main(String[] args) throws IOException {
//        CacheManager.loadCache(Cache.openCache("cache/data/cache"));
//
//        Definitions.loadDefinitions(Definitions.lowPriorityDefinitions);
//        Definitions.loadDefinitions(Definitions.highPriorityDefinitions);
//        GameConstants.WORLD_PROFILE = new WorldProfile("localhost");
//        Rewards.load();
//
//        simulate(1000);
//        simulate(100_000);
//    }

    private static void simulate(int amount) {
        System.out.println("Simulating " + amount);
        Container container = new Container(ContainerType.BANK, ContainerPolicy.ALWAYS_STACK, 1000, Optional.empty());

        for (int i = 0; i < amount; i++) {
            List<Item> rewards;
            if (Utils.randomBoolean(105)) {
                rewards = new ArrayList<>();
                int rate = Utils.random(2);
                switch (rate) {
                    case 0:
                        rewards.add(new Item(ItemId.DAGONHAI_HAT));
                        break;
                    case 1:
                        rewards.add(new Item(ItemId.DAGONHAI_ROBE_TOP));
                        break;
                    case 2:
                        rewards.add(new Item(ItemId.DAGONHAI_ROBE_BOTTOM));
                        break;
                }
            } else if (Utils.randomBoolean(900)) {
                rewards = new ArrayList<>();
                int rate = Utils.random(3);
                switch (rate) {
                    case 0:
                        rewards.add(new Item(ItemId.VESTAS_LONGSWORD));
                        break;
                    case 1:
                        rewards.add(new Item(ItemId.STATIUSS_WARHAMMER));
                        break;
                    case 2:
                        rewards.add(new Item(ItemId.ZURIELS_STAFF));
                        break;
                    case 3:
                        rewards.add(new Item(ItemId.VESTAS_SPEAR));
                        break;
                }
            } else {
                rewards = Rewards.generateRewards(LARGE_CHEST_REWARDS_KEY, 1);
            }

            for (Item item : rewards) {
                container.add(item);
            }
        }

        for (Item item : container.getItems().values()) {
            System.out.println(item);
        }

        System.out.println("-------------------");
    }

}
