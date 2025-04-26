package com.zenyte.game.content.boss.smokedevil;

import com.zenyte.game.util.Colour;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 18/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SmokeDevilInstance extends DynamicArea implements DeathPlugin, CannonRestrictionPlugin, EntityAttackPlugin, LootBroadcastPlugin {
    protected SmokeDevilInstance(@NotNull final Player player, AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY) {
        super(allocatedArea, copiedChunkX, copiedChunkY);
        this.username = player.getUsername();
    }

    private final String username;
    private static final Location[] smokeDevilSpawnTiles = new Location[] {new ImmutableLocation(2371, 9453, 0), new ImmutableLocation(2370, 9443, 0), new ImmutableLocation(2362, 9444, 0), new ImmutableLocation(2361, 9456, 0), new ImmutableLocation(2355, 9454, 0), new ImmutableLocation(2355, 9447, 0)};
    private static final Location bossSpawnTile = new ImmutableLocation(2364, 9447, 0);
    private static final Location insideTile = new ImmutableLocation(2376, 9452, 0);

    @Override
    public void constructed() {
        for (final Location tile : smokeDevilSpawnTiles) {
            World.spawnNPC(NpcId.SMOKE_DEVIL, getLocation(tile));
        }
        World.spawnNPC(NpcId.THERMONUCLEAR_SMOKE_DEVIL, getLocation(bossSpawnTile));
        World.getPlayer(username).ifPresent(player -> {
            player.setLocation(getLocation(insideTile));
            player.lock(1);
            player.sendMessage(Colour.RED.wrap("Should you die in the instance, your items will be permanently lost!"));
            if (player.getNumericAttribute("smoke devil instance warning").intValue() == 0) {
                player.addAttribute("smoke devil instance warning", 1);
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        plain(Colour.RED.wrap("WARNING: ") + "If you die in this instance, your items will be permanently lost!");
                    }
                });
            }
        });
    }

    @Override
    public void enter(Player player) {
        player.setViewDistance(Player.SCENE_DIAMETER);
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.resetViewDistance();
    }

    @Override
    public String name() {
        return username + "\'s thermonuclear smoke devil instance";
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public Location onLoginLocation() {
        return new Location(2379, 9452, 0);
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
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            final String name = ((NPC) entity).getDefinitions().getName();
            if (name.equals("Thermonuclear smoke devil") && player.getKillcount((NPC) entity) > 0) {
                if (!player.getSlayer().isCurrentAssignment(entity)) {
                    player.sendMessage("You can only kill the Thermonuclear smoke devil while you\'re on a slayer task.");
                    return false;
                }
            } else if (name.equals("Smoke devil")) {
                if (!player.getSlayer().isCurrentAssignment(entity)) {
                    player.sendMessage("You can only kill Smoke devils while you\'re on a slayer task.");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

}
