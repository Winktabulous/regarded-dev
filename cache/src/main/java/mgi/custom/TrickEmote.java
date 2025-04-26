package mgi.custom;

import com.zenyte.game.world.entity.masks.Animation;
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
 * @author Kris | 15/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TrickEmote {
    private final Int2IntMap transformedModels = new Int2IntOpenHashMap();
    private final Int2IntMap transformedAnimations = new Int2IntOpenHashMap();
    private final Int2IntMap transformedSounds = new Int2IntOpenHashMap();

    public TrickEmote() {
    }

    public final void packAll() throws IOException {
        packSoundEffects();
        packBaseAnimations();
        packModels();
        packGraphics();
    }

    private void packBaseAnimations() throws IOException {
        final int[] indexedAnims = new int[] {10530, 10531};
        for (int i = 0; i < indexedAnims.length; i++) {
            packAnimationBase(AnimationBase.valueOf("TRICK_BASE_" + (5191 + i)), new int[] {indexedAnims[i]});
        }
    }

    private final void packSoundEffects() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/halloween/sounds/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<>();
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

    private void packAnimationBase(@NotNull final AnimationBase base, final int[] anims) throws IOException {
        for (int anim : anims) {
            final File file = new File("assets/halloween/animations/definitions/" + anim + ".dat");
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
            for(AnimationDefinitions.Sound sound: definitions.getSoundEffects().values()) {
                if(sound != null) {
                    sound.id = transformedSounds.getOrDefault(sound.id, sound.id);
                }
            }
            //System.err.println("Transformed sounds on animation " + anim + ", " + animationIndex);
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
                File file = new File("assets/halloween/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void packGraphics() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/halloween/graphics/"), null, false);
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
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/halloween/models/"), null, true);
        final IntAVLTreeSet list = new IntAVLTreeSet();
        while (it.hasNext()) {
            final File file = it.next();
            final String name = file.getName().replace(".dat", "");
            final int id = Integer.parseInt(name);
            list.add(id);
        }
        for (final Integer originalId : list) {
            final File file = new File("assets/halloween/models/" + originalId + ".dat");
            transformedModels.put((int) originalId, ChristmasMapPacker.modelId);
            packModel(ChristmasMapPacker.modelId++, Files.readAllBytes(Paths.get(file.getPath())));
        }
    }

    public Int2IntMap getTransformedModels() {
        return this.transformedModels;
    }

    public Int2IntMap getTransformedAnimations() {
        return this.transformedAnimations;
    }

    public Int2IntMap getTransformedSounds() {
        return this.transformedSounds;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TrickEmote)) return false;
        final TrickEmote other = (TrickEmote) o;
        if (!other.canEqual(this)) return false;
        final Object this$transformedModels = this.getTransformedModels();
        final Object other$transformedModels = other.getTransformedModels();
        if (this$transformedModels == null ? other$transformedModels != null : !this$transformedModels.equals(other$transformedModels)) return false;
        final Object this$transformedAnimations = this.getTransformedAnimations();
        final Object other$transformedAnimations = other.getTransformedAnimations();
        if (this$transformedAnimations == null ? other$transformedAnimations != null : !this$transformedAnimations.equals(other$transformedAnimations)) return false;
        final Object this$transformedSounds = this.getTransformedSounds();
        final Object other$transformedSounds = other.getTransformedSounds();
        return this$transformedSounds == null ? other$transformedSounds == null :
                this$transformedSounds.equals(other$transformedSounds);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TrickEmote;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $transformedModels = this.getTransformedModels();
        result = result * PRIME + ($transformedModels == null ? 43 : $transformedModels.hashCode());
        final Object $transformedAnimations = this.getTransformedAnimations();
        result = result * PRIME + ($transformedAnimations == null ? 43 : $transformedAnimations.hashCode());
        final Object $transformedSounds = this.getTransformedSounds();
        result = result * PRIME + ($transformedSounds == null ? 43 : $transformedSounds.hashCode());
        return result;
    }

    public String toString() {
        return "TrickEmote(transformedModels=" + this.getTransformedModels() + ", transformedAnimations=" + this.getTransformedAnimations() + ", transformedSounds=" + this.getTransformedSounds() + ")";
    }
}
