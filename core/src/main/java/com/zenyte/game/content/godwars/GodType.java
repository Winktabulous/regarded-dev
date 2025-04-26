package com.zenyte.game.content.godwars;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import dev.kord.core.entity.ReactionEmoji;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import mgi.utilities.StringFormatUtil;

public enum GodType {
    BANDOS(3975, new int[]{
            CustomItemId.BANDOS_MAX_CAPE,
            CustomItemId.IMBUED_BANDOS_CAPE,
            CustomItemId.BANDOS_BOW,
            CustomItemId.BANDOS_CHESTPLATE_OR,
            CustomItemId.BANDOS_TASSETS_OR,
            ItemId.BANDOS_CHESTPLATE_OR,
            ItemId.BANDOS_TASSETS_OR,
            ItemId.ANCIENT_MACE,
            ItemId.BANDOS_GODSWORD,
            ItemId.BANDOS_CHESTPLATE,
            ItemId.BANDOS_TASSETS,
            ItemId.BANDOS_BOOTS,
            ItemId.BANDOS_ROBE_TOP,
            ItemId.BANDOS_ROBE_LEGS,
            ItemId.BANDOS_STOLE,
            ItemId.BANDOS_MITRE,
            ItemId.BANDOS_CLOAK,
            ItemId.BANDOS_CROZIER,
            ItemId.BANDOS_PLATEBODY,
            ItemId.BANDOS_PLATELEGS,
            ItemId.BANDOS_PLATESKIRT,
            ItemId.BANDOS_FULL_HELM,
            ItemId.BANDOS_KITESHIELD,
            ItemId.BANDOS_BRACERS,
            ItemId.BANDOS_DHIDE,
            ItemId.BANDOS_CHAPS,
            ItemId.BANDOS_COIF,
            ItemId.BANDOS_DHIDE_BOOTS,
            ItemId.BANDOS_GODSWORD_OR,
            ItemId.BANDOS_GODSWORD_20782,
            ItemId.BANDOS_GODSWORD_21060,
            ItemId.WAR_BLESSING,
            ItemId.DAMAGED_BOOK_12607,
            ItemId.BOOK_OF_WAR,
            ItemId.GUARDIAN_BOOTS,
            ItemId.BANDOS_DHIDE_SHIELD
    }),

    ZAMORAK(3976, new int[]{
            CustomItemId.ZAMORAK_BOW,
            CustomItemId.IMBUED_ANCIENT_CAPE,
            CustomItemId.ANCIENT_MAX_CAPE,
            ItemId.ZAMORAK_MONK_BOTTOM,
            ItemId.ZAMORAK_MONK_TOP,
            ItemId.ZAMORAK_CAPE,
            ItemId.ZAMORAK_STAFF,
            ItemId.ZAMORAK_PLATEBODY,
            ItemId.ZAMORAK_PLATELEGS,
            ItemId.ZAMORAK_FULL_HELM,
            ItemId.ZAMORAK_KITESHIELD,
            ItemId.ZAMORAK_PLATESKIRT,
            ItemId.ZAMORAK_MJOLNIR,
            ItemId.ZAMORAK_BRACERS,
            ItemId.ZAMORAK_DHIDE,
            ItemId.ZAMORAK_CHAPS,
            ItemId.ZAMORAK_COIF,
            ItemId.ZAMORAK_CROZIER,
            ItemId.ZAMORAK_CLOAK,
            ItemId.ZAMORAK_MITRE,
            ItemId.ZAMORAK_ROBE_TOP,
            ItemId.ZAMORAK_ROBE_LEGS,
            ItemId.ZAMORAK_STOLE,
            ItemId.ZAMORAK_PLATEBODY_10776,
            ItemId.ZAMORAK_ROBE_TOP_10786,
            ItemId.ZAMORAK_DHIDE_10790,
            ItemId.ZAMORAK_GODSWORD,
            ItemId.ZAMORAKIAN_SPEAR,
            ItemId.ZAMORAKIAN_HASTA,
            ItemId.ZAMORAK_HALO,
            ItemId.ZAMORAK_MAX_CAPE,
            ItemId.ZAMORAK_MAX_HOOD,
            ItemId.ZAMORAK_DHIDE_BOOTS,
            ItemId.ZAMORAK_GODSWORD_OR,
            ItemId.IMBUED_ZAMORAK_MAX_CAPE,
            ItemId.IMBUED_ZAMORAK_MAX_HOOD,
            ItemId.IMBUED_ZAMORAK_CAPE,
            ItemId.UNHOLY_BLESSING,
            ItemId.UNHOLY_SYMBOL,
            ItemId.DAMAGED_BOOK_3841,
            ItemId.UNHOLY_BOOK,
            ItemId.UNHOLY_SYMBOL_4683,
            ItemId.STAFF_OF_THE_DEAD,
            ItemId.TOXIC_STAFF_OF_THE_DEAD,
            ItemId.VIGGORAS_CHAINMACE_U,
            ItemId.VIGGORAS_CHAINMACE,
            ItemId.DRAGON_HUNTER_LANCE,
            ItemId.THAMMARONS_SCEPTRE,
            ItemId.THAMMARONS_SCEPTRE_U,
            ItemId.ZAMORAK_DHIDE_SHIELD,
            ItemId.ELDER_CHAOS_HOOD,
            ItemId.ELDER_CHAOS_ROBE,
            ItemId.ELDER_CHAOS_TOP,
            ItemId.STAFF_OF_LIGHT,
            27662,
            27665
    }),

    SARADOMIN(3972, new int[]{
            CustomItemId.SARADOMIN_BOW,
            CustomItemId.SEREN_MAX_CAPE,
            CustomItemId.IMBUED_SEREN_CAPE,
            ItemId.SARADOMIN_CAPE,
            ItemId.SARADOMIN_STAFF,
            ItemId.SARADOMIN_PLATEBODY,
            ItemId.SARADOMIN_PLATELEGS,
            ItemId.SARADOMIN_FULL_HELM,
            ItemId.SARADOMIN_KITESHIELD,
            ItemId.SARADOMIN_PLATESKIRT,
            ItemId.SARADOMIN_MJOLNIR,
            ItemId.SARADOMIN_SYMBOL,
            ItemId.SARADOMIN_BRACERS,
            ItemId.SARADOMIN_DHIDE,
            ItemId.SARADOMIN_CHAPS,
            ItemId.SARADOMIN_COIF,
            ItemId.SARADOMIN_CROZIER,
            ItemId.SARADOMIN_CLOAK,
            ItemId.SARADOMIN_MITRE,
            ItemId.SARADOMIN_ROBE_TOP,
            ItemId.SARADOMIN_ROBE_LEGS,
            ItemId.SARADOMIN_STOLE,
            ItemId.SARADOMIN_PLATE,
            ItemId.SARADOMIN_ROBE_TOP_10784,
            ItemId.SARADOMIN_DHIDE_10792,
            ItemId.SARADOMIN_GODSWORD,
            ItemId.SARADOMIN_SWORD,
            ItemId.SARADOMIN_HALO,
            ItemId.SARAS_BLESSED_SWORD_FULL,
            ItemId.SARADOMINS_BLESSED_SWORD,
            ItemId.SARADOMIN_MAX_CAPE,
            ItemId.SARADOMIN_MAX_HOOD,
            ItemId.SARADOMIN_DHIDE_BOOTS,
            ItemId.SARADOMIN_GODSWORD_OR,
            ItemId.IMBUED_SARADOMIN_MAX_CAPE,
            ItemId.IMBUED_SARADOMIN_MAX_HOOD,
            ItemId.IMBUED_SARADOMIN_CAPE,
            ItemId.HOLY_SYMBOL,
            ItemId.HOLY_SYMBOL_4682,
            ItemId.MONKS_ROBE,
            ItemId.MONKS_ROBE_TOP,
            ItemId.HOLY_BOOK,
            26496,
            ItemId.HOLY_BLESSING,
            ItemId.DAMAGED_BOOK,
            ItemId.HOLY_SANDALS,
            ItemId.DEVOUT_BOOTS,
            ItemId.JUSTICIAR_FACEGUARD,
            ItemId.JUSTICIAR_CHESTGUARD,
            ItemId.JUSTICIAR_LEGGUARDS,
            ItemId.SARADOMIN_DHIDE_SHIELD,
            ItemId.STAFF_OF_LIGHT,
            ItemId.HOLY_WRAPS
    }),

    ARMADYL(3973, new int[]{
            CustomItemId.IMBUED_ARMADYL_CAPE,
            CustomItemId.ARMADYL_BOW,
            CustomItemId.ARMADYL_MAX_CAPE,
            ItemId.ARMADYL_PENDANT,
            ItemId.ARMADYL_CROSSBOW,
            ItemId.ARMADYL_GODSWORD,
            ItemId.ARMADYL_HELMET,
            ItemId.ARMADYL_CHESTPLATE,
            ItemId.ARMADYL_CHAINSKIRT,
            ItemId.ARMADYL_ROBE_TOP,
            ItemId.ARMADYL_ROBE_LEGS,
            ItemId.ARMADYL_STOLE,
            ItemId.ARMADYL_MITRE,
            ItemId.ARMADYL_CLOAK,
            ItemId.ARMADYL_CROZIER,
            ItemId.ARMADYL_PLATEBODY,
            ItemId.ARMADYL_PLATELEGS,
            ItemId.ARMADYL_PLATESKIRT,
            ItemId.ARMADYL_FULL_HELM,
            ItemId.ARMADYL_KITESHIELD,
            ItemId.ARMADYL_BRACERS,
            ItemId.ARMADYL_DHIDE,
            ItemId.ARMADYL_CHAPS,
            ItemId.ARMADYL_COIF,
            ItemId.ARMADYL_DHIDE_BOOTS,
            ItemId.ARMADYL_GODSWORD_OR,
            ItemId.ARMADYL_GODSWORD_20593,
            ItemId.HONOURABLE_BLESSING,
            ItemId.DAMAGED_BOOK_12609,
            ItemId.BOOK_OF_LAW,
            ItemId.CRAWS_BOW,
            ItemId.CRAWS_BOW_U,
            ItemId.ARMADYL_DHIDE_SHIELD,
            27652,
            27655
    }),

    ANCIENT(13080, new int[]{
            CustomItemId.ANCIENT_MAX_CAPE,
            CustomItemId.IMBUED_ANCIENT_CAPE,
            ItemId.ANCIENT_GODSWORD,
            ItemId.ANCIENT_STAFF,
            ItemId.ANCIENT_CROZIER,
            ItemId.ZARYTE_CROSSBOW,
            ItemId.BOOK_OF_DARKNESS,
            26490,
            ItemId.ANCIENT_KITESHIELD,
            ItemId.ANCIENT_DHIDE_SHIELD,
            ItemId.ANCIENT_COIF,
            ItemId.ANCIENT_FULL_HELM,
            ItemId.ANCIENT_MITRE,
            ItemId.ANCIENT_HALO,
            ItemId.ANCIENT_CEREMONIAL_MASK,
            ItemId.TORVA_FULLHELM,
            ItemId.HOOD_OF_DARKNESS,
            ItemId.ANCIENT_CLOAK,
            ItemId.ANCIENT_PLATEBODY,
            ItemId.ANCIENT_DHIDE,
            ItemId.ANCIENT_ROBE_TOP,
            ItemId.TORVA_PLATEBODY,
            ItemId.ANCIENT_CEREMONIAL_TOP,
            ItemId.ROBE_TOP_OF_DARKNESS,
            ItemId.ANCIENT_PLATELEGS,
            ItemId.ANCIENT_CHAPS,
            ItemId.ANCIENT_PLATESKIRT,
            ItemId.ANCIENT_ROBE_LEGS,
            ItemId.TORVA_PLATELEGS,
            ItemId.ANCIENT_CEREMONIAL_LEGS,
            ItemId.ROBE_BOTTOM_OF_DARKNESS,
            ItemId.ANCIENT_BRACERS,
            ItemId.ZARYTE_VAMBRACES,
            ItemId.ANCIENT_CEREMONIAL_GLOVES,
            ItemId.GLOVES_OF_DARKNESS,
            ItemId.ANCIENT_DHIDE_BOOTS,
            ItemId.ANCIENT_CEREMONIAL_BOOTS,
            ItemId.BOOTS_OF_DARKNESS,
            ItemId.ANCIENT_BLESSING,
            ItemId.ANCIENT_STOLE,
            ItemId.ANCIENT_SCEPTRE,
            ItemId.BLOOD_ANCIENT_SCEPTRE_28260,
            ItemId.ICE_ANCIENT_SCEPTRE_28262,
            ItemId.SMOKE_ANCIENT_SCEPTRE_28264,
            ItemId.SHADOW_ANCIENT_SCEPTRE_28266
    });

    private final int varbit;
    private final IntSet protectiveItems;
    private final String key;

    GodType(int varbit, int[] protectiveItems) {
        this.varbit = varbit;
        this.protectiveItems = new IntOpenHashSet(protectiveItems);
        this.key = StringFormatUtil.formatString(name()) + "Kills";
    }

    public IntSet getProtectiveItems() {
        return protectiveItems;
    }

    public int killcountVarbit() {
        return this.varbit;
    }

    public String getKey() {
        return key;
    }

    public void addKillcount(Player player, int amount) {
        final int killCount = player.getNumericAttribute(key).intValue() + amount;
        player.getAttributes().put(key, killCount);
        player.getVarManager().sendBit(varbit, killCount);
    }

    public boolean isWieldingProtectiveItem(final Player player) {
        final Container container = player.getEquipment().getContainer();
        Item item;
        for (int slot = container.getContainerSize() - 1; slot >= 0; slot--) {
            item = container.get(slot);
            if (item == null) {
                continue;
            }
            if (protectiveItems.contains(item.getId())) return true;
        }
        return false;
    }

}
