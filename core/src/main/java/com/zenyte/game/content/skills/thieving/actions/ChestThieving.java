package com.zenyte.game.content.skills.thieving.actions;

import com.zenyte.game.content.skills.thieving.Chest;
import com.zenyte.game.content.skills.thieving.Thieving;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;

/**
 * @author Corey
 * @since 23/11/19
 */
public class ChestThieving extends Action {
    private static final Animation PICKLOCK_ANIM = new Animation(537);
    private static final Animation LOOT_ANIM = new Animation(536);
    private final Chest chest;
    private final WorldObject object;
    private final boolean searchForTraps;

    public ChestThieving(final Chest chest, final WorldObject object, final boolean searchForTraps) {
        this.chest = chest;
        this.object = object;
        this.searchForTraps = searchForTraps;
    }

    public static boolean handleChest(final Player player, final WorldObject object, final boolean searchForTraps) {
        final Chest chest = Chest.getChest(object.getId());
        if (chest == null) {
            return false;
        }
        player.getActionManager().setAction(new ChestThieving(chest, object, searchForTraps));
        return true;
    }

    @Override
    public boolean start() {
        if (player.getSkills().getLevel(SkillConstants.THIEVING) < chest.getLevel()) {
            player.sendMessage("You need a Thieving level of at least " + chest.getLevel() + " to steal from this chest.");
            return false;
        }
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You need some free inventory slots to steal from this chest.");
            return false;
        }
        if (!World.containsObjectWithId(object, object.getId())) {
            return false;
        }
        if (isChestLooted()) {
            player.sendMessage("It looks like this chest has already been looted.");
            return false;
        }
        player.setAnimation(PICKLOCK_ANIM);
        if (searchForTraps) {
            searchForTraps();
        } else {
            if (chest.isTrapped()) {
                WorldTasksManager.schedule(() -> chest.onTriggerTrap(player));
                return false;
            }
            open();
        }
        player.lock(14);
        return true;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        final boolean hasLockpick = player.getInventory().containsItem(new Item(ItemId.LOCKPICK));
        final int requirement = Math.max(1, chest.getLevel() - (hasLockpick ? 8 : 0));
        if (Thieving.success(player, requirement)) {
            success();
        } else {
            failure();
        }
        return -1;
    }

    private void open() {
        player.sendFilteredMessage("You attempt to picklock the chest.");
        delay(2);
    }

    private void searchForTraps() {
        player.sendFilteredMessage("You search the chest for traps.");
        WorldTasksManager.schedule(() -> player.sendFilteredMessage("You find a trap on the chest."), 2);
        WorldTasksManager.schedule(() -> player.sendFilteredMessage("You disable the trap."), 4);
        WorldTasksManager.schedule(this::open, 6);
        delay(7);
    }

    private void success() {
        player.sendFilteredMessage("You manage to unlock the chest.");
        player.sendSound(new SoundEffect(52));
        WorldTasksManager.schedule(() -> {
            player.getSkills().addXp(SkillConstants.THIEVING, chest.getExperience());
            addLoot();
            spawnEmptyChest();
            setLootedChestAttribute();
            chest.onSuccess(player);
        }, 2);
    }

    private void failure() {
        player.sendSound(new SoundEffect(2402));
        player.sendFilteredMessage("You fail to picklock the chest.");
        player.unlock();
        chest.onFailure(player);
    }

    private void spawnEmptyChest() {
        if (World.containsObjectWithId(object, object.getId())) {
            final WorldObject obj = new WorldObject(object);
            obj.setId(chest.getOpenId());
            World.spawnObject(obj);
            WorldTasksManager.schedule(() -> World.spawnObject(object), 1);
        }
    }

    private boolean isChestLooted() {
        final String attribute = "thieving_chest_last_looted_" + object.getPositionHash();
        if (!player.getAttributes().containsKey(attribute)) {
            return false;
        }
        final long lastLootedTime = player.getNumericAttribute(attribute).longValue();
        return lastLootedTime + TimeUnit.TICKS.toMillis(chest.getTime()) > System.currentTimeMillis();
    }

    private void setLootedChestAttribute() {
        if (chest.getTime() <= 1) {
            return;
        }
        player.addAttribute("thieving_chest_last_looted_" + object.getPositionHash(), System.currentTimeMillis());
    }

    private void addLoot() {
        if (chest.getLoot() != null) {
            final Item[] loot = new Item[chest.getLoot().length];
            for (int i = 0; i < chest.getLoot().length; i++) {
                loot[i] = chest.getLoot()[i].generateResult();
            }
            player.getInventory().addOrDrop(loot);
        } else {
            player.getInventory().addOrDrop(chest.getLootTable().generateLoot());
        }
        player.setAnimation(LOOT_ANIM);
        player.sendFilteredMessage("You steal some loot from the chest.");
        WorldTasksManager.schedule(() -> {
            player.sendSound(new SoundEffect(51, 0, 15));
            player.unlock();
        });
    }

    @Override
    public void stop() {
    }
}
