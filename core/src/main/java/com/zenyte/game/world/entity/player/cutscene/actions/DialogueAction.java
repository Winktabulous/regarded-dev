package com.zenyte.game.world.entity.player.cutscene.actions;

import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 18/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DialogueAction implements Runnable {

    private final Dialogue dialogue;
    private boolean finished;

    public DialogueAction(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    @Override
    public void run() {
        dialogue.getPlayer().getDialogueManager().start(dialogue);
        dialogue.setOnCloseRunnable(() -> finished = true);
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
