package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.content.skills.magic.spells.teleports.structures.LeverTeleport;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

public final class WildernessLeverObject implements ObjectAction {

    private static final Location MAGE_BANK_OUTSIDE_LEVER = new Location(3090, 3956, 0);

    private static final Location DESERTED_KEEP_LEVER = new Location(3154, 3923, 0);

    private static final Location HOME_LEVER = new Location(3085, 3510, 0);

    private static final Location ARDOUGNE_LEVER = new Location(2562, 3311, 0);

    private static final Location MAGE_BANK_INSIDE_LEVER = new Location(2539, 4713, 0);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        switch(object.getId()) {
            case 1814:
            case 34:
            case 5960:
                dangerousLeverTeleport(player, object.getId() == 5960 ? MAGE_BANK_OUTSIDE_LEVER : DESERTED_KEEP_LEVER, object, "... and teleport into the Wilderness.");
                break;
            case 1815:
            case 9738:
            case 9472:
                new LeverTeleport(option.equals("Edgeville") ? HOME_LEVER : ARDOUGNE_LEVER, object, "... and teleport out of the Wilderness.", null).teleport(player);
                break;
            case 5959:
                new LeverTeleport(MAGE_BANK_INSIDE_LEVER, object, "... and teleport into the mage's cave.", null).teleport(player);
                break;
        }
    }

    private void dangerousLeverTeleport(final Player player, final Location location, final WorldObject object, final String message) {
        if (player.getBooleanAttribute("lever_warning")) {
            teleport(player, location, object, message);
        } else {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    plain("You're about to teleport to the Wilderness!");
                    options("Are you sure you want to teleport", "Yes.", "Yes, and don't remind me again.", "No.").onOptionOne(() -> teleport(player, location, object, message)).onOptionTwo(() -> {
                        player.addAttribute("lever_warning", 1);
                        teleport(player, location, object, message);
                    });
                }
            });
        }
    }

    private void teleport(final Player player, final Location location, final WorldObject object, final String message) {
        new LeverTeleport(location, object, message, () -> {
            if (object.getId() == ObjectId.LEVER_1814) {
                player.getAchievementDiaries().update(ArdougneDiary.USE_ARDOUGNE_LEVER);
                player.getAchievementDiaries().update(WildernessDiary.USE_LEVER);
            } else if (object.getId() == ObjectId.LEVER_34) {
                player.getAchievementDiaries().update(WildernessDiary.USE_LEVER);
            }
        }).teleport(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 1815, ObjectId.LEVER_9738, ObjectId.LEVER_9472, ObjectId.LEVER_1814, ObjectId.LEVER_5959, ObjectId.LEVER_5960, ObjectId.LEVER_26761, ObjectId.LEVER_34 };
    }
}
