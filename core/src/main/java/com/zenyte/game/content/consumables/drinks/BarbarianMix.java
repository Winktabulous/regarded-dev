package com.zenyte.game.content.consumables.drinks;

import com.zenyte.game.content.consumables.Consumable;
import com.zenyte.game.content.consumables.ConsumableEffects;
import com.zenyte.game.content.consumables.Drinkable;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Kris | 02/12/2018 15:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum BarbarianMix implements Drinkable {
    ATTACK_MIX(new int[] {11431, 11429}, 0, new Boost(SkillConstants.ATTACK, 0.12F, 0)), STRENGTH_MIX(new int[] {11443, 11441}, 3, new Boost(SkillConstants.STRENGTH, 0.1F, 3)), COMBAT_MIX(new int[] {11447, 11445}, 6, new Boost(SkillConstants.ATTACK, 0.1F, 3), new Boost(SkillConstants.STRENGTH, 0.1F, 3)), DEFENCE_MIX(new int[] {11459, 11457}, 6, new Boost(SkillConstants.DEFENCE, 0.1F, 3)), AGILITY_MIX(new int[] {11463, 11461}, 6, new Boost(SkillConstants.AGILITY, 0, 3)), SUPERATTACK_MIX(new int[] {11471, 11469}, 6, new Boost(SkillConstants.ATTACK, 0.15F, 5)), FISHING_MIX(new int[] {11479, 11477}, 2, new Boost(SkillConstants.FISHING, 0, 3)), SUPER_STR_MIX(new int[] {11487, 11485}, 6, new Boost(SkillConstants.STRENGTH, 0.15F, 5)), MAGIC_ESSENCE_MIX(new int[] {11491, 11489}, 6, new Boost(SkillConstants.MAGIC, 0, 3)), SUPER_DEF_MIX(new int[] {11499, 11497}, 6, new Boost(SkillConstants.DEFENCE, 0.15F, 5)), RANGING_MIX(new int[] {11511, 11509}, 6, new Boost(SkillConstants.RANGED, 0.1F, 4)), MAGIC_MIX(new int[] {11515, 11513}, 0, new Boost(SkillConstants.MAGIC, 0, 4)) {
        @Override
        public int healedAmount(final Player player) {
            return Utils.random(3, 6);
        }
    },
    HUNTING_MIX(new int[] {11519, 11517}, 6, new Boost(SkillConstants.HUNTER, 0, 3)), ANTIPOISON_MIX(new int[] {11435, 11433}, 3) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().cureToxin(Toxins.ToxinType.POISON);
            player.getVariables().schedule(150, TickVariable.POISON_IMMUNITY);
        }
    },
    RELICYMS_MIX(new int[] {11439, 11437}, 3) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().weakenDisease();
            if (player.getVarManager().getBitValue(10151) > 0) {
                player.getVarManager().sendBit(10151, 0);
                player.sendMessage("<col=229628>The parasite within you has been weakened.");
            }
        }
    },
    RESTORE_MIX(new int[] {11451, 11449}, 6, Consumable.getRestorations(0.3F, 10, Skills.getAllSkillsExcluding(SkillConstants.HITPOINTS, SkillConstants.AGILITY, SkillConstants.PRAYER))), ENERGY_MIX(new int[] {11455, 11453}, 6) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 10);
        }
    },
    PRAYER_MIX(new int[] {11467, 11465}, 6, new Restoration(SkillConstants.PRAYER, 0.25F, 7)), ANTIPOISON_SUPERMIX(new int[] {11475, 11473}, 6) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().cureToxin(Toxins.ToxinType.POISON);
            player.getVariables().schedule(600, TickVariable.POISON_IMMUNITY);
        }
    },
    SUPER_ENERGY_MIX(new int[] {11483, 11481}, 6) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 20);
        }
    },
    SUPER_RESTORE_MIX(new int[] {11495, 11493}, 6, Consumable.getRestorations(0.25F, 8, Skills.getAllSkillsExcluding(SkillConstants.HITPOINTS))), ANTIDOTE_PLUS_MIX(new int[] {11503, 11501}, 2) {
        @Override
        public void onConsumption(final Player player) {
            player.getToxins().cureToxin(Toxins.ToxinType.POISON);
            player.getVariables().schedule(900, TickVariable.POISON_IMMUNITY);
        }
    },
    ANTIFIRE_MIX(new int[] {11507, 11505}, 0) {
        @Override
        public void onConsumption(final Player player) {
            ConsumableEffects.applyAntifire(player, 600);
        }

        @Override
        public int healedAmount(final Player plauer) {
            return Utils.random(3, 6);
        }
    },
    ZAMORAK_MIX(new int[] {11523, 11521}, 6, new Boost(SkillConstants.ATTACK, 0.2F, 2), new Boost(SkillConstants.STRENGTH, 0.12F, 2), new Debuff(SkillConstants.DEFENCE, 0.1F, 2), new Restoration(SkillConstants.PRAYER, 0.1F, 0)) {
        @Override
        public String startMessage() {
            return "You drink some of the foul liquid.";
        }

        @Override
        public void onConsumption(final Player player) {
            player.applyHit(new Hit((int) Math.floor(player.getHitpoints() * 0.12F), HitType.REGULAR));
        }
    },
    EXTENDED_ANTIFIRE_MIX(new int[] {11962, 11960}, 6) {
        @Override
        public void onConsumption(final Player player) {
            ConsumableEffects.applyAntifire(player, 1200);
        }
    },
    STAMINA_MIX(new int[] {12635, 12633}, 6) {
        @Override
        public void onConsumption(final Player player) {
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 20);
            player.getVariables().schedule(200, TickVariable.STAMINA_ENHANCEMENT);
            player.getVarManager().sendBit(25, 1);
        }
    },
    SUPER_ANTIFIRE_MIX(new int[] {21997, 21994}, 6) {
        @Override
        public void onConsumption(final Player player) {
            ConsumableEffects.applySuperAntifire(player, 300);
        }
    },
    EXTENDED_SUPER_ANTIFIRE_MIX(new int[] {22224, 22221}, 6) {
        @Override
        public void onConsumption(final Player player) {
            ConsumableEffects.applySuperAntifire(player, 600);
        }
    };

    BarbarianMix(final int[] ids, final int heal, final Boost... boosts) {
        this.ids = ids;
        this.heal = heal;
        this.boosts = boosts;
    }

    private static final Item vial = new Item(229);
    /**
     * The ids of the potion. These go in ascending order, so effectively {@code { 1dose, 2doses, 3doses, 4doses }}.
     */
    private final int[] ids;
    private final int heal;
    private final Boost[] boosts;
    public static final BarbarianMix[] values = values();

    @Override
    public Item leftoverItem(int id) {
        final int index = ArrayUtils.indexOf(ids, id);
        if (index == -1) {
            throw new RuntimeException("Invalid id: " + id + " " + this);
        }
        if (index == 0) {
            return new Item(vial);
        }
        return new Item(ids[index - 1]);
    }

    @Override
    public Item getItem(final int doses) {
        if (doses < 0 || doses > ids.length) throw new RuntimeException("The mix " + this + " doesn't support dose " + doses + ".");
        if (doses == 0) {
            return vial;
        }
        return new Item(ids[doses - 1]);
    }

    @Override
    public int healedAmount(final Player player) {
        return heal;
    }

    @Override
    public Boost[] boosts() {
        return boosts;
    }

    @Override
    public String startMessage() {
        return "You drink the lumpy potion.";
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
