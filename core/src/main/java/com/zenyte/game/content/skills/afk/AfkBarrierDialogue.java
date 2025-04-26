package com.zenyte.game.content.skills.afk;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.utils.TimeUnit;

public class AfkBarrierDialogue extends Dialogue {

    public AfkBarrierDialogue(Player player) {
        super(player, NpcId.THE_FISHER_KING);
    }

    @Override
    public void buildDialogue() {
        if(!AfkSkilling.hasAfkTime(player)) {
            npc("You don't have any time left in the guild. You can always ::vote to extend it, but you can also purchase it - here are the options...");
        } else {
            npc("You currently have "+ Utils.getTimeTicks(TimeUnit.MILLISECONDS.toTicks(AfkSkilling.getAfkTime(player) - System.currentTimeMillis()), false) + "remaining. You can always do ::vote to extend it, but you can also purchase it - here are the options...");
        }
        options("Select your extension for the AFK Guild",
                "+<col=000080>1</col> hour - <col=000080>1</col> Vote Point",
                "+<col=000080>12</col> Hours - <col=000080>10</col> Vote Points (<col=000080>save 20%</col>)",
                "+<col=000080>24</col> Hours - <col=000080>18</col> Vote Points (<col=000080>save 33%</col>)",
                "+<col=000080>48</col> Hours - <col=000080>34</col> Vote Points (<col=000080>save 41%</col>)")
                .onOptionOne(() -> AfkSkilling.buyTime(player, 1, 1))
                .onOptionTwo(() -> AfkSkilling.buyTime(player, 10, 12))
                .onOptionThree(() -> AfkSkilling.buyTime(player, 18, 24))
                .onOptionFour(() -> AfkSkilling.buyTime(player, 34, 48));
    }


}
