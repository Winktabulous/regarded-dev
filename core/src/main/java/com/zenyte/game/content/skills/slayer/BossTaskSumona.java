package com.zenyte.game.content.skills.slayer;

import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import mgi.utilities.StringFormatUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public enum BossTaskSumona implements SlayerTask {
	KREE_ARRA_SUMONA(357, p -> p.getSkills().getLevelForXp(SkillConstants.RANGED) >= 70, "Kree'arra", false),
	KRIL_TSUTSAROTH_SUMONA(350.5F, p -> p.getSkills().getLevelForXp(SkillConstants.HITPOINTS) >= 70, "K'ril Tsutsaroth", false),
	COMMANDER_ZILYANA_SUMONA(350, p -> p.getSkills().getLevelForXp(SkillConstants.AGILITY) >= 70, "Commander Zilyana", false),
	GENERAL_GRAARDOR_SUMONA(338, p -> p.getSkills().getLevelForXp(SkillConstants.STRENGTH) >= 70, "General Graardor", false),
	DAGANNOTH_KINGS_SUMONA(331.5F, p -> true, "Dagannoth Kings", false) {
		@Override
		public boolean validate(@NotNull final String name, final NPC npc) {
			if (name == null) {
				throw new NullPointerException("name is marked non-null but is null");
			}
			return name.equalsIgnoreCase("Dagannoth Rex") || name.equalsIgnoreCase("Dagannoth Prime") || name.equalsIgnoreCase("Dagannoth Supreme");
		}

		@Override
		public float getExperience(final NPC npc) {
			return npc.getDefinitions().getName().equalsIgnoreCase("Dagannoth Supreme") ? 255 : 331.5F;
		}
	},
	KING_BLACK_DRAGON_SUMONA(258, p -> true, "King Black Dragon", false),
	VORKATH_SUMONA(750, p -> true, "Vorkath", false),
	VETION_SUMONA(312, p -> true, "Vet'ion reborn", true),
	CRAZY_ARCHEAOLOGIST_SUMONA(275, p -> true, "Crazy archaeologist", true),
	ZULRAH_SUMONA(500, p -> true, "Zulrah", false),
	CERBERUS_SUMONA(690, p -> p.getSkills().getLevelForXp(SkillConstants.SLAYER) >= 91, "Cerberus", false),
	GIANT_MOLE_SUMONA(215, p -> true, "Giant Mole", false),
	CALLISTO_SUMONA(312, p -> true, "Callisto", true),
	CHAOS_ELEMENTAL_SUMONA(250, p -> true, "Chaos elemental", true),
	SCORPIA_SUMONA(260, p -> true, "Scorpia", true),
	KRAKEN_SUMONA(255, p -> p.getSkills().getLevelForXp(SkillConstants.SLAYER) >= 87, "Kraken", false),
	ABYSSAL_SIRE_SUMONA(450, p -> p.getSkills().getLevelForXp(SkillConstants.SLAYER) >= 85, "Abyssal sire", false),
	KALPHITE_QUEEN_SUMONA(535.5F, p -> true, "Kalphite Queen", false),
	PHANTOM_MUSPAH(1763.7F, p -> true, "Phantom Muspah", false),
	VENENATIS_SUMONA(388.8F, p -> true, "Venenatis", true),
	CHAOS_FANATIC_SUMONA(253, p -> true, "Chaos Fanatic", true),
	THERMONUCLEAR_SMOKE_DEVIL_SUMONA(240, p -> p.getSkills().getLevelForXp(SkillConstants.SLAYER) >= 93, "Thermonuclear smoke devil", false),
	GROTESQUE_GUARDIANS_SUMONA(1350, p -> p.getSkills().getLevelForXp(SkillConstants.SLAYER) >= 75 && p.getNumericAttribute("brittle-entrance_unlocked").intValue() == 1, "Grotesque Guardians", false) {
		@Override
		public boolean validate(@NotNull final String name, final NPC npc) {
			if (name == null) {
				throw new NullPointerException("name is marked non-null but is null");
			}
			return name.equals("Dusk");
		}
	},
	BARROWS_SUMONA(255, p -> true, "Barrows Brothers", false) {
		@Override
		public boolean validate(@NotNull final String name, final NPC npc) {
			if (name == null) {
				throw new NullPointerException("name is marked non-null but is null");
			}
			return name.equals("Ahrim the Blighted") || name.equals("Dharok the Wretched") || name.equals("Guthan the Infested") || name.equals("Karil the Tainted") || name.equals("Torag the Corrupted") || name.equals("Verac the Defiled");
		}
	},
	CORPOREAL_BEAST_SUMONA(700, p -> true, "Corporeal beast", false),
	ALCHEMICAL_HYDRA_SUMONA(1320, p -> true, "Alchemical hydra", false),
	GAUNTLET_REG_SUMONA(600, p -> true, "Crystalline Hunllef", false),
	GAUNTLET_CORRUPT_SUMONA(800, p -> true, "Corrupted Hunllef", false),
	JAD_SUMONA(1000, p -> true, "TzTok-Jad", false),
	INFERNO_SUMONA(5000, p -> true, "TzKal-Zuk", false),
	SARACHNIS_SUMONA(200, p -> true, "Sarachnis", false),
	NIGHTMARE_SUMONA(1000, p -> true, "The Nightmare", false),
	NIGHTMARE_PHOSANIS_SUMONA(700, p -> true, "Phosani's Nightmare", false),
	NEX_SUMONA(1300, p -> true, "Nex", false),
	OLM_SUMONA(2500, p -> true, "Great Olm", false),
	TOB_SUMONA(2500, p -> true, "Verzik Vitur", false),
	;

	BossTaskSumona(final float xp, final Predicate<Player> predicate, final String name, final boolean wildernessTask) {
		this.xp = xp;
		this.predicate = predicate;
		monsters = new String[]{name.toLowerCase()};
		this.name = name;
		this.wildernessTask = wildernessTask;
	}

	private final float xp;
	private final Predicate<Player> predicate;
	private final String name;
	private final String[] monsters;
	private final boolean wildernessTask;
	public static final BossTaskSumona[] VALUES = values();
	public static final Map<String, BossTaskSumona> MAPPED_VALUES = new HashMap<>(VALUES.length);

	static {
		for (final BossTaskSumona value : VALUES) {
			MAPPED_VALUES.put(value.name.toLowerCase(), value);
		}
	}

	@Override
	public boolean validate(@NonNull final String name, final NPC npc) {
		if (name == null) {
			throw new NullPointerException("name is marked non-null but is null");
		}
		if (ArrayUtils.contains(this.getMonsterIds(), npc.getId())) {
			return true;
		}
		return name.equalsIgnoreCase(this.name);
	}

	@Override
	public float getExperience(final NPC npc) {
		return xp;
	}

	@Override
	public int getTaskId() {
		return 98;
	}

	public int[] getMonsterIds() {
		return new int[0];
	}

	@Override
	public int getSlayerRequirement() {
		return 0;
	}

	@Override
	public String getTip() {
		return "Not available.";
	}

	@Override
	public String toString() {
		return StringFormatUtil.formatString(name);
	}

	@Override
	public String getTaskName() {
		return name;
	}

	@Override
	public String getEnumName() {
		return name();
	}

	public float getXp() {
		return xp;
	}

	public Predicate<Player> getPredicate() {
		return predicate;
	}

	public String[] getMonsters() {
		return monsters;
	}

	public boolean isWildernessTask() {
		return wildernessTask;
	}

}
