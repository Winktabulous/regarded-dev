package com.zenyte.game.content.rottenpotato.handler.player;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.rottenpotato.handler.PlayerRottenPotatoActionHandler;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

import java.util.Optional;

public class TeleToMe implements PlayerRottenPotatoActionHandler {
    @Override
    public void execute(Player user, Player target) {
        if (!user.getPrivilege().eligibleTo(PlayerPrivilege.DEVELOPER)) {
            final Optional<Raid> raid = target.getRaid();
            if (raid.isPresent() && !target.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
                user.sendMessage("You cannot teleport non-administrators into a raid.");
                return;
            }
            if (user.getArea() instanceof Inferno && !target.getPrivilege().eligibleTo(PlayerPrivilege.DEVELOPER)) {
                user.sendMessage("You cannot teleport a player into the Inferno.");
                return;
            }
        }
        target.log(LogLevel.INFO, "Force teleported by " + user.getName() + " to " + user.getLocation() + ".");
        target.setLocation(user.getLocation());
    }

    @Override
    public String option() {
        return "Teleport player to you.";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.MODERATOR;
    }
}
