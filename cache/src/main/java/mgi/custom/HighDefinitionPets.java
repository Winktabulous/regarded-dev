package mgi.custom;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.custom.christmas.ChristmasMapPacker;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.ByteBuffer;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;

import static mgi.tools.parser.TypeParser.packModel;

/**
 * @author Kris | 03/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HighDefinitionPets {
    private final Int2IntMap transformedModels = new Int2IntOpenHashMap();
    private final Int2IntMap transformedNPCs = new Int2IntOpenHashMap();
    private final Int2IntMap transformedAnimations = new Int2IntOpenHashMap();

    public void packFull() throws IOException {
        packModels();
        packBaseAnimations();
        packNPCs();
        packItems();
    }

    private void packBaseAnimations() throws IOException {
        packAnimationBase(AnimationBase.PET_BASE_5086, new int[] {8672, 8671});
        packAnimationBase(AnimationBase.PET_BASE_5087, new int[] {8363, 8365});
        packAnimationBase(AnimationBase.PET_BASE_5088, new int[] {8326, 8324});
        packAnimationBase(AnimationBase.PET_BASE_5089, new int[] {7719, 7718});
        packAnimationBase(AnimationBase.PET_BASE_5090, new int[] {8315, 8314});
        packAnimationBase(AnimationBase.PET_BASE_5091, new int[] {8344, 8343});
        packAnimationBase(AnimationBase.PET_BASE_5092, new int[] {8360, 8357});
        packAnimationBase(AnimationBase.PET_BASE_5093, new int[] {27, 21});
        packAnimationBase(AnimationBase.PET_BASE_5094, new int[] {7575, 8338, 8337, 8339, 8331, 8335, 7576, 8333});
        packAnimationBase(AnimationBase.PET_BASE_5095, new int[] {8798, 8794});
        packAnimationBase(AnimationBase.PET_BASE_5096, new int[] {6220, 8518});
        packAnimationBase(AnimationBase.PET_BASE_5097, new int[] {8302, 8301});
        packAnimationBase(AnimationBase.PET_BASE_5098, new int[] {8245, 8246});
        packAnimationBase(AnimationBase.PET_BASE_5099, new int[] {8072, 8067});
        packAnimationBase(AnimationBase.PET_BASE_5100, new int[] {8098, 8100});
        packAnimationBase(AnimationBase.PET_BASE_5101, new int[] {8008, 8007});
        packAnimationBase(AnimationBase.PET_BASE_5102, new int[] {8035, 8038});
    }

    private void packAnimationBase(@NotNull final AnimationBase base, final int[] anims) throws IOException {
        for (int anim : anims) {
            final File file = new File("assets/pets/animation definitions/" + anim + ".dat");
            final AnimationDefinitions definitions = new AnimationDefinitions(anim, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            final int[] ids = definitions.getFrameIds();
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    ids[i] = (ids[i] & 65535) | (base.getBaseId() << 16);
                }
            }
            /*val sounds = definitions.getSoundEffects();
            if (sounds != null) {
                for (int i = 0; i < sounds.length; i++) {
                    val effectId = sounds[i];
                    if (effectId == 0) {
                        continue;
                    }
                    val soundId = effectId >> 8;
                    val radius = effectId & 0x1F;
                    val volume = effectId >> 5 & 0x7;

                    sounds[i] = (Math.min(0xF, 1 + radius) | volume << 4) | (transformedSounds.getOrDefault(soundId, soundId) << 8);
                }
                System.err.println("Transformed sounds on animation " + anim + ", " + animationIndex);
            }
*/
            definitions.setId(ChristmasMapPacker.animationIndex++);
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

    public void packItems() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/pets/item definitions/"), null, false);
        int realId = 30150;
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        ItemDefinitions def = null;
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            def = new ItemDefinitions(realId++, new ByteBuffer(FileUtils.readFileToByteArray(file)));
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
            if (def.getParameters() != null) {
                if (def.getParameters().get(528) != null) {
                    final Object op = def.getParameters().get(528);
                    def.getParameters().clear();
                    def.getParameters().put(451, op);
                }
            }
            def.pack();
        }

        class ModelInfo {
            final int id;
            final int zoom;
            final int rx;
            final int ry;
            final int rz;
            final int tx;
            final int ty;

            public ModelInfo(final int id, final int zoom, final int rx, final int ry, final int rz, final int tx, final int ty) {
                this.id = id;
                this.zoom = zoom;
                this.rx = rx;
                this.ry = ry;
                this.rz = rz;
                this.tx = tx;
                this.ty = ty;
            }
        }
        final Object2ObjectAVLTreeMap<String, ModelInfo> customItems = new Object2ObjectAVLTreeMap<String, ModelInfo>();
        customItems.put("Cockroach", new ModelInfo(32377, 1087, 0, 96, 0, 0, -9));
        customItems.put("Spirit mosquito", new ModelInfo(31277, 1922, 200, 113, 0, -20, 9));
        customItems.put("Praying mantis", new ModelInfo(31274, 1922, 113, 43, 0, -20, -26));
        customItems.put("Granite crab", new ModelInfo(31133, 2078, 217, 217, 0, -9, 0));
        customItems.put("Wolpertinger", new ModelInfo(31221, 2600, 113, 148, 0, -43, -43));
        customItems.put("Evil turnip", new ModelInfo(31197, 2409, 0, 0, 0, 0, -39));
        customItems.put("Spirit kalphite", new ModelInfo(24574, 2843, 61, 180, 0, -9, -120));
        //Just reuse the def to make it easier.
        for (final Object2ObjectMap.Entry<String, ModelInfo> custom : customItems.object2ObjectEntrySet()) {
            assert def != null;
            def.setName(custom.getKey());
            final ModelInfo model = custom.getValue();
            def.setInventoryModelId(transformedModels.getOrDefault(model.id, model.id));
            def.setZoom(model.zoom);
            def.setModelRoll(model.rx);
            def.setModelPitch(model.ry);
            def.setModelYaw(model.rz);
            def.setOffsetX(model.tx);
            def.setOffsetY(model.ty);
            def.setId(realId++);
            def.pack();
        }
    }

    private final void packFrames(final int[] ids) {
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/pets/animation frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void packModels() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/pets/models/"), null, false);
        final IntAVLTreeSet list = new IntAVLTreeSet();
        while (it.hasNext()) {
            final File file = it.next();
            final String name = file.getName().replace(".dat", "");
            final int id = Integer.parseInt(name);
            list.add(id);
        }
        for (final Integer originalId : list) {
            final File file = new File("assets/pets/models/" + originalId + ".dat");
            transformedModels.put((int) originalId, ChristmasMapPacker.modelId);
            packModel(ChristmasMapPacker.modelId++, Files.readAllBytes(Paths.get(file.getPath())));
        }
    }

    public void packNPCs() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/pets/npc definitions/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        Preconditions.checkArgument(ChristmasMapPacker.npcId < 15100);
        //Stupid restriction that I can't effectively bypass so let's just leave a small gap.
        ChristmasMapPacker.npcId = 15100;
        final ObjectArrayList<NPCDefinitions> objSet = new ObjectArrayList<NPCDefinitions>();
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            for (int i = 0; i < 1; i++) {
                final NPCDefinitions def = new NPCDefinitions(ChristmasMapPacker.npcId, new ByteBuffer(FileUtils.readFileToByteArray(file)));
                objSet.add(def);
                final int originalId = entry.getIntKey();
                transformedNPCs.put(originalId, ChristmasMapPacker.npcId);
                ChristmasMapPacker.npcId++;
                setNPC(def);
            }
        }
        for (final NPCDefinitions obj : objSet) {
            obj.setWalkAnimation(transformedAnimations.getOrDefault(obj.getWalkAnimation(), obj.getWalkAnimation()));
            obj.setStandAnimation(transformedAnimations.getOrDefault(obj.getStandAnimation(), obj.getStandAnimation()));
            obj.setRotate90Animation(transformedAnimations.getOrDefault(obj.getRotate90Animation(), obj.getRotate90Animation()));
            obj.setRotate180Animation(transformedAnimations.getOrDefault(obj.getRotate180Animation(), obj.getRotate180Animation()));
            obj.setRotate270Animation(transformedAnimations.getOrDefault(obj.getRotate270Animation(), obj.getRotate270Animation()));
            obj.pack();
        }
    }

    private final void setNPC(@NotNull final NPCDefinitions def) {
        if (def.getName().equalsIgnoreCase("Spirit kalphite")) {
            def.setSize(1);
            def.setResizeX(75);
            def.setResizeY(75);
        } else if (!def.getName().equalsIgnoreCase("Baby dragon")) {
            if (def.getSize() == 2) {
                def.setSize(1);
                def.setResizeY(100);
                def.setResizeX(100);
            }
        }
        def.setOptions(new String[] {"Talk-to", def.getName().equalsIgnoreCase("Chameleon") ? "Metamorphosis" : null, "Pick-up", null, null});
        def.setMinimapVisible(false);
        def.setVisible(false);
        def.setClickable(true);
        def.setFamiliar(true);
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
    }
}
