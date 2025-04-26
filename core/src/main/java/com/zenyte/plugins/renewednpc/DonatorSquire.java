package com.zenyte.plugins.renewednpc;

import com.zenyte.ContentConstants;
import com.zenyte.game.content.donation.HomeTeleport;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.utils.StringUtilities;

@SuppressWarnings("all")
public class DonatorSquire extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {

                if(!player.getMemberRank().equalToOrGreaterThan(MemberRank.EXPANSION)) {
                    npc("You must be Expansion donator+ to change your home teleport.");
                    return;
                }

                npc(1, "So how's that "+HomeTeleport.VALUES[((Number)player.getAttributeOrDefault("HOME_TELEPORT", 0)).intValue()].getName()+" home teleport working for you?");

                options(2, "Change home teleport?", "Please switch my home teleport.", "It's fine, thanks.")
                        .onOptionOne(() -> setKey(5))
                        .onOptionTwo(() -> setKey(9));

                player(5, "Please switch my home teleport.");

                options(6, "Choose a location", ContentConstants.SERVER_NAME + " Home", "Mage's Bank", "Forex Enclave", "Donator Island", "Next")
                        .onOptionOne(() -> changeTeleport(player, 0))
                        .onOptionTwo(() -> changeTeleport(player, 1))
                        .onOptionThree(() -> changeTeleport(player, 2))
                        .onOptionFour(() -> changeTeleport(player, 3))
                        .onOptionFive(() -> setKey(7));
                options(7, "Choose a location", "Expansion Island",  "Respected Island", "Legendary Island", "Uber Island", "Previous")
                        .onOptionOne(() -> changeTeleport(player, 4))
                        .onOptionTwo(() -> changeTeleport(player, 5))
                        .onOptionThree(() -> changeTeleport(player, 6))
                        .onOptionFour(() -> changeTeleport(player, 7))
                        .onOptionFive(() -> setKey(6));



                player(9, "It's fine, thanks.");
                npc(10, "Come to see me if you want to change your home teleport again.");

            }
        }));
    }

    public void changeTeleport(Player player, int option) {
        HomeTeleport tp = HomeTeleport.VALUES[option];
        player.getDialogueManager().start(new Dialogue(player, 16060) {
            @Override
            public void buildDialogue() {

                if(!player.getMemberRank().equalToOrGreaterThan(tp.getRequired())) {
                   npc("You must be an "+ StringUtilities.formatEnum(tp.getRequired())+"+ Premium to choose your home teleport.");
                   return;
                }

                npc("You really want your home teleport to be there? Are you sure? Come to see me if you want to change your home teleport again.");
                options("Change home teleport to "+tp.getName()+"?", "Yes, switch my home teleport.",
                        "No i'll keep the "+HomeTeleport.VALUES[((Number)player.getAttributeOrDefault("HOME_TELEPORT", 0)).intValue()].getName()+" home teleport.")
                        .onOptionOne(() -> {
                            setKey(3);
                            player.getAttributes().put("HOME_TELEPORT", tp.ordinal());
                        })
                        .onOptionTwo(() -> setKey(5));
                npc(3, "I have changed your home teleport to "+tp.getName()+".");
                npc(5, "Come to see me if you want to change your home teleport again.");
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 16060 };
    }
}
