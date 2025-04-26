package mgi.types.config;

import com.zenyte.CacheManager;
import mgi.Indice;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.types.draw.sprite.SpriteGroupDefinitions;
import mgi.utilities.ByteBuffer;
import mgi.utilities.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Kris | 21/10/2018 21:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HitbarDefinitions implements Definitions {
    private static final Logger log = LoggerFactory.getLogger(HitbarDefinitions.class);
    private static final int HEIGHT = 5;
    private static final int GREEN = 65280;
    private static final int RED = 16711680;
    private static HitbarDefinitions[] definitions;
    private final int id;
    private int anInt2098 = 255;
    private int anInt2099 = 255;
    private int cycles = -1;
    private final int anInt2101 = 1;
    private int anInt2100 = 70;
    private int secondarySprite = -1;
    private int primarySprite = -1;
    private int size = 30;
    private int extraWidth = 0;

    public HitbarDefinitions(final int id, final ByteBuffer buffer) {
        this.id = id;
        decode(buffer);
    }

    public static final HitbarDefinitions get(final int id) {
        if (id < 0 || id >= definitions.length) throw new RuntimeException("Hitbar " + id + " doesn't exist.");
        return definitions[id];
    }

    public static final void extractAll() {
        final File folder = new File("hitbars");
        if (!folder.exists()) folder.mkdir();
        for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.HITBAR); i++) {
            final HitbarDefinitions defs = HitbarDefinitions.get(i);
            defs.extract(new File("data/hitbars/hitbar " + i + "-size " + defs.getSize() + ".jpg"), 50);
        }
    }

    @Override
    public void load() {
        final Cache cache = CacheManager.getCache();
        final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
        final Group hitBars = configs.findGroupByID(GroupType.HITBAR);
        definitions = new HitbarDefinitions[hitBars.getHighestFileId()];
        for (int id = 0; id < hitBars.getHighestFileId(); id++) {
            final mgi.tools.jagcached.cache.File file = hitBars.findFileByID(id);
            if (file == null) {
                continue;
            }
            final ByteBuffer buffer = file.getData();
            if (buffer == null) {
                continue;
            }
            definitions[id] = new HitbarDefinitions(id, buffer);
        }
    }

    @Override
    public void decode(final ByteBuffer buffer) {
        while (true) {
            final int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                return;
            }
            decode(buffer, opcode);
        }
    }

    @Override
    public void decode(final ByteBuffer buffer, final int opcode) {
        if (opcode == 1) {
            buffer.readShort();
        } else if (opcode == 2) {
            anInt2098 = buffer.readUnsignedByte();
        } else if (opcode == 3) {
            anInt2099 = buffer.readUnsignedByte();
        } else if (opcode == 4) {
            cycles = 0;
        } else if (opcode == 5) {
            anInt2100 = buffer.readUnsignedShort();
        } else if (opcode == 6) {
            buffer.readByte();
        } else if (opcode == 7) {
            secondarySprite = buffer.readSmartInt();
        } else if (opcode == 8) {
            primarySprite = buffer.readSmartInt();
        } else if (opcode == 11) {
            cycles = buffer.readUnsignedShort();
        } else if (opcode == 14) {
            size = buffer.readUnsignedByte();
        } else if (opcode == 15) {
            extraWidth = buffer.readUnsignedByte();
        }
    }

    public void extract(final File file, final float fillPercentage) {
        if (fillPercentage < 0 || fillPercentage > 100) throw new RuntimeException("Fill percentage must be between 0-100.");
        final BufferedImage mainImage = primarySprite == -1 ? null : SpriteGroupDefinitions.get(primarySprite).getImage(0);
        final BufferedImage backgroundImage = secondarySprite == -1 ? null : SpriteGroupDefinitions.get(secondarySprite).getImage(0);
        final BufferedImage resizedImage = new BufferedImage(size, HEIGHT, 1);
        if (primarySprite == -1) {
            fill(resizedImage, 0, (int) (size / 100.0F * fillPercentage), GREEN);
        } else {
            fill(resizedImage, 0, (int) (size / 100.0F * fillPercentage), backgroundImage);
        }
        if (secondarySprite == -1) {
            fill(resizedImage, (int) (size / 100.0F * fillPercentage), size, RED);
        } else {
            fill(resizedImage, (int) (size / 100.0F * fillPercentage), size, mainImage);
        }
        try {
            ImageIO.write(resizedImage, "jpg", file);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void fill(final BufferedImage image, final int startWidth, final int endWidth, final int colour) {
        for (int x = startWidth; x < endWidth; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                image.setRGB(x, y, colour);
            }
        }
    }

    private void fill(final BufferedImage image, final int startWidth, final int endWidth, final BufferedImage fromImage) {
        for (int x = startWidth; x < endWidth; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                image.setRGB(x, y, fromImage.getRGB(x % fromImage.getWidth(), y % fromImage.getHeight()));
            }
        }
    }

    public int getPrimarySprite() { return primarySprite; }
    public int getSecondarySprite() { return secondarySprite; }

    public HitbarDefinitions() {
        this.id = 0;
    }

    public int getSize() {
        return size;
    }
}
