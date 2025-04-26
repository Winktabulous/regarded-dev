package com.zenyte.game.content.chambersofxeric.plugins.object;

import com.zenyte.game.content.boons.impl.SwissArmyMan;
import com.zenyte.game.content.chambersofxeric.npc.IceDemon;
import com.zenyte.game.content.chambersofxeric.npc.IcefiendNPC;
import com.zenyte.game.content.chambersofxeric.room.IceDemonRoom;
import com.zenyte.game.content.chambersofxeric.skills.RaidWoodcutting;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.content.chambersofxeric.room.IceDemonRoom.depositAnimation;

/**
 * @author Kris | 06/07/2019 03:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BrazierObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getRaid().ifPresent(raid -> raid.ifInRoom(player, IceDemonRoom.class, room -> {
            final int chance = player.getSkills().getLevel(SkillConstants.FIREMAKING) / 6;
            player.getActionManager().setAction(new Action() {

                @Override
                public boolean start() {
                    return true;
                }

                @Override
                public boolean process() {
                    return preconditions(player, object, room);
                }

                @Override
                public int processWithDelay() {
                    if (object.getId() == 29747 && Utils.random(80 + chance) < 5) {
                        player.setAnimation(depositAnimation);
                        player.sendSound(new SoundEffect(2066));
                        return 2;
                    }
                    light(player, object);
                    return -1;
                }
            });
        }));
    }

    private boolean preconditions(@NotNull final Player player, @NotNull final WorldObject object, @NotNull final IceDemonRoom room) {
        final Inventory inventory = player.getInventory();
        if (object.getId() == ObjectId.BRAZIER_29747) {
            final IceDemon demon = room.getDemon();
            if (demon != null && demon.getStage() != 0) {
                player.sendMessage("That\'s not going to help now.");
                return false;
            }
            if (!inventory.containsItem(ItemId.KINDLING_20799, 1)) {
                player.sendMessage("You need some kindling to light the brazier.");
                return false;
            }
            if (!player.hasBoon(SwissArmyMan.class) && !inventory.containsItem(590, 1)) {
                player.sendMessage("You need a tinderbox to light the brazier.");
                return false;
            }
        }
        if (!inventory.containsItem(ItemId.KINDLING_20799, 1)) {
            player.sendMessage("You need some kindling to light the brazier.");
            return false;
        }
        return true;
    }

    private void light(@NotNull final Player player, @NotNull final WorldObject object) {
        player.getRaid().ifPresent(raid -> raid.ifInRoom(player, IceDemonRoom.class, room -> {
            if (object.getId() == ObjectId.BRAZIER_29747) {
                if (room.getDemon().getStage() != 0) {
                    player.sendMessage("That\'s not going to help now.");
                    return;
                }
                if (!player.hasBoon(SwissArmyMan.class) && !player.getInventory().containsItem(590, 1)) {
                    player.sendMessage("You need a tinderbox to light the brazier.");
                    return;
                }
                final int amountInInventory = player.getInventory().getAmountOf(ItemId.KINDLING_20799);
                final int amountToDeposit = Math.max(0, Math.min(RaidWoodcutting.CAP_MAXIMUM_KINDLING_STACK, amountInInventory));
                if (amountToDeposit > 0) {
                    final IcefiendNPC icefiend = CollectionUtils.findMatching(room.getIcefiends(), i -> i.getLocation().withinDistance(object, 1));
                    final IceDemonRoom.Brazier brazier = new IceDemonRoom.Brazier(room, amountToDeposit * 5, icefiend, 29748, object.getType(), object.getRotation(), new Location(object));
                    World.spawnObject(brazier);
                    room.getBraziers().add(brazier);
                    raid.addPoints(player, amountToDeposit * 10);
                    player.setAnimation(depositAnimation);
                    player.sendMessage("You put the kindling in the brazier and light it on fire.");
                    player.sendSound(new SoundEffect(2066));
                    player.getSkills().addXp(SkillConstants.FIREMAKING, amountToDeposit * 5);
                    player.getInventory().deleteItem(ItemId.KINDLING_20799, amountInInventory);
                } else {
                    player.sendMessage("You need some kindling to light the brazier.");
                }
                return;
            }
            if (!player.getInventory().containsItem(20799, 1)) {
                player.sendMessage("You need some kindling to light the brazier.");
                return;
            }
            final IceDemonRoom.Brazier brazier = CollectionUtils.findMatching(room.getBraziers(), b -> b == object);
            if (brazier == null) {
                return;
            }
            final int amount = player.getInventory().getAmountOf(20799);
            brazier.setKindling(brazier.getKindling() + (amount * 5));
            raid.addPoints(player, amount * 10);
            player.setAnimation(new Animation(832));
            player.sendMessage("You add the kindling to the fire in the brazier.");
            player.getSkills().addXp(SkillConstants.FIREMAKING, amount * 5);
            player.getInventory().deleteItem(20799, amount);
        }));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.BRAZIER_29748, ObjectId.BRAZIER_29747 };
    }
}
