package mgi.types.draw.sprite;

import java.awt.image.BufferedImage;

/**
 * @author Jire
 */
public final class SpriteEntry {

    private final BufferedImage image;
    private final int offsetX, offsetY;

    public SpriteEntry(BufferedImage image,
                       int offsetX, int offsetY) {
        this.image = image;

        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

}
