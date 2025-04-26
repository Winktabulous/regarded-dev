package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 23/06/2019 15:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GhostDisciple extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                final boolean eligible = DiaryUtil.eligibleFor(DiaryReward.MORYTANIA_LEGS1, player) && DiaryUtil.eligibleFor(DiaryReward.MORYTANIA_LEGS2, player) && DiaryUtil.eligibleFor(DiaryReward.MORYTANIA_LEGS3, player);
                if (!eligible) {
                    player("Hello. I'd like to claim my ecto-tokens.");
                    final int tokens = player.getNumericAttribute("ecto-tokens").intValue();
                    if (tokens == 0) {
                        npc("You haven't got any unclaimed ecto-tokens right now.");
                    } else {
                        npc("Of course, here you go.").executeAction(() -> {
                            player.getInventory().addOrDrop(new Item(4278, tokens));
                            player.addAttribute("ecto-tokens", 0);
                        });
                        item(new Item(4278, tokens), "The ghost disciple hands you " + tokens + " ecto-tokens.");
                    }
                } else {
                    options(TITLE, new DialogueOption("Claim ecto-tokens.", key(5)), new DialogueOption("Can I have another bonecrusher, please?", key(100)));
                    player(5, "Hello. I'd like to claim my ecto-tokens.");
                    final int tokens = player.getNumericAttribute("ecto-tokens").intValue();
                    if (tokens == 0) {
                        npc("You haven't got any unclaimed ecto-tokens right now.");
                    } else {
                        npc("Of course, here you go.").executeAction(() -> {
                            player.getInventory().addOrDrop(new Item(4278, tokens));
                            player.addAttribute("ecto-tokens", 0);
                        });
                        item(new Item(4278, tokens), "The ghost disciple hands you " + tokens + " ecto-tokens.");
                    }
                    player(100, "Can I have another bonecrusher, please?");
                    if (player.containsItem(ItemId.BONECRUSHER) || player.containsItem(ItemId.BONECRUSHER_NECKLACE)) {
                        npc("You already appear to own such a device, mortal.");
                        return;
                    }
                    npc("Of course, here you go.").executeAction(() -> player.getInventory().addOrDrop(new Item(13116)));
                    item(new Item(13116), "The ghost disciple hands you a bonecrusher.");
                }
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.GHOST_DISCIPLE };
    }
}
