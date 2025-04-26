package com.near_reality.game.content.gauntlet.actions;

import com.near_reality.game.content.gauntlet.Gauntlet;
import com.near_reality.game.content.gauntlet.GauntletConstants;
import com.near_reality.game.content.gauntlet.objects.CrystalDeposit;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.plugins.dialogue.PlainChat;

import static com.zenyte.game.item.ItemId.*;

/**
 *
 *
 * @author Andys1814.
 * @since 1/25/2022.
 */
public final class CrystalDepositAction extends Action {

    private static final Animation ANIMATION = new Animation(8347);

    private static final Animation ANIMATION_CORRUPTED = new Animation(8348);

    private static final String STOP_MESSAGE = "Your inventory is too full to hold any more ore.";

    private final CrystalDeposit deposit;

    private final boolean corrupted;

    public CrystalDepositAction(CrystalDeposit deposit, boolean corrupted) {
        this.deposit = deposit;
        this.corrupted = corrupted;
    }

    @Override
    public boolean start() {
        if (!hasPickaxe()) {
            player.getDialogueManager().start(new PlainChat(player, "You need a pickaxe to mine this rock."));
            return false;
        }

        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage(STOP_MESSAGE);
            return false;
        }

        player.sendMessage("You swing your pick at the rock.");
        delay(1);

        return true;
    }

    @Override
    public boolean process() {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage(STOP_MESSAGE);
            return false;
        }

        return true;
    }

    @Override
    public int processWithDelay() {
        player.setAnimation(corrupted ? ANIMATION_CORRUPTED : ANIMATION);

        player.getInventory().addItem(new Item(deposit.getResource()));
        player.sendMessage("You manage to mine some ore.");

        player.getSkills().addXp(SkillConstants.MINING, 1);

        Gauntlet.rollShards(player, corrupted);

        deposit.deplete();

        if (deposit.isDepleted()) {
            deposit.getDepletionMessage().ifPresent(message -> player.sendMessage(message));

            World.removeObject(deposit);
            deposit.setId(deposit.getId() + 1); // Set roots to depleted object variant.
            World.spawnObject(deposit);

            return -1;
        }

        return 2;
    }

    @Override
    public void stop() {
        player.setAnimation(Animation.STOP);
    }

    @Override
    public boolean interruptedByCombat() {
        return false;
    }

    private boolean hasPickaxe() {
        if (player.getEquipment().containsItem(CRYSTAL_PICKAXE_23863) || player.getInventory().containsItem(CRYSTAL_PICKAXE_23863)) {
            return true;
        }

        return player.getEquipment().containsItem(CORRUPTED_PICKAXE) || player.getInventory().containsItem(CORRUPTED_PICKAXE);
    }

}
