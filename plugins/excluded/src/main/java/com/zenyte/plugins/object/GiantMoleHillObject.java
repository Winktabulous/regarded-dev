package com.zenyte.plugins.object;

import com.zenyte.game.content.boss.cerberus.area.CerberusLairInstance;
import com.zenyte.game.content.skills.slayer.BossTask;
import com.zenyte.game.content.skills.slayer.SlayerMaster;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import com.zenyte.plugins.object.memberzones.GiantMoleInstance;

import java.util.Set;

import static com.zenyte.game.content.skills.slayer.RegularTask.HELLHOUNDS;
import static com.zenyte.plugins.object.memberzones.GiantMoleInstance.INSIDE_TILE;

/**
 * @author Tommeh | 02/05/2019 | 18:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GiantMoleHillObject implements ObjectAction {

    private static final int COST = 500_000;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Look-inside")) {
            final Set<Player> players = GlobalAreaManager.get("Falador Mole Lair").getPlayers();
            final int playerCount = players.size();
            player.sendMessage("You look inside the mole hill and see " + (playerCount == 0 ? "no adventurers" : playerCount == 1 ? "1 adventurer" : playerCount + " adventurers") + " inside the mole tunnels.");
        }
        if(option.equalsIgnoreCase("Enter")) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Would you like to enter the public or private instance?",
                            new DialogueOption("Public", () -> player.teleport(new Location(1752, 5235, 0))),
                            new DialogueOption("Private (250k)", () -> startPrivateDialogue(player))
                    );
                }
            });
        }
        if(option.equalsIgnoreCase("Public")) {
            player.teleport(new Location(1752, 5235, 0));
        }
        if(option.equalsIgnoreCase("Private")) {
            startPrivateDialogue(player);
        }
    }

    private void startPrivateDialogue(Player player) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Would you like to create a personal instance for 500,000 GP?",
                        new DialogueOption("Yes", () -> {
                            final int amountInInventory = player.getInventory().getAmountOf(ItemId.COINS_995);
                            final int amountInBank = player.getBank().getAmountOf(ItemId.COINS_995);
                            if ((long) amountInBank + amountInInventory >= COST) {
                                player.lock(1);
                                player.getInventory().deleteItem(new Item(ItemId.COINS_995, COST)).onFailure(remainder -> player.getBank().remove(remainder));
                                player.sendMessage("Please wait a few moments as your instance is being constructed.");
                                try {
                                    final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(8, 16);
                                    final GiantMoleInstance instance = new GiantMoleInstance(player, allocatedArea, (6992 >> 8) << 3, (6992 & 0xFF) << 3);
                                    instance.constructRegion();
                                    player.setLocation(instance.getLocation(INSIDE_TILE));
                                } catch (OutOfSpaceException e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                            setKey(50);
                        }), new DialogueOption("No."));
                plain(50, "You don't have enough coins with you or in your bank.");
            }
        });
    }


    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MOLE_HILL };
    }
}
