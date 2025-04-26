package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;

/**
 * @author Corey
 * @since 14/12/2019
 */
public class PartygoerEventAdvertiser extends NPC implements Spawnable {
    
    private static final ForceTalk[] messages = new ForceTalk[]{
            new ForceTalk("Merry Christmas! Merry Christmas, one and all!"),
            new ForceTalk("Good food... Fine wine... Just climb into the cupboard!"),
            new ForceTalk("The Queen of Snow invites you all to her Christmas gathering!"),
            new ForceTalk("Come one, come all, to the Fantastic Festive Feast at the Land of Snow.")
    };

    private static final Animation bellRingAnimation = new Animation(15083);
    
    private long chatDelay;
    
    public PartygoerEventAdvertiser(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }
    
    @Override
    public boolean validate(int id, String name) {
        return id == ChristmasConstants.PARTYGOER_EVENT_ADVERTISER;
    }
    
    @Override
    public void processNPC() {
        super.processNPC();
        if (chatDelay < System.currentTimeMillis()) {
            chatDelay = System.currentTimeMillis() + Utils.random(8000, 14000);
            setForceTalk(messages[Utils.random(messages.length - 1)]);
            setInvalidAnimation(bellRingAnimation);
        }
    }
    
}
