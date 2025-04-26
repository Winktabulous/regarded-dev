package com.zenyte.game.content.tog;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.tog.juna.JunaEnterDialogue;
import com.zenyte.game.content.tog.juna.JunaOutsideOptionDialogue;
import com.zenyte.game.model.RuneDate;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.GameCommands;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.plugins.events.ServerLaunchEvent;

public final class TearsOfGuthixServerLaunchSubscriber {

    @Subscribe
    public static void onServerStart(ServerLaunchEvent e) {
        new GameCommands.Command(PlayerPrivilege.DEVELOPER, "resettog", (p, args) ->
                p.sendInputName("Whose " + "Tears" + " of Guthix restriction to remove?",
                        name -> World.getPlayer(name).ifPresent(targetPlayer ->
                                targetPlayer.getAttributes().remove(JunaEnterDialogue.LAST_ATTEMPT_DATE_ATTR))));

        RuneDate.addOnCheckDate(player -> {
            if (JunaEnterDialogue.hasTimePassed(player) && player.getBooleanAttribute(JunaOutsideOptionDialogue.TOG_REMINDER_ENABLED_ATTR)) {
                player.sendMessage(Colour.RED.wrap("You are eligible to drink from the Tears of Guthix."));
            }
        });
    }

}
