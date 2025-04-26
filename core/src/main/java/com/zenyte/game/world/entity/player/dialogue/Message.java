package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.world.entity.player.Player;

/**
 * Created by Arham 4 on 2/15/2016.
 */
public interface Message {

    default void executeAction(final Runnable runnable) {
    	
    }

    void display(Player player);

	default void execute(final Player player) {
		
	}
    
    default String continueMessage(final Player player) {
        return player.isOnMobile() ? "Tap here to continue" : "Click here to continue";
    }
	
}