package com.zenyte.game.content.boss.magearenaii;

import com.zenyte.game.content.treasuretrails.DeviceTemperature;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.utils.TimeUnit;
import mgi.utilities.CollectionUtils;

import java.util.Optional;

import static com.zenyte.game.content.treasuretrails.DeviceTemperature.MA2_VISIBLY_SHAKING;
import static com.zenyte.game.content.treasuretrails.DeviceTemperature.MASTER_VISIBLY_SHAKING;

/**
 * @author Kris | 22/06/2019 16:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EnchantedSymbol extends ItemPlugin {

    private enum GodSpawnLocation {
        SPAWN_1(new Location(3022, 3831, 0)),
        SPAWN_2(new Location(3172, 3898, 0)),
        SPAWN_3(new Location(3150, 3878, 0)),
        SPAWN_4(new Location(3158, 3842, 0)),
        SPAWN_5(new Location(3168, 3796, 0)),
        SPAWN_6(new Location(3215, 3883, 0)),
        SPAWN_7(new Location(3231, 3873, 0)),
        SPAWN_8(new Location(3260, 3886, 0)),
        SPAWN_9(new Location(3296, 3876, 0)),
        SPAWN_10(new Location(3276, 3842, 0)),
        SPAWN_11(new Location(3248, 3831, 0)),
        SPAWN_12(new Location(3261, 3800, 0)),
        SPAWN_13(new Location(3334, 3899, 0)),
        SPAWN_14(new Location(3304, 3942, 0)),
        SPAWN_15(new Location(3233, 3909, 0));

        private final Location tile;
        private final Location deviceTile;
        private static final GodSpawnLocation[] values = values();

        GodSpawnLocation(final Location tile) {
            this.tile = tile;
            this.deviceTile = tile.transform(2, 2, 0);
        }
    }

    @Override
    public void handle() {
        bind("Activate", (player, item, container, slotId) -> {
            if (!WildernessArea.isWithinWilderness(player)) {
                player.sendMessage("You need to be inside the Wilderness to use this.");
                return;
            }
            if (player.getTemporaryAttributes().get("MAII NPC") != null) {
                player.sendMessage("You already have a creature spawned.");
                return;
            }
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options(TITLE, new DialogueOption("Saradomin", () -> use(God.SARADOMIN, player)), new DialogueOption("Guthix", () -> use(God.GUTHIX, player)), new DialogueOption("Zamorak", () -> use(God.ZAMORAK, player)), new DialogueOption("Cancel"));
                }
            });
        });
    }


    private enum God {
        SARADOMIN, GUTHIX, ZAMORAK
    }

    private final void use(final God god, final Player player) {
        final long lastCheck = player.getNumericAttribute("last MAII god spawn check").longValue();
        final int lastTile = player.getNumericAttribute("last MAII god tile").intValue();
        final boolean update = lastCheck < System.currentTimeMillis();
        GodSpawnLocation currentSpawn = CollectionUtils.findMatching(GodSpawnLocation.values, spawn -> spawn.tile.getPositionHash() == lastTile);
        if (update || currentSpawn == null) {
            currentSpawn = Utils.getRandomElement(GodSpawnLocation.values);
            player.addAttribute("last MAII god tile", currentSpawn.tile.getPositionHash());
            player.addAttribute("last MAII god spawn check", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30));
        }
        final Location center = new Location(currentSpawn.deviceTile);
        final Location tile = player.getLocation();
        final int lastDistance = player.getNumericTemporaryAttribute("last enchanted symbol distance").intValue();
        final int distance = Math.max(Math.abs(center.getX() - tile.getX()), Math.abs(center.getY() - tile.getY()));
        player.addTemporaryAttribute("last enchanted symbol distance", distance);
        final Optional<DeviceTemperature> temperature = DeviceTemperature.get(DeviceTemperature.MA2_HOT_COLD_TEMPERATURES, distance);
        if (lastCheck == 0) {
            player.sendMessage("You feel a shift in the energy eminating from the enchanted symbol. The creatures have moved.");
        }
        if (!temperature.isPresent()) {
            player.getDialogueManager().start(new ItemChat(player, new Item(21800), "The enchanted symbol is inactive."));
            return;
        }
        final DeviceTemperature temp = temperature.get();
        if (lastDistance == 0) {
            player.sendMessage(temp.getMessage() + ".");
        } else if (lastDistance == distance) {
            player.sendMessage(temp.getMessage() + ", and the same temperature as last time.");
        } else {
            player.sendMessage(temp.getMessage() + ", " + (lastDistance < distance ? "but" : "and") + " " + (lastDistance < distance ? "colder than" : "warmer than") + " last time.");
        }
        if (temp == MA2_VISIBLY_SHAKING) {
            player.getAttributes().remove("last MAII god spawn check");
            switch (god) {
            case GUTHIX: 
                new Derwen(player, currentSpawn.tile).spawn();
                break;
            case ZAMORAK: 
                new Porazdir(player, currentSpawn.tile).spawn();
                break;
            case SARADOMIN: 
                new JusticiarZachariah(player, currentSpawn.tile).spawn();
                break;
            }
        } else {
            player.applyHit(new Hit(Utils.random(3, 16), HitType.DEFAULT));
            player.sendMessage("The power of the enchanted symbol hurts you in the process.");
        }
    }

    @Override
    public int[] getItems() {
        return new int[] {21800};
    }
}
