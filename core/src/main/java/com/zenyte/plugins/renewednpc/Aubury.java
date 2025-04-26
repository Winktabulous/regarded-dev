package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary;
import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary;
import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 26/11/2018 19:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Aubury extends NPCPlugin {

    private static final Location CAVE_LOCATION = new Location(2911, 4832, 0);

    private static final ForceTalk TELEPORT_FORCETALK = new ForceTalk("Senventior Disthine Molenko!");

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
// TODO - Open up Aubury's Shop via right-click trade
            @Override
            public void buildDialogue() {
                npc("Do you want to buy some runes?");
                options(TITLE, "Yes please!", "Oh, it's a rune shop. No thank you, then.", "Can you teleport me to " + "the Rune Essence?").onOptionOne(() -> player.openShop("Aubury's Rune Shop")).onOptionTwo(() -> setKey(5)).onOptionThree(() -> setKey(10));
                player(5, "Oh, it's a rune shop. No thank you, then.");
                npc("Well, if you find someone who does want runes, please<br><br>send them my way.");
                player(10, "Can you teleport me to the Rune Essence?").executeAction(() -> Aubury.teleport(player, npc));
            }
        }));
        bind("Teleport", Aubury::teleport);
    }

    public static void teleport(final Player player, final NPC npc) {
        player.lock();
        npc.faceEntity(player);
        npc.setGraphics(CombatSpell.CURSE.getCastGfx());
        npc.setForceTalk(TELEPORT_FORCETALK);
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            @Override
            public void run() {
                switch(ticks++) {
                    case 0:
                        World.sendProjectile(npc, player, CombatSpell.CURSE.getProjectile());
                        break;
                    case 1:
                        player.setGraphics(CombatSpell.CURSE.getHitGfx());
                        break;
                    case 2:
                        if (npc.getId() == NpcId.AUBURY_11435 || npc.getId() == 2886) {
                            player.getAchievementDiaries().update(VarrockDiary.TELEPORT_TO_ESSENCE_MINE);
                        } else if (npc.getId() == 5314) {
                            player.getAchievementDiaries().update(ArdougneDiary.TELEPORT_RUNE_ESSENCE_MINE);
                        } else if (npc.getId() == 5034) {
                            player.getAchievementDiaries().update(LumbridgeDiary.TELEPORT_TO_ESSENCE_MINE);
                        } else if (npc.getId() == NpcId.BRIMSTAIL_11431 || npc.getId() == 4913) {
                            player.getAchievementDiaries().update(WesternProvincesDiary.TELEPORT_ESSENCE_MINE);
                        }
                        player.setLocation(CAVE_LOCATION);
                        player.unlock();
                        stop();
                        break;
                }
            }
        }, 0, 1);
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.AUBURY_11435 };
    }
}
