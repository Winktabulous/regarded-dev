package com.zenyte.game.content.trouver;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum TrouverData {


    RUNE_POUCH(12791, 24416),
    FIRE_CAPE(6570, 24223),
    INFERNAL_CAPE(21295, 24224),
    AVAS_ASSEMBLER(22109, 24222),
    IMBUED_GUTHIX_CAPE(21793, 24249),
    IMBUED_SARADOMIN_CAPE(21793, 24248),
    IMBUED_ZAMORAK_CAPE(21795, 24250),
    FIRE_MAX_CAPE(13329, 24134),
    INFERNAL_MAX_CAPE(21285, 24133),
    ASSEMBLER_MAX_CAPE(21898, 24135),
    IMBUED_GUTHIX_MAX_CAPE(21784, 24234),
    IMBUED_SARADOMIN_MAX_CAPE(21776, 24238),
    IMBUED_ZAMORAK_MAX_CAPE(21780, 24233),
    BRONZE_DEFENDER(8844, 24136),
    IRON_DEFENDER(8845, 24137),
    STEEL_DEFENDER(8846, 24138),
    BLACK_DEFENDER(8847, 24139),
    MITHRIL_DEFENDER(8848, 24140),
    ADAMANT_DEFENDER(8849, 24141),
    RUNE_DEFENDER(8850, 24142),
    RUNE_DEFENDER_T(23230, 27009),
    DRAGON_DEFENDER(12954, 24143),
    DRAGON_DEFENDER_T(19722, 27008),
    AVERNIC_DEFENDER(ItemId.AVERNIC_DEFENDER, ItemId.AVERNIC_DEFENDER_L),
    VOID_MELEE_HELM(11665, 24185),
    VOID_MELEE_HELM_OR(26477, 27007),
    VOID_MAGE_HELM(11663, 24183),
    VOID_MAGE_HELM_OR(26473, 27005),
    VOID_RANGER_HELM(11664, 24184),
    VOID_RANGER_HELM_OR(26475, 27006),
    VOID_KNIGHT_TOP(8839, 24177),
    VOID_KNIGHT_TOP_OR(26463, 27000),
    ELITE_VOID_TOP(13072, 24178),
    ELITE_VOID_TOP_OR(26469, 27003),
    VOID_KNIGHT_ROBE(8840, 24179),
    VOID_KNIGHT_ROBE_OR(26465, 27001),
    ELITE_VOID_ROBE(13073, 24180),
    ELITE_VOID_ROBE_OR(26471, 27004),
    VOID_KNIGHT_GLOVES(8842, 24182),
    VOID_KNIGHT_GLOVES_OR(26467, 27002),
    VOID_KNIGHT_MACE(8841, 24181),
    DECORATIVE_ARMOUR_11896(11896, 24163),
    DECORATIVE_ARMOUR_11897(11897, 24164),
    DECORATIVE_ARMOUR_11898(11898, 24165),
    DECORATIVE_ARMOUR_11899(11899, 24166),
    DECORATIVE_ARMOUR_11900(11900, 24167),
    DECORATIVE_ARMOUR_11901(11901, 24168),
    GUTHIX_HALO(12639, 24171),
    SARADOMIN_HALO(12637, 24169),
    ZAMORAK_HALO(12638, 24170),
    ARMADYL_HALO(24192, 24194),
    BANDOS_HALO(24195, 24197),
    SEREN_HALO(24198, 24200),
    ANCIENT_HALO(24201, 24203),
    BRASSICA_HALO(24204, 24206),
    FIGHTER_HAT(10548, 24173),
    RANGER_HAT(10550, 24174),
    HEALER_HAT(10547, 24172),
    RUNNER_HAT(10549, 24533),
    FIGHTER_TORSO(10551, 24175),
    PENANCE_SKIRT(10555, 24176);


    public static final List<TrouverData> VALUES = List.of(values());
    public static final List<Integer> ORIGINAL_ITEMS = Arrays.stream(values()).map(TrouverData::getOriginalItemId).collect(Collectors.toList());

    public static int[] PROTECTED_ITEMS = Arrays.stream(values()).mapToInt(e -> e.getOriginalItemId()).toArray();

    private int originalItemId;
    private int protectedItemId;

    TrouverData(int originalItemId, int protectedItemId) {
        this.originalItemId = originalItemId;
        this.protectedItemId = protectedItemId;
    }

    public int getOriginalItemId() {
        return originalItemId;
    }

    public void setOriginalItemId(int originalItemId) {
        this.originalItemId = originalItemId;
    }

    public int getProtectedItemId() {
        return protectedItemId;
    }

    public void setProtectedItemId(int protectedItemId) {
        this.protectedItemId = protectedItemId;
    }


    public static void protectItem(Player player, Item item, int slot) {
        if (!ORIGINAL_ITEMS.contains(item.getId())) {
            needsSomething(player, "This item can't be protected");
            return;
        }

        if (!player.getInventory().containsItem(TrouverConstants.TROUVER_PARCHMENT_ITEM_ID)) {
            needsSomething(player, "You need a trouver parchment for me to protect your item.");
            return;
        }

        if (!player.getInventory().containsItem(995, 500_000)) {
            needsSomething(player, "You need 500,000 coins for me to protect your item.");
            return;
        }

        Optional<TrouverData> find = getByOriginalId(item.getId());

        if(find.isEmpty()) {
            needsSomething(player, "This item can't be protected");
            return;
        }

        player.getInventory().deleteItem(995, 500_000);
        player.getInventory().deleteItem(TrouverConstants.TROUVER_PARCHMENT_ITEM_ID, 1);

        player.getInventory().replaceItem(find.get().getProtectedItemId(), item.getAmount(), slot);
    }

    public static Optional<TrouverData> getByOriginalId(int originalItemId) {
        return VALUES.stream().filter(e -> e.getOriginalItemId() == originalItemId).findFirst();
    }

    public static Optional<TrouverData> getByProtectedId(int protectedId) {
        return VALUES.stream().filter(e -> e.getProtectedItemId() == protectedId).findFirst();
    }

    private static void needsSomething(Player player, String missing) {
        player.getDialogueManager().start(new Dialogue(player, 7456) {
            @Override
            public void buildDialogue() {
                npc(missing);
            }
        });
    }

}


