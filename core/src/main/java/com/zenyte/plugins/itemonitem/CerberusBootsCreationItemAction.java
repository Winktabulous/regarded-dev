package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.BossDropItem;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 21-4-2018 | 23:12
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CerberusBootsCreationItemAction implements ItemOnItemAction {

    private static final Graphics GRAPHICS = new Graphics(759);
    private static final Animation ANIMATION = new Animation(4462);

    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final BossDropItem item = BossDropItem.getItemByMaterials(from, to);
        if (item == null) {
        	player.sendMessage("Nothing interesting happens.");
            return;
        }
        if (player.getSkills().getLevelForXp(SkillConstants.MAGIC) < 60 || player.getSkills().getLevelForXp(SkillConstants.RUNECRAFTING) < 60) {
            player.sendMessage("You need a Magic and Runecrafting level of at least 60 to do this.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                final Item base = item.getMaterials()[0];
                final Item crystal = item.getMaterials()[1];
                final Item product = item.getItem();
                doubleItem(base, crystal, "Are you sure you wish to infuse the " + base.getName() + " and " + crystal.getName() + " to create " + product.getName() + "? This can not be reversed.");
                options(TITLE, "Proceed with the infusion.", "Cancel")
                        .onOptionOne(() -> {
                            player.setGraphics(GRAPHICS);
                            player.setAnimation(ANIMATION);
                            player.getInventory().deleteItem(base);
                            player.getInventory().deleteItem(crystal);
                            player.getInventory().addItem(product);
                            player.getSkills().addXp(SkillConstants.MAGIC, 200);
                            player.getSkills().addXp(SkillConstants.RUNECRAFTING, 200);
                            setKey(5);
                        });
                item(5, product, "You successfully infuse the " + base.getName() + " and " + crystal.getName() + " to create " + product.getName() + ".");
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { 11840, 6920, 2577, 13231, 13227, 13229};
    }
}
