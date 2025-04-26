package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlayerChat;


public final class CorpseCartPlugin implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Search")) {
            Item item = new Item(ItemId.SEVERED_LEG_24792);
            long lastSearch = (long) player.getTemporaryAttributes().getOrDefault("CORPSE_CART", 0L);
            boolean has = player.getInventory().containsItem(item) || player.getBank().containsItem(item) || player.getEquipment().containsItem(item);
            if(!has) {
                if (player.getInventory().hasSpaceFor(item)) {
                    player.getTemporaryAttributes().put("CORPSE_CART", System.currentTimeMillis());
                    player.getDialogueManager().start(new ItemChat(player, item, "For some reason, you take a severed leg."));
                    player.getInventory().addItem(item);
                } else {
                    player.sendMessage("You don't have the space to do this.");
                }
            } else {
                player.getDialogueManager().start(new PlayerChat(player, "I probably shouldn't take any more of these."));
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CORPSE_CART_39161 , ObjectId.CORPSE_CART};
    }
}
