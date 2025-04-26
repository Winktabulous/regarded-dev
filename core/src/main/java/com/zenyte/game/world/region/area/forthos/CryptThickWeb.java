package com.zenyte.game.world.region.area.forthos;

import com.zenyte.game.content.boss.sarachnis.SarachnisInstance;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;

import java.util.function.Consumer;

/**
 * @author Andys1814
 */
public final class CryptThickWeb implements ObjectAction {

    private final Item PRICE = new Item(995, 25000);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getY() == 9911) {
            player.lock(1);
            player.addWalkSteps(player.getX(), player.getY() + 1, 5, false);
            return;
        }

        Consumer<Player> enter = p -> {
            var paisFee = false;
            if (p.getInventory().containsItem(PRICE)) {
                if (p.getInventory().deleteItem(PRICE).isFailure()) {
                    p.sendMessage("You do not have enough coins to enter.");
                    return;
                }
                paisFee = true;
            }
            else if (p.getBank().containsItem(PRICE)) {
                if (p.getBank().remove(PRICE).isFailure()) {
                    p.sendMessage("You do not have enough coins to enter.");
                    return;
                }
                paisFee = true;
            }
            if (paisFee) {
                player.lock(1);
                Item weapon = player.getWeapon();
                player.setAnimation(new Animation(Equipment.getAttackAnimation(weapon.getId(), 1)));

                try {
                    final AllocatedArea area = MapBuilder.findEmptyChunk(8, 8);
                    var instance = new SarachnisInstance(player, area);
                    instance.constructRegion();
                    WorldTasksManager.schedule(() -> {
                        p.addWalkSteps(p.getX(), p.getY() - 1, 5, false);
                        p.sendMessage("You enter deeper into the ancient burial crypt.");

                    });
                }
                catch (OutOfSpaceException ignored) {}
            }
            else
                p.sendMessage("You do not have enough coins to enter.");

        };

        if (option.equalsIgnoreCase("Enter-crypt")) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("<col=FF0000>You are about to enter a dangerous area.<br><col=00FF00>There is a fee to instance this battle.",
                        new DialogueOption("Pay 25K and enter.", () -> {
                            enter.accept(player);
                        }),
                        new DialogueOption("Do not enter."));
                }
            });
        }
        else if (option.equalsIgnoreCase("Quick-enter")) {
            enter.accept(player);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 34858 };
    }
}
