package com.zenyte.plugins.dialogue;

import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.object.SpiritTree;

/**
 * @author Tommeh | 15 dec. 2017 : 19:01:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class SpiritTreeMenuD extends OptionsMenuD {
	private static final Animation ANIMATION = new Animation(828, 15);

	public SpiritTreeMenuD(Player player) {
		super(player, "Spirit Tree Locations", SpiritTree.getAvailableOptions());
	}

	@Override
	public void handleClick(int slotId) {
		final SpiritTree tree = SpiritTree.getTree(player);
		final SpiritTree destinationTree = SpiritTree.get(slotId);
		final SpiritTree currentTree = SpiritTree.getTree(player);
		if (destinationTree == currentTree) {
			player.getDialogueManager().start(new NPCChat(player, 4982, "You're already here."));
			return;
		}
		final Location location = destinationTree.getLocation();
		player.lock(2);
		player.setAnimation(ANIMATION);
		player.getDialogueManager().start(new ItemChat(player, new Item(6063), "You place your hands on the dry though bark of the<br> spirit tree, and feel a surge of energy run through<br> your veins."));
		if (location == null) {
			return;
		}
		if (tree.equals(SpiritTree.GRAND_EXCHANGE)) {
			player.getAchievementDiaries().update(VarrockDiary.USE_SPIRIT_TREE);
		} else if (destinationTree.equals(SpiritTree.GNOME_STRONGHOLD)) {
			player.getAchievementDiaries().update(WesternProvincesDiary.TRAVEL_TO_GNOME_STRONGHOLD);
		}
		if (destinationTree.equals(SpiritTree.PORT_SARIM) || destinationTree.equals(SpiritTree.ETCETERIA) || destinationTree.equals(SpiritTree.BRIMHAVEN) || destinationTree.equals(SpiritTree.HOSIDIUS) || destinationTree.equals(SpiritTree.FARMING_GUILD)) {
			SherlockTask.TELEPORT_TO_PLANTED_SPIRIT_TREE.progress(player);
		}
		WorldTasksManager.schedule(() -> player.setLocation(location), 1);
	}

	@Override
	public boolean cancelOption() {
		return true;
	}
}
