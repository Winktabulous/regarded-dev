package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 29 mei 2018 | 19:17:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class MageBankGodStatueObject implements ObjectAction {

    private static final Animation PRAY_ANIM = new Animation(645);

    private static final Graphics POOF_GFX = new Graphics(188, 0, 60);

    private static final Item SARADOMIN_CAPE = new Item(2412);

    private static final Item GUTHIX_CAPE = new Item(2413);

    private static final Item ZAMORAK_CAPE = new Item(2414);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final String god = name.substring(10);
        final Item cape = object.getId() == 2873 ? SARADOMIN_CAPE : object.getId() == 2874 ? ZAMORAK_CAPE : GUTHIX_CAPE;
        player.lock(2);
        player.setAnimation(PRAY_ANIM);
        player.getDialogueManager().start(new PlainChat(player, "You kneel and chant to " + god + "...", false));
        WorldTasksManager.schedule(() -> {
            player.getDialogueManager().start(new PlainChat(player, "You kneel and chant to " + god + "...<br>You feel a rush of energy charge through your veins.<br>Suddenly a cape appears before you."));
            player.getPacketDispatcher().sendGraphics(POOF_GFX, object);
            World.spawnFloorItem(cape, object, player, 200, 0);
        }, 2);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STATUE_OF_SARADOMIN_2873, ObjectId.STATUE_OF_ZAMORAK_2874, ObjectId.STATUE_OF_GUTHIX };
    }
}
