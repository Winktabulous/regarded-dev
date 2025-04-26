package com.zenyte.game.content.skills.slayer;

import com.zenyte.game.GameConstants;
import com.zenyte.game.content.boss.grotesqueguardians.boss.Dusk;
import com.zenyte.game.content.kebos.alchemicalhydra.npc.AlchemicalHydra;
import com.zenyte.game.content.kebos.konar.map.KaruulmSlayerDungeon;
import com.zenyte.game.content.minigame.fightcaves.FightCaves;
import com.zenyte.game.content.minigame.fightcaves.npcs.TzTokJad;
import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.npc.impl.zuk.TzKalZuk;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.*;
import com.zenyte.game.world.region.area.godwars.GodwarsDungeonArea;
import com.zenyte.game.world.region.area.kourend.*;
import com.zenyte.game.world.region.area.taskonlyareas.KalphiteCave;
import com.zenyte.game.world.region.area.taskonlyareas.KrakenCove;
import com.zenyte.game.world.region.area.taskonlyareas.StrongholdSlayerDungeon;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import mgi.utilities.StringFormatUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static com.zenyte.game.content.skills.slayer.SlayerMaster.*;

/**
 * @author Kris | 26. okt 2017 : 11:27.54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>} TODO: Check over the quantities
 * as mid-masters are all
 * the same and not equal to RS. TODO: Read over all of the names and make sure there are no exceptions for
 * singularities. TODO: Add
 * more possible monsters that may be slain for all given tasks, a lot are missing, such as superiors.
 */
public enum RegularTask implements SlayerTask {
    BANSHEES(new Task[]{new Task(TURAEL, 8, 15, 50), new Task(MAZCHNA, 8, 40, 70), new Task(VANNAKA, 6, 60, 120),
            new Task(CHAELDAR, 5, 110, 170)}, 38, 15, 20, "Banshees are low level tortored souls, however they should" +
            " not be taken lightly. You'll need something to protect yourself from their screams.", "Banshee",
            "Twisted Banshee", "Screaming banshee", "Screaming twisted banshee"),
    BATS(new Task[]{new Task(TURAEL, 7, 15, 50), new Task(MAZCHNA, 7, 40, 70)}, 8, 1, 5, "Bats are low level flappy " +
            "monsters, commonly found within dark areas.", "Bat", "Giant bat", "Albino bat", "Deathwing") {
    },
    BIRDS(new Task[]{new Task(TURAEL, 6, 15, 50)}, 5, 1, 0,
            "Birds can be found all across " + GameConstants.SERVER_NAME + ". You should have no problems knocking " +
                    "them out.", "Duck", "Chicken", "Undead Chicken", "Rooster", "Seagull", "Penguin", "Entrana " +
            "firebird", "Bird", "Chompy bird", "Terrorbird", "Mounted Terrorbird", "Mounted terrorbird gnome",
            "Vulture", "Oomlie bird", "Baby roc") {
    },
    BEARS(new Task[]{new Task(TURAEL, 7, 15, 50), new Task(MAZCHNA, 6, 40, 70), new Task(KRYSTILIA, 6, 50, 100)}, 13,
            1, 13, "Bears are fierce monsters found within the forests of " + GameConstants.SERVER_NAME + ".", "Black" +
            " bear", "Bear cub", "Grizzly bear", "Grizzly bear cub", "Callisto", "Bear", "Reanimated bear", "Artio"),
    CAVE_BUGS(new Task[]{new Task(TURAEL, 8, 10, 20), new Task(MAZCHNA, 8, 40, 70), new Task(VANNAKA, 7, 60, 120)},
            63, 7, 0, "Cave bugs are found only in the darkest, moist caverns. Thou shall not forget a light source, " +
            "or thou will be bit by bugs.", "Cave bug"),
    CAVE_CRAWLERS(new Task[]{new Task(TURAEL, 8, 15, 20), new Task(MAZCHNA, 8, 40, 70), new Task(VANNAKA, 7, 60, 120)
            , new Task(CHAELDAR, 5, 110, 170)}, 37, 10, 10, "Cave crawlers are spiky poisonous critters. Slay them " +
            "fast or they'll heal right back up.", "Cave crawler", "Chasm crawler"),
    CAVE_SLIMES(new Task[]{new Task(TURAEL, 8, 10, 20), new Task(MAZCHNA, 8, 40, 70), new Task(VANNAKA, 7, 60, 120),
            new Task(CHAELDAR, 6, 110, 170)}, 62, 17, 15, "Cave slime, a poisonous blob can be found in the darkest " +
            "moist caverns. Beware of explosions!", "Cave slime"),
    COWS(new Task[]{new Task(TURAEL, 8, 15, 50)}, 6, 1, 5, "Cows are commonly in large farms. You should have no " +
            "problems defeating them.", "Cow", "Cow calf", "Undead cow", "Unicow"),
    CRAWLING_HANDS(new Task[]{new Task(TURAEL, 8, 15, 50), new Task(MAZCHNA, 8, 40, 70), new Task(VANNAKA, 6, 60,
            120)}, 39, 5, 0, "Crawling hands can only be found in the Slayer tower.", "Crawling hand", "Crushing hand"),
    DESERT_LIZARDS(new Task[]{new Task(TURAEL, 8, 15, 50), new Task(MAZCHNA, 8, 40, 70), new Task(VANNAKA, 7, 60,
            120), new Task(CHAELDAR, 5, 110, 170)}, 68, 22, 15, "Desert lizards, as their name suggests, can be found" +
            " in the desert. Don't forget to bring some ice coolers.", "Desert lizard", "Lizard", "Small lizard",
            "Sulphur Lizard"),
    DOGS(new Task[]{new Task(TURAEL, 7, 15, 50), new Task(MAZCHNA, 7, 40, 70)}, 22, 1, 15, "Dogs can be found all " +
            "across " + GameConstants.SERVER_NAME + ". Don't take them lightly though, while they may be a man's " +
            "best friend, they can become a fierce predators.", "Wild dog", "Guard dog", "Jackal", "Dog", "Reanimated" +
            " dog"),
    DWARVES(new Task[]{new Task(TURAEL, 7, 15, 50)}, 57, 1, 6, "Dwarves are characterised by their short stature. " +
            "They can often be found mining rocks or drinking beer.", "Dwarf", "Dwarf gang member", "Chaos dwarf",
            "Black Guard"),
    GHOSTS(new Task[]{new Task(TURAEL, 7, 15, 50), new Task(MAZCHNA, 7, 40, 70)}, 12, 1, 13, "Ghosts can be found in " +
            "all haunted, dark places. You should have no trouble defeating them.", "Ghost", "Tortured soul"),
    GOBLINS(new Task[]{new Task(TURAEL, 7, 15, 50)}, 2, 1, 0, "Goblins are mainly found in their village, however you" +
            " may also find some clusters in other locations across " + GameConstants.SERVER_NAME + ".", "Goblin",
            "Reanimated goblin", "Cave goblin guard", "Goblin Champion", "Sergeant strongstack", "Sergeant grimspike"
            , "Sergeant steelwill"),
    ICEFIENDS(new Task[]{new Task(TURAEL, 8, 15, 50)}, 75, 1, 20, "Icefiends are small demons often found in icy " +
            "areas. Their attacks shouldn't be underestimated, as deal damage in an odd fashion.", "Icefiend"),
    KALPHITE(new Task[]{new Task(TURAEL, 6, 15, 50), new Task(MAZCHNA, 6, 40, 70), new Task(VANNAKA, 7, 60, 120),
            new Task(CHAELDAR, 11, 110, 170), new Task(NIEVE, 9, 120, 185), new Task(DURADEL, 9, 130, 200),
            new Task(KONAR_QUO_MATEN, 9, 120, 170, KalphiteLair.class, KalphiteCave.class)}, 53, 1, 15, "Kalphite are" +
            " large bugs found within the depths of the desert. You might want to defeat them with a Keris.",
            "Kalphite worker", "Kalphite soldier", "Kalphite guardian", "Kalphite queen", "Reanimated kalphite"),
    MINOTAURS(new Task[]{new Task(TURAEL, 7, 10, 20)}, 76, 1, 7, "Minotaurs are accurate, yet weak creatures. Their " +
            "high accuracy however makes up for the lack of defence.", "Minotaur", "Reanimated minotaur"),
    MONKEYS(new Task[]{new Task(TURAEL, 6, 15, 50)}, 1, 1, 0, "Monkeys are small creatures often found inhabiting " +
            "leafy, thick jungles.", "Monkey", "Reanimated monkey", "Monkey guard", "Monkey archer", "Zombie monkey",
            "Aberab", "Awowogei", "Bonzara", "Daga", "Dugopul", "Duke", "Elder guard", "Hamab", "Ifaba", "Iwazaru",
            "Jumaane", "Kikazaru", "Kruk", "Mizaru", "Monkey Child", "Muruwoi", "Oobapohk", "Padulah", "Solihib",
            "Sleeping monkey", "Trefaji", "Tutab", "Uwogo", "Tortured gorilla", "Maniacal monkey", "Maniacal monkey archer", "Demonic gorilla"),
    RATS(new Task[]{new Task(TURAEL, 7, 15, 50)}, 3, 1, 0, "Rats are tiny animals found around cities, sewers and " +
            "dungeons. You should have no problems crushing them.", "Rat", "Giant rat", "Dungeon rat", "Crypt rat",
            "Brine rat", "Giant crypt rat", "Blessed giant rat", "Angry giant rat"),
    SCORPIONS(new Task[]{new Task(TURAEL, 7, 15, 50), new Task(MAZCHNA, 7, 40, 70), new Task(KRYSTILIA, 6, 60, 100)},
            7, 1, 7, "Scorpions are vicious monsters found all across " + GameConstants.SERVER_NAME + ".", "Scorpion"
            , "Reanimated scorpion", "King scorpion", "Poison scorpion", "Pit scorpion", "Scorpia", "Scorpia's " +
            "offspring"),
    SKELETONS(new Task[]{new Task(TURAEL, 7, 15, 50), new Task(MAZCHNA, 6, 40, 70), new Task(KRYSTILIA, 5, 60, 100)},
            11, 1, 15, "Skeletons are undead monsters found all across " + GameConstants.SERVER_NAME + ", often " +
            "inhabiting graveyards and dungeons.", "Skeleton", "Skeleton mage", "Skeleton champion", "Calvar'ion", "Calvar'ion reborn", "Vet'ion", "Vet" +
            "'ion reborn"),
    SPIDERS(new Task[]{new Task(TURAEL, 6, 15, 50), new Task(KRYSTILIA, 6, 60, 100)}, 4, 1, 0, "Spiders are usually " +
            "small, eight legged creatures found roaming all across " + GameConstants.SERVER_NAME + ".", "Spider",
            "Giant spider", "Shadow spider", "Giant crypt spider", "Venenatis", "Kalrag", "Jungle spider", "Deadly " +
            "red spider", "Blessed spider", "Crypt spider", "Poison spider", "Temple spider", "Sarachnis", "Spindel", "araxxor"),
    ARAXYTES(new Task[] {
        new Task(TURAEL, 6, 15, 30),
        new Task(NIEVE, 6, 40, 60),
        new Task(DURADEL, 6, 60, 80)
    }, 4, 92, 0,
        "Spiders are usually small, eight legged creatures found roaming all across " + GameConstants.SERVER_NAME + ".",
        "araxxor", "araxyte", "dreadborn araxyte"),
    WOLVES(new Task[]{new Task(TURAEL, 7, 15, 50), new Task(MAZCHNA, 7, 40, 70)}, 9, 1, 20, "Wolves are dog-like " +
            "animals usually found in a herd.", "Wolf", "White wolf", "Big wolf", "Dire wolf", "Jungle wolf", "Desert" +
            " wolf", "Ice wolf"),
    ZOMBIES(new Task[]{new Task(TURAEL, 7, 15, 50), new Task(MAZCHNA, 7, 40, 70)}, 10, 1, 10, "Zombies are undead " +
            "monsters often found in dungeons and sewers.", "Zombie", "Summoned zombie", "Zombie pirate", "Zombie " +
            "swab", "Zombies champion", "Zombie rat", "Monkey zombie", "Small zombie monkey", "Large zombie monkey",
            "Undead cow", "Undead chicken"),
    CATABLEPON(new Task[]{new Task(MAZCHNA, 8, 40, 70)}, 78, 1, 35, "Catablepons are cow-like creatures held within " +
            "the stronghold of security, draining the strength of their enemies.", "Catablepon"),
    COCKATRICE(new Task[]{new Task(MAZCHNA, 8, 40, 70), new Task(VANNAKA, 8, 60, 120), new Task(CHAELDAR, 6, 110,
            170)}, 44, 25, 25, "Cockatrice are winged reptiles with a piercing gaze. Don't forget a mirror shield " +
            "before fighting them.", player -> player.getSkills().getLevelForXp(SkillConstants.DEFENCE) >= 20,
            "Cockatrice", "Cockathrice"),
    EARTH_WARRIORS(new Task[]{new Task(MAZCHNA, 6, 40, 70), new Task(VANNAKA, 6, 60, 120), new Task(KRYSTILIA, 6, 75,
            130)}, 54, 1, 35, "Earth warriors are humanoid elementals of earth, found in a dark dirt-lined chamber in" +
            " Edgeville.", "Earth warrior"),
    FLESH_CRAWLERS(new Task[]{new Task(MAZCHNA, 7, 40, 70)}, 77, 1, 15, "Flesh crawlers are six-legged insects found " +
            "in the Stronghold of security.", "Flesh crawler"),
    GHOULS(new Task[]{new Task(MAZCHNA, 7, 40, 70), new Task(VANNAKA, 7, 60, 120)}, 23, 1, 25, "Ghouls are " +
            "zombie-like creatures found in dark places.", "Ghoul", "Ghoul champion"),
    HILL_GIANTS(new Task[]{new Task(MAZCHNA, 7, 40, 70), new Task(VANNAKA, 7, 60, 120)}, 14, 1, 25, "Hill giants are " +
            "large foes with high, but inaccurate attacks.", "Hill giant", "Giant champion", "Cyclops"),
    HOBGOBLIN(new Task[]{new Task(MAZCHNA, 7, 40, 70), new Task(VANNAKA, 7, 60, 120)}, 21, 1, 20, "Hobgoblins are " +
            "smelly creatures with strong attacks.", "Hobgoblin", "Hobgoblin champion"),
    ICE_WARRIORS(new Task[]{new Task(MAZCHNA, 7, 40, 70), new Task(VANNAKA, 7, 60, 120), new Task(KRYSTILIA, 7, 100,
            150)}, 19, 1, 45, "Ice warriors are cold-hearted elemental warriors, often found in icy caverns.", "Ice " +
            "warrior"),
    /*KILLERWATTS(new Task[] { new Task(MAZCHNA, 6, 40, 70), new Task(VANNAKA, 6, 60,
            120) }, 73, 37, 50, "Killerwatts are electrical creatures. You may want to insulte yourself from the " +
            "ground.", "Killerwatt"),*/
    MOGRES(new Task[]{new Task(MAZCHNA, 6, 40, 70), new Task(VANNAKA, 7, 60, 120), new Task(CHAELDAR, 6, 110, 170)},
            67, 32, 30, "Mogres are large foes often found inhabiting areas close to water.", "Mogre"),
    PYREFIEND(new Task[]{new Task(MAZCHNA, 8, 40, 70), new Task(VANNAKA, 8, 60, 120),
            new Task(CHAELDAR, 6, 110, 170)}, 47, 30, 25, "Pyrefiends are small demons of fire, striking magical " +
            "attacks close-up.", "Pyrefiend", "Flaming pyrelord"),
    ROCKSLUGS(new Task[]{new Task(MAZCHNA, 8, 40, 70), new Task(VANNAKA, 7, 60, 120),
            new Task(CHAELDAR, 5, 110, 170)}, 51, 20, 20, "Rockslugs are slimy small creatures who can only be " +
            "defeated with the use of brine sabre or salt.", "Rockslug", "Giant rockslug"),
    SHADES(new Task[]{new Task(MAZCHNA, 8, 40, 70), new Task(VANNAKA, 8, 60, 120)}, 64, 1, 30, "Shades are undead, " +
            "shadowy remains of a long departed soul.", "Shade", "Loar shade", "Phrin shade", "Riyl shade", "Asyn " +
            "shade", "Fiyr shade"),
    /*VAMPIRES(new Task[] { new Task(MAZCHNA, 6, 40, 70), new Task(VANNAKA, 7, 60,
            120) }, 34, 1, 35, "Vampires are dangerous humanoid creatures, biting anything they can. Bringing garlic " +
            "may be of great help.", "Vampire"),*/
    WALL_BEASTS(new Task[]{new Task(MAZCHNA, 7, 10, 20), new Task(VANNAKA, 6, 60, 120), new Task(CHAELDAR, 6, 110,
            170)}, 61, 35, 30,
            "Wall beasts are found in the darkest of caverns around " + GameConstants.SERVER_NAME + ". Bring a spiny " +
                    "helmet to avoid its deathly grasp.",
            player -> player.getSkills().getLevelForXp(SkillConstants.DEFENCE) >= 5, "Wall beast"),
    ABYSSAL_DEMONS(new Task[]{new Task(VANNAKA, 5, 60, 120), new Task(KRYSTILIA, 5, 75, 125), new Task(CHAELDAR, 12, 110, 170), new Task(NIEVE, 9, 120
            , 185), new Task(DURADEL, 12, 130, 250), new Task(KONAR_QUO_MATEN, 9, 120, 170, CatacombsOfKourend.class,
            SlayerTower.class)}, 42, 85, 85, "Abyssal demons are demons from the abyss, possessing the ability to " +
            "teleport itself as well as its target.", Range.of("Augment my abbies", 200, 250), "Abyssal demon",
            "Abyssal sire", "Greater abyssal demon", "Reanimated abyssal"),
    ABERRANT_SPECTRES(new Task[]{new Task(VANNAKA, 8, 60, 120), new Task(CHAELDAR, 8, 110, 170), new Task(NIEVE, 6,
            120, 185), new Task(DURADEL, 7, 130, 200), new Task(KONAR_QUO_MATEN, 6, 120, 170,
            CatacombsOfKourend.class, SlayerTower.class, StrongholdSlayerDungeon.class)}, 41, 60, 65, "Aberrant " +
            "spectres are large smelly ghosts. It's suggested that you use a nose peg to avoid the foul smell.",
            Range.of("Smell ya later", 200, 250), "Aberrant spectre", "Deviant spectre", "Abhorrent spectre",
            "Repugnant spectre"),
    ANKOU(new Task[]{new Task(VANNAKA, 7, 30, 60), new Task(NIEVE, 5, 50, 90), new Task(DURADEL, 5, 40, 130),
            new Task(KRYSTILIA, 6, 40, 130), new Task(KONAR_QUO_MATEN, 5, 50, 50, StrongholdOfSecurity.class,
            StrongholdSlayerDungeon.class, CatacombsOfKourend.class)}, 79, 1, 40, "Ankou are skeletal, ghostly undead" +
            " humanoids, found in numerous locations across " + GameConstants.SERVER_NAME + ".", Range.of("Ankou very" +
            " much", 91, 149), "Ankou"),
    BASILISKS(new Task[]{new Task(VANNAKA, 8, 60, 120), new Task(CHAELDAR, 7, 110, 170), new Task(KONAR_QUO_MATEN, 5, 110, 170, FremennikSlayerDungeon.class), new Task(NIEVE, 6, 120, 185), new Task(DURADEL, 7, 130, 200)},
            43, 40, 40, "Basilisks are deadly creatures with possessing the eyes of evil. A mirror shield is " +
            "suggested when fighting them.", player -> player.getSkills().getLevelForXp(SkillConstants.DEFENCE) >= 20
            , "Basilisk", "Monstrous basilisk", "Basilisk Knight"),
    BLUE_DRAGONS(new Task[]{new Task(VANNAKA, 7, 60, 120), new Task(CHAELDAR, 8, 110, 170), new Task(NIEVE, 4, 120,
            185), new Task(DURADEL, 4, 138, 150), new Task(KONAR_QUO_MATEN, 4, 120, 170, CatacombsOfKourend.class,
            TaverleyDungeon.class, MythsGuildBasement.class)}, 25, 1, 65, "Blue dragons are one of the weakest " +
            "chromatic dragons, but should not be underestimated as their breath can defeat even the strongest of " +
            "opponents.", "Blue dragon", "Baby blue dragon", "Vorkath", "Brutal blue dragon", "Baby blue dragon", 241
            , 242, 243),
    BLOODVELD(new Task[]{new Task(VANNAKA, 8, 60, 120), new Task(CHAELDAR, 8, 110, 170), new Task(NIEVE, 9, 120, 185)
            , new Task(DURADEL, 8, 130, 200), new Task(KONAR_QUO_MATEN, 9, 120, 170, CatacombsOfKourend.class,
            GodwarsDungeonArea.class, SlayerTower.class, StrongholdSlayerDungeon.class)}, 48, 50, 50, "Bloodveld are " +
            "creatures with a large tongue.", Range.of("Bleed me dry", 200, 250), "Bloodveld", "Mutated bloodveld",
            "Insatiable bloodveld", "Insatiable mutated bloodveld", "Reanimated bloodveld"),
    //BRINE_RATS(new Task[] { new Task(VANNAKA, 7, 60, 120), new Task(CHAELDAR, 7, 110, 170),
    //		new Task(NIEVE, 3, 160, 200) }, 84, 47, 45, "Brine rats are large, bald vermin found in dark dungeons.",
    //        "Brine rat"),
    BRONZE_DRAGONS(new Task[]{new Task(VANNAKA, 7, 30, 60), new Task(CHAELDAR, 8, 60, 120), new Task(KONAR_QUO_MATEN,
            5, 30, 50, CatacombsOfKourend.class, BrimhavenDungeon.class)}, 58, 1, 75, "Bronze dragons are the weakest" +
            " metallic dragons, but should definitely not be taken lightly.", Range.of("Pedal to the metals", 30, 50)
            , "Bronze dragon"),
    CROCODILES(new Task[]{new Task(VANNAKA, 6, 60, 120)}, 65, 1, 50, "Crocodiles are mostly found in desert areas, " +
            "near water. Their bite can be quite dangerous.", "Crocodile"),
    DAGANNOTH(new Task[]{new Task(VANNAKA, 7, 60, 120), new Task(CHAELDAR, 11, 110, 170), new Task(NIEVE, 8, 120,
            185), new Task(DURADEL, 9, 130, 200), new Task(KONAR_QUO_MATEN, 8, 120, 170, CatacombsOfKourend.class,
            LighthouseDungeon.class, WaterbirthDungeon.class)}, 35, 1, 75, "Dagannoth are sea monsters, often found " +
            "near Fremennik.", "Dagannoth fledgeling", "Dagannoth spawn", "Dagannoth rex", "Dagannoth prime",
            "Dagannoth supreme", "Dagannoth mother", "Dagannoth", "Reanimated dagannoth"),
    DUST_DEVILS(new Task[]{new Task(VANNAKA, 8, 60, 120), new Task(KRYSTILIA, 5, 75, 125), new Task(CHAELDAR, 9, 110, 170), new Task(NIEVE, 6, 120,
            185), new Task(DURADEL, 5, 130, 200), new Task(KONAR_QUO_MATEN, 6, 120, 170, CatacombsOfKourend.class,
            SmokeDungeonArea.class)}, 49, 65, 70, "Dust devils are creatures made of a lot of dust, sand and ash. You" +
            " might want to protect yourself from their dust with a face mask.", Range.of("To dust you shall return",
            200, 250), "Dust devil", "Choke devil"),
    /*ELVES(new Task[] { new Task(VANNAKA, 7, 60, 120), new Task(CHAELDAR, 8, 110, 170), new Task(NIEVE, 4, 60, 90),
    new Task(DURADEL, 4, 100,
            170) }, 56, 1, 70, "Elves are humanoid creatures found near Isafdar. They strike with accurate, high " +
            "hitting attacks.", "Elf warrior"),*/
    FEVER_SPIDERS(new Task[]{new Task(VANNAKA, 7, 60, 120), new Task(CHAELDAR, 7, 110, 170)}, 69, 42, 40, "Fever " +
            "spiders are large spiders known to only be found on Braindeath island. It's suggested that you protect " +
            "your hands with slayer gloves while fighting them, to avoid their fierce diseasing attacks.", "Fever " +
            "spider"),
    FIRE_GIANTS(new Task[]{new Task(VANNAKA, 7, 60, 120), new Task(CHAELDAR, 12, 110, 170), new Task(NIEVE, 9, 120,
            190), new Task(DURADEL, 7, 130, 200), new Task(KRYSTILIA, 7, 100, 150), new Task(KONAR_QUO_MATEN, 9, 120,
            170, KaruulmSlayerDungeon.class, BrimhavenDungeon.class, WaterfallDungeon.class,
            StrongholdSlayerDungeon.class, CatacombsOfKourend.class)}, 16, 1, 65, "Fire giants are large fiery foes " +
            "found all across " + GameConstants.SERVER_NAME + ".", "Fire giant"),
    GARGOYLES(new Task[]{new Task(VANNAKA, 5, 60, 120), new Task(CHAELDAR, 11, 110, 170), new Task(NIEVE, 6, 120,
            185), new Task(DURADEL, 8, 130, 244), new Task(KONAR_QUO_MATEN, 5, 120, 170, SlayerTower.class)}, 46, 75,
            80, "Gargoyles are large winged bat-like creatures known to only be found in the Slayer tower. You'll " +
            "need to finish them off with a rock hammer.", Range.of("Get smashed", 200, 250), "Gargoyle", "Dusk",
            "Marble gargoyle") {
        @Override
        public float getExperience(final NPC npc) {
            if (npc instanceof Dusk) {
                return 1350;
            }
            return npc.getMaxHitpoints() * (npc instanceof SuperiorNPC ? 10 : 1);
        }
    },
    GREEN_DRAGONS(new Task[]{new Task(VANNAKA, 6, 30, 60), new Task(KRYSTILIA, 4, 60, 100)}, 24, 1, 52, "Green " +
            "dragons are the weakest of chromatic dragons, found across the Wilderness.", "Green dragon", "Baby green" +
            " dragon", "Brutal green dragon", 5194, 5872, 5873),
    HARPIE_BUG_SWARMS(new Task[]{new Task(VANNAKA, 8, 30, 60), new Task(CHAELDAR, 6, 60, 120)}, 70, 33, 45, "Harpie " +
            "bug swarms are accurate, small insects bundled up. You'll need to wield a lit bug lantern to shed some " +
            "light on them and land a successful hit.",
            player -> player.getSkills().getLevelForXp(SkillConstants.FIREMAKING) >= 33, "Harpie bug swarm"),
    HELLHOUNDS(new Task[]{new Task(VANNAKA, 7, 60, 120), new Task(CHAELDAR, 9, 110, 170), new Task(NIEVE, 8, 120,
            185), new Task(DURADEL, 10, 130, 200), new Task(KRYSTILIA, 7, 70, 123), new Task(KONAR_QUO_MATEN, 8, 120,
            170, CatacombsOfKourend.class, StrongholdSlayerDungeon.class, TaverleyDungeon.class,
            WitchavenDungeon.class)}, 31, 1, 75, "Hellhounds are fierce demonic dogs, found in the darkest dungeons."
            , "Hellhound", "Cerberus"),
    ICE_GIANTS(new Task[]{new Task(VANNAKA, 7, 60, 120), new Task(KRYSTILIA, 6, 100, 160)}, 15, 1, 50, "Ice giants " +
            "are large foes only found in cold, icy caverns.", "Ice giant"),
    INFERNAL_MAGES(new Task[]{new Task(VANNAKA, 8, 60, 120), new Task(CHAELDAR, 7, 110, 170)}, 40, 45, 40, "Infernal " +
            "mages are evil magicians, only known to be found in the Slayer tower.", "Infernal mage", "Malevolent " +
            "mage"),
    JELLIES(new Task[]{new Task(VANNAKA, 8, 60, 120), new Task(KRYSTILIA, 5, 100, 150), new Task(CHAELDAR, 10, 110, 170), new Task(KONAR_QUO_MATEN, 6,
            120, 170, FremennikSlayerDungeon.class, CatacombsOfKourend.class)}, 50, 52, 57, "Jellies are wobbly cubes" +
            " of jelly, known to be found in the Fremennik slayer dungeon.", "Jelly", "Warped jelly", "Vitreous " +
            "jelly", "Vitreous warped jelly"),
    JUNGLE_HORRORS(new Task[]{new Task(VANNAKA, 8, 60, 120), new Task(CHAELDAR, 10, 110, 170)}, 81, 1, 65, "Jungle " +
            "horrors are horrible emaciated apes, found on the Mos Le'Harmless island.", "Jungle horror"),
    KURASK(new Task[]{new Task(VANNAKA, 7, 60, 120), new Task(CHAELDAR, 12, 110, 170), new Task(NIEVE, 3, 120, 185),
            new Task(DURADEL, 4, 130, 200), new Task(KONAR_QUO_MATEN, 3, 120, 170, FremennikSlayerDungeon.class)}, 45
            , 70, 65, "Kurasks are large horned apes, dealing inaccurate but wild strikes. You'll need a leaf-bladed" +
            " spear to damage them.", "Kurask", "King kurask"),
    LESSER_DEMONS(new Task[]{new Task(VANNAKA, 7, 60, 120), new Task(CHAELDAR, 9, 110, 170), new Task(KRYSTILIA, 6,
            80, 120)}, 28, 1, 60, "Lesser demons are one of the smallest standard demons, yet they shouldn't be " +
            "underestimated.", "Lesser demon"),
    /*MOLANISKS(new Task[]{
            new Task(VANNAKA, 7, 60, 120), new Task(CHAELDAR, 6, 110, 170)
    }, 87, 39, 50,
            "Molanisks are mole-like beings only found in dark caverns. A slayer bell is required" +
                    " to attract their attention.",
            "Molanisk"),*/
    MOSS_GIANTS(new Task[]{new Task(VANNAKA, 7, 60, 120)}, 17, 1, 40, "Moss giants are large, humanoid creatures tied" +
            " to nature. They can mostly be found in dungeons and caverns.", "Moss giant"),
    NECHRYAEL(new Task[]{new Task(VANNAKA, 5, 60, 120), new Task(KRYSTILIA, 5, 75, 125), new Task(CHAELDAR, 12, 110, 170), new Task(NIEVE, 7, 120,
            185), new Task(DURADEL, 9, 130, 200), new Task(KONAR_QUO_MATEN, 7, 110, 110, CatacombsOfKourend.class,
            SlayerTower.class)}, 52, 80, 85, "Nechryael are humanoid demons, possessing the power to spawn small " +
            "death spawns.", Range.of("Nechs please", 200, 250), "Nechryael", "Nechryarch", "Greater Nechryael"),
    OGRES(new Task[]{new Task(VANNAKA, 7, 60, 120)}, 20, 1, 40,
            "Ogres are large humanoids found all across " + GameConstants.SERVER_NAME + ".", "Ogre", "Ogre chieftain"
            , "Enclave ogre", "Reanimated ogre", "Ogress warrior", "Ogress shaman"),
    OTHERWORDLY_BEINGS(new Task[]{new Task(VANNAKA, 8, 30, 60)}, 55, 1, 40, "Otherwordly beings are ghostly, " +
            "invisible creatures only to be found on Zanaris.", "Otherworldly being"),
    /*SEA_SNAKES(new Task[] { new Task(VANNAKA, 6, 30,
            60) }, 71, 1, 50, "Sea snakes are poisonous snakes found on the island of Miscellania.", "Sea snake " +
            "hatchling", "Sea snake young", "Giant sea snake"),*/
	/*SHADOW_WARRIORS(new Task[] { new Task(VANNAKA, 8, 60, 120),
			new Task(CHAELDAR, 8, 110, 170) }, 32, 1, 60, "Shadow warriors are deceased knights in supernatural form.",
            "Shadow warrior"),*/
    SPIRITUAL_CREATURES(new Task[]{new Task(VANNAKA, 5, 60, 120), new Task(CHAELDAR, 10, 110, 170), new Task(NIEVE, 6
            , 120, 240), new Task(DURADEL, 7, 130, 200), new Task(KRYSTILIA, 6, 100, 150)}, 89, 63, 60, "Spiritual " +
            "creatures are religious monsters found in the god wars dungeon.", "Spiritual ranger", "Spiritual mage",
            "Spiritual warrior"),
    /*TERROR_DOGS(new Task[] { new Task(VANNAKA, 6, 60,
            120) }, 86, 40, 60, "Terror dogs are terrifying dog-like beasts found in the lair of Tarn Razorlor.",
            "Terror dog"),*/
    TROLLS(new Task[]{new Task(VANNAKA, 7, 60, 120), new Task(CHAELDAR, 11, 110, 170), new Task(NIEVE, 6, 120, 185),
            new Task(DURADEL, 6, 130, 200), new Task(KONAR_QUO_MATEN, 6, 120, 170, TrollStrongholdArea.class,
            Keldagrim.class, DeathPlateau.class)}, 18, 1, 60,
            "Trolls are ugly, small creatures found all across " + GameConstants.SERVER_NAME + ".", "Mountain troll",
            "Troll", "Ice troll grunt", "Ice troll runt", "Ice troll male", "Ice troll female", "Ice troll", "River " +
            "troll", "Troll spectator", "Thrower troll", "Stick", "Kraka", "Pee Hat", "Troll general", "Reanimated " +
            "troll"),
    TUROTH(new Task[]{new Task(VANNAKA, 8, 60, 120), new Task(CHAELDAR, 10, 110, 170), new Task(NIEVE, 3, 120, 185),
            new Task(KONAR_QUO_MATEN, 3, 120, 170, FremennikSlayerDungeon.class)}, 36, 55, 60, "Turoth are " +
            "three-legged hairless creatures found in dark dungeons. You'll need a leaf-bladed spear to inflict " +
            "damage on them.", "Turoth"),
    WEREWOLVES(new Task[]{new Task(VANNAKA, 7, 60, 120)}, 33, 1, 60, "Werewolves are part human, part wolf creatures " +
            "found in Canifis. You might find wolfbane to be quite useful there.", "Werewolf"),
    AVIANSIES(new Task[]{new Task(CHAELDAR, 8, 110, 170), new Task(NIEVE, 6, 120, 200), new Task(DURADEL, 8, 120,
            250), new Task(KRYSTILIA, 7, 80, 150), new Task(KONAR_QUO_MATEN, 6, 120, 170, GodwarsDungeonArea.class)},
            94, 1, 0, "Aviansies are strong vicious birds known to only be found in the Godwars dungeon.",
            player -> player.getSlayer().isUnlocked("Watch the Birdie"), Range.of("Birds of a feather", 130, 250),
            "Aviansie", "Kree'arra", "Flight kilisa", "Wingman skree", "Flockleader geerin", "Reanimated aviansie"),
    BLACK_DEMONS(new Task[]{new Task(CHAELDAR, 8, 110, 170), new Task(NIEVE, 9, 120, 185), new Task(DURADEL, 8, 130,
            200), new Task(KRYSTILIA, 7, 100, 150), new Task(KONAR_QUO_MATEN, 9, 120, 170, CatacombsOfKourend.class,
            ChasmOfFire.class, TaverleyDungeon.class, BrimhavenDungeon.class)}, 30, 1, 80, "Black demons are large " +
            "winged demons, found in many areas around " + GameConstants.SERVER_NAME + ".", Range.of("It's dark in " +
            "here", 200, 250), "Black demon", "Demonic gorilla", "Balfrug kreeyath", "Skotizo", "Porazdir"),
    CAVE_HORRORS(new Task[]{new Task(CHAELDAR, 10, 110, 170), new Task(NIEVE, 5, 120, 185), new Task(DURADEL, 4, 80,
            200)}, 80, 58, 85, "Cave horrors are large horrible apes found on the Mos Le'Harmless island. You'll " +
            "need a witchwood icon to fight them.", Range.of("Horrorific", 200, 250), "Cave horror", "Cave " +
            "abomination", "Reanimated horror"),
    //Kraken quantities doubled as they need to be the same as they were in OSRS due to task-requirement to slay.
    CAVE_KRAKEN(new Task[]{new Task(CHAELDAR, 12, Slayer.HALVED_QUANTITIES ? 220 : 110, Slayer.HALVED_QUANTITIES ?
            340 : 170), new Task(NIEVE, 6, Slayer.HALVED_QUANTITIES ? 200 : 100,
            Slayer.HALVED_QUANTITIES ? 240 : 120), new Task(DURADEL, 9, Slayer.HALVED_QUANTITIES ? 200 : 100,
            Slayer.HALVED_QUANTITIES ? 240 : 120), new Task(KONAR_QUO_MATEN, 9, Slayer.HALVED_QUANTITIES ? 160 : 80,
            Slayer.HALVED_QUANTITIES ? 200 : 100, KrakenCove.class)}, 92, 87, 80, "Cave kraken are large monsters " +
            "found in their cove. You'll find that magic is the most suitable style for defeating them.", Range.of(
            "Krack on", Slayer.HALVED_QUANTITIES ? 300 : 150, Slayer.HALVED_QUANTITIES ? 500 : 250), "Cave " +
            "kraken", "Kraken", "Whirlpool"),
    FOSSIL_ISLAND_WYVERN(new Task[]{new Task(CHAELDAR, 7, 110, 170), new Task(NIEVE, 5, 20, 60), new Task(DURADEL, 5,
            20, 60)}, 106, 66, 80, "Fossil island wyvern are flying wyverns found only on the Fossil island. They're" +
            " large and have many different shapes.",
            player -> !player.getSlayer().isUnlocked("Stop the Wyvern") || player.getSettings().valueOf(Setting.STOP_THE_WYVERN_SLAYER_REWARD) != 0, Range.of("Wyver-nother two", 55, 75), "Spitting wyvern", "Taloned wyvern", "Long-tailed wyvern", "Ancient wyvern"),
    GREATER_DEMONS(new Task[]{new Task(CHAELDAR, 8, 110, 170), new Task(NIEVE, 7, 120, 185), new Task(DURADEL, 9, 130
            , 200), new Task(KRYSTILIA, 8, 100, 150), new Task(KONAR_QUO_MATEN, 7, 120, 170,
            KaruulmSlayerDungeon.class, CatacombsOfKourend.class, ChasmOfFire.class, BrimhavenDungeon.class)}, 29, 1,
            75, "Greater demons are large winged demons found all across " + GameConstants.SERVER_NAME + ".",
            Range.of("Greater challenge", 150, 200), "Tormented demon", "Greater demon", "K'ril tsutsaroth", "Tstanon Karlak", "Skotizo"),
    LIZARDMEN(new Task[]{new Task(CHAELDAR, 8, 110, 170), new Task(NIEVE, 8, 90, 120), new Task(DURADEL, 10, 130,
            210), new Task(KONAR_QUO_MATEN, 8, 90, 110, KourendBattlefront.class, LizardmanCanyon.class,
            LizardmanSettlement.class, KebosSwamp.class, MolchAndLizardmanTemple.class)}, 90, 1, 0, "Lizardmen are " +
            "reptilian humanoids found on their canyon and settlement.", player -> player.getSlayer().isUnlocked(
            "Reptile got ripped"), "Lizardman", "Lizardman shaman", "Lizardman brute"),
    MUTATED_ZYGOMITES(new Task[]{new Task(CHAELDAR, 7, 60, 120), new Task(NIEVE, 2, 11, 86), new Task(DURADEL, 2, 20,
            30), new Task(KONAR_QUO_MATEN, 2, 10, 25, ZanarisArea.class, FossilIsland.class)}, 74, 57, 60, "Mutated " +
            "zygomites are a giant mushroom found in Zanaris. You'll need a fungicide spray to finish them off.",
            "Zygomite", "Ancient zygomite", "Mutated zygomite"),
    IRON_DRAGONS(new Task[]{new Task(CHAELDAR, 12, 60, 120), new Task(NIEVE, 5, 25, 60), new Task(DURADEL, 5, 40, 60)
            , new Task(KONAR_QUO_MATEN, 5, 30, 50, CatacombsOfKourend.class, BrimhavenDungeon.class)}, 59, 1, 80,
            "Iron dragons are fierce metallic dragons found mainly in the Brimhaven dungeon.", Range.of("Pedal to the" +
            " metals", 60, 100), "Iron dragon"),
    SKELETAL_WYVERNS(new Task[]{new Task(CHAELDAR, 7, 110, 170), new Task(NIEVE, 5, 3, 70), new Task(DURADEL, 7, 20,
            50), new Task(KONAR_QUO_MATEN, 9, 50, 70, AsgarnianIceDungeon.class)}, 72, 72, 70, "Skeletal wyverns are " +
            "large winged reptiles, known to only be found in icy dungeons.", Range.of("Wyver-nother one", 50, 70),
            "Skeletal wyvern"),
    STEEL_DRAGONS(new Task[]{new Task(CHAELDAR, 9, 110, 170), new Task(NIEVE, 5, 30, 110), new Task(DURADEL, 7, 10,
            20), new Task(KONAR_QUO_MATEN, 5, 30, 50, CatacombsOfKourend.class, BrimhavenDungeon.class)}, 60, 1, 85,
            "Steel dragons are fierce metallic dragons found mainly in the Brimhaven dungeon.", Range.of("Pedal to " +
            "the metals", 40, 60), "Steel dragon"),
    TZHAAR(new Task[]{new Task(CHAELDAR, 8, 110, 170), new Task(NIEVE, 10, 130, 200),
            new Task(DURADEL, 10, 130, 200)}, 96, 1, 0, "TzHaar are golem-like creatures found inhabiting the city of" +
            " fire.", player -> player.getSlayer().isUnlocked("Hot stuff"), "TzHaar-Ket", "TzHaar-Xil", "TzHaar-Mej",
            "TzHaar-Hur", "Tz-Kih", "Tz-Kek", "Tok-Xil", "Yt-MejKot", "Ket-Zek", "TzTok-Jad", "Yt-HurKot", "Jal-Nib",
            "Jal-MejRah", "Jal-Ak", "Jal-ImKot", "Jal-Xil", "Jal-Zek", "JalTok-Jad", "TzKal-Zuk", "Jal-MejJak",
            "Reanimated TzHaar") {
        @Override
        public boolean validate(final String name, final NPC npc) {
            RegionArea location = GlobalAreaManager.getArea(npc.getLocation());
            if (location instanceof Inferno || location instanceof FightCaves) {
                return false;
            }
            return super.validate(name, npc);
        }
    },
    BLACK_DRAGONS(new Task[]{new Task(NIEVE, 6, 10, 40), new Task(DURADEL, 9, 10, 20), new Task(KRYSTILIA, 7, 5, 20),
            new Task(KONAR_QUO_MATEN, 6, 10, 15, CatacombsOfKourend.class, MythsGuildBasement.class,
                    EvilChickenLair.class, TaverleyDungeon.class)}, 27, 1, 80, "Black dragons are the most fierce " +
            "chromatic dragons around " + GameConstants.SERVER_NAME + ". Do not treat them lightly.", Range.of("Fire " +
            "& Darkness", 40, 60), "Black dragon", "Baby black dragon", "King black dragon", "Brutal black dragon",
            1871, 1872, 7955),
    DARK_BEASTS(new Task[]{new Task(NIEVE, 5, 10, 20), new Task(DURADEL, 11, 10, 20), new Task(KONAR_QUO_MATEN, 8, 10
            , 15, MournerTunnels.class)}, 66, 90, 90, "Dark beasts are horned beasts from a darker dimension.",
            Range.of("Need more darkness", 100, 149), "Dark beast", "Night beast"),
    MITHRIL_DRAGONS(new Task[]{new Task(NIEVE, 5, 4, 9), new Task(DURADEL, 10, 5, 10), new Task(KONAR_QUO_MATEN, 5, 3
            , 6, AncientCavern.class)}, 93, 1, 0,
            "Mithril dragons are one of the most dangerous dragons in all of " + GameConstants.SERVER_NAME + ". You " +
                    "might want to use magic to defeat them.", player -> player.getSlayer().isUnlocked("I hope you " +
            "mith me"), Range.of("I really mith you", 20, 40), "Mithril dragon"),
    RED_DRAGONS(new Task[]{new Task(NIEVE, 5, 30, 80), new Task(DURADEL, 8, 30, 65), new Task(KONAR_QUO_MATEN, 5, 120
            , 170, BrimhavenDungeon.class, CatacombsOfKourend.class, MythsGuildBasement.class) //TODO forthos dung
    }, 26, 1, 68, "Red dragons are powerful chromatic dragons found in the Brimhaven dungeon.",
            player -> player.getSlayer().isUnlocked("Seeing red"), "Red dragon", "Baby red dragon", "Brutal red " +
            "dragon", 244, 245, 246),
    /*MINIONS_OF_SCABARAS(new Task[] { new Task(NIEVE, 4, 30,
            60) }, 85, 1, 0, "Scabarites are ancient egiptian creatures. Their bite can be very dangerous. They're " +
            "found in the Sophanem dungeon.", Range
                    .of("Get scabaright on it", 130, 170), "Scarabs", "Scarab swarms", "Locust riders", "Scarab
                    mages", "Giant scarab"),*/
    SMOKE_DEVILS(new Task[]{new Task(NIEVE, 7, 110, 185), new Task(DURADEL, 9, 130, 200), new Task(KONAR_QUO_MATEN, 7
            , 120, 170, YanilleUndergroundArea.class)}, 95, 93, 85, "Smoke devils are creatures made of smoke, dust " +
            "and dirt. They are found in their own dungeon. You might want to use some type of face protection to " +
            "protect yourself from the smoke.", "Smoke devil", "Thermonuclear smoke devil", "Nuclear smoke devil"),
    SUQAHS(new Task[]{new Task(NIEVE, 8, 130, 185), new Task(DURADEL, 8, 60, 90)}, 83, 1, 85, "Suqahs are strange " +
            "creatures unique to the Lunar isle.", Range.of("Suq-a-nother one", 185, 250), "Suqah"),
    WATERFIENDS(new Task[]{new Task(DURADEL, 2, 130, 200), new Task(KONAR_QUO_MATEN, 2, 120, 170, AncientCavern.class
            , KrakenCove.class)}, 88, 1, 75, "Waterfiends are fiendish embodiments of water. You'll find that " +
            "crushing them is the easiest way to slay them.", "Waterfiend"),
    BANDITS(new Task[]{new Task(KRYSTILIA, 6, 75, 125)}, 102, 1, 0, "Bandits are wilderness outlaws. You'll find " +
            "them at their camp.", "Bandit", "Guard bandit", "Black Heather", "Speedy Keith", "Donny the lad"),
    DARK_WARRIORS(new Task[]{new Task(KRYSTILIA, 6, 70, 125)}, 103, 1, 0, "Dark warriors are chaotic warriors found " +
            "in their own fortress.", "Dark warrior"),
    ENTS(new Task[]{new Task(KRYSTILIA, 6, 35, 60)}, 101, 1, 0, "Ents are large living trees near the Woodcutting " +
            "guild.", "Ent"),
    LAVA_DRAGONS(new Task[]{new Task(KRYSTILIA, 6, 35, 60)}, 104, 1, 0, "Lava dragons are large dragons found deep in" +
            " the Wilderness. You might want to protect yourself from their fierce fire.", "Lava dragon"),
    MAGIC_AXES(new Task[]{new Task(KRYSTILIA, 7, 70, 125)}, 91, 1, 0, "Magic axes are animated steel battleaxes, " +
            "found deep in the Wilderness. You'll need a lockpick to reach them.", "Magic axe"),
    MAMMOTHS(new Task[]{new Task(KRYSTILIA, 6, 75, 125)}, 99, 1, 0, "Mammoths are large beautiful creatures found in " +
            "the wilderness.", "Mammoth"),
    ROGUES(new Task[]{new Task(KRYSTILIA, 5, 75, 125)}, 100, 1, 0, "Rogues are humans gone rogue. They can be found " +
            "deep in the Wilderness.", "Rogue"),
    SPIRITUAL_MAGE(new Task[]{new Task(KRYSTILIA, 6, 130, 200)}, 89, 83, 0, "Spiritual mages are strong mages found " +
            "in the godwars dungeon.", Range.of("Spiritual fervour", 181, 250), "Spiritual mage"),
    REVENANTS(new Task[]{new Task(KRYSTILIA, 5, 40, 100)}, 107, 1, 30, "Revenants are the ghostly versions of " +
            "creatures slain during the God Wars. You may wish to protect yourself with a bracelet of ethereum.",
            "Revenant imp", "Revenant goblin", "Revenant pyrefiend", "Revenant hobgoblin", "Revenant cyclops",
            "Revenant hellhound", "Revenant demon", "Revenant ork", "Revenant dark beast", "Revenant knight",
            "Revenant dragon"),
    VAMPYRE(new Task[]{
            new Task(MAZCHNA, 5, 10, 20),
            new Task(VANNAKA, 5, 10, 20),
            new Task(CHAELDAR, 5, 80, 120),
            new Task(KONAR_QUO_MATEN, 5, 100, 160),
            new Task(NIEVE, 5, 110, 170),
            new Task(DURADEL, 5, 100, 210),
    }, 109, 0, 35, "", "Vampyre Juvinate", "Vampyre Juvenile", "Vyre", "Vyrewatch Sentinel", "Vyrewatch", "Feral Vampyre", "Vanstrom Klause"),

    ADAMANT_DRAGONS(new Task[]{new Task(NIEVE, 2, 3, 7), new Task(DURADEL, 2, 4, 9), new Task(KONAR_QUO_MATEN, 5, 3,
            6, LithkrenVault.class)}, 108, 1, 0, "Adamant dragons are metallic dragons created by Zorgoth. You may " +
            "wish to protect yourself against their powerful dragonfire.", Range.of("Ada'mind some more", 20, 30),
            "Adamant dragon"),
    RUNE_DRAGONS(new Task[]{new Task(NIEVE, 2, 3, 6), new Task(DURADEL, 2, 3, 8), new Task(KONAR_QUO_MATEN, 5, 3, 6,
            LithkrenVault.class)}, 109, 1, 0, "Rune dragons are metallic dragons created by Zorgoth. You may wish to " +
            "protect yourself against their powerful dragonfire.", Range.of("RUUUUUNE", 30, 60), "Rune dragon"),
    CHAOS_DRUIDS(new Task[]{new Task(KRYSTILIA, 5, 50, 85)}, 110, 1, 0, "Chaos druids are followers of Guthix and " +
            "zamorak. I've found that ranging them works the best.", "Chaos druid", "Elder chaos druid", "Reanimated" +
            " chaos druid"),
    DRAKES(new Task[]{new Task(KONAR_QUO_MATEN, 10, 125, 140, KaruulmSlayerDungeon.class)}, 112, 84, 0, "Drakes are " +
            "wingless dragons found in the middle level of the Karuulm Slayer Dungeon in Mount Karuulm.", "Drake"),
    HYDRAS(new Task[]{new Task(KONAR_QUO_MATEN, 10, 125, 190, KaruulmSlayerDungeon.class)}, 113, 95, 0, "Hydras are " +
            "draconic creatures found in the lower level of the Karuulm Slayer Dungeon in Mount Karuulm.", "Hydra",
            "Alchemical Hydra") {
        @Override
        public float getExperience(final NPC npc) {
            if (npc instanceof AlchemicalHydra) {
                return 1320;
            }
            return npc.getMaxHitpoints() * (npc instanceof SuperiorNPC ? 10 : 1);
        }
    },
    WYRMS(new Task[]{new Task(KONAR_QUO_MATEN, 10, 125, 190, KaruulmSlayerDungeon.class)}, 111, 62, 0, "Wyrms are " +
            "draconic creatures found in the lower level of the Karuulm Slayer Dungeon in Mount Karuulm.", "Wyrm"),
    BOSS(new Task[]{new Task(DURADEL, 8, 3, 35), new Task(NIEVE, 8, 3, 35), new Task(KRYSTILIA, 8, 3, 35),
            new Task(KONAR_QUO_MATEN, 8, 3, 35)}, 98, 1, 0, null, player -> player.getSlayer().isUnlocked("Like a " +
            "boss")),
    TZTOK_JAD(new Task[0], 97, 1, 0, "TzTok-Jad is a fierce monster found at the end of the Fight Caves.", "Tz-Kih",
            "Tz-Kek", "Tok-Xil", "Yt-MejKot", "Ket-Zek", "TzTok-Jad") {
        @Override
        public float getExperience(final NPC npc) {
            if (npc instanceof TzTokJad) {
                return 25250;
            }
            return super.getExperience(npc);
        }

        @Override
        public boolean validate(final String name, final NPC npc) {
            if (!(GlobalAreaManager.getArea(npc.getLocation()) instanceof FightCaves)) {
                return false;
            }
            return super.validate(name, npc);
        }

        @Override
        public String toString() {
            return "TzTok-Jad";
        }
    },
    TZKAL_ZUK(new Task[0], 105, 1, 0, "TzKal-Zuk is a fierce monster found at the end of the Inferno.", "TzKal-Zuk",
            "Jal-Nib", "Jal-MejRah", "Jal-Ak", "Jal-AkRek-Mej", "Jal-AkRek-Xil", "Jal-AkRek-Ket", "Jal-ImKot", "Jal" +
            "-Xil", "Jal-Zek", "JalTok-Jad", "Yt-HurKot", "Yt-HurKot", "Jal-MejJak") {
        @Override
        public float getExperience(final NPC npc) {
            if (npc instanceof TzKalZuk) {
                return 101890;
            }
            return super.getExperience(npc);
        }

        @Override
        public boolean validate(final String name, final NPC npc) {
            RegionArea location = GlobalAreaManager.getArea(npc.getLocation());
            if (!(location instanceof Inferno inferno)) {
                return false;
            }
            if (inferno.isPracticeMode()) {
                return false;
            }
            return super.validate(name, npc);
        }

        @Override
        public String toString() {
            return "TzKal-Zuk";
        }
    };

    private final Task[] taskSet;
    private final int slayerRequirement;
    private final int taskId;
    private final int combatRequirement;
    private final String[] monsters;
    private final int[] monsterIds;
    private final String tip;
    private final Predicate<Player> predicate;
    private final Range extendedRange;
    public static final RegularTask[] VALUES = values();

    RegularTask(@NotNull final Task[] taskSet, final int taskId, final int slayerRequirement,
                final int combatRequirement, final String tip, final Object... monsters) {
        this(taskSet, taskId, slayerRequirement, combatRequirement, tip, null, null, monsters);
    }

    RegularTask(@NotNull final Task[] taskSet, final int taskId, final int slayerRequirement,
                final int combatRequirement, final String tip, final Range range, final Object... monsters) {
        this(taskSet, taskId, slayerRequirement, combatRequirement, tip, null, range, monsters);
    }

    RegularTask(@NotNull final Task[] taskSet, final int taskId, final int slayerRequirement,
                final int combatRequirement, final String tip, final Predicate<Player> predicate,
                final Object... monsters) {
        this(taskSet, taskId, slayerRequirement, combatRequirement, tip, predicate, null, monsters);
    }

    RegularTask(@NotNull final Task[] taskSet, final int taskId, final int slayerRequirement,
                final int combatRequirement, final String tip, final Predicate<Player> predicate,
                final Range extendedRange, final Object... monsters) {
        this.taskSet = taskSet;
        this.taskId = taskId;
        this.combatRequirement = combatRequirement;
        this.slayerRequirement = slayerRequirement;
        this.tip = tip;
        this.predicate = predicate;
        this.extendedRange = extendedRange;
        final ObjectList<String> npcs = new ObjectArrayList<>(monsters.length);
        final IntArrayList intArray = new IntArrayList(monsters.length);
        for (final Object monster : monsters) {
            if (monster instanceof String) {
                npcs.add(((String) monster).toLowerCase());
            } else {
                intArray.add((int) monster);
            }
        }
        this.monsterIds = intArray.toIntArray();
        this.monsters = npcs.toArray(new String[0]);
    }

    public final Task getCertainTaskSet(final SlayerMaster slayerMaster) {
        for (final Task set : taskSet) {
            if (set.getSlayerMaster() == slayerMaster) {
                return set;
            }
        }
        return null;
    }

    public final String getSingularName() {
        if (equals(TZHAAR)) {
            return "TzHaar";
        }
        if (equals(DWARVES)) {
            return "Dwarf";
        }
        if (equals(WOLVES)) {
            return "Wolf";
        }
        if (equals(WEREWOLVES)) {
            return "Werewolf";
        }
        String name = name().toLowerCase().replace("_", " ");
        if (name.charAt(name.length() - 1) == 's') {
            name = name.substring(0, name.length() - 1);
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String isAssignable(final String name) {
        final String lowercaseName = name.toLowerCase();
        final BossTask bossTask = BossTask.MAPPED_VALUES.get(lowercaseName);
        if (bossTask != null) {
            return StringFormatUtil.formatString(bossTask.getTaskName());
        }
        for (final RegularTask assignable : VALUES) {
            for (final String matchingName : assignable.monsters) {
                if (matchingName.equals(name)) {
                    return assignable.getSingularName();
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (equals(TZHAAR)) {
            return "TzHaar";
        }
        final String name = name().toLowerCase().replace("_", " ");
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    @Override
    public boolean validate(final String name, final NPC npc) {
        if (ArrayUtils.contains(this.monsterIds, npc.getId())) {
            return true;
        }
        for (final String match : monsters) {
            if (name.equalsIgnoreCase(match)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public float getExperience(final NPC npc) {
        final BossTask bossAssignment = BossTask.MAPPED_VALUES.get(npc.getDefinitions().getName().toLowerCase());
        if (bossAssignment != null) {
            return bossAssignment.getXp();
        }
        return npc.getMaxHitpoints() * (npc instanceof SuperiorNPC ? 10 : 1);
    }

    public static final class Range {
        private final String extensionName;
        private final int min;
        private final int max;

        public static Range of(final String extensionName, final int min, final int max) {
            return new Range(extensionName, min, max);
        }

        public Range(String extensionName, int min, int max) {
            this.extensionName = extensionName;
            this.min = min;
            this.max = max;
        }

        public String getExtensionName() {
            return extensionName;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }
    }

    @Override
    public String getTaskName() {
        return toString();
    }

    @Override
    public String getEnumName() {
        return name();
    }

    public Task[] getTaskSet() {
        return taskSet;
    }

    public boolean hasTaskWith(SlayerMaster slayerMaster) {
        for (Task task : taskSet) {
            if (task.getSlayerMaster() == slayerMaster) {
                return true;
            }
        }
        return false;
    }

    public int getSlayerRequirement() {
        return slayerRequirement;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getCombatRequirement() {
        return combatRequirement;
    }

    public String[] getMonsters() {
        return monsters;
    }

    public int[] getMonsterIds() {
        return monsterIds;
    }

    public String getTip() {
        return tip;
    }

    public Predicate<Player> getPredicate() {
        return predicate;
    }

    public Range getExtendedRange() {
        return extendedRange;
    }

}
