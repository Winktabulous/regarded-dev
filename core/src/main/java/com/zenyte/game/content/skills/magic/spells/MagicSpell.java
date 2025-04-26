package com.zenyte.game.content.skills.magic.spells;

import com.google.common.base.CaseFormat;
import com.zenyte.game.content.minigame.duelarena.Duel;
import com.zenyte.game.content.minigame.duelarena.DuelSetting;
import com.zenyte.game.content.skills.magic.SpellDefinitions;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.plugins.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 29. dets 2017 : 3:36.34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface MagicSpell extends Plugin {
	int EARTH_RUNE = 557;
	int ASTRAL_RUNE = 9075;
	int AIR_RUNE = 556;
	int WATER_RUNE = 555;
	int FIRE_RUNE = 554;
	int MIND_RUNE = 558;
	int NATURE_RUNE = 561;
	int CHAOS_RUNE = 562;
	int DEATH_RUNE = 560;
	int BLOOD_RUNE = 565;
	int SOUL_RUNE = 566;
	int COSMIC_RUNE = 564;
	int BODY_RUNE = 559;
	int LAW_RUNE = 563;

	int getDelay();

	default boolean hasDefenceRequirement(@NotNull final Player player) {
		if (player.getSkills().getLevelForXp(SkillConstants.DEFENCE) < 40) {
			player.sendMessage("You need a Defence level of at least 40 to cast lunar spells.");
			return false;
		}
		return true;
	}

	default int getLevel() {
		final SpellDefinitions definitions = SpellDefinitions.SPELLS.get(getSpellName());
		if (definitions == null) {
			return 0;
		}
		return definitions.getLevel();
	}

	default Item[] getRunes() {
		final SpellDefinitions definitions = SpellDefinitions.SPELLS.get(getSpellName());
		if (definitions == null) {
			return new Item[0];
		}
		return definitions.getRunes();
	}

	default boolean canCast(final Player player) {
		return getSpellbook() == player.getCombatDefinitions().getSpellbook() || getSpellName().contains("zenyte");//for home tps on normal/ancient since they have the same name
	}

	default boolean canUse(final Player player) {
		final Duel duel = player.getDuel();
		if (duel == null) {
			return true;
		}
		if (duel.hasRule(DuelSetting.NO_MAGIC)) {
			player.sendMessage("You cannot use magic in a no-magic duel.");
			return false;
		}
		return true;
	}

	default void addXp(final Player player, final double xp) {
		addXp(player, SkillConstants.MAGIC, xp);
	}

	default void addXp(final Player player, final int skill, final double xp) {
		final VarManager varManager = player.getVarManager();
		if (varManager.getBitValue(VarCollection.FOUNTAIN_OF_RUNE.getId()) == 1 && !player.getPrivilege().eligibleTo(PlayerPrivilege.DEVELOPER)) {
			return;
		}
		player.getSkills().addXp(skill, xp);
	}

	default String getSpellName() {
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, getClass().getSimpleName()).replaceAll("_", " ");
	}

	Spellbook getSpellbook();
}
