package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Kris | 27/04/2019 02:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GodwarsMountainCrack implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Crawl-through")) {
            if (player.getSkills().getLevel(SkillConstants.AGILITY) < 60) {
                player.getDialogueManager().start(new PlainChat(player, "You need an Agility level of at least 60 to use this shortcut."));
                return;
            }
            player.lock(4);
            player.setAnimation(new Animation(844));
            new FadeScreen(player, () -> player.setLocation(player.getLocation().matches(new Location(2904, 3720, 0)) ? new Location(2899, 3713, 0) : new Location(2904, 3720, 0))).fade(4);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LITTLE_CRACK };
    }
}
