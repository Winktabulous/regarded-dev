package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 04/10/2019 | 15:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class DesertAmuletNardahStructure implements TeleportStructure {

    private static final Graphics graphics = new Graphics(284, 0, 92);
    private static final Animation animation = new Animation(3872);

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
        if (player.getTemporaryAttributes().remove("nardah restricted teleport") != null) {
            player.sendMessage(Colour.RED.wrap("You have used up your daily Nardah teleport for today."));
        }
    }
}