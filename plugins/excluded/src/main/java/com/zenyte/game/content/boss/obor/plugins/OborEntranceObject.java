package com.zenyte.game.content.boss.obor.plugins;

import com.zenyte.game.content.boss.obor.OborInstance;
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
 * @author Tommeh | 13/05/2019 | 22:13
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class OborEntranceObject implements ObjectAction {

    private static final Logger log = LoggerFactory.getLogger(OborEntranceObject.class);

    private static final Item GIANT_KEY = new Item(20754);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.getInventory().containsItem(GIANT_KEY)) {
            player.sendMessage("The gate is locked shut.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("Enter Obor\'s Lair?", "Yes.", "No.").onOptionOne(() -> setKey(5));
                options(5, "<col=d80000>You will lose all of your items dropped if you die!</col>", "I know I\'m risking everything I have.", "I need to prepare some more.").onOptionOne(() -> {
                    player.getInventory().deleteItem(GIANT_KEY);
                    player.lock();
                    new FadeScreenAction(player, 2, () -> {
                        try {
                            final AllocatedArea area = MapBuilder.findEmptyChunk(8, 8);
                            final OborInstance instance = new OborInstance(player, area);
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
        return new Object[] { ObjectId.GATE_29486, ObjectId.GATE_29487 };
    }
}
