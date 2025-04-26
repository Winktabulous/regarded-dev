package com.zenyte.game.content.chambersofxeric.skills;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.plugins.dialogue.SkillDialogue;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

/**
 * @author Kris | 2. mai 2018 : 22:17:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RaidHerblore {

    /**
     * The raid brewable potions enum, containing every combination and the necessary ingredients to create them.
     */
    public enum RaidPotion {
        WEAK_ELDER_POTION(47, 6, 20916, 20910, RaidHerb.GOLPAR), WEAK_TWISTED_POTION(47, 6, 20928, 20912, RaidHerb.GOLPAR), WEAK_KODAI_POTION(47, 6, 20940, 20911, RaidHerb.GOLPAR), ELDER_POTION(59, 9, 20920, 20910, RaidHerb.GOLPAR), TWISTED_POTION(59, 9, 20932, 20912, RaidHerb.GOLPAR), KODAI_POTION(59, 9, 20944, 20911, RaidHerb.GOLPAR), STRONG_ELDER_POTION(70, 13, 20924, 20910, RaidHerb.GOLPAR), STRONG_TWISTED_POTION(70, 13, 20936, 20912, RaidHerb.GOLPAR), STRONG_KODAI_POTION(70, 13, 20948, 20911, RaidHerb.GOLPAR), WEAK_REVITALISATION(52, 10, 20952, 20910, RaidHerb.BUCHU), WEAK_PRAYER_ENHANCE(52, 10, 20964, 20912, RaidHerb.BUCHU), WEAK_XERICS_AID(52, 10, 20976, 20911, RaidHerb.BUCHU), REVITALISATION(65, 17, 20956, 20910, RaidHerb.BUCHU), PRAYER_ENHANCE(65, 17, 20968, 20912, RaidHerb.BUCHU), XERICS_AID(65, 17, 20980, 20911, RaidHerb.BUCHU), STRONG_REVITALISATION(78, 26.5, 20960, 20910, RaidHerb.BUCHU), STRONG_PRAYER_ENHANCE(78, 26.5, 20972, 20912, RaidHerb.BUCHU), STRONG_XERICS_AID(78, 26.5, 20984, 20911, RaidHerb.BUCHU), WEAK_OVERLOAD(60, 30, 20988, new int[] {20916, 20928, 20940}, RaidHerb.NOXIFER), OVERLOAD(75, 50, 20992, new int[] {20920, 20932, 20944}, RaidHerb.NOXIFER), STRONG_OVERLOAD(90, 70, 20996, new int[] {20924, 20936, 20948}, RaidHerb.NOXIFER);
        private static final RaidPotion[] values = values();
        private static final Int2ObjectMap<RaidPotion> map = new Int2ObjectOpenHashMap<>(values.length * 4);
        /**
         * The grouped potions. The potions in each of the groups require the same ingredients, but rely on the player's herblore level to differentiate whichever potion the player
         * will brew. The higher their level, the better potion the player would brew. The player cannot make lower tier potions than what their tier grants them.
         */
        private static final RaidPotion[][] groupedValues = new RaidPotion[][] {new RaidPotion[] {WEAK_ELDER_POTION, ELDER_POTION, STRONG_ELDER_POTION}, new RaidPotion[] {WEAK_TWISTED_POTION, TWISTED_POTION, STRONG_TWISTED_POTION}, new RaidPotion[] {WEAK_KODAI_POTION, KODAI_POTION, STRONG_KODAI_POTION}, new RaidPotion[] {WEAK_REVITALISATION, REVITALISATION, STRONG_REVITALISATION}, new RaidPotion[] {WEAK_PRAYER_ENHANCE, PRAYER_ENHANCE, STRONG_PRAYER_ENHANCE}, new RaidPotion[] {WEAK_XERICS_AID, XERICS_AID, STRONG_XERICS_AID}, new RaidPotion[] {WEAK_OVERLOAD, OVERLOAD, STRONG_OVERLOAD}};

        static {
            for (final RaidPotion pot : values) {
                map.put(pot.potionId, pot);
            }
        }

        /**
         * The experience granted for brewing the potions.
         */
        private final double xp;
        /**
         * The herblore level required to brew the potion.
         */
        private final int level;
        /**
         * The item id of the potion being brewed.
         */
        private final int potionId;
        /**
         * An array of item ids containing the necessary secondary ingredients required to make the potion.
         */
        private final int[] secondaryIngredient;
        /**
         * The herb necessary to brew this potions.
         */
        private final RaidHerb herb;

        RaidPotion(final int level, final double xp, final int potionId, final int secondaryIngredient, final RaidHerb herb) {
            this(level, xp, potionId, new int[] {secondaryIngredient}, herb);
        }

        RaidPotion(final int level, final double xp, final int potionId, final int[] secondaryIngredient, final RaidHerb herb) {
            this.level = level;
            this.xp = xp;
            this.potionId = potionId;
            this.secondaryIngredient = secondaryIngredient;
            this.herb = herb;
        }

        /**
         * Calculates an array of potions that the player can brew based on the items used together.
         *
         * @param player   the player attempting to brew the potions.
         * @param used     the item used on the other item.
         * @param usedWith the item the other item was being used on.
         * @return an array of potions, can be empty if none is found.
         */
        @NotNull
        public static final Item[] getPotions(@NotNull final Player player, @NotNull final Item used, @NotNull final Item usedWith) {
            final Item vial = used.getId() == 20801 ? used : usedWith.getId() == 20801 ? usedWith : null;
            final Item other = vial == null ? null : vial == used ? usedWith : used;
            final ObjectArrayList<Item> list = new ObjectArrayList<Item>();
            final int level = player.getSkills().getLevel(SkillConstants.HERBLORE);
            for (final RaidHerblore.RaidPotion[] pots : RaidPotion.groupedValues) {
                RaidPotion potion = null;
                for (final RaidHerblore.RaidPotion pot : pots) {
                    if ((vial != null && (pot.herb.clean == other.getId() || ArrayUtils.contains(pot.secondaryIngredient, other.getId()))) || (pot.herb.clean == used.getId() && ArrayUtils.contains(pot.secondaryIngredient, usedWith.getId()) || pot.herb.clean == usedWith.getId() && ArrayUtils.contains(pot.secondaryIngredient, used.getId()))) {
                        if (potion == null) {
                            potion = pot;
                            continue;
                        }
                        if (level >= pot.level) {
                            potion = pot;
                        }
                    }
                }
                if (potion != null) {
                    final ArrayList<Item> requiredItems = new ArrayList<Item>();
                    for (final int secondaries : potion.secondaryIngredient) {
                        requiredItems.add(new Item(secondaries));
                    }
                    requiredItems.add(new Item(potion.herb.clean));
                    if (potion.secondaryIngredient.length <= 1) {
                        requiredItems.add(new Item(20801));
                    }
                    if (!player.getInventory().containsItems(requiredItems)) {
                        continue;
                    }
                    list.add(new Item(potion.potionId));
                }
            }
            return list.toArray(new Item[0]);
        }

        public double getXp() {
            return xp;
        }

        public int getLevel() {
            return level;
        }

        public int getPotionId() {
            return potionId;
        }

        public int[] getSecondaryIngredient() {
            return secondaryIngredient;
        }

        public RaidHerb getHerb() {
            return herb;
        }
    }


    /**
     * An enum containing all the grimy herbs that can be used in raid herbloring.
     */
    public enum RaidHerb {
        GOLPAR(27, 20904, 20905, 8), BUCHU(39, 20907, 20908, 9), NOXIFER(55, 20901, 20902, 11);
        public static final RaidHerb[] values = values();
        private static final Int2ObjectMap<RaidHerb> map = new Int2ObjectOpenHashMap<>();

        static {
            for (final RaidHerb herb : values) {
                map.put(herb.grimy, herb);
            }
        }

        /**
         * The level requirement to clean the grimy herb.
         */
        private final int level;
        /**
         * The item id of the grimy herb.
         */
        private final int grimy;
        /**
         * The item id of the cleaned herb.
         */
        private final int clean;
        /**
         * The experience granted for cleaning the grimy herb.
         */
        private final int xp;

        RaidHerb(int level, int grimy, int clean, int xp) {
            this.level = level;
            this.grimy = grimy;
            this.clean = clean;
            this.xp = xp;
        }

        public int getLevel() {
            return level;
        }

        public int getGrimy() {
            return grimy;
        }

        public int getClean() {
            return clean;
        }

        public int getXp() {
            return xp;
        }
    }


    /**
     * The action executed when a player begins cleaning herbs.
     */
    public static final class RaidHerbCleaningAction extends Action {
        /**
         * The sound effect played when a player cleans a herb.
         */
        private static final SoundEffect sound = new SoundEffect(3923);
        /**
         * The id of the herb the player began cleaning.
         */
        private final int id;

        /**
         * Cleans a herb, or whatever herb is in the inventory slot defined by the optional slot id.
         *
         * @param player the player cleaning the herb.
         * @param id     the item id of the herb.
         * @param slot   the optional slot if specifically cleaning a herb at whatever slot the player clicked in; if empty, cleans the first result in inventory.
         * @return whether or not a/the herb was cleaned.
         */
        public static final boolean clean(@NotNull final Player player, final int id, @NotNull final OptionalInt slot) {
            final RaidHerblore.RaidHerb raidHerb = RaidHerb.map.get(id);
            if (raidHerb == null) {
                return false;
            }
            if (player.getSkills().getLevel(SkillConstants.HERBLORE) < raidHerb.level) {
                player.sendMessage("You need a Herblore level of at least " + raidHerb.level + " to clean a " + raidHerb.toString().toLowerCase() + " leaf.");
                return false;
            }
            if (slot.isPresent()) {
                player.getInventory().replaceItem(raidHerb.clean, 1, slot.getAsInt());
            } else {
                final int result = player.getInventory().deleteItem(id, 1).getSucceededAmount();
                if (result == 0) {
                    return false;
                }
                player.getInventory().addItem(raidHerb.clean, 1);
            }
            player.sendSound(sound);
            player.getSkills().addXp(SkillConstants.HERBLORE, raidHerb.xp);
            player.sendFilteredMessage("You clean the " + raidHerb.toString().toLowerCase() + ".");
            return true;
        }

        @Override
        public boolean start() {
            delay(2);
            return true;
        }

        @Override
        public boolean process() {
            return player.getInventory().containsItem(id, 1);
        }

        @Override
        public int processWithDelay() {
            if (!clean(player, id, OptionalInt.empty())) {
                return -1;
            }
            return 2;
        }

        public RaidHerbCleaningAction(int id) {
            this.id = id;
        }
    }


    /**
     * The potion brewing skill dialogue, displaying all the potions the player can create based on the contents of their inventory.
     */
    public static final class RaidMixDialogue extends SkillDialogue {
        public RaidMixDialogue(final Player player, final Item[] items) {
            super(player, items);
        }

        @Override
        public void run(final int slotId, final int amount) {
            if (slotId < 0 || slotId >= items.length) {
                return;
            }
            final Item item = items[slotId];
            if (item == null) {
                return;
            }
            final RaidHerblore.RaidPotion pot = RaidPotion.map.get(item.getId());
            if (pot == null) {
                return;
            }
            if (amount <= 0) {
                return;
            }
            player.getActionManager().setAction(new RaidPotionAction(pot, amount));
        }
    }


    /**
     * The potion brewing action.
     */
    private static final class RaidPotionAction extends Action {
        /**
         * The sound effect played when a player mixes a potion together.
         */
        private static final SoundEffect brewingSound = new SoundEffect(2608);
        /**
         * The animation played when a player mixes a potion together.
         */
        private static final Animation brewingAnimation = new Animation(363);
        /**
         * The potion constant that the player is brewing.
         */
        private final RaidPotion potion;
        /**
         * The number of potions that the player requested to brew.
         */
        private int amount;

        RaidPotionAction(final RaidPotion potion, final int amount) {
            this.potion = potion;
            this.amount = amount;
        }

        @Override
        public boolean start() {
            if (player.getSkills().getLevel(SkillConstants.HERBLORE) < potion.level) {
                player.sendMessage("You need a Herblore level of at least " + potion.level + " to mix a " + potion.toString().toLowerCase() + ".");
                return false;
            }
            return check();
        }

        /**
         * Checks whether or not the player has th enecessary ingredients to brew a potion, and builds a message if the player fails to meet the requiremernts, with the missing
         * requirements listed out.
         *
         * @return whether or nor the player can continue brewing.
         */
        private boolean check() {
            final Inventory inventory = player.getInventory();
            List<String> list = null;
            if (!inventory.containsItem(potion.herb.clean, 1)) {
                list = new ObjectArrayList<>();
                list.add(ItemDefinitions.getOrThrow(potion.herb.clean).getName().toLowerCase());
            }
            for (final int ing : potion.secondaryIngredient) {
                if (!inventory.containsItem(ing, 1)) {
                    if (list == null) {
                        list = new ObjectArrayList<>();
                    }
                    list.add(ItemDefinitions.getOrThrow(ing).getName().toLowerCase());
                }
            }
            if (potion.secondaryIngredient.length <= 1) {
                if (!inventory.containsItem(20801, 1)) {
                    if (list == null) {
                        list = new ObjectArrayList<>();
                    }
                    list.add(ItemDefinitions.getOrThrow(20801).getName().toLowerCase());
                }
            }
            if (list != null && !list.isEmpty()) {
                final StringBuilder builder = new StringBuilder();
                builder.append("You need ");
                int index = 0;
                for (final String missingIngredients : list) {
                    builder.append(Utils.startWithVowel(missingIngredients) ? "an " : "a ");
                    if (index == list.size() - 2) {
                        builder.append(missingIngredients).append(" and ");
                    } else {
                        builder.append(missingIngredients).append(", ");
                    }
                    index++;
                }
                builder.delete(builder.length() - 2, builder.length());
                final String potName = potion.toString().toLowerCase().replace("_", " ");
                builder.append(" to mix ").append(Utils.startWithVowel(potName) ? "an" : "a").append(" ").append(potName).append(".");
                player.sendMessage(builder.toString());
                return false;
            }
            return true;
        }

        @Override
        public boolean process() {
            return true;
        }

        @Override
        public int processWithDelay() {
            final Inventory inventory = player.getInventory();
            player.setAnimation(brewingAnimation);
            player.sendSound(brewingSound);
            inventory.deleteItem(potion.herb.clean, 1);
            inventory.deleteItem(20801, 1);
            for (final int ing : potion.secondaryIngredient) {
                inventory.deleteItem(ing, 1);
            }
            final Item pot = new Item(potion.potionId, 1);
            pot.setAttribute("brewer", player.getUsername());
            inventory.addItem(pot);
            player.getSkills().addXp(SkillConstants.HERBLORE, potion.xp);
            final StringBuilder builder = new StringBuilder();
            for (int i = 0, length = potion.secondaryIngredient.length; i < length; i++) {
                builder.append(ItemDefinitions.getOrThrow(potion.secondaryIngredient[i]).getName().toLowerCase());
                if (length == 3) {
                    if (i == 2) {
                        continue;
                    }
                    builder.append(i == 0 ? ", " : " and ");
                }
            }
            player.sendFilteredMessage("You mix your " + ItemDefinitions.getOrThrow(potion.herb.clean).getName().toLowerCase() + " together with your " + builder + " into a potion perfectly!");
            if (--amount <= 0) {
                return -1;
            }
            if (!check()) {
                return -1;
            }
            return 1;
        }
    }
}
