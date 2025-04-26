package com.zenyte.game.content.tombsofamascut;

import mgi.types.config.StructDefinitions;

/**
 * @author Savions.
 */
public enum InvocationType {

	TRY_AGAIN(417),
	PERSISTENCE(418),
	SOFTCORE_RUN(419),
	HARDCORE_RUN(420),
	WALK_FOR_IT(421),
	JOG_FOR_IT(422),
	RUN_FOR_IT(423),
	SPRINT_FOR_IT(424),
	NEED_SOME_HELP(425),
	NEED_LESS_HELP(426),
	NO_HELP_NEEDED(427),
	WALK_THE_PATH(428),
	PATHSEEKER(429),
	PATHFINDER(430),
	PATHMASTER(431),
	QUIET_PRAYERS(432),
	DEADLY_PRAYERS(433),
	ON_A_DIET(434),
	DEHYDRATION(435),
	OVERLY_DRAINING(436),
	LIVELY_LARVAE(437),
	MORE_OVERLORDS(438),
	BLOWING_MUD(439),
	MEDIC(440),
	AERIAL_ASSAULT(441),
	NOT_JUST_A_HEAD(442),
	ARTERIAL_SPRAY(444),
	BLOOD_THINNERS(445),
	UPSET_STOMACH(447),
	DOUBLE_TROUBLE(448),
	KEEP_BACK(449),
	STAY_VIGILANT(542),
	FEELING_SPECIAL(543),
	MIND_THE_GAP(603),
	GOTTA_HAVE_FAITH(752),
	JUNGLE_JAPES(949),
	SHAKING_THINGS_UP(1275),
	BOULDERDASH(1276),
	ANCIENT_HASTE(1278),
	ACCELERATION(1688),
	PENETRATION(2874),
	OVERCLOCKED(2933),
	OVERCLOCKED_2(2934),
	INSANITY(2971);

	public static final InvocationType[] VALUES = values();
	private static final int STRUCT_ID_PARAM = 1159;
	private static final int STRUCT_CATEGORY_PARAM = 1161;
	private static final int STRUCT_LEVEL_MODIFIER_PARAM = 1162;
	private static int[] INDICES;
	private static InvocationCategoryType[] CATAGORIES;
	private static int[] LEVEL_MODIFIERS;

	static {
		INDICES = new int[VALUES.length];
		LEVEL_MODIFIERS = new int[VALUES.length];
		CATAGORIES = new InvocationCategoryType[VALUES.length];
		for (int indx = 0; indx < VALUES.length; indx++) {
			final StructDefinitions definitions = StructDefinitions.get(VALUES[indx].structId);
			INDICES[indx] = definitions.getParamAsInt(STRUCT_ID_PARAM);
			CATAGORIES[indx] = InvocationCategoryType.VALUES[definitions.getParamAsInt(STRUCT_CATEGORY_PARAM) - 3];
			LEVEL_MODIFIERS[indx] = definitions.getParamAsInt(STRUCT_LEVEL_MODIFIER_PARAM);
		}
	}

	private final int structId;

	InvocationType(final int structId) {
		this.structId = structId;
	}

	public final int getIndex() { return INDICES[ordinal()]; }
	public final InvocationCategoryType getCategory() { return CATAGORIES[ordinal()]; }
	public final int getLevelModifier() { return LEVEL_MODIFIERS[ordinal()]; }
}
