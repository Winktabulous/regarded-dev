package mgi.types.worldmap;

import mgi.types.config.UnderlayDefinitions;
import mgi.utilities.ByteBuffer;

import java.util.Arrays;

/**
 * @author Tommeh | 4-12-2018 | 19:26
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public abstract class WorldMapNode {

    public static boolean POST_REV_209_FORMAT = true;
    public int minPlane;
    public int maxPlane;
    public int centerRegionX;
    public int centerRegionY;
    public int regionX;
    public int regionY;
    public short[][][] underlays;
    public short[][][] overlays;
    public byte[][][] overlayShapes;
    public byte[][][] overlayRotations;
    public WorldMapGameObject[][][][] objects;
    public int[][] flags = new int[64][64];
    public int[][] overlayTileHeights = new int[64][64];
    public int offsetChunksX;
    public int offsetChunksY;

    public void smoothenedPixel(final int var1, final int var2, final int var3, final UnderlayDefinitions definitions) {
        if (definitions != null) {
            if (var3 + var1 >= 0 && var3 + var2 >= 0) {
                if (var1 - var3 <= 64 && var2 - var3 <= 64) {
                    int var5 = Math.max(0, var1 - var3);
                    int var6 = Math.min(64, var3 + var1);
                    int var7 = Math.max(0, var2 - var3);
                    int var8 = Math.min(64, var3 + var2);
                    for (int var9 = var5; var9 < var6; ++var9) {
                        for (int var10 = var7; var10 < var8; ++var10) {
                            this.hue[var9][var10] += definitions.getHue() * 256 / definitions.getHueMultiplier();
                            this.saturation[var9][var10] += definitions.getSaturation();
                            this.lightness[var9][var10] += definitions.getLightness();
                            ++this.count[var9][var10];
                        }
                    }
                }
            }
        }
    }

    public int[][] hue = new int[64][64];
    public int[][] saturation = new int[64][64];
    public int[][] lightness = new int[64][64];
    public int[][] count = new int[64][64];

    public int method5557(int var1, int var2) {
        if (var1 >= 0 && var2 >= 0 && var1 < 64 && var2 < 64) {
            if (this.lightness[var1][var2] == 0) {
                return 0;
            } else {
                int var3 = this.hue[var1][var2] / this.count[var1][var2];
                int var4 = this.saturation[var1][var2] / this.count[var1][var2];
                int var5 = this.lightness[var1][var2] / this.count[var1][var2];
                return method2019((double) var3 / 256.0, (double) var4 / 256.0, (double) var5 / 256.0);
            }
        } else {
            return 0;
        }
    }

    public static final int method2019(double var0, double var2, double var4) {
        double var6 = var4;
        double var8 = var4;
        double var10 = var4;
        if (var2 != 0.0) {
            double var12;
            if (var4 < 0.5) {
                var12 = var4 * (1.0 + var2);
            } else {
                var12 = var2 + var4 - var4 * var2;
            }
            double var14 = var4 * 2.0 - var12;
            double var16 = var0 + 0.3333333333333333;
            if (var16 > 1.0) {
                --var16;
            }
            double var20 = var0 - 0.3333333333333333;
            if (var20 < 0.0) {
                ++var20;
            }
            if (var16 * 6.0 < 1.0) {
                var6 = var14 + 6.0 * (var12 - var14) * var16;
            } else if (2.0 * var16 < 1.0) {
                var6 = var12;
            } else if (var16 * 3.0 < 2.0) {
                var6 = var14 + (0.6666666666666666 - var16) * (var12 - var14) * 6.0;
            } else {
                var6 = var14;
            }
            if (6.0 * var0 < 1.0) {
                var8 = var14 + var0 * (var12 - var14) * 6.0;
            } else if (2.0 * var0 < 1.0) {
                var8 = var12;
            } else if (var0 * 3.0 < 2.0) {
                var8 = var14 + 6.0 * (0.6666666666666666 - var0) * (var12 - var14);
            } else {
                var8 = var14;
            }
            if (6.0 * var20 < 1.0) {
                var10 = var14 + var20 * 6.0 * (var12 - var14);
            } else if (2.0 * var20 < 1.0) {
                var10 = var12;
            } else if (var20 * 3.0 < 2.0) {
                var10 = var14 + 6.0 * (var12 - var14) * (0.6666666666666666 - var20);
            } else {
                var10 = var14;
            }
        }
        int var22 = (int) (var6 * 256.0);
        int var13 = (int) (256.0 * var8);
        int var23 = (int) (var10 * 256.0);
        int var15 = var23 + (var13 << 8) + (var22 << 16);
        return var15;
    }

    /**
     * Loads all of the attributes of this tile in this region or chunk, from the buffer.
     *
     * @param x          the x position inside the region or chunk
     * @param y          the y position inside the region or chunk.
     * @param areaBuffer the buffer from which we load information.
     */
    void decodeTile(int x, int y, final ByteBuffer areaBuffer) {
        final int flag = areaBuffer.readUnsignedByte();
        flags[x][y] = flag;
        if (flag == 0) return;
        if ((flag & 1) != 0) {
            decodeSmall(x, y, areaBuffer);
        } else {
            decodeLarge(x, y, areaBuffer);
        }
    }

    /**
     * Decodes the tile's underlays and potentially overlays too, if flagged.
     *
     * @param x          the x position inside the region or chunk
     * @param y          the y position inside the region or chunk.
     * @param areaBuffer the buffer from which we load information.
     */
    private void decodeSmall(int x, int y, final ByteBuffer areaBuffer) {
        final int flag = flags[x][y];
        if ((flag & 2) != 0) {
            this.overlays[0][x][y] = (short) readShortOrByte(areaBuffer);
        }
        this.underlays[0][x][y] = (short) readShortOrByte(areaBuffer);
    }

    /**
     * Decodes the tile's underlays, and if flagged, underlays or/and objects too.
     *
     * @param x          the x position inside the region or chunk
     * @param y          the y position inside the region or chunk.
     * @param areaBuffer the buffer from which we load information.
     */
    private void decodeLarge(int x, int y, final ByteBuffer areaBuffer) {
        final int flag = flags[x][y];
        final int highestPlaneWithObjects = ((flag & 24) >> 3) + 1;
        final boolean containsOverlays = (flag & 2) != 0;
        final boolean containsObjects = (flag & 4) != 0;
        this.underlays[0][x][y] = (short) readShortOrByte(areaBuffer);
        if (containsOverlays) {
            overlayTileHeights[x][y] = areaBuffer.readUnsignedByte();
            for (int plane = 0; plane < overlayTileHeights[x][y]; plane++) {
                final int id = readShortOrByte(areaBuffer);
                if (id != 0) {
                    int attributes = areaBuffer.readUnsignedByte();
                    this.overlays[plane][x][y] = (short) id;
                    this.overlayShapes[plane][x][y] = (byte) (attributes >> 2);
                    this.overlayRotations[plane][x][y] = (byte) (attributes & 3);
                }
            }
        }
        if (containsObjects) {
            for (int z = 0; z < highestPlaneWithObjects; ++z) {
                final int length = areaBuffer.readUnsignedByte();
                if (length != 0) {
                    final WorldMapGameObject[] array = this.objects[z][x][y] = new WorldMapGameObject[length];
                    for (int index = 0; index < length; index++) {
                        final int id = areaBuffer.readBigSmart();
                        final int attributes = areaBuffer.readUnsignedByte();
                        array[index] = new WorldMapGameObject(id, attributes >> 2, attributes & 3);
                    }
                }
            }
        }
    }

    private static int readShortOrByte(ByteBuffer areaBuffer) {
        return POST_REV_209_FORMAT ? areaBuffer.readUnsignedShort() : areaBuffer.readUnsignedByte();
    }

    /**
     * Encodes this region or chunk into the buffer.
     *
     * @param x          the x position inside the region or chunk
     * @param y          the y position inside the region or chunk.
     * @param areaBuffer the buffer to which we write information.
     */
    public void encode(final int x, final int y, final ByteBuffer areaBuffer) {
        final int flag = flags[x][y];
        areaBuffer.writeByte(flag);
        if (flag == 0) return;
        if ((flag & 1) != 0) {
            encodeSmall(x, y, areaBuffer);
        } else {
            encodeLarge(x, y, areaBuffer);
        }
    }

    /**
     * Encodes this region's or chunk's underlays and if flagged, overlays into the buffer.
     *
     * @param x          the x position inside the region or chunk
     * @param y          the y position inside the region or chunk.
     * @param areaBuffer the buffer to which we write information.
     */
    private void encodeSmall(final int x, final int y, final ByteBuffer areaBuffer) {
        final int flag = flags[x][y];
        if ((flag & 2) != 0) {
            writeShortOrByte(areaBuffer, overlays[0][x][y]);
        }
        writeShortOrByte(areaBuffer, underlays[0][x][y] == 23 ? 0 : underlays[0][x][y]);
    }

    /**
     * Encodes this region's or chunk's underlays and if flagged, overlays or/and objects into the buffer.
     *
     * @param x          the x position inside the region or chunk
     * @param y          the y position inside the region or chunk.
     * @param areaBuffer the buffer to which we write information.
     */
    private void encodeLarge(final int x, final int y, final ByteBuffer areaBuffer) {
        final int flag = flags[x][y];
        final int objectTileHeight = ((flag & 24) >> 3) + 1;
        final boolean containsOverlays = (flag & 2) != 0;
        final boolean containsObjects = (flag & 4) != 0;
        writeShortOrByte(areaBuffer, underlays[0][x][y] == 23 ? 0 : underlays[0][x][y]);
        if (containsOverlays) {
            areaBuffer.writeByte(this.overlayTileHeights[x][y]);
            for (int plane = 0; plane < this.overlayTileHeights[x][y]; plane++) {
                final short id = overlays[plane][x][y];
                writeShortOrByte(areaBuffer, id);
                if (id != 0) {
                    areaBuffer.writeByte(((overlayShapes[plane][x][y] << 2) | (this.overlayRotations[plane][x][y] & 3)));
                }
            }
        }
        if (containsObjects) {
            for (int z = 0; z < objectTileHeight; ++z) {
                final int length = objects[z][x][y] == null ? 0 : objects[z][x][y].length;
                areaBuffer.writeByte(length);
                if (length != 0) {
                    for (int index = 0; index < length; index++) {
                        final WorldMapGameObject object = objects[z][x][y][index];
                        areaBuffer.writeBigSmart(object.getId());
                        areaBuffer.writeByte((object.getType() << 2 | (object.getRotation() & 3)));
                    }
                }
            }
        }
    }

    private void writeShortOrByte(ByteBuffer areaBuffer, short value) {
        if (POST_REV_209_FORMAT)
            areaBuffer.writeShort(value);
        else
            areaBuffer.writeByte(value);
    }

    public void setMinPlane(int minPlane) {
        this.minPlane = minPlane;
    }

    public void setMaxPlane(int maxPlane) {
        this.maxPlane = maxPlane;
    }

    public void setCenterRegionX(int centerRegionX) {
        this.centerRegionX = centerRegionX;
    }

    public void setCenterRegionY(int centerRegionY) {
        this.centerRegionY = centerRegionY;
    }

    public void setRegionX(int regionX) {
        this.regionX = regionX;
    }

    public void setRegionY(int regionY) {
        this.regionY = regionY;
    }

    public void setUnderlays(short[][][] underlays) {
        this.underlays = underlays;
    }

    public void setOverlays(short[][][] overlays) {
        this.overlays = overlays;
    }

    public void setOverlayShapes(byte[][][] overlayShapes) {
        this.overlayShapes = overlayShapes;
    }

    public void setOverlayRotations(byte[][][] overlayRotations) {
        this.overlayRotations = overlayRotations;
    }

    public void setObjects(WorldMapGameObject[][][][] objects) {
        this.objects = objects;
    }

    public void setFlags(int[][] flags) {
        this.flags = flags;
    }

    public void setOverlayTileHeights(int[][] overlayTileHeights) {
        this.overlayTileHeights = overlayTileHeights;
    }

    public void setOffsetChunksX(int offsetChunksX) {
        this.offsetChunksX = offsetChunksX;
    }

    public void setOffsetChunksY(int offsetChunksY) {
        this.offsetChunksY = offsetChunksY;
    }

    public void setHue(int[][] hue) {
        this.hue = hue;
    }

    public void setSaturation(int[][] saturation) {
        this.saturation = saturation;
    }

    public void setLightness(int[][] lightness) {
        this.lightness = lightness;
    }

    public void setCount(int[][] count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "WorldMapNode(minPlane=" + this.minPlane + ", maxPlane=" + this.maxPlane + ", centerRegionX=" + this.centerRegionX + ", centerRegionY=" + this.centerRegionY + ", regionX=" + this.regionX + ", regionY=" + this.regionY + ", underlays=" + Arrays.deepToString(this.underlays) + ", overlays=" + Arrays.deepToString(this.overlays) + ", overlayShapes=" + Arrays.deepToString(this.overlayShapes) + ", overlayRotations=" + Arrays.deepToString(this.overlayRotations) + ", objects=" + Arrays.deepToString(this.objects) + ", flags=" + Arrays.deepToString(this.flags) + ", overlayTileHeights=" + Arrays.deepToString(this.overlayTileHeights) + ", offsetChunksX=" + this.offsetChunksX + ", offsetChunksY=" + this.offsetChunksY + ", hue=" + Arrays.deepToString(this.hue) + ", saturation=" + Arrays.deepToString(this.saturation) + ", lightness=" + Arrays.deepToString(this.lightness) + ", count=" + Arrays.deepToString(this.count) + ")";
    }

}
