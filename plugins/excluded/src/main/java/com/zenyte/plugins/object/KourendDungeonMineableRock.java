package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.mining.MiningDefinitions;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCObjectEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Optional;

/**
 * @author Kris | 10/05/2019 16:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KourendDungeonMineableRock implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Mine")) {
            final Optional<MiningDefinitions.PickaxeDefinitions.PickaxeResult> axe = MiningDefinitions.PickaxeDefinitions.get(player, true);
            if (!axe.isPresent()) {
                player.getDialogueManager().start(new PlainChat(player, "You need a pickaxe to mine this rock. You do not have a pickaxe which you have the Mining level to use."));
                return;
            }
            player.setAnimation(axe.get().getDefinition().getAnim());
            WorldTasksManager.schedule(() -> {
                player.setAnimation(Animation.STOP);
                if (World.containsObjectWithId(object, object.getId())) {
                    World.removeObject(object);
                    WorldTasksManager.schedule(() -> {
                        CharacterLoop.forEach(object, 1, Entity.class, entity -> {
                            if (CollisionUtil.collides(object.getX(), object.getY(), 2, entity.getX(), entity.getY(), entity.getSize())) {
                                if (entity instanceof Player) {
                                    entity.setRouteEvent(new ObjectEvent(((Player) entity), new ObjectStrategy(object), null));
                                } else {
                                    entity.setRouteEvent(new NPCObjectEvent(((NPC) entity), new ObjectStrategy(object)));
                                }
                            }
                        });
                        World.spawnObject(object);
                    }, 100);
                }
            }, 3);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROCKS_28890 };
    }
}
