package com.zenyte.game.content.hiscores;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

import java.util.function.Function;

public enum HiscoresCategoryEntry {
	//Skills
	OVERALL(player -> (long) player.getSkills().getTotalLevel(), player -> player.getSkills().getTotalXpLong()),
	ATTACK(player -> (long) player.getSkills().getLevelForXp(SkillConstants.ATTACK), player -> (long) player.getSkills().getExperience(SkillConstants.ATTACK)),
	STRENGTH(player -> (long) player.getSkills().getLevelForXp(SkillConstants.STRENGTH), player -> (long) player.getSkills().getExperience(SkillConstants.STRENGTH)),
	DEFENCE(player -> (long) player.getSkills().getLevelForXp(SkillConstants.DEFENCE), player -> (long) player.getSkills().getExperience(SkillConstants.DEFENCE)),
	HITPOINTS(player -> (long) player.getSkills().getLevelForXp(SkillConstants.HITPOINTS), player -> (long) player.getSkills().getExperience(SkillConstants.HITPOINTS)),
	RANGED(player -> (long) player.getSkills().getLevelForXp(SkillConstants.RANGED), player -> (long) player.getSkills().getExperience(SkillConstants.RANGED)),
	MAGIC(player -> (long) player.getSkills().getLevelForXp(SkillConstants.MAGIC), player -> (long) player.getSkills().getExperience(SkillConstants.MAGIC)),
	PRAYER(player -> (long) player.getSkills().getLevelForXp(SkillConstants.PRAYER), player -> (long) player.getSkills().getExperience(SkillConstants.PRAYER)),
	RUNECRAFTING(player -> (long) player.getSkills().getLevelForXp(SkillConstants.RUNECRAFTING), player -> (long) player.getSkills().getExperience(SkillConstants.RUNECRAFTING)),
	AGILITY(player -> (long) player.getSkills().getLevelForXp(SkillConstants.AGILITY), player -> (long) player.getSkills().getExperience(SkillConstants.AGILITY)),
	HERBLORE(player -> (long) player.getSkills().getLevelForXp(SkillConstants.HERBLORE), player -> (long) player.getSkills().getExperience(SkillConstants.HERBLORE)),
	THIEVING(player -> (long) player.getSkills().getLevelForXp(SkillConstants.THIEVING), player -> (long) player.getSkills().getExperience(SkillConstants.THIEVING)),
	CRAFTING(player -> (long) player.getSkills().getLevelForXp(SkillConstants.CRAFTING), player -> (long) player.getSkills().getExperience(SkillConstants.CRAFTING)),
	FLETCHING(player -> (long) player.getSkills().getLevelForXp(SkillConstants.FLETCHING), player -> (long) player.getSkills().getExperience(SkillConstants.FLETCHING)),
	SLAYER(player -> (long) player.getSkills().getLevelForXp(SkillConstants.SLAYER), player -> (long) player.getSkills().getExperience(SkillConstants.SLAYER)),
	MINING(player -> (long) player.getSkills().getLevelForXp(SkillConstants.MINING), player -> (long) player.getSkills().getExperience(SkillConstants.MINING)),
	SMITHING(player -> (long) player.getSkills().getLevelForXp(SkillConstants.SMITHING), player -> (long) player.getSkills().getExperience(SkillConstants.SMITHING)),
	FISHING(player -> (long) player.getSkills().getLevelForXp(SkillConstants.FISHING), player -> (long) player.getSkills().getExperience(SkillConstants.FISHING)),
	COOKING(player -> (long) player.getSkills().getLevelForXp(SkillConstants.COOKING), player -> (long) player.getSkills().getExperience(SkillConstants.COOKING)),
	FIREMAKING(player -> (long) player.getSkills().getLevelForXp(SkillConstants.FIREMAKING), player -> (long) player.getSkills().getExperience(SkillConstants.FIREMAKING)),
	WOODCUTTING(player -> (long) player.getSkills().getLevelForXp(SkillConstants.WOODCUTTING), player -> (long) player.getSkills().getExperience(SkillConstants.WOODCUTTING)),
	FARMING(player -> (long) player.getSkills().getLevelForXp(SkillConstants.FARMING), player -> (long) player.getSkills().getExperience(SkillConstants.FARMING)),
	HUNTER(player -> (long) player.getSkills().getLevelForXp(SkillConstants.HUNTER), player -> (long) player.getSkills().getExperience(SkillConstants.HUNTER)),
	//CONSTRUCTION(player -> Long.valueOf(player.getSkills().getLevelForXp(SkillConstants.CONSTRUCTION)), player -> (long) player.getSkills().getExperience(SkillConstants.CONSTRUCTION)),
	//Bosses
	ABYSSAL_SIRE(player -> (long) player.getNotificationSettings().getKillcount("Abyssal Sire")),
	ALCHEMICAL_HYDRA(player -> (long) player.getNotificationSettings().getKillcount("Alchemical Hydra")),
	ANGEL_OF_DEATH(player -> (long) player.getNotificationSettings().getKillcount("Angel of Death")),
	BARROWS(player -> (long) player.getNotificationSettings().getKillcount("Barrows")),
	BRYOPHYTA(player -> (long) player.getNotificationSettings().getKillcount("Bryophyta")),
	CALLISTO(player -> (long) player.getNotificationSettings().getKillcount("Callisto")),
	CERBERUS(player -> (long) player.getNotificationSettings().getKillcount("Cerberus")),
	CHAOS_ELEMENTAL(player -> (long) player.getNotificationSettings().getKillcount("Chaos Elemental")),
	CHAOS_FANATIC(player -> (long) player.getNotificationSettings().getKillcount("Chaos Fanatic")),
	COMMANDER_ZILYANA(player -> (long) player.getNotificationSettings().getKillcount("Commander Zilyana")),
	CORPOREAL_BEAST(player -> (long) player.getNotificationSettings().getKillcount("Corporeal Beast")),
	CRAZY_ARCHAEOLOGIST(player -> (long) player.getNotificationSettings().getKillcount("Crazy Archaeologist")),
	DAGANNOTH_PRIME(player -> (long) player.getNotificationSettings().getKillcount("Dagannoth Prime")),
	DAGANNOTH_REX(player -> (long) player.getNotificationSettings().getKillcount("Dagannoth Rex")),
	DAGANNOTH_SUPREME(player -> (long) player.getNotificationSettings().getKillcount("Dagannoth Supreme")),
	GANODERMIC_BEAST(player -> (long) player.getNotificationSettings().getKillcount("Ganodermic Beast")),
	GENERAL_GRAARDOR(player -> (long) player.getNotificationSettings().getKillcount("General Graardor")),
	GIANT_MOLE(player -> (long) player.getNotificationSettings().getKillcount("Giant Mole")),
	GROTESQUE_GUARDIANS(player -> (long) player.getNotificationSettings().getKillcount("Grotesque Guardians")),
	HESPORI(player -> (long) player.getNotificationSettings().getKillcount("Hespori")),
	KRIL_TSUTSAROTH(player -> (long) player.getNotificationSettings().getKillcount("K'Ril Tsutsaroth")),
	KING_BLACK_DRAGON(player -> (long) player.getNotificationSettings().getKillcount("King Black Dragon")),
	KRAKEN(player -> (long) player.getNotificationSettings().getKillcount("Kraken")),
	KREEARRA(player -> (long) player.getNotificationSettings().getKillcount("Kree'Arra")),
	MIMIC(player -> (long) player.getNotificationSettings().getKillcount("Mimic")),
	NEX(player -> (long) player.getNotificationSettings().getKillcount("Nex")),
	NIGHTMARE(player -> (long) player.getNotificationSettings().getKillcount("The Nightmare")),
	PHOSANIS_NIGHTMARE(player -> (long) player.getNotificationSettings().getKillcount("Phosani's Nightmare")),
	OBOR(player -> (long) player.getNotificationSettings().getKillcount("Obor")),
	RISE_OF_THE_SIX(player -> (long) player.getNotificationSettings().getKillcount("Rise of the Six")),
	SARACHNIS(player -> (long) player.getNotificationSettings().getKillcount("Sarachnis")),
	SCORPIA(player -> (long) player.getNotificationSettings().getKillcount("Scorpia")),
	SKOTIZO(player -> (long) player.getNotificationSettings().getKillcount("Skotizo")),
	THE_GAUNTLET(player -> player.getNumericAttribute("gauntlet_completions").longValue()),
	THE_CORRUPTED_GAUNTLET(player -> player.getNumericAttribute("corrupted_gauntlet_completions").longValue()),
	THERMONUCLEAR_SMOKE_DEVIL(player -> (long) player.getNotificationSettings().getKillcount("Thermonuclear Smoke Devil")),
	TZKAL_ZUK(player -> (long) player.getNotificationSettings().getKillcount("Tzkal-zuk")),
	TZTOK_JAD(player -> (long) player.getNotificationSettings().getKillcount("Tztok-jad")),
	VANSTROM_KLAUSE(player -> (long) player.getNotificationSettings().getKillcount("Vanstrom Klause")),
	VENENATIS(player -> (long) player.getNotificationSettings().getKillcount("Venenatis")),
	VETION(player -> (long) player.getNotificationSettings().getKillcount("Vet'Ion")),
	VORKATH(player -> (long) player.getNotificationSettings().getKillcount("Vorkath")),
	ZALCANO(player -> (long) player.getNotificationSettings().getKillcount("Zalcano")),
	WINTERTODT(player -> (long) player.getNotificationSettings().getKillcount("Wintertodt")),
	ZULRAH(player -> (long) player.getNotificationSettings().getKillcount("Zulrah")),
	//Raids
	CHAMBERS_OF_XERIC(player -> player.getNumericAttribute("chambersofxeric").longValue()),
	CHAMBERS_OF_XERIC_CM(player -> player.getNumericAttribute("challengechambersofxeric").longValue()),
	//THEATRE_OF_BLOOD(player -> {}),
	//PVP
	WILDERNESS(player -> player.getNumericAttribute("WildernessKills").longValue(), player -> player.getNumericAttribute("WildernessDeaths").longValue()),
	;

	private final Function<Player, Long>[] saveFunctions;

	@SafeVarargs
	HiscoresCategoryEntry(Function<Player, Long>... saveFunctions) {
		this.saveFunctions = saveFunctions;
	}

	public long[] values(Player player) {
		int length = saveFunctions.length;
		long[] values = new long[length];
		for (int i = 0; i < length; i++) {
			values[i] = saveFunctions[i].apply(player);
		}

		return values;
	}

}
