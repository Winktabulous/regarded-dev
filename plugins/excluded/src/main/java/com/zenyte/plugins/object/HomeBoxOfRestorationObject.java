package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.utils.TimeUnit;

/**
 * @author Tommeh | 19-3-2019 | 18:07
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class HomeBoxOfRestorationObject implements ObjectAction {

    private static final Graphics HEAL_GFX = new Graphics(1177);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Restore")) {
            final long time = player.getNumericAttribute("box of restoration delay").longValue();
            if (time <= System.currentTimeMillis()) {
                player.getToxins().reset();
                player.heal(player.getMaxHitpoints());
                player.getCombatDefinitions().setSpecialEnergy(100);
                player.getVariables().setRunEnergy(100);
                for (int i = 0; i < SkillConstants.SKILLS.length; i++) {
                    if (player.getSkills().getLevel(i) < player.getSkills().getLevelForXp(i)) {
                        player.getSkills().setLevel(i, player.getSkills().getLevelForXp(i));
                    }
                }
                player.setGraphics(HEAL_GFX);
                player.sendFilteredMessage("The box of restoration completely restores your health.");
                player.addAttribute("box of restoration delay", System.currentTimeMillis() + (TimeUnit.MINUTES.toMillis(getDelay(player))));
            } else {
                final int totalSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(time - System.currentTimeMillis());
                final int seconds = totalSeconds % 60;
                final int minutes = totalSeconds / 60;
                player.sendMessage("You need to wait another " + (minutes == 0 ? (seconds + " seconds") : (minutes + " minutes") + " until you can use this again."));
            }
        } else if (option.equalsIgnoreCase("Remove-skull")) {
            if (player.getVariables().getTime(TickVariable.SKULL) > TimeUnit.MINUTES.toTicks(20)) {
                player.getDialogueManager().start(new PlainChat(player, "You are currently under the effects of a permanent skull and cannot be unskulled."));
                return;
            }
            player.blockIncomingHits();
            player.getVariables().removeSkull();
            player.sendFilteredMessage("Your skull has been removed.");
        }
    }

    private int getDelay(final Player player) {
        final MemberRank memberRank = player.getMemberRank();
        if (memberRank.equalToOrGreaterThan(MemberRank.UBER)) {
            return 0;
        } else if (memberRank.equalToOrGreaterThan(MemberRank.MYTHICAL)) {
            return 1;
        } else if (memberRank.equalToOrGreaterThan(MemberRank.LEGENDARY)) {
            return 2;
        } else if (memberRank.equalToOrGreaterThan(MemberRank.RESPECTED)) {
            return 4;
        } else if (memberRank.equalToOrGreaterThan(MemberRank.EXTREME)) {
            return 5;
        } else if (memberRank.equalToOrGreaterThan(MemberRank.EXPANSION)) {
            return 6;
        } else if (memberRank.equalToOrGreaterThan(MemberRank.PREMIUM)) {
            return 7;
        }
        return 10;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                ObjectId.BOX_OF_RESTORATION,
                ObjectId.BOX_OF_RESTORATION_35022,
                ObjectId.BOX_OF_RESTORATION_35021
        };
    }
}
