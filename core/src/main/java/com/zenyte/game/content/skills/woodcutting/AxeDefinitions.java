package com.zenyte.game.content.skills.woodcutting;

import com.near_reality.game.content.crystal.recipes.chargeable.CrystalTool;
import com.near_reality.game.content.skills.woodcutting.AxeDefinition;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 11. dets 2017 : 4:25.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum AxeDefinitions implements AxeDefinition {
	NO_LEVEL(-1, -1, -1, -1, -1, -1),
	BRONZE(1351, 1, 6, 879, 3291, 3291),
	IRON(1349, 1, 6, 877, 3290, 3290),
	STEEL(1353, 5, 5, 875, 3289, 3289),
	BLACK(1361, 11, 5, 873, 3288, 3288),
	MITHRIL(1355, 21, 4, 871, 3287, 3287),
	ADAMANT(1357, 31, 3, 869, 3286, 3286),
	RUNE(1359, 41, 2, 867, 3285, 3285),
	GILDED(23279, 41, 2, 8303, 8305, 8305),
	DRAGON(6739, 61, -1, 2846, 3292, 3292) {
	    public int getCutTime() {
	        return Utils.random(1, 2);
        }
	},
	THIRD_AGE(20011, 61, -1, 7264, 7266, 7266) {
        public int getCutTime() {
            return Utils.random(1, 2);
        }
    },
	INFERNAL(13241, 61, -1, 2117, 2116, 2116) {
        public int getCutTime() {
            return Utils.random(1, 2);
        }
    },
    UNCHARGED_INFERNAL(13242, 61, -1, 2117, 2116, 2116) {
        public int getCutTime() {
            return Utils.random(1, 2);
        }
    },
	CRYSTAL_AXE_INACTIVE(
			CrystalTool.Axe.INSTANCE.getInactiveId(),
			CrystalTool.Axe.INSTANCE.getLevelRequired(),
			-1, CrystalTool.Axe.INSTANCE.getTreeCutAnimation().getId(),
			CrystalTool.Axe.INSTANCE.getTrunkCutAnimation().getId(),
			CrystalTool.Axe.INSTANCE.getCanoeCutAnimation().getId()
	) {
		public int getCutTime() {
			return Utils.random(1, 2);
		}
	};

	private final int itemId, levelRequired, cutTime;
	private final Animation treeCutEmote;
	private final Animation trunkCutEmote;
	private final Animation canoeCutEmote;

	private static final AxeDefinitions[] ENUM_VALUES = values();
	
	public static final AxeDefinition[] VALUES;
	
	static {
		final List<AxeDefinition> axes = new ArrayList<>();
		for (int i = ENUM_VALUES.length - 1; i >= 1; i--)
			axes.add(ENUM_VALUES[i]);
		axes.add(CrystalTool.Axe.INSTANCE);
		VALUES = axes.toArray(AxeDefinition[]::new);
	}

	AxeDefinitions(final int itemId,
				   final int levelRequried,
				   final int axeTime,
				   final int treeCutEmoteId,
				   final int trunkCutEmoteId,
				   final int canoeCutEmoteId
	) {
		this.itemId = itemId;
		levelRequired = levelRequried;
		cutTime = axeTime;
		treeCutEmote = new Animation(treeCutEmoteId);
		trunkCutEmote = new Animation(trunkCutEmoteId);
		canoeCutEmote = new Animation(canoeCutEmoteId);
	}

	@Override
	public int getItemId() {
	    return itemId;
	}

	@Override
	public int getLevelRequired() {
	    return levelRequired;
	}

	@Override
	public int getCutTime() {
	    return cutTime;
	}

	@Override
	public Animation getTreeCutAnimation() {
	    return treeCutEmote;
	}

	@Nullable
	@Override
	public Animation getTrunkCutAnimation() {
		return trunkCutEmote;
	}

	@Nullable
	@Override
	public Animation getCanoeCutAnimation() {
		return canoeCutEmote;
	}
}
