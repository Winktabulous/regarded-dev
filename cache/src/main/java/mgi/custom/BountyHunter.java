package mgi.custom;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.ObjectDefinitions;
import mgi.types.draw.sprite.SpriteGroupDefinitions;
import mgi.utilities.ByteBuffer;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;

public class BountyHunter {

    public final void packAll() throws IOException {
        //packInterface();
        packObjects();
        packSprites();
    }

    private void packObjects() throws IOException {
        Iterator<File> it = FileUtils.iterateFiles(new File("assets/objects/bounty_hunter/"), null, false);
        final Int2ObjectAVLTreeMap<File> sortedMap = new Int2ObjectAVLTreeMap<File>();
        while (it.hasNext()) {
            final File file = it.next();
            final int originalId = Integer.parseInt(file.getName().replace(".dat", ""));
            sortedMap.put(originalId, file);
        }

        for (final Int2ObjectMap.Entry<File> entry : sortedMap.int2ObjectEntrySet()) {
            final File file = entry.getValue();
            final ObjectDefinitions def = new ObjectDefinitions(entry.getIntKey(), new ByteBuffer(FileUtils.readFileToByteArray(file)));
            def.pack();
        }
    }

    public LinkedHashMap<Integer, LinkedHashMap<Integer, BufferedImage>> group_child_sprite_mapping = new LinkedHashMap<>();
    private void packSprites() throws IOException {
        for (final File file : Objects
                .requireNonNull(Paths.get("assets/sprites/bounty_hunter/").toFile().listFiles())) {
            try {
                final String[] split = file.getName().replace(".png", "").split("_");
                final int groupId = Integer.parseInt(split[0]);
                if(!group_child_sprite_mapping.containsKey(groupId))
                    group_child_sprite_mapping.put(groupId, new LinkedHashMap<>());
                final int spriteId = Integer.parseInt(split[1]);
                final BufferedImage image = ImageIO.read(file);
                if(!group_child_sprite_mapping.get(groupId).containsKey(spriteId))
                    group_child_sprite_mapping.get(groupId).put(spriteId, image);
            } catch (Exception e) {
                System.err.println("Could not pack sprite '" + file + "'");
                e.printStackTrace();
            }
        }
        for(Integer group_id: group_child_sprite_mapping.keySet()) {
            if(group_child_sprite_mapping.get(group_id).size() == 1) {
                BufferedImage image = group_child_sprite_mapping.get(group_id).values().stream().findAny().get();
                final SpriteGroupDefinitions group = new SpriteGroupDefinitions(group_id, image.getWidth(),
                        image.getHeight());
                group.setHeight(image.getHeight());
                group.setWidth(image.getWidth());
                group.setImage(0, image);
                group.pack();
            } else {
                BufferedImage image = group_child_sprite_mapping.get(group_id).values().stream().findAny().get();
                final SpriteGroupDefinitions group = new SpriteGroupDefinitions(group_id, image.getWidth(),
                        image.getHeight());
                group.setHeight(image.getHeight());
                group.setWidth(image.getWidth());
                for(Integer child_id: group_child_sprite_mapping.get(group_id).keySet()) {
                    BufferedImage child = group_child_sprite_mapping.get(group_id).get(child_id);
                    group.setImage(child_id, child);
                }
                group.pack();
            }
        }
    }
}
