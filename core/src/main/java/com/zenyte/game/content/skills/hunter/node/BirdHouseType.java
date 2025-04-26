package com.zenyte.game.content.skills.hunter.node;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.utils.Ordinal;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Kris | 25/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@Ordinal
public enum BirdHouseType {
    REGULAR(5, 5, 15, 28, 0.2, ItemId.BIRD_HOUSE, ItemId.LOGS),
    OAK(15, 14, 20, 42, 0.25, ItemId.OAK_BIRD_HOUSE, ItemId.OAK_LOGS),
    WILLOW(25, 24, 25, 56, 0.3, ItemId.WILLOW_BIRD_HOUSE, ItemId.WILLOW_LOGS),
    TEAK(35, 34, 30, 70, 0.35, ItemId.TEAK_BIRD_HOUSE, ItemId.TEAK_LOGS),
    MAPLE(45, 44, 35, 82, 0.4, ItemId.MAPLE_BIRD_HOUSE, ItemId.MAPLE_LOGS),
    MAHOGANY(50, 49, 40, 96, 0.45, ItemId.MAHOGANY_BIRD_HOUSE, ItemId.MAHOGANY_LOGS),
    YEW(60, 59, 45, 102, 0.5, ItemId.YEW_BIRD_HOUSE, ItemId.YEW_LOGS),
    MAGIC(75, 74, 50, 114, 0.55, ItemId.MAGIC_BIRD_HOUSE, ItemId.MAGIC_LOGS),
    REDWOOD(90, 89, 55, 120, 0.6, ItemId.REDWOOD_BIRD_HOUSE, ItemId.REDWOOD_LOGS);
    private final int craftingRequirement;
    private final int hunterRequirement;
    private final double craftingExperience;
    private final double hunterExperience;
    private final double chanceOfNest;
    private final int birdhouseId;
    private final int logsId;
    private static final List<BirdHouseType> values = Collections.unmodifiableList(Arrays.asList(values()));
    private static final List<Item> ambiguousBirdhouseMenu;

    static {
        final ObjectArrayList<Item> list = new ObjectArrayList<Item>();
        for (final BirdHouseType value : values) {
            list.add(new Item(value.birdhouseId));
        }
        ambiguousBirdhouseMenu = Collections.unmodifiableList(list);
    }

    public static final Optional<BirdHouseType> findThroughBirdhouse(final int id) {
        return Optional.ofNullable(CollectionUtils.findMatching(values, value -> value.birdhouseId == id));
    }

    public static final Optional<BirdHouseType> findThroughLogs(final int id) {
        return Optional.ofNullable(CollectionUtils.findMatching(values, value -> value.logsId == id));
    }

    public final int getCreateableAmountThroughMaterials(@NotNull final Player player) {
        final Inventory inventory = player.getInventory();
        return Math.min(inventory.getAmountOf(ItemId.CLOCKWORK), inventory.getAmountOf(logsId));
    }

    BirdHouseType(int craftingRequirement, int hunterRequirement, double craftingExperience, double hunterExperience, double chanceOfNest, int birdhouseId, int logsId) {
        this.craftingRequirement = craftingRequirement;
        this.hunterRequirement = hunterRequirement;
        this.craftingExperience = craftingExperience;
        this.hunterExperience = hunterExperience;
        this.chanceOfNest = chanceOfNest;
        this.birdhouseId = birdhouseId;
        this.logsId = logsId;
    }

    public static List<BirdHouseType> getValues() {
        return values;
    }

    public static List<Item> getAmbiguousBirdhouseMenu() {
        return ambiguousBirdhouseMenu;
    }

    public int getCraftingRequirement() {
        return craftingRequirement;
    }

    public int getHunterRequirement() {
        return hunterRequirement;
    }

    public double getCraftingExperience() {
        return craftingExperience;
    }

    public double getHunterExperience() {
        return hunterExperience;
    }

    public double getChanceOfNest() {
        return chanceOfNest;
    }

    public int getBirdhouseId() {
        return birdhouseId;
    }

    public int getLogsId() {
        return logsId;
    }
}
