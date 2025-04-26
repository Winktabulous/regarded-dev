package com.near_reality.game.content.gauntlet.plugins;

import com.near_reality.game.content.gauntlet.GauntletPlayerAttributesKt;
import com.near_reality.game.content.gauntlet.Gauntlet;
import com.near_reality.game.content.gauntlet.GauntletConstants;
import com.near_reality.game.content.gauntlet.GauntletType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 *
 * @author Andys1814.
 * @since 1/19/2022.
 */
public final class GauntletEntrance implements ObjectAction {

    private static final int ENTRANCE = 37340;
    private static final int[] SKILL_REQS = { SkillConstants.AGILITY, SkillConstants.FARMING, SkillConstants.HERBLORE, SkillConstants.HUNTER, SkillConstants.MINING, SkillConstants.SMITHING, SkillConstants.WOODCUTTING};

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
//        if (!DeveloperCommands.INSTANCE.getEnabledGauntlet()){
//            player.getDialogueManager().start(new Dialogue(player) {
//                @Override
//                public void buildDialogue() {
//                    plain("Gauntlet is currently disabled.");
//                }
//            });
//            return;
//        }

        // Don't allow players to enter Gauntlet not having spoken to Bryn.
        if (!GauntletPlayerAttributesKt.getSpokenToBrynn(player)) {
            player.getDialogueManager().start(new NPCChat(player, GauntletConstants.BRYN, "Don't think you want to be heading down there without knowing what you're getting into! Come and see me, if you really want to go down there."));
            return;
        }

        // Don't allow players to enter Gauntlet with existing rewards in chest.
        if (GauntletPlayerAttributesKt.getGauntletRewardType(player) != null) {
            player.getDialogueManager().start(new NPCChat(player, GauntletConstants.BRYN, "There's something in that there chest waiting for you. Don't leave it for too long."));
            return;
        }

        // Don't allow players to enter Gauntlet with a pet.
        if (player.getFollower() != null) {
            player.getDialogueManager().start(new NPCChat(player, GauntletConstants.BRYN, "Don't think you can be taking any plus ones down there with you."));
            return;
        }

        for (int i : SKILL_REQS) {
            if (player.getSkills().getLevelForXp(i) < 70) {
                player.getDialogueManager().start(new NPCChat(player, GauntletConstants.BRYN, "Don't think you are strong enough to enter the Gauntlet. You need at least level 70 " + SkillConstants.SKILLS[i] + "."));
                return;
            }
        }

        GauntletType type = GauntletType.STANDARD;
        if(option.equalsIgnoreCase("Normal-NoPrep"))
            type = GauntletType.STANDARD_NO_PREP;
        else if(option.equalsIgnoreCase("Corrupted-NoPrep"))
            type = GauntletType.CORRUPTED_NO_PREP;
        else if(option.equalsIgnoreCase("Enter-corrupted"))
            type = GauntletType.CORRUPTED;
        Gauntlet gauntlet = Gauntlet.construct(player, type);
        if (gauntlet == null) {
            player.getDialogueManager().start(new PlainChat(player, "Unable to construct Gauntlet map instance - please report this to a staff member."));
            return;
        }

        gauntlet.start();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ENTRANCE };
    }

}
