package com.zenyte.plugins.object;

import com.zenyte.game.content.minigame.motherlode.UpperMotherlodeArea;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class MotherlodeUpperLadder implements ObjectAction {

    private static final Location TOP = new Location(3755, 5675, 0);

    private static final Location BOTTOM = new Location(3755, 5672, 0);

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(UpperMotherlodeArea.polygon.contains(player.getLocation()) ? TOP : BOTTOM), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final boolean top = UpperMotherlodeArea.polygon.contains(player.getLocation());
        if (top || (player.getBooleanAttribute("motherlode_upstairs") && player.getSkills().getLevelForXp(SkillConstants.MINING) >= 72)) {
            player.lock();
            WorldTasksManager.schedule(() -> {
                player.setAnimation(top ? Animation.LADDER_DOWN : Animation.LADDER_UP);
                WorldTasksManager.schedule(() -> {
                    player.setLocation(top ? BOTTOM : TOP);
                    player.unlock();
                });
            });
        } else
            player.getDialogueManager().start(new NPCChat(player, 6562, "Ye wants to go up there, eh? Well, ye\'ll need at least 72 Mining first. An\' don\'t think you can fool me with yer potions and fancy stat-boosts. Get yer level up for real. Ye also need to buy upstairs access from me."));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 19044, 19045, ObjectId.LADDER_19047, ObjectId.LADDER_19049 };
    }
}
