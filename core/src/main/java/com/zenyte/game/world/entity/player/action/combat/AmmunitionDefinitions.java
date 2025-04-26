package com.zenyte.game.world.entity.player.action.combat;

import com.near_reality.game.content.custom.GodBow;
import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import mgi.types.config.items.ItemDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.zenyte.game.item.ItemId.*;

/**
 * @author Kris | 5. jaan 2018 : 1:54.53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum AmmunitionDefinitions implements AmmunitionDefinition {

    BRONZE_ARROW(new int[] { 882, 598, 883, 5616, 5622 }, true, new Graphics(19, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(10, 40, 36, 41, 21, 5, 11, 5), 23357, 841, 843, 849, 853, 857, 861, 12788, 10280, 10282, 10284, 839, 845, 847, 851, 855, 859, 6724, 12424, 11235, 12765, 12766, 12767,
            12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    IRON_ARROW(new int[] { 884, 2532, 885, 5617, 5623 }, true, new Graphics(18, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(9, 40, 36, 41, 21, 5, 11, 5), 23357, 841, 843, 849, 853, 857, 861, 12788, 10280, 10282, 10284, 839, 845, 847, 851, 855, 859, 6724, 12424, 11235, 12765, 12766, 12767, 12768, DARK_BOW_BH, SCORCHING_BOW
            , ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    STEEL_ARROW(new int[] { 886, 2534, 887, 5618, 5624 }, true, new Graphics(20, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(11, 40, 36, 41, 21, 5, 11, 5), 843, 849, 853, 857, 861, 12788, 10280, 10282, 10284, 845, 847, 851, 855, 859, 6724, 12424, 11235, 12765, 12766,
            12767, 12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    MITHRIL_ARROW(new int[] { 888, 2536, 889, 5619, 5625 }, true, new Graphics(21, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(12, 40, 36, 41, 21, 5, 11, 5), 849, 853, 857, 861, 12788, 10280, 10282, 10284, 847, 851, 855, 859, 6724, 12424, 11235, 12765,
            12766, 12767, 12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    ADAMANT_ARROW(new int[] { 890, 2538, 891, 5620, 5626 }, true, new Graphics(22, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(13, 40, 36, 41, 21, 5, 11, 5), 853, 857, 861, 12788, 10282, 10284, 851, 855, 859, 6724, 12424,
            11235, 12765, 12766, 12767, 12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    BROAD_ARROW(new int[]{4160}, true, new Graphics(23, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(1384, 40, 36, 41, 21, 5, 11, 5), 861, 859, 12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    RUNE_ARROW(new int[] { 892, 2540, 893, 5621, 5627 }, true, new Graphics(24, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(15, 40, 36, 41, 21, 5, 11, 5), 857, 861, 12788, 10282, 10284, 855, 859, 6724, 12424, 11235, 12765, 12766, 12767, 12768, DARK_BOW_BH, SCORCHING_BOW,
            ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    AMETHYST_ARROW(new int[] { 21326, 21328, 21332, 21334, 21336 }, true, new Graphics(1385, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(1384, 40, 36, 41, 21, 5, 11, 5), 861, 12788, 10284, 859, 6724, 12424, 11235, 12765, 12766, 12767, 12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    DRAGON_ARROW(new int[] { 11212, 11217, 11227, 11228, 11229 }, true, new Graphics(1116, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(1120, 40, 36, 41, 21, 5, 11, 5), 12424, 11235, 12765, 12766, 12767, 12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    OGRE_ARROW(2866, true, new Graphics(243, 0, 96), new SoundEffect(2693, 0, 0), new Projectile(242, 40, 36, 41, 21, 5, 11, 5), ItemId.OGRE_BOW, ItemId.COMP_OGRE_BOW),





    LIT_BRONZE_ARROW(942, true, new Graphics(26, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(17, 40, 36, 41, 21, 5, 11, 5), 23357, 841, 843, 849, 853, 857, 861, 12788, 10280, 10282, 10284, 839, 845, 847, 851, 855, 859, 6724, 12424, 11235, 12765, 12766, 12767,
        12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    LIT_IRON_ARROW(2533, true, new Graphics(26, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(17, 40, 36, 41, 21, 5, 11, 5), 23357, 841, 843, 849, 853, 857, 861, 12788, 10280, 10282, 10284, 839, 845, 847, 851, 855, 859, 6724, 12424, 11235, 12765, 12766, 12767,
        12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    LIT_STEEL_ARROW(2535, true, new Graphics(26, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(17, 40, 36, 41, 21, 5, 11, 5), 843, 849, 853, 857, 861, 12788, 10280, 10282, 10284, 845, 847, 851, 855, 859, 6724, 12424, 11235, 12765, 12766,
            12767, 12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    LIT_MITHRIL_ARROW(2537, true, new Graphics(26, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(17, 40, 36, 41, 21, 5, 11, 5), 849, 853, 857, 861, 12788, 10280, 10282, 10284, 847, 851, 855, 859, 6724, 12424, 11235, 12765, 12766, 12767, 12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    LIT_ADAMANT_ARROW(2539, true, new Graphics(26, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(17, 40, 36, 41, 21, 5, 11, 5), 853, 857, 861, 12788, 10282, 10284, 851, 855, 859, 6724, 12424, 11235, 12765, 12766, 12767, 12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    LIT_RUNE_ARROW(2541, true, new Graphics(26, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(17, 40, 36, 41, 21, 5, 11, 5), 857, 861, 12788, 10282, 10284, 855, 859, 6724, 12424, 11235, 12765, 12766, 12767, 12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    LIT_AMETHYST_ARROW(21330, true, new Graphics(26, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(17, 40, 36, 41, 21, 5, 11, 5), 861, 12788, 10284, 859, 6724, 12424, 11235, 12765, 12766, 12767, 12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    LIT_DRAGON_ARROW(11222, true, new Graphics(1116, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(1121, 40, 36, 41, 21, 5, 11, 5), 12424, 11235, 12765, 12766, 12767, 12768, DARK_BOW_BH, SCORCHING_BOW, ItemId.TWISTED_BOW, ItemId.PURPLE_TWISTED_BOW, ItemId.BLUE_TWISTED_BOW, ItemId.WHITE_TWISTED_BOW, ItemId.RED_TWISTED_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW, ItemId.VENATOR_BOW_UNCHARGED),

    BRUTAL_ARROW(new int[] { 4773, 4778, 4783, 4788, 4793, 4798, 4803 }, true, new Graphics(403, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(404, 40, 36, 41, 21, 5, 11, 5), 4827),






    BRONZE_BOLT(new int[] { 877, 879 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        767, 837, 9174, 9176, 9177, 9179, 9181, 9183, ItemId.RUNE_CROSSBOW, 11165, 11167, 11785, 21012, 21902, DRAGON_CROSSBOW_CR, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B),

    BLURITE_BOLT(new int[] { 9139, 9335 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9176, 9177, 9179, 9181, 9183, ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, 21902, DRAGON_CROSSBOW_CR, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B),

    SILVER_BOLT(9145, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9177, 9179, 9181, 9183, ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, 21902, DRAGON_CROSSBOW_CR, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B),

    IRON_BOLT(new int[] { 9140, 880 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9177, 9179, 9181, 9183, ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, 21902, DRAGON_CROSSBOW_CR, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B),

    STEEL_BOLT(new int[] { 9141, 9336 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9179, 9181, 9183, ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, 21902, DRAGON_CROSSBOW_CR, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B),

    MITHRIL_BOLT(new int[] { 9142, 9337, 9338 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9181, 9183, ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, 21902, DRAGON_CROSSBOW_CR, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B),

    ADAMANT_BOLT(new int[] { 9143, 9339, 9340 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9183, ItemId.RUNE_CROSSBOW, 26486, 11785, ItemId.DRAGON_HUNTER_CROSSBOW, 21902, DRAGON_CROSSBOW_CR, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B),

    RUNITE_BOLT(new int[] { 9144, 9341, 9342 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        ItemId.RUNE_CROSSBOW, 26486, 11785, ItemId.DRAGON_HUNTER_CROSSBOW, 21902, DRAGON_CROSSBOW_CR, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B),

    DRAGON_BOLT(new int[] { 21905, 21924, 21926, 21928, 21955, 21957, 21959, 21961, 21963, 21965, 21967, 21969, 21971, 21973 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR),

    BROAD_BOLT(new int[] { 11875, 21316 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5), ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, 21902),

    BOLT_RACK(4740, false, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5), 4934, 4935, 4936, 4937, 4938, 4734),

	BONE_BOLT(8882, true, new SoundEffect(1081, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5), 8880),

	KEBBIT_BOLT(new int[] { 10158, 10159 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5), 10156),






    BRONZE_JAVELIN(new int[] { 825, 831, 5642, 5648 }, false, new SoundEffect(2699, 0, 20),
            new Projectile(200, 38, 36, 42, 1, 5, 120, 3), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    IRON_JAVELIN(new int[] { 826, 832, 5643, 5649 }, false, new SoundEffect(2699, 0, 20),
            new Projectile(201, 38, 36, 42, 1, 5, 120, 3), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    STEEL_JAVELIN(new int[] { 827, 833, 5644, 5650 }, false, new SoundEffect(2699, 0, 20),
            new Projectile(202, 38, 36, 42, 1, 5, 120, 3), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    //TODO Verify id
    MITHRIL_JAVELIN(new int[] { 828, 834, 5645, 5651 }, false, new SoundEffect(2699, 0, 20),
            new Projectile(203, 38, 36, 42, 1, 5, 120, 3), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    ADAMANT_JAVELIN(new int[] { 829, 835, 5646, 5652 }, false, new SoundEffect(2699, 0, 20),
            new Projectile(204, 38, 36, 42, 1, 5, 120, 3), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    RUNE_JAVELIN(new int[] { 830, 836, 5647, 5653 }, false, new SoundEffect(2699, 0, 20),
            new Projectile(205, 38, 36, 42, 1, 5, 120, 3), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    AMETHYST_JAVELIN(new int[] { 21318, 21320, 21322, 21324 }, false, new SoundEffect(2699, 0, 20),
            new Projectile(1386, 38, 36, 42, 1, 5, 120, 3), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    DRAGON_JAVELIN(new int[] { 19484, 19486, 19488, 19490 }, false, new SoundEffect(2699, 0, 20),
            new Projectile(1301, 38, 36, 42, 1, 5, 120, 3), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),






    BRONZE_THROWNAXE(800, true, new Graphics(43, 0, 96), new SoundEffect(2706, 0, 0),
            new Projectile(36, 40, 36, 32, 21, 0, 11, 5)),

	IRON_THROWNAXE(801, true, new Graphics(42, 0, 96), new SoundEffect(2706, 0, 0),
            new Projectile(35, 40, 36, 32, 21, 0, 11, 5)),

	STEEL_THROWNAXE(802, true, new Graphics(44, 0, 96), new SoundEffect(2706, 0, 0),
            new Projectile(37, 40, 36, 32, 21, 0, 11, 5)),

	MITHRIL_THROWNAXE(803, true, new Graphics(45, 0, 96), new SoundEffect(2706, 0, 0),
            new Projectile(38, 40, 36, 32, 21, 0, 11, 5)),

	ADAMANT_THROWNAXE(804, true, new Graphics(46, 0, 96), new SoundEffect(2706, 0, 0),
            new Projectile(39, 40, 36, 32, 21, 0, 11, 5)),

	RUNE_THROWNAXE(805, true, new Graphics(48, 0, 96), new SoundEffect(2706, 0, 0),
            new Projectile(41, 40, 36, 32, 21, 0, 11, 5)),

	DRAGON_THROWNAXE(20849, true, new Graphics(1320, 0, 96), new SoundEffect(2706, 0, 0),
            new Projectile(1319, 40, 36, 32, 21, 0, 11, 5)),

    MORRIGANS_THROWING_AXE(22634, true, new Graphics(1624, 0, 96), new SoundEffect(2706, 0, 0),
            new Projectile(1623, 42, 30, 40, 15, 3, 64, 5)),

    MORRIGANS_JAVELIN(22636, true, new Graphics(1619, 0, 96), new SoundEffect(2706, 0, 0),
            new Projectile(1620, 42, 30, 40, 15, 3, 64, 5)),





    BRONZE_KNIFE(new int[]{864, 870, 5654, 5661}, true, new Graphics(219, 0, 96), new SoundEffect(2707, 0, 0),
            new Projectile(212, 40, 36, 32, 21, 0, 11, 5)),

    IRON_KNIFE(new int[]{863, 871, 5655, 5662}, true, new Graphics(220, 0, 96), new SoundEffect(2707, 0, 0),
            new Projectile(213, 40, 36, 32, 21, 0, 11, 5)),

    STEEL_KNIFE(new int[]{865, 872, 5656, 5663}, true, new Graphics(221, 0, 96), new SoundEffect(2707, 0, 0),
            new Projectile(214, 40, 36, 32, 21, 0, 11, 5)),

    BLACK_KNIFE(new int[]{869, 874, 5658, 5665}, true, new Graphics(222, 0, 96), new SoundEffect(2707, 0, 0),
            new Projectile(215, 40, 36, 32, 21, 0, 11, 5)),

    MITHRIL_KNIFE(new int[]{866, 873, 5657, 5664}, true, new Graphics(223, 0, 96), new SoundEffect(2707, 0, 0),
            new Projectile(216, 40, 36, 32, 21, 0, 11, 5)),

    ADAMANT_KNIFE(new int[]{867, 875, 5659, 5666}, true, new Graphics(224, 0, 96), new SoundEffect(2707, 0, 0),
            new Projectile(217, 40, 36, 32, 21, 0, 11, 5)),

    RUNE_KNIFE(new int[]{868, 876, 5660, 5667}, true, new Graphics(225, 0, 96), new SoundEffect(2707, 0, 0),
            new Projectile(218, 40, 36, 32, 21, 0, 11, 5)),

    DRAGON_KNIFE(new int[]{22804, 22812, 22814}, true, null, new SoundEffect(2707, 0, 0),
            new Projectile(28, 40, 36, 32, 21, 0, 11, 5)),
    DRAGON_KNIFE_POISONED(new int[]{22806, 22808, 22810}, true, null, new SoundEffect(2707, 0, 0),
            new Projectile(697, 40, 36, 32, 21, 0, 11, 5)),



    BRONZE_DART(new int[]{806, 819, 5628, 5635}, true, new Graphics(232, 0, 96), new SoundEffect(2696, 0, 0),
            new Projectile(226, 40, 36, 32, 21, 0, 11, 5)),

    IRON_DART(new int[]{807, 813, 5629, 5636}, true, new Graphics(233, 0, 96), new SoundEffect(2696, 0, 0),
            new Projectile(226, 40, 36, 32, 21, 0, 11, 5)),

    STEEL_DART(new int[]{808, 821, 5630, 5637}, true, new Graphics(234, 0, 96), new SoundEffect(2696, 0, 0),
            new Projectile(228, 40, 36, 32, 21, 0, 11, 5)),

    BLACK_DART(new int[]{3093, 3094, 5631, 5638}, true, new Graphics(273, 0, 96), new SoundEffect(2696, 0, 0),
            new Projectile(34, 40, 36, 32, 21, 0, 11, 5)),

    MITHRIL_DART(new int[]{809, 815, 5632, 5639}, true, new Graphics(235, 0, 96), new SoundEffect(2696, 0, 0),
            new Projectile(229, 40, 36, 32, 21, 0, 11, 5)),

    ADAMANT_DART(new int[]{810, 816, 5633, 5640}, true, new Graphics(236, 0, 96), new SoundEffect(2696, 0, 0),
            new Projectile(230, 40, 36, 32, 21, 0, 11, 5)),

    RUNE_DART(new int[]{811, 817, 5634, 5641}, true, new Graphics(237, 0, 96), new SoundEffect(2696, 0, 0),
            new Projectile(231, 40, 36, 32, 21, 0, 11, 5)),

    AMETHYST_DART(new int[]{ItemId.AMETHYST_DART, ItemId.AMETHYST_DARTP, ItemId.AMETHYST_DARTP_25855, ItemId.AMETHYST_DARTP_25857}, true,
            new Graphics(1937, 0, 96), new SoundEffect(2696, 0, 0),
            new Projectile(1936, 40, 36, 32, 21, 0, 11, 5)),

    DRAGON_DART(new int[]{11230, 11231, 11233, 11234}, true, new Graphics(1123, 0, 96), new SoundEffect(2696, 0, 0),
            new Projectile(1122, 40, 36, 32, 21, 0, 11, 5)),





    GREY_CHINCHOMPA(10033, false, new SoundEffect(2706, 0, 0),
            new Projectile(908, 40, 36, 21, 21, 11, 11, 5)),

	RED_CHINCHOMPA(10034, false, new SoundEffect(2706, 0, 0),
            new Projectile(909, 40, 36, 21, 21, 11, 11, 5)),

	BLACK_CHINCHOMPA(11959, false, new SoundEffect(2706, 0, 0),
            new Projectile(1272, 40, 36, 21, 21, 11, 11, 5)),

	TOKTZ_XIL_UL(6522, true, new SoundEffect(2706, 0, 0),
            new Projectile(442, 40, 36, 32, 21, 0, 11, 5)),






    HOLY_WATER(732, false, null, new Projectile(192, 25, 20, 30, 15, 10, 64, 5)),

    MUD_PIE(7170, false, null, new Projectile(561, 25, 20, 30, 15, 10, 64, 5)),//TODO: write effect.

    HARRALANDER_TAR(10145, false, null, null, null, 10148),

    TARROMIN_TAR(10144, false, null, null, null, 10147),

    MARRENTILL_TAR(10143, false, null, null, null, 10146),

    GUAM_TAR(10142, false, null, null, null, 10149),




    BLOWPIPE_BRONZE_DART(806, true, null, new SoundEffect(2696, 0, 0),
            new Projectile(226, 35, 36, 32, 21, 0, 105, 5)),

    BLOWPIPE_IRON_DART(807, true, null, new SoundEffect(2696, 0, 0),
            new Projectile(227, 35, 36, 32, 21, 0, 105, 5)),

    BLOWPIPE_STEEL_DART(808, true, null, new SoundEffect(2696, 0, 0),
            new Projectile(228, 35, 36, 32, 21, 0, 105, 5)),

    BLOWPIPE_BLACK_DART(3093, true, null, new SoundEffect(2696, 0, 0),
            new Projectile(34, 35, 36, 32, 21, 0, 105, 5)),

    BLOWPIPE_MITHRIL_DART(809, true, null, new SoundEffect(2696, 0, 0),
            new Projectile(229, 35, 36, 32, 21, 0, 105, 5)),

    BLOWPIPE_ADAMANT_DART(810, true, null, new SoundEffect(2696, 0, 0),
            new Projectile(230, 35, 36, 32, 21, 0, 105, 5)),

    BLOWPIPE_RUNE_DART(811, true, null, new SoundEffect(2696, 0, 0),
            new Projectile(231, 35, 36, 32, 21, 0, 105, 5)),

    BLOWPIPE_AMETHYST_DART(ItemId.AMETHYST_DART, true, null, new SoundEffect(2696, 0, 0),
            new Projectile(1936, 35, 36, 32, 21, 0, 105, 5)),

    BLOWPIPE_DRAGON_DART(11230, true, null, new SoundEffect(2696, 0, 0),
            new Projectile(1122, 35, 36, 32, 21, 0, 105, 5)),




    CONCENTRATED_BRONZE_JAVELIN(new int[] { 825, 831, 5642, 5648 }, false, new SoundEffect(2536, 0, 0),
            new Projectile(200, 38, 36, 49, 1, -18, 120, 5), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    CONCENTRATED_IRON_JAVELIN(new int[] { 826, 832, 5643, 5649 }, false, new SoundEffect(2536, 0, 0),
            new Projectile(201, 38, 36, 49, 1, -18, 120, 5), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    CONCENTRATED_STEEL_JAVELIN(new int[] { 827, 833, 5644, 5650 }, false, new SoundEffect(2536, 0, 0),
            new Projectile(202, 38, 36, 49, 1, -18, 120, 5), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    CONCENTRATED_MITHRIL_JAVELIN(new int[] { 828, 834, 5645, 5651 }, false, new SoundEffect(2536, 0, 0),
            new Projectile(203, 38, 36, 49, 1, -18, 120, 5), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    CONCENTRATED_ADAMANT_JAVELIN(new int[] { 829, 835, 5646, 5652 }, false, new SoundEffect(2536, 0, 0),
            new Projectile(204, 38, 36, 49, 1, -18, 120, 5), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    CONCENTRATED_RUNE_JAVELIN(new int[] { 830, 836, 5647, 5653 }, false, new SoundEffect(2536, 0, 0),
            new Projectile(205, 38, 36, 49, 1, -18, 120, 5), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    CONCENTRATED_AMETHYST_JAVELIN(new int[] { 21318, 21320, 21322, 21324 }, false, new SoundEffect(2536, 0, 0),
            new Projectile(1386, 38, 36, 49, 1, -18, 120, 5), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    CONCENTRATED_DRAGON_JAVELIN(new int[] { 19484, 19486, 19488, 19490 }, false, new SoundEffect(2536, 0, 0),
            new Projectile(1301, 38, 36, 49, 1, -18, 120, 5), ItemId.LIGHT_BALLISTA, ItemId.HEAVY_BALLISTA, ItemId.HEAVY_BALLISTA_OR),

    CRYSTAL_ARROW(
            BowItemIds.CRYSTAL_BOW_ITEM_IDS,
            false,
            new Graphics(250, 0, 96),
            new SoundEffect(1352, 0, 0),
            new Projectile(249, 40, 36, 41, 21, 5, 11, 5),
            BowItemIds.CRYSTAL_BOW_ITEM_IDS
    ),

    CRAWS_BOW_ARROW(new int[] { 22547, 22550 }, false, new Graphics(1611, 0, 96), new SoundEffect(1352, 0, 0),
            new Projectile(1574, 40, 36, 41, 21, 5, 11, 5), 22547, 22550),

    WEBWEAVER_BOW(new int[] { 27652, 27655 }, false, new Graphics(2283, 0, 96), new SoundEffect(1352, 0, 0),
            new Projectile(2282, 40, 36, 41, 21, 5, 11, 5), 27652, 27655),

    STARTER_ARROW(22333, false, new Graphics(1525, 0, 96), new SoundEffect(2693, 0, 0),
            new Projectile(1524, 40, 36, 41, 21, 5, 11, 5), 22333),


    ENCHANTED_OPAL_BOLT(new int[] { 9236 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        767, 837, 9174, 9176, 9177, 9179, 9181, 9183, ItemId.RUNE_CROSSBOW, 26486, 11165, 11167, 11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    ENCHANTED_JADE_BOLT(new int[] { 9237 }, true, new SoundEffect(2695, 0, 0, ItemId.ZARYTE_CROSSBOW),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9176, 9177, 9179, 9181, 9183, ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    ENCHANTED_PEARL_BOLT(new int[] { 9238 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9177, 9179, 9181, 9183, ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    ENCHANTED_TOPAZ_BOLT(new int[] { 9239 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9179, 9181, 9183, ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    ENCHANTED_SAPPHIRE_BOLT(new int[] { 9240 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9181, 9183, ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    ENCHANTED_EMERALD_BOLT(new int[] { 9241 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9181, 9183, ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    ENCHANTED_RUBY_BOLT(new int[] { 9242 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9183, ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    ENCHANTED_DIAMOND_BOLT(new int[] { 9243 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        9183, ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    ENCHANTED_DRAGON_BOLT(new int[] { 9244 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    ENCHANTED_ONYX_BOLT(new int[] { 9245 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        ItemId.RUNE_CROSSBOW, 26486, 11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),

    DRAGON_ENCHANTED_OPAL_BOLT(new int[] { 21932 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    DRAGON_ENCHANTED_JADE_BOLT(new int[] { 21934 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    DRAGON_ENCHANTED_PEARL_BOLT(new int[] { 21936 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    DRAGON_ENCHANTED_TOPAZ_BOLT(new int[] { 21938 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    DRAGON_ENCHANTED_SAPPHIRE_BOLT(new int[] { 21940 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    DRAGON_ENCHANTED_EMERALD_BOLT(new int[] { 21942 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    DRAGON_ENCHANTED_RUBY_BOLT(new int[] { 21944 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    DRAGON_ENCHANTED_DIAMOND_BOLT(new int[] { 21946 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    DRAGON_ENCHANTED_DRAGON_BOLT(new int[] { 21948 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    DRAGON_ENCHANTED_ONYX_BOLT(new int[] { 21950 }, true, new SoundEffect(2695, 0, 0),
            new Projectile(27, 38, 36, 41, 7, 5, 11, 5),
        11785, 21012, ItemId.DRAGON_HUNTER_CROSSBOW_T, ItemId.DRAGON_HUNTER_CROSSBOW_B, 21902, DRAGON_CROSSBOW_CR, ItemId.ZARYTE_CROSSBOW),
    IMBUED_TEPHRA(23_907, false, null, new Projectile(1731, 40, 36, 21, 21, 11, 11, 5)),
    ;

	AmmunitionDefinitions(final int itemId, final boolean retrievable, final Graphics drawbackGfx, SoundEffect soundEffect, final Projectile projectile, final int... bows) {
		this(new int[] { itemId }, retrievable, drawbackGfx, soundEffect, projectile, bows);
	}

	AmmunitionDefinitions(final int itemId, final boolean retrievable, SoundEffect soundEffect, final Projectile projectile, final int... bows) {
		this(itemId, retrievable, null, soundEffect, projectile, bows);
	}

	AmmunitionDefinitions(final int[] itemId, final boolean retrievable, SoundEffect soundEffect, final Projectile projectile, final int... bows) {
		this(itemId, retrievable, null, soundEffect, projectile, bows);
	}

	AmmunitionDefinitions(final int[] itemId, final boolean retrievable, final Graphics drawbackGfx, SoundEffect soundEffect, final Projectile projectile, final int... bows) {
		this.itemIds = itemId;
		this.drawbackGfx = drawbackGfx;
		this.soundEffect = soundEffect;
		this.projectile = projectile;
		this.bows = bows;
		this.retrievable = retrievable;
		this.weapon = ItemDefinitions.getOrThrow(itemId[0]).getSlot() == EquipmentSlot.WEAPON.getSlot();
	}

    private static final AmmunitionDefinitions[] VALUES = values();

	private static final Map<Integer, AmmunitionDefinition> definitions = new HashMap<>(VALUES.length);
	private static final Map<Integer, AmmunitionDefinition> blowpipeDefinitions = new HashMap<>(VALUES.length);
	private static final Map<Integer, AmmunitionDefinition> concentratedDefinitions = new HashMap<>(VALUES.length);

    static {
		for (final AmmunitionDefinitions defs : AmmunitionDefinitions.VALUES) {
			if (defs.toString().startsWith("BLOWPIPE")) {
				for (final int i : defs.itemIds) {
					blowpipeDefinitions.put(i, defs);
				}
			} else if (defs.toString().startsWith("CONCENTRATED")) {
				for (final int i : defs.itemIds) {
                    concentratedDefinitions.put(i, defs);
                }
            } else {
                for (final int i : defs.itemIds) {
                    definitions.put(i, defs);
                }
            }
        }
        definitions.put(GodBow.Saradomin.INSTANCE.getItemId(), GodBow.Saradomin.INSTANCE);
        definitions.put(GodBow.Bandos.INSTANCE.getItemId(), GodBow.Bandos.INSTANCE);
        definitions.put(GodBow.Zamorak.INSTANCE.getItemId(), GodBow.Zamorak.INSTANCE);
        definitions.put(GodBow.Armadyl.INSTANCE.getItemId(), GodBow.Armadyl.INSTANCE);
    }

    private final int[] itemIds;
    private final Graphics drawbackGfx;
    private final SoundEffect soundEffect;
    private final Projectile projectile;
    private final int[] bows;
    private final boolean retrievable;
    private final boolean weapon;

    public static AmmunitionDefinition getDefinitions(final int id) {
        return definitions.get(id);
    }

    public static AmmunitionDefinition getBlowpipeDefinitions(final int id) {
        return blowpipeDefinitions.get(id);
    }

    public static AmmunitionDefinition getConcentratedDefinitions(final int id) {
        return concentratedDefinitions.get(id);
    }

    @Override
    public boolean isCompatible(final int bowId) {
        return bows.length == 0 || ArrayUtils.contains(bows, bowId);
    }

    @NotNull
    @Override
    public int[] getItemIds() {
        return itemIds;
    }

    @Override
    public Graphics getDrawbackGfx() {
        return drawbackGfx;
    }

    @Override
    public SoundEffect getSoundEffect() {
        return soundEffect;
    }

    @Override
    public Projectile getProjectile() {
        return projectile;
    }

    @NotNull
    @Override
    public int[] getBows() {
        return bows;
    }

    @Override
    public boolean isRetrievable() {
        return retrievable;
    }

    @Override
    public boolean isWeapon() {
        return weapon;
    }


}
