package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.boons.impl.SwissArmyMan;
import com.zenyte.game.content.skills.farming.*;
import com.zenyte.game.content.skills.woodcutting.TreeDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.dialogue.PlayerChat;

/**
 * @author Kris | 03/02/2019 19:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Clearing extends Action {
    private static final Animation SPADE_ANIM = new Animation(830);
    private static final Item SPADE = new Item(952);
    private static final String permissionAttr = "dig up farming patch";

    public Clearing(final FarmingSpot spot) {
        this.spot = spot;
    }

    private final FarmingSpot spot;

    @Override
    public boolean start() {
        if (spot.getPatch() == FarmingPatch.FARMING_GUILD_REDWOOD) {
            player.getDialogueManager().start(new PlainChat(player, "This looks like a rather complicated job to take on yourself.<br>Perhaps Alexandra is willing to clear this out for you instead."));
            return false;
        }
        if (!player.hasBoon(SwissArmyMan.class) && !player.getInventory().containsItems(SPADE)) {
            player.sendMessage("You need a spade to clear this patch.");
            return false;
        }
        final PatchState state = spot.getState();
        final boolean tree = spot.getProduct().isTree();
        final FarmingProduct product = spot.getProduct();
        if ((state == PatchState.GROWING || state == PatchState.WATERED) && tree) {
            final boolean permission = player.getTemporaryAttributes().remove(permissionAttr) != null;
            if (!permission) {
                sendTreeClearRequest();
                return false;
            }
        } else if (tree && state != PatchState.STUMP && state != PatchState.DISEASED && state != PatchState.DEAD && product != FarmingProduct.SPIRIT_TREE && product != FarmingProduct.CALQUAT) {
            player.getDialogueManager().start(new PlainChat(player, "You need to chop this tree down first, and then dig up the tree stump."));
            return false;
        }
        if (state == PatchState.WEEDS) {
            player.sendMessage("There aren't any crops in this patch to dig up.");
            return false;
        } else if ((state == PatchState.GROWING || state == PatchState.WATERED) && !tree) {
            player.getDialogueManager().start(new PlayerChat(player, "Dig up these healthy plants? Why would I want to do that?"));
            return false;
        } else if (state == PatchState.GROWN && product != FarmingProduct.HESPORI && product != FarmingProduct.GRAPE && product != FarmingProduct.SPIRIT_TREE && product != FarmingProduct.CALQUAT && spot.getPatch().getType() != PatchType.ANIMA_PATCH) {
            player.getActionManager().setAction(new Harvesting(spot));
            return false;
        }
        player.sendFilteredMessage("You start digging the farming patch...");
        animate();
        delay(3);
        return true;
    }

    private void sendTreeClearRequest() {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Are you sure you want to dig up this patch?", new DialogueOption("Yes, I want to clear it for new crops.", () -> {
                    player.getTemporaryAttributes().put(permissionAttr, true);
                    player.getActionManager().setAction(Clearing.this);
                }), new DialogueOption("No, I want to leave it as it is."));
            }
        });
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        final boolean tree = spot.getProduct().isTree();
        if (tree) {
            final PatchState state = spot.getState();
            if (spot.getPatch().getType() != PatchType.CALQUAT_PATCH && (state == PatchState.GROWN || state == PatchState.REGAINING_PRODUCE)) {
                player.sendMessage("You are too late - the tree has already regrown.");
                return -1;
            }
        }
        if ((tree && spot.getProduct().getTreeDefinitions() == TreeDefinitions.FRUIT_TREE) || Utils.random(5) <= 3) {
            if (Utils.random(200) == 0) {
                player.getInventory().addOrDrop(new Item(22875, 1));
                player.sendMessage("You find a Hespori seed.");
            }
            final int value = spot.getValue();
            final FarmingProduct product = spot.getProduct();
            final PatchType type = product.getType();
            player.sendFilteredMessage("You have successfully cleared this patch for new crops.");
            if (tree) {
                final int rootsAmount = getRootsAmount();
                if (rootsAmount > 0) {
                    player.getInventory().addOrDrop(new Item(spot.getProduct().getProduct().getId(), rootsAmount));
                }
            }
            spot.clear();
            if (type == PatchType.HESPORI_PATCH) {
                if (value == 8) {
                    final Item seed = new Item(22881 + ((Utils.random(2) * 2)), Utils.random(1, 2));
                    LootBroadcastPlugin.fireEvent(player.getName(), seed, player.getLocation(), false);
                    player.getInventory().addOrDrop(seed);
                    player.getCollectionLog().add(seed);
                    NPCDrops.rollTable(player, NPCDrops.getTable(8583), drop -> {
                        final Item item = new Item(drop.getItemId(), Utils.random(drop.getMinAmount(), drop.getMaxAmount()));
                        LootBroadcastPlugin.fireEvent(player.getName(), item, player.getLocation(), false);
                        player.getInventory().addOrDrop(item);
                        player.getCollectionLog().add(item);
                    });
                    spot.refresh();
                    player.getSkills().addXp(SkillConstants.FARMING, product.getHarvestExperience());
                }
            }
            return -1;
        }
        animate();
        return 2;
    }

    private final int getRootsAmount() {
        final int level = player.getSkills().getLevelForXp(SkillConstants.FARMING);
        switch (spot.getProduct().getTreeDefinitions()) {
        case OAK: 
            return level >= 39 ? 4 : level >= 31 ? 3 : level >= 23 ? 2 : 1;
        case WILLOW_TREE: 
            return level >= 54 ? 4 : level >= 46 ? 3 : level >= 38 ? 2 : 1;
        case MAPLE_TREE: 
            return level >= 69 ? 4 : level >= 61 ? 3 : level >= 53 ? 2 : 1;
        case YEW_TREE: 
            return level >= 84 ? 4 : level >= 76 ? 3 : level >= 68 ? 2 : 1;
        case MAGIC_TREE: 
            return level >= 99 ? 4 : level >= 91 ? 3 : level >= 83 ? 2 : 1;
        }
        return 0;
    }

    private void animate() {
        player.setAnimation(SPADE_ANIM);
        player.sendSound(new SoundEffect(1470));
    }

    @Override
    public void stop() {
        player.getTemporaryAttributes().remove("dig up tree patch");
    }
}
