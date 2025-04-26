package mgi.custom;

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.SpotAnimationDefinition;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.ByteBuffer;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;

import static mgi.tools.parser.TypeParser.packModel;

/**
 * @author Kris | 20/04/2022
 */
public class Korasi {

    public static final int ITEM_ID = 32001;
    public static final int ITEM_ID_PLACEHOLDER = 33001;

    public void pack() {
        try {
            packBaseAnimations();
            packGraphics(new int[] { 32767, 32766 });
            packModels();
            packItem();
            packSpecialAttackInformation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void packSpecialAttackInformation() {
        final EnumDefinitions descriptionEnum = EnumDefinitions.get(1739);
        descriptionEnum.getValues().put(ITEM_ID, "Disrupt: Deal a magic-based attack, hitting up to three targets for 50-150% of your maximum melee hit.");
        descriptionEnum.pack();
        final EnumDefinitions costEnum = EnumDefinitions.get(906);
        costEnum.getValues().put(ITEM_ID, 500);
        costEnum.pack();
    }

    public void packModels() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/korasi/models/"), null, true);
        final IntAVLTreeSet list = new IntAVLTreeSet();
        while (it.hasNext()) {
            final File file = it.next();
            final String name = file.getName().replace(".dat", "");
            final int id = Integer.parseInt(name);
            list.add(id);
        }
        for (final Integer originalId : list) {
            final File file = new File("assets/korasi/models/" + originalId + ".dat");
            packModel(originalId, Files.readAllBytes(Paths.get(file.getPath())));
        }
    }

    private void packBaseAnimations() throws IOException {
        packAnimationBase(new int[] { 32767, 32766, 32765, 32764 });
        packAnimationBase(new int[] { 32763, 32762 });
    }

    private void packAnimationBase(final int[] anims) throws IOException {
        for (int anim : anims) {
            final File file = new File("assets/korasi/animations/definitions/" + anim + ".dat");
            final AnimationDefinitions definitions = new AnimationDefinitions(anim, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            final int[] ids = definitions.getFrameIds();
            definitions.pack();
            packFrames(ids);
        }
    }

    private void packGraphics(final int[] graphics) throws IOException {
        for (int graphic : graphics) {
            final File file = new File("assets/korasi/graphics/" + graphic + ".dat");
            final SpotAnimationDefinition definitions = new SpotAnimationDefinition(graphic, new ByteBuffer(FileUtils.readFileToByteArray(file)));
            definitions.pack();
        }
    }

    private void packItem() throws IOException {
        final File file = new File("assets/korasi/items/" + ITEM_ID + ".dat");
        final ItemDefinitions definitions = new ItemDefinitions(ITEM_ID, new ByteBuffer(FileUtils.readFileToByteArray(file)));
        definitions.setGrandExchange(true);
        definitions.setPrice(5000000);
        definitions.setPlaceholderId(ITEM_ID_PLACEHOLDER);
        definitions.pack();

        final ItemDefinitions definitionsPlaceHolder = new ItemDefinitions(ITEM_ID_PLACEHOLDER, new ByteBuffer(FileUtils.readFileToByteArray(file)));
        definitionsPlaceHolder.setGrandExchange(true);
        definitionsPlaceHolder.setPlaceholderId(ITEM_ID);
        definitionsPlaceHolder.setPlaceholderTemplate(14401);
        definitionsPlaceHolder.pack();
    }

    private void packFrames(final int[] ids) {
        Arrays.sort(ids);
        for (int id : ids) {
            try {
                File file = new File("assets/korasi/animations/frames/" + id + ".dat");
                FramePacker.add(id, IOUtils.toByteArray(Files.newInputStream(file.toPath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
