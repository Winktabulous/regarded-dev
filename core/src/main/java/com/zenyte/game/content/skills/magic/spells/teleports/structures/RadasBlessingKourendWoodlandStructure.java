package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 17/11/2019 | 15:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class RadasBlessingKourendWoodlandStructure implements TeleportStructure {

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
        if (player.getTemporaryAttributes().remove("kourend woodland restricted teleport") != null) {
            final int teleports = player.getVariables().getKourendWoodlandTeleports();
            final int limit = player.getNumericTemporaryAttribute("kourend woodland teleport limit").intValue();
            player.sendMessage(Colour.RED.wrap("You have used up " + teleports + "/" + limit + " of your Kourend Woodland teleports for today."));
        }
    }

}
