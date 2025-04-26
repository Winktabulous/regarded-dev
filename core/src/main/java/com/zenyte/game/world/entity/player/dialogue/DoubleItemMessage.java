package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;

/**
 * Created by Arham 4 on 2/15/2016.
 * <p>
 * Represents a singular message with two items in it.
 */
public class DoubleItemMessage implements Message {

    private final Item first, second;
    private final String message;
    private Runnable runnable;

    public DoubleItemMessage(final Item first, final Item second, final String message) {
        this.first = first;
        this.second = second;
        this.message = message;
    }

    /**
     * DO NOT OVERRIDE
     */
    @Override
    public void display(final Player player) {
    	player.getInterfaceHandler().sendInterface(InterfacePosition.DIALOGUE, 11);
		player.getPacketDispatcher().sendComponentSettings(11, 4, -1, -1, AccessMask.CONTINUE);
		player.getPacketDispatcher().sendComponentItem(11, 1, first.getId(), 400);
		player.getPacketDispatcher().sendComponentItem(11, 3, second.getId(), 400);
		player.getPacketDispatcher().sendComponentText(11, 2, message);
        player.getPacketDispatcher().sendComponentText(11, 4, continueMessage(player));
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