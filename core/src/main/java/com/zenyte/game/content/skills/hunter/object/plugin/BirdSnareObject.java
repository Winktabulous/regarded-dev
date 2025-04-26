package com.zenyte.game.content.skills.hunter.object.plugin;

import com.zenyte.game.content.skills.hunter.HunterUtils;
import com.zenyte.game.content.skills.hunter.actions.CheckPlacedTrap;
import com.zenyte.game.content.skills.hunter.actions.DismantlePlacedTrap;
import com.zenyte.game.content.skills.hunter.node.TrapType;
import com.zenyte.game.content.skills.hunter.object.HunterTrap;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 29/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BirdSnareObject implements HunterObjectPlugin {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final HunterTrap trap = HunterUtils.findTrap(TrapType.BIRD_SNARE, object, null).orElseThrow(RuntimeException::new);
        switch (option) {
        case "Check": 
            player.getActionManager().setAction(new CheckPlacedTrap(trap, false));
            break;
        case "Investigate": 
            //TODO: More of this.
            player.sendMessage("This trap hasn't been smoked.");
            break;
        case "Dismantle": 
            player.getActionManager().setAction(new DismantlePlacedTrap(trap));
            break;
        default: 
            throw new IllegalStateException(option);
        }
    }

    @Override
    public TrapType type() {
        return TrapType.BIRD_SNARE;
    }
}
