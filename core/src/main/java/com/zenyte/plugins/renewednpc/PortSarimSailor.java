package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.sailing.Sailing;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25/11/2018 19:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PortSarimSailor extends NPCPlugin {

    private static void handle(Player player, NPC npc, boolean talkTo) {
        var cost = new Item(995, 30);
        var hasGloves = player.getEquipment().containsItem(ItemId.KARAMJA_GLOVES_3);
        if (hasGloves)
            cost.setAmount(15);
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                if (talkTo)
                    npc("Do you want to go on a trip to Karamja?");
                npc("The trip will cost you %d coins.".formatted(cost.getAmount()));
                options(TITLE, "Yes please.", "No, thank you.").onOptionOne(() -> setKey(10));
                player(10, "Yes please!").executeAction(() -> {
                    if (player.getInventory().containsItem(cost) && !player.getInventory().deleteItem(cost).isFailure()) {
                        player.sendMessage("You pay %d coins and board the ship.".formatted(cost.getAmount()));
                        Sailing.sail(player, "Port Sarim", "Karamja");
                    }
                    else {
                        setKey(15);
                    }
                });
                npc(15, "You don't have enough gold on you to go on this trip.");
            }
        });
    }

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) return;
            handle(player, npc, true);
        });
        bind("Pay-fare", (player, npc) -> handle(player, npc, false));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.CAPTAIN_TOBIAS, NpcId.SEAMAN_LORRIS, NpcId.SEAMAN_THRESNOR };
    }
}
