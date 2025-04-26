package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import mgi.utilities.StringFormatUtil;

/**
 * @author Tommeh | 30 jul. 2018 | 23:38:43
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class ZamorakianSpearOnOttoNPCAction implements ItemOnNPCAction {

    private static final Item ZAMORAKIAN_SPEAR = new Item(11824);

    private static final Item ZAMORAKIAN_HASTA = new Item(11889);

    private static final Item COST = new Item(995, 300000);

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        final boolean canWield = player.getSkills().getLevel(SkillConstants.ATTACK) >= 70;
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Yes, I can convert a Zamorakian spear into a hasta.<br>The spirits require me to request " + StringFormatUtil.format(COST.getAmount()) + " coins from<br>you for this service.");
                if (!canWield) {
                    npc("<col=ff0000>~ WARNING ~</col><br><br>You do not have the requirements to wield a hasta!");
                }
                options("Do you wish to convert your spear?", "Yes", "No").onOptionOne(() -> {
                    if (player.getInventory().containsItem(COST)) {
                        setKey(5);
                        new FadeScreen(player, () -> {
                            player.getInventory().deleteItem(ZAMORAKIAN_SPEAR);
                            player.getInventory().deleteItem(COST);
                            player.getInventory().addItem(ZAMORAKIAN_HASTA);
                            WorldTasksManager.schedule(() -> player.getDialogueManager().start(new ItemChat(player, ZAMORAKIAN_HASTA, "Otto successfully transforms your Zamorakian spear into a hasta.")));
                        }).fade(2);
                    } else {
                        setKey(10);
                    }
                });
                plain(5, "Otto sets to work...", false);
                npc(10, "Sorry but you don\'t seem to have enough gold on you for this service.");
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ZAMORAKIAN_SPEAR.getId() };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 2914 };
    }
}
