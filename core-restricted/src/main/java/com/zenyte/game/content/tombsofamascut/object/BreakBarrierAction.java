package com.zenyte.game.content.tombsofamascut.object;

import com.zenyte.game.content.skills.mining.MiningDefinitions;
import com.zenyte.game.content.tombsofamascut.encounter.HetEncounter;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Optional;

/**
 * @author Savions.
 */
public class BreakBarrierAction implements ObjectAction {

	private static final SoundEffect BREAKABLE_WALL_MINED_SOUND = new SoundEffect(6547);

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		player.getActionManager().setAction(new Action() {

			@Override public boolean start() {
				final Optional<MiningDefinitions.PickaxeDefinitions.PickaxeResult> axe = MiningDefinitions.PickaxeDefinitions.get(player, true);
				if (axe.isEmpty()) {
					player.getDialogueManager().start(new PlainChat(player, "You need a pickaxe to do that. You do not have a pickaxe which you have the Mining level to use."));
					return false;
				}
				player.setAnimation(axe.get().getDefinition().getAnim());
				player.sendMessage("You swing your pick at the barrier.");
				delay(2);
				return true;
			}

			@Override public boolean process() {
				return true;
			}

			@Override public int processWithDelay() {
				if (player.getArea() instanceof final HetEncounter encounter) {
					final WorldObject barrier = encounter.getObject(object.getLocation());
					if (barrier != null && (barrier.getId() == HetEncounter.BREAKABLE_WALL_1 - 1 || barrier.getId() == HetEncounter.BREAKABLE_WALL_2 - 1)) {
						player.setAnimation(Animation.STOP);
						player.sendMessage("The barrier crumbles apart.");
						World.removeObject(barrier);
						barrier.setId(HetEncounter.BREAKABLE_WALL_BROKEN_ID);
						player.sendSound(BREAKABLE_WALL_MINED_SOUND);
						player.getSkills().addXp(Skills.MINING, 25);
						World.spawnObject(barrier);
					}
				}
				return -1;
			}
		});
	}

	@Override public Object[] getObjects() {
		return new Object[] {HetEncounter.BREAKABLE_WALL_1 - 1, HetEncounter.BREAKABLE_WALL_2 - 1};
	}
}
