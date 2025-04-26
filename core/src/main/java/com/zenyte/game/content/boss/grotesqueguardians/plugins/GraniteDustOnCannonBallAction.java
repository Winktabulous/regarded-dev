package com.zenyte.game.content.boss.grotesqueguardians.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 13/08/2019 | 23:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class GraniteDustOnCannonBallAction implements ItemOnItemAction {
    private static final int CANNONBALL = 2;
    private static final int GRANITE_DUST = 21726;
    private static final int GRANITE_CANNONBALL = 21728;

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final Item balls = from.getId() == CANNONBALL ? from : to;
        final Item dust = from.getId() == GRANITE_DUST ? from : to;
        if (player.getSkills().getLevel(SkillConstants.SMITHING) < 50) {
            player.sendMessage("You need a Smithing level of at least 50 to make granite cannonballs.");
            return;
        }
        player.sendInputInt("How many Granite cannonballs do you wish to make?", amount -> {
            if (balls.getAmount() >= amount && dust.getAmount() >= amount) {
                player.getInventory().deleteItem(CANNONBALL, amount);
                player.getInventory().deleteItem(GRANITE_DUST, amount);
                player.getInventory().addItem(GRANITE_CANNONBALL, amount);
                player.sendMessage("You apply a thick coating of granite dust to your cannonballs.");
            } else {
                player.sendMessage("You don't have enough materials to make that many.");
            }
        });
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {ItemPair.of(CANNONBALL, GRANITE_DUST)};
    }

    @Override
    public int[] getItems() {
        return new int[0];
    }
}
