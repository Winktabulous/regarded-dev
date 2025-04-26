package com.zenyte.plugins.dialogue;

import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.cutscene.actions.FadeScreenAction;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class KylieMinnowD extends Dialogue {

    private static final Location DOCK = new Location(2600, 3425, 0);
    private static final Location PLATFORM = new Location(2614, 3440, 0);

    private final boolean rowboat;

    public KylieMinnowD(final Player player, final int npcId, final boolean rowboat) {
        super(player, npcId);
        this.rowboat = rowboat;
    }

    @Override
    public void buildDialogue() {
        if(rowboat) {
            final String name = player.inArea("Fishing platform") ? "Fishing Guild" : "minnow platform";
            if(player.inArea("Fishing platform") || player.getSkills().getLevel(SkillConstants.FISHING) >= 82/* && checkSuit()*/) {
				options(1, "Travel to the "+name+"?", "Yes", "No").onOptionOne(() -> travel(!player.inArea("Fishing platform")));
			} else {
                plain(1, "You need at least " + Colour.RED + "level 82 fishing</col> to travel to the minnow platform.");
				//plain(1, "You need at least " + Colour.RED + "level 82 fishing</col> and the " + Colour.RED + "angler outfit</col> to travel to the minnow platform.");
			}
            return;
        }

        if(player.inArea("Fishing platform") || player.inArea("Uber Zone")) {
            // todo minnow trading need npc varbit
            npc(1, "You can bring me 40 minnows for a raw shark!");
        } else {
            player(1, "So, how about letting me out onto your fishing platform?");
            if (player.getSkills().getLevel(SkillConstants.FISHING) >= 82/* && checkSuit()*/) {
                npc(2, "Sure thing critter, just hop in that row boat!");
                options(3, "Travel to the fishing platform?", "Yes", "No").onOptionOne(() -> travel(!player.inArea("Fishing platform")));
            } else {
                npc(2, "Sorry, hun. I don't think you quite have what it takes to travel to the minnow platform.");
                plain(3, "You need at least " + Colour.RED + "level 82 fishing</col> to travel to the minnow platform.");
                //plain(3, "You need at least " + Colour.RED + "level 82 fishing</col> and the " + Colour.RED + "angler outfit</col> to travel to the minnow platform.");
            }
        }
    }

    private void travel(final boolean platform) {
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if(ticks == 0) {
					new FadeScreenAction(player, 3).run();
				}
                if(ticks == 2) {
                    player.setLocation(platform ? PLATFORM : DOCK);
                    stop();
                }

                ticks++;
            }
        }, 0, 0);
    }

    private boolean checkSuit() {
        final Item helm = player.getEquipment().getItem(EquipmentSlot.HELMET);
        final Item body = player.getEquipment().getItem(EquipmentSlot.PLATE);
        final Item legs = player.getEquipment().getItem(EquipmentSlot.LEGS);
        final Item boots = player.getEquipment().getItem(EquipmentSlot.BOOTS);

        if(helm == null || body == null || legs == null || boots == null) {
			return false;
		}

        return helm.getId() == 13258 && body.getId() == 13259 && legs.getId() == 13260 && boots.getId() == 13261;
    }
}
