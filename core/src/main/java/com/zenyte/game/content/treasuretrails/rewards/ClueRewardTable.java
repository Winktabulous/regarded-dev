package com.zenyte.game.content.treasuretrails.rewards;

import com.google.common.collect.ImmutableList;
import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ImmutableItem;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import com.zenyte.plugins.renewednpc.MonkOfEntrana;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;
import org.jetbrains.annotations.NotNull;

import javax.print.attribute.standard.MediaSize;
import java.util.List;

import static com.zenyte.game.item.ItemId.*;

/**
 * @author Kris | 24/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class ClueRewardTable {
    protected final List<ImmutableItem> firelightersTable = tableOf(item(RED_FIRELIGHTER, 4, 10), item(GREEN_FIRELIGHTER, 4, 10), item(BLUE_FIRELIGHTER, 4, 10), item(PURPLE_FIRELIGHTER, 4, 10), item(WHITE_FIRELIGHTER, 4, 10));
    protected final List<ImmutableItem> teleportsTable = tableOf(item(CHARGE_DRAGONSTONE_JEWELLERY_SCROLL, 5, 15), item(NARDAH_TELEPORT, 5, 15), item(MOS_LEHARMLESS_TELEPORT, 5, 15), item(MORTTON_TELEPORT, 5, 15), item(FELDIP_HILLS_TELEPORT, 5, 15), item(LUNAR_ISLE_TELEPORT, 5, 15), item(DIGSITE_TELEPORT, 5, 15), item(PISCATORIS_TELEPORT, 5, 15), item(PEST_CONTROL_TELEPORT, 5, 15), item(TAI_BWO_WANNAI_TELEPORT, 5, 15), item(LUMBERYARD_TELEPORT, 5, 15));
    //TODO: Iorwerth camp teleport w/ Prifddinas upon revision upgrade
    protected final List<ImmutableItem> scrollbookTable = tableOf(item(MASTER_SCROLL_BOOK_EMPTY));
    protected final List<ImmutableItem> godPagesTable = tableOf(item(SARADOMIN_PAGE_1), item(SARADOMIN_PAGE_2), item(SARADOMIN_PAGE_3), item(SARADOMIN_PAGE_4), item(ZAMORAK_PAGE_1), item(ZAMORAK_PAGE_2), item(ZAMORAK_PAGE_3), item(ZAMORAK_PAGE_4), item(GUTHIX_PAGE_1), item(GUTHIX_PAGE_2), item(GUTHIX_PAGE_3), item(GUTHIX_PAGE_4), item(BANDOS_PAGE_1), item(BANDOS_PAGE_2), item(BANDOS_PAGE_3), item(BANDOS_PAGE_4), item(ARMADYL_PAGE_1), item(ARMADYL_PAGE_2), item(ARMADYL_PAGE_3), item(ARMADYL_PAGE_4), item(ANCIENT_PAGE_1), item(ANCIENT_PAGE_2), item(ANCIENT_PAGE_3), item(ANCIENT_PAGE_4));
    protected final List<ImmutableItem> blessingsTable = tableOf(item(HOLY_BLESSING), item(UNHOLY_BLESSING), item(PEACEFUL_BLESSING), item(WAR_BLESSING), item(HONOURABLE_BLESSING), item(ANCIENT_BLESSING));
    private final List<ImmutableItem> rewards = new ObjectArrayList<>();
    private final List<ImmutableItem> entranaRewards = new ObjectArrayList<>();
    private int totalWeight = 0;
    private int totalEntranaWeight = 0;

    public void calculate() {
        assert totalWeight == 0 : "Table has already been calculated.";
        defineTables();
        for (final ImmutableItem reward : rewards) {
            totalWeight += (int) reward.getRate();
        }
        for (final ImmutableItem reward : entranaRewards) {
            totalEntranaWeight += (int) reward.getRate();
        }
    }

    protected abstract void defineTables();

    protected void appendTable(final int rarity, final List<ImmutableItem> table) {
        final int percDecrease = rarity > 200000 ? 75 : rarity > 5000 ? 50 : rarity > 500 ? 10 : 0;
        final int adjustedRarity = (int) (rarity * (1 - (percDecrease / 100.0F)));
        final double probability = 1.0 / adjustedRarity;//Get the probability of hitting this table.
        final int weight = (int) (1000000 * probability);//Calculate a weight out of the probability, scaled to a weight of a million.
        for (final ImmutableItem item : table) {
            rewards.add(new ImmutableItem(item.getId(), item.getMinAmount(), item.getMaxAmount(), weight, adjustedRarity));
            if (!MonkOfEntrana.isForbiddenOnEntrana(new Item(item.getId()))) {
                entranaRewards.add(new ImmutableItem(item.getId(), item.getMinAmount(), item.getMaxAmount(), weight));
            }
        }
    }

    protected abstract int minRolls();

    protected abstract int maxRolls();

    public final List<Item> roll(final boolean entrana, boolean xamphurBoost, boolean clueBooster) {
        int min = minRolls();
        int max = maxRolls();
        if (xamphurBoost) {
            max *= 2;
        }

        if (clueBooster) {
            min += 2;
            max += 2;
        }

        return roll(min, max, entrana, true);
    }

    public final List<Item> roll(final int min, final int max, final boolean entrana, final boolean rollClue) {
        assert min <= max;
        final ObjectArrayList<Item> loot = new ObjectArrayList<Item>(max);
        final int count = Utils.random(min, max);
        for (int i = 0; i < count; i++) {
            final int roll = Utils.random(entrana ? totalEntranaWeight : totalWeight);
            int current = 0;
            for (final ImmutableItem item : (entrana ? entranaRewards : rewards)) {
                if ((current += (int) item.getRate()) >= roll) {
                    final int amount = Utils.random(item.getMinAmount(), item.getMaxAmount());
                    loot.add(new Item(item.getId(), amount));
                    if (item.getId() == ItemDefinitions.getOrThrow(SUPER_ATTACK4).getNotedOrDefault()) {
                        loot.add(new Item(ItemDefinitions.getOrThrow(SUPER_STRENGTH4).getNotedOrDefault(), amount));
                        loot.add(new Item(ItemDefinitions.getOrThrow(SUPER_DEFENCE4).getNotedOrDefault(), amount));
                    }
                    break;
                }
            }
        }
        if (rollClue) {
            final int masterScrollRate = getMasterScrollRate();
            if (masterScrollRate > 0 && Utils.random(masterScrollRate - 1) == 0) {
                loot.add(new Item(ClueItem.MASTER.getScrollBox()));
            }
        }
        return loot;
    }

    protected abstract int getMasterScrollRate();

    public void print() {
        for (final ImmutableItem reward : rewards) {
            System.err.println(reward.getId() + ", " + ItemDefinitions.getOrThrow(reward.getId()).getName() + ", " + (1.0 / (reward.getRate() / totalWeight)));
        }
    }
    ObjectArrayList<OtherDropViewerEntry> entries = new ObjectArrayList<>();

    public ObjectArrayList<OtherDropViewerEntry> toEntries() {
        if(entries.size() == 0)
            enumerateEntries();
        return entries;
    }

    private void enumerateEntries() {
        for (final ImmutableItem reward : rewards) {
            OtherDropViewerEntry entry = new OtherDropViewerEntry(reward.getId(), reward.getMinAmount(), reward.getMaxAmount(), reward.getRate(), totalWeight, "");
            entries.add(entry);
        }
    }

    protected final ImmutableItem item(final int id) {
        return item(id, 1);
    }

    protected final ImmutableItem item(final int id, final int amount) {
        return item(id, amount, amount);
    }

    protected final ImmutableItem item(final int id, final int minAmount, final int maxAmount) {
        assert minAmount <= maxAmount;
        assert minAmount >= 1;
        return new ImmutableItem(id, minAmount, maxAmount);
    }

    protected final ImmutableList<ImmutableItem> tableOf(@NotNull final ImmutableItem... items) {
        return ImmutableList.<ImmutableItem>builder().add(items).build();
    }

    protected final ImmutableItem notedItem(final int id, final int amount) {
        return notedItem(id, amount, amount);
    }

    protected final ImmutableItem notedItem(final int id, final int minAmount, final int maxAmount) {
        assert minAmount <= maxAmount;
        assert minAmount >= 1;
        final int notedId = ItemDefinitions.getOrThrow(id).getNotedOrDefault();
        return new ImmutableItem(notedId, minAmount, maxAmount);
    }

    public List<ImmutableItem> getRewards() {
        return rewards;
    }

    public List<ImmutableItem> getEntranaRewards() {
        return entranaRewards;
    }
}
