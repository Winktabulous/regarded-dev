package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 04/10/2019 | 15:23
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class WesternBannerFishingColonyStructure implements TeleportStructure {

    private static final Animation START_ANIM = new Animation(714);
    private static final Graphics START_GFX = new Graphics(111, 0, 70);

    @Override
    public Animation getStartAnimation() {
        return START_ANIM;
    }

    @Override
    public Graphics getStartGraphics() {
        return START_GFX;
    }

    @Override
    public void end(Player player, Teleport teleport) {
        TeleportStructure.super.end(player, teleport);
        if (player.getTemporaryAttributes().remove("fishing colony restricted teleport") != null) {
            final int teleports = player.getVariables().getFishingColonyTeleports();
            final int limit = player.getNumericTemporaryAttribute("fishing colony teleport limit").intValue();
            player.sendMessage(Colour.RED.wrap("You have used up " + teleports + "/" + limit + " of your Fishing Colony teleports for today."));
        }
    }

}