package com.zenyte.game.content.kebos.npcs;

import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Tommeh | 16/11/2019 | 21:16
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class Tyss extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Greetings stranger. How can I help you?");
                    options(TITLE, "Change my spellbook", "Nevermind").onOptionOne(() -> setKey(5));
                    npc(5, "As you wish.").executeAction(() -> {
                        final Spellbook spellbook = player.getCombatDefinitions().getSpellbook();
                        if (spellbook.equals(Spellbook.ARCEUUS)) {
                            player.getCombatDefinitions().setSpellbook(Spellbook.NORMAL, true);
                        } else {
                            player.getCombatDefinitions().setSpellbook(Spellbook.ARCEUUS, true);
                            player.getAchievementDiaries().update(KourendDiary.SWITCH_TO_NECROMANCY_SPELLBOOK);
                        }
                    });
                }
            });
        });
        bind("Spellbook", (player, npc) -> {
            final Spellbook spellbook = player.getCombatDefinitions().getSpellbook();
            if (spellbook.equals(Spellbook.ARCEUUS)) {
                player.getCombatDefinitions().setSpellbook(Spellbook.NORMAL, true);
            } else {
                player.getCombatDefinitions().setSpellbook(Spellbook.ARCEUUS, true);
                player.getAchievementDiaries().update(KourendDiary.SWITCH_TO_NECROMANCY_SPELLBOOK);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.TYSS };
    }
}
