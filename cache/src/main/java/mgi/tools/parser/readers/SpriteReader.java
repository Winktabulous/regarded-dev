package mgi.tools.parser.readers;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mgi.tools.parser.TypeProperty;
import mgi.tools.parser.TypeReader;
import mgi.types.Definitions;
import mgi.types.draw.sprite.SpriteEntry;
import mgi.types.draw.sprite.SpriteGroupDefinitions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Tommeh | 01/02/2020 | 14:42
 * @author Jire
 */
public class SpriteReader implements TypeReader {
    @Override
    public ArrayList<Definitions> read(final Map<String, Object> properties) throws NoSuchFieldException, IllegalAccessException {
        final ArrayList<Definitions> defs = new ArrayList<>();
        if (properties.containsKey("inherit")) {
            final Object inherit = properties.get("inherit");
            defs.add(SpriteGroupDefinitions.get((int) inherit));
        } else {
            defs.add(new SpriteGroupDefinitions());
        }
        for (final Definitions definition : defs) {
            final SpriteGroupDefinitions sprite = (SpriteGroupDefinitions) definition;
            TypeReader.setFields(sprite, properties);
            if (properties.containsKey(TypeProperty.IMAGES.getIdentifier())) {
                final Map<String, Object> map = (Map<String, Object>) properties.get(TypeProperty.IMAGES.getIdentifier());
                boolean clear = false;
                if (map.containsKey("clear")) {
                    clear = (Boolean) map.get("clear");
                }
                map.remove("clear");
                final Int2ObjectAVLTreeMap<SpriteEntry> images = new Int2ObjectAVLTreeMap<>();
                for (final Map.Entry<String, Object> entry : map.entrySet()) {
                    final int id = Integer.parseInt(entry.getKey());
                    String str = entry.getValue().toString();
                    final String path;
                    final int xOffset, yOffset;
                    if (str.contains(";")) {
                        final String[] mainSplit = str.split(";");
                        path = mainSplit[0];

                        final String[] subSplit = mainSplit[1].split(",");
                        xOffset = Integer.parseInt(subSplit[0]);
                        yOffset = Integer.parseInt(subSplit[1]);
                    } else {
                        path = str;
                        xOffset = yOffset = 0;
                    }
                    try {
                        final BufferedImage image = ImageIO.read(new File("./assets/sprites/" + path));
                        //Exception for emote tab emotes transparency; easier to do this programmatically. Higher revision emotes have a darker "locked" sprite.
                        if (path.startsWith("emotes/")) {
                            final int colourToSearch = 42 << 16 | 37 << 8 | 27 | -16777216;
                            final int colourToReplace = 64 << 16 | 57 << 8 | 40 | -16777216;
                            final int borderColour = 51 << 16 | 46 << 8 | 32 | -16777216;
                            final int width = image.getWidth();
                            final int height = image.getHeight();
                            for (int x = 0; x < width; x++) {
                                for (int y = 0; y < height; y++) {
                                    final int colour = image.getRGB(x, y);
                                    if (colour == colourToSearch) {
                                        //Lets identify if this pixel is a border pixel and create a thin 1px inner border out of the image to match the consistency.
                                        final boolean isBorderPixel = x == 0 || y == 0 || x == width - 1 || y == height - 1 || image.getRGB(x - 1, y) == 0 || image.getRGB(x + 1, y) == 0 || image.getRGB(x, y - 1) == 0 || image.getRGB(x, y + 1) == 0;
                                        image.setRGB(x, y, isBorderPixel ? borderColour : colourToReplace);
                                    }
                                }
                            }
                        }
                        images.put(id, new SpriteEntry(image, xOffset, yOffset));
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Did not load sprite with id "+id+" at "+path);
                    }
                }
                if (clear || sprite.getImages() == null) {
                    sprite.setImages(images);
                } else {
                    for (final Int2ObjectMap.Entry<SpriteEntry> entry : images.int2ObjectEntrySet()) {
                        sprite.getImages().put(entry.getIntKey(), entry.getValue());
                        sprite.setImage(entry.getIntKey(), entry.getValue());
                    }
                }
            }
        }
        return defs;
    }

    @Override
    public String getType() {
        return "sprite";
    }
}
