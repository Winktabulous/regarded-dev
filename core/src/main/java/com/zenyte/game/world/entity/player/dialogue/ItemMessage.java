package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;

/**
 * Created by Arham 4 on 2/15/2016.
 * <p>
 * Represents a singular message with an Item in it.
 */
public class ItemMessage implements Message {

    private final int itemId;
    private final String message;
    private Runnable runnable;
	private final boolean showContinue;

    public ItemMessage(final Item item, final String message, boolean showContinue) {
        this.itemId = item.getId();
        this.message = message;
		this.showContinue = showContinue;
    }

	public ItemMessage(final int itemId, final String message) {
		this.itemId = itemId;
		this.message = message;
		this.showContinue = true;
	}

    /**
     * DO NOT OVERRIDE
     */
    @Override
    public void display(final Player player) {
    	player.getInterfaceHandler().sendInterface(InterfacePosition.DIALOGUE, 193);
		player.getPacketDispatcher().sendComponentItem(193, 1, itemId, 400);
		player.getPacketDispatcher().sendComponentText(193, 2, message);
		if (showContinue) {
			player.getPacketDispatcher().sendClientScript(2868, continueMessage(player));
			player.getPacketDispatcher().sendComponentSettings(193, 0, 0, 0, AccessMask.CONTINUE);
		} else {
			player.getPacketDispatcher().sendClientScript(2868, "");
		}
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