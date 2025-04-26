package com.zenyte.game.content.chompy.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemChain;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;

public class SwampToad extends NPCPlugin {
    public static final int SWAMP_TOAD_ID = 1473;
    private static final Animation inflatingAnim = new Animation(1019);
    private static final Animation resetAnim = new Animation(1022);

    @Override
    public void handle() {
        bind("Inflate", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                final Item bellows = getBellows(player);
                start(player, npc, bellows, bellows == null ? -1 : player.getInventory().getContainer().getSlot(bellows));
            }
            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    private static Item getBellows(final Player player) {
        final int[] bellowsIds = ItemChain.OGRE_BELLOWS.getAllButFirst();
        final Inventory inventory = player.getInventory();
        if (player.carryingAny(bellowsIds)) {
            return inventory.getAny(bellowsIds);
        }
        return null;
    }

    public static void start(final Player player, final NPC toad, final Item bellows, final int slot) {
        if (player.getInventory().checkSpace()) {
            player.lock(4);
            WorldTasksManager.schedule(() -> inflate(player, toad));
            WorldTasksManager.schedule(() -> finish(player, toad, bellows, slot), 2);
        }
    }

    private static void inflate(final Player player, final NPC toad) {
        player.setForceTalk(new ForceTalk("Come here toady!"));
        player.setAnimation(SwampBubbles.suckingAnim);
        player.setGraphics(SwampBubbles.bellowsGfx);
        toad.setAnimation(inflatingAnim);
    }

    private static void finish(final Player player, final NPC toad, final Item bellows, final int slot) {
        if (bellows == null) {
            toad.setForceTalk(new ForceTalk("Hisss..."));
            toad.setAnimation(resetAnim);
            player.sendMessage("The air seems too thin to stay in the toad.");
            player.sendMessage("Perhaps you need something thicker than air.");
            return;
        }
        handleEscape(player);
        if (!toad.isFinished()) {
            toad.finish();
            toad.setRespawnTask();
        }
        final Inventory inventory = player.getInventory();
        inventory.replaceItem(ItemChain.OGRE_BELLOWS.before(bellows.getId()), 1, slot);
        inventory.addItem(ItemId.BLOATED_TOAD, 1);
        player.sendFilteredMessage("You manage to catch the toad and inflate it with the swamp gas.");
    }

    private static void handleEscape(final Player player) {
        final Inventory inventory = player.getInventory();
        if (inventory.getAmountOf(ItemId.BLOATED_TOAD) >= 3) {
            inventory.deleteItem(ItemId.BLOATED_TOAD, 1);
            player.sendMessage("One of your bloated toads manages to escape.");
        }
    }

    @Override
    public int[] getNPCs() {
        return new int[] {SWAMP_TOAD_ID};
    }
}
