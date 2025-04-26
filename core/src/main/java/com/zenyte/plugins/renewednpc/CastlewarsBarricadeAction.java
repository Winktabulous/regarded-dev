package com.zenyte.plugins.renewednpc;

import com.zenyte.game.content.minigame.castlewars.CastleWars;
import com.zenyte.game.content.minigame.castlewars.CastleWarsTeam;
import com.zenyte.game.content.skills.firemaking.FiremakingAction;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.DoubleItemChat;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.itemonnpc.ItemOnBarricadeAction;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class CastlewarsBarricadeAction extends NPCPlugin {

    public static final Item WATER_BUCKET = new Item(1929, 1);

    public static final Item BUCKET = new Item(1925, 1);

    @Override
    public void handle() {
        bind("Burn", new OptionHandler() {

            @Override
            public void handle(final Player player, final NPC npc) {
                if (!player.getInventory().containsItem(FiremakingAction.TINDERBOX) && !player.getInventory().containsItem(CastleWars.EXPLOSIVE_POTION)) {
                    player.getDialogueManager().start(new DoubleItemChat(player, FiremakingAction.TINDERBOX, CastleWars.EXPLOSIVE_POTION, "You need a tinderbox or explosive potion to burn barricades!"));
                    return;
                }
                if (!player.getInventory().containsItem(CastleWars.EXPLOSIVE_POTION)) {
                    npc.setTransformation(npc.getId() == 5722 ? 5723 : 5725);
                } else {
                    if (player.getInventory().containsItem(CastleWars.EXPLOSIVE_POTION)) {
                        player.getInventory().deleteItem(CastleWars.EXPLOSIVE_POTION);
                        World.sendGraphics(ItemOnBarricadeAction.EXPLOSION, npc.getLocation());
                        final boolean team = npc.getId() < 5723;
                        if (team) {
                            CastleWarsTeam.setSaraBarricades(Math.max(0, CastleWarsTeam.getSaraBarricades() - 1));
                        } else {
                            CastleWarsTeam.setZamBarricades(Math.max(0, CastleWarsTeam.getZamBarricades() - 1));
                        }
                        npc.finish();
                    }
                }
            }
        });
        bind("Extinguish", new OptionHandler() {

            @Override
            public void handle(final Player player, final NPC npc) {
                if (!player.getInventory().containsItem(WATER_BUCKET)) {
                    player.getDialogueManager().start(new ItemChat(player, WATER_BUCKET, "You need a bucket of water to extinguish a flaming barricade!"));
                    return;
                }
                player.getInventory().deleteItem(WATER_BUCKET);
                player.getInventory().addItem(BUCKET);
                npc.setTransformation(npc.getId() == 5723 ? 5722 : 5724);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.BARRICADE, NpcId.BARRICADE_5723, NpcId.BARRICADE_5724, NpcId.BARRICADE_5725 };
    }
}
