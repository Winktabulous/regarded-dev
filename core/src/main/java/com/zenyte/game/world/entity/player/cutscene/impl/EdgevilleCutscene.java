package com.zenyte.game.world.entity.player.cutscene.impl;

import com.zenyte.game.GameConstants;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.model.HintArrowPosition;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.Cutscene;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraResetAction;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.plugins.renewednpc.ZenyteGuide;

/**
 * @author Kris | 26/01/2019 01:49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EdgevilleCutscene extends Cutscene {

    @Override
    public void build() {
        int time = 0;
        addActions(time+=6, () -> player.lock(), () -> player.setViewDistance(30), () -> player.getAppearance().setInvisible(true),
                () -> player.setLocation(ZenyteGuide.SPAWN_LOCATION), () -> chat(player, "Very well! Let's begin then.."));

        addActions(time+=12, () -> action(player, "Here you can find the shopping hub where you can find everything from food, and skilling supplies, to combat weaponry/ ammunition and runes..", 3080, 3509),
                new CameraPositionAction(player, new Location(3081, 3504), 500, 5, 10),
                new CameraLookAction(player, new Location(3076, 3509), 0, 5, 10));//

        addActions(time+=12, () -> action(player, "Over here we have a furnace where you can smelt your ores into bars, aswell as anvils to smith your bars into equipment.", 3106, 3495),
                new CameraPositionAction(player, new Location(3111, 3494), 1000, 5, 10),
                new CameraLookAction(player, new Location(3101, 3491, 0), 0, 5, 10));


        addActions(time+=12, () -> action(player, "In this hub you will also find the Emblem trader where you can sell emblems to and also spend your blood money.", 3109, 3509),
                new CameraPositionAction(player, new Location(3109, 3502), 1000, 5, 10),
                new CameraLookAction(player, new Location(3106, 3508, 0), 0, 5, 10));

        addActions(time+=12, () -> action(player, "In here you will find the <col=00080>Iron Man Tutor</col> for all your ironman related inquiries like reverting your ironman mode and claiming armour" +
                        " In here you will also find Frank who you can spend your vote points on.", 3109, 3509),
                new CameraPositionAction(player, new Location(3104, 3508), 1000, 5, 10),
                new CameraLookAction(player, new Location(3113, 3509, 0), 0, 5, 10));//

        addActions(time+=12, () -> action(player, "And over here you can find the slayer hub. With all of the slayer masters on " + GameConstants.SERVER_NAME + ".", 3086, 3514),
                new CameraPositionAction(player, new Location(3079, 3472), 1000, 5, 10),
                new CameraLookAction(player, new Location(3076, 3472, 0), 0, 5, 10));

        addActions(time+=12, () -> action(player, "This is the <col=00080>" + GameConstants.SERVER_NAME + " Portal</col> which you can use to teleport all around " + GameConstants.SERVER_NAME + "!", 3094, 3503),
                new CameraPositionAction(player, new Location(3101, 3494), 1000, 5, 10),
                new CameraLookAction(player, new Location(3094, 3501, 0), 0, 5, 10));

        addActions(time+=12, () -> action(player, "And finally over here we have the main hub of the home area where you can find <col=00080>Bankers & Grand Exchange Clerks</col>.", 3094, 3492, HintArrowPosition.EAST),
                new CameraPositionAction(player, new Location(3086, 3493), 1000, 5, 10),//
                new CameraLookAction(player, new Location(3094, 3495, 0), 0, 5, 10), () -> player.setLocation(ZenyteGuide.HOME_ZENYTE_GUIDE));

        addActions(time+=12, () -> player.setViewDistance(15), () -> player.getPacketDispatcher().resetHintArrow(), () -> ZenyteGuide.finishTutorial(player),
                new CameraResetAction(player));
    }

    private void action(final Player player, final String message, final int x, final int y) {
        action(player, message, x, y, HintArrowPosition.CENTER);
    }

    private void action(final Player player, final String message, final int x, final int y, final HintArrowPosition position) {
        player.getPacketDispatcher().sendHintArrow(new HintArrow(x, y, (byte) 50, position));
        chat(player, message);
    }

    private void chat(final Player player, final String message) {
        player.getDialogueManager().start(new NPCChat(player, ZenyteGuide.NPC_ID, message, false));
    }
}
