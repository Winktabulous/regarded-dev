package com.zenyte.game.content.skills.hunter.node;

import com.zenyte.game.content.skills.hunter.plugins.ImplingJarPlugin;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.utils.Ordinal;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.CollectionUtils;
import net.runelite.api.NpcID;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tommeh | 2-11-2018 | 18:13
 * @author Corey 09/01/20
 */


@Ordinal
public enum Impling {
    
    BABY(NpcId.BABY_IMPLING, NpcId.BABY_IMPLING_1645, ImplingJarPlugin.ImplingJar.BABY, 17, 18, 20),
    YOUNG(NpcId.YOUNG_IMPLING, NpcId.YOUNG_IMPLING_1646, ImplingJarPlugin.ImplingJar.YOUNG, 22, 20, 22),
    GOURMET(NpcId.GOURMET_IMPLING, NpcId.GOURMET_IMPLING_1647, ImplingJarPlugin.ImplingJar.GOURMET, 28, 22, 24),
    EARTH(NpcId.EARTH_IMPLING, NpcId.EARTH_IMPLING_1648, ImplingJarPlugin.ImplingJar.EARTH, 36, 25, 27),
    ESSENCE(NpcId.ESSENCE_IMPLING, NpcId.ESSENCE_IMPLING_1649, ImplingJarPlugin.ImplingJar.ESSENCE, 42, 27, 29),
    ECLECTIC(NpcId.ECLECTIC_IMPLING, NpcId.ECLECTIC_IMPLING_1650, ImplingJarPlugin.ImplingJar.ECLECTIC, 50, 32, 34),
    NATURE(NpcId.NATURE_IMPLING, NpcId.NATURE_IMPLING_1651, ImplingJarPlugin.ImplingJar.NATURE, 58, 34, 36, true),
    MAGPIE(NpcId.MAGPIE_IMPLING, NpcId.MAGPIE_IMPLING_1652, ImplingJarPlugin.ImplingJar.MAGPIE, 65, 44, 216, true),
    NINJA(NpcId.NINJA_IMPLING, NpcId.NINJA_IMPLING_1653, ImplingJarPlugin.ImplingJar.NINJA, 74, 52, 240, true),
    CRYSTAL(NpcID.CRYSTAL_IMPLING, NpcId.CRYSTAL_IMPLING, ImplingJarPlugin.ImplingJar.CRYSTAL, 80, 80, 280, true, false),
    DRAGON(NpcId.DRAGON_IMPLING, NpcId.DRAGON_IMPLING_1654, ImplingJarPlugin.ImplingJar.DRAGON, 83, 65, 300, true),
    LUCKY(NpcID.LUCKY_IMPLING, NpcId.LUCKY_IMPLING_7302, ImplingJarPlugin.ImplingJar.LUCKY, 89, 80, 380, true),

    ;
    
    public static final Int2ObjectMap<Impling> implings;
    public static final List<Impling> notRareImplings;
    public static final String SURFACE_IMPLING_TRACKER_ATTRIBUTE_KEY = "implings_caught_";
    public static final String PURO_IMPLING_TRACKER_ATTRIBUTE_KEY = "implings_caught_puro_";
    public static final List<Impling> values =  Arrays.asList(values());
    
    static {
        CollectionUtils.populateMap(values, implings = new Int2ObjectOpenHashMap<>(values.size()), Impling::getNpcId);
        CollectionUtils.populateMap(values, implings, Impling::getWorldNpcId);

        notRareImplings = new ObjectArrayList<>(values.size());
        for (Impling value : values) {
            if (!value.isRare()) {
                notRareImplings.add(value);
            }
        }
    }
    
    private final int npcId;

    public int getWorldNpcId() {
        return worldNpcId;
    }

    private final int worldNpcId;
    private final ImplingJarPlugin.ImplingJar jar;
    private final int level;
    private final double experiencePuroPuro, experienceGielinor;
    private final boolean isRare;
    private final boolean autoSpawn;
    
    Impling(int puroNpcId, int worldNpcId, ImplingJarPlugin.ImplingJar jar, int level, double experiencePuroPuro, double experienceGielinor, boolean isRare, boolean autoSpawn) {
        this.npcId = puroNpcId;
        this.worldNpcId = worldNpcId;
        this.jar = jar;
        this.level = level;
        this.experiencePuroPuro = experiencePuroPuro;
        this.experienceGielinor = experienceGielinor;
        this.isRare = isRare;
        this.autoSpawn = autoSpawn;
    }

    Impling(int puroNpcId, int worldNpcId, ImplingJarPlugin.ImplingJar jar, int level, double experiencePuroPuro, double experienceGielinor, boolean isRare) {
        this(puroNpcId, worldNpcId, jar, level, experiencePuroPuro, experienceGielinor, isRare, true);
    }
    
    Impling(int npcId, int worldNpcId, ImplingJarPlugin.ImplingJar jar, int level, double experiencePuroPuro, double experienceGielinor) {
        this(npcId, worldNpcId, jar, level, experiencePuroPuro, experienceGielinor, false, true);
    }

    public static Impling get(final int npcId) {
        return implings.get(npcId);
    }
    
    public static boolean contains(final int npcId) {
        return implings.containsKey(npcId);
    }
    
    public String formattedName() {
        return StringUtils.capitalize(name().toLowerCase());
    }
    

    
    public int getNpcId() {
        return npcId;
    }
    
    public ImplingJarPlugin.ImplingJar getJar() {
        return jar;
    }
    
    public int getLevel() {
        return level;
    }
    
    public double getExperiencePuroPuro() {
        return experiencePuroPuro;
    }
    
    public double getExperienceGielinor() {
        return experienceGielinor;
    }
    
    public boolean isRare() {
        return isRare;
    }

    public boolean isAutoSpawn() {
        return autoSpawn;
    }
}
