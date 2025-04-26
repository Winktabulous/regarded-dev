package com.zenyte.game.content.chambersofxeric.skills;

import com.zenyte.game.content.chambersofxeric.room.IceDemonRoom;
import com.zenyte.game.content.skills.woodcutting.actions.Woodcutting;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Kris | 23. sep 2019 : 14:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RaidWoodcutting extends Action {
    /**
     * The sound effect played when the played cuts a log.
     */
    private static final SoundEffect sound = new SoundEffect(2070);
    /**
     * The sound effect played when the tree falls down.
     */
    private static final SoundEffect treeFallingSound = new SoundEffect(2734, 8, 0);
    public static final int CAP_MAXIMUM_KINDLING_STACK = 28;
    /**
     * The tree game object which we are chopping at the time.
     */
    private final WorldObject tree;
    /**
     * The milestones at which the player receives more and more kindling. Each milestone gives an extra kindling, up to a total of 8 maximum.
     */
    private final int[] milestones = new int[] {1, 24, 36, 48, 60, 72, 84, 96};
    /**
     * The experience granted at different milestones.
     */
    private final float[] experience = new float[] {30.0F, 45.0F, 55.0F, 62.5F, 38.5F, 73.5F, 77.75F, 81.5F};
    /**
     * The best hatchet that the player can use which they are carrying.
     */
    private Woodcutting.AxeResult hatchet;
    /**
     * The number of ticks the action has been executing for.
     */
    private int ticks;

    public RaidWoodcutting(final WorldObject tree) {
        this.tree = tree;
    }

    @Override
    public boolean start() {
        final Optional<Woodcutting.AxeResult> optionalAxe = Woodcutting.getAxe(player);
        if (!optionalAxe.isPresent()) {
            player.sendMessage("You do not have an axe which you have the woodcutting level to use.");
            return false;
        }
        this.hatchet = optionalAxe.get();
        if (!player.getInventory().hasFreeSlots()) {
            player.sendFilteredMessage("Not enough space in your inventory.");
            return false;
        }
        player.sendMessage("You swing your hatchet at the sapling...");
        delay(1);
        return true;
    }

    @Override
    public boolean process() {
        if (ticks++ % 4 == 0) player.setAnimation(hatchet.getDefinition().getTreeCutAnimation());
        return World.exists(tree);
    }

    /**
     * Checks whether or not the player successfully chopped a log. Unrelated to the player's woodcutting level or the tree, or even the axe.
     *
     * @return whether or not they successfully chopped a log.
     */
    public boolean success() {
        return Utils.random(5) <= 4;
    }

    @Override
    public int processWithDelay() {
        if (!success()) {
            return this.hatchet.getDefinition().getCutTime();
        }
        final int amountInInventory = player.getInventory().getAmountOf(ItemId.KINDLING_20799);
        if (amountInInventory >= CAP_MAXIMUM_KINDLING_STACK) {
            player.setAnimation(new Animation(-1));
            player.sendMessage("You already have the maximum amount of kindlings.");
            return -1;
        } else {
            addLog(player);
            if (Utils.random(7) == 0) {
                final WorldObject stump = new WorldObject(29764, tree.getType(), tree.getRotation(), tree.getX(), tree.getY(), tree.getPlane());
                World.spawnObject(stump);
                WorldTasksManager.schedule(() -> World.spawnObject(tree), 15);
                World.sendSoundEffect(stump, treeFallingSound);
                player.setAnimation(new Animation(-1));
                return -1;
            }
            if (!player.getInventory().hasFreeSlots()) {
                player.setAnimation(new Animation(-1));
                player.sendFilteredMessage("Not enough space in your inventory.");
                return -1;
            }
        }
        return 2;
    }

    /**
     * Adds some logs in the player's inventory if they're within a raid.
     *
     * @param player the player who is chopping the tree.
     */
    private void addLog(final Player player) {
        player.getRaid().ifPresent(raid -> {
            final MutableInt amount = new MutableInt(1);
            raid.ifInRoom(player, IceDemonRoom.class, room -> amount.setValue(Math.min(player.getInventory().getFreeSlots(), Utils.random(1, getMaximumAmountKindling(player)))));
            final float experience = this.experience[amount.intValue() - 1];
            final int amountInInventory = player.getInventory().getAmountOf(ItemId.KINDLING_20799);
            final int amountToAdd = Math.max(0, Math.min(amount.intValue(), CAP_MAXIMUM_KINDLING_STACK - amountInInventory));
            if (amountToAdd > 0) {
                player.getSkills().addXp(8, experience);
                player.sendMessage("You get some kindling.");
                player.getInventory().addItem(ItemId.KINDLING_20799, amountToAdd);
                player.sendSound(sound);
            }
        });
    }

    /**
     * Calculates teh maximum amount of kindling that the player can receive from the tree based on their woodcutting level.
     *
     * @param player the player cutting the tree.
     * @return the maximum number of kindling they can receive based on their woodcutting level.
     */
    private int getMaximumAmountKindling(@NotNull final Player player) {
        final int level = Math.max(player.getSkills().getLevel(SkillConstants.WOODCUTTING), player.getSkills().getLevelForXp(SkillConstants.WOODCUTTING));
        int max = 0;
        for (final int milestone : milestones) {
            if (level < milestone) {
                break;
            }
            max++;
        }
        return max;
    }

    @Override
    public void stop() {
        delay(3);
    }
}
