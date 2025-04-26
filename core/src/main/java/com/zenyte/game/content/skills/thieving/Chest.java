package com.zenyte.game.content.skills.thieving;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.content.drops.AllotmentSeedTable;
import com.zenyte.game.content.drops.CommonSeedTable;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.content.skills.thieving.tables.RoguesChestTable;
import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ImmutableItem;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.CharacterLoop;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.List;

/**
 * @author Kris | 21. okt 2017 : 19:28.03
 * @author Corey 23/11/19
 */
public enum Chest {
    COINS_10_CHEST(13, 12, 7.8, true, 11735, 11743, new ImmutableItem(995, 10)), NATURE_RUNE_CHEST(28, 25, 25, true, 11736, 11743, new ImmutableItem(995, 3), new ImmutableItem(561, 1, 2)), COINS_50_CHEST(43, 83, 125, true, 11737, 11743, new ImmutableItem(995, 50)), ARROW_TIP_CHEST(47, 350, 150, true, 11742, 11743, new ImmutableItem(41, 5), new ImmutableItem(995, 20)), 
    //    DORGESH_KAAN_CHEST(52, 350, 200, true, -1, -1, new ImmutableItem(995, 100, 300, 35), new ImmutableItem(4548, 1, 1, 16), new ImmutableItem(4537, 1, 1, 16), new ImmutableItem(10981, 1, 1, 16), new ImmutableItem(5013, 1, 1, 17)),
    BLOOD_RUNE_CHEST(59, 225, 250, true, 11738, 11743, new ImmutableItem(995, 500), new ImmutableItem(565, 2)) {
        @Override
        public void onSuccess(Player player) {
            WorldTasksManager.schedule(() -> {
                player.sendMessage("Suddenly a second magical trap triggers.");
                player.setAnimation(Animation.STOP);
                player.setLocation(new Location(2584, 3337));
            });
        }
    },
    STONE_CHEST(64, 1, 280, 34429, 34430, () -> {
        // https://oldschool.runescape.wiki/w/Stone_chest
        if (Utils.random(299) == 0) {
            return new Item[] {new Item(ItemId.XERICS_TALISMAN_INERT)};
        }
        if (Utils.random(99) == 0) {
            return new Item[] {new Item(ClueItem.MEDIUM.getScrollBox())};
        }
        final Item[] common = new Item[] {new ImmutableItem(ItemId.COINS_995, 30, 359).generateResult(), new Item(ItemId.XERICIAN_FABRIC, 1), new ImmutableItem(ItemId.LIZARDMAN_FANG, 1, 3).generateResult(), new Item(ItemId.UNCUT_SAPPHIRE, 1)};
        final Item[] uncommon = new Item[] {new Item(ItemId.UNCUT_RUBY, 1)};
        final Item[] boltTips = new Item[] {new ImmutableItem(ItemId.OPAL_BOLT_TIPS, 4, 12).generateResult(), new ImmutableItem(ItemId.JADE_BOLT_TIPS, 4, 12).generateResult(), new ImmutableItem(ItemId.PEARL_BOLT_TIPS, 4, 12).generateResult(), new ImmutableItem(ItemId.TOPAZ_BOLT_TIPS, 4, 12).generateResult(), new ImmutableItem(ItemId.SAPPHIRE_BOLT_TIPS, 4, 12).generateResult(), new ImmutableItem(ItemId.EMERALD_BOLT_TIPS, 4, 12).generateResult(), new ImmutableItem(ItemId.RUBY_BOLT_TIPS, 4, 12).generateResult(), new ImmutableItem(ItemId.DIAMOND_BOLT_TIPS, 4, 12).generateResult(), new ImmutableItem(ItemId.DRAGONSTONE_BOLT_TIPS, 4, 12).generateResult(), new ImmutableItem(ItemId.ONYX_BOLT_TIPS, 4, 12).generateResult()};
        Item[] tableResult;
        final int roll = Utils.random(100);
        if (roll <= 4) {
            tableResult = boltTips;
        } else if (roll <= 6) {
            return new Item[] {AllotmentSeedTable.roll()};
        } else if (roll <= 15) {
            return new Item[] {CommonSeedTable.roll()};
        } else if (roll <= 35) {
            tableResult = uncommon;
        } else {
            tableResult = common;
        }
        return new Item[] {tableResult[Utils.random(tableResult.length - 1)]};
    }) {
        @Override
        public void onFailure(Player player) {
            if (Utils.random(20) == 0) {
                WorldTasksManager.schedule(() -> player.sendFilteredMessage("You have activated a trap on the chest."));
                final Teleport teleport = new Teleport() {
                    @Override
                    public TeleportType getType() {
                        return TeleportType.XERICS_TELEPORT;
                    }
                    @Override
                    public Location getDestination() {
                        return new Location(1312, 3678, 0);
                    }
                    @Override
                    public int getLevel() {
                        return 0;
                    }
                    @Override
                    public double getExperience() {
                        return 0;
                    }
                    @Override
                    public int getRandomizationDistance() {
                        return 3;
                    }
                    @Override
                    public Item[] getRunes() {
                        return null;
                    }
                    @Override
                    public int getWildernessLevel() {
                        return 100;
                    }
                    @Override
                    public boolean isCombatRestricted() {
                        return false;
                    }
                };
                teleport.teleport(player);
            }
        }
    },
    PALADIN_CHEST(72, 667, 500, true, 11739, 11743, new ImmutableItem(995, 1000), new ImmutableItem(383), new ImmutableItem(449), new ImmutableItem(1623)) {
        @Override
        public void onSuccess(Player player) {
            WorldTasksManager.schedule(() -> {
                player.sendMessage("Suddenly a second magical trap triggers.");
                player.setAnimation(Animation.STOP);
                player.setLocation(new Location(2696, 3281));
            });
        }
    },
    
//    DORGESH_KAAN_RICH_CHEST(78, 500, 650, -1, -1, new ImmutableItem(995, 500, 2500), new ImmutableItem(1623), new ImmutableItem(1621), new ImmutableItem(1619), new ImmutableItem(1617), new ImmutableItem(1615), new ImmutableItem(1625), new ImmutableItem(1627), new ImmutableItem(1629), new ImmutableItem(4548), new ImmutableItem(5013), new ImmutableItem(10954), new ImmutableItem(10956), new ImmutableItem(10958), new ImmutableItem(2351), new ImmutableItem(10973), new ImmutableItem(10980)),
    ROGUES_CHEST(84, 6, 100, true, 26757, 11743, () -> {
        // https://oldschool.runescape.wiki/w/Chest_(Rogues%27_Castle)
        if (Utils.random(98) == 0) {
            return new Item[] {new Item(ClueItem.HARD.getScrollBox())};
        }
        return new Item[] {RoguesChestTable.roll()};
    }) {
        @Override
        public void onSuccess(Player player) {
            player.getAchievementDiaries().update(WildernessDiary.STEAL_FROM_ROGUES_CHEST);
            final List<NPC> list = CharacterLoop.find(player.getLocation(), 30, NPC.class, n -> {
                final String name = n.getDefinitions().getName().toLowerCase();
                return !n.isDead() && name.contains("rogue");
            });
            if (list.isEmpty()) {
                return;
            }
            for (final NPC npc : list) {
                npc.getCombat().setTarget(player);
                npc.setForceTalk(new ForceTalk("Someone's stealing from us, get them!"));
            }
        }
    };
    public static final Int2ObjectOpenHashMap<Chest> data = new Int2ObjectOpenHashMap<>();

    static {
        for (Chest data : values()) {
            Chest.data.put(data.getClosedId(), data);
        }
    }

    private final int level;
    private final int time;
    private final int closedId;
    private final int openId;
    private final double experience;
    private final boolean trapped;
    private final ImmutableItem[] loot;
    private final ChestLoot lootTable;

    Chest(final int level, final int time, final double experience, final boolean trapped, final int closedId, final int openId, final ImmutableItem... loot) {
        this.level = level;
        this.time = time;
        this.experience = experience;
        this.trapped = trapped;
        this.closedId = closedId;
        this.openId = openId;
        this.loot = loot;
        this.lootTable = null;
    }

    Chest(final int level, final int time, final double experience, final boolean trapped, final int closedId, final int openId, final ChestLoot lootTable) {
        this.level = level;
        this.time = time;
        this.experience = experience;
        this.trapped = trapped;
        this.closedId = closedId;
        this.openId = openId;
        this.loot = null;
        this.lootTable = lootTable;
    }

    Chest(final int level, final int time, final double experience, final int closedId, final int openId, final ChestLoot lootTable) {
        this.level = level;
        this.time = time;
        this.experience = experience;
        this.trapped = false;
        this.closedId = closedId;
        this.openId = openId;
        this.loot = null;
        this.lootTable = lootTable;
    }

    public static Chest getChest(final int objectId) {
        return data.get(objectId);
    }

    public void onSuccess(final Player player) {
    }

    public void onFailure(final Player player) {
    }

    public void onTriggerTrap(final Player player) {
        final int hitAmount = Utils.random(player.getMaxHitpoints() / 14, player.getMaxHitpoints() / 8);
        player.applyHit(new Hit(Math.max(hitAmount, 1), HitType.DEFAULT));
        player.sendSound(new SoundEffect(player.getDamageSound()));
        player.sendFilteredMessage("You have activated a trap on the chest.");
    }


    public interface ChestLoot {
        Item[] generateLoot();
    }

    public int getLevel() {
        return level;
    }

    public int getTime() {
        return time;
    }

    public int getClosedId() {
        return closedId;
    }

    public int getOpenId() {
        return openId;
    }

    public double getExperience() {
        return experience;
    }

    public boolean isTrapped() {
        return trapped;
    }

    public ImmutableItem[] getLoot() {
        return loot;
    }

    public ChestLoot getLootTable() {
        return lootTable;
    }
}
