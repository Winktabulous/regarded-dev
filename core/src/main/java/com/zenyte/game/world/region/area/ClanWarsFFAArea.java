package com.zenyte.game.world.region.area;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.skills.construction.CombatDummyNPC;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import com.zenyte.plugins.events.ServerLaunchEvent;

/**
 * @author Kris | 02/05/2019 23:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ClanWarsFFAArea extends PolygonRegionArea implements RandomEventRestrictionPlugin, DeathPlugin {

    @Subscribe
    public static void onServerLaunched(ServerLaunchEvent event) {
        World.spawnObject(new WorldObject(ObjectId.BANK_CHEST, 10, 0, new Location(3326, 4753)));
        World.spawnObject(new WorldObject(50081, 10, 0, new Location( 3324, 4754)));
        World.spawnObject(new WorldObject(24911, 10, 0, new Location( 3331, 4754)));
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        { 3264, 4864 },
                        { 3264, 4736 },
                        { 3392, 4736 },
                        { 3392, 4864 }
                })
        };
    }

    @Override
    public void enter(final Player player) {

    }

    @Override
    public void leave(final Player player, final boolean logout) {
        for (int i = 0; i < SkillConstants.SKILLS.length; i++) {
            if (player.getSkills().getLevel(i) < player.getSkills().getLevelForXp(i)) {
                player.getSkills().setLevel(i, player.getSkills().getLevelForXp(i));
            }
        }
        player.blockIncomingHits();
        player.getCombatDefinitions().setSpecialEnergy(100);
        player.getVariables().setRunEnergy(100);
        player.getToxins().reset();
        player.getVariables().resetScheduled();
        player.getPrayerManager().deactivateActivePrayers();
        player.resetFreeze();
    }

    @Override
    public String name() {
        return "Clan Wars: FFA";
    }

    @Override
    public boolean isSafe() {
        return true;
    }

    @Override
    public String getDeathInformation() {
        return "Deaths within the free-for-all zone are always safe.";
    }

    @Override
    public Location getRespawnLocation() {
        return new Location(3361, 3156, 0);
    }
}
