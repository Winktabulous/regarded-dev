package com.zenyte.game.content.kebos.konar.plugins.item;

import com.zenyte.game.content.skills.prayer.ectofuntus.Bonecrusher;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 26/10/2019 | 16:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class BonecrusherNecklace extends ItemPlugin  {
    public static final Item bonecrusher = new Item(ItemId.BONECRUSHER);
    public static final Item dragonBoneNecklace = new Item(ItemId.DRAGONBONE_NECKLACE);
    public static final Item hydraTail = new Item(ItemId.HYDRA_TAIL);
    public static final Item bonecrusherNecklace = new Item(ItemId.BONECRUSHER_NECKLACE);

    @Override
    public void handle() {
        bind("Wear", (player, item, slotId) -> {
            player.getEquipment().wear(slotId);
            player.addTemporaryAttribute("bonecrusher_necklace_effect_delay", Utils.currentTimeMillis() + 9000);
        });
        bind("Activity", (player, item, container, slotId) -> {
            player.getSettings().toggleSetting(Setting.BONECRUSHING_INACTIVE);
            player.sendMessage(!Bonecrusher.enabled(player) ? "Your bonecrusher necklace is no longer crushing bones." : "Your bonecrusher necklace is now crushing bones.");
        });
        bind("Dismantle", (player, item, slotId) -> {
            if (!player.getInventory().checkSpace(1)) {
                return;
            }
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(bonecrusherNecklace, "Are you sure you wish to dismantle the Bonecrusher Necklace into a Hydra Tail, Dragonbone necklace and Bonecrusher?");
                    options("Dismantle the Bonecrusher necklace?", "Yes.", "No.").onOptionOne(() -> {
                        player.getInventory().deleteItemsIfContains(new Item[] {bonecrusherNecklace}, () -> {
                            player.getInventory().addOrDrop(bonecrusher, hydraTail, dragonBoneNecklace);
                            player.sendMessage("You have successfully dismantled your Bonecrusher necklace into a Hydra Tail, Dragonbone necklace and Bonecrusher.");
                        });
                    });
                }
            });
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.BONECRUSHER_NECKLACE};
    }
}
