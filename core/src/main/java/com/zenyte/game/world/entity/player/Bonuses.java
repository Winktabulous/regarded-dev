package com.zenyte.game.world.entity.player;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.plugins.item.Blowpipe;
import mgi.types.config.items.ItemDefinitions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A utility class for manipulating & setting player bonuses.
 *
 * @author Mack
 */
public class Bonuses {
    private final Player player;
    private final Map<Integer, Integer> bonuses;

    /**
     * Creates a new {@link Bonuses} instance for the player.
     *
     * @param player
     */
    public Bonuses(final Player player) {
        this.player = player;
        bonuses = new HashMap<>();
    }

    public void setBonus(final int index, final int bonus) {
        bonuses.put(index, bonus);
    }

    /**
     * Updates the bonus information.
     */
    public void update() {
        if (!bonuses.isEmpty()) {
            bonuses.clear();
        }
        for (int equipmentIndex = 0; equipmentIndex < Equipment.SIZE; equipmentIndex++) {
            final Item item = player.getEquipment().getItem(equipmentIndex);
            if (Objects.nonNull(item) && Objects.nonNull(item.getDefinitions())) {
                final ItemDefinitions defs = item.getDefinitions();
                final int[] data = defs.getBonuses();
                if (data == null) continue;
                for (final Bonus bonus : Bonus.VALUES) {
                    if (bonus.getBonusIndex() >= data.length) continue;
                    final int existing_value = bonuses.getOrDefault(bonus.getBonusIndex(), 0);
                    int b = data[bonus.getBonusIndex()];
                    if (b == 0) continue;
                    final int id = item.getId();
                    if (equipmentIndex == 2 && bonus == Bonus.PRAYER && (id == 12851 || id == 12853)) {
                        if (CombatUtilities.hasFullBarrowsSet(player, "Verac's")) b += 4;
                    }
                    if (equipmentIndex == 3 && bonus.equals(Bonus.ATT_RANGED)) {
                        final Item wep = player.getWeapon();
                        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON.getSlot());
                        if (weaponId == 12926 && Blowpipe.getBlowpipeAmmunition(wep) != -1) {
                            final ItemDefinitions ammo = ItemDefinitions.get(Blowpipe.getBlowpipeAmmunition(wep));
                            if (ammo == null || ammo.getBonuses() == null) continue;
                            b += ammo.getBonuses()[Bonus.ATT_RANGED.bonusIndex];
                        }
                    }
                    else if (equipmentIndex == 3 && bonus.equals(Bonus.RANGE_STRENGTH)) {
                        final Item wep = player.getWeapon();
                        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON.getSlot());
                        if (weaponId == 10033) continue;
                        if (weaponId == 12926 && Blowpipe.getBlowpipeAmmunition(wep) != -1) {
                            final ItemDefinitions ammo = ItemDefinitions.get(Blowpipe.getBlowpipeAmmunition(wep));
                            if (ammo == null || ammo.getBonuses() == null) continue;
                            b += ammo.getBonuses()[Bonus.RANGE_STRENGTH.bonusIndex];
                        }
                    }
                    else if (equipmentIndex == 13 && bonus.equals(Bonus.RANGE_STRENGTH)) {
                        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON.getSlot());
                        if (weaponId == 10033 || weaponId == 12926 || weaponId == 7170) {
                            continue;
                        }
                        if (weaponId != -1 && weaponId != ItemId.HEAVY_BALLISTA && weaponId != ItemId.HEAVY_BALLISTA_OR && !CombatUtilities.isTwistedBow(weaponId)) {
                            final ItemDefinitions weapon = ItemDefinitions.get(weaponId);
                            if (weapon == null || weapon.getBonuses() != null && weapon.getBonuses()[11] != 0) continue;
                        }
                    }
                    bonuses.put(bonus.getBonusIndex(), existing_value + b);
                }
            }
        }
        final boolean stats = player.getInterfaceHandler().isVisible(84);
        final boolean bank = player.getInterfaceHandler().isVisible(GameInterface.BANK.getId());
        if (stats || bank) {
            final int interfaceId = stats ? 84 : GameInterface.BANK.getId();
            for (final Bonus bonus : Bonus.VALUES) {
                final int child = stats ? bonus.getChildId() : bonus.getBankChildId();
                if (bonus.equals(Bonus.UNDEAD)) {
                    final int necklace = player.getEquipment().getId(EquipmentSlot.AMULET);
                    final int percentage = necklace == ItemId.SALVE_AMULET || necklace == ItemId.SALVE_AMULETI ? 15 :
							necklace == ItemId.SALVE_AMULET_E || necklace == ItemId.SALVE_AMULETEI ? 20 : 0;
                    final String suffix = necklace == ItemId.SALVE_AMULET || necklace == ItemId.SALVE_AMULET_E ? " " +
							"(melee)" : necklace == ItemId.SALVE_AMULETI || necklace == ItemId.SALVE_AMULETEI ? " (all" +
							" styles)" : "";
                    player.getPacketDispatcher().sendComponentText(interfaceId, child, bonus.getBonusName() + ": "
							+ percentage + "%" + suffix);
                    continue;
                } else if (bonus.equals(Bonus.SLAYER)) {
                    final Item helmet = player.getHelmet();
                    String percentage = "0";
                    String suffix = "";
                    if (helmet != null) {
                        final String name = helmet.getName().toLowerCase();
                        if (name.contains("black mask") || name.contains("slayer helm")) {
                            final boolean allStyles = name.contains("(i)");
                            percentage = "15";
                            suffix = allStyles ? " (all styles)" : " (melee)";
                        }
                    }
                    player.getPacketDispatcher().sendComponentText(interfaceId, child, bonus.getBonusName() + ": "
							+ percentage + "%" + suffix);
                    continue;
                }
                final int value = bonuses.getOrDefault(bonus.getBonusIndex(), 0);
                String prefix = "";
                String suffix = "";
                if (value >= 0) {
                    prefix = "+";
                } else {
                    prefix = "";
                }
                if (bonus.equals(Bonus.MAGIC_DAMAGE)) {
                    suffix = "%";
                }
                player.getPacketDispatcher().sendComponentText(interfaceId, child, bonus.getBonusName() + ": " + prefix + value + suffix);
            }
        }
    }


    public enum Bonus {
        ATT_STAB(0, 24, "Stab", 90),
        ATT_SLASH(1, 25, "Slash", 91),
        ATT_CRUSH(2, 26, "Crush", 92),
        ATT_MAGIC(3, 27, "Magic", 93),
        ATT_RANGED(4, 28, "Range", 94),
        DEF_STAB(5, 30, "Stab", 96),
        DEF_SLASH(6, 31, "Slash", 97),
        DEF_CRUSH(7, 32, "Crush", 98),
        DEF_MAGIC(8, 33, "Magic", 99),
        DEF_RANGE(9, 34, "Range", 100),
        STRENGTH(10, 36, "Melee strength", 102),
        RANGE_STRENGTH(11, 37, "Ranged strength", 103),
        MAGIC_DAMAGE(12, 38, "Magic damage", 104),
        PRAYER(13, 39, "Prayer", 105),
        UNDEAD(14, 41, "Undead", 107),
        SLAYER(15, 42, "Slayer", 108);

        private final int bonusIndex;
        private final int childId;

        public int getBankChildId() {
            return bankChildId;
        }

        private final int bankChildId;
        private final String bonusName;
        public static final Bonus[] VALUES = values();

        Bonus(final int bonusIndex, final int childId, final String bonusName, int bankChildId) {
            this.bankChildId = bankChildId;
            this.bonusIndex = bonusIndex;
            this.childId = childId;
            this.bonusName = bonusName;
        }

        public int getBonusIndex() {
            return bonusIndex;
        }

        public int getChildId() {
            return childId;
        }

        public String getBonusName() {
            return bonusName;
        }
    }

    /**
     * Gets the bonus value for the specified index.
     *
     * @param bonusIndex
     * @return
     */
    public int getBonus(final int bonusIndex) {
        return bonuses.getOrDefault(bonusIndex, 0);
    }

    public int getBonus(final Bonus bonus) {
        return getBonus(bonus.bonusIndex);
    }

    public int getDefenceBonus(final AttackType type) {
        final Bonuses.Bonus bonus = type.isMagic() ? Bonus.DEF_MAGIC : type.isRanged() ? Bonus.DEF_RANGE : type == AttackType.CRUSH ? Bonus.DEF_CRUSH : type == AttackType.SLASH ? Bonus.DEF_SLASH : Bonus.DEF_STAB;
        return getBonus(bonus.getBonusIndex());
    }
}
