package com.zenyte.game.content.creaturecreation;

import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.TimeUnit;
import mgi.types.config.items.ItemDefinitions;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Chris
 * @since August 22 2020
 */
public class CreatedCreatureNpc extends NPC {
    /**
     * The creature's maximum spawn distance from its creator (player).
     */
    private static final int MAX_SPAWN_DISTANCE = 8;
    /**
     * The amount of ticks this creature will be spawned for until it gets removed.
     */
    private static final long LIFESPAN_TICKS = TimeUnit.MINUTES.toTicks(7);
    /**
     * The name of the player who created this creature.
     */
    private final String playerName;
    /**
     * The amount of game ticks this creature has been alive for. Used for disposal after certain amount of time passes.
     */
    private final MutableInt ticks = new MutableInt();

    public CreatedCreatureNpc(final int id, @NotNull final Location location, @NotNull final Player player) {
        super(id, location, Direction.SOUTH, 0);
        this.playerName = player.getUsername();
        setSpawned(true);
    }

    @Override
    public void processNPC() {
        if (ticks.getAndIncrement() >= LIFESPAN_TICKS) {
            World.getPlayer(playerName).ifPresent(player -> player.sendMessage("Your created creature has disappeared."));
            finish();
            return;
        } else if (isDying() || isDead()) {
            SymbolOfLifeActivateDialogue.playerCreature.remove(playerName);
        }
        super.processNPC();
    }

    @Override
    public boolean canAttack(final Player player) {
        if (!player.getUsername().equals(playerName)) {
            player.sendMessage("You did not create this creature.");
            return false;
        }
        return true;
    }

    @Override
    public boolean canBeMulticannoned(@NotNull final Player player) {
        return player.getUsername().equalsIgnoreCase(playerName);
    }

    /**
     * Convert part of the drop to noted form if corresponding diary is completed.
     */
    @Override
    public void dropItem(final Player killer, Item item, final Location tile, boolean guaranteedDrop) {
        final SymbolOfLife symbol = SymbolOfLife.of(getId());
        final Optional<DiaryReward> diary = symbol.getDiaryForNoted();
        if (diary.isPresent() && DiaryUtil.eligibleFor(diary.get(), killer) && symbol.getAllMaterials().contains(item.getId())) {
            item = new Item(ItemDefinitions.getOrThrow(item.getId()).getNotedId(), item.getAmount());
        }
        super.dropItem(killer, item, tile, guaranteedDrop);
    }

    @Override
    public void finish() {
        SymbolOfLifeActivateDialogue.playerCreature.remove(playerName);
        super.finish();
    }
}
