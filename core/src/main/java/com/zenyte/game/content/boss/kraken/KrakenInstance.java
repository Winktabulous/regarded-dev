package com.zenyte.game.content.boss.kraken;

import com.zenyte.game.content.skills.slayer.Assignment;
import com.zenyte.game.content.skills.slayer.BossTask;
import com.zenyte.game.content.skills.slayer.BossTaskSumona;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;
import com.zenyte.game.world.region.area.plugins.LogoutPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/11/2018 17:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KrakenInstance extends DynamicArea implements EntityAttackPlugin, DeathPlugin, LogoutPlugin, LootBroadcastPlugin {
    public static final Location OUTSIDE_TILE = new Location(2280, 10016, 0);
    public static final Location INSIDE_TILE = new Location(2280, 10022, 0);

    public KrakenInstance(final Player player, boolean donator, final AllocatedArea allocatedArea, final int copiedChunkX, final int copiedChunkY) {
        super(allocatedArea, copiedChunkX, copiedChunkY);
        this.player = player;
        this.donatorInstance = donator;
    }

    private boolean donatorInstance;
    private final Player player;

    @Override
    public void constructed() {
        final Kraken kraken = new Kraken(496, getLocation(2278, 10034, 0), Direction.SOUTH, 5);
        kraken.spawn();
        player.sendMessage(Colour.RED.wrap("Should you die in the instance, your items will be permanently lost!"));
        if (player.getNumericAttribute("kraken instance warning").intValue() == 0) {
            player.addAttribute("kraken instance warning", 1);
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    plain(Colour.RED.wrap("WARNING: ") + "If you die in this instance, your items will be permanently lost!");
                }
            });
        }
        player.setLocation(getLocation(INSIDE_TILE));
    }

    @Override
    public void enter(Player player) {
        player.setForceMultiArea(true);
        player.getTemporaryAttributes().put(Kraken.CA_TASK_INSTANCE_STARTED_ATT, Boolean.TRUE);
    }
    @Override
    public void leave(Player player, boolean logout) {
        player.setForceMultiArea(false);
        player.getTemporaryAttributes().remove(Kraken.CA_TASK_INSTANCE_STARTED_ATT);
        player.getTemporaryAttributes().remove(Kraken.CA_TASK_INSTANCE_KC_ATT);
        if (logout) {
            player.forceLocation(OUTSIDE_TILE);
        }
    }

    @Override
    public void onLogout(final @NotNull Player player) {
        player.setLocation(OUTSIDE_TILE);
    }

    @Override
    public Location onLoginLocation() {
        return OUTSIDE_TILE;
    }

    @Override
    public String name() {
        return player.getName() + "'s Kraken instance";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if(donatorInstance)
            return true;
        if (entity instanceof NPC) {
            final int id = ((NPC) entity).getId();
            if (id == 492 || id == 493) {
                final Assignment assignment = player.getSlayer().getAssignment();
                if (assignment == null || assignment.getTask() != RegularTask.CAVE_KRAKEN) {
                    player.getDialogueManager().start(new Dialogue(player, 7412) {
                        @Override
                        public void buildDialogue() {
                            npc("You can only kill the Cave krakens while on a slayer task!");
                        }
                    });
                    return false;
                }
            } else if (id == 494 || id == 496) {
                final Assignment assignment = player.getSlayer().getAssignment();
                if (assignment != null && (assignment.getTask() == RegularTask.CAVE_KRAKEN || assignment.getTask() == BossTask.KRAKEN || assignment.getTask() == BossTaskSumona.KRAKEN_SUMONA)) {
                    return true;
                }
                player.getDialogueManager().start(new Dialogue(player, 7412) {
                    @Override
                    public void buildDialogue() {
                        npc("You can only kill the Krakens while on a slayer task!");
                    }
                });
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public String getDeathInformation() {
        return null;
    }

    @Override
    public Location getRespawnLocation() {
        return null;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

}
