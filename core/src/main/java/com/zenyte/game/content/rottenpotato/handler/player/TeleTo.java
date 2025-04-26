package com.zenyte.game.content.rottenpotato.handler.player;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.rottenpotato.handler.PlayerRottenPotatoActionHandler;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

import java.util.Optional;

public class TeleTo implements PlayerRottenPotatoActionHandler {
    @Override
    public void execute(Player user, Player target) {
        if (!user.getPrivilege().eligibleTo(PlayerPrivilege.DEVELOPER)) {
            final Optional<Raid> raid = target.getRaid();
            if (raid.isPresent() && !user.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
                user.sendMessage("You cannot teleport to a player in a raid as a non-administrator.");
                return;
            }
            if (target.getArea() instanceof Inferno && !user.getPrivilege().eligibleTo(PlayerPrivilege.DEVELOPER)) {
                user.sendMessage("You cannot teleport to a player in the Inferno.");
                return;
            }
        }
        target.log(LogLevel.INFO, user.getName() + " force teleported to you at " + target.getLocation() + ".");
        user.setLocation(target.getLocation());
    }

    @Override
    public String option() {
        return "Teleport to player.";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.MODERATOR;
    }
}
