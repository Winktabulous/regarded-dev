package com.zenyte.game.world.region.area;

import com.zenyte.game.content.event.christmas2019.AChristmasWarble;
import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.content.follower.impl.MiscPet;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 13/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LandOfSnowArea extends PolygonRegionArea {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                new RSPolygon(new int[][]{
                        {2048, 5440},
                        {2048, 5376},
                        {2112, 5376},
                        {2112, 5440}
                }), new RSPolygon(new int[][]{
                        {2432, 5440},
                        {2432, 5376},
                        {2496, 5376},
                        {2496, 5440}
                })
        };
    }

    public static final void spawnPet(@NotNull final Player player) {
        //Just in case, not to override the player's own pet.
        if (player.getFollower() != null) {
            return;
        }
        player.setFollower(new Follower(MiscPet.AREA_LOCKED_SNOW_IMP.petId(), player));
    }

    @Override
    public void enter(Player player) {
        /*if (!player.getPrivilege().eligibleTo(PlayerPrivilege.DEVELOPER)) {
            player.lock(2);
            player.setLocation(new Location(3091, 3503, 0));
            return;
        }*/
        player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 167);
    
        //If further enough in quest condition; use static method above to spawn the pet once the player progresses to that point in area.
        if (AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.SPOKEN_TO_PERSONAL_IMP)) {
            spawnPet(player);
        }
    }

    @Override
    public void leave(Player player, boolean logout) {
        if (player.getFollower() != null && player.getFollower().getPet() == MiscPet.AREA_LOCKED_SNOW_IMP) {
                player.setFollower(null);
        }
        player.getInventory().deleteItems(new Item(ChristmasConstants.BEDSHEETS_ID, 28), new Item(ChristmasConstants.CHAINS_ID, 28), new Item(ChristmasConstants.MULLED_WINE, 28),
                new Item(ChristmasConstants.YULE_LOG, 28), new Item(ChristmasConstants.TURKEY_DRUMSTICK, 28), new Item(ChristmasConstants.ROAST_POTATOES, 28)); // just in case
        player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
    }

    @Override
    public String name() {
        return "Land of Snow";
    }
}
