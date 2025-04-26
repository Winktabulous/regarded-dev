package com.zenyte.plugins.renewednpc;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Tommeh | 13-3-2019 | 18:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GliderCaptain extends NPCPlugin {

    @Override
    public void handle() {
        bind("Glider", (player, npc) -> GameInterface.GNOME_GLIDER.open(player));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                6091,
                NpcId.CAPTAIN_ERRDO,// 6088
                NpcId.CAPTAIN_SHORACKS,// 7178
                NpcId.GNORMADIUM_AVLAFRIM_7517,// 7517
                NpcId.GNORMADIUM_AVLAFRIM_10445,
                NpcId.GNORMADIUM_AVLAFRIM_10446,
                NpcId.GNORMADIUM_AVLAFRIM_10447,
                NpcId.GNORMADIUM_AVLAFRIM_10448,
                NpcId.GNORMADIUM_AVLAFRIM_10450,
                NpcId.GNORMADIUM_AVLAFRIM_10451,
                NpcId.CAPTAIN_DALBUR,// 10452
                NpcId.CAPTAIN_DALBUR_10453,
                NpcId.CAPTAIN_DALBUR_10454,
                NpcId.CAPTAIN_DALBUR_10455,
                NpcId.CAPTAIN_DALBUR_10456,
                NpcId.CAPTAIN_DALBUR_10457,
                NpcId.CAPTAIN_DALBUR_10458,
                NpcId.CAPTAIN_BLEEMADGE,// 10459
                NpcId.CAPTAIN_BLEEMADGE_10461,
                NpcId.CAPTAIN_BLEEMADGE_10462,
                NpcId.CAPTAIN_BLEEMADGE_10463,
                NpcId.CAPTAIN_BLEEMADGE_10464,
                NpcId.CAPTAIN_BLEEMADGE_10465,
                NpcId.CAPTAIN_BLEEMADGE_10466,
                NpcId.CAPTAIN_ERRDO_10467,
                NpcId.CAPTAIN_ERRDO_10468,
                NpcId.CAPTAIN_ERRDO_10469,
                NpcId.CAPTAIN_ERRDO_10470,
                NpcId.CAPTAIN_ERRDO_10471,
                NpcId.CAPTAIN_ERRDO_10472,
                NpcId.CAPTAIN_ERRDO_10473,
                NpcId.CAPTAIN_KLEMFOODLE,// 10479
                NpcId.CAPTAIN_KLEMFOODLE_10480,
                NpcId.CAPTAIN_KLEMFOODLE_10481,
                NpcId.CAPTAIN_KLEMFOODLE_10482,
                NpcId.CAPTAIN_KLEMFOODLE_10483,
                NpcId.CAPTAIN_KLEMFOODLE_10484,
                NpcId.CAPTAIN_KLEMFOODLE_10485,
                NpcId.CAPTAIN_SHORACKS_10486,
                NpcId.CAPTAIN_SHORACKS_10487,
                NpcId.CAPTAIN_SHORACKS_10488,
                NpcId.CAPTAIN_SHORACKS_10489,
                NpcId.CAPTAIN_SHORACKS_10490,
                NpcId.CAPTAIN_SHORACKS_10491,
        };
    }
}
