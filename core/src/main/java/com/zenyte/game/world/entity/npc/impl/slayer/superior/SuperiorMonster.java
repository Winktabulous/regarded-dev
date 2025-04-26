package com.zenyte.game.world.entity.npc.impl.slayer.superior;

import com.zenyte.game.world.entity.npc.impl.slayer.superior.impl.*;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Kris | 27/05/2019 23:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum SuperiorMonster {
    CRAWLING_HAND(CrushingHand.class, "Crawling hand"),
    CAVE_CRAWLER(ChasmCrawler.class, "Cave crawler"),
    BANSHEE(ScreamingBanshee.class, "Banshee"),
    TWISTED_BANSHEE(ScreamingTwistedBanshee.class, "Twisted banshee"),
    ROCK_SLUG(GiantRockslug.class, "Rockslug"),
    COCKATRICE(Cockathrice.class, "Cockatrice"),
    PYREFIEND(FlamingPyrelord.class, "Pyrefiend"),
    BASILISK(MonstrousBasilisk.class, "Basilisk"),
    BASILISK_KNIGHT(BasiliskSentinel.class, "Basilisk Knight"),
    INFERNAL_MAGE(MalevolentMage.class, "Infernal mage"),
    BLOODVELD(InsatiableBloodveld.class, "Bloodveld"),
    MUTATED_BLOODVELD(InsatiableMutatedBloodveld.class, "Mutated bloodveld"),
    JELLY(VitreousJelly.class, "Jelly"),
    WARPED_JELLY(VitreousWarpedJelly.class, "Warped jelly"),
    CAVE_HORROR(CaveAbomination.class, "Cave horror"),
    ABERRANT_SPECTRE(AbhorrentSpectre.class, "Aberrant spectre"),
    DEVIANT_SPECTRE(RepugnantSpectre.class, "Deviant spectre"),
    DUST_DEVIL(ChokeDevil.class, "Dust devil"),
    KURASK(KingKurask.class, "Kurask"),
    GARGOYLE(MarbleGargoyle.class, "Gargoyle"),
    NECHRYAEL(Nechryarch.class, "Nechryael", "Greater nechryael"),
    ABYSSAL_DEMON(GreaterAbyssalDemon.class, "Abyssal demon"),
    DARK_BEAST(NightBeast.class, "Dark beast"),
    SMOKE_DEVIL(NuclearSmokeDevil.class, "Smoke devil");
    public static final IntSet superiorMonsters = new IntOpenHashSet(new int[] {7388, 7389, 7390, 7391, 7392, 7393, 7394, 7395, 7396, 7397, 7398, 7399, 7400, 7401, 7402, 7403, 7404, 7405, 7407, 7411, 7410, 7409, 7406});
    private static final SuperiorMonster[] values = values();
    private static final Object2ObjectMap<String, Class<? extends SuperiorNPC>> map = new Object2ObjectOpenHashMap<>(values.length);

    static {
        for (final SuperiorMonster value : values) {
            for (final String name : value.inferior) {
                map.put(name.toLowerCase(), value.superior);
            }
        }
    }

    private final Class<? extends SuperiorNPC> superior;
    private final String[] inferior;

    SuperiorMonster(final Class<? extends SuperiorNPC> superiorClass, final String... inferior) {
        this.superior = superiorClass;
        this.inferior = inferior;
    }

    @NotNull
    public static final Optional<Class<? extends SuperiorNPC>> getSuperior(@NotNull final String inferiorName) {
        return Optional.ofNullable(map.get(inferiorName.toLowerCase()));
    }
}
