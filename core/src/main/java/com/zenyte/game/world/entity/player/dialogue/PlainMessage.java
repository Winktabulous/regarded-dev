package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;

/**
 * Created by admin on 2/15/2016.
 * <p>
 * Represents a plain message, with no entities in the dialogue.
 */
public class PlainMessage implements Message {

    private final String message;
    private Runnable runnable;
    private final boolean showContinue;
    private final int lineSpacing;

    public PlainMessage(final String message, final boolean showContinue) {
        this(message, showContinue, 0);
    }

    public PlainMessage(final String message, final boolean showContinue, final int lineSpacing) {
        this.message = message;
        this.showContinue = showContinue;
        this.lineSpacing = lineSpacing;
    }

    @Override
    public final void display(final Player player) {
    	player.getInterfaceHandler().sendInterface(InterfacePosition.DIALOGUE, 229);
		player.getPacketDispatcher().sendComponentText(229, 1, message);
		player.getPacketDispatcher().sendClientScript(600, 1, 1, lineSpacing, 15007745);
		player.getPacketDispatcher().sendComponentSettings(229, 2, -1, -1, AccessMask.CONTINUE);
        player.getPacketDispatcher().sendComponentText(229, 2, showContinue ? continueMessage(player) : "");
		player.getPacketDispatcher().sendComponentVisibility(229, 2, !showContinue);
    }

	@Override
	public void executeAction(final Runnable runnable) {
		this.runnable = runnable;
	}
	
	@Override
	public void execute(final Player player) {
		if (runnable != null) {
			runnable.run();
		}
	}

}