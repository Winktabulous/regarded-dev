package com.zenyte.plugins.object;

import com.zenyte.game.content.boss.kraken.KrakenInstance;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.plugins.dialogue.PlainChat;
import mgi.utilities.StringFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author Tommeh | 23 mei 2018 | 01:06:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class KrakenBossEntrance implements ObjectAction {

    private static final Logger log = LoggerFactory.getLogger(KrakenBossEntrance.class);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == ObjectId.CREVICE_537) {
            if (optionId == 1) {
                player.setLocation(KrakenInstance.INSIDE_TILE);
            } else if (optionId == 2) {
                final Item price = new Item(995, 25000);
                if (!player.getBank().containsItem(price) && !player.getInventory().containsItem(price)) {
                    player.getDialogueManager().start(new PlainChat(player, "You don\'t have enough gold in both your inventory and bank to start this instance."));
                    return;
                }
                player.getDialogueManager().start(new Dialogue(player) {

                    @Override
                    public void buildDialogue() {
                        options("Enter a private Kraken boss cave?", "Pay " + StringFormatUtil.format(price.getAmount()) + " coins.", "Cancel.").onOptionOne(() -> {
                            try {
                                final AllocatedArea area = MapBuilder.findEmptyChunk(8, 8);
                                final KrakenInstance instance = new KrakenInstance(player, false, area, 282, 1250);
                                instance.constructRegion();
                                if (player.getInventory().containsItem(price)) {
                                    player.getInventory().deleteItem(price);
                                } else if (player.getBank().containsItem(price)) {
                                    player.getBank().remove(price);
                                }
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        });
                    }
                });
            } else if (optionId == 3) {
                player.sendMessage("You peek through the crevice...");
                WorldTasksManager.schedule(() -> {
                    final Set<Player> players = GlobalAreaManager.get("Kraken Boss Room").getPlayers();
                    final int playerCount = players.size();
                    player.sendMessage("Standard cave: " + (playerCount == 0 ? "No adventurers." : playerCount + (playerCount == 1 ? " adventurer." : " adventurers.")));
                }, 2);
            }
        } else {
            player.setLocation(KrakenInstance.OUTSIDE_TILE);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CREVICE_537, ObjectId.CREVICE_538 };
    }
}
