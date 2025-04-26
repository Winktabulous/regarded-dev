package com.zenyte.game.content.event.easter2020.area;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import com.zenyte.plugins.SkipPluginScan;

import java.util.Arrays;
import java.util.List;

/**
 * @author Corey
 * @since 26/03/2020
 */
@SkipPluginScan
public class RabbitWarrenArea extends PolygonRegionArea implements RandomEventRestrictionPlugin, CannonRestrictionPlugin {
    
    private static final Location[] critterSpawns = new Location[]{
            new Location(2228, 4298),
            new Location(2227, 4305),
            new Location(2222, 4306),
            new Location(2224, 4309),
            new Location(2226, 4313),
            new Location(2230, 4311),
            new Location(2232, 4308),
            new Location(2217, 4316),
            new Location(2214, 4313),
            new Location(2208, 4312),
            new Location(2203, 4312),
            new Location(2196, 4309),
            new Location(2199, 4306),
            new Location(2203, 4305),
            new Location(2206, 4306),
            new Location(2212, 4307),
            new Location(2216, 4308),
            new Location(2219, 4308),
            new Location(2211, 4302),
            new Location(2217, 4301),
            new Location(2203, 4300),
            new Location(2197, 4302),
            new Location(2195, 4298),
            new Location(2198, 4298),
            new Location(2192, 4302),
            new Location(2192, 4309),
            new Location(2188, 4311),
            new Location(2184, 4310),
            new Location(2184, 4315),
            new Location(2185, 4323),
            new Location(2184, 4326),
            new Location(2183, 4332),
            new Location(2189, 4326),
            new Location(2190, 4333),
            new Location(2186, 4341),
            new Location(2191, 4339),
            new Location(2196, 4337),
            new Location(2195, 4333),
            new Location(2196, 4327),
            new Location(2194, 4321),
            new Location(2190, 4318),
            new Location(2200, 4318),
            new Location(2195, 4317),
            new Location(2234, 4317),
            new Location(2228, 4317),
            new Location(2222, 4320),
            new Location(2225, 4323),
            new Location(2231, 4328),
            new Location(2226, 4329),
            new Location(2219, 4326),
            new Location(2223, 4337),
            new Location(2229, 4343),
            new Location(2227, 4338),
            new Location(2218, 4345),
            new Location(2221, 4343),
            new Location(2219, 4338),
            new Location(2215, 4336),
            new Location(2222, 4331),
            new Location(2216, 4329),
            new Location(2214, 4323),
            new Location(2209, 4318),
            new Location(2200, 4329),
            new Location(2210, 4331),
            new Location(2211, 4337),
            new Location(2201, 4339)
    };
    
    private static final List<GameInterface> closedTabs = Arrays.asList(
            GameInterface.COMBAT_TAB,
            GameInterface.SKILLS_TAB,
            GameInterface.JOURNAL_HEADER_TAB,
            GameInterface.INVENTORY_TAB,
            GameInterface.EQUIPMENT_TAB,
            GameInterface.PRAYER_TAB_INTERFACE,
            GameInterface.SPELLBOOK,
            GameInterface.EMOTE_TAB,
            GameInterface.GAME_NOTICEBOARD);
    
    public RabbitWarrenArea() {
        for (Location spawn : critterSpawns) {
            World.spawnNPC(Utils.random(EasterConstants.RAT_ID, EasterConstants.SNAIL_ID), spawn, Direction.SOUTH, 7);
        }
    }
    
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{
                new RSPolygon(new int[][]{
                        {2176, 4288},
                        {2176, 4352},
                        {2240, 4352},
                        {2240, 4288}
                })
        };
    }
    
    @Override
    public void enter(Player player) {
        if (!player.getPrivilege().eligibleTo(PlayerPrivilege.DEVELOPER)) {
            player.lock(2);
            player.setLocation(new Location(3088, 3474, 0));
            return;
        }
        player.setViewDistance(Player.SCENE_DIAMETER);
        player.setRunSilent(true);
        player.getToxins().reset();
        player.getAppearance().setNpcId(EasterConstants.PLAYER_BUNNY_NPC);
        player.getAppearance().resetRenderAnimation();
        closedTabs.forEach(tab -> player.getInterfaceHandler().closeInterface(tab));
    }
    
    @Override
    public void leave(Player player, boolean logout) {
        player.resetViewDistance();
        player.setRunSilent(false);
        player.getAppearance().setNpcId(-1);
        player.getAppearance().resetRenderAnimation();
        closedTabs.forEach(tab -> tab.open(player));
    }
    
    @Override
    public String name() {
        return "Rabbit Warren";
    }
}
