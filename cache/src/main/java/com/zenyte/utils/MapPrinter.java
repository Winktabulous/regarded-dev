package com.zenyte.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Kris | 27. sept 2018 : 00:29:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public interface MapPrinter {

    /**
     * Base X coordinate for the image.
     */
    int BASE_X = 1152;

    /**
     * Base Y coordinate for the image.
     */
    int BASE_Y = 10496;

    /**
     * Scale of the map. Note: Has to be identical to the underlying base image's scale.
     */
    int MAP_SCALE = 2;

    /**
     * Loads the base buffered image.
     *
     * @return base buffered image.
     * @throws IOException file not found.
     */
    default BufferedImage loadBaseImage(final int plane) throws IOException {
        return ImageIO.read(new File("data/map/full_image_" + plane + ".png"));
    }

    /**
     * Writes the image to input file.
     *
     * @param image image to write
     * @throws IOException
     */
    default void writeImage(final BufferedImage image, final int plane) throws IOException {
        ImageIO.write(image, "png", new File(path(plane)));
    }

    String path(final int plane);

    default void load(final int plane) throws IOException {
        final BufferedImage bufferedImage = loadBaseImage(plane);
        final Graphics2D graphics = bufferedImage.createGraphics();
        draw(graphics, plane);
        graphics.dispose();
        writeImage(bufferedImage, plane);
    }

    /**
     * Base method for drawing the map.
     *
     * @throws IOException from loading and writing to the image.
     */
    void draw(Graphics2D graphics, final int plane) throws IOException;

    /**
     * Gets the respective X coordinate to draw on the map.
     *
     * @param x the x coordinate in the game.
     * @return x coordinate for the image.
     */
    default int getX(final int x) {
        return (x - BASE_X) * MAP_SCALE;
    }

    /**
     * Gets the respective Y coordinate to draw on the map.
     *
     * @param y the y coordinate in the game.
     * @return y coordinate for the image.
     */
    default int getY(final int y) {
        return (BASE_Y - y) * MAP_SCALE;
    }

}
