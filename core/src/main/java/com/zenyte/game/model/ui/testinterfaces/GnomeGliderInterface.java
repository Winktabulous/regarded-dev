package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.content.glider.GliderLocation;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;

/**
 * @author Tommeh | 13-3-2019 | 17:26
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GnomeGliderInterface extends Interface {
    @Override
    protected void attach() {
        put(4, "Ta Quir Priw");
        put(7, "Sindarpos");
        put(10, "Lemanto Andra");
        put(13, "Kar-Hewo");
        put(16, "Gandius");
        put(21, "Lemantolly Undri");
        put(25, "Ookookolly Undri");
    }

    @Override
    public void open(Player player) {
        player.getVarManager().sendVar(153, -1);
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("Ta Quir Priw", player -> handle(player, GliderLocation.TA_QUIR_PRIW));
        bind("Sindarpos", player -> handle(player, GliderLocation.SINDARPOS));
        bind("Lemanto Andra", player -> handle(player, GliderLocation.LEMANTO_ANDRA));
        bind("Kar-Hewo", player -> handle(player, GliderLocation.KAR_HEWO));
        bind("Gandius", player -> handle(player, GliderLocation.GANDIUS));
        bind("Lemantolly Undri", player -> handle(player, GliderLocation.LEMANTOLLY_UNDRI));
        bind("Ookookolly Undri", player -> handle(player, GliderLocation.OOKOOKOLLY_UNDRI));
    }

    private void handle(final Player player, final GliderLocation destinationGlider) {
        final GliderLocation currentGlider = GliderLocation.getGlider(player);
        if (currentGlider == null) {
            return;
        }
        if (currentGlider.equals(destinationGlider)) {
            player.sendMessage("You are already at this glider location.");
            return;
        }
        if (destinationGlider.equals(GliderLocation.LEMANTOLLY_UNDRI)) {
            player.getAchievementDiaries().update(WesternProvincesDiary.TRAVEL_TO_FELDIP_HILLS);
        }
        player.lock(4);
        player.getVarManager().sendVar(153, currentGlider.ordinal() << 14 | destinationGlider.ordinal());
        new FadeScreen(player, () -> {
            player.getAchievementDiaries().update(KaramjaDiary.USE_GNOME_GLIDER);
            player.setLocation(destinationGlider.getLocation());
            player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        }).fade(3);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.GNOME_GLIDER;
    }
}
