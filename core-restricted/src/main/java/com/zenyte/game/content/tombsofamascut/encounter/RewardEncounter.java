package com.zenyte.game.content.tombsofamascut.encounter;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.InvocationCategoryType;
import com.zenyte.game.content.tombsofamascut.raid.EncounterType;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidParty;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Savions
 */
public class RewardEncounter extends TOARaidArea {

    public static final int SPIRIT_ID = 11829;
    private static final RewardPair[] COMMON_REWARDS = {
            new RewardPair(ItemId.COINS_995, 1),
            new RewardPair(ItemId.DEATH_RUNE, 20),
            new RewardPair(ItemId.SOUL_RUNE, 40),
            new RewardPair(ItemId.GOLD_ORE, 90),
            new RewardPair(ItemId.DRAGON_DART_TIP, 100),
            new RewardPair(ItemId.MAHOGANY_LOGS, 100),
            new RewardPair(ItemId.SAPPHIRE, 200),
            new RewardPair(ItemId.EMERALD, 250),
            new RewardPair(ItemId.GOLD_BAR, 250),
            new RewardPair(ItemId.POTATO_CACTUS, 250),
            new RewardPair(ItemId.RAW_SHARK, 250),
            new RewardPair(ItemId.RUBY, 300),
            new RewardPair(ItemId.DIAMOND, 400),
            new RewardPair(ItemId.RAW_MANTA_RAY, 450),
            new RewardPair(ItemId.CACTUS_SPINE, 600),
            new RewardPair(ItemId.DRAGONSTONE, 600),
            new RewardPair(ItemId.BATTLESTAFF, 1100),
            new RewardPair(ItemId.COCONUT_MILK, 1100),
            new RewardPair(ItemId.LILY_OF_THE_SANDS, 1100),
            new RewardPair(ItemId.TOADFLAX_SEED, 1400),
            new RewardPair(ItemId.RANARR_SEED, 1800),
            new RewardPair(ItemId.TORSTOL_SEED, 2200),
            new RewardPair(ItemId.SNAPDRAGON_SEED, 2200),
            new RewardPair(ItemId.DRAGON_MED_HELM, 4000),
            new RewardPair(ItemId.MAGIC_SEED, 6500),
            new RewardPair(ItemId.BLOOD_ESSENCE, 7500)
    };
    private static final int NO_PURPLE = 0;
    private static final int SMALL_PURPLE = 1;
    private static final int HARD_PURPLE = 2;
    private boolean rolledPurple;

    static {
        VarManager.appendPersistentVarbit(14319);
    }

    public RewardEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
        super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
    }

    @Override
    public void constructed() {
        super.constructed();
        new Spirit(getLocation(encounterType.getNpcLocation())).spawn();
        rollRewards();
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
    }

    @Override
    public void onRoomStart() {

    }

    @Override
    public void onRoomEnd() {

    }

    @Override
    public void onRoomReset() {

    }

    private void rollRewards() {
        final List<Player> orderedPlayers = new ArrayList<>(party.getPlayers());
        orderedPlayers.sort((o1, o2) -> o2.getTOAManager().getCurrentPoints() - o1.getTOAManager().getCurrentPoints());
        orderedPlayers.forEach(this::rollType);
    }

    private void rollType(Player player) {
        final int raidLevel = party.getCompletedRaidLevel();
        final int x = Math.min(400, raidLevel);
        final int y = Math.max(0, Math.min(150, raidLevel - 400));
        final int quotient = 10500 - 20 * (x + (y / 3));
        final int chance = player.getTOAManager().getCurrentPoints() / quotient;
        final float privateServerBoost = 2.0F;
        final float worldBoost = World.hasBoost(XamphurBoost.TOA_BOOST) ? 1.2F : 1.0F;
        if (!rolledPurple && Utils.random(99) <= (chance * privateServerBoost * worldBoost)) {
            final int purpleRoll = Utils.random(6);
            if (purpleRoll <= 3) {
                if (raidLevel >= 50 || Utils.random(49) == 0) {
                    rollIndividual(player, SMALL_PURPLE);
                    return;
                }
            }
            else {
                if (raidLevel >= 150 || Utils.random(49) == 0) {
                    rollIndividual(player, HARD_PURPLE);
                    return;
                }
            }
        }
        rollIndividual(player, NO_PURPLE);
    }

    private void rollIndividual(Player player, int purpleType) {
        final Container container = new Container(ContainerType.TOA_REWARD, ContainerPolicy.ALWAYS_STACK, 6, Optional.of(player));
        final int points = player.getTOAManager().getCurrentPoints();
        if (points < 1500) {
            container.add(new Item(ItemId.FOSSILISED_DUNG));
        }
        else {
            final int raidLevel = party.getCompletedRaidLevel();
            if (party.getTotalDeaths() < 1) {
                if (raidLevel >= 350 && !player.containsAny(ItemId.MASORI_CRAFTING_KIT))
                    container.add(new Item(ItemId.MASORI_CRAFTING_KIT));
                if (raidLevel >= 400 && !player.containsAny(ItemId.MENAPHITE_ORNAMENT_KIT))
                    container.add(new Item(ItemId.MENAPHITE_ORNAMENT_KIT));
                if (raidLevel >= 450) {
                    if (party.getPartySettings().allActive(InvocationCategoryType.AKKHA)
                            && !player.containsAny(ItemId.REMNANT_OF_AKKHA)) {
                        container.add(new Item(ItemId.REMNANT_OF_AKKHA));
                    }
                    else if (party.getPartySettings().allActive(InvocationCategoryType.ZEBAK)
                            && !player.containsAny(ItemId.REMNANT_OF_ZEBAK)) {
                        container.add(new Item(ItemId.REMNANT_OF_ZEBAK));
                    }
                    else if (party.getPartySettings().allActive(InvocationCategoryType.BA_BA)
                            && !player.containsAny(ItemId.REMNANT_OF_BABA)) {
                        container.add(new Item(ItemId.REMNANT_OF_BABA));
                    }
                    else if (party.getPartySettings().allActive(InvocationCategoryType.KEPHRI)
                            && !player.containsAny(ItemId.REMNANT_OF_KEPHRI)) {
                        container.add(new Item(ItemId.REMNANT_OF_KEPHRI));
                    }
                    else if (party.getPartySettings().allActive(InvocationCategoryType.THE_WARDENS)
                            && !player.containsAny(ItemId.ANCIENT_REMNANT)) {
                        container.add(new Item(ItemId.ANCIENT_REMNANT));
                    }
                }
                if (raidLevel >= 500)
                    container.add(new Item(ItemId.CURSED_PHALANX));
            }
            if (purpleType != NO_PURPLE) {
                rolledPurple = true;
                if (purpleType == SMALL_PURPLE) {
                    rewardAnnounceRare(player, container, Utils.random(1) == 0 ? ItemId.OSMUMTENS_FANG : ItemId.LIGHTBEARER);
                }
                else {
                    final int random = Utils.random(10);
                    if (random < 3)
                        rewardAnnounceRare(player, container, ItemId.ELIDINIS_WARD);
                    else if (random < 5)
                        rewardAnnounceRare(player, container, ItemId.MASORI_MASK);
                    else if (random < 7)
                        rewardAnnounceRare(player, container, ItemId.MASORI_BODY);
                    else if (random < 9)
                        rewardAnnounceRare(player, container, ItemId.MASORI_CHAPS);
                    else
                        rewardAnnounceRare(player, container, ItemId.TUMEKENS_SHADOW_UNCHARGED);

                }
            }
            else {
                final float itemQuantityFactor = raidLevel < 300 ? 1F : (1.15F + .01F * ((float) (raidLevel - 300) / 5));
                final List<RewardPair> potentialRewards = new ArrayList<>();
                for (RewardPair rewardPair : COMMON_REWARDS)
                    if (points >= rewardPair.divisor)
                        potentialRewards.add(rewardPair);

                for (int i = 0; i < 3; i++) {
                    if (container.getSize() > 5) break;
                    final RewardPair pair = potentialRewards.get(Utils.random(potentialRewards.size() - 1));
                    container.add(new Item(pair.id, (int) (Math.floor((double) points / pair.divisor) * itemQuantityFactor)));
                }
            }
            if (container.getSize() < 6 && Utils.random(14) == 0)
                container.add(new Item(ItemId.THREAD_OF_ELIDINIS));

            if (container.getSize() < 6 && Utils.random(19) == 0) {
                final int random = Utils.random(2);
                if (random == 0)
                    container.add(new Item(ItemId.BREACH_OF_THE_SCARAB));
                else if (random == 1)
                    container.add(new Item(ItemId.JEWEL_OF_THE_SUN));
                else
                    container.add(new Item(ItemId.EYE_OF_THE_CORRUPTOR));
            }
            if (container.getSize() < 6 && Utils.random(25) == 0)
                container.add(new Item(ItemId.CLUE_SCROLL_ELITE));
        }
        player.getVarManager().sendBit(14139, 1);
        player.getTOAManager().setRewardContainer(container);
    }

    private void rewardAnnounceRare(Player player, Container container, int itemId) {
        Item reward = new Item(itemId, 1);
        WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, reward, "Tombs of Amascut");
        container.add(reward);
    }

    static class Spirit extends NPC {

        public Spirit(Location tile) {
            super(SPIRIT_ID, tile, Direction.NORTH, 0);
        }

        @Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }

        @Override public void setTarget(Entity target, TargetSwitchCause cause) { }
    }

    record RewardPair(int id, int divisor) {}
}
