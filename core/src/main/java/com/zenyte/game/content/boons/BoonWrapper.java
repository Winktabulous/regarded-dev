package com.zenyte.game.content.boons;

import com.zenyte.game.content.boons.impl.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.Map;

public enum BoonWrapper {
    UnholyIntervention(com.zenyte.game.content.boons.impl.UnholyIntervention.class, 1),
    BarbarianFisher(com.zenyte.game.content.boons.impl.BarbarianFisher.class, 2),
    HardWorkPaysOff(com.zenyte.game.content.boons.impl.HardWorkPaysOff.class, 3),
    BoneCruncher(com.zenyte.game.content.boons.impl.BoneCruncher.class,4),
    BountifulSacrifice(com.zenyte.game.content.boons.impl.BountifulSacrifice.class,5),
    NoOnesHome(com.zenyte.game.content.boons.impl.NoOnesHome.class, 6),
    DrawPartner(com.zenyte.game.content.boons.impl.DrawPartner.class, 7),
    SliceNDice(com.zenyte.game.content.boons.impl.SliceNDice.class, 8),
    DharoksBlessing(com.zenyte.game.content.boons.impl.DharoksBlessing.class, 9),
    MasterOfTheCraft(com.zenyte.game.content.boons.impl.MasterOfTheCraft.class, 10),
    RevItUp(com.zenyte.game.content.boons.impl.RevItUp.class, 11),
    FirstImpressions(com.zenyte.game.content.boons.impl.FirstImpressions.class, 12),
    ClueCollector(com.zenyte.game.content.boons.impl.ClueCollector.class, 13),
    NoPetDebt(com.zenyte.game.content.boons.impl.NoPetDebt.class, 14),
    CrystalCatalyst(com.zenyte.game.content.boons.impl.CrystalCatalyst.class, 15),
    ImRubberYoureGlue(com.zenyte.game.content.boons.impl.ImRubberYoureGlue.class, 16),
    LunarEnthusiast(com.zenyte.game.content.boons.impl.LunarEnthusiast.class, 17),
    HammerDown(com.zenyte.game.content.boons.impl.HammerDown.class, 18),
    SlayersSpite(com.zenyte.game.content.boons.impl.SlayersSpite.class, 19),
    DagaWHO(com.zenyte.game.content.boons.impl.DagaWHO.class, 20),
    IceForTheEyeless(com.zenyte.game.content.boons.impl.IceForTheEyeless.class, 21),
    RunForrestRun(com.zenyte.game.content.boons.impl.RunForrestRun.class, 22),
    LessIsMore(com.zenyte.game.content.boons.impl.LessIsMore.class, 23),
    HoarderMentality(com.zenyte.game.content.boons.impl.HoarderMentality.class, 24),
    CorporealScrutiny(com.zenyte.game.content.boons.impl.CorporealScrutiny.class, 25),
    CryptKeeper(com.zenyte.game.content.boons.impl.CryptKeeper.class, 26),
    RelentlessPrecision(com.zenyte.game.content.boons.impl.RelentlessPrecision.class, 27),
    FourSure(com.zenyte.game.content.boons.impl.FourSure.class, 28),
    DivineHealing(com.zenyte.game.content.boons.impl.DivineHealing.class, 29),
    IWantItAll(com.zenyte.game.content.boons.impl.IWantItAll.class, 30),
    DoubleTap(com.zenyte.game.content.boons.impl.DoubleTap.class, 31),
    Locksmith(com.zenyte.game.content.boons.impl.Locksmith.class, 32),
    LethalAttunement(com.zenyte.game.content.boons.impl.LethalAttunement.class, 33),
    CrushingBlow(com.zenyte.game.content.boons.impl.CrushingBlow.class, 34),
    SousChef(com.zenyte.game.content.boons.impl.SousChef.class, 35),
    Woodsman(com.zenyte.game.content.boons.impl.Woodsman.class, 36),
    Pyromaniac(com.zenyte.game.content.boons.impl.Pyromaniac.class, 37),
    Mixologist(com.zenyte.game.content.boons.impl.Mixologist.class, 38),
    MinerFortyNiner(com.zenyte.game.content.boons.impl.MinerFortyNiner.class, 39),
    Botanist(com.zenyte.game.content.boons.impl.Botanist.class, 40),
    TrackStar(com.zenyte.game.content.boons.impl.TrackStar.class, 41),
    NoShardRequired(com.zenyte.game.content.boons.impl.NoShardRequired.class, 42),
    SwissArmyMan(com.zenyte.game.content.boons.impl.SwissArmyMan.class, 43),
    SuperiorSorcery(com.zenyte.game.content.boons.impl.SuperiorSorcery.class, 44),
    SleightOfHand(com.zenyte.game.content.boons.impl.SleightOfHand.class, 45),
    SpecialBreed(com.zenyte.game.content.boons.impl.SpecialBreed.class, 46),
    AnimalTamer(com.zenyte.game.content.boons.impl.AnimalTamer.class, 47),
    IVoted(com.zenyte.game.content.boons.impl.IVoted.class, 48),
    BurnBabyBurn(com.zenyte.game.content.boons.impl.BurnBabyBurn.class, 49),
    ArcaneKnowledge(com.zenyte.game.content.boons.impl.ArcaneKnowledge.class, 50),
    Alchoholic(com.zenyte.game.content.boons.impl.Alchoholic.class, 51),
    SustainedAggression(com.zenyte.game.content.boons.impl.SustainedAggression.class, 52),
    HoleyMoley(com.zenyte.game.content.boons.impl.HoleyMoley.class, 53),
    TheRedeemer(com.zenyte.game.content.boons.impl.TheRedeemer.class, 54),
    FamiliarsFortune(com.zenyte.game.content.boons.impl.FamiliarsFortune.class, 55),
    ThePointyEnd(com.zenyte.game.content.boons.impl.ThePointyEnd.class, 56),
    HashSlingingSlasher(com.zenyte.game.content.boons.impl.HashSlingingSlasher.class, 57),
    HolierThanThou(com.zenyte.game.content.boons.impl.HolierThanThou.class, 58),
    AshesToAshes(com.zenyte.game.content.boons.impl.AshesToAshes.class, 59),
    BrawnOfJustice(com.zenyte.game.content.boons.impl.BrawnOfJustice.class, 60),
    VigourOfInquisition(com.zenyte.game.content.boons.impl.VigourOfInquisition.class, 61),
    DoubleChins(com.zenyte.game.content.boons.impl.DoubleChins.class, 62),
    EndlessQuiver(com.zenyte.game.content.boons.impl.EndlessQuiver.class, 63),
    InfallibleShackles(com.zenyte.game.content.boons.impl.InfallibleShackles.class, 64),
    IgnoranceIsBliss(com.zenyte.game.content.boons.impl.IgnoranceIsBliss.class, 65),
    ContractKiller(com.zenyte.game.content.boons.impl.ContractKiller.class, 66),
    SlayersFavor(com.zenyte.game.content.boons.impl.SlayersFavor.class, 67),
    FarmersFortune(com.zenyte.game.content.boons.impl.FarmersFortune.class, 68),
    MinionsMight(com.zenyte.game.content.boons.impl.MinionsMight.class, 69),
    SoulStealer(SoulStealer.class, 70),
    SlayersSovereignty(com.zenyte.game.content.boons.impl.SlayersSovereignty.class, 71),
    HolyInterventionI(com.zenyte.game.content.boons.impl.HolyInterventionI.class, 72),
    HolyInterventionII(com.zenyte.game.content.boons.impl.HolyInterventionII.class, 73),
    HolyInterventionIII(com.zenyte.game.content.boons.impl.HolyInterventionIII.class, 74),
    NoShardRequiredII(com.zenyte.game.content.boons.impl.NoShardRequiredII.class, 75),
    NoShardRequiredIII(com.zenyte.game.content.boons.impl.NoShardRequiredIII.class, 76),
    TheLegendaryFisherman(com.zenyte.game.content.boons.impl.TheLegendaryFisherman.class, 77),
    CantBeAxed(com.zenyte.game.content.boons.impl.CantBeAxed.class, 78),
    EyeDontSeeYou(com.zenyte.game.content.boons.impl.EyeDontSeeYou.class, 79),
    AllGassedUp(com.zenyte.game.content.boons.impl.AllGassedUp.class, 80),
    JabbasRightHand(com.zenyte.game.content.boons.impl.JabbasRightHand.class, 81),
    //TorvasGluttony(com.zenyte.game.content.boons.impl.TorvasGluttony.class, 78),
    Enlightened(com.zenyte.game.content.boons.impl.Enlightened.class, -1),
    SoulStealerI(UnknownBoon.class, -1),
    Unknown(UnknownBoon.class, -1),
    SoulStealerII(UnknownBoon.class, -1),
    Enraged(com.zenyte.game.content.boons.impl.Enraged.class, -1),

    ;

    private final Class<? extends Boon> perk;
    private final int id;
    private static final BoonWrapper[] VALUES = values();
    public static final Map<Class<? extends Boon>, BoonWrapper> PERKS_BY_CLASS = new HashMap<>();
    private static final Map<String, BoonWrapper> PERKS_BY_NAME = new HashMap<>();
    public static final Int2ObjectOpenHashMap<BoonWrapper> PERKS_BY_ID = new Int2ObjectOpenHashMap<>();

    static {
        for (final BoonWrapper value : VALUES) {
            PERKS_BY_CLASS.put(value.getPerk(), value);
            PERKS_BY_NAME.put(value.name(), value);
            PERKS_BY_NAME.put("TwistedTradeOff", RelentlessPrecision);
            PERKS_BY_NAME.put("VitursOffering", FourSure);
            PERKS_BY_NAME.put("TumekensTribute", DivineHealing);
            PERKS_BY_ID.put(value.getId(), value);
        }
    }

    public static BoonWrapper get(final Class<? extends Boon> perk) {
        if(!PERKS_BY_CLASS.containsKey(perk))
            throw new RuntimeException("MISSING BOON WRAPPER: " + perk.getSimpleName());
        return PERKS_BY_CLASS.get(perk);
    }

    public static BoonWrapper getByString(final String perk) {
        return PERKS_BY_NAME.get(perk);
    }

    public static BoonWrapper get(final int id) {
        return PERKS_BY_ID.get(id);
    }

    BoonWrapper(Class<? extends Boon> perk, int id) {
        this.perk = perk;
        this.id = id;
    }

    public Class<? extends Boon> getPerk() {
        return perk;
    }

    public int getId() {
        return id;
    }
}
