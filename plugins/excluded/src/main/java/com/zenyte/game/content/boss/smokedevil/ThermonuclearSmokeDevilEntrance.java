package com.zenyte.game.content.boss.smokedevil;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import mgi.utilities.StringFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author Tommeh | 21 aug. 2018 | 16:59:45
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ThermonuclearSmokeDevilEntrance implements ObjectAction {

    private static final Logger log = LoggerFactory.getLogger(ThermonuclearSmokeDevilEntrance.class);

    private static final Location outsideTile = new Location(2379, 9452, 0);

    private static final Location insideTile = new Location(2376, 9452, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == ObjectId.CREVICE_535) {
            if (option.equals("Peek")) {
                player.sendMessage("You peek through the crevice...");
                WorldTasksManager.schedule(() -> {
                    final Set<Player> players = GlobalAreaManager.get("Thermonuclear Boss Room").getPlayers();
                    final int playerCount = players.size();
                    player.sendMessage("Standard cave: " + (playerCount == 0 ? "No adventurers." : playerCount + (playerCount == 1 ? " adventurer." : " adventurers.")));
                }, 2);
            } else if (option.equals("Private")) {
                player.getDialogueManager().start(new Dialogue(player) {

                    @Override
                    public void buildDialogue() {
                        final int price = 100000;
                        options("Enter a private Thermonuclear boss cave?", "Pay " + StringFormatUtil.format(price) + " coins.", "Cancel.").onOptionOne(() -> {
                            try {
                                final int amountInInventory = player.getInventory().getAmountOf(ItemId.COINS_995);
                                final int amountInBank = player.getBank().getAmountOf(ItemId.COINS_995);
                                if ((long) amountInBank + amountInInventory < price) {
                                    setKey(100);
                                    return;
                                }
                                player.lock(10);
                                player.getInventory().deleteItem(new Item(ItemId.COINS_995, price)).onFailure(remainder -> player.getBank().remove(remainder));
                                final AllocatedArea area = MapBuilder.findEmptyChunk(7, 5);
                                final SmokeDevilInstance instance = new SmokeDevilInstance(player, area, 293, 1179);
                                instance.constructRegion();
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        });
                        plain(100, "You need at least " + StringFormatUtil.format(price) + " coins to start a private Thermonuclear boss instance.");
                    }
                });
            } else {
                player.teleport(insideTile);
            }
        } else {
            player.teleport(outsideTile);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CREVICE_535, ObjectId.CREVICE_536 };
    }
}
