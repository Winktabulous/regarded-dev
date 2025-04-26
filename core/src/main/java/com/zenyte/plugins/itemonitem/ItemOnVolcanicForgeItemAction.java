package com.zenyte.plugins.itemonitem;

import com.zenyte.game.content.skills.smithing.VolcanicForge;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 30 aug. 2018 | 18:57:15
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class ItemOnVolcanicForgeItemAction implements ItemOnObjectAction {

    private static final ForceTalk GOBLIN_SHOUT = new ForceTalk("My Precious!!! NOOOOO!!!");

    private static final Animation GOBLIN_ANIM = new Animation(6184);

    private static final Animation PLAYER_ANIM = new Animation(734, 5);

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
        final int ward = item.getName().contains("shard") && item.getName().contains("Odium") ? 11926 : item.getName().contains("shard") && item.getName().contains("Malediction") ? 11924 : -1;
        if (ward != -1) {
            for (final Integer id : VolcanicForge.WARDS.get(ward)) {
                if (!player.getInventory().containsItem(id, 1)) {
                    player.sendMessage("You don't have all the shards with you to assemble the ward.");
                    return;
                }
            }
            player.lock();
            player.getDialogueManager().start(new PlainChat(player, "You drop the three shield shards into the mouth of the volcanic<br><br> chamber of fire.", false));
            WorldTasksManager.schedule(() -> {
                player.getActionManager().setAction(new VolcanicForge(ward));
            }, 4);
            return;
        }
        if (item.getId() == 1635) {
            player.lock();
            WorldTasksManager.schedule(new WorldTask() {

                int ticks;

                NPC goblin;

                @Override
                public void run() {
                    switch(ticks++) {
                        case 0:
                            player.addWalkSteps(player.getX(), player.getY() - 1);
                            break;
                        case 2:
                            goblin = new NPC(3028, new Location(player.getX() - 1, player.getY(), player.getPlane()), true);
                            goblin.spawn();
                            goblin.lock();
                            goblin.faceEntity(player);
                            goblin.setForceTalk(GOBLIN_SHOUT);
                            break;
                        case 3:
                            goblin.setAnimation(GOBLIN_ANIM);
                            break;
                        case 4:
                            player.unlock();
                            goblin.finish();
                            stop();
                            break;
                    }
                }
            }, 0, 1);
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1635, 11928, 11929, 11930, 11931, 11932, 11933 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.VOLCANIC_FORGE };
    }
}
