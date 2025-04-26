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

/**
 * @author Kris | 5. aug 2018 : 23:40:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum BossTask implements SlayerTask {
	KREE_ARRA(357, p -> p.getSkills().getLevelForXp(SkillConstants.RANGED) >= 70, "Kree'arra", false),
	KRIL_TSUTSAROTH(350.5F, p -> p.getSkills().getLevelForXp(SkillConstants.HITPOINTS) >= 70, "K'ril Tsutsaroth", false),
	COMMANDER_ZILYANA(350, p -> p.getSkills().getLevelForXp(SkillConstants.AGILITY) >= 70, "Commander Zilyana", false),
	GENERAL_GRAARDOR(338, p -> p.getSkills().getLevelForXp(SkillConstants.STRENGTH) >= 70, "General Graardor", false),
	DAGANNOTH_KINGS(331.5F, p -> true, "Dagannoth Kings", false) {
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

	/*DAGANNOTH_SUPREME(255, p -> true, "Dagannoth Supreme", false),
	DAGANNOTH_REX(331.5F, p -> true, "Dagannoth Rex", false),
	DAGANNOTH_PRIME(331.5F, p -> true, "Dagannoth Prime", false),*/
	KING_BLACK_DRAGON(258, p -> true, "King Black Dragon", false),
	VORKATH(750, p -> true, "Vorkath", false),
	VETION(312, p -> true, "Vet'ion reborn", true),
	CRAZY_ARCHEAOLOGIST(275, p -> true, "Crazy archaeologist", true),
	ZULRAH(500, p -> true, "Zulrah", false),
	CERBERUS(690, p -> p.getSkills().getLevelForXp(SkillConstants.SLAYER) >= 91, "Cerberus", false),
	GIANT_MOLE(215, p -> true, "Giant Mole", false), CALLISTO(312, p -> true, "Callisto", true),
	CHAOS_ELEMENTAL(250, p -> true, "Chaos elemental", true),
	SCORPIA(260, p -> true, "Scorpia", true),
	KRAKEN(255, p -> p.getSkills().getLevelForXp(SkillConstants.SLAYER) >= 87, "Kraken", false),
	ABYSSAL_SIRE(450, p -> p.getSkills().getLevelForXp(SkillConstants.SLAYER) >= 85, "Abyssal sire", false),
	KALPHITE_QUEEN(535.5F, p -> true, "Kalphite Queen", false),
	PHANTOM_MUSPAH(1763.7F, p -> true, "Phantom Muspah", false),
	VENENATIS(388.8F, p -> true, "Venenatis", true),
	CHAOS_FANATIC(253, p -> true, "Chaos Fanatic", true),
	THERMONUCLEAR_SMOKE_DEVIL(240, p -> p.getSkills().getLevelForXp(SkillConstants.SLAYER) >= 93, "Thermonuclear smoke devil", false),
    GROTESQUE_GUARDIANS(1350, p -> p.getSkills().getLevelForXp(SkillConstants.SLAYER) >= 75 && p.getNumericAttribute("brittle-entrance_unlocked").intValue() == 1, "Grotesque Guardians", false) {
		@Override
		public boolean validate(@NotNull final String name, final NPC npc) {
			if (name == null) {
				throw new NullPointerException("name is marked non-null but is null");
			}
			return name.equals("Dusk");
		}
	},
	BARROWS(255, p -> true, "Barrows Brothers", false) {
		@Override
		public boolean validate(@NotNull final String name, final NPC npc) {
			if (name == null) {
				throw new NullPointerException("name is marked non-null but is null");
			}
			return name.equals("Ahrim the Blighted") || name.equals("Dharok the Wretched") || name.equals("Guthan the Infested") || name.equals("Karil the Tainted") || name.equals("Torag the Corrupted") || name.equals("Verac the Defiled");
		}
	},

	//The below monsters can never be acquired as a boss task, their only purpose is for experience amplification when slaying(e.g. Skotizo as a demons task giving more experience).
	SKOTIZO(618.5F, p -> false, "Skotizo", false),
	TZKAL_ZUK(25_000F, p -> false, "TzKal-Zuk", false),
	TZTOK_JAD(10_000F, p -> false, "TzTok-Jad", false),

	ARAXXOR(1708, p -> p.getSkills().getLevelForXp(SkillConstants.SLAYER) >= 92, "Araxxor", false),

	;

	BossTask(final float xp, final Predicate<Player> predicate, final String name, final boolean assignableByKrystilia) {
		this.xp = xp;
		this.predicate = predicate;
		monsters = new String[] {name.toLowerCase()};
		this.name = name;
		this.assignableByKrystilia = assignableByKrystilia;
	}

	private final float xp;
	private final Predicate<Player> predicate;
	private final String name;
	private final String[] monsters;
	private final boolean assignableByKrystilia;
	public static final BossTask[] VALUES = values();
	public static final Map<String, BossTask> MAPPED_VALUES = new HashMap<String, BossTask>(VALUES.length);

	static {
		for (final BossTask value : VALUES) {
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

	public boolean isAssignableByKrystilia() {
		return assignableByKrystilia;
	}
}
