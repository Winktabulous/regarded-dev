package mgi.custom;

import it.unimi.dsi.fastutil.ints.*;
import mgi.custom.christmas.ChristmasMapPacker;
import mgi.tools.parser.TypeParser;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.SpotAnimationDefinition;
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
 * @author Kris | 14/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CustomTeleport {
    private final Int2IntMap transformedModels = new Int2IntOpenHashMap();
    private final Int2IntMap transformedAnimations = new Int2IntOpenHashMap();

    public final void packAll() throws IOException {
        packSoundEffects();
        packBaseAnimations();
        packModels();
        packGraphics();
    }

    private void packBaseAnimations() throws IOException {
        final int[] indexedAnims = new int[] {8939, 8940, 8941, 8942};
        for (int i = 0; i < indexedAnims.length; i++) {
            packAnimationBase(AnimationBase.valueOf("TELEPORT_BASE_" + (5187 + i)), new int[] {indexedAnims[i]});
        }
    }

    private final void packSoundEffects() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/teleportation/sounds/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            TypeParser.packSound(ChristmasMapPacker.soundEffectId++, IOUtils.toByteArray(new FileInputStream(file)));
        }
    }

    private void packAnimationBase(@NotNull final AnimationBase base, final int[] anims) throws IOException {
        for (int anim : anims) {
            final File file = new File("assets/teleportation/animations/definitions/" + anim + ".dat");
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
            definitions.setId(ChristmasMapPacker.animationIndex++);
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
                File file = new File("assets/teleportation/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void packGraphics() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/teleportation/graphics/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }
        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final SpotAnimationDefinition def = new SpotAnimationDefinition(ChristmasMapPacker.graphicsId++, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            def.setModelId(transformedModels.getOrDefault(def.getModelId(), def.getModelId()));
            def.setAnimationId(transformedAnimations.getOrDefault(def.getAnimationId(), def.getAnimationId()));
            def.pack();
        }
    }

    public void packModels() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/teleportation/models/"), null, true);
        final IntAVLTreeSet list = new IntAVLTreeSet();
        while (it.hasNext()) {
            final File file = it.next();
            final String name = file.getName().replace(".dat", "");
            final int id = Integer.parseInt(name);
            list.add(id);
        }
        for (final Integer originalId : list) {
            final File file = new File("assets/teleportation/models/" + originalId + ".dat");
            transformedModels.put((int) originalId, ChristmasMapPacker.modelId);
            packModel(ChristmasMapPacker.modelId++, Files.readAllBytes(Paths.get(file.getPath())));
        }
    }
}
