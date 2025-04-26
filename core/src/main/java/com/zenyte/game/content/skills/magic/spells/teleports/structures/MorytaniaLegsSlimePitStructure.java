package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 26-4-2019 | 19:54
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MorytaniaLegsSlimePitStructure implements TeleportStructure {

    private static final Graphics graphics = new Graphics(1236, 0, 92);
    private static final Animation animation = new Animation(3874);

    @Override
    public Animation getStartAnimation() {
        return animation;
    }

    @Override
    public Graphics getStartGraphics() {
        return graphics;
    }

    @Override
    public void end(Player player, Teleport teleport) {
        TeleportStructure.super.end(player, teleport);
        if (player.getTemporaryAttributes().remove("slime pit restricted teleport") != null) {
            final int teleports = player.getVariables().getSlimePitTeleports();
            final int limit = player.getNumericTemporaryAttribute("slime pit teleport limit").intValue();
            player.sendMessage(Colour.RED.wrap("You have used up " + teleports + "/" + limit + " of your Slime Pit teleports for today."));
        }
    }
}

