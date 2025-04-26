package com.zenyte.game.model.item.degradableitems;

import com.near_reality.game.content.crystal.recipes.chargeable.CrystalArmour;
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalTool;
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalWeapon;
import com.near_reality.game.content.custom.PolyporeStaff;
import com.near_reality.game.item.CustomItemId;
import com.near_reality.game.model.item.degrading.Degradeable;
import com.zenyte.game.content.tombsofamascut.TOAConstants;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.plugins.item.BloodEssence;
import com.zenyte.plugins.item.TomeOfFire;
import com.zenyte.plugins.itemonitem.SerpentineHelmetChargingAction;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Kris | 28. dets 2017 : 1:50.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum DegradableItem implements Degradeable {

//    AHRIMS_HOOD(DegradeType.TICKS, 4708, 4856, 0, 0),
//    AHRIMS_HOOD_100(DegradeType.TICKS, 4856, 4857, 90000, 67500),
//    AHRIMS_HOOD_75(DegradeType.TICKS, 4857, 4858, 67500, 45000),
//    AHRIMS_HOOD_50(DegradeType.TICKS, 4858, 4859, 45000, 22500),
//    AHRIMS_HOOD_25(DegradeType.TICKS, 4859, 4860, 22500, 0),
//    AHRIMS_STAFF(DegradeType.TICKS, 4710, 4862, 0, 0),
//    AHRIMS_STAFF_100(DegradeType.TICKS, 4862, 4863, 90000, 67500),
//    AHRIMS_STAFF_75(DegradeType.TICKS, 4863, 4864, 67500, 45000),
//    AHRIMS_STAFF_50(DegradeType.TICKS, 4864, 4865, 45000, 22500),
//    AHRIMS_STAFF_25(DegradeType.TICKS, 4865, 4866, 22500, 0),
//    AHRIMS_ROBETOP(DegradeType.TICKS, 4712, 4868, 0, 0),
//    AHRIMS_ROBETOP_100(DegradeType.TICKS, 4868, 4869, 90000, 67500),
//    AHRIMS_ROBETOP_75(DegradeType.TICKS, 4869, 4870, 67500, 45000),
//    AHRIMS_ROBETOP_50(DegradeType.TICKS, 4870, 4871, 45000, 22500),
//    AHRIMS_ROBETOP_25(DegradeType.TICKS, 4871, 4872, 22500, 0),
//    AHRIMS_ROBESKIRT(DegradeType.TICKS, 4714, 4874, 0, 0),
//    AHRIMS_ROBESKIRT_100(DegradeType.TICKS, 4874, 4875, 90000, 67500),
//    AHRIMS_ROBESKIRT_75(DegradeType.TICKS, 4875, 4876, 67500, 45000),
//    AHRIMS_ROBESKIRT_50(DegradeType.TICKS, 4876, 4877, 45000, 22500),
//    AHRIMS_ROBESKIRT_25(DegradeType.TICKS, 4877, 4878, 22500, 0),
//    DHAROKS_HELM(DegradeType.TICKS, 4716, 4880, 0, 0),
//    DHAROKS_HELM_100(DegradeType.TICKS, 4880, 4881, 90000, 67500),
//    DHAROKS_HELM_75(DegradeType.TICKS, 4881, 4882, 67500, 45000),
//    DHAROKS_HELM_50(DegradeType.TICKS, 4882, 4883, 45000, 22500),
//    DHAROKS_HELM_25(DegradeType.TICKS, 4883, 4884, 22500, 0),
//    DHAROKS_GREATAXE(DegradeType.TICKS, 4718, 4886, 0, 0),
//    DHAROKS_GREATAXE_100(DegradeType.TICKS, 4886, 4887, 90000, 67500),
//    DHAROKS_GREATAXE_75(DegradeType.TICKS, 4887, 4888, 67500, 45000),
//    DHAROKS_GREATAXE_50(DegradeType.TICKS, 4888, 4889, 45000, 22500),
//    DHAROKS_GREATAXE_25(DegradeType.TICKS, 4889, 4890, 22500, 0),
//    DHAROKS_PLATEBODY(DegradeType.TICKS, 4720, 4892, 0, 0),
//    DHAROKS_PLATEBODY_100(DegradeType.TICKS, 4892, 4893, 90000, 67500),
//    DHAROKS_PLATEBODY_75(DegradeType.TICKS, 4893, 4894, 67500, 45000),
//    DHAROKS_PLATEBODY_50(DegradeType.TICKS, 4894, 4895, 45000, 22500),
//    DHAROKS_PLATEBODY_25(DegradeType.TICKS, 4895, 4896, 22500, 0),
//    DHAROKS_PLATELEGS(DegradeType.TICKS, 4722, 4898, 0, 0),
//    DHAROKS_PLATELEGS_100(DegradeType.TICKS, 4898, 4899, 90000, 67500),
//    DHAROKS_PLATELEGS_75(DegradeType.TICKS, 4899, 4900, 67500, 45000),
//    DHAROKS_PLATELEGS_50(DegradeType.TICKS, 4900, 4901, 45000, 22500),
//    DHAROKS_PLATELEGS_25(DegradeType.TICKS, 4901, 4902, 22500, 0),
//    GUTHANS_HELM(DegradeType.TICKS, 4724, 4904, 0, 0),
//    GUTHANS_HELM_100(DegradeType.TICKS, 4904, 4905, 90000, 67500),
//    GUTHANS_HELM_75(DegradeType.TICKS, 4905, 4906, 67500, 45000),
//    GUTHANS_HELM_50(DegradeType.TICKS, 4906, 4907, 45000, 22500),
//    GUTHANS_HELM_25(DegradeType.TICKS, 4907, 4908, 22500, 0),
//    GUTHANS_WARSPEAR(DegradeType.TICKS, 4726, 4910, 0, 0),
//    GUTHANS_WARSPEAR_100(DegradeType.TICKS, 4910, 4911, 90000, 67500),
//    GUTHANS_WARSPEAR_75(DegradeType.TICKS, 4911, 4912, 67500, 45000),
//    GUTHANS_WARSPEAR_50(DegradeType.TICKS, 4912, 4913, 45000, 22500),
//    GUTHANS_WARSPEAR_25(DegradeType.TICKS, 4913, 4914, 22500, 0),
//    GUTHANS_PLATEBODY(DegradeType.TICKS, 4728, 4916, 0, 0),
//    GUTHANS_PLATEBODY_100(DegradeType.TICKS, 4916, 4917, 90000, 67500),
//    GUTHANS_PLATEBODY_75(DegradeType.TICKS, 4917, 4918, 67500, 45000),
//    GUTHANS_PLATEBODY_50(DegradeType.TICKS, 4918, 4919, 45000, 22500),
//    GUTHANS_PLATEBODY_25(DegradeType.TICKS, 4919, 4920, 22500, 0),
//    GUTHANS_CHAINSKIRT(DegradeType.TICKS, 4730, 4922, 0, 0),
//    GUTHANS_CHAINSKIRT_100(DegradeType.TICKS, 4922, 4923, 90000, 67500),
//    GUTHANS_CHAINSKIRT_75(DegradeType.TICKS, 4923, 4924, 67500, 45000),
//    GUTHANS_CHAINSKIRT_50(DegradeType.TICKS, 4924, 4925, 45000, 22500),
//    GUTHANS_CHAINSKIRT_25(DegradeType.TICKS, 4925, 4926, 22500, 0),
//    KARILS_COIF(DegradeType.TICKS, 4732, 4928, 0, 0),
//    KARILS_COIF_100(DegradeType.TICKS, 4928, 4929, 90000, 67500),
//    KARILS_COIF_75(DegradeType.TICKS, 4929, 4930, 67500, 45000),
//    KARILS_COIF_50(DegradeType.TICKS, 4930, 4931, 45000, 22500),
//    KARILS_COIF_25(DegradeType.TICKS, 4931, 4932, 22500, 0),
//    KARILS_CROSSBOW(DegradeType.TICKS, 4734, 4934, 0, 0),
//    KARILS_CROSSBOW_100(DegradeType.TICKS, 4934, 4935, 90000, 67500),
//    KARILS_CROSSBOW_75(DegradeType.TICKS, 4935, 4936, 67500, 45000),
//    KARILS_CROSSBOW_50(DegradeType.TICKS, 4936, 4937, 45000, 22500),
//    KARILS_CROSSBOW_25(DegradeType.TICKS, 4937, 4938, 22500, 0),
//    KARILS_LEATHERTOP(DegradeType.TICKS, 4736, 4940, 0, 0),
//    KARILS_LEATHERTOP_100(DegradeType.TICKS, 4940, 4941, 90000, 67500),
//    KARILS_LEATHERTOP_75(DegradeType.TICKS, 4941, 4942, 67500, 45000),
//    KARILS_LEATHERTOP_50(DegradeType.TICKS, 4942, 4943, 45000, 22500),
//    KARILS_LEATHERTOP_25(DegradeType.TICKS, 4943, 4944, 22500, 0),
//    KARILS_LEATHERSKIRT(DegradeType.TICKS, 4738, 4946, 0, 0),
//    KARILS_LEATHERSKIRT_100(DegradeType.TICKS, 4946, 4947, 90000, 67500),
//    KARILS_LEATHERSKIRT_75(DegradeType.TICKS, 4947, 4948, 67500, 45000),
//    KARILS_LEATHERSKIRT_50(DegradeType.TICKS, 4948, 4949, 45000, 22500),
//    KARILS_LEATHERSKIRT_25(DegradeType.TICKS, 4949, 4950, 22500, 0),
//    TORAGS_HELM(DegradeType.TICKS, 4745, 4952, 0, 0),
//    TORAGS_HELM_100(DegradeType.TICKS, 4952, 4953, 90000, 67500),
//    TORAGS_HELM_75(DegradeType.TICKS, 4953, 4954, 67500, 45000),
//    TORAGS_HELM_50(DegradeType.TICKS, 4954, 4955, 45000, 22500),
//    TORAGS_HELM_25(DegradeType.TICKS, 4955, 4956, 22500, 0),
//    TORAGS_HAMMERS(DegradeType.TICKS, 4747, 4958, 0, 0),
//    TORAGS_HAMMERS_100(DegradeType.TICKS, 4958, 4959, 90000, 67500),
//    TORAGS_HAMMERS_75(DegradeType.TICKS, 4959, 4960, 67500, 45000),
//    TORAGS_HAMMERS_50(DegradeType.TICKS, 4960, 4961, 45000, 22500),
//    TORAGS_HAMMERS_25(DegradeType.TICKS, 4961, 4962, 22500, 0),
//    TORAGS_PLATEBODY(DegradeType.TICKS, 4749, 4964, 0, 0),
//    TORAGS_PLATEBODY_100(DegradeType.TICKS, 4964, 4965, 90000, 67500),
//    TORAGS_PLATEBODY_75(DegradeType.TICKS, 4965, 4966, 67500, 45000),
//    TORAGS_PLATEBODY_50(DegradeType.TICKS, 4966, 4967, 45000, 22500),
//    TORAGS_PLATEBODY_25(DegradeType.TICKS, 4967, 4968, 22500, 0),
//    TORAGS_PLATELEGS(DegradeType.TICKS, 4751, 4970, 0, 0),
//    TORAGS_PLATELEGS_100(DegradeType.TICKS, 4970, 4971, 90000, 67500),
//    TORAGS_PLATELEGS_75(DegradeType.TICKS, 4971, 4972, 67500, 45000),
//    TORAGS_PLATELEGS_50(DegradeType.TICKS, 4972, 4973, 45000, 22500),
//    TORAGS_PLATELEGS_25(DegradeType.TICKS, 4973, 4974, 22500, 0),
//    VERACS_HELM(DegradeType.TICKS, 4753, 4976, 0, 0),
//    VERACS_HELM_100(DegradeType.TICKS, 4976, 4977, 90000, 67500),
//    VERACS_HELM_75(DegradeType.TICKS, 4977, 4978, 67500, 45000),
//    VERACS_HELM_50(DegradeType.TICKS, 4978, 4979, 45000, 22500),
//    VERACS_HELM_25(DegradeType.TICKS, 4979, 4980, 22500, 0),
//    VERACS_FLAIL(DegradeType.TICKS, 4755, 4982, 0, 0),
//    VERACS_FLAIL_100(DegradeType.TICKS, 4982, 4983, 90000, 67500),
//    VERACS_FLAIL_75(DegradeType.TICKS, 4983, 4984, 67500, 45000),
//    VERACS_FLAIL_50(DegradeType.TICKS, 4984, 4985, 45000, 22500),
//    VERACS_FLAIL_25(DegradeType.TICKS, 4985, 4986, 22500, 0),
//    VERACS_BRASSARD(DegradeType.TICKS, 4757, 4988, 0, 0),
//    VERACS_BRASSARD_100(DegradeType.TICKS, 4988, 4989, 90000, 67500),
//    VERACS_BRASSARD_75(DegradeType.TICKS, 4989, 4990, 67500, 45000),
//    VERACS_BRASSARD_50(DegradeType.TICKS, 4990, 4991, 45000, 22500),
//    VERACS_BRASSARD_25(DegradeType.TICKS, 4991, 4992, 22500, 0),
//    VERACS_PLATESKIRT(DegradeType.TICKS, 4759, 4994, 0, 0),
//    VERACS_PLATESKIRT_100(DegradeType.TICKS, 4994, 4995, 90000, 67500),
//    VERACS_PLATESKIRT_75(DegradeType.TICKS, 4995, 4996, 67500, 45000),
//    VERACS_PLATESKIRT_50(DegradeType.TICKS, 4996, 4997, 45000, 22500),
//    VERACS_PLATESKIRT_25(DegradeType.TICKS, 4997, 4998, 22500, 0),
    AMULET_OF_THE_DAMNED(DegradeType.TICKS, 12853, -1, 90000, 0),
	ABYSSAL_TENTACLE(DegradeType.OUTGOING_HIT, ItemId.ABYSSAL_TENTACLE, ItemId.KRAKEN_TENTACLE, 10_000, 0),
    ABYSSAL_TENTACLE_OR(DegradeType.OUTGOING_HIT, 26484, ItemId.KRAKEN_TENTACLE, 10_000, 0),
    AMULET_OF_BLOOD_FURY(DegradeType.CUSTOM, ItemId.AMULET_OF_BLOOD_FURY, ItemId.AMULET_OF_FURY, 10_000, 0),
    ARCLIGHT(DegradeType.USE, 19675, 6746, 10000, 0),
    FULL_TRIDENT_OF_THE_SEAS(DegradeType.TRIDENT, ItemId.TRIDENT_OF_THE_SEAS_FULL, ItemId.TRIDENT_OF_THE_SEAS, 0, 0),
	TRIDENT_OF_THE_SEAS(DegradeType.TRIDENT, ItemId.TRIDENT_OF_THE_SEAS, ItemId.UNCHARGED_TRIDENT, 2500, 0),
	TRIDENT_OF_THE_SEAS_E(DegradeType.TRIDENT, ItemId.TRIDENT_OF_THE_SEAS_E, ItemId.UNCHARGED_TRIDENT_E, 20000, 0),
	TRIDENT_OF_THE_SWAMP(DegradeType.TRIDENT, ItemId.TRIDENT_OF_THE_SWAMP, ItemId.UNCHARGED_TOXIC_TRIDENT, 2500, 0, item -> new Item[]{new Item(12934, item.getCharges())}),
	TRIDENT_OF_THE_SWAMP_E(DegradeType.TRIDENT, ItemId.TRIDENT_OF_THE_SWAMP_E, ItemId.UNCHARGED_TOXIC_TRIDENT_E, 20000, 0, item -> new Item[]{new Item(12934, item.getCharges())}),
	IBANS_STAFF(DegradeType.IBANS_BLAST, 1409, 1410, 120, 0),
    UPGRADED_IBANS_STAFF(DegradeType.IBANS_BLAST, 12658, 1410, 2500, 0),
    FULL_SARADOMIN_BLESSED_SWORD(DegradeType.OUTGOING_HIT, 12808, 12809, 0, 0),
    SARADOMIN_BLESSED_SWORD(DegradeType.OUTGOING_HIT, 12809, 12804, 10000, 0),
    TOXIC_STAFF_OF_THE_DEAD(DegradeType.SPELL, 12904, 12902, SerpentineHelmetChargingAction.MAX_CHARGES, 0, item -> new Item[]{new Item(12934, (int) (item.getCharges() * SerpentineHelmetChargingAction.SCALES_TO_CHARGES_RATIO))}),
	SERPENTINE_HELM(DegradeType.TICKS, 12931, 12929, SerpentineHelmetChargingAction.MAX_CHARGES, 0, item -> new Item[]{new Item(12934, (int) (item.getCharges() * SerpentineHelmetChargingAction.SCALES_TO_CHARGES_RATIO))}),
	TANZANITE_HELM(DegradeType.TICKS, 13197, 13196, SerpentineHelmetChargingAction.MAX_CHARGES, 0, item -> new Item[]{new Item(12934, (int) (item.getCharges() * SerpentineHelmetChargingAction.SCALES_TO_CHARGES_RATIO))}),
	MAGMA_HELM(DegradeType.TICKS, 13199, 13198, SerpentineHelmetChargingAction.MAX_CHARGES, 0, item -> new Item[]{new Item(12934, (int) (item.getCharges() * SerpentineHelmetChargingAction.SCALES_TO_CHARGES_RATIO))}),
	BONECRUSHER(DegradeType.USE, 13116, 13116, 2147483647, 0),
    INFERNAL_AXE(DegradeType.USE, 13241, 13242, 5000, 0),
    INFERNAL_PICKAXE(DegradeType.USE, 13243, 13244, 5000, 0),
    INFERNAL_HARPOON(DegradeType.USE, 21031, 21033, 5000, 0),
    RECHARGED_RING_OF_SUFFERING(DegradeType.RECOIL, 20655, 19550, 100000, 0),
    IMBUED_RECHARGED_RING_OF_SUFFERING(DegradeType.RECOIL, 20657, 19710, 100000, 0),
    TOME_OF_FIRE(DegradeType.USE, TomeOfFire.TOME_OF_FIRE, TomeOfFire.TOME_OF_FIRE_EMPTY, 20000, 0, item -> new Item[]{new Item(20718, item.getCharges() / 20)}),
    DRAGONFIRE_SHIELD(DegradeType.USE, 11283, 11284, 50, 0),
    ANCIENT_WYVERN_SHIELD(DegradeType.USE, 21633, 21634, 50, 0),
    DRAGONFIRE_WARD(DegradeType.USE, 22002, 22003, 50, 0),
    DRAGON_KITE(DegradeType.USE, CustomItemId.DRAGON_KITE, CustomItemId.DRAGON_KITE, 50, 0),
    CRYSTAL_BOW_NEW(DegradeType.OUTGOING_HIT, 4212, 4214, 0, 0),
    CRYSTAL_BOW_10(DegradeType.OUTGOING_HIT, 4214, 4215, 2500, 2250),
    CRYSTAL_BOW_9(DegradeType.OUTGOING_HIT, 4215, 4216, 2250, 2000),
    CRYSTAL_BOW_8(DegradeType.OUTGOING_HIT, 4216, 4217, 2000, 1750),
    CRYSTAL_BOW_7(DegradeType.OUTGOING_HIT, 4217, 4218, 1750, 1500),
    CRYSTAL_BOW_6(DegradeType.OUTGOING_HIT, 4218, 4219, 1500, 1250),
    CRYSTAL_BOW_5(DegradeType.OUTGOING_HIT, 4219, 4220, 1250, 1000),
    CRYSTAL_BOW_4(DegradeType.OUTGOING_HIT, 4220, 4221, 1000, 750),
    CRYSTAL_BOW_3(DegradeType.OUTGOING_HIT, 4221, 4222, 750, 500),
    CRYSTAL_BOW_2(DegradeType.OUTGOING_HIT, 4222, 4223, 500, 250),
    CRYSTAL_BOW_1(DegradeType.OUTGOING_HIT, 4223, 4207, 250, 0),
    IMBUED_CRYSTAL_BOW_NEW(DegradeType.OUTGOING_HIT, 11748, 11749, 0, 0),
    IMBUED_CRYSTAL_BOW_10(DegradeType.OUTGOING_HIT, 11749, 11750, 2500, 2250),
    IMBUED_CRYSTAL_BOW_9(DegradeType.OUTGOING_HIT, 11750, 11751, 2250, 2000),
    IMBUED_CRYSTAL_BOW_8(DegradeType.OUTGOING_HIT, 11751, 11752, 2000, 1750),
    IMBUED_CRYSTAL_BOW_7(DegradeType.OUTGOING_HIT, 11752, 11753, 1750, 1500),
    IMBUED_CRYSTAL_BOW_6(DegradeType.OUTGOING_HIT, 11753, 11754, 1500, 1250),
    IMBUED_CRYSTAL_BOW_5(DegradeType.OUTGOING_HIT, 11754, 11755, 1250, 1000),
    IMBUED_CRYSTAL_BOW_4(DegradeType.OUTGOING_HIT, 11755, 11756, 1000, 750),
    IMBUED_CRYSTAL_BOW_3(DegradeType.OUTGOING_HIT, 11756, 11757, 750, 500),
    IMBUED_CRYSTAL_BOW_2(DegradeType.OUTGOING_HIT, 11757, 11758, 500, 250),
    IMBUED_CRYSTAL_BOW_1(DegradeType.OUTGOING_HIT, 11758, 4207, 250, 0),
    CRYSTAL_SHIELD_NEW(DegradeType.INCOMING_HIT, 4224, 4225, 0, 0),
    CRYSTAL_SHIELD_10(DegradeType.INCOMING_HIT, 4225, 4226, 2500, 2250),
    CRYSTAL_SHIELD_9(DegradeType.INCOMING_HIT, 4226, 4227, 2250, 2000),
    CRYSTAL_SHIELD_8(DegradeType.INCOMING_HIT, 4227, 4228, 2000, 1750),
    CRYSTAL_SHIELD_7(DegradeType.INCOMING_HIT, 4228, 4229, 1750, 1500),
    CRYSTAL_SHIELD_6(DegradeType.INCOMING_HIT, 4229, 4230, 1500, 1250),
    CRYSTAL_SHIELD_5(DegradeType.INCOMING_HIT, 4230, 4231, 1250, 1000),
    CRYSTAL_SHIELD_4(DegradeType.INCOMING_HIT, 4231, 4232, 1000, 750),
    CRYSTAL_SHIELD_3(DegradeType.INCOMING_HIT, 4232, 4233, 750, 500),
    CRYSTAL_SHIELD_2(DegradeType.INCOMING_HIT, 4233, 4234, 500, 250),
    CRYSTAL_SHIELD_1(DegradeType.INCOMING_HIT, 4234, 4207, 250, 0),
    IMBUED_CRYSTAL_SHIELD_NEW(DegradeType.INCOMING_HIT, 11759, 11760, 0, 0),
    IMBUED_CRYSTAL_SHIELD_10(DegradeType.INCOMING_HIT, 11760, 11761, 2500, 2250),
    IMBUED_CRYSTAL_SHIELD_9(DegradeType.INCOMING_HIT, 11761, 11762, 2250, 2000),
    IMBUED_CRYSTAL_SHIELD_8(DegradeType.INCOMING_HIT, 11762, 11763, 2000, 1750),
    IMBUED_CRYSTAL_SHIELD_7(DegradeType.INCOMING_HIT, 11763, 11764, 1750, 1500),
    IMBUED_CRYSTAL_SHIELD_6(DegradeType.INCOMING_HIT, 11764, 11765, 1500, 1250),
    IMBUED_CRYSTAL_SHIELD_5(DegradeType.INCOMING_HIT, 11765, 11766, 1250, 1000),
    IMBUED_CRYSTAL_SHIELD_4(DegradeType.INCOMING_HIT, 11766, 11767, 1000, 750),
    IMBUED_CRYSTAL_SHIELD_3(DegradeType.INCOMING_HIT, 11767, 11768, 750, 500),
    IMBUED_CRYSTAL_SHIELD_2(DegradeType.INCOMING_HIT, 11768, 11769, 500, 250),
    IMBUED_CRYSTAL_SHIELD_1(DegradeType.INCOMING_HIT, 11769, 4207, 250, 0),
    CRYSTAL_HALBERD_NEW(DegradeType.OUTGOING_HIT, 13091, 13092, 0, 0),
    CRYSTAL_HALBERD_10(DegradeType.OUTGOING_HIT, 13092, 13093, 2500, 2250),
    CRYSTAL_HALBERD_9(DegradeType.OUTGOING_HIT, 13093, 13094, 2250, 2000),
    CRYSTAL_HALBERD_8(DegradeType.OUTGOING_HIT, 13094, 13095, 2000, 1750),
    CRYSTAL_HALBERD_7(DegradeType.OUTGOING_HIT, 13095, 13096, 1750, 1500),
    CRYSTAL_HALBERD_6(DegradeType.OUTGOING_HIT, 13096, 13097, 1500, 1250),
    CRYSTAL_HALBERD_5(DegradeType.OUTGOING_HIT, 13097, 13098, 1250, 1000),
    CRYSTAL_HALBERD_4(DegradeType.OUTGOING_HIT, 13098, 13099, 1000, 750),
    CRYSTAL_HALBERD_3(DegradeType.OUTGOING_HIT, 13099, 13100, 750, 500),
    CRYSTAL_HALBERD_2(DegradeType.OUTGOING_HIT, 13100, 13101, 500, 250),
    CRYSTAL_HALBERD_1(DegradeType.OUTGOING_HIT, 13101, 4207, 250, 0),
    IMBUED_CRYSTAL_HALBERD_NEW(DegradeType.OUTGOING_HIT, 13080, 13081, 0, 0),
    IMBUED_CRYSTAL_HALBERD_10(DegradeType.OUTGOING_HIT, 13081, 13082, 2500, 2250),
    IMBUED_CRYSTAL_HALBERD_9(DegradeType.OUTGOING_HIT, 13082, 13083, 2250, 2000),
    IMBUED_CRYSTAL_HALBERD_8(DegradeType.OUTGOING_HIT, 13083, 13084, 2000, 1750),
    IMBUED_CRYSTAL_HALBERD_7(DegradeType.OUTGOING_HIT, 13084, 13085, 1750, 1500),
    IMBUED_CRYSTAL_HALBERD_6(DegradeType.OUTGOING_HIT, 13085, 13086, 1500, 1250),
    IMBUED_CRYSTAL_HALBERD_5(DegradeType.OUTGOING_HIT, 13086, 13087, 1250, 1000),
    IMBUED_CRYSTAL_HALBERD_4(DegradeType.OUTGOING_HIT, 13087, 13088, 1000, 750),
    IMBUED_CRYSTAL_HALBERD_3(DegradeType.OUTGOING_HIT, 13088, 13089, 750, 500),
    IMBUED_CRYSTAL_HALBERD_2(DegradeType.OUTGOING_HIT, 13089, 13090, 500, 250),
    IMBUED_CRYSTAL_HALBERD_1(DegradeType.OUTGOING_HIT, 13090, 4207, 250, 0),
    SLAYERS_STAFF_ENCHANTED(DegradeType.OUTGOING_HIT, 21255, 4170, 2500, 0),
    BRYOPHYTAS_STAFF(DegradeType.USE, 22370, 22368, 1000, 0),
    BRACELET_OF_ETHEREUM(DegradeType.USE, 21816, 21817, 16000, 0),
    BRACELET_OF_CLAY(DegradeType.USE, 11074, -1, 28, 0),
	SCYTHE_OF_VITUR(DegradeType.OUTGOING_HIT, ItemId.SCYTHE_OF_VITUR, ItemId.SCYTHE_OF_VITUR_UNCHARGED, 20000, 0),
    HOLY_SCYTHE_OF_VITUR(DegradeType.OUTGOING_HIT, ItemId.HOLY_SCYTHE_OF_VITUR, ItemId.HOLY_SCYTHE_OF_VITUR_UNCHARGED, 20000, 0),
    SANGUINE_SCYTHE_OF_VITUR(DegradeType.OUTGOING_HIT, ItemId.SANGUINE_SCYTHE_OF_VITUR, ItemId.SANGUINE_SCYTHE_OF_VITUR_UNCHARGED, 20000, 0),
    SANGUINESTI_STAFF(DegradeType.TRIDENT, ItemId.SANGUINESTI_STAFF, ItemId.SANGUINESTI_STAFF_UNCHARGED, 20000, 0),
    HOLY_SANGUINESTI_STAFF(DegradeType.TRIDENT, ItemId.HOLY_SANGUINESTI_STAFF, ItemId.HOLY_SANGUINESTI_STAFF_UNCHARGED, 20000, 0),
    TUMEKENS_SHADOW(DegradeType.TRIDENT, ItemId.TUMEKENS_SHADOW, ItemId.TUMEKENS_SHADOW_UNCHARGED, 20000, 0,
            item -> new Item[]{new Item(ItemId.SOUL_RUNE, item.getCharges() * TOAConstants.SOUL_RUNES_MOD), new Item(ItemId.CHAOS_RUNE, item.getCharges() * TOAConstants.CHAOS_RUNES_MOD)}),
    CRAWS_BOW(DegradeType.OUTGOING_HIT, 22550, 22547, 17000, 0),

    WEBWEAVER_BOW(DegradeType.OUTGOING_HIT, 27655, 27652, 17000, 0),

    VIGGORAS_CHAINMACE(DegradeType.OUTGOING_HIT, 22545, 22542, 17000, 0),

    URSINE_CHAINMACE(DegradeType.OUTGOING_HIT, 27660, 27657, 17000, 0),

    THAMMARONS_SCEPTRE(DegradeType.OUTGOING_HIT, ItemId.THAMMARONS_SCEPTRE,  ItemId.THAMMARONS_SCEPTRE_U, 17000, 0),
    THAMMARONS_SCEPTRE_A(DegradeType.OUTGOING_HIT, ItemId.THAMMARONS_SCEPTRE_A, ItemId.THAMMARONS_SCEPTRE_AU, 17000, 0),

    ACCURSED_SCEPTRE(DegradeType.OUTGOING_HIT, ItemId.ACCURSED_SCEPTRE_27665,  ItemId.ACCURSED_SCEPTRE_U_27662, 17000, 0),
    ACCURSED_SCEPTRE_A(DegradeType.OUTGOING_HIT, ItemId.ACCURSED_SCEPTRE_A_27679, ItemId.ACCURSED_SCEPTRE_AU_27676, 17000, 0),

    STARTER_STAFF(DegradeType.OUTGOING_HIT, 22333, 22333, 1000, 0),
    STARTER_BOW(DegradeType.OUTGOING_HIT, 22335, 22335, 1000, 0),
    XERICS_TALISMAN(DegradeType.USE, 13393, 13392, 1000, 0),
//    ICE_GLOVES(DegradeType.USE, 1580, -1, 150, 0),
    //Exception, handled differently.
    BLOWPIPE(DegradeType.USE, 12926, 12924, 0, 0, item -> {
        final int scales = item.getNumericAttribute("blowpipeScales").intValue();
        final int darts = item.getNumericAttribute("blowpipeDarts").intValue();
        final int type = item.getNumericAttribute("blowpipeDartType").intValue();
        return new Item[]{new Item(type, darts), new Item(12934, scales)};
    }),
    CELESTIAL_RING(DegradeType.CUSTOM, ItemId.CELESTIAL_RING, ItemId.CELESTIAL_RING_UNCHARGED, 10_000, 0),
    BLOOD_ESSENCE(DegradeType.USE, ItemId.BLOOD_ESSENCE_ACTIVE, -1, BloodEssence.CHARGES, 0),
    VENATOR_BOW(DegradeType.OUTGOING_HIT, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED, 50_000, 0)
    ;

    public static final Map<Integer, Degradeable> ITEMS = new HashMap<>();
    private static final Map<Integer, Degradeable> BROKEN_ITEMS = new HashMap<>();
    private static final Int2IntMap degradedToFullIdMap = new Int2IntOpenHashMap();

    static {
        for (final Degradeable value : values()) {
            register(value);
        }
        register(CrystalTool.Axe.INSTANCE);
        register(CrystalTool.Pickaxe.INSTANCE);
        register(CrystalTool.Harpoon.INSTANCE);
        register(CrystalWeapon.BladeOfSaeldor.INSTANCE);
        register(CrystalWeapon.BowOfFaerdhinen.INSTANCE);
        register(CrystalWeapon.Halberd.INSTANCE);
        register(CrystalWeapon.Shield.INSTANCE);
        register(CrystalWeapon.Bow.INSTANCE);
        register(CrystalArmour.Body.INSTANCE);
        register(CrystalArmour.Helm.INSTANCE);
        register(CrystalArmour.Legs.INSTANCE);
        register(PolyporeStaff.INSTANCE);
    }

    static void register(Degradeable value) {
        ITEMS.put(value.getItemId(), value);
        BROKEN_ITEMS.put(value.getNextId(), value);
        degradedToFullIdMap.put(value.getItemId(), iterateToFullId(value.getItemId()));
    }

    private final DegradeType type;
    private final int itemId;
    private final int nextId;

    DegradableItem(final DegradeType type, final int itemId, final int nextId, final int maximumCharges, final int minimumCharges, final Function<Item, Item[]> function) {
        this.type = type;
        this.itemId = itemId;
        this.nextId = nextId;
        this.maximumCharges = maximumCharges;
        this.minimumCharges = minimumCharges;
        this.function = function;
    }

    private final int maximumCharges;
    private final int minimumCharges;
    private final Function<Item, Item[]> function;

    DegradableItem(final DegradeType type, final int itemId, final int nextId, final int maximumCharges, final int minimumCharges) {
        this(type, itemId, nextId, maximumCharges, minimumCharges, null);
    }

    /**
     * Gets the default charges of the given item.
     *
     * @param itemId
     * @return fully charged item's charges.
     */
    public static int getDefaultCharges(final int itemId, final int defaultValue) {
        final Degradeable item = ITEMS.get(itemId);
        if (item == null) {
            return defaultValue;
        }
        return item.getMaximumCharges();
    }

    public static int getCompletelyDegradedId(final int itemId) {
        return getCompletelyDegradedId(itemId, false);
    }

    /**
     * Gets the completely degraded variant of the item.
     * If there are no entries for the specific item in the code,
     * it will return the same id as the item.
     *
     * @param itemId
     * @return completely degraded id.
     */
    public static int getCompletelyDegradedId(final int itemId, boolean pvp) {
        int id = itemId;
        Degradeable last = null;
        while (true) {
            final Degradeable item = ITEMS.get(id);
            if (item == last || item == null) {
                return id;
            }
            if(pvp && (item == MAGMA_HELM || item == TANZANITE_HELM || item == SERPENTINE_HELM))
                return ItemId.SERPENTINE_HELM_UNCHARGED;
            last = item;
            id = item.getNextId();
        }
    }

    private static final Int2IntMap fullMap = new Int2IntOpenHashMap();

    static {
        for (final DegradableItem value : values()) {
            final int completelyDegradedItem = getCompletelyDegradedId(value.itemId);
            final int full = iterateToFullCharges(completelyDegradedItem);
            fullMap.put(value.itemId, full);
        }
    }

    private static int iterateToFullCharges(final int itemId) {
        int id = itemId;
        Degradeable last = null;
        final Degradeable current = DegradableItem.ITEMS.get(itemId);
        int charges = current == null ? 0 : current.getMaximumCharges();
        while (true) {
            final Degradeable item = BROKEN_ITEMS.get(id);
            if (item == last || item == null) {
                return charges;
            }
            last = item;
            id = item.getItemId();
            if (item.getMaximumCharges() > charges) {
                charges = item.getMinimumCharges();
            }
        }
    }

    private static int iterateToFullId(final int itemId) {
        int id = itemId;
        Degradeable last = null;
        final Degradeable current = DegradableItem.ITEMS.get(itemId);
        int charges = current == null ? 0 : current.getMaximumCharges();
        while (true) {
            final Degradeable item = BROKEN_ITEMS.get(id);
            if (item == last || item == null) {
                return id;
            }
            last = item;
            id = item.getItemId();
            if (item.getMaximumCharges() > charges) {
                charges = item.getMaximumCharges();
            }
        }
    }

    public static int getFullCharges(final int itemId) {
        return fullMap.get(itemId);
    }

    public static int[] findSubstitutes(final int id) {
        final int fullId = degradedToFullIdMap.get(id);
        if (fullId == 0) {
            return null;
        }
        Degradeable deg = ITEMS.get(fullId);
        if (deg == null) {
            return null;
        }
        final IntArrayList list = new IntArrayList();
        list.add(deg.getItemId());
        while (true) {
            final Degradeable nextDeg = DegradableItem.ITEMS.get(deg.getNextId());
            if (nextDeg == null || nextDeg == deg) {
                break;
            }
            deg = nextDeg;
            list.add(deg.getItemId());
        }
        return list.toIntArray();
    }

    @NotNull
    @Override
    public DegradeType getType() {
        return type;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public int getNextId() {
        return nextId;
    }

    @Override
    public int getMaximumCharges() {
        return maximumCharges;
    }

    @Override
    public int getMinimumCharges() {
        return minimumCharges;
    }

    @Override
    public Function<Item, Item[]> getFunction() {
        return function;
    }
}
