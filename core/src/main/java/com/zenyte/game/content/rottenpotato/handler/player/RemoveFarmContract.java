package com.zenyte.game.content.rottenpotato.handler.player;

import com.zenyte.game.content.rottenpotato.handler.PlayerRottenPotatoActionHandler;
import com.zenyte.game.content.skills.farming.contract.FarmingContract;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;

public class RemoveFarmContract implements PlayerRottenPotatoActionHandler {

    @Override
    public void execute(Player user, Player target) {
        target.getAttributes().remove(FarmingContract.CONTRACT_ATTR);
        target.getAttributes().remove(FarmingContract.CONTRACT_DIFFICULTY_ATTR);
        target.putBooleanAttribute(FarmingContract.COMPLETED_ATTR, false);
        target.log(LogLevel.INFO, "Farming contracted cleared by " + user.getName() + ".");
        user.sendMessage("Successfully cleared farming contract for " + Colour.RED.wrap(target.getUsername()) + ".");
    }

    @Override
    public String option() {
        return "Clear farming contract";
    }

    @Override
    public PlayerPrivilege getPrivilege() {
        return PlayerPrivilege.ADMINISTRATOR;
    }
}
