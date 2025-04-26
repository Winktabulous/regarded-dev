package mgi.custom.christmas;

import com.zenyte.CacheManager;
import com.zenyte.ContentConstants;
import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.MapUtils;
import com.zenyte.game.world.region.Regions;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.custom.AnimationBase;
import mgi.custom.FramePacker;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Group;
import mgi.tools.parser.TypeParser;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.SpotAnimationDefinition;
import mgi.types.config.ObjectDefinitions;
import mgi.types.config.VarbitDefinitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.ByteBuffer;
import mgi.utilities.CollectionUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static mgi.tools.parser.TypeParser.*;

/**
 * @author Kris | 12/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ChristmasMapPacker {
    private static final String[] nullOps = new String[] {null, null, null, null, null};
    private final Int2IntMap transformedModels = new Int2IntOpenHashMap();
    private final Int2IntMap transformedObjects = new Int2IntOpenHashMap();
    private final Int2IntMap transformedNPCs = new Int2IntOpenHashMap();
    private final Int2IntMap transformedAnimations = new Int2IntOpenHashMap();
    private final Int2IntMap transformedVarbits = new Int2IntOpenHashMap();
    private final Int2IntMap transformedSounds = new Int2IntOpenHashMap();
    public static final IntOpenHashSet set = new IntOpenHashSet();
    public static int animationIndex = 15000;//Animations count from here on forward through ++.
    public static final int varbitIndex = 15000;
    public static final int varpIndex = 3700;
    public static int impModel;
    public static int baseObjectId = 50000;
    public static int objectId = baseObjectId;
    public static int modelId = 58000;
    public static int npcId = 15000;
    public static int itemId = 30100;
    public static int graphicsId = 2500;
    public static int soundEffectId = 7506;

    public void packFull() throws IOException {
        packAmbientSounds();
        packJingles();
        packSoundEffects();
        packVarbits();
        packBaseAnimations();
        bellsSwayAnimation();
        frozenNPCBaseAnimation();
        lanternSwayAnimation();
        stagsAnimation();
        fountainAnimation();
        grandfathersClockAnimation();
        scourgesBedAnimation();
        candleTableAnimation();
        santaAnimation();
        packModels();
        packGraphics();
        packObjects();
        packNPCs();
        packItems();
        packMusic();
        final Collection<WorldObject> objs = MapUtils.decode(new ByteBuffer(Files.readAllBytes(Paths.get("assets/christmas/objects.dat"))));
        objs.forEach(o -> {
            set.add(o.getId());
            o.setId(transformedObjects.getOrDefault(o.getId(), o.getId()));
        });
        final Collection<WorldObject> scourgeObjs = MapUtils.decode(new ByteBuffer(Files.readAllBytes(Paths.get("assets/christmas/scourge objects.dat"))));
        scourgeObjs.forEach(o -> {
            set.add(o.getId());
            o.setId(transformedObjects.getOrDefault(o.getId(), o.getId()));
        });
        packMapPre209(8276, Files.readAllBytes(Paths.get("assets/christmas/Land of snow floor.dat")), Regions.inject(MapUtils.encode(objs).getBuffer(), o -> false, new WorldObject(7389, 22, 0, new Location(18, 24, 0)), //Map icon for Christmas cupboard
        new WorldObject(2771, 22, 0, new Location(29, 18, 0))//Map icon for Fountain
        ));
        packMapPre209(9812, Files.readAllBytes(Paths.get("assets/christmas/Land of snow Scourge floor.dat")), Regions.inject(MapUtils.encode(scourgeObjs).getBuffer(), o -> false));
    }

    public void pack() throws IOException {
        packFull();
        //FramePacker.write();
        //AnimationBase.pack();
    }

    /**
     * Packs the animation base and the specific animations defined by the ids in the array which correspond to the animations in the 562 source(may also correspond to clunkier versions in the osrs
     * cache.
     *
     * @throws IOException if a file is missing.
     */
    private void packBaseAnimations() throws IOException {
        packAnimationBase(AnimationBase.EBE_BASE, new int[] {12681, 12683, 12644, 12682, 12680, 12679});
        packAnimationBase(AnimationBase.IMP_BASE, new int[] {169, 5285, 4287, 4626, 4288, 5218, 7574, 2955, 6826, 6986, 11059, 11903, 171, 2954, 4289, 4627, 4648, 168, 6628, 170, 6630, 172, 4618, 4616, 6629});
        final int[] humanoidAnims = new int[] {10020, 9869, 12673, 10021, 12689, 808, 12677, 6531, 12675, 12674, 12672, 3040, 9870, 12633, 822, 820, 821, 12676, 819, 12657};
        for (int i = 0; i < humanoidAnims.length; i++) {
            packAnimationBase(AnimationBase.valueOf("HUMANOID_BASE" + (1 + i)), new int[] {humanoidAnims[i]});
        }
        packAnimationBase(AnimationBase.TINY_THOM_BASE, new int[] {12685, 12684, 12639, 12656});
        packAnimationBase(AnimationBase.CAMEL_BASE, new int[] {9671, 9669, 1896, 9668, 9666, 9667, 9675, 9674, 9670, 9673, 9672, 6440, 6441});
        packAnimationBase(AnimationBase.SANTA_NPC_BASE, new int[] {11046, 12686, 11047});
        packAnimationBase(AnimationBase.GOBLIN_BASE, new int[] {6203, 6202});
        packAnimationBase(AnimationBase.REGULAR_CHAT_BASE, new int[] {9827});
        packAnimationBase(AnimationBase.HUMANOID_BASE_21, new int[] {6865});
        final int[] humanoidChatAnimations = new int[] {9752, 9765, 9780, 9781, 9811, 9840, 9851};
        for (int i = 0; i < humanoidChatAnimations.length; i++) {
            packAnimationBase(AnimationBase.valueOf("HUMANOID_BASE_" + (22 + i)), new int[] {humanoidChatAnimations[i]});
        }
        final int[] humanoidEventAnimations = new int[] {12643, 12645, 12658, 12664, 12667, 12665, 12661, 12663};
        for (int i = 0; i < humanoidEventAnimations.length; i++) {
            packAnimationBase(AnimationBase.valueOf("HUMANOID_BASE_" + (29 + i)), new int[] {humanoidEventAnimations[i]});
        }
        packAnimationBase(AnimationBase.FROZEN_NPC_DEFREEZE_BASE, new int[] {12635});
        packAnimationBase(AnimationBase.WATER_LANDING_BASE, new int[] {12646});
        packAnimationBase(AnimationBase.DRAMATIC_POINT_BASE, new int[] {12659});
        final int[] snowballHumanoidAnimations = new int[] {11044, 7531, 7529, 7530};
        for (int i = 0; i < snowballHumanoidAnimations.length; i++) {
            packAnimationBase(AnimationBase.valueOf("HUMANOID_BASE_" + (37 + i)), new int[] {snowballHumanoidAnimations[i]});
        }
        packAnimationBase(AnimationBase.FREEZE_GRAPHICS_BASE, new int[] {11045});
        packAnimationBase(AnimationBase.SNOWBALL_FLYING_BASE, new int[] {5065});
        packAnimationBase(AnimationBase.SNOWBALL_IMPACT_BASE, new int[] {5066});
        packAnimationBase(AnimationBase.IMP_EXPLOSION_BASE, new int[] {4628});
        packAnimationBase(AnimationBase.SCOURGES_GRAVE_ANIMATION, new int[] {12660});
        int[] humanoidTurnAnimations = new int[] {4612, 4613, 4614, 4615};
        for (int i = 0; i < humanoidTurnAnimations.length; i++) {
            packAnimationBase(AnimationBase.valueOf("HUMANOID_BASE_" + (41 + i)), new int[] {humanoidTurnAnimations[i]});
        }
        packAnimationBase(AnimationBase.POOF_EXPLOSION, new int[] {328});
        final int[] frozenAnimations = new int[] {12634, 12636};
        for (int i = 0; i < frozenAnimations.length; i++) {
            packAnimationBase(AnimationBase.FROZEN_OBJECT_BASE, new int[] {frozenAnimations[i]});
        }
        final int[] frozenNPCAnimations = new int[] {13655, 13637, 13654, 13638, 13651, 13640, 13652, 13641, 13653, 13642};
        for (int i = 0; i < frozenNPCAnimations.length; i++) {
            packAnimationBase(AnimationBase.valueOf("FROZEN_NPC_BASE_" + (1 + i)), new int[] {frozenNPCAnimations[i]});
        }
    }

    private void packAnimationBase(@NotNull final AnimationBase base, final int[] anims) throws IOException {
        for (int anim : anims) {
            final File file = new File("assets/christmas/animations/definitions/" + anim + ".dat");
            final AnimationDefinitions definitions = new AnimationDefinitions(anim, new ByteBuffer(FileUtils.readFileToByteArray(file)), true);
            final int[] ids = definitions.getFrameIds();
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    ids[i] = (ids[i] & 65535) | (base.getBaseId() << 16);
                }
            }
            if(definitions.getSoundEffects() != null) {
                for (AnimationDefinitions.Sound sound : definitions.getSoundEffects().values()) {
                    if(sound != null) {
                        sound.id = transformedSounds.getOrDefault(sound.id, sound.id);
                    }
                }
            }
            //System.err.println("Transformed sounds on animation " + anim + ", " + animationIndex);
            //Same animation, just needs to be adjusted to high-revision skeleton.
            if (anim == 6865) {
                definitions.setSoundEffects(AnimationDefinitions.get(anim).getSoundEffects());
            }
            if (anim == 12643) {
                definitions.setLeftHandItem(30106);
            } else if (anim == 7529) {
                definitions.setRightHandItem(30111);
            } else if (anim == 7530) {
                definitions.setRightHandItem(30112);
            }
            //System.err.println("Animation " + anim + " redirected to " + animationIndex);
            definitions.setId(animationIndex++);
            if (definitions.getId() == 15068) {
                definitions.setSoundEffects(null);//Remove Santa's "Ho ho ho!"
            } else if (definitions.getId() == 15094 || definitions.getId() == 15084) {
                definitions.setLeftHandItem(0);
                definitions.setRightHandItem(0);
            }
            transformedAnimations.put(anim, definitions.getId());
            definitions.pack();
            packFrames(ids);
        }
    }

    private final void packFrames(final int[] ids) {
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/christmas/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final void packAmbientSounds() throws IOException {
        TypeParser.packSound(7501, IOUtils.toByteArray(new FileInputStream(new File("assets/christmas/sound effects/ambient sounds/7501 - cupboard, radius 7.dat"))));
        TypeParser.packSound(7502, IOUtils.toByteArray(new FileInputStream(new File("assets/christmas/sound effects/ambient sounds/7502 - fountain, radius 10.dat"))));
        TypeParser.packSound(7503, IOUtils.toByteArray(new FileInputStream(new File("assets/christmas/sound effects/ambient sounds/7503 - wind whoosh, radius 10.dat"))));
        TypeParser.packSound(7504, IOUtils.toByteArray(new FileInputStream(new File("assets/christmas/sound effects/ambient sounds/7504 - burning, radius 1-2.dat"))));
        TypeParser.packSound(7505, IOUtils.toByteArray(new FileInputStream(new File("assets/christmas/sound effects/ambient sounds/7505 - clock, radius 2.dat"))));
    }

    private final void packJingles() throws IOException {
        CacheManager.getCache().getArchive(ArchiveType.JINGLES).addGroup(new Group(1000, new mgi.tools.jagcached.cache.File(new ByteBuffer(IOUtils.toByteArray(new FileInputStream(new File("assets/christmas/jingles/dramatic jingle.dat")))))));
    }

    private static final void packMusic() throws IOException {
        CacheManager.getCache().getArchive(ArchiveType.MUSIC).addGroup(new Group(2500, new mgi.tools.jagcached.cache.File(new ByteBuffer(IOUtils.toByteArray(new FileInputStream(new File("assets/christmas/music/Silent Knight soundtrack.dat")))))));
        CacheManager.getCache().getArchive(ArchiveType.MUSIC).addGroup(new Group(2501, new mgi.tools.jagcached.cache.File(new ByteBuffer(IOUtils.toByteArray(new FileInputStream(new File("assets/christmas/music/Smorgasbord soundtrack.dat")))))));
    }

    private final void packSoundEffects() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/christmas/sound effects/animation sounds/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final int originalId = entry.getIntKey();
            transformedSounds.put(originalId, soundEffectId);
            TypeParser.packSound(soundEffectId++, IOUtils.toByteArray(new FileInputStream(file)));
        }
    }

    private static final void bellsSwayAnimation() {
        final int[] lengths = new int[] {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
        final int[] ids = new int[] {185073704, 185073705, 185073706, 185073697, 185073699, 185073689, 185073691, 185073698, 185073692, 185073701, 185073688, 185073703, 185073693, 185073695, 185073700, 185073690, 185073694, 185073709, 185073696, 185073711, 185073708, 185073710, 185073702, 185073707};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (AnimationBase.CHRISTMAS_BELLS_SWAY.getBaseId() << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(4131);
        def.setId(11012);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setPrecedenceAnimating(0);
        def.setForcedPriority(5);
        def.setStretches(false);
        def.setReplyMode(2);
        def.setPriority(0);
        def.setFrameStep(24);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/christmas/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final void frozenNPCBaseAnimation() {
        final int[] lengths = new int[] {2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2};
        final int[] ids = new int[] {199884849, 199884829, 199884869, 199884879, 199884853, 199884923, 199884860, 199884800, 199884847, 199884859, 199884931, 199884826, 199884921, 199884825, 199884816, 199884840, 199884913, 199884811, 199884897, 199884901, 199884873, 199884827, 199884916, 199884821, 199884848, 199884904, 199884824, 199884814, 199884922, 199884801, 199884952, 199884919, 199884857, 199884809, 199884884, 199884915, 199884855, 199884932, 199884907, 199884852, 199884830, 199884925, 199884898, 199884817, 199884865, 199884894, 199884929, 199884882, 199884893, 199884872, 199884834, 199884867, 199884886, 199884902, 199884942, 199884818, 199884926, 199884864, 199884892, 199884941, 199884868, 199884881, 199884935};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (AnimationBase.FROZEN_NPCS_BASE.getBaseId() << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(4131);
        def.setId(11013);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setPrecedenceAnimating(0);
        def.setForcedPriority(5);
        def.setStretches(false);
        def.setReplyMode(2);
        def.setPriority(0);
        def.setFrameStep(63);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/christmas/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final void lanternSwayAnimation() {
        final int[] lengths = new int[] {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        final int[] ids = new int[] {125239310, 125239301, 125239305, 125239308, 125239298, 125239304, 125239300, 125239306, 125239311, 125239307, 125239309, 125239303, 125239297, 125239299, 125239302, 125239296};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (AnimationBase.LANTERN_SWAY_BASE.getBaseId() << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(4131);
        def.setId(11014);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setPrecedenceAnimating(0);
        def.setForcedPriority(5);
        def.setStretches(false);
        def.setReplyMode(2);
        def.setPriority(0);
        def.setFrameStep(16);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/christmas/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final void stagsAnimation() {
        final int[] lengths = new int[] {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
        final int[] ids = new int[] {125173824, 125173788, 125173812, 125173806, 125173790, 125173776, 125173796, 125173803, 125173822, 125173838, 125173784, 125173817, 125173770, 125173802, 125173801, 125173785, 125173824, 125173788, 125173812, 125173806, 125173790, 125173776, 125173796, 125173803, 125173822, 125173838, 125173784, 125173817, 125173770, 125173802, 125173801, 125173785, 125173824, 125173788, 125173812, 125173806, 125173790, 125173776, 125173796, 125173803, 125173822, 125173838, 125173784, 125173817, 125173770, 125173802, 125173801, 125173785, 125173814, 125173773, 125173798, 125173783, 125173810, 125173846, 125173800, 125173820, 125173837, 125173813, 125173804, 125173829, 125173811, 125173774, 125173845, 125173792, 125173824, 125173788, 125173812, 125173806, 125173790, 125173776, 125173796, 125173803, 125173822, 125173838, 125173784, 125173817, 125173770, 125173802, 125173801, 125173785, 125173824, 125173788, 125173812, 125173806, 125173790, 125173776, 125173796, 125173803, 125173822, 125173838, 125173784, 125173817, 125173770, 125173802, 125173801, 125173785, 125173824, 125173788, 125173812, 125173806, 125173790, 125173807, 125173805, 125173843, 125173830, 125173764, 125173784, 125173763, 125173825, 125173797, 125173801, 125173785};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (AnimationBase.STAGS_BASE.getBaseId() << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(4131);
        def.setId(11015);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setPrecedenceAnimating(0);
        def.setForcedPriority(5);
        def.setStretches(false);
        def.setReplyMode(2);
        def.setPriority(0);
        def.setFrameStep(112);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/christmas/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final void fountainAnimation() {
        final int[] lengths = new int[] {4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
        final int[] ids = new int[] {154402818, 154402816, 154402821, 154402822, 154402823, 154402824, 154402820, 154402819, 154402817, 154402825};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (AnimationBase.FOUNTAIN_BASE.getBaseId() << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(4131);
        def.setId(11016);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setPrecedenceAnimating(0);
        def.setForcedPriority(5);
        def.setStretches(false);
        def.setReplyMode(2);
        def.setPriority(0);
        def.setFrameStep(10);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/christmas/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final void grandfathersClockAnimation() {
        final int[] lengths = new int[] {6, 6, 6, 6, 6, 6, 6, 6};
        final int[] ids = new int[] {33292288, 33292289, 33292290, 33292291, 33292292, 33292293, 33292294, 33292295};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (AnimationBase.GRANDFATHERS_CLOCK_BASE.getBaseId() << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(4131);
        def.setId(11017);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setPrecedenceAnimating(0);
        def.setForcedPriority(5);
        def.setStretches(false);
        def.setReplyMode(2);
        def.setPriority(0);
        def.setFrameStep(8);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/christmas/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final void scourgesBedAnimation() {
        final int[] lengths = new int[] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
        final int[] ids = new int[] {199622752, 199622893, 199622766, 199622857, 199622815, 199622739, 199622661, 199622867, 199622797, 199622881, 199622754, 199622876, 199622818, 199622771, 199622731, 199622823, 199622662, 199622773, 199622868, 199622822, 199622737, 199622690, 199622799, 199622767, 199622709, 199622845};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (AnimationBase.SCOURGES_BED_BASE.getBaseId() << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(4131);
        def.setId(11018);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setPrecedenceAnimating(0);
        def.setForcedPriority(5);
        def.setStretches(false);
        def.setReplyMode(2);
        def.setPriority(0);
        def.setFrameStep(26);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/christmas/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final void candleTableAnimation() {
        final int[] lengths = new int[] {8, 8, 8, 8, 8, 8, 8};
        final int[] ids = new int[] {193331204, 193331201, 193331200, 193331202, 193331205, 193331206, 193331203};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (AnimationBase.CANDLE_TABLE_BASE.getBaseId() << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(4131);
        def.setId(11019);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setPrecedenceAnimating(0);
        def.setForcedPriority(5);
        def.setStretches(false);
        def.setReplyMode(2);
        def.setPriority(0);
        def.setFrameStep(7);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/christmas/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final void santaAnimation() {
        final int[] lengths = new int[] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
        final int[] ids = new int[] {177275027, 177274967, 177275018, 177274995, 177275017, 177275063, 177275009, 177274974, 177274980, 177275055, 177274984, 177275046, 177275053, 177274972, 177274976, 177275068, 177275025, 177275023, 177275044, 177275020, 177275066, 177275021, 177275072, 177274985, 177275049, 177274968, 177274971, 177274957, 177274998, 177275022, 177274978, 177274989, 177274964, 177275034, 177275008, 177275039, 177274956, 177274962, 177275042, 177274993, 177275027, 177274967, 177275018, 177274995, 177275017, 177275063, 177275009, 177274974, 177274980, 177275055, 177274984, 177275046, 177275053, 177274972, 177274976, 177275068, 177275025, 177275023, 177275044, 177275020, 177275029, 177275062, 177275052, 177275070, 177275026, 177275005, 177275003, 177274994, 177275019, 177275006, 177274969, 177275069, 177275002, 177275024, 177275035, 177275051, 177275058, 177275054, 177274986, 177275057, 177275027, 177274967, 177275018, 177274995, 177275017, 177275063, 177275009, 177274974, 177274980, 177275055, 177274984, 177275046, 177275053, 177274972, 177274976, 177275068, 177275025, 177275023, 177275044, 177275027, 177274967, 177275018, 177274995, 177275017, 177275063, 177275009, 177274974, 177274980, 177275055, 177274984, 177275046, 177275053, 177274972, 177274976, 177275068, 177275025, 177275023, 177275044, 177275020, 177275020, 177275016, 177275071, 177275000, 177274999, 177275013, 177274992, 177274997, 177275004, 177274981, 177274959, 177275064, 177275038, 177275033, 177274988, 177275056, 177274979, 177275061, 177274977, 177274966, 177275012, 177275031, 177274960, 177275074, 177275059, 177275050, 177274973, 177275073, 177274990, 177275011, 177275040, 177275014, 177275032, 177275045, 177275060, 177275047, 177274996, 177274987, 177275007, 177275065, 177274954, 177275027, 177274967, 177275018, 177274995, 177275017, 177275063, 177275009, 177274974, 177274980, 177275055, 177274984, 177275046, 177275053, 177274972, 177274976, 177275068, 177275025, 177275023, 177275044, 177275020, 177275037, 177275041, 177274963, 177275043, 177274965, 177274975, 177275015, 177275001, 177275036, 177275048, 177274982, 177275030, 177274970, 177275028, 177274991, 177274961, 177274958, 177274955, 177275010, 177274983};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (AnimationBase.SANTA_BASE.getBaseId() << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(4131);
        def.setId(11020);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setPrecedenceAnimating(0);
        def.setForcedPriority(5);
        def.setStretches(false);
        def.setReplyMode(2);
        def.setPriority(0);
        def.setFrameStep(200);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/christmas/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void packModels() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/christmas/models/"), null, false);
        final IntAVLTreeSet list = new IntAVLTreeSet();
        while (it.hasNext()) {
            final File file = it.next();
            final String name = file.getName().replace(".dat", "");
            final int id = Integer.parseInt(name);
            list.add(id);
        }
        for (final Integer originalId : list) {
            final File file = new File("assets/christmas/models/" + originalId + ".dat");
            transformedModels.put((int) originalId, modelId);
            packModel(modelId++, Files.readAllBytes(Paths.get(file.getPath())));
        }
        if (ContentConstants.CHRISTMAS) {
            it = FileUtils.iterateFiles(new File("assets/christmas/christmas-y entities models/"), null, false);
            final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
            while (it.hasNext()) {
                final File file = it.next();
                final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
                sortedMap.put(originalId, file);
            }
            for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
                final File file = entry.getValue();
                final byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
                packModel(Integer.parseInt(file.getName().replace(".dat", "")), bytes);
            }
        }
    }

    public void packVarbits() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/christmas/varbits/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        int lowestId = Integer.MAX_VALUE;
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            if (originalId < lowestId) {
                lowestId = originalId;
            }
            sortedMap.put(originalId, file);
        }
        assert varbitIndex > lowestId;
        final int varbitOffset = varbitIndex - lowestId;
        final Int2IntOpenHashMap varpMap = new Int2IntOpenHashMap();
        int currentVarp = varpIndex;
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final VarbitDefinitions defs = new VarbitDefinitions(entry.getIntKey() + varbitOffset, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            transformedVarbits.put(entry.getIntKey(), entry.getIntKey() + varbitOffset);
            final int currentBase = defs.getBaseVar();
            if (!varpMap.containsKey(currentBase)) {
                defs.setBaseVar(currentVarp);
                varpMap.put(currentBase, currentVarp);
                currentVarp++;
            } else {
                defs.setBaseVar(varpMap.get(currentBase));
            }
            defs.pack();
        }
        for (int i = 15024; i <= 15026; i++) {
            final VarbitDefinitions def = new VarbitDefinitions(i, new ByteBuffer(new byte[1]));
            def.setBaseVar(3702);
            def.setStartBit(5 + (i - 15024));
            def.setEndBit(5 + (i - 15024));
            def.pack();
        }
    }

    public void packObjects() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/christmas/object definitions/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        final ObjectArrayList<ObjectDefinitions> objSet = new ObjectArrayList<ObjectDefinitions>();
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final ObjectDefinitions def = new ObjectDefinitions(objectId, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            def.setVarbit(transformedVarbits.getOrDefault(def.getVarbit(), def.getVarbit()));
            objSet.add(def);
            final int originalId = entry.getIntKey();
            if (objectId == baseObjectId + 51) {
                def.setOptions(new String[] {"Pet", null, null, null, null});
            }
            if (objectId == baseObjectId + 77) {
                def.setOptions(new String[] {"Admire", null, null, null, null});
            }
            if (originalId == 47766) {
                def.setAmbientSoundId(7501);
                def.setAnInt455(7);
            } else if (originalId == 47747) {
                def.setAmbientSoundId(7502);
                def.setAnInt455(10);
            } else if (originalId == 2617) {
                def.setAmbientSoundId(7503);
                def.setAnInt455(10);
            } else if (originalId == 47872 || originalId == 47873 || originalId == 47875) {
                def.setAmbientSoundId(7504);
                def.setAnInt455(originalId == 47875 ? 1 : 2);
            } else if (originalId == 47900) {
                def.setAmbientSoundId(7505);
                def.setAnInt455(2);
            }
            transformedObjects.put(originalId, objectId);
            if (def.getTransformedIds() != null) {
                for (final int id : def.getTransformedIds()) {
                    if (id > 0) {
                        set.add(id);
                    }
                }
            }
            objectId++;
            final int[] models = def.getModels();
            if (models != null) {
                for (int i = 0; i < models.length; i++) {
                    models[i] = transformedModels.getOrDefault(models[i], models[i]);
                }
            }
            def.pack();
        }
        final Int2IntOpenHashMap map = new Int2IntOpenHashMap();
        for (final ObjectDefinitions obj : objSet) {
            if (obj.getTransformedIds() != null) {
                for (int i = 0; i < obj.getTransformedIds().length; i++) {
                    if (obj.getTransformedIds()[i] == -1) {
                        continue;
                    }
                    obj.getTransformedIds()[i] = transformedObjects.getOrDefault(obj.getTransformedIds()[i], obj.getTransformedIds()[i]);
                    if (obj.getFinalTransformation() != -1) {
                        obj.setFinalTransformation(transformedObjects.getOrDefault(obj.getFinalTransformation(), obj.getFinalTransformation()));
                    }
                }
            }
            if (obj.getAnimationId() > 0) {
                map.put(obj.getId(), obj.getAnimationId());
            }
        }
        for (final ObjectDefinitions obj : objSet) {
            if (obj.getTransformedIds() != null) {
                for (int i = 0; i < obj.getTransformedIds().length; i++) {
                    if (obj.getTransformedIds()[i] == -1) {
                        continue;
                    }
                    if (map.containsKey(obj.getTransformedIds()[i])) {
                        obj.setAnimationId(map.get(obj.getTransformedIds()[i]));
                        break;
                    }
                }
            }
            final int anim = obj.getAnimationId();
            if (anim == 12647) {
                obj.setAnimationId(11012);
            } else if (anim == 12634) {
                obj.setAnimationId(11013);
            } else if (anim == 7546) {
                obj.setAnimationId(11014);
            } else if (anim == 7552) {
                obj.setAnimationId(11015);
            } else if (anim == 9660) {
                obj.setAnimationId(11016);
            } else if (anim == 3511) {
                obj.setAnimationId(11017);
            } else if (anim == 12682) {
                obj.setAnimationId(11018);
            } else if (anim == 12298) {
                obj.setAnimationId(11019);
            } else if (anim == 12686) {
                obj.setAnimationId(11020);
            } else if (anim == 12660) {
                obj.setAnimationId(15101);
            }
            obj.pack();
        }
        ChristmasObject.CAVE_ENTRANCE.builder().options(nullOps).build().pack();
        ChristmasObject.HOLE.builder().options(nullOps).build().pack();
        ChristmasObject.HOLLOW_LOG.builder().options(nullOps).build().pack();
        ChristmasObject.ALT_HOLLOW_LOG.builder().options(nullOps).build().pack();
        ChristmasObject.CHEST.builder().options(nullOps).build().pack();
        ChristmasObject.TUNNEL.builder().options(nullOps).build().pack();
        ChristmasObject.SNOW_DRIFT.builder().options(nullOps).build().pack();
    }

    public void packGraphics() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/christmas/graphic definitions/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final SpotAnimationDefinition def = new SpotAnimationDefinition(graphicsId++, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            def.setModelId(transformedModels.getOrDefault(def.getModelId(), def.getModelId()));
            def.setAnimationId(transformedAnimations.getOrDefault(def.getAnimationId(), def.getAnimationId()));
            def.pack();
        }
    }

    public void packItems() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/christmas/item definitions/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final ItemDefinitions def = new ItemDefinitions(itemId++, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            def.setInventoryModelId(transformedModels.getOrDefault(def.getInventoryModelId(), def.getInventoryModelId()));
            def.setPrimaryMaleModel(transformedModels.getOrDefault(def.getPrimaryMaleModel(), def.getPrimaryMaleModel()));
            def.setPrimaryFemaleModel(transformedModels.getOrDefault(def.getPrimaryFemaleModel(), def.getPrimaryFemaleModel()));
            def.setSecondaryMaleModel(transformedModels.getOrDefault(def.getSecondaryMaleModel(), def.getSecondaryMaleModel()));
            def.setSecondaryFemaleModel(transformedModels.getOrDefault(def.getSecondaryFemaleModel(), def.getSecondaryFemaleModel()));
            def.setTertiaryMaleModel(transformedModels.getOrDefault(def.getTertiaryMaleModel(), def.getTertiaryMaleModel()));
            def.setTertiaryFemaleModel(transformedModels.getOrDefault(def.getTertiaryFemaleModel(), def.getTertiaryFemaleModel()));
            def.setPrimaryMaleHeadModelId(transformedModels.getOrDefault(def.getPrimaryMaleHeadModelId(), def.getPrimaryMaleHeadModelId()));
            def.setPrimaryFemaleHeadModelId(transformedModels.getOrDefault(def.getPrimaryFemaleHeadModelId(), def.getPrimaryFemaleHeadModelId()));
            def.setSecondaryMaleHeadModelId(transformedModels.getOrDefault(def.getSecondaryMaleHeadModelId(), def.getSecondaryMaleHeadModelId()));
            def.setSecondaryFemaleHeadModelId(transformedModels.getOrDefault(def.getSecondaryFemaleHeadModelId(), def.getSecondaryFemaleHeadModelId()));
            if (def.getId() == ChristmasConstants.GHOST_BOTTOMS_ID) {
                def.setPrimaryFemaleModel(def.getPrimaryMaleModel());
            }
            if (def.getParameters() != null) {
                if (def.getParameters().get(528) != null) {
                    final Object op = def.getParameters().get(528);
                    def.getParameters().clear();
                    def.getParameters().put(451, op);
                }
            }
            def.pack();
        }
        ItemDefinitions def = new ItemDefinitions(itemId++, new ByteBuffer(new byte[1]));
        def.setName("Snow imp");
        def.setInventoryModelId(impModel);
        def.setZoom(1383);
        def.setOffsetX(-4);
        def.setOffsetY(0);
        def.setModelPitch(163);
        def.setModelRoll(10);
        def.setModelYaw(0);
        def.pack();
        def = new ItemDefinitions(itemId++, new ByteBuffer(new byte[1]));
        def.setName("Christmas scythe");
        def.setGrandExchange(true);
        def.setInventoryModelId(52533);
        def.setPrimaryMaleModel(52534);
        def.setPrimaryFemaleModel(52534);
        def.setZoom(2930);
        def.setOffsetX(-5);
        def.setOffsetY(-9);
        def.setModelPitch(409);
        def.setModelRoll(1243);
        def.setModelYaw(0);
        def.getInventoryOptions()[1] = "Wear";
        def.pack();
    }

    public void packNPCs() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/christmas/npc definitions/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            //Snow imp pet duplicate.
            if (originalId == 9379) {
                for (int i = 15000; i <= 15001; i++) {
                    sortedMap.put(i, file);
                }
            } else if (originalId == 9378) {
                for (int i = 15002; i <= 15003; i++) {
                    sortedMap.put(i, file);
                }
            }
            sortedMap.put(originalId, file);
        }
        final ObjectArrayList<NPCDefinitions> objSet = new ObjectArrayList<NPCDefinitions>();
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final NPCDefinitions def = new NPCDefinitions(npcId, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            def.setVarbit(transformedVarbits.getOrDefault(def.getVarbit(), def.getVarbit()));
            objSet.add(def);
            final int originalId = entry.getIntKey();
            transformedNPCs.put(originalId, npcId);
            npcId++;
            final int[] models = def.getModels();
            if (models != null) {
                for (int i = 0; i < models.length; i++) {
                    models[i] = transformedModels.getOrDefault(models[i], models[i]);
                }
            }
            if (originalId >= 15000 && originalId <= 15003) {
                def.setFamiliar(true);
                def.setOptions(new String[] {"Talk-to", null, originalId <= 15001 ? "Pick-up" : null, null, null});
                def.setMinimapVisible(false);
                def.setVisible(false);
                def.setClickable(true);
                impModel = def.getModels()[0];
                def.setFamiliar(originalId == 15000 || originalId == 15002);
            }
            if (originalId == 9397 || originalId == 9416) {
                //TODO: Varbit verze
                def.setStandAnimation(3040);//Weird render animation bs.
            }
            def.setContrast(def.getContrast() / 5);
            //15030 -> 15022 stand
            //15023/15025 -> 15025
            final int[] chatModels = def.getChatModels();
            if (chatModels != null) {
                for (int i = 0; i < chatModels.length; i++) {
                    chatModels[i] = transformedModels.getOrDefault(chatModels[i], chatModels[i]);
                }
            }
        }
        for (final NPCDefinitions obj : objSet) {
            if (obj.getTransmogrifiedIds() != null) {
                for (int i = 0; i < obj.getTransmogrifiedIds().length; i++) {
                    if (obj.getTransmogrifiedIds()[i] == -1) {
                        continue;
                    }
                    obj.getTransmogrifiedIds()[i] = transformedNPCs.getOrDefault(obj.getTransmogrifiedIds()[i], obj.getTransmogrifiedIds()[i]);
                }
            }
        }
        for (final NPCDefinitions obj : objSet) {
            if (obj.getTransmogrifiedIds() != null) {
                for (int i = 0; i < obj.getTransmogrifiedIds().length; i++) {
                    if (obj.getTransmogrifiedIds()[i] == -1) {
                        continue;
                    }
                    final int index = i;
                    final NPCDefinitions otherDef = CollectionUtils.findMatching(objSet, o -> o.getId() == obj.getTransmogrifiedIds()[index]);
                    if (otherDef != null) {
                        obj.setStandAnimation(otherDef.getStandAnimation());
                        obj.setWalkAnimation(otherDef.getWalkAnimation());
                        obj.setRotate90Animation(otherDef.getRotate90Animation());
                        obj.setRotate180Animation(otherDef.getRotate180Animation());
                        obj.setRotate270Animation(otherDef.getRotate270Animation());
                        break;
                    }
                }//15042
            }
            //Partygoers
            if (obj.getId() == 15023 || obj.getId() == 15025 || obj.getId() == 15031) {
                obj.setStandAnimation(12672);
            }
            if (obj.getId() == 15022) {
                final NPCDefinitions otherDef = CollectionUtils.findMatching(objSet, o -> o.getId() == 15021);
                obj.setStandAnimation(otherDef.getStandAnimation());
                obj.setWalkAnimation(otherDef.getWalkAnimation());
                obj.setRotate90Animation(otherDef.getRotate90Animation());
                obj.setRotate180Animation(otherDef.getRotate180Animation());
                obj.setRotate270Animation(otherDef.getRotate270Animation());
            }
            obj.setWalkAnimation(transformedAnimations.getOrDefault(obj.getWalkAnimation(), obj.getWalkAnimation()));
            obj.setStandAnimation(transformedAnimations.getOrDefault(obj.getStandAnimation(), obj.getStandAnimation()));
            obj.setRotate90Animation(transformedAnimations.getOrDefault(obj.getRotate90Animation(), obj.getRotate90Animation()));
            obj.setRotate180Animation(transformedAnimations.getOrDefault(obj.getRotate180Animation(), obj.getRotate180Animation()));
            obj.setRotate270Animation(transformedAnimations.getOrDefault(obj.getRotate270Animation(), obj.getRotate270Animation()));
            if (obj.getId() == 15062 || obj.getId() == 15063) {
                if (obj.getWalkAnimation() == 15023) {
                    obj.setWalkAnimation(15009);
                }
            }
            //15022 to 15021
            obj.pack();
        }
        /*for (val def : objSet) {
            if (def.getTransmogrifiedIds() != null) {
                for (int i = 0; i < def.getTransmogrifiedIds().length; i++) {
                    if (def.getTransmogrifiedIds()[i] == -1) {
                        continue;
                    }
                    def.getTransmogrifiedIds()[i] = transformedNPCs.getOrDefault(def.getTransmogrifiedIds()[i], def.getTransmogrifiedIds()[i]);
                }
            }
            def.pack();
        }*/
    }
}
