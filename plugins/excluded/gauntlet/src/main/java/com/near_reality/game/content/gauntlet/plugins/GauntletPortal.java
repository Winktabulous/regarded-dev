package com.near_reality.game.content.gauntlet.plugins;

import com.near_reality.game.content.commands.DeveloperCommands;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

public final class GauntletPortal implements ObjectAction {

    private static final int GAUNTLET_PORTAL = 36081;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {

        if (!DeveloperCommands.INSTANCE.getEnabledGauntlet()){
            player.getDialogueManager().start(new PlainChat(player, "Gauntlet is currently disabled."));
            return;
        }

        player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 641);
        player.getPacketDispatcher().sendClientScript(2921);

        WorldTasksManager.schedule(() -> {
            player.setLocation(new Location(3030, 6128, 1));
            player.getPacketDispatcher().sendClientScript(2922);
        }, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { GAUNTLET_PORTAL };
    }

}
