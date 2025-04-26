package mgi.custom.easter;

import com.google.common.base.Preconditions;
import com.zenyte.CacheManager;
import com.zenyte.game.content.event.easter2020.EasterConstants;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.custom.AnimationBase;
import mgi.custom.FramePacker;
import mgi.custom.christmas.ChristmasMapPacker;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.Group;
import mgi.tools.parser.TypeParser;
import mgi.types.component.ComponentDefinitions;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.SpotAnimationDefinition;
import mgi.types.config.ObjectDefinitions;
import mgi.types.config.VarbitDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.ByteBuffer;
import mgi.utilities.CollectionUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import static mgi.tools.parser.TypeParser.*;

/**
 * @author Kris | 23/03/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EasterMapPacker {
    private static final boolean OUTPUT = false;
    private final Int2IntMap transformedModels = new Int2IntOpenHashMap();
    private final Int2IntMap transformedObjects = new Int2IntOpenHashMap();
    private final Int2IntMap transformedNPCs = new Int2IntOpenHashMap();
    private final Int2IntMap transformedAnimations = new Int2IntOpenHashMap();
    private final Int2IntMap transformedVarbits = new Int2IntOpenHashMap();
    private final Int2IntMap transformedSounds = new Int2IntOpenHashMap();
    private final Int2IntMap transformedItems = new Int2IntOpenHashMap();
    public static final IntOpenHashSet set = new IntOpenHashSet();
    private final Int2ObjectMap<int[]> modelMap = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<ObjectDefinitions> objectMap = new Int2ObjectOpenHashMap<>();

    public final void packAll() throws IOException {
//        packSoundEffects();
//        packVarbits();
//        preloadItems();
        packBaseAnimations();
        packModels();
//        packGraphics();
//        packObjects();
        packNPCs();
//        packItems();
//        packMusic();
        // packInstructionsInterface();
        //testInterfaces();
//        final Collection<WorldObject> objs = MapUtils.decode(new ByteBuffer(Files.readAllBytes(Paths.get("assets/easter/map/l_9810.dat"))));
//        objs.forEach(o -> {
//            set.add(o.getId());
//            o.setId(transformedObjects.getOrDefault(o.getId(), o.getId()));
//        });
//        final Collection<WorldObject> scourgeObjs = MapUtils.decode(new ByteBuffer(Files.readAllBytes(Paths.get("assets/easter/map/l_9811.dat"))));
//        scourgeObjs.forEach(o -> {
//            set.add(o.getId());
//            o.setId(transformedObjects.getOrDefault(o.getId(), o.getId()));
//        });
//        packMapPre209(8771, Files.readAllBytes(Paths.get("assets/easter/map/m_9810.dat")), Regions.inject(MapUtils.encode(objs).getBuffer(), o -> false));
//        packMapPre209(8772, Files.readAllBytes(Paths.get("assets/easter/map/m_9811.dat")), Regions.inject(MapUtils.encode(scourgeObjs).getBuffer(), o -> false));
    }

    public void packInstructionsInterface() throws IOException {
        packModel(53016, Files.readAllBytes(Paths.get("assets/easter/interfaces/models/712-bottompiece.dat")));
        packModel(53017, Files.readAllBytes(Paths.get("assets/easter/interfaces/models/712-toppiece.dat")));
        final Cache cache = CacheManager.getCache();
        final Group group = new Group(712);
        for (int i = 0; i < 4; i++) {
            final ComponentDefinitions def = new ComponentDefinitions(712 << 16 | i, new ByteBuffer(Files.readAllBytes(Paths.get("assets/easter/interfaces/712/" + i + ".dat"))));
            //We duplicated component 1 so 1 == 2 right now, and split the model 32827 into two custom separate models as it exceeded boundaries.
            if (def.modelId == 32827) {
                def.modelId = i == 1 ? 53016 : 53017;
            }
            group.addFile(new mgi.tools.jagcached.cache.File(def.encode()));
        }
        cache.getArchive(ArchiveType.INTERFACES).addGroup(group);
    }

    private static final void packMusic() throws IOException {
        CacheManager.getCache().getArchive(ArchiveType.MUSIC).addGroup(new Group(2502, new mgi.tools.jagcached.cache.File(new ByteBuffer(IOUtils.toByteArray(new FileInputStream(new File("assets/easter/music/Lazy Wabbit.dat")))))));
        //Cannot pack other one due to lack of instructions.
        /*Game.getCacheMgi().getArchive(ArchiveType.MUSIC).addGroup(new Group(2503, new mgi.tools.jagcached.cache.File(new ByteBuffer(IOUtils.toByteArray(new FileInputStream(new File("assets" +
                "/easter/music/Hare-brained Machines.dat")))))));*/
    }

    public void testInterfaces() throws IOException {
        packModel(53006, Files.readAllBytes(Paths.get("assets/easter/interfaces/models/32820.dat")));
        packModel(53007, Files.readAllBytes(Paths.get("assets/easter/interfaces/models/32821.dat")));
        packModel(53008, Files.readAllBytes(Paths.get("assets/easter/interfaces/models/32825.dat")));
        packModel(53009, Files.readAllBytes(Paths.get("assets/easter/interfaces/models/32826.dat")));
        packModel(53010, Files.readAllBytes(Paths.get("assets/easter/interfaces/models/32828.dat")));
        packModel(53011, Files.readAllBytes(Paths.get("assets/easter/interfaces/models/32830.dat")));
        packModel(53012, Files.readAllBytes(Paths.get("assets/easter/interfaces/models/32829.dat")));
        packModel(53013, Files.readAllBytes(Paths.get("assets/easter/interfaces/models/32824.dat")));
        packModel(53014, Files.readAllBytes(Paths.get("assets/easter/interfaces/models/32822.dat")));
        packModel(53015, Files.readAllBytes(Paths.get("assets/easter/interfaces/models/32823.dat")));
        EnumDefinitions enumDef;
        enumDef = new EnumDefinitions();
        enumDef.setId(208);
        enumDef.setKeyType("int");
        enumDef.setValueType("model");
        enumDef.setDefaultInt(-1);
        enumDef.setValues(new HashMap<Integer, Object>() {
            {
                put(0, 53009);
                put(1, 53011);
                put(2, 53010);
                put(3, 53008);
                put(4, 53007);
                put(5, 53006);
            }
        });
        TypeParser.getDefinitions().add(enumDef);
        for (int id = 10820; id <= 10832; id++) {
            packClientScript(id, Files.readAllBytes(Paths.get("assets/easter/clientscripts/" + id)));
        }
        final Cache cache = CacheManager.getCache();
        final Group group = new Group(711);
        final Int2IntOpenHashMap map = new Int2IntOpenHashMap();
        map.put(32821, 53007);
        map.put(32825, 53008);
        map.put(32826, 53009);
        map.put(32828, 53010);
        map.put(32830, 53011);
        map.put(32829, 53012);
        map.put(32824, 53013);
        map.put(32822, 53014);
        map.put(32823, 53015);
        map.put(537, 53016);
        for (int i = 0; i < 31; i++) {
            final ComponentDefinitions def = new ComponentDefinitions(711 << 16 | i, new ByteBuffer(Files.readAllBytes(Paths.get("assets/easter/interfaces/711/" + i + ".dat"))));
            def.animation = transformedAnimations.getOrDefault(def.animation, def.animation);
            if (def.onLoadListener != null && def.onLoadListener.length > 0 && Integer.parseInt(def.onLoadListener[0].toString()) == 398) {
            } else
            //def.onLoadListener[0] = 10820;
            if (def.onClickListener != null && def.onClickListener.length > 0) {
            }
            //def.onClickListener[0] = 10829;
            def.onLoadListener = null;
            def.onClickListener = null;
            def.onMouseRepeatListener = null;
            def.onMouseLeaveListener = null;
            def.onMouseOverListener = null;
            if (i == 4) {
                def.setModelZoom(625);
                def.setOffsetX2d(-52);
                def.setOffsetY2d(38);
            } else if (i >= 6 && i <= 9) {
                def.setModelZoom(800);
                def.setOffsetX2d(-1);
                def.setOffsetY2d(-3);
            } else if (i == 10) {
                def.setModelZoom(625);
                def.setOffsetX2d(-121);
                def.setOffsetY2d(-98);
            } else if (i == 11) {
                def.setModelZoom(625);
                def.setOffsetX2d(-119);
                def.setOffsetY2d(-117);
            } else if (i == 17) {
                def.setModelZoom(625);
                def.setOffsetX2d(-224);
                def.setOffsetY2d(4);
            }
            def.textureId = -1;
            def.modelId = map.getOrDefault(def.modelId, def.modelId);
            def.animation = -1;
            def.alternateAnimation = -1;
            group.addFile(new mgi.tools.jagcached.cache.File(def.encode()));
        }
        cache.getArchive(ArchiveType.INTERFACES).addGroup(group);
    }

    public void packObjects() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/easter/object definitions/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        final ObjectArrayList<ObjectDefinitions> objSet = new ObjectArrayList<ObjectDefinitions>();
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final ObjectDefinitions def = new ObjectDefinitions(ChristmasMapPacker.objectId, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            if (ArrayUtils.contains(objectsToTransform, def.getId())) {
                modelMap.put(def.getId(), def.getModels());
                def.setModels(null);
            }
            objectMap.put(def.getId(), def);
            def.setVarbit(transformedVarbits.getOrDefault(def.getVarbit(), def.getVarbit()));
            objSet.add(def);
            final int originalId = entry.getIntKey();
            def.setAnimationId(transformedAnimations.getOrDefault(def.getAnimationId(), def.getAnimationId()));
            def.setAmbientSoundId(transformedSounds.getOrDefault(def.getAmbientSoundId(), def.getAmbientSoundId()));
            transformedObjects.put(originalId, ChristmasMapPacker.objectId);
            if (def.getTransformedIds() != null) {
                for (final int id : def.getTransformedIds()) {
                    if (id > 0) {
                        set.add(id);
                    }
                }
            }
            ChristmasMapPacker.objectId++;
            final int[] models = def.getModels();
            if (models != null) {
                for (int i = 0; i < models.length; i++) {
                    models[i] = transformedModels.getOrDefault(models[i], models[i]);
                }
            }
            def.pack();
            if (OUTPUT) {
                if (def.getVarbit() > 0) {
                    System.err.println(def);
                }
            }
        }
        final Int2IntOpenHashMap map = new Int2IntOpenHashMap();
        for (final ObjectDefinitions obj : objSet) {
            if (obj.getTransformedIds() != null) {
                if (obj.getFinalTransformation() != -1) {
                    obj.setFinalTransformation(transformedObjects.getOrDefault(obj.getFinalTransformation(), obj.getFinalTransformation()));
                    for (int i = 0; i < obj.getTransformedIds().length; i++) {
                        if (obj.getTransformedIds()[i] == -1) {
                            obj.getTransmogrifiedIds()[i] = obj.getFinalTransformation();
                        }
                    }
                }
                for (int i = 0; i < obj.getTransformedIds().length; i++) {
                    if (obj.getTransformedIds()[i] == -1) {
                        continue;
                    }
                    obj.getTransformedIds()[i] = transformedObjects.getOrDefault(obj.getTransformedIds()[i], obj.getTransformedIds()[i]);
                }
            }
            if (obj.getAnimationId() > 0) {
                map.put(obj.getId(), obj.getAnimationId());
            }
        }
        for (final ObjectDefinitions obj : objSet) {
            if (ArrayUtils.contains(objectsToTransform, obj.getId())) {
                if (obj.getTransmogrifiedIds() != null) {
                    final IntAVLTreeSet transforms = new IntAVLTreeSet(obj.getTransformedIds());
                    transforms.remove(-1);
                    for (final Integer transform : transforms) {
                        final ObjectDefinitions def = CollectionUtils.findMatching(objSet, t -> t.getId() == transform);
                        if (def != null && def.getModels() != null) {
                            modelMap.put(def.getId(), def.getModels());
                            def.setModels(null);
                        }
                    }
                }
            }
        }
        //Assert animations
        /* for (val obj : objSet) {
            val transmogrified = obj.getTransmogrifiedIds();
            if (transmogrified == null) {
                continue;
            }
            val ids = new IntOpenHashSet(transmogrified);
            ids.remove(-1);
            if (ids.size() <= 1) {
                continue;
            }
            val stanceAnimations = new IntOpenHashSet();
            for (val id : ids) {
                //Lets find an obj with identical id
                val identical = Utils.findMatching(objSet, o -> o.getId() == id);
                Preconditions.checkArgument(identical != null);
                stanceAnimations.add(identical.getAnimationId());
            }
            if (stanceAnimations.size() > 1) {
                System.err.println("Issue on object: " + obj.getId() + ", " + obj.getName() + ", " + stanceAnimations);
            }
        }*/
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
            obj.pack();
        }
    }

    public void packModels() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/easter/models/"), null, true);
        final IntAVLTreeSet list = new IntAVLTreeSet();
        while (it.hasNext()) {
            final File file = it.next();
            final String name = file.getName().replace(".dat", "");
            final int id = Integer.parseInt(name);
            list.add(id);
        }
        for (final Integer originalId : list) {
            File file = new File("assets/easter/models/" + originalId + ".dat");
            if (!file.exists()) {
                file = new File("assets/easter/models/extra/" + originalId + ".dat");
            }
            transformedModels.put((int) originalId, ChristmasMapPacker.modelId);
            packModel(ChristmasMapPacker.modelId++, Files.readAllBytes(Paths.get(file.getPath())));
        }
    }

    private final void packSoundEffects() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/easter/sounds/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final int originalId = entry.getIntKey();
            transformedSounds.put(originalId, ChristmasMapPacker.soundEffectId);
            TypeParser.packSound(ChristmasMapPacker.soundEffectId++, IOUtils.toByteArray(new FileInputStream(file)));
        }
    }

    public void packVarbits() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/easter/varbit definitions/"), null, true);
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
        assert ChristmasMapPacker.varbitIndex > lowestId;
        final int varbitOffset = ChristmasMapPacker.varbitIndex - lowestId + 50;//add 50 so there's wiggle room. TODO: Find a better solution.
        final Int2IntOpenHashMap varpMap = new Int2IntOpenHashMap();
        int currentVarp = ChristmasMapPacker.varpIndex + 10;
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
    }

    public void packGraphics() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/easter/graphic definitions/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        if (OUTPUT) {
            System.err.println("First graphics id: " + ChristmasMapPacker.graphicsId);
        }
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final SpotAnimationDefinition def = new SpotAnimationDefinition(ChristmasMapPacker.graphicsId++, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            def.setModelId(transformedModels.getOrDefault(def.getModelId(), def.getModelId()));
            def.setAnimationId(transformedAnimations.getOrDefault(def.getAnimationId(), def.getAnimationId()));
            def.pack();
        }
    }

    private void packBaseAnimations() throws IOException {
        final int[] indexedAnims = new int[] {7, 8, 9, 424, 1242, 1243, 3101, 3210, 3211, 3212, 4931, 4932, 5325, 5326, 5598, 6518, 6613, 6614, 6615, 6616, 6617, 6618, 6824, 8145, 8146, 8582, 8794, 8798, 8882, 8883, 8901, 8902, 8903, 8904, 9179, 9180, 10556, 11501, 11502, 11503, 11504, 11505, 11507, 11509, 11510, 11511, 11512, 11513, 11514, 11515, 11516, 11518, 11519, 11520, 11521, 11522, 11523, 11525, 11527, 11528, 11529, 11530, 11531, 11532, 11533, 11534, 11535, 11536, 11537, 11538, 11539, 11540, 11542, 11543, 11544, 11545, 11546, 11547, 11548, 11549, 11550, 11551, 11552, 11710};
        for (int i = 0; i < indexedAnims.length; i++) {
            packAnimationBase(AnimationBase.valueOf("EASTER_OBJECT_BASE_" + (5103 + i)), new int[] {indexedAnims[i]});
        }
        /*val def = AnimationDefinitions.get(424);
        def.setMergedBoneGroups(new int[] {
                9, 11, 13, 15, 17, 19, 165, 167, 169, 171, 173, 175, 177, 179, 9999999
        });
        def.setId(ChristmasMapPacker.animationIndex++);
        System.err.println("Anim: :::" + (ChristmasMapPacker.animationIndex - 1));
        def.pack();*/
        //Anim: :::15242
    }

    private void packAnimationBase(@NotNull final AnimationBase base, final int[] anims) throws IOException {
        for (int anim : anims) {
            final File file = new File("assets/easter/animation definitions/" + anim + ".dat");
            final AnimationDefinitions definitions = new AnimationDefinitions(anim, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            final int[] ids = definitions.getFrameIds();
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    ids[i] = (ids[i] & 65535) | (base.getBaseId() << 16);
                }
            }
            final int[] altIds = definitions.getExtraFrameIds();
            if (altIds != null) {
                for (int i = 0; i < altIds.length; i++) {
                    altIds[i] = (altIds[i] & 65535) | (base.getBaseId() << 16);
                }
            }
            if (anim == 424 || anim == 11547) {
                definitions.setMergedBoneGroups(new int[] {9, 11, 13, 15, 17, 19, 165, 167, 169, 171, 173, 175, 177, 179, 9999999});
            }
            final int left = definitions.getLeftHandItem();
            final int right = definitions.getRightHandItem();
            definitions.setLeftHandItem(transformedItems.getOrDefault(definitions.getLeftHandItem(), definitions.getLeftHandItem()));
            definitions.setRightHandItem(transformedItems.getOrDefault(definitions.getRightHandItem(), definitions.getRightHandItem()));
            if (anim == 11542) {
                definitions.setLeftHandItem(0);
                definitions.setRightHandItem(0);
            }
            if (OUTPUT) {
                if (left > 0 && definitions.getLeftHandItem() == left) {
                    System.err.println("Untransformed item: " + left + ", " + anim);
                }
                if (right > 0 && definitions.getRightHandItem() == right) {
                    System.err.println("Untransformed item: " + right + ", " + anim);
                    System.err.println(transformedItems);
                }
            }
            if(definitions.getSoundEffects() != null) {
                for (AnimationDefinitions.Sound sound : definitions.getSoundEffects().values()) {
                    if(sound != null)
                        sound.id = transformedSounds.getOrDefault(sound.id, sound.id);
                }
            }
            //System.err.println("Transformed sounds on animation " + anim + ", " + animationIndex);
            //System.err.println("Animation " + anim + " redirected to " + animationIndex);
            definitions.setId(ChristmasMapPacker.animationIndex++);
            if (OUTPUT) {
                System.err.println("Easter anim trans: " + anim + ", " + definitions.getId());
            }
            transformedAnimations.put(anim, definitions.getId());
            definitions.pack();
            packFrames(ids);
            if (altIds != null) {
                packFrames(altIds);
            }
        }
    }

    private final void packFrames(final int[] ids) {
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/easter/animation frames/" + id + ".dat");
                FileInputStream inputStream = new FileInputStream(file);
                FramePacker.add(id, IOUtils.toByteArray(inputStream));
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
            //Lets make an exception for a known problematic npc of id 15197
        val matching = Utils.findMatching(objSet, o -> o.getId() == 15197);
        val def = Utils.findMatching(objSet, o -> o.getId() == 15213);
        var firstTransformedId = -1;
        val matchingTransforms = matching.getTransmogrifiedIds();
        for (val transform : matchingTransforms) {
            if (transform == -1) {
                continue;
            }
            firstTransformedId = transform;
            break;
        }
        for (int i = 0; i < matchingTransforms.length; i++) {
            val transId = matchingTransforms[i];
            if (transId != -1 && transId != firstTransformedId) {
                matchingTransforms[i] = -1;
            }
        }
        val newTransforms = def.getTransmogrifiedIds();
        for (int i = 0; i < newTransforms.length; i++) {
            val transId = newTransforms[i];
            if (transId != -1 && transId == firstTransformedId) {
                newTransforms[i] = -1;
            }
        }
        System.err.println(def.getId() + ", " + matching.getId());//15213, 15197
     */
    private void injectObjectAsNPC(final int objectId, final ObjectArrayList<NPCDefinitions> objSet) {
        NPCDefinitions def = new NPCDefinitions(ChristmasMapPacker.npcId++, new ByteBuffer(new byte[1]));
        objSet.add(def);
        ObjectDefinitions objectDef = objectMap.get(objectId);
        Preconditions.checkArgument(objectDef != null);
        //Lets define the basics of this NPC.
        setFields(def, objectDef);
        final int[] transformedIds = objectDef.getTransformedIds();
        if (transformedIds == null) {
            return;
        }
        final IntArrayList transforms = new IntArrayList(new IntAVLTreeSet(transformedIds));
        transforms.rem(-1);
        final NPCDefinitions[] bases = new NPCDefinitions[transforms.size()];
        bases[0] = def;
        for (int i = 0; i < transforms.size() - 1; i++) {
            bases[1 + i] = new NPCDefinitions(ChristmasMapPacker.npcId++, new ByteBuffer(new byte[1]));
            objSet.add(bases[1 + i]);
            //Lets define the basics of this NPC.
            setFields(bases[1 + i], objectDef);
        }
        final Int2IntOpenHashMap map = new Int2IntOpenHashMap();
        int index = 0;
        for (final Integer transform : transforms) {
            map.put(transform.intValue(), ChristmasMapPacker.npcId + (index++));
        }
        //Lets define the transformations for this based on the assumption the follow-up NPCs will be defined too.
        final int[] transformedTransformations = new int[transformedIds.length];
        for (int i = 0; i < transformedIds.length; i++) {
            transformedTransformations[i] = map.getOrDefault(transformedIds[i], transformedIds[i]);
        }
        //Go over each base and set the animation to different type, alter the transformations array respectively.
        for (int i = 0; i < bases.length; i++) {
            final NPCDefinitions base = bases[i];
            final int[] transformationsForBase = new int[transformedTransformations.length];
            System.arraycopy(transformedTransformations, 0, transformationsForBase, 0, transformationsForBase.length);
            final int transformationToKeep = map.get(transforms.getInt(i));
            for (int x = 0; x < transformationsForBase.length; x++) {
                final int value = transformationsForBase[x];
                if (value != -1 && value != transformationToKeep) {
                    transformationsForBase[x] = -1;
                }
            }
            base.setTransmogrifiedIds(transformationsForBase);
        }
        for (final Integer transformedId : transforms) {
            def = new NPCDefinitions(ChristmasMapPacker.npcId++, new ByteBuffer(new byte[1]));
            objSet.add(def);
            objectDef = objectMap.get(transformedId.intValue());
            Preconditions.checkArgument(objectDef != null);
            //Lets define the basics of this NPC.
            setFields(def, objectDef);
        }
    }

    private static final int[] objectsToTransform = new int[] {ChristmasMapPacker.baseObjectId + 403, ChristmasMapPacker.baseObjectId + 404, ChristmasMapPacker.baseObjectId + 407, ChristmasMapPacker.baseObjectId + 411, ChristmasMapPacker.baseObjectId + 412, ChristmasMapPacker.baseObjectId + 413, ChristmasMapPacker.baseObjectId + 414, ChristmasMapPacker.baseObjectId + 415, ChristmasMapPacker.baseObjectId + 334, ChristmasMapPacker.baseObjectId + 333};

    private final void setFields(final NPCDefinitions def, final ObjectDefinitions objectDef) {
        def.setVarbit(objectDef.getVarbit());
        final int[] models = modelMap.get(objectDef.getId());
        if (models != null) {
            for (int i = 0; i < models.length; i++) {
                models[i] = transformedModels.getOrDefault(models[i], models[i]);
            }
        }
        def.setModels(models);
        def.setName("<col=00ffff>" + objectDef.getName() + "</col>");
        def.setOptions(objectDef.getOptions());
        def.setCombatLevel(0);
        def.setSize(Math.max(objectDef.getSizeX(), objectDef.getSizeY()) + (def.getId() == 15260 || def.getId() == 15261 ? 2 : 0) + (objectDef.getId() == 46415 || objectDef.getId() == 46411 || objectDef.getId() == 46412 ? 1 : 0));
        def.setAmbience(objectDef.getAmbient());
        def.setContrast(objectDef.getContrast());
        def.setMinimapVisible(false);
        def.setVisible(true);
        def.setStandAnimation(objectDef.getAnimationId());
    }

    public void packNPCs() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/easter/npc definitions/"), null, true);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        final ObjectArrayList<NPCDefinitions> objSet = new ObjectArrayList<NPCDefinitions>();
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final NPCDefinitions def = new NPCDefinitions(ChristmasMapPacker.npcId, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            def.setVarbit(transformedVarbits.getOrDefault(def.getVarbit(), def.getVarbit()));
            objSet.add(def);
            final int originalId = entry.getIntKey();
            transformedNPCs.put(originalId, ChristmasMapPacker.npcId);
            ChristmasMapPacker.npcId++;
            final int[] models = def.getModels();
            if (models != null) {
                for (int i = 0; i < models.length; i++) {
                    models[i] = transformedModels.getOrDefault(models[i], models[i]);
                }
            }
            def.setContrast(def.getContrast() / 5);
            final int[] chatModels = def.getChatModels();
            if (chatModels != null) {
                for (int i = 0; i < chatModels.length; i++) {
                    chatModels[i] = transformedModels.getOrDefault(chatModels[i], chatModels[i]);
                }
            }
            if (OUTPUT) {
                System.err.println(def);
            }
        }
//        for (int i : objectsToTransform) {
//            injectObjectAsNPC(i, objSet);
//        }
        //7427, 7443
        //Lets make an exception for a known problematic npc of id 15197
        final NPCDefinitions matching = CollectionUtils.findMatching(objSet, o -> o.getId() == 15197);
        final NPCDefinitions def = CollectionUtils.findMatching(objSet, o -> o.getId() == 15213);
        int firstTransformedId = -1;
        final int[] matchingTransforms = matching.getTransmogrifiedIds();
        for (final int transform : matchingTransforms) {
            if (transform == -1) {
                continue;
            }
            firstTransformedId = transform;
            break;
        }
        for (int i = 0; i < matchingTransforms.length; i++) {
            final int transId = matchingTransforms[i];
            if (transId != -1 && transId != firstTransformedId) {
                matchingTransforms[i] = -1;
            }
        }
        final int[] newTransforms = def.getTransmogrifiedIds();
        for (int i = 0; i < newTransforms.length; i++) {
            final int transId = newTransforms[i];
            if (transId != -1 && transId == firstTransformedId) {
                newTransforms[i] = -1;
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
        //Assert animations
        /*for (val obj : objSet) {
            val transmogrified = obj.getTransmogrifiedIds();
            if (transmogrified == null) {
                continue;
            }
            val ids = new IntOpenHashSet(transmogrified);
            ids.remove(-1);
            if (ids.size() <= 1) {
                continue;
            }
            val stanceAnimations = new IntOpenHashSet();
            val walkAnimations = new IntOpenHashSet();
            for (val id : ids) {
                //Lets find an obj with identical id
                val identical = Utils.findMatching(objSet, o -> o.getId() == id);
                Preconditions.checkArgument(identical != null);
                stanceAnimations.add(identical.getStandAnimation());
                walkAnimations.add(identical.getWalkAnimation());
            }
            if (stanceAnimations.size() > 1 || walkAnimations.size() > 1) {
                System.err.println("Issue on npc: " + obj.getId() + ", " + obj.getName());
            }
        }*/
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
                }
            }
            obj.setWalkAnimation(transformedAnimations.getOrDefault(obj.getWalkAnimation(), obj.getWalkAnimation()));
            obj.setStandAnimation(transformedAnimations.getOrDefault(obj.getStandAnimation(), obj.getStandAnimation()));
            obj.setRotate90Animation(transformedAnimations.getOrDefault(obj.getRotate90Animation(), obj.getRotate90Animation()));
            obj.setRotate180Animation(transformedAnimations.getOrDefault(obj.getRotate180Animation(), obj.getRotate180Animation()));
            obj.setRotate270Animation(transformedAnimations.getOrDefault(obj.getRotate270Animation(), obj.getRotate270Animation()));
            obj.pack();
        }
    }

    public void preloadItems() {
        int id = ChristmasMapPacker.itemId;
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/easter/item definitions/"), null, false);
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            transformedItems.put(originalId, id++);
        }
    }

    public void packItems() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/easter/item definitions/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final ItemDefinitions def = new ItemDefinitions(ChristmasMapPacker.itemId++, new ByteBuffer(FileUtils.readFileToByteArray(file)));
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
            if (def.getId() == EasterConstants.EasterItem.IMPLING_NET.getItemId()) {
                def.setPrimaryMaleModel(19840); // osrs net
                def.setPrimaryFemaleModel(19840);
            } else if (def.getId() == EasterConstants.EasterItem.CHOCATRICE_CAPE.getItemId()) {
                def.setParameters(new Int2ObjectOpenHashMap<Object>() {
                    {
                        put(451, "Operate");
                    }
                });
            } else if (def.getId() == EasterConstants.EasterItem.EASTER_CARROT.getItemId()) {
                def.setMaleOffset(27);
                def.setFemaleOffset(27);
                def.setOption(4, "Destroy");
            }
            if (def.getParameters() != null) {
                if (def.getParameters().get(528) != null) {
                    final Object op = def.getParameters().get(528);
                    def.getParameters().clear();
                    def.getParameters().put(451, op);
                }
            }
            def.pack();
            if (OUTPUT) {
                System.err.println(def);
            }
        }
    }
}
