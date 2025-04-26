package com.zenyte.game.content.creaturecreation;

import com.zenyte.game.util.WorldUtil;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.IntLists;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Chris
 * @since August 22 2020
 */
public class SymbolOfLifeActivateDialogue extends Dialogue {
    public static final Object2ObjectOpenHashMap<String, CreatedCreatureNpc> playerCreature = new Object2ObjectOpenHashMap<>();
    private final SymbolOfLife symbolOfLife;

    public SymbolOfLifeActivateDialogue(@NotNull final Player player, @NotNull final SymbolOfLife symbolOfLife) {
        super(player, NpcId.HOMUNCULUS);
        this.symbolOfLife = symbolOfLife;
    }

    @Override
    public void buildDialogue() {
        if (player.getInventory().containsAll(symbolOfLife.getAllMaterials())) {
            npc("You haveee materials need. Here goes!").executeAction(this::createCreature);
        } else {
            plain("You need " + getMissingMaterials() + " to do that.");
        }
    }

    private void createCreature() {
        player.getInventory().deleteItemsIfContains(symbolOfLife.getAllMaterials(), () -> {
            final CreatedCreatureNpc createdCreature = new CreatedCreatureNpc(symbolOfLife.getNpcId(), getCreateSpawnLocation(), player);
            playerCreature.put(player.getUsername(), createdCreature);
            createdCreature.spawn();
            createdCreature.setTarget(player);
            player.sendSound(3417);
        });
    }

    private ImmutableLocation getCreateSpawnLocation() {
        final Location baseSpawnTile = RandomLocation.random(player.getLocation(), 5);
        final int size = NPCDefinitions.getOrThrow(symbolOfLife.getNpcId()).getSize();
        final Location spawnTile = WorldUtil.findEmptySquare(baseSpawnTile, 10, size, Optional.of(location -> !player.isProjectileClipped(location, true))).orElse(player.getLocation());
        return new ImmutableLocation(spawnTile);
    }

    private String getMissingMaterials() {
        final StringBuilder missingMaterials = new StringBuilder();
        final IntLists.UnmodifiableList requiredMaterials = symbolOfLife.getAllMaterials();
        for (int index = 0; index < requiredMaterials.size(); index++) {
            final int requiredMaterial = requiredMaterials.getInt(index);
            if (!player.carryingItem(requiredMaterial)) {
                missingMaterials.append(index == requiredMaterials.size() - 1 ? " and " : "").append(ItemDefinitions.nameOf(requiredMaterial).toLowerCase());
            }
        }
        return missingMaterials.toString();
    }
}
