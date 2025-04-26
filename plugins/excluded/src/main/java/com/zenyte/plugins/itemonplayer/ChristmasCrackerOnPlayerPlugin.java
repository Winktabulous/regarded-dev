package com.zenyte.plugins.itemonplayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnPlayerPlugin;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 15/06/2019 10:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ChristmasCrackerOnPlayerPlugin implements ItemOnPlayerPlugin {

    private static final Animation anim = new Animation(365);
    private static final int[] partyhats = new int[] {
            1038, 1040, 1042, 1044, 1046, 1048
    };

    private static final Item[] loot = new Item[] {
            new Item(1973),
            new Item(2355),
            new Item(1969),
            new Item(441, 5),
            new Item(1897),
            new Item(1718),
            new Item(950),
            new Item(1635),
            new Item(1217),
            new Item(563)
    };

    @Override
    public void handleItemOnPlayerAction(final Player player, final Item item, final int slot, final Player target) {
        if (target.getVarManager().getBitValue(SettingVariables.ACCEPT_AID_VARBIT_ID) != 1) {
            player.sendMessage("The other player is not accepting aid.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(item, "Pull the cracker with " + target.getName() + "? You will be the one receiving the partyhat.");
                options(TITLE, new DialogueOption("Pull the cracker.", () -> {
                    if (isUnavailable(player) || isUnavailable(target)) {
                        player.sendMessage("Unable to process request.");
                        return;
                    }
                    if (player.getInventory().getItem(slot) != item) {
                        return;
                    }
                    target.setFaceLocation(player.getLocation());
                    player.setAnimation(anim);
                    player.getInventory().deleteItem(item);
                    player.getInventory().addOrDrop(new Item(partyhats[Utils.random(partyhats.length - 1)]));
                    target.getInventory().addOrDrop(new Item(Utils.getRandomElement(loot)));
                }), new DialogueOption("Cancel."));
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {
                962
        };
    }
}
