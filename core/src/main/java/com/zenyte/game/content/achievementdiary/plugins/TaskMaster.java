package com.zenyte.game.content.achievementdiary.plugins;

import com.zenyte.game.content.achievementdiary.AchievementDiaries;
import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 27/11/2018 11:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TaskMaster extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new TaskmasterD(player, npc)));
        bind("Bars", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                boolean on = player.getBooleanAttribute("noted_bars_metalic_dragons");
                npc("Do you want to " + (on ? "disable" : "enable") + " noted metal bars from metalic dragons in the Brimhaven Dungeon?");
                options(TITLE, "Yes.", "Nevermind.")
                        .onOptionOne(() -> {
                            player.toggleBooleanAttribute("noted_bars_metalic_dragons");
                            player.sendMessage(on ? "You will no longer receive bars in noted form from metalic dragons in the Brimhaven Dungeon." : "From now on, bars from metalic dragons in the Brimhaven Dungeon will be noted.");
                        });
            }
        }));
    }

    @Override
    public int[] getNPCs() {
        IntArrayList list = new IntArrayList();
        for (Diary[] diary : AchievementDiaries.ALL_DIARIES) {
            list.add(diary[0].taskMaster());
        }
        return list.toIntArray();
    }
}
