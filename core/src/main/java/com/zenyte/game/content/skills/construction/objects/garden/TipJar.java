package com.zenyte.game.content.skills.construction.objects.garden;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionDialogue;

/**
 * @author Kris | 24. veebr 2018 : 4:03.58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class TipJar implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TIP_JAR };
    }

    @Override
    public void handleObjectAction(final Player player, final Construction construction, final RoomReference reference, final WorldObject object, final int optionId, final String option) {
        if (optionId == 4) {
            if (player != construction.getPlayer()) {
                player.sendMessage("You can't manage someone else's tip jar!");
                return;
            }
            final com.zenyte.game.content.skills.construction.TipJar jar = construction.getTipJar();
            player.getDialogueManager().start(new OptionDialogue(player, "Tip jar: " + jar.getAmount() + " coin" + (jar.getAmount() == 1 ? "" : "s"), new String[] { jar.isDisplayingNotifications() ? "Disable notifications." : "Enable notifications.", jar.isBankCoinsOnLogout() ? "Disable banking coins on logout." : "Enable banking coins on logout.", "Withdraw money.", "Cancel." }, new Runnable[] { () -> jar.setDisplayingNotifications(!jar.isDisplayingNotifications()), () -> jar.setBankCoinsOnLogout(!jar.isBankCoinsOnLogout()), () -> jar.withdraw(), null }));
        } else if (optionId == 1) {
            player.sendInputInt("How many coins would you like to enqueue to the jar?", amount -> {
                final int inv = player.getInventory().getAmountOf(995);
                if (inv < amount) {
                    amount = inv;
                }
                if (amount == 0) {
                    player.sendMessage("You have no coins to enqueue to the jar.");
                    return;
                }
                player.getInventory().deleteItem(995, amount);
                construction.getTipJar().addMoney(player.getName(), amount);
            });
        }
    }
}
