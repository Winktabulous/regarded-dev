package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Tommeh | 24 apr. 2018 | 17:27:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DestroyItemMessage implements Message {
	
    private final Map<Integer, Runnable> optionActions = new HashMap<Integer, Runnable>();

    private final Item item;
    private final String title, message;
    private Runnable runnable;

    public DestroyItemMessage(final Item item, final String title, final String message) {
        this.item = item;
        this.message = message;
        this.title = title;
    }

    public DestroyItemMessage(final Item item, final String message) {
        this(item, "Are you sure you want to destroy this item?", message);
    }
    
    public DestroyItemMessage onNo(final Runnable runnable) {
    	optionActions.put(0, runnable);
    	return this;
    }
    
    public DestroyItemMessage onYes(final Runnable runnable) {
    	optionActions.put(1, runnable);
    	return this;
    }
    
    
    public void onClick(final Player player, final int option) {
    	final Runnable action = optionActions.get(option);
    	if (action == null) {
			return;
		}
    	action.run();
    }

    @Override
    public void display(final Player player) {
    	player.getInterfaceHandler().sendInterface(InterfacePosition.DIALOGUE, 584);
		player.getPacketDispatcher().sendClientScript(814, item.getId(), item.getAmount() <= 1 ? -1 :
                item.getAmount(), title, message);
		player.getPacketDispatcher().sendComponentSettings(584, 0, 0, 1, AccessMask.CONTINUE);
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