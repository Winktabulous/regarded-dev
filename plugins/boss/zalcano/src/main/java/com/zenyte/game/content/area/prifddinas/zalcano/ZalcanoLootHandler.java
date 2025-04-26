package com.zenyte.game.content.area.prifddinas.zalcano;

import com.near_reality.game.content.crystal.CrystalShardKt;
import com.zenyte.CacheManager;
import com.zenyte.game.GameConstants;
import com.zenyte.game.content.rewards.Rewards;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.PlayerInformation;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.near_reality.game.world.info.WorldProfile;
import mgi.tools.jagcached.cache.Cache;
import mgi.types.Definitions;

import java.io.IOException;
import java.util.*;

public class ZalcanoLootHandler {

    private static final String DEFAULT_LOOT_KEY = "zalcano-default-loot";
    private static final String RARE_LOOT_KEY = "zalcano-rare-loot";

    private final ZalcanoInstance instance;
    private final Map<Player, Integer> healthDamage = new HashMap<>();
    private final Map<Player, Integer> shieldDamage = new HashMap<>();

    public ZalcanoLootHandler(ZalcanoInstance instance) {
        this.instance = instance;
    }

    /**
     * Adds damage to the player
     *
     * @param attacker
     * @param damage
     * @param isShieldDamage
     */
    public void addDamage(Player attacker, int damage, boolean isShieldDamage) {
        if (isShieldDamage) {
            var currentDamage = shieldDamage.getOrDefault(attacker, 0);
            currentDamage += damage;
            shieldDamage.put(attacker, currentDamage);
        } else {
            var currentDamage = healthDamage.getOrDefault(attacker, 0);
            currentDamage += damage;
            healthDamage.put(attacker, currentDamage);
        }
    }

    public void rewardPlayers(ZalcanoBoss boss, Location deathLocation) {
        /*healthDamage.entrySet().stream()
                .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .forEach(k -> System.out.println(k.getKey() + ": " + k.getValue()));

        shieldDamage.entrySet().stream()
                .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .forEach(k -> System.out.println(k.getKey() + ": " + k.getValue()));*/

        for (Player player : instance.getPlayers()) {
            player.getNotificationSettings().increaseKill("zalcano");
            player.getNotificationSettings().sendBossKillCountNotification("zalcano");
            player.getCombatAchievements().checkKcTask("zalcano", 25, CAType.ZALCANO_VETERAN);
            int perfectKc = player.getCombatAchievements().getCurrentTaskValue(CAType.PERFECT_ZALCANO) + 1;
            if (perfectKc >= 5) {
                player.getCombatAchievements().complete(CAType.PERFECT_ZALCANO);
            }
            player.getCombatAchievements().setCurrentTaskValue(CAType.PERFECT_ZALCANO, perfectKc);
            List<Item> dropForPlayer = rewardPlayer(player);
            for (Item item : dropForPlayer) {
                boss.dropItem(player, item);
                player.getCollectionLog().add(item);
            }
        }
    }

    private List<Item> rewardPlayer(Player player) {
        if (!hasCriteria(player, 30, 30)) {
            player.sendMessage("You did not to enough damage to Zalcano to be rewarded.");
            return null;
        }

        int shardsToAdd = 1;
        List<Item> dropForPlayer = new ArrayList<>();
        boolean boostedLoot = World.hasBoost(XamphurBoost.BONUS_ZALCANO_LOOT);
        if (hasCriteria(player, 50, 50)) {
            dropForPlayer.addAll(Rewards.generateRewards(RARE_LOOT_KEY, boostedLoot ? 2 : 1));
            shardsToAdd = 2;
        }

        Player mvp = totalDamageMap().keySet().iterator().next();
        if (player.equals(mvp)) {
            shardsToAdd = 3;
            dropForPlayer.add(new Item(ZalcanoConstants.INFERNAL_ASHES_ITEM));
            mvp.getCombatAchievements().complete(CAType.THE_SPURNED_HERO);
        }

        if(boostedLoot) {
            shardsToAdd *= 2;
        }

        dropForPlayer.addAll(Rewards.generateRewards(DEFAULT_LOOT_KEY, 1));
        dropForPlayer.add(new Item(CrystalShardKt.CRYSTAL_SHARD, shardsToAdd));

        return dropForPlayer;
    }

//    public static void main(String[] args) throws IOException {
//       CacheManager.loadCache(Cache.openCache("cache/data/cache"));
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
        Player player = new Player(new PlayerInformation("jacmob", "", 0, null, null), null);
        ZalcanoLootHandler zalcanoLootHandler = new ZalcanoLootHandler(null);
        zalcanoLootHandler.healthDamage.put(player, 100);
        zalcanoLootHandler.shieldDamage.put(player, 100);

        for (int i = 0; i < amount; i++) {
            List<Item> items = zalcanoLootHandler.rewardPlayer(player);
            for (Item item : items) {
                container.add(item);
            }
        }

        for (Item item : container.getItems().values()) {
            System.out.println(item);
        }

        System.out.println("-------------------");
    }

    private Map<Player, Integer> totalDamageMap() {
        var newMap = new HashMap<Player, Integer>();
        for (var entry : healthDamage.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue());
        }

        for (var entry : shieldDamage.entrySet()) {
            if (newMap.containsKey(entry.getKey())) {
                var currentDamage = newMap.getOrDefault(entry.getKey(), 0);
                newMap.put(entry.getKey(), currentDamage);
            }
        }
        return newMap;
    }

    private boolean hasCriteria(Player player, int shieldDamageCriteria, int healthDamageCriteria) {
        var currentShieldDamage = shieldDamage.getOrDefault(player, 0);
        var currentHealthDamage = healthDamage.getOrDefault(player, 0);
        return currentHealthDamage >= healthDamageCriteria && currentShieldDamage >= shieldDamageCriteria;
    }

}
