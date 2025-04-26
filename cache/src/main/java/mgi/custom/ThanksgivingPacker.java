package mgi.custom;

import mgi.types.config.AnimationDefinitions;
import mgi.types.config.SpotAnimationDefinition;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Kris | 18/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ThanksgivingPacker {
    public void pack() {
        packStartAnimation();
        packEndAnimation();
        packTurkeyAnimation();
        packPoofAnimation();
        packGraphics();
    }

    private static void packStartAnimation() {
        final int[] lengths = new int[] {5, 5, 5, 5, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        final int[] ids = new int[] {176291848, 176291872, 176291853, 176291842, 176291852, 176291874, 176291851, 176291843, 176291874, 176291851, 176291843, 176291874, 176291851, 176291843, 176291874, 176291851, 176291843};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (5005 << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(828);
        def.setId(11008);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/animations/frames/thanksgiving2019/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void packEndAnimation() {
        final int[] lengths = new int[] {3, 3, 3, 4, 4, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        final int[] ids = new int[] {176291869, 176291864, 176291850, 176291844, 176291868, 176291860, 176291866, 176291876, 176291873, 176291870, 176291871, 176291849, 176291846, 176291878, 176291855, 176291841, 176291845, 176291847, 176291875, 176291856, 176291854, 176291867, 176291858, 176291861, 176291857, 176291859, 176291863};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (5005 << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(828);
        def.setId(11009);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/animations/frames/thanksgiving2019/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void packTurkeyAnimation() {
        final int[] lengths = new int[] {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 3};
        final int[] ids = new int[] {176357396, 176357664, 176357459, 176357413, 176357771, 176357618, 176357777, 176357653, 176357788, 176357645, 176357485, 176357624, 176357515, 176357449, 176357521, 176357576, 176357655, 176357606, 176357528, 176357518, 176357477, 176357744, 176357634, 176357403, 176357388, 176357742, 176357749, 176357564, 176357494, 176357691, 176357608, 176357502, 176357448, 176357758, 176357728, 176357609, 176357568, 176357783, 176357709, 176357646, 176357580, 176357456, 176357778, 176357470, 176357501, 176357474, 176357802, 176357686, 176357617, 176357522, 176357475, 176357532, 176357750, 176357782, 176357755, 176357493, 176357731, 176357743, 176357705, 176357512, 176357734, 176357503, 176357596, 176357736, 176357779, 176357703, 176357471, 176357690, 176357383, 176357507, 176357376, 176357402, 176357392, 176357421, 176357379, 176357495, 176357378, 176357684, 176357585, 176357569, 176357429, 176357774, 176357573, 176357649, 176357697, 176357757, 176357613, 176357381, 176357418, 176357523, 176357386, 176357543, 176357451, 176357737, 176357688, 176357752, 176357652, 176357445, 176357540, 176357762, 176357784, 176357659, 176357529, 176357529, 176357529, 176357529, 176357529, 176357529, 176357733, 176357535, 176357406, 176357751, 176357432, 176357500, 176357665, 176357729, 176357704, 176357581, 176357628, 176357658, 176357712, 176357641, 176357557, 176357554, 176357559, 176357615, 176357393, 176357583, 176357696, 176357460, 176357478, 176357721, 176357496, 176357396};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (5003 << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(828);
        def.setId(11010);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/animations/frames/thanksgiving2019/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void packPoofAnimation() {
        final int[] lengths = new int[] {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
        final int[] ids = new int[] {145817611, 145817609, 145817616, 145817612, 145817604, 145817605, 145817614, 145817601, 145817600, 145817602, 145817603, 145817613, 145817610, 145817608, 145817607, 145817615, 145817606, 145817617};
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (ids[i] & 65535) | (5004 << 16);
        }
        final AnimationDefinitions def = AnimationDefinitions.get(828);
        def.setId(11011);
        def.setFrameLengths(lengths);
        def.setFrameIds(ids);
        def.setExtraFrameIds(null);
        def.setMergedBoneGroups(null);
        def.setSoundEffects(null);
        def.pack();
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/animations/frames/thanksgiving2019/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final void packGraphics() {
        final SpotAnimationDefinition gfxDef = SpotAnimationDefinition.get(678);
        gfxDef.setAnimationId(11011);
        gfxDef.setModelId(52532);
        gfxDef.setRecolorFrom(null);
        gfxDef.setRecolorTo(null);
        gfxDef.setId(5005);
        gfxDef.pack();
    }
}
