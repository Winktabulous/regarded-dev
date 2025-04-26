package com.zenyte.game.content.follower.impl;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.GameConstants;
import com.zenyte.game.content.boons.impl.NoPetDebt;
import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.content.follower.Pet;
import com.zenyte.game.content.follower.PetWrapper;
import com.zenyte.game.content.minigame.wintertodt.RewardCrate;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.followers.AbyssalOrphanD;
import com.zenyte.plugins.dialogue.followers.BabyMoleD;
import com.zenyte.plugins.dialogue.followers.CallistoCubD;
import com.zenyte.plugins.dialogue.followers.HellpuppyD;
import com.zenyte.plugins.dialogue.followers.IkkleHydraD;
import com.zenyte.plugins.dialogue.followers.JalNibRekD;
import com.zenyte.plugins.dialogue.followers.KalphitePrincessD;
import com.zenyte.plugins.dialogue.followers.MidnightD;
import com.zenyte.plugins.dialogue.followers.MuphinD;
import com.zenyte.plugins.dialogue.followers.NexlingD;
import com.zenyte.plugins.dialogue.followers.NoonD;
import com.zenyte.plugins.dialogue.followers.OlmletD;
import com.zenyte.plugins.dialogue.followers.PetChaosElementalD;
import com.zenyte.plugins.dialogue.followers.PetDagannothPrimeD;
import com.zenyte.plugins.dialogue.followers.PetDagannothRexD;
import com.zenyte.plugins.dialogue.followers.PetDagannothSupremeD;
import com.zenyte.plugins.dialogue.followers.PetDarkCoreD;
import com.zenyte.plugins.dialogue.followers.PetGeneralGraardorD;
import com.zenyte.plugins.dialogue.followers.PetKrakenD;
import com.zenyte.plugins.dialogue.followers.PetKreeArraD;
import com.zenyte.plugins.dialogue.followers.PetKrilTsutsarothD;
import com.zenyte.plugins.dialogue.followers.PetPenanceQueenD;
import com.zenyte.plugins.dialogue.followers.PetSmokeDevilD;
import com.zenyte.plugins.dialogue.followers.PetSnakelingD;
import com.zenyte.plugins.dialogue.followers.PetZilyanaD;
import com.zenyte.plugins.dialogue.followers.PhoenixD;
import com.zenyte.plugins.dialogue.followers.PrinceBlackDragonD;
import com.zenyte.plugins.dialogue.followers.ScorpiasOffspringD;
import com.zenyte.plugins.dialogue.followers.SkotosD;
import com.zenyte.plugins.dialogue.followers.TzRekJadD;
import com.zenyte.plugins.dialogue.followers.VenenatisSpiderlingD;
import com.zenyte.plugins.dialogue.followers.VetionD;
import com.zenyte.plugins.dialogue.followers.VorkiD;
import com.zenyte.plugins.dialogue.followers.YoungllefD;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

/**
 * @author Tommeh | 23-11-2018 | 18:04
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum BossPet implements Pet {
    SRARACHA(ItemId.SRARACHA, NpcId.SRARACHA_2144, 2000, null, NpcId.SARACHNIS),
    SRARACHA_ORANGE(ItemId.SRARACHA_25842, NpcId.SRARACHA_11159, 2000, null),
    SRARACHA_BLUE(ItemId.SRARACHA_25843, NpcId.SRARACHA_11160, 2000, null),
    KALPHITE_PRINCESS_FIRST(12647, 6638, 2000, KalphitePrincessD.class, 963, 965),
    KALPHITE_PRINCESS_SECOND(12654, 6637, 2000, KalphitePrincessD.class),
    PET_CHAOS_ELEMENTAL(11995, 2055, 200, PetChaosElementalD.class, 2054),
    PET_XAMPHUR(32083, 10959, 300, null, 10951),
    PET_FRANK(30500, 10970, 650, null, NpcId.VANSTROM_KLAUSE_9569),
    LITTLE_NIGHTMARE(ItemId.LITTLE_NIGHTMARE, NpcId.LITTLE_NIGHTMARE_9399, 0, null),

    PET_REAVER(CustomItemId.BLOOD_TENTACLES, 10960, 300, null),
    PET_DAGANNOTH_PRIME(12644, 6629, 3000, PetDagannothPrimeD.class, 2266),
    PET_DAGANNOTH_REX(12645, 6630, 3000, PetDagannothRexD.class, 2267),
    PET_DAGANNOTH_SUPREME(12643, 6628, 3000, PetDagannothSupremeD.class, 2265),
    PET_DARK_CORE(12816, 318, 3500, PetDarkCoreD.class, 319),
    PET_GENERAL_GRAARDOR(12650, 6632, 3500, PetGeneralGraardorD.class, 2215),
    PET_KRIL_TSUTSAROTH(12652, 6634, 3500, PetKrilTsutsarothD.class, 3129),
    PET_KREE_ARRA(12649, 6631, 3500, PetKreeArraD.class, 3162),
    PET_ZILYANA(12651, 6633, 3500, PetZilyanaD.class, 2205),
    PET_KRAKEN(12655, 6640, 2000, PetKrakenD.class, 494),
    PET_SMOKE_DEVIL(12648, 6639, 2000, PetSmokeDevilD.class, 499),
    PET_SMOKE_DEVIL_REGULAR(22663, 8483, 2000, PetSmokeDevilD.class),
    GREEN_PET_SNAKELING(12921, 2130, 2500, PetSnakelingD.class, 2042, 2043, 2044),
    RED_PET_SNAKELING(12939, 2131, 2500, PetSnakelingD.class),
    TURQOISE_PET_SNAKELING(12940, 2132, 2500, PetSnakelingD.class),
    PRINCE_BLACK_DRAGON(12653, 6636, 2000, PrinceBlackDragonD.class, 239),
    SCORPIAS_OFFSPRING(13181, 5561, 1500, ScorpiasOffspringD.class, 6615),
    VENENATIS_OFFSPRING(13177, 5557, 1500, VenenatisSpiderlingD.class, 6610),
    CALLISTO_CUB(13178, 5558, 1500, CallistoCubD.class, 6609),
    PURPLE_VETION(13179, 5559, 1500, VetionD.class, 6611, 6612),
    ORANGE_VETION(13180, 5560, 1500, VetionD.class),
    VORKI(21992, 8029, 2000, VorkiD.class, 8061),
    HELLPUPPY(13247, 3099, 2000, HellpuppyD.class, 5862),
    BABY_MOLE(12646, 6635, 2500, BabyMoleD.class, 5779, 6499),
    NOON(21748, 7892, 2000, NoonD.class, 7888),
    MIDNIGHT(21750, 7893, 2000, MidnightD.class),
    PET_PENANCE_QUEEN(12703, 6674, 1000, PetPenanceQueenD.class),
    ABYSSAL_ORPHAN(13262, 5884, 2560, AbyssalOrphanD.class),
    TZREK_JAD(13225, 5893, 200, TzRekJadD.class),
    SKOTOS(21273, 7671, 50, SkotosD.class, 7286),
    OLMLET(20851, 7520, 65, OlmletD.class),
    JAL_NIB_REK(21291, 7675, 100, JalNibRekD.class),
    PET_CORPOREAL_CRITTER(22318, 8010, 3500, PetDarkCoreD.class, 319),
    PUPPADILE(22376, 8201),
    TEKTINY(22378, 8202),
    VANGUARD(22380, 8203),
    VASA_MINIRIO(22382, 8204),
    VESPINA(22384, 8205),
    LIL_ZIK(22473, 8337),
    SMOLCANO(23760, 8739, 1250, null, 9049, 9050),
    IKKLE_HYDRA_GREEN(22746, 8492, 2000, IkkleHydraD.class, 8621),
    IKKLE_HYDRA_BLUE(22748, 8493, 2000, IkkleHydraD.class),
    IKKLE_HYDRA_RED(22750, 8494, 2000, IkkleHydraD.class),
    IKKLE_HYDRA_LAST(22752, 8495, 2000, IkkleHydraD.class),
    TZREK_ZUK(22319, 8011),
    PHOENIX(20693, 7370, RewardCrate.PHOENIX_PET_CHANCE, PhoenixD.class),
    NEXLING(26348, 11277, 500, NexlingD.class),
    YOUNGLLEF(23757, 8737, 1000, YoungllefD.class),
    CORRUPTED_YOUNGLLEF(23759, 8738, 1000, YoungllefD.class),
    MUPHIN_RANGED(ItemId.MUPHIN, NpcId.MUPHIN, 2500, MuphinD.class),
    MUPHIN_MELEE(ItemId.MUPHIN_27592, NpcId.MUPHIN_12006, 2500, MuphinD.class),
    MUPHIN_SHIELD(ItemId.MUPHIN_27593, NpcId.MUPHIN_12007, 2500, MuphinD.class),
    TUMEKENS_GUARDIAN(ItemId.TUMEKENS_GUARDIAN, NpcId.TUMEKENS_GUARDIAN),
    ELIDINIS_GUARDIAN(ItemId.ELIDINIS_GUARDIAN, NpcId.ELIDINIS_GUARDIAN),
    TUMEKENS_DAMAGED_GUARDIAN(ItemId.TUMEKENS_DAMAGED_GUARDIAN, NpcId.TUMEKENS_DAMAGED_GUARDIAN),
    ELIDINIS_DAMAGED_GUARDIAN(ItemId.ELIDINIS_DAMAGED_GUARDIAN, NpcId.ELIDINIS_DAMAGED_GUARDIAN),
    AKKHITO(ItemId.AKKHITO, NpcId.AKKHITO),
    BABI(ItemId.BABI, NpcId.BABI),
    KEPHRITI(ItemId.KEPHRITI, NpcId.KEPHRITI),
    ZEBO(ItemId.ZEBO, NpcId. ZEBO),
    BUTCH(ItemId.BUTCH, NpcId.BUTCH),
    BARON(ItemId.BARON, NpcId.BARON),
    GANODERMIC_RUNT(32100, 14698),
    AHRIM_THE_BOBBLED(32186, 16045),
    DHAROK_THE_BOBBLED(32187, 16046),
    GUTHAN_THE_BOBBLED(32188, 16047),
    KARIL_THE_BOBBLED(32189, 16048),
    TORAG_THE_BOBBLED(32190, 16049),
    VERAC_THE_BOBBLED(32191, 16050),
    NID(ItemId.NID, NpcId.NID),
    RAX(ItemId.RAX, NpcId.RAX),
    SMOLDERING_DEMON(CustomItemId.SMOLDERING_DEMON, NpcId.TORRMENTED_DEMON_13602)
    ;

    public static final Int2IntMap metamorphosisMap = new Int2IntOpenHashMap();
    public static final BossPet[] snakelings = {GREEN_PET_SNAKELING, RED_PET_SNAKELING, TURQOISE_PET_SNAKELING};
    public static final BossPet[] kalphiteQueens = {KALPHITE_PRINCESS_FIRST, KALPHITE_PRINCESS_SECOND};
    public static final BossPet[] vetions = {ORANGE_VETION, PURPLE_VETION};
    public static final BossPet[] grotesqueGuardians = {NOON, MIDNIGHT};
    public static final BossPet[] youngllefPets = {YOUNGLLEF, CORRUPTED_YOUNGLLEF};
    public static final BossPet[] hydras = {IKKLE_HYDRA_GREEN, IKKLE_HYDRA_BLUE, IKKLE_HYDRA_RED, IKKLE_HYDRA_LAST};
    public static final BossPet[] infernoPets = {JAL_NIB_REK, TZREK_ZUK};
    public static final BossPet[] muphins = {MUPHIN_RANGED, MUPHIN_MELEE, MUPHIN_SHIELD};

    static {
        metamorphosisMap.put(6639, 8483);
        metamorphosisMap.put(8483, 6639);
        metamorphosisMap.put(6638, 6637);
        metamorphosisMap.put(6637, 6638);
        metamorphosisMap.put(2130, 2131);
        metamorphosisMap.put(2131, 2132);
        metamorphosisMap.put(2132, 2130);
        metamorphosisMap.put(5559, 5560);
        metamorphosisMap.put(5560, 5559);
        metamorphosisMap.put(7893, 7892);
        metamorphosisMap.put(7892, 7893);
        metamorphosisMap.put(7675, 8011);
        metamorphosisMap.put(8011, 7675);
        metamorphosisMap.put(8492, 8493);
        metamorphosisMap.put(8493, 8494);
        metamorphosisMap.put(8494, 8495);
        metamorphosisMap.put(8495, 8492);
        metamorphosisMap.put(8010, 318);
        metamorphosisMap.put(318, 8010);
        //Chambers of Xeric
        metamorphosisMap.put(7520, 8201);
        metamorphosisMap.put(8201, 8202);
        metamorphosisMap.put(8202, 8203);
        metamorphosisMap.put(8203, 8204);
        metamorphosisMap.put(8204, 8205);
        metamorphosisMap.put(8205, 7520);
        //Chameleons
        metamorphosisMap.put(15158, 15159);
        metamorphosisMap.put(15159, 15160);
        metamorphosisMap.put(15160, 15161);
        metamorphosisMap.put(15161, 15162);
        metamorphosisMap.put(15162, 15163);
        metamorphosisMap.put(15163, 15164);
        metamorphosisMap.put(15164, 15165);
        metamorphosisMap.put(15165, 15166);
        metamorphosisMap.put(15166, 15167);
        metamorphosisMap.put(15167, 15158);
        //Youngllef
        metamorphosisMap.put(8729, 8730);
        metamorphosisMap.put(8730, 8729);

        //Muphin
        metamorphosisMap.put(NpcId.MUPHIN, NpcId.MUPHIN_12006);
        metamorphosisMap.put(NpcId.MUPHIN_12006, NpcId.MUPHIN_12007);
        metamorphosisMap.put(NpcId.MUPHIN_12007, NpcId.MUPHIN);
    }

    BossPet(final int itemId, final int petId) {
        this(itemId, petId, -1, null, -1);
    }

    BossPet(final int itemId, final int petId, final int rarity, final Class<? extends Dialogue> dialogue) {
        this(itemId, petId, rarity, dialogue, -1);
    }

    BossPet(final int itemId, final int petId, final int rarity, final Class<? extends Dialogue> dialogue, final int... bossIds) {
        this.itemId = itemId;
        this.petId = petId;
        this.rarity = rarity;
        this.dialogue = dialogue;
        this.bossIds = bossIds;
    }

    private final int itemId;
    private final int petId;
    private final int rarity;
    private final Class<? extends Dialogue> dialogue;
    private final int[] bossIds;
    public static final BossPet[] VALUES = values();
    public static final Int2ObjectOpenHashMap<BossPet> BOSS_PETS_BY_NPC_ID = new Int2ObjectOpenHashMap<>(VALUES.length);
    public static final Int2ObjectOpenHashMap<BossPet> BOSS_PETS_BY_BOSS_NPC_ID = new Int2ObjectOpenHashMap<>(VALUES.length);
    public static final Int2ObjectOpenHashMap<BossPet> BOSS_PETS_BY_PET_NPC_ID = new Int2ObjectOpenHashMap<>(VALUES.length);
    public static final Int2ObjectOpenHashMap<BossPet> BOSS_PETS_BY_ITEM_ID = new Int2ObjectOpenHashMap<>(VALUES.length);

    public int getRarity(final Player player, final int bossId) {
        int finalRarity = rarity;
        // boosted drop rate calculation for events; disabled for raids
        boolean boosted = GameConstants.BOOSTED_BOSS_PETS || World.hasBoost(XamphurBoost.BONUS_PET_RATES);
        if (boosted && !this.equals(OLMLET))
            finalRarity = (int) (rarity - (rarity * GameConstants.BOOSTED_BOSS_PET_RATE));
        if (this.equals(PET_CHAOS_ELEMENTAL)) {
            finalRarity = bossId == 2054 ? (boosted ? 170 : 200) : (boosted ? 850 : 1000);
        } else if (this.equals(TZREK_JAD)) {
            finalRarity = player != null && (player.getSlayer().getAssignment() != null && player.getSlayer().getAssignment().getTask().equals(RegularTask.TZTOK_JAD)) ? 100 : 200;
        }

        if (player != null && player.getVariables().getPetBoosterTick() > 0) {
            finalRarity *= 0.9;
        }

        return finalRarity;
    }

    public static BossPet getByNPC(final int bossId) {
        return BOSS_PETS_BY_NPC_ID.get(bossId);
    }

    @Nullable
    public static BossPet getByPetId(final int petId) {
        return BOSS_PETS_BY_PET_NPC_ID.getOrDefault(petId, null);
    }
    public static BossPet getByBossNPC(final int bossId) {
        return BOSS_PETS_BY_BOSS_NPC_ID.get(bossId);
    }

    public static BossPet getByItem(final int itemId) {
        return BOSS_PETS_BY_ITEM_ID.get(itemId);
    }

    static {
        for (final BossPet pet : VALUES) {
            for (final int bossId : pet.getBossIds()) {
                BOSS_PETS_BY_NPC_ID.put(bossId, pet);
                if (bossId == -1) {
                    continue;
                }
                BOSS_PETS_BY_BOSS_NPC_ID.put(bossId, pet);
            }
            BOSS_PETS_BY_PET_NPC_ID.put(pet.petId, pet);
            BOSS_PETS_BY_ITEM_ID.put(pet.itemId, pet);
        }
    }

    @Override
    public int itemId() {
        return itemId;
    }

    @Override
    public int petId() {
        return petId;
    }

    @Override
    public String petName() {
        return name();
    }

    @Override
    public boolean hasPet(Player player) {
        if (isSnakeling(this)) {
            final int[] ids = new int[snakelings.length];
            for (int index = 0; index < snakelings.length; index++) {
                ids[index] = snakelings[index].itemId();
            }
            return (PetWrapper.checkFollower(player) && ArrayUtils.contains(snakelings, player.getFollower().getPet())) || player.containsAny(ids);
        }
        if (isKQ(this)) {
            final int[] ids = new int[kalphiteQueens.length];
            for (int index = 0; index < kalphiteQueens.length; index++) {
                ids[index] = kalphiteQueens[index].itemId();
            }
            return (PetWrapper.checkFollower(player) && ArrayUtils.contains(kalphiteQueens, player.getFollower().getPet())) || player.containsAny(ids);
        }
        if (isYoungllef(this)) {
            final int[] ids = new int[youngllefPets.length];
            for (int index = 0; index < youngllefPets.length; index++) {
                ids[index] = youngllefPets[index].itemId();
            }
            return (PetWrapper.checkFollower(player) && ArrayUtils.contains(youngllefPets, player.getFollower().getPet())) || player.containsAny(ids);
        }
        if (isVetion(this)) {
            final int[] ids = new int[vetions.length];
            for (int index = 0; index < vetions.length; index++) {
                ids[index] = vetions[index].itemId();
            }
            return (PetWrapper.checkFollower(player) && ArrayUtils.contains(vetions, player.getFollower().getPet())) || player.containsAny(ids);
        }
        if (isGrotesqueGuardian(this)) {
            final int[] ids = new int[grotesqueGuardians.length];
            for (int index = 0; index < grotesqueGuardians.length; index++) {
                ids[index] = grotesqueGuardians[index].itemId();
            }
            return (PetWrapper.checkFollower(player) && ArrayUtils.contains(grotesqueGuardians, player.getFollower().getPet())) || player.containsAny(ids);
        }
        if (this.equals(PET_DARK_CORE) || this.equals(PET_CORPOREAL_CRITTER)) {
            return (PetWrapper.checkFollower(player) && (player.getFollower().getPet().equals(PET_DARK_CORE) || player.getFollower().getPet().equals(PET_CORPOREAL_CRITTER))) || player.containsItem(PET_DARK_CORE.getItemId()) || player.containsItem(PET_CORPOREAL_CRITTER.getItemId());
        }
        if (this.equals(JAL_NIB_REK) || this.equals(TZREK_ZUK)) {
            final int[] ids = new int[infernoPets.length];
            for (int index = 0; index < infernoPets.length; index++) {
                ids[index] = infernoPets[index].itemId();
            }
            return (PetWrapper.checkFollower(player) && ArrayUtils.contains(infernoPets, player.getFollower().getPet())) || player.containsAny(ids);
        }
        if (this.equals(MUPHIN_RANGED) || this.equals(MUPHIN_MELEE) || this.equals(MUPHIN_SHIELD)) {
            final int[] ids = new int[muphins.length];
            for (int index = 0; index < muphins.length; index++) {
                ids[index] = muphins[index].itemId();
            }
            return (PetWrapper.checkFollower(player) && ArrayUtils.contains(muphins, player.getFollower().getPet())) || player.containsAny(ids);
        }
        return (PetWrapper.checkFollower(player) && player.getFollower().getPet().equals(this)) || player.containsItem(this.getItemId());
    }

    @Override
    public Class<? extends Dialogue> dialogue() {
        return dialogue;
    }

    @Override
    public boolean roll(final Player player, int rarity) {
        if(player.getBoonManager().hasBoon(NoPetDebt.class)) {
            rarity /= 2;
        }
        if (rarity == -1 || Utils.random(rarity) != 0) {
            return false;
        }
        final Item item = new Item(itemId);
        player.getCollectionLog().add(item);
        if (hasPet(player)) {
            player.sendMessage("<col=ff0000>You have a funny feeling like you would have been followed...</col>");
            WorldBroadcasts.broadcast(player, BroadcastType.PET, this);
            return false;
        }
        if (player.getFollower() != null) {
            if (player.getInventory().addItem(item).isFailure()) {
                if (player.getBank().add(item).isFailure()) {
                    player.sendMessage("There was not enough space in your bank, and therefore the pet was lost.");
                    return false;
                }
                player.sendMessage("<col=ff0000>You have a funny feeling like you're being followed - The pet has " +
                        "been added to your bank.</col>");
                return false;
            }
            player.sendMessage("<col=ff0000>You feel something weird sneaking into your backpack.</col>");
            WorldBroadcasts.broadcast(player, BroadcastType.PET, this);
        } else {
            player.sendMessage("<col=ff0000>You have a funny feeling like you're being followed.</col>");
            player.setFollower(new Follower(petId, player));
            WorldBroadcasts.broadcast(player, BroadcastType.PET, this);
        }
        return true;
    }

    public static boolean isKQ(final Pet pet) {
        for (final BossPet p : kalphiteQueens) {
            if (p.equals(pet)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isYoungllef(final Pet pet) {
        for (final BossPet p : youngllefPets) {
            if (p.equals(pet)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSnakeling(final Pet pet) {
        for (final BossPet p : snakelings) {
            if (p.equals(pet)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGrotesqueGuardian(final Pet pet) {
        for (final BossPet p : grotesqueGuardians) {
            if (p.equals(pet)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVetion(final Pet pet) {
        for (final BossPet p : vetions) {
            if (p.equals(pet)) {
                return true;
            }
        }
        return false;
    }

    public int getItemId() {
        return itemId;
    }

    public int getPetId() {
        return petId;
    }

    public Class<? extends Dialogue> getDialogue() {
        return dialogue;
    }

    public int[] getBossIds() {
        return bossIds;
    }
}
