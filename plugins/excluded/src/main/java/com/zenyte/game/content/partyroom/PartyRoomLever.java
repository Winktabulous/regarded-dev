package com.zenyte.game.content.partyroom;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 26/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PartyRoomLever implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.faceDirection(Direction.EAST);
        pullLever(player, object);
        if (FaladorPartyRoom.getPartyRoom().getVariables().isDisabled()) {
            player.sendMessage("The party room has been disabled.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options(TITLE, new DialogueOption("Balloon Bonanza.", key(100)), new DialogueOption("Nightly Dance.", key(200)), new DialogueOption("No action."));
                plain(100, "Balloon Bonanze costs 1000 coins.");
                if (player.getInventory().containsItem(ItemId.COINS_995, 1000)) {
                    options("Continue?", new DialogueOption("Yes.", () -> {
                        if (!player.getPrivilege().eligibleTo(FaladorPartyRoom.getPartyRoom().getVariables().getMinimumPrivilegeToDropBalloons())) {
                            setKey(115);
                            return;
                        }
                        if (!player.getInventory().containsItem(ItemId.COINS_995, 1000)) {
                            player.sendMessage("You need at least 1000 coins to pull the lever for a balloon bonanza.");
                            return;
                        }
                        player.getInventory().deleteItem(new Item(ItemId.COINS_995, 1000));
                        pullLever(player, object);
                        final PartyRoomVariables variables = FaladorPartyRoom.getPartyRoom().getVariables();
                        variables.pull();
                        final long seconds = TimeUnit.TICKS.toSeconds(variables.getCountdown());
                        player.sendMessage("You pull the lever. The balloons will begin to drop in " + seconds + " seconds.");
                    }), new DialogueOption("No."));
                    plain(115, "You are not eligible to perform Balloon Bonanza right now.");
                }
                plain(200, "Nightly Dance costs 500 coins.");
                if (player.getInventory().containsItem(ItemId.COINS_995, 500)) {
                    options("Continue?", new DialogueOption("Yes.", () -> {
                        if (!player.getPrivilege().eligibleTo(FaladorPartyRoom.getPartyRoom().getVariables().getMinimumPrivilegeToDropBalloons())) {
                            setKey(215);
                            return;
                        }
                        if (FaladorPartyRoom.getPartyRoom().isDancing()) {
                            setKey(250);
                            return;
                        }
                        if (!player.getInventory().containsItem(ItemId.COINS_995, 500)) {
                            player.sendMessage("You need at least 500 coins to pull the lever for a Nightly Dance.");
                            return;
                        }
                        player.getInventory().deleteItem(new Item(ItemId.COINS_995, 1000));
                        pullLever(player, object);
                        FaladorPartyRoom.getPartyRoom().startKnightsDance();
                    }), new DialogueOption("No."));
                    plain(215, "You are not eligible to perform Nightly Dance right now.");
                    plain(250, "The party room knights are already here!");
                }
            }
        });
    }

    @Override
    public int getDelay() {
        return 1;
    }

    private final void pullLever(@NotNull final Player player, @NotNull final WorldObject object) {
        player.setAnimation(new Animation(6933));
        World.sendObjectAnimation(object, new Animation(6934));
        player.sendSound(new SoundEffect(2400));
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LEVER_26194 };
    }
}
