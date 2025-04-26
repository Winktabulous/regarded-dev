package com.zenyte.game.content.consumables.drinks;

import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.consumables.ConsumableEffects;
import com.zenyte.game.content.consumables.Drinkable;
import com.zenyte.game.content.tombsofamascut.AbstractTOARaidArea;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.impl.wilderness.ChaosFanatic;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.types.config.items.ItemDefinitions;
import org.apache.commons.lang3.ArrayUtils;

import static com.zenyte.game.item.ItemId.*;

/**
 * @author Kris | 02/12/2018 13:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum Potion implements Drinkable {
    STRENGTH_POTION(new int[] {119, 117, 115, 113}, new Boost(SkillConstants.STRENGTH, 0.1F, 3)),
    ATTACK_POTION(new int[] {125, 123, 121, 2428}, new Boost(SkillConstants.ATTACK, 0.1F, 3)),
    DEFENCE_POTION(new int[] {137, 135, 133, 2432}, new Boost(SkillConstants.DEFENCE, 0.1F, 3)),
    SUPER_ATTACK_POTION(new int[] {149, 147, 145, 2436}, new Boost(SkillConstants.ATTACK, 0.15F, 5)),
    FISHING_POTION(new int[] {155, 153, 151, 2438}, new Boost(SkillConstants.FISHING, 0, 3)),
    SUPER_STRENGTH_POTION(new int[] {161, 159, 157, 2440}, new Boost(SkillConstants.STRENGTH, 0.15F, 5)),
    SUPER_DEFENCE_POTION(new int[] {167, 165, 163, 2442}, new Boost(SkillConstants.DEFENCE, 0.15F, 5)),
    RANGING_POTION(new int[] {173, 171, 169, 2444}, new Boost(SkillConstants.RANGED, 0.1F, 4)),
    ZAMORAK_BREW(new int[] {193, 191, 189, 2450}, new Boost(SkillConstants.ATTACK, 0.2F, 2),
            new Boost(SkillConstants.STRENGTH, 0.12F, 2), new Debuff(SkillConstants.DEFENCE, 0.1F, 2), new Restoration(SkillConstants.PRAYER, 0.1F, 0)) {
        @Override
        public String startMessage() {
            return "You drink some of the foul liquid.";
        }

        @Override
        public void onConsumption(final Player player) {
            player.applyHit(new Hit((int) Math.floor(player.getHitpoints() * 0.12F), HitType.REGULAR));
            player.getCombatAchievements().removeCurrentTaskFlag(CAType.PRAYING_TO_THE_GODS, ChaosFanatic.CA_TASK_PRAYING_TO_THE_GODS_STARTED);
        }
    },
    AGILITY_POTION(new int[] {3038, 3036, 3034, 3032}, new Boost(SkillConstants.AGILITY, 0, 3)),
    MAGIC_POTION(new int[] {3046, 3044, 3042, 3040}, new Boost(SkillConstants.MAGIC, 0, 4)),
    SARADOMIN_BREW(new int[] {6691, 6689, 6687, 6685}, new Boost(SkillConstants.HITPOINTS, 0.15F, 2), new Boost(SkillConstants.DEFENCE, 0.2F, 2), new Debuff(SkillConstants.STRENGTH, 0.1F, 2), new Debuff(SkillConstants.ATTACK, 0.1F, 2), new Debuff(SkillConstants.MAGIC, 0.1F, 2), new Debuff(SkillConstants.RANGED, 0.1F, 2)) {
        @Override
        public String startMessage() {
            return "You drink some of the foul liquid.";
        }
    },
    MAGIC_ESSENCE(new int[] {9024, 9023, 9022, 9021}, new Boost(SkillConstants.MAGIC, 0, 3)),
    COMBAT_POTION(new int[] {9745, 9743, 9741, 9739}, new Boost(SkillConstants.ATTACK, 0.1F, 3), new Boost(SkillConstants.STRENGTH, 0.1F, 3)), HUNTER_POTION(new int[] {10004, 10002, 10000, 9998}, new Boost(SkillConstants.HUNTER, 0, 3)), SUPER_RANGING(new int[] {11725, 11724, 11723, 11722}, new Boost(SkillConstants.RANGED, 0.15F, 5)) {
        @Override
        public boolean canConsume(final Player player) {
            if (!player.inArea("Nightmare zone")) {
                player.sendMessage("You can only sip an overload while in the Nightmare zone.");
                return false;
            }
            return true;
        }
    },
    SUPER_MAGIC_POTION(new int[] {11729, 11728, 11727, 11726}, new Boost(SkillConstants.MAGIC, 0.15F, 5)) {
        @Override
        public boolean canConsume(final Player player) {
            if (!player.inArea("Nightmare zone")) {
                player.sendMessage("You can only sip an overload while in the Nightmare zone.");
                return false;
            }
            return true;
        }
    },
    ANCIENT_BREW(new int[] {26346, 26344, 26342, 26340}, new Boost(SkillConstants.MAGIC, 0.05F, 2), new CappedBoost(SkillConstants.PRAYER, 0.1F, 2, 0.05F, 0), new Debuff(SkillConstants.ATTACK, 0.1F, 2), new Debuff(SkillConstants.STRENGTH, 0.1F, 2), new Debuff(SkillConstants.DEFENCE, 0.1F, 2)) {
        @Override public void onConsumption(Player player) {
            player.getCombatAchievements().removeCurrentTaskFlag(CAType.PRAYING_TO_THE_GODS, ChaosFanatic.CA_TASK_PRAYING_TO_THE_GODS_STARTED);
        }
    },
    SUPER_COMBAT_POTION(new int[] {12701, 12699, 12697, 12695}, new Boost(SkillConstants.ATTACK, 0.15F, 5), new Boost(SkillConstants.STRENGTH, 0.15F, 5), new Boost(SkillConstants.DEFENCE, 0.15F, 5)), ALT_PRAYER_POTION(new int[] {20396, 20395, 20394, 20393}, new Restoration(SkillConstants.PRAYER, 0.25F, 7)), BATTLEMAGE_POTION(new int[] {22458, 22455, 22452, 22449}, new Boost(SkillConstants.MAGIC, 0, 4), new Boost(SkillConstants.DEFENCE, 0.15F, 5)) {
        @Override
        public String startMessage() {
            return "You drink some of your %s. It's a little tangy.";
        }
    },
    BASTION_POTION(new int[] {ItemId.BASTION_POTION1, ItemId.BASTION_POTION2, ItemId.BASTION_POTION3, ItemId.BASTION_POTION4},
            new Boost(SkillConstants.RANGED, 0.1F, 4),
            new Boost(SkillConstants.DEFENCE, 0.15F, 5)) {
        @Override
        public String startMessage() {
            return "You drink some of your %s. It's a little tangy.";
        }
    },
    PRAYER_POTION(new int[] {143, 141, 139, 2434}, new Restoration(SkillConstants.PRAYER, 0.25F, 7)) {
        @Override public void onConsumption(Player player) {
            player.getCombatAchievements().removeCurrentTaskFlag(CAType.PRAYING_TO_THE_GODS, ChaosFanatic.CA_TASK_PRAYING_TO_THE_GODS_STARTED);
        }
    },
    ANTIPOISON(new int[] {179, 177, 175, 2446}) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().cureToxin(Toxins.ToxinType.POISON);
            player.getVariables().schedule(150, TickVariable.POISON_IMMUNITY);
        }
    },
    SUPERANTIPOISON(new int[] {185, 183, 181, 2448}) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().cureToxin(Toxins.ToxinType.POISON);
            player.getVariables().schedule(600, TickVariable.POISON_IMMUNITY);
        }
    },
    ANTIFIRE_POTION(new int[] {2458, 2456, 2454, 2452}) {
        @Override
        public void onConsumption(final Player player) {
            ConsumableEffects.applyAntifire(player, 600);
        }
    },
    ENERGY_POTION(new int[] {3014, 3012, 3010, 3008}) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 10);
        }
    },
    SUPER_ENERGY_POTION(new int[] {3022, 3020, 3018, 3016}) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 20);
        }
    },
    RELICYMS_BALM(new int[] {4848, 4846, 4844, 4842}) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().weakenDisease();
            if (player.getVarManager().getBitValue(10151) > 0) {
                player.getVarManager().sendBit(10151, 0);
                player.sendMessage("<col=229628>The parasite within you has been weakened.");
            }
        }
    },
    ANTIDOTE_PLUS(new int[] {5949, 5947, 5945, 5943}) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().cureToxin(Toxins.ToxinType.POISON);
            player.getVariables().schedule(900, TickVariable.POISON_IMMUNITY);
        }
    },
    ANTIDOTE_PLUS_PLUS(new int[] {5958, 5956, 5954, 5952}) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().cureToxin(Toxins.ToxinType.POISON);
            player.getToxins().cureToxin(Toxins.ToxinType.VENOM);
            player.getVariables().schedule(1200, TickVariable.POISON_IMMUNITY);
        }
    },
    EXTENDED_ANTIFIRE(new int[] {11957, 11955, 11953, 11951}) {
        @Override
        public void onConsumption(final Player player) {
            ConsumableEffects.applyAntifire(player, 1200);
        }
    },
    STAMINA_POTION(new int[] {12631, 12629, 12627, 12625}) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 20);
            player.getVariables().schedule(200, TickVariable.STAMINA_ENHANCEMENT);
            player.getVarManager().sendBit(25, 1);
        }
    },
    ANTI_VENOM(new int[] {12911, 12909, 12907, 12905}) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().resetVenom();
            player.getVariables().schedule(1200, TickVariable.POISON_IMMUNITY);
            player.getVariables().schedule(100, TickVariable.VENOM_IMMUNITY);
        }
    },
    ANTI_VENOM_PLUS(new int[] {12919, 12917, 12915, 12913}) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().resetVenom();
            player.getVariables().schedule(1500, TickVariable.POISON_IMMUNITY);
            player.getVariables().schedule(300, TickVariable.VENOM_IMMUNITY);
        }
    },
    EXTENDED_ANTI_VENOM(new int[] {EXTENDED_ANTIVENOM1, EXTENDED_ANTIVENOM2, EXTENDED_ANTIVENOM3, EXTENDED_ANTIVENOM4}) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().resetVenom();
            player.getVariables().schedule(1770, TickVariable.POISON_IMMUNITY);
            player.getVariables().schedule(630, TickVariable.VENOM_IMMUNITY);
        }
    },
    ALT_SUPER_ENERGY_POTION(new int[] {20551, 20550, 20549, 20548}) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 20);
        }
    },
    SUPER_ANTIFIRE_POTION(new int[] {21987, 21984, 21981, 21978}) {
        @Override
        public void onConsumption(final Player player) {
            ConsumableEffects.applySuperAntifire(player, 300);
        }
    },
    EXTENDED_SUPER_ANTIFIRE(new int[] {22218, 22215, 22212, 22209}) {
        @Override
        public void onConsumption(final Player player) {
            ConsumableEffects.applySuperAntifire(player, 600);
        }
    },
    RESTORE_POTION(new int[] {131, 129, 127, 2430}, Consumable.getRestorations(0.3F, 10, SkillConstants.DEFENCE, SkillConstants.ATTACK, SkillConstants.STRENGTH, SkillConstants.MAGIC, SkillConstants.RANGED)),
    SUPER_RESTORE_POTION(new int[] {3030, 3028, 3026, 3024}, Consumable.getRestorations(0.25F, 8, Skills.getAllSkillsExcluding(SkillConstants.HITPOINTS))) {
        @Override public void onConsumption(Player player) {
            player.getCombatAchievements().removeCurrentTaskFlag(CAType.PRAYING_TO_THE_GODS, ChaosFanatic.CA_TASK_PRAYING_TO_THE_GODS_STARTED);
        }
    },
    SANFEW_SERUM(new int[] {10931, 10929, 10927, 10925}, Consumable.getRestorations(0.25F, 8, Skills.getAllSkillsExcluding(SkillConstants.HITPOINTS))) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().cureToxin(Toxins.ToxinType.POISON);
            player.getToxins().cureToxin(Toxins.ToxinType.DISEASE);
            player.getVariables().schedule(600, TickVariable.POISON_IMMUNITY);
            if (player.getVarManager().getBitValue(10151) > 0) {
                player.getVarManager().sendBit(10151, 0);
                player.sendMessage("<col=229628>The parasite within you has been weakened.");
            }
            player.getCombatAchievements().removeCurrentTaskFlag(CAType.PRAYING_TO_THE_GODS, ChaosFanatic.CA_TASK_PRAYING_TO_THE_GODS_STARTED);
        }

        @Override
        public String startMessage() {
            return "You drink some of your Sanfew serum.";
        }
    },
    OVERLOAD(new int[] {11733, 11732, 11731, 11730}) {
        @Override
        public boolean canConsume(final Player player) {
            final int overload = player.getVariables().getTime(TickVariable.OVERLOAD);
            if (overload > 0) {
                player.sendMessage("You are still suffering the effects of a fresh dose of overload.");
                return false;
            }
            if (player.getHitpoints() < 50) {
                player.sendMessage("You need at least 50 hitpoints to survive the overload.");
                return false;
            }
            if (WildernessArea.isWithinWilderness(player)) {
                player.sendMessage("You cannot drink an overload potion in the wilderness.");
                return false;
            }
            return true;
        }

        @Override
        public void onConsumption(final Player player) {
            ConsumableEffects.damagePlayer(player, 5);
            player.getVariables().schedule(500, TickVariable.OVERLOAD);
            player.getVariables().setOverloadType((short) 3);
            ConsumableEffects.applyOverload(player);
        }
    },
    GUTHIX_REST(new int[] {ItemId.GUTHIX_REST1, ItemId.GUTHIX_REST2, ItemId.GUTHIX_REST3, ItemId.GUTHIX_REST4}, new Boost(SkillConstants.HITPOINTS, 0, 5)) {
        @Override
        public String startMessage() {
            return "You drink the herbal tea.";
        }

        @Override
        public String endMessage(Player player) {
            return "The potion restores some energy.";
        }

        @Override
        public String emptyMessage(final Player player) {
            return "You have finished your potion.";
        }

        public Item vial() {
            return new Item(ItemId.EMPTY_CUP);
        }

        @Override
        public void onConsumption(final Player player) {
            if (player.getHitpoints() < (player.getMaxHitpoints() + 5)) {
                player.sendMessage("The tea boosts your hitpoints.");
            }
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 5);
            if (player.getToxins().isVenomed()) {
                player.getToxins().cureToxin(Toxins.ToxinType.VENOM);
            } else if (player.getToxins().isPoisoned()) {
                player.getToxins().setDamage(player.getToxins().getDamage() - 1);
                if (player.getToxins().getDamage() <= 0) {
                    player.getToxins().cureToxin(Toxins.ToxinType.POISON);
                }
            }
        }
    },
    ABSORPTION(new int[] {11737, 11736, 11735, 11734}) {
        @Override
        public boolean canConsume(final Player player) {
            if (!player.inArea("Nightmare zone")) {
                player.sendMessage("You can only sip the potion while in the Nightmare zone.");
                return false;
            }
            return true;
        }

        @Override
        public void onConsumption(Player player) {
            player.getVariables().setAbsorption(Math.min(1000, player.getVariables().getAbsorption() + 50));
        }
    },
    EGNIOL(new int[] {ItemId.EGNIOL_POTION_1, ItemId.EGNIOL_POTION_2, ItemId.EGNIOL_POTION_3, ItemId.EGNIOL_POTION_4}, new Restoration(SkillConstants.PRAYER, 0.25F, 7)) {
        @Override
        public boolean canConsume(Player player) {
            if (!player.getTemporaryAttributes().containsKey("gauntlet")) {
                player.sendMessage("You can only drink this potion inside the Gauntlet.");
                return false;
            }
            return true;
        }

        @Override
        public void onConsumption(Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 40);
            player.getVariables().schedule(200, TickVariable.STAMINA_ENHANCEMENT);
            player.getVarManager().sendBit(25, 1);
        }
    },
    FORGOTTEN_BREW(new int[] { ItemId.FORGOTTEN_BREW1, ItemId.FORGOTTEN_BREW2, ItemId.FORGOTTEN_BREW3, ItemId.FORGOTTEN_BREW4 }, new Restoration(SkillConstants.PRAYER, 0.25F, 8), new Boost(SkillConstants.MAGIC, 0.08F, 30), new Debuff(SkillConstants.STRENGTH, 0.1F, 2), new Debuff(SkillConstants.ATTACK, 0.1F, 2), new Debuff(SkillConstants.DEFENCE, 0.1F, 2)),
    TOA_NECTAR(new int[]{ItemId.NECTAR_1, ItemId.NECTAR_2, ItemId.NECTAR_3, ItemId.NECTAR_4}, new Boost(SkillConstants.HITPOINTS, 0.15F, 3), new Debuff(SkillConstants.DEFENCE, 0.05F, 5), new Debuff(SkillConstants.STRENGTH, 0.05F, 5), new Debuff(SkillConstants.ATTACK, 0.05F, 5), new Debuff(SkillConstants.MAGIC, 0.05F, 5), new Debuff(SkillConstants.RANGED, 0.05F, 5)) {

        @Override
        public boolean canConsume(final Player player) {
            return super.canConsume(player) && player.getArea() instanceof AbstractTOARaidArea;
        }

        @Override
        public String startMessage() {
            return "You drink some of the nectar. It hurts! This was not made for mortals.";
        }

        @Override
        public Item leftoverItem(int id) {
            final int[] ids = getIds();
            final int index = ArrayUtils.indexOf(ids, id);
            return index == 0 ? null : new Item(ids[index - 1]);
        }
    },
    TOA_TEARS(new int[]{ItemId.TEARS_OF_ELIDINIS_1, ItemId.TEARS_OF_ELIDINIS_2, ItemId.TEARS_OF_ELIDINIS_3, ItemId.TEARS_OF_ELIDINIS_4}, Consumable.getRestorations(0.25F, 3, Skills.getAllSkillsExcluding(SkillConstants.HITPOINTS, SkillConstants.PRAYER))) {

        @Override
        public boolean canConsume(final Player player) {
            return  super.canConsume(player) && player.getArea() instanceof AbstractTOARaidArea;
        }

        @Override
        public void onConsumption(Player player) {
            if (player.getArea() instanceof final AbstractTOARaidArea area) {
                player.getPrayerManager().restorePrayerPoints((player.getSkills().getLevelForXp(SkillConstants.PRAYER) / 4) + 10);
                for (Player p : area.getPlayers()) {
                    if (p != null && !p.getAppearance().isTransformedIntoNpc() && !p.isFinished() && !p.isDying()
                            && player.getLocation().getTileDistance(p.getLocation()) <= 1 && !ProjectileUtils.isProjectileClipped(player, p, player.getPosition(), p.getPosition(), true)
                            && p.getPrayerManager().getPrayerPoints() < p.getSkills().getLevelForXp(Skills.PRAYER)) {
                        p.getPrayerManager().restorePrayerPoints((p.getSkills().getLevelForXp(SkillConstants.PRAYER) / 10) + 10);
                        p.sendMessage(player.getName() + " has restored some of your prayer points.");
                    }
                }
            }
        }

        @Override
        public Item leftoverItem(int id) {
            final int[] ids = getIds();
            final int index = ArrayUtils.indexOf(ids, id);
            return index == 0 ? null : new Item(ids[index - 1]);
        }
    },

    TOA_AMBROSIA(new int[] {ItemId.AMBROSIA_1, ItemId.AMBROSIA_2}) {
        @Override
        public boolean canConsume(final Player player) {
            if (player.getArea() instanceof final AbstractTOARaidArea area) {
                if (area.isDeHydration()) {
                    player.sendMessage("You've been prevented from drinking this potion within the Tombs of Amascut");
                    return false;
                }
                return super.canConsume(player);
            }
            return false;
        }

        @Override
        public String startMessage() {
            return "You drink the ambrosia. You feel reinvigorated.";
        }

        @Override
        public void onConsumption(Player player) {
            player.getVariables().setRunEnergy(100);
            player.getToxins().cureToxin(Toxins.ToxinType.POISON);
            player.getToxins().cureToxin(Toxins.ToxinType.VENOM);
            player.getVariables().schedule(1200, TickVariable.POISON_IMMUNITY);
            player.setHitpoints(player.getMaxHitpoints() + (int) Math.floor(player.getSkills().getLevelForXp(Skills.HITPOINTS) * .25F) + 2);
            player.getPrayerManager().setPrayerPoints(player.getPrayerManager().getPrayerPoints() + (int) Math.floor(player.getSkills().getLevelForXp(Skills.PRAYER) * .2F) + 5);
        }

        @Override
        public Item leftoverItem(int id) {
            final int[] ids = getIds();
            final int index = ArrayUtils.indexOf(ids, id);
            return index == 0 ? null : new Item(ids[index - 1]);
        }
    },

    TOA_SALT(new int[] {ItemId.SMELLING_SALTS_1, ItemId.SMELLING_SALTS_2}) {

        @Override
        public boolean canConsume(final Player player) {
            return super.canConsume(player) && player.getArea() instanceof AbstractTOARaidArea;
        }

        @Override
        public String startMessage() {
            return "You crush the salts. Your heart rate increases.";
        }

        @Override
        public void onConsumption(Player player) {
            player.getVariables().schedule(800, TickVariable.SALT);
            ConsumableEffects.applySalt(player);
        }

        @Override
        public String endMessage(Player player) {
            return "You have 1 dose left.";
        }

        @Override
        public String emptyMessage(final Player player) {
            return "You've used all of the remaining salt.";
        }

        @Override
        public Item leftoverItem(int id) {
            final int[] ids = getIds();
            final int index = ArrayUtils.indexOf(ids, id);
            return index == 0 ? null : new Item(ids[index - 1]);
        }
    },
    BLESSED_CRYSTAL_SCARAB(new int[] {ItemId.BLESSED_CRYSTAL_SCARAB_1, ItemId.BLESSED_CRYSTAL_SCARAB_2}) {
        @Override
        public boolean canConsume(final Player player) {
            return super.canConsume(player) && player.getArea() instanceof AbstractTOARaidArea;
        }

        @Override
        public String startMessage() {
            return "You consume the blessed crystal scarab.";
        }

        @Override
        public void onConsumption(Player player) {
            player.getVariables().schedule(36, TickVariable.BLESSED_SCARAB);
        }

        @Override
        public String endMessage(Player player) {
            return "You have 1 dose left.";
        }

        @Override
        public String emptyMessage(final Player player) {
            return "There is nothing left.";
        }

        @Override
        public Item leftoverItem(int id) {
            final int[] ids = getIds();
            final int index = ArrayUtils.indexOf(ids, id);
            return index == 0 ? null : new Item(ids[index - 1]);
        }
    },
    TOA_ADRENALINE(new int[] {ItemId.LIQUID_ADRENALINE_1, ItemId.LIQUID_ADRENALINE_2}) {

        @Override
        public boolean canConsume(final Player player) {
            return super.canConsume(player) &&  player.getArea() instanceof AbstractTOARaidArea;
        }

        @Override
        public String startMessage() {
            return "You drink some of the potion, reducing the energy cost of your special attacks.</col>";
        }

        @Override
        public void onConsumption(Player player) {
            player.getVariables().schedule(250, TickVariable.LIQUID_ADRENALINE);
        }

        @Override
        public Item leftoverItem(int id) {
            final int[] ids = getIds();
            final int index = ArrayUtils.indexOf(ids, id);
            return index == 0 ? null : new Item(ids[index - 1]);
        }
    },

    BLIGHTED_SUPER_RESTORE(new int[] {ItemId.BLIGHTED_SUPER_RESTORE1, ItemId.BLIGHTED_SUPER_RESTORE2, ItemId.BLIGHTED_SUPER_RESTORE3, ItemId.BLIGHTED_SUPER_RESTORE4}) {
        @Override
        public void onConsumption(Player player) {
            player.getCombatAchievements().removeCurrentTaskFlag(CAType.PRAYING_TO_THE_GODS, ChaosFanatic.CA_TASK_PRAYING_TO_THE_GODS_STARTED);
            final int restoreAmount = (int) (8 + player.getSkills().getLevel(SkillConstants.PRAYER) * 0.27);
            final Boost[] boosts = Consumable.getRestorations(0.25F, restoreAmount, Skills.getAllSkillsExcluding(SkillConstants.HITPOINTS));
            for (Boost boost : boosts)
                boost.apply(player);
        }

        @Override
        public boolean canConsume(Player player) {

            if (super.canConsume(player)) {
                if (!WildernessArea.isWithinWilderness(player)) {
                    player.sendMessage("The blighted potion can be used only in the Wilderness.");
                    return false;
                }
                return true;
            } else
                return false;
        }
    },
    ;

    Potion(final int[] ids, final Boost... boosts) {
        this.ids = ids;
        this.boosts = boosts;
    }

    private static final Item vial = new Item(229);
    /**
     * The ids of the potion. These go in ascending order, so effectively {@code { 1dose, 2doses, 3doses, 4doses }}.
     */
    private final int[] ids;
    private final Boost[] boosts;
    public static final Potion[] values = values();
    public static final Int2ObjectMap<Potion> POTIONS = new Int2ObjectOpenHashMap<>();

    static {
        for (final Potion potion : values) {
            for (final int id : potion.getIds()) {
                final int noted = ItemDefinitions.getOrThrow(id).getNotedId();
                if (noted != -1) {
                    POTIONS.put(noted, potion);
                }
                POTIONS.put(id, potion);
            }
        }
    }

    public static Potion get(final int id) {
        return POTIONS.get(id);
    }

    @Override
    public Item leftoverItem(int id) {
        final int index = ArrayUtils.indexOf(ids, id);
        if (index == -1) {
            throw new RuntimeException("Invalid id: " + id + " " + this);
        }
        if (index == 0) {
            return new Item(vial());
        }
        return new Item(ids[index - 1]);
    }

    @Override
    public Item getItem(final int doses) {
        if (doses < 0 || doses > ids.length) throw new RuntimeException("The potion " + this + " doesn't support dose " + doses + ".");
        if (doses == 0) {
            return new Item(vial());
        }
        return new Item(ids[doses - 1]);
    }

    public Item vial() {
        return vial;
    }

    @Override
    public Boost[] boosts() {
        return boosts;
    }

    @Override
    public String startMessage() {
        return "You drink some of your %s.";
    }

    @Override
    public String endMessage(Player player) {
        return "You have %d dose%s of potion left.";
    }

    @Override
    public int getDoses(int id) {
        final int index = ArrayUtils.indexOf(ids, id);
        if (index == -1) {
            throw new RuntimeException("Invalid id: " + id + " " + this);
        }
        return index + 1;
    }

    @Override
    public String emptyMessage(final Player player) {
        return "You have finished your potion.";
    }

    public int[] getIds() {
        return ids;
    }
}
