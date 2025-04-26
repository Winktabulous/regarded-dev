package com.zenyte.plugins.item;

import com.zenyte.game.content.skills.magic.spells.teleports.ItemTeleport;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16/03/2019 02:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HunterCape extends ItemPlugin {
    @Override
    public void handle() {
        bind("Teleport", (player, item, slotId) -> {
           player.getDialogueManager().start(new Dialogue(player) {
               @Override
               public void buildDialogue() {
                   options("Which hunting location would you like to teleport to?", "Carnivorous chinchompas (Feldip " +
                           "Hills)", "Black chinchompas (Wilderness)", "Cancel")
                           .onOptionOne(() -> ItemTeleport.MAX_CAPE_CARNIVEROUS_CHINCHOMPAS.teleport(player))
                           .onOptionTwo(() -> ItemTeleport.MAX_CAPE_BLACK_CHINCHOMPAS.teleport(player));
               }
           });
        });
    }

    @Override
    public int[] getItems() {
        return SkillcapePerk.HUNTER.getSkillCapes();
    }
}
