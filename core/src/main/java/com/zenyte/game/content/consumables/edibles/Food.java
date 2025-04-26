package com.zenyte.game.content.consumables.edibles;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.consumables.ConsumableAnimation;
import com.zenyte.game.content.consumables.Edible;
import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.EdiblePlugin;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;

/**
 * @author Kris | 29. okt 2017 : 14:24.16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum Food implements Edible {

    JANGERBERRIES(247, 0) {
        @Override
        public Boost[] boosts() {
            return new Boost[] {
                    new Boost(SkillConstants.ATTACK, 0, 2),
                    new Boost(SkillConstants.STRENGTH, 0, 1),
                    new Restoration(SkillConstants.PRAYER, 0, 1),
                    new Debuff(SkillConstants.DEFENCE, 0, 1)
            };
        }
    },
	SHRIMP(315, 3),
    ANCHOVIES(319, 1),
    SARDINE(325, 4),
    SALMON(329, 9),
    TROUT(333, 7),
    GIANT_CARP(337, 6),
    COD(339, 7),
    HERRING(347, 5),
    PIKE(351, 8),
    MACKEREL(355, 6),
    TUNA(361, 10),
    BASS(365, 13),
    SWORDFISH(373, 14),
    LOBSTER(379, 12),
    SHARK(385, 20),
    MANTA_RAY(391, 22),
    SEA_TURTLE(397, 21),
    EDIBLE_SEAWEED(403, 4),
    STRANGE_FRUIT(464, 0) {
        public void onConsumption(final Player player) {
            if (player.getVariables().getRunEnergy() < 100) {
                player.getVariables().setRunEnergy(Math.min(100, player.getVariables().getRunEnergy() + 30));
            }
            player.getToxins().cureToxin(Toxins.ToxinType.POISON);
        }
    },
    TENTI_PINEAPPLE(1851, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("You can't consume the pineapple whole!");
            return false;
        }
    },
    UGTHANKI_MEAT(1861, 3),
    CHOPPED_TOMATO(1869, 2, new Item(1923)),
    CHOPPED_ONION(1871, 1, new Item(1923)) {
        @Override
        public String endMessage(Player player) {
            if (player.getHitpoints() < player.getMaxHitpoints()) {
                return "It makes your eyes water, but it does heal some health.";
            }
            return "It's sad to see a grown " + (player.getAppearance().isMale() ? "man" : "woman") + " cry.";
        }
    },
    ONION_AND_TOMATO(1875, 3, new Item(1923)) {
        @Override
        public String endMessage(Player player) {
            if (player.getHitpoints() < player.getMaxHitpoints()) {
                return "It makes your eyes water a little, but it does heal some health.";
            }
            return "It's sad to see a grown " + (player.getAppearance().isMale() ? "man" : "woman") + " cry.";
        }
    },
    SMELLY_UGTHANKI_KEBAB(1883, 19) {
        @Override
        public void onConsumption(final Player player) {
            if (player.getHitpoints() < player.getMaxHitpoints()) {
                player.setForceTalk(new ForceTalk("Yum!"));
            }
        }
    },
    FRESH_UGTHANKI_KEBAB(1885, 19) {
        @Override
        public void onConsumption(final Player player) {
            if (player.getHitpoints() < player.getMaxHitpoints()) {
                player.setForceTalk(new ForceTalk("Yum!"));
            }
        }
    },
    CAKE(1891, 4, new Item(1893)) {
        @Override
        public String startMessage() {
            return "You eat part of the %s.";
        }
    },
    TWO_THIRDS_OF_A_CAKE(1893, 4, new Item(1895)) {
        @Override
        public String startMessage() {
            return "You eat some more of the %s.";
        }
    },
    SLICE_OF_CAKE(1895, 4) {
        @Override
        public String startMessage() {
            return "You eat the slice of %s.";
        }
    },
    CHOCOLATE_CAKE(1897, 5, new Item(1899)) {
        @Override
        public String startMessage() {
            return "You eat part of the %s.";
        }
    },
    TWO_THIRDS_OF_A_CHOCOLATE_CAKE(1899, 5, new Item(1901)) {
        @Override
        public String startMessage() {
            return "You eat some more of the %s.";
        }
    },
    CHOCOLATE_SLICE(1901, 5) {
        @Override
        public String startMessage() {
            return "You eat the slice of %s.";
        }
    },
    POTATO(1942, 1),
    ONION(1957, 1) {
        @Override
        public String endMessage(Player player) {
            if (player.getHitpoints() < player.getMaxHitpoints()) {
                return "It makes your eyes water, but it does heal some health.";
            }
            return "It's sad to see a grown " + (player.getAppearance().isMale() ? "man" : "woman") + " cry.";
        }
    },
    PUMPKIN(1959, 14),
    EASTER_EGG(1961, 14),
    BANANA(1963, 2),
    CABBAGE(1965, 1) {
        @Override
        public String startMessage() {
            return "You eat the %s. Yuck!";
        }
    },
    ALT_CABBAGE(1967, 1) {
        @Override
        public String startMessage() {
            return "You eat the %s. Yuck!";
        }
    },
    SPINACH_ROLL(1969, 2),
    KEBAB(1971, 0) {
        @Override
        public void onConsumption(final Player player) {
            int roll = Utils.random(1, 100);
            String endMessage = "";
            int healAmount = 0;

            if (roll <= 66) {
                endMessage = "It heals some health.";
                healAmount = (int) (player.getSkills().getLevelForXp(SkillConstants.HITPOINTS) * 0.10);
            } else if (roll <= 87) {
                endMessage = "That was a good kebab. You feel a lot better.";
                healAmount = Utils.random(10, 20);
            } else if (roll <= 96) {
                endMessage = "That kebab didn't seem to do a lot.";
            } else {
                endMessage = "Wow, that was an amazing kebab! You feel really invigorated.";
                healAmount = 30;
                player.getSkills().boostSkill(SkillConstants.ATTACK, Utils.random(1, 3));
                player.getSkills().boostSkill(SkillConstants.STRENGTH, Utils.random(1, 3));
                player.getSkills().boostSkill(SkillConstants.DEFENCE, Utils.random(1, 3));
            }

            player.sendMessage(endMessage);
            player.heal(healAmount);
        }

        @Override
        public String endMessage(final Player player) {
            return null;
        }
    },
    CHOCOLATE_BAR(1973, 3),
    TOMATO(1982, 2),
    ROTTEN_APPLE(1984, 0) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Yuck!"));
        }
        @Override
        public String startMessage() {
            return "It's rotten, you spit it out.";
        }
        @Override
        public String endMessage(final Player player) {
            return null;
        }
    },
    CHEESE(1985, 2),
    STEW(2003, 11, new Item(1923)),
    CURRY(2011, 19, new Item(1923)),
    LEMON(2102, 2),
    LEMON_CHUNKS(2104, 2),
    LEMON_SLICES(2106, 2),
    ORANGE(2108, 2),
    ORANGE_CHUNKS(2110, 2),
    ORANGE_SLICES(2112, 2),

    TURKEY_DRUMSTICK(ChristmasConstants.TURKEY_DRUMSTICK, 1),
    ROAST_POTATOES(ChristmasConstants.ROAST_POTATOES, 1),
    YULE_LOG(ChristmasConstants.YULE_LOG, 1),

    PINEAPPLE(2114, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("You can't consume the pineapple whole!");
            return false;
        }
    },
    PINEAPPLE_CHUNKS(2116, 2),
    PINEAPPLE_RING(2118, 2),
    LIME(2120, 2),
    LIME_CHUNKS(2122, 2),
    LIME_SLICES(2124, 2),
    DWELLBERRIES(2126, 2),
    EQUA_LEAVES(2128, 1) {
        @Override
        public String startMessage() {
            return "You eat the leaves; chewy but tasty.";
        }
    },
    POT_OF_CREAM(2130, 1) {
        @Override
        public String startMessage() {
            return "You eat the cream. You get some on your nose.";
        }
    },
    CHICKEN(2140, 3),
    MEAT(2142, 3),
    MYSTERY_MEAT(24782, 5) {
        @Override
        public String startMessage() {
            return "It heals some health, and tastes concerningly nice.";
        }
    },
    LAVA_EEL(2149, 11),
    TOADS_LEGS(2152, 3) {
        @Override
        public String startMessage() {
            return "You eat the %s. They're a bit chewy.";
        }
        @Override
        public String endMessage(final Player player) {
            return null;
        }
    },
    EQUA_TOADS_LEGS(2154, 0),//TODO: Find info
    SPICY_TOADS_LEGS(2156, 0),//TODO: Find info
    SEASONED_LEGS(2158, 0),//TODO: Find info
    SPICY_WORM(2160, 0),//TODO: Find info
    KING_WORM(2162, 2),
    ODD_GNOMEBOWL(2173, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("This gnomebowl doesn't look very appetising.");
            return false;
        }
    },//TODO Find info
    UNFINISHED_CHOCOLATE_BOWL_1(2179, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish the bowl first.");
            return false;
        }
    },
    UNFINISHED_CHOCOLATE_BOWL_2(2181, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish the bowl first.");
            return false;
        }
    },
    UNFINISHED_CHOCOLATE_BOWL_3(2183, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish the bowl first.");
            return false;
        }
    },
    CHOCOLATE_BOMB(2185, 15) {
        //TODO: Can be eaten at the same tick as another food!
    },
    TANGLED_TOADS_LEGS(2187, 15) {
        //TODO: Healing is delayed by one tick.
    },
    UNFINISHED_WORMHOLE_BOWL(2189, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish the bowl first.");
            return false;
        }
    },
    WORM_HOLE(2191, 12),
    UNFINISHED_VEG_BOWL(2193, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish the bowl first.");
            return false;
        }
    },
    VEG_BALL(2195, 12),
    ODD_CRUNCHIES(2197, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("These crunchies doesn't look very appetising.");
            return false;
        }
    },//TODO: Find info
    WORM_CRUNCHIES(2205, 8),
    UNFINISHED_CHOCO_CRUNCHY(2207, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish these crunchies first.");
            return false;
        }
    },//TODO: Find info
    CHOCCHIP_CRUNCHIES(2209, 7),
    UNFINISHED_SPICY_CRUNCHIES(2211, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish these crunchies first.");
            return false;
        }
    },//TODO: Find info
    SPICY_CRUNCHIES(2213, 7),
    UNFINISHED_TOAD_CRUNCHIES(2215, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish these crunchies first.");
            return false;
        }
    },//TODO: Find info
    TOAD_CRUNCHIES(2217, 8),
    PREMADE_WM_BATTA(2219, 11),
    PREMADE_TD_BATTA(2221, 11),
    PREMADE_CT_BATTA(2223, 11),
    PREMADE_FRT_BATTA(2225, 11),
    PREMADE_VEG_BATTA(2227, 11),
    PREMADE_CHOC_BOMB(2229, 15),
    PREMADE_TTL(2231, 15),
    PREMADE_WORM_HOLE(2233, 12),
    PREMADE_VEG_BALL(2235, 12),
    PREMADE_WM_CRUN(2237, 8),
    PREMADE_CH_CRUNCH(2239, 7),
    PREMADE_SY_CRUNCH(2241, 7),
    PREMADE_TD_CRUNCH(2243, 7),
    ODD_BATTA(2245, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("This batta doesn't look very appetising.");
            return false;
        }
    },//TODO: Find info
    UNFINISHED_WORM_BATTA(2251, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish this batta first.");
            return false;
        }
    },//TODO: Find info
    WORM_BATTA(2253, 11),
    TOAD_BATTA(2255, 11),
    UNFINISHED_CHEESE_TOM_BATTA(2257, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish this batta first.");
            return false;
        }
    },//TODO: Find info
    CHEESE_TOM_BATTA(2259, 11),
    UNFINISHED_FRUIT_BATTA1(2261, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish this batta first.");
            return false;
        }
    },//TODO: Find info
    UNFINISHED_FRUIT_BATTA2(2263, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish this batta first.");
            return false;
        }
    },//TODO: Find info
    UNFINISHED_FRUIT_BATTA3(2265, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish this batta first.");
            return false;
        }
    },//TODO: Find info
    UNFINISHED_FRUIT_BATTA4(2267, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish this batta first.");
            return false;
        }
    },//TODO: Find info
    UNFINISHED_FRUIT_BATTA5(2269, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish this batta first.");
            return false;
        }
    },//TODO: Find info
    UNFINISHED_FRUIT_BATTA6(2271, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish this batta first.");
            return false;
        }
    },//TODO: Find info
    UNFINISHED_FRUIT_BATTA7(2273, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish this batta first.");
            return false;
        }
    },//TODO: Find info
    UNFINISHED_FRUIT_BATTA8(2275, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish this batta first.");
            return false;
        }
    },//TODO: Find info
    FRUIT_BATTA(2277, 11),
    UNFINISHED_VEGETABLE_BATTA(2279, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("I should finish this batta first.");
            return false;
        }
    },//TODO: Find info
    VEGETABLE_BATTA(2281, 11),
    PLAIN_PIZZA(2289, 7, new Item(2291)) {
        @Override
        public int delay() {
            return 1;
        }

        @Override
        public String startMessage() {
            return "You eat half of the %s.";
        }
    },
    HALF_OF_A_PLAIN_PIZZA(2291, 7) {
        @Override
        public int delay() {
            return 1;
        }

        @Override
        public String startMessage() {
            return "You eat the remaining plain pizza.";
        }
    },
    MEAT_PIZZA(2293, 8, new Item(2295)) {
        @Override
        public int delay() {
            return 1;
        }

        @Override
        public String startMessage() {
            return "You eat half of the %s.";
        }
    },
    HALF_OF_A_MEAT_PIZZA(2295, 8) {
        @Override
        public int delay() {
            return 1;
        }

        @Override
        public String startMessage() {
            return "You eat the remaining meat pizza.";
        }
    },
    ANCHOVY_PIZZA(2297, 9, new Item(2299)) {
        @Override
        public int delay() {
            return 1;
        }

        @Override
        public String startMessage() {
            return "You eat half of the %s.";
        }
    },
    HALF_OF_AN_ANCHOVY_PIZZA(2299, 9) {
        @Override
        public int delay() {
            return 1;
        }

        @Override
        public String startMessage() {
            return "You eat the remaining anchovy pizza.";
        }
    },
    PINEAPPLE_PIZZA(2301, 11, new Item(2303)) {
        @Override
        public int delay() {
            return 1;
        }

        @Override
        public String startMessage() {
            return "You eat half of the %s.";
        }
    },
    HALF_OF_A_PINEAPPLE_PIZZA(2303, 11) {
        @Override
        public int delay() {
            return 1;
        }

        @Override
        public String startMessage() {
            return "You eat the remaining pineapple pizza.";
        }
    },
    BREAD(2309, 5),
    APPLE_PIE(2323, 7, new Item(2335)),
    REDBERRY_PIE(2325, 5, new Item(2333)),
    MEAT_PIE(2327, 6, new Item(2331)),
    HALF_OF_A_MEAT_PIE(2331, 6),
    HALF_A_REDBERRY_PIE(2333, 5, new Item(2313)),
    HALF_AN_APPLE_PIE(2335, 7, new Item(2313)),
    OOMLIE_WRAP(2343, 0) {
        @Override
        public String startMessage() {
            return "You eat the %s; it tastes very gamy.";
        }
    },//TODO: Healed amount varies depending on hp level.
    ROCK_CAKE(2379, 0) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Ow! I nearly broke a tooth!"));
            if (player.getHitpoints() > 1)
                player.applyHit(new Hit((int) Math.ceil(player.getHitpoints() * 0.1F), HitType.REGULAR));
        }
        @Override
        public String endMessage(final Player player) {
            return null;
        }
    },
    CAVE_NIGHTSHADE(2398, 0) {
        @Override
        public void onConsumption(final Player player) {
            player.setForceTalk(new ForceTalk("Ahhhh! What have I done!"));
            if (player.getHitpoints() > 1)
                player.applyHit(new Hit(Math.min(player.getHitpoints() - 1, 15), HitType.REGULAR));
        }
        @Override
        public String endMessage(final Player player) {
            return null;
        }
    },
    COOKED_CHOMPY(2878, 10),
    COOKED_KARAMBWAN(3144, 18) {
        @Override
        public boolean canConsume(final Player player) {
            return player.getVariables().getKarambwanDelay() <= 0;
        }

        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setKarambwanDelay(2);
            player.getVariables().setPotionDelay(3);
        }
    },
    POISON_KARAMBWAN(3146, 0) {
        @Override
        public void onConsumption(final Player player) {
            if (player.getHitpoints() > 1)
                player.applyHit(new Hit(Math.min(player.getHitpoints() - 1, 5), HitType.POISON));
        }
        @Override
        public String endMessage(final Player player) {
            return null;
        }
    },
    SLICED_BANANA(3162, 2),
    SEAWEED_SANDWICH(3168, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("You really, really do not want to eat that.");
            return false;
        }
    },
    COOKED_RABBIT(3228, 5),
    THIN_SNAIL_MEAT(3369, 0) {
        @Override
        public int healedAmount(Player player) {
            return Utils.random(5, 7);
        }
    },
    LEAN_SNAIL_MEAT(3371, 8),
    FAT_SNAIL_MEAT(3373, 0) {
        @Override
        public int healedAmount(Player player) {
            return Utils.random(7, 9);
        }
    },
    COOKED_SLIMY_EEL(3381, 0) {
        @Override
        public int healedAmount(Player player) {
            return Utils.random(6, 10);
        }
    },
    MONKEY_NUTS(4012, 4),
    MONKEY_BAR(4014, 4),
    BANANA_STEW(4016, 11, new Item(1923)),
    UNDEAD_COOKED_CHICKEN(4291, 1),
    UNDEAD_COOKED_MEAT(4293, 1),
    WHITE_PEARL(4485, 2, new Item(4486)){
        @Override
        public String startMessage() {
            return "You eat the %s and spit out the seed when you're done. Mmm, tasty.";
        }
        @Override
        public String endMessage(final Player player) {
            return null;
        }
    },
    GIANT_FROG_LEGS(4517, 0),//TODO: Find out the amount it heals.
    BLUE_SWEETS(4558, 0),//TODO: Find out amount
    DEEP_BLUE_SWEETS(4559, 0),//TODO: Find out amount
    WHITE_SWEETS(4560, 0),//TODO: Find out amount
    PURPLE_SWEETS(4561, 0),//TODO: Find out amount, this is the unstackable version!
    RED_SWEETS(4562, 0),//TODO: Find out amount
    GREEN_SWEETS(4563, 0),//TODO: Find out amount
    PINK_SWEETS(4564, 0),//TODO Find out amount
    SUPER_KEBAB(4608, 0),//TODO Uses normal kebab's attributes & may affect rc, agility, slayer, fm.
    BLACK_MUSHROOM(4620, 0) {
        @Override
        public String startMessage() {
            return "Eugh! It tastes horrible, and stains your fingers black.";
        }
        @Override
        public String endMessage(final Player player) {
            return null;
        }
    },
    CAVE_EEL(5003, 0) {
        @Override
        public int healedAmount(Player player) {
            return Utils.random(8, 12);
        }
    },
    FROG_SPAWN(5004, 0) {
        @Override
        public String startMessage() {
            return "You eat the frogspawn. Yuck!";
        }
        @Override
        public String endMessage(final Player player) {
            return null;
        }
        @Override
        public int healedAmount(Player player) {
            return Utils.random(3, 6);
        }
    },
    STRAWBERRY(5504, 0) {
        @Override
        public int healedAmount(Player player) {
            return (int) Math.ceil(player.getMaxHitpoints() * 0.06F);
        }
    },
    //ROTTEN_POTATO: Handled elsewhere.
    PAPAYA_FRUIT(5972, 8) {
        public void onConsumption(final Player player) {
            if (player.getVariables().getRunEnergy() < 100) {
                player.getVariables().setRunEnergy(Math.min(100, player.getVariables().getRunEnergy() + 5));
            }
        }
    },
    WATERMELON(5982, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("You can't consume the watermelon whole!");
            return false;
        }
    },
    WATERMELON_SLICE(5984, 0) {
        @Override
        public int healedAmount(Player player) {
            return (int) Math.ceil(player.getMaxHitpoints() * 0.05F);
        }
    },
    COOKED_SWEETCORN(5988, 0) {
        @Override
        public int healedAmount(Player player) {
            return (int) Math.ceil(player.getMaxHitpoints() * 0.1F);
        }
    },
    FISHLIKE_THING(6202, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("It looks vile and smells even worse. You're not eating that!");
            return false;
        }
    },
    ALT_FISHLIKE_THING(6206, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("It looks vile and smells even worse. You're not eating that!");
            return false;
        }
    },
    SPIDER_ON_STICK(6297, 0) {
        @Override
        public int healedAmount(Player player) {
            return Utils.random(7, 10);
        }
        @Override
        public void onConsumption(final Player player) {
            //Adding the stick through consumption as it is a stackable item and causes issues otherwise!
            player.getInventory().addItem(new Item(6305, 1));
        }
    },
    SPIDER_ON_SHAFT(6299, 0) {
        @Override
        public int healedAmount(Player player) {
            return Utils.random(7, 10);
        }
    },
    GOUT_TUBER(6311, 12) {
        @Override
        public void onConsumption(final Player player) {
            if (player.getVariables().getRunEnergy() < 100) {
                player.getVariables().setRunEnergy(Math.min(100, player.getVariables().getRunEnergy() + 50));
            }
        }
    },
    WHITE_TREE_FRUIT(6469, 3) {
        @Override
        public void onConsumption(final Player player) {
            if (player.getVariables().getRunEnergy() < 100) {
                player.getVariables().setRunEnergy(Math.min(100,
                        player.getVariables().getRunEnergy() + Utils.random(5, 10)));
            }
        }
    },
    BAKED_POTATO(6701, 4),
    POTATO_WITH_BUTTER(6703, 14),
    POTATO_WITH_CHEESE(6705, 16),
    POISONED_CHEESE(6768, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("Ummm... let me think about this one.....No! That would be stupid.");
            return false;
        }
    },
    CHOC_ICE(6794, 6),
    PEACH(6883, 8),
    BAGUETTE(6961, 6),
    TRIANGLE_SANDWICH(6962, 6),
    ROLL(6963, 6),
    SQUARE_SANDWICH(6965, 6),
    ALT_SHARK(6969, 20),
    CHILLI_POTATO(7054, 14),
    EGG_POTATO(7056, 16),
    MUSHROOM_POTATO(7058, 20),
    TUNA_POTATO(7060, 22),
    CHILLI_CON_CARNE(7062, 0, new Item(1923)),//TODO: Find out amount
    EGG_AND_TOMATO(7064, 8, new Item(1923)),
    MUSHROOM_AND_ONION(7066, 0, new Item(1923)),//TODO: Find out amount
    TUNA_AND_CORN(7068, 13, new Item(1923)),
    MINCED_MEAT(7070, 0, new Item(1923)),//TODO: Find out amount
    SPICY_SAUCE(7072, 0, new Item(1923)),//TODO: Find out amount
    SCRAMBLED_EGG(7078, 5, new Item(1923)),
    FRIED_MUSHROOMS(7082, 5, new Item(1923)),
    FRIED_ONIONS(7084, 0, new Item(1923)),//TODO: Find out amount
    CHOPPED_TUNA(7086, 10, new Item(1923)),
    SWEETCORN(7088, 0),//TODO: Find out amount or circumstances cuz raw
    DRAGONFRUIT(ItemId.DRAGONFRUIT, 10),
    GARDEN_PIE(7178, 6, new Item(7180)) {
        @Override
        public Boost[] boosts() {
            return new Boost[]{new Boost(SkillConstants.FARMING, 0, 3)};
        }
    },
    HALF_A_GARDEN_PIE(7180, 6, new Item(2313)) {
        @Override
        public Boost[] boosts() {
            return new Boost[]{new Boost(SkillConstants.FARMING, 0, 3)};
        }
    },
    FISH_PIE(7188, 6, new Item(7190)) {
        @Override
        public Boost[] boosts() {
            return new Boost[] { new Boost(SkillConstants.FISHING, 0, 3) };
        }
    },
    HALF_A_FISH_PIE(7190, 6, new Item(2313)) {
        @Override
        public Boost[] boosts() {
            return new Boost[] { new Boost(SkillConstants.FISHING, 0, 3) };
        }
    },
    ADMIRAL_PIE(7198, 8, new Item(7200)) {
        @Override
        public Boost[] boosts() {
            return new Boost[]{new Boost(SkillConstants.FISHING, 0, 5)};
        }
    },
    HALF_AN_ADMIRAL_PIE(7200, 8, new Item(2313)) {
        @Override
        public Boost[] boosts() {
            return new Boost[]{new Boost(SkillConstants.FISHING, 0, 5)};
        }
    },
    WILD_PIE(7208, 11, new Item(7210)) {
        @Override
        public Boost[] boosts() {
            return new Boost[] { new Boost(SkillConstants.SLAYER, 0, 5), new Boost(SkillConstants.RANGED, 0, 4) };
        }
    },
    HALF_A_WILD_PIE(7210, 11, new Item(2313)) {
        @Override
        public Boost[] boosts() {
            return new Boost[] { new Boost(SkillConstants.SLAYER, 0, 5), new Boost(SkillConstants.RANGED, 0, 4) };
        }
    },
    SUMMER_PIE(7218, 11, new Item(7220)) {
        @Override
        public Boost[] boosts() {
            return new Boost[] { new Boost(SkillConstants.AGILITY, 0, 5) };
        }
        @Override
        public void onConsumption(final Player player) {
            if (player.getVariables().getRunEnergy() < 100) {
                player.getVariables().setRunEnergy(Math.min(100,
                        player.getVariables().getRunEnergy() + 10));
            }
        }
    },
    HALF_A_SUMMER_PIE(7220, 11, new Item(2313)) {
        @Override
        public Boost[] boosts() {
            return new Boost[]{new Boost(SkillConstants.AGILITY, 0, 5)};
        }

        @Override
        public void onConsumption(final Player player) {
            if (player.getVariables().getRunEnergy() < 100) {
                player.getVariables().setRunEnergy(Math.min(100,
                        player.getVariables().getRunEnergy() + 10));
            }
        }
    },
    DRAGONFRUIT_PIE(ItemId.DRAGONFRUIT_PIE, 20, new Item(ItemId.HALF_A_DRAGONFRUIT_PIE)) {
        @Override
        public Boost[] boosts() {
            return new Boost[]{new Boost(SkillConstants.FLETCHING, 0, 4)};
        }
    },
    HALF_A_DRAGONFRUIT_PIE(ItemId.HALF_A_DRAGONFRUIT_PIE, 20, new Item(ItemId.PIE_DISH)) {
        @Override
        public Boost[] boosts() {
            return new Boost[]{new Boost(SkillConstants.FLETCHING, 0, 4)};
        }
    },
    ROAST_RABBIT(7223, 7),
    ROASTED_CHOMPY(7228, 10),
    SPICY_STEW(7479, 0, new Item(1923)), //TODO: Affects a lot! http://oldschoolrunescape.wikia.com/wiki/Spicy_stew
    COOKED_CRAB_MEAT1(7521, 2, new Item(7523)),
    COOKED_CRAB_MEAT2(7523, 2, new Item(7524)),
    COOKED_CRAB_MEAT3(7524, 2, new Item(7525)),
    COOKED_CRAB_MEAT4(7525, 2, new Item(7526)),
    COOKED_CRAB_MEAT5(7526, 2),
    COOKED_FISHCAKE(7530, 11),
    COOKED_JUBBLY(7568, 15),
    RED_BANANA(7572, 5),
    TCHIKI_MONKEY_NUTS(7573, 5),//TODO: Does NOT add a delay in combat.
    SLICED_RED_BANANA(7574, 2) {
        @Override
        public String startMessage() {
            return "You eat the sliced red banana. Yum.";
        }
        @Override
        public String endMessage(final Player player) {
            return null;
        }
    },
    TCHIKI_NUT_PASTE(7575, 5) {
        @Override
        public String startMessage() {
            return "You eat the Tchiki monkey nut paste. It sticks to the roof of your mouth.";
        }
        @Override
        public String endMessage(final Player player) {
            return null;
        }
    },
    STUFFED_SNAKE(7579, 20),
    FIELD_RATION(7934, 10),
    FRESH_MONKFISH(7943, 1),
    MONKFISH(7946, 16),
    LOCUST_MEAT(9052, 3) {
        @Override
        public String startMessage() {
            return "Juices spurt into your mouth as you chew. It's tastier than it looks.";
        }
        @Override
        public String endMessage(final Player player) {
            return null;
        }
    },
    MINT_CAKE(9475, 0) {
        @Override
        public void onConsumption(final Player player) {
            if (player.getVariables().getRunEnergy() < 100) {
                player.getVariables().setRunEnergy(Math.min(100,
                        player.getVariables().getRunEnergy() + 50));
            }
        }
    },
    ALT_FRUIT_BATTA(9527, 11),
    ALT_TOAD_BATTA(9529, 11),
    ALT_WORM_BATTA(9531, 11),
    ALT_VEGETABLE_BATTA(9533, 11),
    ALT_CHEESE_TOM_BATTA(9535, 11),
    ALT_TOAD_CRUNCHIES(9538, 8),
    ALT_SPICY_CRUNCHIES(9540, 7),
    ALT_WORM_CRUNCHIES(9542, 8),
    ALT_CHOCCHIP_CRUNCHIES(9544, 7),
    ALT_WORM_HOLE(9547, 12),
    ALT_VEG_BALL(9549, 12),
    ALT_TANGLED_TOADS_LEGS(9551, 15) {
        //TODO: Healing is delayed by one tick.
    },
    ALT_CHOCOLATE_BOMB(9553, 15) {
        //TODO: Can be eaten at the same tick as another food!
    },
    ROAST_BIRD_MEAT(9980, 6),
    ROAST_BEAST_MEAT(9988, 8),
    SPICY_TOMATO(9994, 0, new Item(1923)),//TODO: Find out amount
    SPICY_MINCED_MEAT(9996, 0, new Item(1923)),//TODO: Find out amount
    RAINBOW_FISH(10136, 11),
    STACKABLE_PURPLE_SWEETS(10476, 0) {
        @Override
        public int healedAmount(final Player player) {
            return Utils.random(1, 3);
        }
        @Override
        public void onConsumption(final Player player) {
            if (player.getVariables().getRunEnergy() < 100) {
                player.getVariables().setRunEnergy(Math.min(100,
                        player.getVariables().getRunEnergy() + 10));
            }
        }

        @Override
        public boolean canConsume(Player player) {
            if (player.getArea() instanceof Inferno) {
                player.sendMessage("The heat of the inferno causes the wrapper to stick to the sweet. You cannot see anyway to open it.");
                return false;
            }
            return super.canConsume(player);
        }
    },
    GREEN_GLOOP_SOUP(10960, 2),
    FROGBURGER(10962, 2),
    COATED_FROGS_LEGS(10963, 2),
    BAT_SHISH(10964, 2),
    FINGERS(10965, 2),
    GRUBS_A_LA_MODE(10966, 2),
    ROAST_FROG(10967, 2),
    MUSHROOMS(10968, 2),
    FILLETS(10969, 2),
    LOACH(10970, 3) {
        @Override
        public Boost[] boosts() {
            return new Boost[] { new Boost(SkillConstants.COOKING, 0, 1) };
        }
    },
    EEL_SUSHI(10971, 10),
    SHRUNK_OGLEROOT(11205, 0),//TODO: Find out amount
    ROE(11324, 3),
    CAVIAR(11326, 3),
    CHOCOLATE_STRAWBERRY(11910, 2),
    DARK_CRAB(11936, 22),
    SACRED_EEL(13339, 0),//TODO: Find out amount
    SERVERY_MEAT_PIE(13403, 0),//TODO: Find out amount
    SERVERY_PLAIN_PIZZA(13409, 0),//TODO: Find out amount
    SERVERY_PINEAPPLE_PIZZA(13412, 0),//TODO: find out amount
    SERVERY_COOKED_MEAT(13413, 0),//TODO: Find out amount
    SERVERY_POTATO(13414, 0),//TODO: Find out amount
    SERVERY_STEW(13418, 0),//TODO: Find out amount
    GOLOVANOVA_FRUIT(13426, 0),//TODO: Find out amount
    BOLOGANO_FRUIT(13427, 0),//TODO: Find out amount
    LOGAVANO_FRUIT(13428, 0),//TODO: Find out amount
    ANGLERFISH(13441, 0) {
        @Override
        public int healedAmount(final Player player) {
            final int hitpoints = player.getSkills().getLevelForXp(SkillConstants.HITPOINTS);
            final int c = hitpoints < 25 ? 2 : hitpoints < 50 ? 4 : hitpoints < 75 ? 6 : hitpoints < 93 ? 8 : 13;
            return (int) Math.floor(hitpoints / 10F) + c;
        }
        @Override
        public void heal(final Player player) {
            final int heal = healedAmount(player);
            final int hp = player.getSkills().getLevelForXp(SkillConstants.HITPOINTS);
            final int currentHealth = player.getSkills().getLevel(SkillConstants.HITPOINTS);
            final int boost = hp < 10 ? 0 : hp < 20 ? 3 : hp < 25 ? 4 : hp < 30 ? 6 : hp < 40 ? 7 : hp < 50 ? 8 :
                    hp < 60 ? 11 : hp < 70 ? 12 : hp < 75 ? 13 : hp < 80 ? 15 : hp < 90 ? 16 : hp < 92 ? 17 : 22;
            final int max = hp + boost;
            if (currentHealth > max + heal) {
                return;
            }
            player.setHitpoints((currentHealth + heal) >= max ? max : (currentHealth + heal));
        }

        @Override
        public String endMessage(Player player) {
            final int hp = player.getSkills().getLevelForXp(SkillConstants.HITPOINTS);
            final int boost = hp < 10 ? 0 : hp < 20 ? 3 : hp < 25 ? 4 : hp < 30 ? 6 : hp < 40 ? 7 : hp < 50 ? 8 :
                    hp < 60 ? 11 : hp < 70 ? 12 : hp < 75 ? 13 : hp < 80 ? 15 : hp < 90 ? 16 : hp < 92 ? 17 : 22;
            if (player.getHitpoints() >= player.getMaxHitpoints() + boost)
                return null;
            return "It heals some health.";
        }

    },
    BOTANICAL_PIE(19662, 7, new Item(19659)) {
        @Override
        public Boost[] boosts() {
            return new Boost[] { new Boost(SkillConstants.HERBLORE, 0, 4) };
        }
    },
    HALF_A_BOTANICAL_PIE(19659, 7, new Item(2313)) {
        @Override
        public Boost[] boosts() {
            return new Boost[] { new Boost(SkillConstants.HERBLORE, 0, 4) };
        }
    },
    ALT_2_SHARK(20390, 20),
    ALT_MONKFISH(20547, 16),
    PYSK_FISH(20856, 5),
    SUPHI_FISH(20858, 8),
    LECKISH_FISH(20860, 11),
    BRAWK_FISH(20862, 14),
    MYCIL_FISH(20864, 17),
    ROQED_FISH(20866, 20),
    KYREN_FISH(20868, 23),
    PRAEL_BAT(20873, 8),
    GIRAL_BAT(20875, 11),
    PHLUXIA_BAT(20877, 14),
    KRYKET_BAT(20879, 17),
    MURNG_BAT(20881, 20),
    PSYKK_BAT(20883, 23) {
        @Override
        public void consume(final Player player, final Item item, final int slotId) {
            final boolean healing = player.getHitpoints() < player.getMaxHitpoints();
            final RegionArea area = player.getArea();
            final boolean consumable = !((area instanceof EdiblePlugin && !((EdiblePlugin) area).eat(player, this)) || !canConsume(player));
            super.consume(player, item, slotId);
            if (consumable && healing) {
                Raid.applyPsykkEffect(player, item);
            }
        }
    },
    INFERNAL_EEL(21293, 0) {
        @Override
        public boolean canConsume(final Player player) {
            player.sendMessage("Removing the eel from the lava has caused it to harden. You don't want to test your teeth against one of these.");
            return false;
        }
    },
    MUSHROOM_PIE(21690, 8, new Item(21687)) {
        @Override
        public Boost[] boosts() {
            return new Boost[] { new Boost(SkillConstants.CRAFTING, 0, 4) };
        }
    },
    HALF_A_MUSHROOM_PIE(21687, 8, new Item(2313)) {
        @Override
        public Boost[] boosts() {
            return new Boost[] { new Boost(SkillConstants.CRAFTING, 0, 4) };
        }
    },
    PADDLEFISH(23874, 20) {
        @Override
        public String startMessage() {
            return "You eat the food.";
        }

        @Override
        public String endMessage(Player player) {
            return "It heals some health.";
        }
    },
    CORRUPTED_PADDLEFISH(25958, 16) {
        @Override
        public boolean canConsume(final Player player) {
            return player.getVariables().getKarambwanDelay() <= 0;
        }

        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setKarambwanDelay(2);
            player.getVariables().setPotionDelay(3);
        }
    },
    CRYSTAL_PADDLEFISH(25960, 16) {
        @Override
        public boolean canConsume(final Player player) {
            return player.getVariables().getKarambwanDelay() <= 0;
        }

        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setKarambwanDelay(2);
            player.getVariables().setPotionDelay(3);
        }
    },
    COOKED_MYSTERY_MEAT(ItemId.COOKED_MYSTERY_MEAT, 5) {
        @Override
        public String endMessage(Player player) {
            if (player.getHitpoints() >= player.getMaxHitpoints()) return null;
            return "It heals some health, and tastes concerningly nice.";
        }
    },
    HONEY_LOCUST(ItemId.HONEY_LOCUST, 20) {
        @Override
        public void heal(final Player player) {
            final int heal = healedAmount(player);
            final int hp = player.getSkills().getLevelForXp(SkillConstants.HITPOINTS);
            final int currentHealth = player.getSkills().getLevel(SkillConstants.HITPOINTS);
            final int max = hp + heal;
            if (currentHealth > max + heal) {
                return;
            }
            player.setHitpoints(Math.min((currentHealth + heal), max));
        }

        @Override
        public String endMessage(Player player) {
            if (player.getHitpoints() >= player.getMaxHitpoints() + healedAmount(player))
                return null;
            return "It heals some health.";
        }
    },

    SILK_DRESSING_2(ItemId.SILK_DRESSING_2, 0, new Item(ItemId.SILK_DRESSING_1)) {
        @Override
        public void heal(final Player player) {
            player.getVariables().schedule(100, TickVariable.SILK_DRESSING);
        }

        @Override
        public String startMessage() {
            return null;
        };

        @Override
        public String endMessage(Player player) {
            return "You consume the silk dressing.";
        }
    },
    SILK_DRESSING_1(ItemId.SILK_DRESSING_1, 0) {
        @Override
        public void heal(final Player player) {
            player.getVariables().schedule(100, TickVariable.SILK_DRESSING);
        }

        @Override
        public String endMessage(Player player) {
            return "You consume the silk dressing.";
        }
    },
    BLIGHTED_MANTA_RAY(ItemId.BLIGHTED_MANTA_RAY, 22) {
        @Override
        public boolean canConsume(Player player) {
            if (!WildernessArea.isWithinWilderness(player)) {
                player.sendMessage("The blighted manta ray can be eaten only in the Wilderness.");
                return false;
            }
            return super.canConsume(player);
        }
    },
    BLIGHTED_ANGLERFISH(ItemId.BLIGHTED_ANGLERFISH, 0) {
        @Override
        public int healedAmount(final Player player) {
            final int hitpoints = player.getSkills().getLevelForXp(SkillConstants.HITPOINTS);
            final int c = hitpoints < 25 ? 2 : hitpoints < 50 ? 4 : hitpoints < 75 ? 6 : hitpoints < 93 ? 8 : 13;
            return (int) Math.floor(hitpoints / 10F) + c;
        }
        @Override
        public void heal(final Player player) {
            final int heal = healedAmount(player);
            final int hp = player.getSkills().getLevelForXp(SkillConstants.HITPOINTS);
            final int currentHealth = player.getSkills().getLevel(SkillConstants.HITPOINTS);
            final int boost = hp < 10 ? 0 : hp < 20 ? 3 : hp < 25 ? 4 : hp < 30 ? 6 : hp < 40 ? 7 : hp < 50 ? 8 :
                    hp < 60 ? 11 : hp < 70 ? 12 : hp < 75 ? 13 : hp < 80 ? 15 : hp < 90 ? 16 : hp < 92 ? 17 : 22;
            final int max = hp + boost;
            if (currentHealth > max + heal) {
                return;
            }
            player.setHitpoints((currentHealth + heal) >= max ? max : (currentHealth + heal));
        }

        @Override
        public String endMessage(Player player) {
            final int hp = player.getSkills().getLevelForXp(SkillConstants.HITPOINTS);
            final int boost = hp < 10 ? 0 : hp < 20 ? 3 : hp < 25 ? 4 : hp < 30 ? 6 : hp < 40 ? 7 : hp < 50 ? 8 :
                    hp < 60 ? 11 : hp < 70 ? 12 : hp < 75 ? 13 : hp < 80 ? 15 : hp < 90 ? 16 : hp < 92 ? 17 : 22;
            if (player.getHitpoints() >= player.getMaxHitpoints() + boost)
                return null;
            return "It heals some health.";
        }
        @Override
        public boolean canConsume(Player player) {
            if (!WildernessArea.isWithinWilderness(player)) {
                player.sendMessage("The blighted anglerfish can be eaten only in the Wilderness.");
                return false;
            }
            return super.canConsume(player);
        }
    },
    BLIGHTED_KARAMBWAN(ItemId.BLIGHTED_KARAMBWAN, 18) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setKarambwanDelay(2);
            player.getVariables().setPotionDelay(3);
        }
        @Override
        public boolean canConsume(Player player) {
            if (player.getVariables().getKarambwanDelay() <= 0) {
                if (!WildernessArea.isWithinWilderness(player)) {
                    player.sendMessage("The blighted manta ray can be eaten only in the Wilderness.");
                    return false;
                }
                return true;
            } else
                return false;
        }
    };

    public static final Food[] values = values();
    private final int amount;

    private final int id;
    private final Item leftOver;
    Food(final int id, final int amount, final Item leftOver) {
        this.id = id;
        this.amount = amount;
        this.leftOver = leftOver;
    }

    Food(final int id, final int amount) {
        this(id, amount, null);
    }

    @Override
    public Animation animation() {
        return ConsumableAnimation.EAT_ANIM;
    }

    @Override
    public int healedAmount(Player player) {
        return amount;
    }

    @Override
    public Item leftoverItem(int id) {
        return leftOver;
    }

    public int getId() {
        return id;
    }

}
