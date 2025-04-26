package com.zenyte.game.content.skills.magic.spells.lunar;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.content.multicannon.DwarfMultiCannon;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.MagicSpell;
import com.zenyte.game.content.skills.magic.spells.NPCSpell;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.npc.combatdefs.StatDefinitions;
import com.zenyte.game.world.entity.npc.combatdefs.StatType;
import com.zenyte.game.world.entity.npc.race.Demon;
import com.zenyte.game.world.entity.pathfinding.events.player.CombatEntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.PredictedEntityStrategy;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.plugins.events.PlayerResetEvent;
import com.zenyte.plugins.events.SpellbookChangeEvent;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 16. veebr 2018 : 15:20.23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class MonsterExamine extends Interface implements NPCSpell {
	private static final Graphics PGFX = new Graphics(1059, 0, 32);
	private static final Graphics NGFX = new Graphics(736);
	private static final Animation ANIM = new Animation(6293);
	private static final SoundEffect SOUND = new SoundEffect(3620);

	@Subscribe
	public static final void onPlayerReset(final PlayerResetEvent event) {
		if (event.getPlayer().getInterfaceHandler().isPresent(GameInterface.MONSTER_EXAMINE)) {
			GameInterface.SPELLBOOK.open(event.getPlayer());
		}
	}

	@Subscribe
	public static final void onSpellbookChange(final SpellbookChangeEvent event) {
		if (event.getPlayer().getInterfaceHandler().isPresent(GameInterface.MONSTER_EXAMINE)) {
			GameInterface.SPELLBOOK.open(event.getPlayer());
		}
	}

	@Override
	public int getDelay() {
		return 5000;
	}

	@Override
	public boolean spellEffect(final Player player, final NPC npc) {
		if (npc == null || npc.isDead() || npc.isFinished()) {
			return false;
		}
		player.getActionManager().setAction(new MonsterExamineChaseAction(this, npc));
		return false;
	}


	private static final class MonsterExamineChaseAction extends Action {
		private MonsterExamineChaseAction(@NotNull final MagicSpell spell, @NotNull final NPC target) {
			this.spell = spell;
			this.target = target;
		}

		private final MagicSpell spell;
		private final NPC target;

		@Override
		public boolean start() {
			player.setCombatEvent(new CombatEntityEvent(player, new PredictedEntityStrategy(target)));
			player.setLastTarget(target);
			player.setFaceEntity(target);
			return pathfind();
		}

		boolean pathfind() {
			final int maxDistance = 10;
			player.resetWalkSteps();
			if (player.isProjectileClipped(target, false) || !withinRange(target, target.getSize()) || (target.hasWalkSteps() && !withinRange(target.getNextPosition(target.isRun() ? 2 : 1), target.getSize()))) {
				appendWalksteps();
				if (player.getWalkSteps().size() >= 2) {
					final int size = target.getSize();
					final Location nextPos = player.getNextPosition(1);
					final int distanceX = nextPos.getX() - target.getNextPosition(1).getX();
					final int distanceY = nextPos.getY() - target.getNextPosition(1).getY();
					if (!(distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) && !ProjectileUtils.isProjectileClipped(player, target, nextPos, target, false)) {
						player.getWalkSteps().resetAllButFirst();
					}
				}
			}
			return true;
		}

		final void appendWalksteps() {
			player.getCombatEvent().process();
		}

		final boolean withinRange(final Position targetPosition, final int targetSize) {
			final Location target = targetPosition.getPosition();
			final int distanceX = player.getX() - target.getX();
			final int distanceY = player.getY() - target.getY();
			final int npcSize = player.getSize();
			if (distanceX == -npcSize - 10 && distanceY == -npcSize - 10 || distanceX == targetSize + 10 && distanceY == targetSize + 10 || distanceX == -npcSize - 10 && distanceY == targetSize + 10 || distanceX == targetSize + 10 && distanceY == -npcSize - 10) {
				return false;
			}
			return !(distanceX > targetSize + 10 || distanceY > targetSize + 10 || distanceX < -npcSize - 10 || distanceY < -npcSize - 10);
		}

		@Override
		public boolean process() {
			return pathfind();
		}

		protected boolean isWithinAttackDistance() {
			final boolean checkProjectile = target.checkProjectileClip(player, false);
			if (checkProjectile && ProjectileUtils.isProjectileClipped(player, target, player.getNextPosition(player.isRun() ? 2 : 1), target, false)) {
				return false;
			}
			int maxDistance = 10;
			final Location nextLocation = target.getNextPosition(target.isRun() ? 2 : 1);
			if ((player.isFrozen() || player.isStunned()) && (CollisionUtil.collides(player.getX(), player.getY(), player.getSize(), nextLocation.getX(), nextLocation.getY(), target.getSize()) || !withinRange(target, maxDistance, target.getSize()))) {
				return false;
			}
			final int distanceX = player.getX() - target.getX();
			final int distanceY = player.getY() - target.getY();
			final int size = target.getSize();
			return distanceX <= size + maxDistance && distanceX >= -1 - maxDistance && distanceY <= size + maxDistance && distanceY >= -1 - maxDistance;
		}

		final boolean withinRange(final Position targetPosition, final int maximumDistance, final int targetSize) {
			final Location target = targetPosition.getPosition();
			final int distanceX = player.getX() - target.getX();
			final int distanceY = player.getY() - target.getY();
			final int npcSize = player.getSize();
			if (distanceX == -npcSize - maximumDistance && distanceY == -npcSize - maximumDistance || distanceX == targetSize + maximumDistance && distanceY == targetSize + maximumDistance || distanceX == -npcSize - maximumDistance && distanceY == targetSize + maximumDistance || distanceX == targetSize + maximumDistance && distanceY == -npcSize - maximumDistance) {
				return false;
			}
			return !(distanceX > targetSize + maximumDistance || distanceY > targetSize + maximumDistance || distanceX < -npcSize - maximumDistance || distanceY < -npcSize - maximumDistance);
		}

		@Override
		public int processWithDelay() {
			if (!isWithinAttackDistance()) {
				return 0;
			}
			if (!target.isAttackable()) {
				player.sendMessage("You can't find out their stats.");
				return -1;
			}
			final SpellState state = new SpellState(player, spell.getLevel(), spell.getRunes());
			if (!state.check()) {
				return -1;
			}
			player.resetWalkSteps();
			state.remove();
			spell.addXp(player, 61);
			player.setFaceEntity(target);
			player.sendSound(SOUND);
			player.setGraphics(PGFX);
			player.setAnimation(ANIM);
			target.setGraphics(NGFX);
			target.performDefenceAnimation(player);
			player.getTemporaryAttributes().put("monster examine npc", target);
			if (target.getName(player).contains("troll")) {
				player.getAchievementDiaries().update(KourendDiary.CAST_MONSTER_EXAMINE);
			}
			GameInterface.MONSTER_EXAMINE.open(player);
			return -1;
		}

		@Override
		public boolean interruptedByCombat() {
			return false;
		}

		@Override
		public boolean interruptedByDialogue() {
			return false;
		}

		@Override
		public void stop() {
		}
	}

	private static final String getStats(final NPC npc) {
		final NPCCombatDefinitions combat = npc.getCombatDefinitions();
		final StatDefinitions statDefs = combat.getStatDefinitions();
		return "<col=c6a901>Stats</col><br>" + (npc.getCombatLevel() <= 0 ? "" : ("Combat level : " + npc.getCombatLevel() + "<br>")) + "Hitpoints : " + npc.getHitpoints() + "<br>Attack : " + statDefs.get(StatType.ATTACK) + "<br>Defence : " + statDefs.get(StatType.DEFENCE) + "<br>Strength : " + statDefs.get(StatType.STRENGTH) + "<br>Magic : " + statDefs.get(StatType.MAGIC) + "<br>Ranged : " + statDefs.get(StatType.RANGED) + (combat.getMaxHit() <= 0 ? "" : ("<br>Max standard hit : " + combat.getMaxHit())) + "<br>Main attack style: " + StringFormatUtil.formatString(combat.getAttackStyle().toString()) + "<br>";
	}

	private static final String getAggressiveStats(final NPC npc) {
		final NPCCombatDefinitions combat = npc.getCombatDefinitions();
		final StatDefinitions statDefs = combat.getStatDefinitions();
		return "<col=c6a901>Aggressive Stats</col><br>Attack speed : " + combat.getAttackSpeed() + "<br>Stab bonus : " + statDefs.get(StatType.ATTACK_STAB) + "<br>Slash bonus : " + statDefs.get(StatType.ATTACK_SLASH) + "<br>Crush bonus : " + statDefs.get(StatType.ATTACK_CRUSH) + "<br>Magic bonus : " + statDefs.get(StatType.ATTACK_MAGIC) + "<br>Ranged bonus : " + statDefs.get(StatType.ATTACK_RANGED) + "<br>Strength bonus : " + statDefs.get(StatType.MELEE_STRENGTH_BONUS) + "<br>Ranged strength bonus : " + statDefs.get(StatType.RANGED_STRENGTH_BONUS) + "<br>Attack bonus : " + statDefs.get(combat.getAttackStatType()) + "<br>";
	}

	private static final String getDefensiveStats(final NPC npc) {
		final NPCCombatDefinitions combat = npc.getCombatDefinitions();
		final StatDefinitions statDefs = combat.getStatDefinitions();
		return "<col=c6a901>Defensive Stats</col><br>Stab bonus : " + statDefs.get(StatType.DEFENCE_STAB) + "<br>Slash bonus : " + statDefs.get(StatType.DEFENCE_SLASH) + "<br>Crush bonus : " + statDefs.get(StatType.DEFENCE_CRUSH) + "<br>Magic bonus : " + statDefs.get(StatType.DEFENCE_MAGIC) + "<br>Ranged bonus : " + statDefs.get(StatType.DEFENCE_RANGED) + "<br>";
	}

	private static final String getOtherAttributes(final NPC npc) {
		final NPCCombatDefinitions combat = npc.getCombatDefinitions();
		final StringBuilder attributes = new StringBuilder();
		attributes.append("<col=c6a901>Other Attributes</col><br>");
		final String name = npc.getDefinitions().getName().toLowerCase();
		final String task = RegularTask.isAssignable(name);
		if (task != null) {
			attributes.append("Can be killed for ").append(task).append(" assignment.<br>");
		}
		if (combat.isPoisonImmune()) {
			attributes.append("Is poison immune.<br>");
		}
		if (combat.isVenomImmune()) {
			attributes.append("Is venom immune.<br>");
		}
		if (DwarfMultiCannon.canSetupCannon(npc.getLocation())) {
			attributes.append("Can be attacked using a dwarf multicannon.<br>");
		}
		if (Demon.isDemon(npc, false)) {
			attributes.append("Is vulnerable to demonbane weapons.<br>");
		}
		if (CombatUtilities.isDraconic(npc)) {
			attributes.append("Is vulnerable to dragonbane weapons.<br>");
		}
		return attributes.toString();
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

	@Override
	protected void attach() {
		put(17, "Close");
	}

	@Override
	public void open(Player player) {
		final Object attr = player.getTemporaryAttributes().get("monster examine npc");
		if (!(attr instanceof NPC)) {
			throw new RuntimeException("Opening monster examine illegally.");
		}
		final NPC npc = (NPC) attr;
		final PacketDispatcher dispatcher = player.getPacketDispatcher();
		player.getInterfaceHandler().sendInterface(getInterface());
		dispatcher.sendClientScript(1179, getStats(npc) + "|" + getAggressiveStats(npc) + "|" + getDefensiveStats(npc) + "|" + getOtherAttributes(npc) + "|" + npc.getDefinitions().getName());
	}

	@Override
	protected void build() {
		bind("Close", player -> {
			GameInterface.SPELLBOOK.open(player);
			player.setFaceEntity(null);
		});
	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.MONSTER_EXAMINE;
	}
}
