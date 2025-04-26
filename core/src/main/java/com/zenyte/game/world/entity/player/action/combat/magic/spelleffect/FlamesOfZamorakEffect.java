package com.zenyte.game.world.entity.player.action.combat.magic.spelleffect;

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Entity.EntityType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.npc.combatdefs.StatType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Kris | 8. jaan 2018 : 1:09.45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class FlamesOfZamorakEffect implements SpellEffect {
	@Override
	public void spellEffect(final Entity player, final Entity target, final int damage) {
		if (target.getEntityType() == EntityType.PLAYER) {
			if (player instanceof Player) {
				final Player p = (Player) player;
				p.getAchievementDiaries().update(WildernessDiary.CAST_GOD_SPELL);
			}
			final Player p2 = (Player) target;
			if (p2.getSkills().getLevel(SkillConstants.MAGIC) < p2.getSkills().getLevelForXp(SkillConstants.MAGIC)) {
				return;
			}
		} else {
			final NPC npc = (NPC) target;
			final NPCCombatDefinitions defs = NPCCDLoader.get(npc.getId());
			if (defs == null) {
				return;
			}
			if (npc.getCombatDefinitions().getStatDefinitions().get(StatType.MAGIC) < defs.getStatDefinitions().get(StatType.MAGIC)) return;
		}
		target.drainSkill(SkillConstants.MAGIC, 5.0F);
	}
}
