package com.zenyte.game.model.item.degradableitems;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 18 jan. 2018 : 17:53:54
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public enum RepairableItem {
    AHRIMS_HOOD(60000, 4708, 4856, 4857, 4858, 4859, 4860),
    AHRIMS_STAFF(100000, 4710, 4862, 4863, 4864, 4865, 4866),
    AHRIMS_ROBETOP(90000, 4712, 4868, 4869, 4870, 4871, 4872),
    AHRIMS_ROBESKIRT(80000, 4714, 4874, 4875, 4876, 4877, 4878),
    DHAROKS_HELM(60000, 4716, 4880, 4881, 4882, 4883, 4884),
    DHAROKS_GREATAXE(100000, 4718, 4886, 4887, 4888, 4889, 4890),
    DHAROKS_BODY(90000, 4720, 4892, 4893, 4894, 4895, 4896),
    DHAROKS_LEGS(80000, 4722, 4898, 4899, 4900, 4901, 4902),
    GUTHANS_HELM(60000, 4724, 4904, 4905, 4906, 4907, 4908),
    GUTHANS_WARSPEAR(100000, 4726, 4910, 4911, 4912, 4913, 4914),
    GUTHANS_PLATEBODY(90000, 4728, 4916, 4917, 4918, 4919, 4920),
    GUTHANS_CHAINSKIRT(80000, 4730, 4922, 4923, 4924, 4925, 4926),
    KARILS_COIF(60000, 4732, 4928, 4929, 4930, 4931, 4932),
    KARILS_CROSSBOW(100000, 4734, 4934, 4935, 4936, 4937, 4938),
    KARILS_LEATHERTOP(90000, 4736, 4940, 4941, 4942, 4943, 4944),
    KARILS_LEATHERSKIRT(80000, 4738, 4946, 4947, 4948, 4949, 4950),
    TORAGS_HELM(60000, 4745, 4952, 4953, 4954, 4955, 4956),
    TORAGS_HAMMERS(100000, 4747, 4958, 4959, 4960, 4961, 4962),
    TORAGS_PLATEBODY(90000, 4749, 4964, 4965, 4966, 4967, 4968),
    TORAGS_PLATELEGS(80000, 4751, 4970, 4971, 4972, 4973, 4974),
    VERACS_HELM(60000, 4753, 4976, 4977, 4978, 4979, 4980),
    VERACS_FLAIL(100000, 4755, 4982, 4983, 4984, 4985, 4986),
    VERACS_BRASSARD(90000, 4757, 4988, 4989, 4990, 4991, 4992),
    VERACS_PLATESKIRT(80000, 4759, 4994, 4995, 4996, 4997, 4998),
    /**
     * Untradeables Only enqueue untradeable items under the FIRE_CAPE entry of the
     * enum!
     */
    FIRE_CAPE(100000, 6570, 20445),
    INFERNAL_CAPE(300000, 21295, 21287),
    FIRE_MAX_CAPE(100000, 13329, 20447),
    INFERNAL_MAX_CAPE(300000, 21285, 21289),
    AVAS_ASSEMBLER(100000, 22109, 21914),
    ASSEMBLER_MAX_CAPE(100000, 21898, 21916),
    BRONZE_DEFENDER(1000, 8844, 20449),
    IRON_DEFENDER(2000, 8845, 20451),
    STEEL_DEFENDER(2500, 8846, 20453),
    BLACK_DEFENDER(5000, 8847, 20455),
    MITHRIL_DEFENDER(15000, 8848, 20457),
    ADAMANT_DEFENDER(25000, 8849, 20459),
    RUNE_DEFENDER(35000, 8850, 20461),
    DRAGON_DEFENDER(300000, 12954, 20463),
    VOID_MAGE_HELM(250000, 11663, 20477),
    VOID_RANGER_HELM(250000, 11664, 20479),
    VOID_MELEE_HELM(250000, 11665, 20481),
    VOID_KNIGHT_TOP(400000, 8839, 20465),
    VOID_KNIGHT_ROBE(400000, 8840, 20469),
    VOID_KNIGHT_GLOVES(250000, 8842, 20475),
    ELITE_VOID_TOP(400000, 13072, 20467),
    ELITE_VOID_ROBE(400000, 13073, 20471),
    DECORATIVE_ARMOUR_11896(5000, 11896, 20495),
    DECORATIVE_ARMOUR_11897(5000, 11897, 20497),
    DECORATIVE_ARMOUR_11898(5000, 11898, 20499),
    DECORATIVE_ARMOUR_11899(5000, 11899, 20501),
    DECORATIVE_ARMOUR_11900(5000, 11900, 20503),
    DECORATIVE_ARMOUR_11901(5000, 11901, 20505),
    SARADOMIN_HALO(25000, 12637, 20537),
    ZAMORAK_HALO(25000, 12638, 20539),
    GUTHIX_HALO(25000, 12639, 20541),
    ARMADYL_HALO(25000, 24192, 24147),
    BANDOS_HALO(25000, 24195, 24149),
    SEREN_HALO(25000, 24198, 24151),
    ANCIENT_HALO(25000, 24201, 24153),
    BRASSICA_HALO(25000, 24204, 24155),
    FIGHTER_HAT(200000, 10548, 20507),
    RANGER_HAT(200000, 10550, 20509),
    HEALER_HAT(200000, 10547, 20511),
    FIGHTER_TORSO(200000, 10551, 20513),
    PENANCE_SKIRT(100000, 10555, 20515),
    AVERNIC_DEFENDER(1_000_000, ItemId.AVERNIC_DEFENDER, ItemId.AVERNIC_DEFENDER_BROKEN),
    MASORI_ASSEMBLER(200_000, ItemId.MASORI_ASSEMBLER, ItemId.MASORI_ASSEMBLER_BROKEN),
    MASORI_MAX_CAPE(200_000, ItemId.MASORI_ASSEMBLER_MAX_CAPE, ItemId.MASORI_ASSEMBLER_MAX_CAPE_BROKEN),
    ;
    private final int[] ids;
    private final int repairCost;
    public static final RepairableItem[] VALUES = values();

    RepairableItem(final int repairCost, final int... ids) {
        this.ids = ids;
        this.repairCost = repairCost;
    }

    public boolean isTradeable() {
        return this.ordinal() < FIRE_CAPE.ordinal();
    }

    public int getRepairCost() {
        return (int) (repairCost * 0.3);
    }

    public static RepairableItem getItem(Item item) {
        for (final RepairableItem repairable : VALUES) {
            for (int index = 0; index < repairable.getIds().length; index++) {
                if (item.getId() == repairable.getIds()[index]) {
                    return repairable;
                }
            }
        }
        return null;
    }

    public float getCost(Player player, Item item, boolean armourstand) {
        if (ordinal() <= VERACS_PLATESKIRT.ordinal()) {
            if (armourstand) {
                return getRepairCost() - ((1.0F - (player.getSkills().getLevel(SkillConstants.SMITHING) / 200.0F)) * getRepairCost()) * (((item.getCharges() / 90000.0F)));
            } else {
                return getRepairCost() - (getRepairCost() * (((item.getCharges() / 90000.0F))));
            }
        } else {
            return getRepairCost();
        }
    }

    public int[] getIds() {
        return ids;
    }
}
