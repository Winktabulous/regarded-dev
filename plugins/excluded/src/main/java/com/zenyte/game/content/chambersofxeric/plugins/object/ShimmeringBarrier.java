package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.room.DeathlyRoom;
import com.zenyte.game.content.rots.RotsInstance;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import static com.zenyte.game.content.chambersofxeric.room.DeathlyRoom.keystoneCrystal;
import static com.zenyte.game.content.chambersofxeric.room.DeathlyRoom.placingKeystoneAnimation;

/**
 * @author Kris | 06/07/2019 04:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class ShimmeringBarrier implements ObjectAction, ItemOnObjectAction {

    private static final SoundEffect sound = new SoundEffect(1657, 10, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        dispel(player, object);
    }

    private void dispel(final Player player, final WorldObject object) {
        if (player.inArea("Rise of the Six")) {
            RotsInstance area = (RotsInstance) player.getArea();
            if (!area.isCompleted()) {
                player.sendMessage("I wonder if there is something that can dispel this?");
                return;
            }

            player.resetWalkSteps();
            if (player.getY() >= object.getY()) {
                player.addWalkSteps(player.getX(), object.getY() - 1, -1, false);
            } else {
                player.addWalkSteps(player.getX(), object.getY() + 1, -1, false);
            }
            player.lock(player.hasWalkSteps() ? 2 : 1);
            return;
        }

        player.getRaid().ifPresent(raid -> raid.ifInRoom(player, DeathlyRoom.class, room -> {
            if (!player.getInventory().containsItem(keystoneCrystal)) {
                player.sendMessage("You're going to need a magical keystone to dispel this barrier.");
                return;
            }
            player.setAnimation(placingKeystoneAnimation);
            World.sendSoundEffect(player.getLocation(), sound);
            player.sendMessage("Your keystone glows as it is absorbed into the barrier, which disperses.");
            player.getInventory().deleteItem(keystoneCrystal);
            World.removeObject(object);
            Raid.incrementPoints(player, 2000);

            boolean anyNpcsKilled = false;
            for (NPC npc : room.getNpcs()) {
                if (npc.isDead() || npc.isFinished()) {
                    anyNpcsKilled = true;
                    break;
                }
            }

            if (!anyNpcsKilled) {
                player.getCombatAchievements().complete(CAType.NO_TIME_FOR_DEATH);
            }
            room.getNpcs().forEach(NPC::sendDeath);
        }));
    }

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        dispel(player, object);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { keystoneCrystal.getId() };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SHIMMERING_BARRIER };
    }
}
