package com.zenyte.game.model.ui.testinterfaces;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary;
import com.zenyte.game.content.sailing.CharterLocation;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.PlainChat;

import static com.zenyte.game.content.sailing.CharterLocation.*;

/**
 * @author Tommeh | 27-10-2018 | 20:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ChartershipInterface extends Interface {
    @Override
    protected void attach() {
        put(2, "Port Tyras");
        put(5, "Port Phasmatys");
        put(8, "Catherby");
        put(11, "Shipyard");
        put(14, "Musa Point");
        put(17, "Brimhaven");
        put(20, "Port Khazard");
        put(23, "Port Sarim");
        put(26, "Mos Le'Harmless");
        put(32, "Corsair Cove");
        put(33, "Prifddinas");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("Port Tyras", player -> charter(player, PORT_TYRAS));
        bind("Port Phasmatys", player -> charter(player, PORT_PHASMATYS));
        bind("Catherby", player -> charter(player, CATHERBY));
        bind("Shipyard", player -> charter(player, SHIPYARD));
        bind("Brimhaven", player -> charter(player, BRIMHAVEN));
        bind("Musa Point", player -> charter(player, MUSA_POINT));
        bind("Port Khazard", player -> charter(player, PORT_KHAZARD));
        bind("Port Sarim", player -> charter(player, PORT_SARIM));
        bind("Mos Le'Harmless", player -> charter(player, MOS_LE_HARMLESS));
        bind("Corsair Cove", player -> charter(player, CORSAIR_COVE));
        bind("Prifddinas", player -> charter(player, PRIFDDINAS));
    }

    private void charter(final Player player, final CharterLocation destination) {
        final CharterLocation location = CharterLocation.getLocation(player.getLocation());
        if (location == null || destination == null) {
            return;
        }
        final Item cost = new Item(995, destination.getCosts()[location.ordinal()]);
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        if (!player.getInventory().containsItem(cost)) {
            player.getDialogueManager().start(new PlainChat(player, "You don't have enough gold in your inventory to sail<br>to " + destination + "."));
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                plain("To sail to " + destination + " from here will cost you " + cost.getAmount() + " gold.<br><br>Are you sure you want to pay that?");
                options(TITLE, "Ok", "Choose again", "No").onOptionOne(() -> {
                    player.lock(3);
                    new FadeScreen(player, () -> {
                        if (location.equals(CharterLocation.SHIPYARD)) {
                            player.getAchievementDiaries().update(KaramjaDiary.CHARTER_A_SHIP_FROM_SHIPYARD);
                        }
                        player.getInventory().deleteItem(cost);
                        player.setLocation(destination.getLocation());
                    }).fade(3);
                }).onOptionTwo(() -> GameInterface.SHIP_DESTINATION_CHART.open(player));
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SHIP_DESTINATION_CHART;
    }
}
