package com.zenyte.game.world.region.area;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.PrayerPlugin;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.TeleportPlugin;
import com.zenyte.game.world.region.area.plugins.TradePlugin;
import com.zenyte.plugins.renewednpc.ZenyteGuide;

/**
 * @author Tommeh | 1-2-2019 | 16:03
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RegisterIslandArea extends PolygonRegionArea implements TeleportPlugin, TradePlugin, PrayerPlugin, RandomEventRestrictionPlugin {

    public static final Location ZENYTE_GUIDE_LOCATION = new Location(3362, 7206, 1);

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(13424)};
    }

    @Override
    public void enter(Player player) {
        World.findNPC(ZenyteGuide.NPC_ID, ZENYTE_GUIDE_LOCATION, 10)
                .ifPresent(npc -> player.getPacketDispatcher().sendHintArrow(new HintArrow(npc)));
        WorldTasksManager.schedule(() -> GameInterface.CHARACTER_DESIGN.open(player), 1);
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Register Island";
    }

    @Override
    public boolean canTeleport(final Player player, final Teleport teleport) {
        return false;
    }

    @Override
    public boolean canTrade(final Player player, final Player partner) {
        return false;
    }

    @Override
    public boolean activatePrayer(final Player player, final Prayer prayer) {
        return false;
    }

}

