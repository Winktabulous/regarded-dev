package com.zenyte.game.content.event.halloween2019;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 10/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GrimReaperNPC extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                if (HalloweenUtils.isCompleted(player)) {
                    return;
                }
                player.getDialogueManager().start(new Dialogue(player, npc.getId()) {
                    @Override
                    public void buildDialogue() {
                        npc("Hello, " + player.getName() + ". I've come here in regards to this year's Halloween.");
                        npc("Suspiciously enough, nothing has happened in the past couple of weeks. I had these rewards already created in anticipation for the worst, but" +
                                " I suppose I can give them off to you for free now.").executeAction(() -> {
                                    if (player.getInventory().getFreeSlots() >= 5) {
                                        for (int i = 9921; i <= 9925; i++) {
                                            player.getInventory().addOrDrop(new Item(i));
                                            player.getTrackedHolidayItems().add(i);
                                        }
                                        player.addAttribute("Halloween event 2019", 1);
                                        player.getVarManager().sendBit(1000, 1);
                                        player.getVarManager().sendVar(HalloweenUtils.COMPLETED_VARP, 1);
                                        player.getPacketDispatcher().sendGraphics(new Graphics(86, 0, 90), new Location(3080, 3501, 0));
                                    }
                        });
                        if (player.getInventory().getFreeSlots() < 5) {
                            npc("However, it looks like you don't have enough space to hold all these items. Come talk to me when you've made room for at least five items.");
                            return;
                        }
                        plain("The Grim Reaper gives you the skeleton outfit and teaches you a couple emotes. He vanishes right after without a trace.");
                        plain("Congratulations! You have unlocked the 'Zombie Hand' and 'Trick' emotes!");
                    }
                });
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                //HalloweenNPC.GRIM_REAPER_BASE.getRepackedNPC(), HalloweenNPC.GRIM_REAPER.getRepackedNPC()
        };
    }
}
