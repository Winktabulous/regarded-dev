package com.zenyte.game.content.skills.thieving;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 24/03/2019 21:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FloorTrapObject implements ObjectAction {

    private static final Animation animation = new Animation(2244);

    private static final SoundEffect sound = new SoundEffect(2387);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Search")) {
            player.lock(2);
            player.setAnimation(animation);
            player.sendSound(sound);
            player.getTemporaryAttributes().put("Wall safe disarmed", true);
            if (player.getSkills().getLevel(SkillConstants.THIEVING) >= player.getSkills().getLevelForXp(SkillConstants.THIEVING)) {
                player.drainSkill(SkillConstants.THIEVING, 1);
            }
            WorldTasksManager.schedule(() -> player.sendMessage("You temporarily disarm the trap!"), 1);
        }
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.FLOOR };
    }
}
