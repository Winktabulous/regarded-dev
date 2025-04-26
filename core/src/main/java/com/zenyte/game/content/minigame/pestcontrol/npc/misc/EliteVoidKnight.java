package com.zenyte.game.content.minigame.pestcontrol.npc.misc;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;

/**
 * @author Kris | 27/11/2018 11:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EliteVoidKnight extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", EliteVoidKnight::upgrade);
    }

    public static void upgrade(final Player player, final NPC npc) {
        Item voidPiece = null;
        for (int slot = 0; slot < 28; slot++) {
            final Item item = player.getInventory().getItem(slot);
            if (item == null) {
                continue;
            }
            if (item.getId() == 8839 || item.getId() == 8840) {
                voidPiece = item;
                break;
            }
        }
        upgrade(player, npc, voidPiece);
    }

    public static void upgrade(final Player player, final NPC npc, final Item voidPiece) {
        if (voidPiece == null) {
            player.getDialogueManager().start(new NPCChat(player, npc.getId(), "Hi, I'm the Elite Void Knight. Bring " + "me your void knight robes and I will upgrade them for 40 points."));
            return;
        }
//        if (!DiaryUtil.eligibleFor(DiaryReward.WESTERN_BANNER3, player)) {
//            player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You'll need to complete the hard " + "western provinces diary first in order to upgrade your void armour."));
//            return;
//        }
        final int points = player.getNumericAttribute("pest_control_points").intValue();
        if (points < 40) {
            player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You need at least 40 void knight points to upgrade your " + voidPiece.getName() + "."));
            return;
        }
        final Item elitePiece = new Item(voidPiece.getId() == 8839 ? 13072 : 13073);
        final int remainingPoints = points - 40;
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                options("Pay 40 Commendation points to upgrade it?", "Okay", "Cancel").onOptionOne(() -> {
                    player.addAttribute("pest_control_points", remainingPoints);
                    player.getCollectionLog().add(elitePiece);
                    player.getInventory().deleteItem(voidPiece);
                    player.getInventory().addItem(elitePiece);
                    setKey(5);
                });
                item(5, elitePiece, "The Elite Void Knight upgrades your armour.<br>Commendation points remaining: " + remainingPoints);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.ELITE_VOID_KNIGHT };
    }
}
