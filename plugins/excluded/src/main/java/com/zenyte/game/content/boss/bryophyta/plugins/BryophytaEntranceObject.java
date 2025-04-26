package com.zenyte.game.content.boss.bryophyta.plugins;

import com.zenyte.game.content.boss.bryophyta.BryophytaInstance;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.actions.FadeScreenAction;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tommeh | 17/05/2019 | 15:03
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class BryophytaEntranceObject implements ObjectAction {

    private static final Logger log = LoggerFactory.getLogger(BryophytaEntranceObject.class);

    private static final Item MOSSY_KEY = new Item(22374);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.getInventory().containsItem(MOSSY_KEY)) {
            player.sendMessage("The gate is locked shut.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                plain("Warning! You\'re about to enter an instanced area, anything left on<br><br>the ground when you leave will be lost. Would you like to continue?");
                options("Are you sure you wish to open it?", "Yes, let\'s go!", "I don\'t think I\'m quite ready yet.").onOptionOne(() -> {
                    player.getInventory().deleteItem(MOSSY_KEY);
                    player.lock();
                    new FadeScreenAction(player, 2, () -> {
                        try {
                            final AllocatedArea area = MapBuilder.findEmptyChunk(8, 8);
                            final BryophytaInstance instance = new BryophytaInstance(player, area);
                            instance.constructRegion();
                        } catch (OutOfSpaceException e) {
                            log.error("", e);
                        }
                    }).run();
                });
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GATE_32534 };
    }
}
