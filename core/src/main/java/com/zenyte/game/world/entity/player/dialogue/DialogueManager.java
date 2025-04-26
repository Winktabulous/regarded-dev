package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.OptionsMenuD;

/**
 * @author Kris | 30. aug 2018 : 22:02:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class DialogueManager {

    private final Player player;

    private Dialogue lastDialogue;

    public DialogueManager(final Player player) {
        this.player = player;
    }

    public void next() {
        if (lastDialogue == null) {
            return;
        }
        lastDialogue.communicateNext();
    }

    public void start(final Dialogue dialogue) {
        finish();
        lastDialogue = dialogue;
        try {
            dialogue.buildDialogue();
            if (dialogue.getOnBuild() != null) {
                dialogue.getOnBuild().run();
            }
		} catch (final Exception e) {
			e.printStackTrace();
			finish();
			return;
		}
		if (dialogue.dialogue.get(1) == null) {
			return;
		}
		dialogue.setKey(1);
		dialogue.communicate();
	}

    public void item(final Item item, final String message) {
        start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                item(item, message);
            }
        });
    }

	public void finish() {
		if (lastDialogue == null) {
			return;
		}
		if (lastDialogue.getOnCloseRunnable() != null) {
			lastDialogue.getOnCloseRunnable().run();
		}
		lastDialogue = null;
        player.getInterfaceHandler().closeInterface(InterfacePosition.DIALOGUE);
    }

    public void onMenuOption(final int slotId) {
        if (!(lastDialogue instanceof OptionsMenuD)) {
            return;
        }
        ((OptionsMenuD) lastDialogue).handleInterface(slotId);
    }

    public void onClick(final int componentId, final int slotId) {
        if (lastDialogue == null) {
            return;
        }
        lastDialogue.communicateNext(slotId, componentId);
    }

    public Dialogue getLastDialogue() {
        return lastDialogue;
    }

    public void setLastDialogue(Dialogue lastDialogue) {
        this.lastDialogue = lastDialogue;
    }

    public Player getPlayer() {
        return player;
    }

}
