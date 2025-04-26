package com.zenyte.game.world.region.area.wizardsguild;

import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.TemporaryDoubleDoor;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 24. juuni 2018 : 16:25:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WizardsGuildDoor implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (!player.inArea("Wizards guild") && player.getSkills().getLevel(SkillConstants.MAGIC) < 66) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Magic level of at least 66 to enter the Wizards guild."));
            return;
        }
        player.getAchievementDiaries().update(ArdougneDiary.ENTER_THE_MAGIC_GUILD);
        TemporaryDoubleDoor.handleDoubleDoor(player, object);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MAGIC_GUILD_DOOR, ObjectId.MAGIC_GUILD_DOOR_1733 };
    }
}
