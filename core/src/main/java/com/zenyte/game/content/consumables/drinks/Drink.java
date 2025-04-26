package com.zenyte.game.content.consumables.drinks;

import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.consumables.ConsumableAnimation;
import com.zenyte.game.content.consumables.Drinkable;
import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.GameSetting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.DrinkablePlugin;

/**
 * @author Kris | 02/12/2018 12:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum Drink implements Drinkable {
    POISON_CHALICE(ItemId.POISON_CHALICE, cocktailGlass, "You drink the strange green liquid.", "It has a slight taste of apricot."), //TODO: Has a variety of effects.
    KARAMJAN_RUM(ItemId.KARAMJAN_RUM, null, "You drink the rum. You feel slightly reinvigorated...", "...and slightly dizzy too.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Debuff(SkillConstants.ATTACK, 0, 4), new Debuff(SkillConstants.STRENGTH, 0, 5)), BLAMISH_OIL(ItemId.BLAMISH_OIL, null, null, null) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("You know... I'd really rather not.");
            return false;
        }
    },
    MULLED_WINE(ChristmasConstants.MULLED_WINE, null, "You drink the mulled wine.", "Mmm, that tastes good - nice and fruity. It reminds me of lazy afternoons by the fire."), CHOCOLATEY_MILK(ItemId.CHOCOLATEY_MILK, new Item(ItemId.BUCKET), "You drink the chocolatey milk.", null, new Restoration(SkillConstants.HITPOINTS, 0, 4)), HALF_FULL_WINE_JUG(ItemId.HALF_FULL_WINE_JUG, jug, "It makes you feel a bit dizzy.", null, new Restoration(SkillConstants.HITPOINTS, 0, 7)), JUG_OF_BAD_WINE(ItemId.JUG_OF_BAD_WINE, jug, "It makes you feel a bit dizzy.", null, new Debuff(SkillConstants.ATTACK, 0, 3)), JUG_OF_WINE(ItemId.JUG_OF_WINE, jug, "It makes you feel a bit dizzy.", null, new Restoration(SkillConstants.HITPOINTS, 0, 11), new Debuff(SkillConstants.ATTACK, 0, 2)), VODKA(ItemId.VODKA, null, "You drink the vodka. You feel slightly reinvigorated...", "...and slightly dizzy too.", new Boost(SkillConstants.STRENGTH, 0, 3), new Debuff(SkillConstants.ATTACK, 0, 3)), WHISKY(ItemId.WHISKY, null, "You drink the whisky. You feel slightly reinvigorated...", "...and slightly dizzy too.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 3), new Debuff(SkillConstants.ATTACK, 0, 4)), GIN(ItemId.GIN, null, "You drink the gin. You feel slightly reinvigorated...", "...and slightly dizzy too.", new Restoration(SkillConstants.HITPOINTS, 0, 2), new Boost(SkillConstants.STRENGTH, 0, 1), new Debuff(SkillConstants.ATTACK, 0, 4)), BRANDY(ItemId.BRANDY, null, "You drink the brandy. You feel slightly reinvigorated...", "...and slightly dizzy too.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Debuff(SkillConstants.ATTACK, 0, 3), new Debuff(SkillConstants.STRENGTH, 0, 3)), BRAINDEATH_RUM(ItemId.BRAINDEATH_RUM, null, "With a sense of impending doom you drink the 'rum'. You try very hard not to die.", null, new Debuff(SkillConstants.DEFENCE, 0.1F, 0), new Debuff(SkillConstants.ATTACK, 0.05F, 0), new Debuff(SkillConstants.PRAYER, 0.05F, 0), new Debuff(SkillConstants.MAGIC, 0.05F, 0), new Debuff(SkillConstants.AGILITY, 0.05F, 0), new Debuff(SkillConstants.HERBLORE, 0.05F, 0), new Boost(SkillConstants.STRENGTH, 0, 3), new Boost(SkillConstants.MINING, 0, 1)), RED_TROUBLE_BREWING_RUM(ItemId.RUM, null, null, null) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("Trouble brewing not implemented yet.");
            return false;
        }
    },
    BLUE_TROUBLE_BREWING_RUM(ItemId.RUM_8941, null, null, null) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("Trouble brewing not implemented yet.");
            return false;
        }
    },
    BOTTLE_OF_WINE(ItemId.BOTTLE_OF_WINE, new Item(ItemId.EMPTY_WINE_BOTTLE), "You drink the wine.", "It makes you feel a bit dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 14)), PREMADE_BLURB_SP(ItemId.PREMADE_BLURB_SP, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 6), new Boost(SkillConstants.STRENGTH, 0, 6), new Debuff(SkillConstants.DEFENCE, 0, 4)), PREMADE_CHOC_SDY(ItemId.PREMADE_CHOC_SDY, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 6), new Debuff(SkillConstants.ATTACK, 0, 4)), PREMADE_DR_DRAGON(ItemId.PREMADE_DR_DRAGON, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 7), new Debuff(SkillConstants.DEFENCE, 0, 4)), PREMADE_FR_BLAST(ItemId.PREMADE_FR_BLAST, cocktailGlass, "You drink the cocktail. It tastes great.", null, new Restoration(SkillConstants.HITPOINTS, 0, 9)), PREMADE_P_PUNCH(ItemId.PREMADE_P_PUNCH, cocktailGlass, "You drink the cocktail. It tastes great.", null, new Restoration(SkillConstants.HITPOINTS, 0, 9)), PREMADE_SGG(ItemId.PREMADE_SGG, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 4), new Debuff(SkillConstants.ATTACK, 0, 3)), PREMADE_WIZ_BLZD(ItemId.PREMADE_WIZ_BLZD, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 6), new Debuff(SkillConstants.ATTACK, 0, 4)), PINEAPPLE_PUNCH(ItemId.PINEAPPLE_PUNCH, cocktailGlass, "You drink the cocktail. It tastes great.", null, new Restoration(SkillConstants.HITPOINTS, 0, 9)), WIZARD_BLIZZARD(ItemId.WIZARD_BLIZZARD, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 6), new Debuff(SkillConstants.ATTACK, 0, 4)), BLURBERRY_SPECIAL(ItemId.BLURBERRY_SPECIAL, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 6), new Boost(SkillConstants.STRENGTH, 0, 6), new Debuff(SkillConstants.ATTACK, 0, 4)), CHOC_SATURDAY(ItemId.CHOC_SATURDAY, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 7), new Debuff(SkillConstants.ATTACK, 0, 4)), SHORT_GREEN_GUY(ItemId.SHORT_GREEN_GUY, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 4), new Debuff(SkillConstants.ATTACK, 0, 3)), FRUIT_BLAST(ItemId.FRUIT_BLAST, cocktailGlass, "You drink the cocktail. It tastes great.", null, new Restoration(SkillConstants.HITPOINTS, 0, 9)), DRUNK_DRAGON(ItemId.DRUNK_DRAGON, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 7), new Debuff(SkillConstants.ATTACK, 0, 4)), DIRTY_BLAST(ItemId.DIRTY_BLAST, null, null, null) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I'd really rather not..");
            return false;
        }
    },
    ALT_WIZARD_BLIZZARD(ItemId.WIZARD_BLIZZARD_9508, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 6), new Debuff(SkillConstants.ATTACK, 0, 4)), ALT_BLURBERRY_SPECIAL(ItemId.BLURBERRY_SPECIAL_9520, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 6), new Boost(SkillConstants.STRENGTH, 0, 6), new Debuff(SkillConstants.ATTACK, 0, 4)), ALT_CHOC_SATURDAY(ItemId.CHOC_SATURDAY_9518, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 7), new Debuff(SkillConstants.ATTACK, 0, 4)), ALT_SHORT_GREEN_GUY(ItemId.SHORT_GREEN_GUY_9510, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 4), new Debuff(SkillConstants.ATTACK, 0, 3)), ALT_FRUIT_BLAST(ItemId.FRUIT_BLAST_9514, cocktailGlass, "You drink the cocktail. It tastes great.", null, new Restoration(SkillConstants.HITPOINTS, 0, 9)), ALT_DRUNK_DRAGON(ItemId.DRUNK_DRAGON_9516, cocktailGlass, "You drink the cocktail. It tastes great,", "although you feel slightly dizzy.", new Restoration(SkillConstants.HITPOINTS, 0, 5), new Boost(SkillConstants.STRENGTH, 0, 7), new Debuff(SkillConstants.ATTACK, 0, 4)), ALT_PINEAPPLE_PUNCH(ItemId.PINEAPPLE_PUNCH_9512, cocktailGlass, "You drink the cocktail. It tastes great.", null, new Restoration(SkillConstants.HITPOINTS, 0, 9)), UNFINISHED_COCKTAIL(ItemId.UNFINISHED_COCKTAIL, ItemId.UNFINISHED_COCKTAIL_2044, ItemId.UNFINISHED_COCKTAIL_2046, ItemId.UNFINISHED_COCKTAIL_2050, ItemId.UNFINISHED_COCKTAIL_2052, ItemId.UNFINISHED_COCKTAIL_2056, ItemId.UNFINISHED_COCKTAIL_2058, ItemId.UNFINISHED_COCKTAIL_2060, ItemId.UNFINISHED_COCKTAIL_2062, ItemId.UNFINISHED_COCKTAIL_2066, ItemId.UNFINISHED_COCKTAIL_2068, ItemId.UNFINISHED_COCKTAIL_2070, ItemId.UNFINISHED_COCKTAIL_2072, ItemId.UNFINISHED_COCKTAIL_2076, ItemId.UNFINISHED_COCKTAIL_2078, ItemId.UNFINISHED_COCKTAIL_2082, ItemId.UNFINISHED_COCKTAIL_2086, ItemId.UNFINISHED_COCKTAIL_2088, ItemId.UNFINISHED_COCKTAIL_2090) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("Perhaps I should finish this first.");
            return false;
        }
    },
    ODD_COCKTAIL(ItemId.ODD_COCKTAIL, ItemId.ODD_COCKTAIL_2096, ItemId.ODD_COCKTAIL_2098) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I'd really rather not.");
            return false;
        }
    },
    WINTER_SQIRKJUICE(ItemId.WINTER_SQIRKJUICE, beerGlass, "You drink the Winter sq'irkjuice. It tastes great.", null) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 5);
        }
    },
    SPRING_SQIRKJUICE(ItemId.SPRING_SQIRKJUICE, beerGlass, "You drink the Spring sq'irkjuice. It tastes great.", null, new Boost(SkillConstants.THIEVING, 0, 1)) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 10);
        }
    },
    AUTUMN_SQIRKJUICE(ItemId.AUTUMN_SQIRKJUICE, beerGlass, "You drink the Autumn sq'irkjuice. It tastes great.", null, new Boost(SkillConstants.THIEVING, 0, 2)) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 15);
        }
    },
    SUMMER_SQIRKJUICE(ItemId.SUMMER_SQIRKJUICE, beerGlass, "You drink the Summer sq'irkjuice. It tastes great.", null, new Boost(SkillConstants.THIEVING, 0, 3)) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 20);
        }
    },
    REDUCED_CADAVA_POTION(ItemId.REDUCED_CADAVA_POTION, null, null, null) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("However bad the day's been, drinking this is unlikely to make it better.");
            return false;
        }
    },
    SHRINK_ME_QUICK(ItemId.SHRINKMEQUICK, vial, "You drink the Shrink-Me-Quick potion.", null), CADAVA_POTION(ItemId.CADAVA_POTION, vial, "You drink the cadava potion.", null), CUP_OF_TEA(ItemId.CUP_OF_TEA_1978, new Item(ItemId.EMPTY_CUP), "You drink the tea.", null, new Restoration(SkillConstants.HITPOINTS, 0, 3), new Boost(SkillConstants.ATTACK, 0, 3)) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    CUP_OF_NETTLE_TEA(ItemId.CUP_OF_TEA_4242, new Item(ItemId.EMPTY_CUP), "You drink the tea.", null) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 5);
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    CUP_OF_MILKY_NETTLE_TEA(ItemId.CUP_OF_TEA_4243, new Item(ItemId.EMPTY_CUP), "You drink the tea.", null, new Boost(SkillConstants.ATTACK, 0, 2)) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 5);
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    CUP_OF_TEA_GHOSTS_AHOY(ItemId.CUP_OF_TEA_4245, new Item(ItemId.EMPTY_CUP), "You drink the tea.", null, new Restoration(SkillConstants.HITPOINTS, 0, 3), new Boost(SkillConstants.ATTACK, 0, 2)) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    CUP_OF_MILKY_TEA_GHOSTS_AHOY(4546, new Item(ItemId.EMPTY_CUP), "You drink the tea.", null, new Restoration(SkillConstants.HITPOINTS, 0, 3), new Boost(SkillConstants.ATTACK, 0, 2)) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    CUP_OF_TEA_CLAY(ItemId.CUP_OF_TEA_7730, new Item(ItemId.EMPTY_CUP), "You drink the tea.", null, new Boost(SkillConstants.CONSTRUCTION, 0, 1)) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    CUP_OF_MILKY_TEA_CLAY(ItemId.CUP_OF_TEA_7731, new Item(ItemId.EMPTY_CUP), "You drink the tea.", null, new Boost(SkillConstants.CONSTRUCTION, 0, 1)) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    CUP_OF_TEA_WHITE(ItemId.CUP_OF_TEA_7733, new Item(ItemId.EMPTY_CUP), "You drink the tea.", null, new Boost(SkillConstants.CONSTRUCTION, 0, 2)) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    CUP_OF_MILKY_TEA_WHITE(ItemId.CUP_OF_TEA_7734, new Item(ItemId.EMPTY_CUP), "You drink the tea.", null, new Boost(SkillConstants.CONSTRUCTION, 0, 2)) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    CUP_OF_TEA_GOLD(ItemId.CUP_OF_TEA_7736, new Item(ItemId.EMPTY_CUP), "You drink the tea.", null, new Boost(SkillConstants.CONSTRUCTION, 0, 3)) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    CUP_OF_MILKY_TEA_GOLD(ItemId.CUP_OF_TEA_7737, new Item(ItemId.EMPTY_CUP), "You drink the tea.", null, new Boost(SkillConstants.CONSTRUCTION, 0, 3)) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    NETTLE_TEA(ItemId.NETTLE_TEA, new Item(1923), "You drink the tea.", null, new Restoration(SkillConstants.HITPOINTS, 0, 3)) {
        @Override
        public void onConsumption(final Player player) {
            if (player.getHitpoints() < player.getMaxHitpoints()) {
                player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 5);
            }
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    MILKY_NETTLE_TEA(ItemId.NETTLE_TEA_4240, new Item(ItemId.BOWL), "You drink the tea.", null, new Restoration(SkillConstants.HITPOINTS, 0, 3)) {
        @Override
        public void onConsumption(final Player player) {
            if (player.getHitpoints() < player.getMaxHitpoints()) {
                player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 5);
            }
            player.setForceTalk(new ForceTalk("Aaah, nothing like a nice cuppa tea!"));
        }
    },
    NETTLE_WATER(ItemId.NETTLEWATER, new Item(ItemId.BOWL), "You drink the nettle water.", null, new Restoration(SkillConstants.HITPOINTS, 0, 1)), TEA_FLASK(ItemId.TEA_FLASK, null, "You drink the tea.", null, new Boost(SkillConstants.ATTACK, 0, 3)) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Ahhh, tea is so refreshing!"));
        }
    },
    BRAVERY_POTION(ItemId.BRAVERY_POTION, vial, "You drink the bravery potion.", null);

    Drink(final int id, final Item leftOver, final String startMessage, final String endMessage, final Boost... boosts) {
        this.ids = new int[] {id};
        this.leftOver = leftOver;
        this.startMessage = startMessage;
        this.endMessage = endMessage;
        this.boosts = boosts;
    }

    Drink(final int... ids) {
        this.ids = ids;
        this.leftOver = null;
        this.startMessage = null;
        this.endMessage = null;
        this.boosts = null;
    }

    private final int[] ids;
    private final Item leftOver;
    private final String startMessage;
    private final String endMessage;
    private final Boost[] boosts;

    @Override
    public Item getItem(final int doses) {
        if (doses < 0 || doses > ids.length) throw new RuntimeException("The drink " + this + " doesn't support dose " + doses + ".");
        if (doses == 0) {
            return leftoverItem(-1);
        }
        return new Item(ids[doses - 1]);
    }

    public static final Drink[] values = values();
    private static final SoundEffect SOUND_EFFECT = new SoundEffect(2401);

    @Override
    public void consume(final Player player, final Item item, final int slotId) {
        final RegionArea area = player.getArea();
        if ((area instanceof DrinkablePlugin && !((DrinkablePlugin) area).drink(player, this)) || !canConsume(player)) {
            return;
        }
        if (this.equals(TEA_FLASK)) {
            if (item.getCharges() <= 0) return;
        }
        final String name = item.getDefinitions().getName().toLowerCase();
        final Inventory inventory = player.getInventory();
        player.getPacketDispatcher().sendSoundEffect(SOUND_EFFECT);
        player.setAnimation(ConsumableAnimation.getEatAnimation(player));
        final String startMessage = startMessage();
        if (startMessage != null) {
            player.sendFilteredMessage(String.format(startMessage, name));
        }
        final String endMessage = endMessage(player);
        if (endMessage != null) {
            player.sendFilteredMessage(endMessage);
        }
        if (this.equals(TEA_FLASK)) {
            item.setCharges(item.getCharges() - 1);
        } else {
            final Item leftover = leftoverItem(item.getId());
            if (leftover != null) {
                if (leftover.getId() == ItemId.VIAL && player.getNumericAttribute(GameSetting.SMASH_VIALS.toString()).intValue() == 1) {
                    inventory.deleteItem(slotId, new Item(item.getId(), 1));
                    player.sendFilteredMessage("You quickly smash the empty vial using the trick a Barbarian taught you.");
                } else {
                    inventory.set(slotId, leftover);
                }
            } else {
                inventory.deleteItem(slotId, new Item(item.getId(), 1));
            }
        }
        applyEffects(player);
    }

    @Override
    public void applyEffects(final Player player) {
        player.getVariables().setPotionDelay(delay());
        onConsumption(player);
        heal(player);
        final Consumable.Boost[] boosts = boosts();
        if (boosts != null) {
            for (Boost boost : boosts) {
                boost.apply(player);
            }
        }
    }

    @Override
    public Item leftoverItem(int id) {
        return leftOver;
    }

    @Override
    public Boost[] boosts() {
        return boosts;
    }

    @Override
    public String startMessage() {
        return startMessage;
    }

    @Override
    public String endMessage(Player player) {
        return endMessage;
    }

    @Override
    public int getDoses(int id) {
        return 1;
    }

    @Override
    public String emptyMessage(Player player) {
        return endMessage(player);
    }

    public int[] getIds() {
        return ids;
    }
}
