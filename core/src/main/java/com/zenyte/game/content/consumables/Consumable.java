package com.zenyte.game.content.consumables;

import com.near_reality.game.content.consumables.drinks.DivinePotion;
import com.zenyte.game.content.consumables.drinks.*;
import com.zenyte.game.content.consumables.edibles.Food;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 12/11/2018 22:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface Consumable {
    static Restoration[] getRestorations(final float percentage, final int amount, final int... skills) {
        final Restoration[] restorations = new Restoration[skills.length];
        for (int i = 0; i < skills.length; i++) {
            restorations[i] = new Restoration(skills[i], percentage, amount);
        }
        return restorations;
    }

    int healedAmount(Player player);

    void heal(final Player player);

    Item leftoverItem(int id);

    Boost[] boosts();

    Animation animation();

    int delay();

    String startMessage();

    String endMessage(Player player);

    void onConsumption(final Player player);

    @SuppressWarnings("all")
    boolean canConsume(final Player player);

    void consume(final Player player, final Item item, final int slotId);

    void applyEffects(final Player player);

    Int2ObjectOpenHashMap<Consumable> consumables = new Int2ObjectOpenHashMap<>(500);
    Int2ObjectOpenHashMap<Consumable> food = new Int2ObjectOpenHashMap<>((int) (Food.values.length / 0.75F));
    Int2ObjectOpenHashMap<Consumable> gourdDrinks = new Int2ObjectOpenHashMap<>(500);

    /**
     * Initializes all of the consumable items in the game; this is necessary to be done manually
     * because static blocks are only initialized when the respective class is referenced, and they are not
     * allowed inside interfaces, so therefore if we add static blocks in classes like Food and others, and
     * reference {@link Consumable#consumables} through that, those static blocks would not be initialized,
     * as those classes aren't actually referenced, returning us with an empty map.
     */
    static void initialize() {
        for (final Food food : Food.values) {
            consumables.put(food.getId(), food);
            Consumable.food.put(food.getId(), food);
        }
        addAll(Ale.values);
        addAll(Drink.values);
        addAll(Potion.values);
        addAll(BarbarianMix.values);
        for (final GourdPotion drink : GourdPotion.values) {
            for (final int id : drink.getIds()) {
                consumables.put(id, drink);
                gourdDrinks.put(id, drink);
            }
        }
        addAll(KegAle.values);
        addAll(DivinePotion.Companion.getAll());
    }

    private static void addAll(Drinkable[] drinks) {
        for (Drinkable drink : drinks) {
            for (final int id : drink.getIds()) {
                consumables.put(id, drink);
            }
        }
    }


    class Restoration extends Boost {
        public Restoration(final int skill, final float percentage, final int amount) {
            super(skill, percentage, amount);
        }

        @Override
        public void apply(final Player player) {
            final int realLevel = getBaseLevel(player);
            final int currentLevel = getCurrentLevel(player);
            if (currentLevel >= realLevel) return;
            float boostedPercentage = percentage;
            if (skill == SkillConstants.PRAYER && hasPrayerBoostItem(player)) {
                boostedPercentage += 0.02F;
            }
            final int boostedLevel = (int) (currentLevel + amount + (realLevel * boostedPercentage));
            player.getSkills().setLevel(skill, Math.min(realLevel, boostedLevel));
        }

        private boolean hasPrayerBoostItem(@NotNull final Player player) {
            return player.carryingItem(ItemId.HOLY_WRENCH) || player.carryingItem(ItemId.RING_OF_THE_GODS_I) || SkillcapePerk.PRAYER.isCarrying(player);
        }
    }


    class Debuff extends Boost {
        public Debuff(final int skill, final float percentage, final int amount) {
            super(skill, percentage, amount);
        }

        @Override
        public void apply(final Player player) {
            int level = getCurrentLevel(player);
            final int boostedLevel = (int) (level - amount - Math.floor(level * percentage));
            player.getSkills().setLevel(skill, Math.max(0, boostedLevel));
        }
    }

    class CappedBoost extends Boost {
        private final float capPercentage;
        private final int capAmount;

        public CappedBoost(final int skill, final float percentage, final int amount, final float capPercentage, final int capAmount) {
            super(skill, percentage, amount);
            this.capPercentage = capPercentage;
            this.capAmount = capAmount;
        }

        @Override
        public void apply(final Player player) {
            final int current = getCurrentLevel(player);
            final int max = getBaseLevel(player);
            final int delta = calculateDelta(max, percentage, amount);
            final int cap = calculateDelta(max, capPercentage, capAmount);

            final int level;
            if (delta + current <= max + cap) {
                level = delta;
            } else {
                level = max + cap - current;
            }

            player.getSkills().setLevel(skill, current + level);
        }

        private int calculateDelta(int max, float perc, int delta) {
            return (((int) (max * perc)) * (delta >= 0 ? 1 : -1)) + delta;
        }

    }


    class Boost {
        public Boost(final int skill, final float percentage, final int amount) {
            this.skill = skill;
            this.percentage = percentage;
            this.amount = amount;
        }

        final int skill;
        final float percentage;
        final int amount;

        public void apply(final Player player) {
            final int realLevel = getBaseLevel(player);
            final int currentLevel = getCurrentLevel(player);
            final int boostedLevel = getBoostedLevel(realLevel, currentLevel);
            if (boostedLevel < currentLevel) return;
            player.getSkills().setLevel(skill, boostedLevel);
        }

        public int getBoostedLevel(Player player) {
            return getBoostedLevel(getBaseLevel(player), getCurrentLevel(player));
        }

        private int getBoostedLevel(int realLevel, int currentLevel) {
            return (int) (Math.min(currentLevel, realLevel) + amount + (realLevel * percentage));
        }

        public int getBaseLevel(final Player player) {
            return player.getSkills().getLevelForXp(skill);
        }

        public int getCurrentLevel(final Player player) {
            return player.getSkills().getLevel(skill);
        }

        public int getSkill() {
            return skill;
        }
    }
}
