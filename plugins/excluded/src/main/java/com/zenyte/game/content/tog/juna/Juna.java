package com.zenyte.game.content.tog.juna;

import com.zenyte.game.content.tog.TearsOfGuthixCaveArea;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Chris
 * @since September 07 2020
 */
public class Juna implements ObjectAction {
    public static final WorldObject JUNA_OBJECT = new WorldObject(3193, 10, 1, 3252, 9516, 2);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (option.toLowerCase()) {
            case "talk-to":
                Dialogue greetingDialogue = player.inArea(TearsOfGuthixCaveArea.class) ? new JunaInsideGreetingDialogue(player, NpcId.JUNA)
                        : new JunaOutsideGreetingDialogue(player, NpcId.JUNA);
                player.getDialogueManager().start(greetingDialogue);
                break;
            case "story":
                player.getDialogueManager().start(new JunaEnterDialogue(player, NpcId.JUNA));
                break;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{JUNA_OBJECT.getId()};
    }
}
