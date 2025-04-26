package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;

/**
 * Created by Arham 4 on 2/15/2016.
 * <p>
 * Represents a singular message with a Player talking.
 */
public class PlayerMessage implements Message {

    private final Expression expression;
    private final String message;
    private Runnable runnable;
    private boolean cantContinue;

    public PlayerMessage(final Expression expression, final String message) {
        this.expression = expression;
        this.message = message;
    }

    public void setCantContinue() {
    	cantContinue = true;
	}

    /**
     * DO NOT OVERRIDE
     */
    @Override
    public void display(final Player player) {
    	player.getInterfaceHandler().sendInterface(InterfacePosition.DIALOGUE, 217);
		player.getPacketDispatcher().sendClientScript(600, 1, 1, 16, 14221318);
		player.getPacketDispatcher().sendComponentPlayerHead(217, 2);
		player.getPacketDispatcher().sendComponentText(217, 4, player.getName());
		if (cantContinue) {
			player.getPacketDispatcher().sendComponentVisibility(217, 5, true);
		} else {
			player.getPacketDispatcher().sendComponentText(217, 5, continueMessage(player));
		}
		player.getPacketDispatcher().sendComponentText(217, 6, message);
		player.getPacketDispatcher().sendComponentAnimation(217, 2, expression.getId());
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