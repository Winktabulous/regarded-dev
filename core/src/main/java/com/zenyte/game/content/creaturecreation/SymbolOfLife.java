package com.zenyte.game.content.creaturecreation;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.IntListUtils;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.npc.NpcId;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntLists;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Optional;

/**
 * @author Chris
 * @since August 22 2020
 */
public enum SymbolOfLife {
    NORTH_EAST("Feather of chicken and eye of newt", DiaryReward.ARDOUGNE_CLOAK2, 3058, 4410, NpcId.NEWTROOST, ItemId.EYE_OF_NEWT, ItemId.FEATHER), NORTH_WEST("Horn of unicorn and hide of cow", DiaryReward.ARDOUGNE_CLOAK2, 3018, 4410, NpcId.UNICOW, ItemId.UNICORN_HORN, ItemId.COWHIDE), SOUTH_EAST("Red spiders' eggs and a sardine raw", DiaryReward.ARDOUGNE_CLOAK2, 3043, 4361, NpcId.SPIDINE, ItemId.RED_SPIDERS_EGGS, ItemId.RAW_SARDINE), SOUTH_WEST("Swordfish raw and chicken uncooked", null, 3034, 4361, NpcId.SWORDCHICK, ItemId.RAW_SWORDFISH, ItemId.RAW_CHICKEN), EAST("Raw meat of jubbly bird and a lobster raw", DiaryReward.ARDOUGNE_CLOAK1, 3066, 4380, NpcId.JUBSTER, ItemId.RAW_JUBBLY, ItemId.RAW_LOBSTER), WEST("Legs of giant frog and a cave eel uncooked", DiaryReward.ARDOUGNE_CLOAK1, 3012, 4380, NpcId.FROGEEL, ItemId.GIANT_FROG_LEGS, ItemId.RAW_CAVE_EEL);
    private static final ImmutableSet<SymbolOfLife> SYMBOLS_OF_LIFE = Sets.immutableEnumSet(EnumSet.allOf(SymbolOfLife.class));
    private static final Int2ObjectOpenHashMap<SymbolOfLife> NPC_MAP;

    static {
        CollectionUtils.populateMap(SYMBOLS_OF_LIFE.asList(), NPC_MAP = new Int2ObjectOpenHashMap<>(SYMBOLS_OF_LIFE.size()), SymbolOfLife::getNpcId);
    }

    private final String inspectMessage;
    private final ImmutableLocation objectLocation;
    private final int npcId;
    private final int firstMaterial;
    private final int secondMaterial;
    /**
     * The diary reward required for this altar to convert drops to noted form.
     */
    private final DiaryReward diaryForNoted;
    private final IntLists.UnmodifiableList allMaterials;

    SymbolOfLife(@NotNull final String message, final DiaryReward diary, final int x, final int y, final int npcId, final int firstItem, final int secondItem) {
        this.inspectMessage = message + "...";
        this.diaryForNoted = diary;
        this.objectLocation = new ImmutableLocation(x, y, 0);
        this.npcId = npcId;
        this.firstMaterial = firstItem;
        this.secondMaterial = secondItem;
        this.allMaterials = IntListUtils.unmodifiable(firstItem, secondItem);
    }

    public static SymbolOfLife of(@NotNull final ImmutableLocation location) {
        for (final SymbolOfLife symbolOfLife : SYMBOLS_OF_LIFE) {
            if (symbolOfLife.getObjectLocation().getPositionHash() == location.getPositionHash()) {
                return symbolOfLife;
            }
        }
        throw new IllegalArgumentException("Could not find symbol of life with location: " + location);
    }

    public static SymbolOfLife of(final int npcId) {
        return NPC_MAP.get(npcId);
    }

    public Optional<DiaryReward> getDiaryForNoted() {
        return diaryForNoted == null ? Optional.empty() : Optional.of(diaryForNoted);
    }

    public String getInspectMessage() {
        return inspectMessage;
    }

    public ImmutableLocation getObjectLocation() {
        return objectLocation;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getFirstMaterial() {
        return firstMaterial;
    }

    public int getSecondMaterial() {
        return secondMaterial;
    }

    public IntLists.UnmodifiableList getAllMaterials() {
        return allMaterials;
    }
}
