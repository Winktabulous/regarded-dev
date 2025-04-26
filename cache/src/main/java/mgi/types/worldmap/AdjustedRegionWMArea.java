package mgi.types.worldmap;

import mgi.utilities.ByteBuffer;

/**
 * @author Tommeh | 2-12-2018 | 19:43
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class AdjustedRegionWMArea implements WorldMapType {
    private int plane;
    private int numberOfPlanes;
    private int minRegionX;
    private int minRegionY;
    private int maxRegionX;
    private int maxRegionY;
    private int mapMinRegionX;
    private int mapMinRegionY;
    private int mapMaxRegionX;
    private int mapMaxRegionY;

    @Override
    public void decode(final ByteBuffer buffer) {
        plane = buffer.readUnsignedByte();
        numberOfPlanes = buffer.readUnsignedByte();
        minRegionX = buffer.readUnsignedShort();
        minRegionY = buffer.readUnsignedShort();
        maxRegionX = buffer.readUnsignedShort();
        maxRegionY = buffer.readUnsignedShort();
        mapMinRegionX = buffer.readUnsignedShort();
        mapMinRegionY = buffer.readUnsignedShort();
        mapMaxRegionX = buffer.readUnsignedShort();
        mapMaxRegionY = buffer.readUnsignedShort();
    }

    @Override
    public void encode(final ByteBuffer buffer) {
        buffer.writeByte(0);
        buffer.writeByte(plane);
        buffer.writeByte(numberOfPlanes);
        buffer.writeShort(minRegionX);
        buffer.writeShort(minRegionY);
        buffer.writeShort(maxRegionX);
        buffer.writeShort(maxRegionY);
        buffer.writeShort(mapMinRegionX);
        buffer.writeShort(mapMinRegionY);
        buffer.writeShort(mapMaxRegionX);
        buffer.writeShort(mapMaxRegionY);
    }

    public int getPlane() {
        return plane;
    }

    public int getNumberOfPlanes() {
        return numberOfPlanes;
    }

    public int getMinRegionX() {
        return minRegionX;
    }

    public int getMinRegionY() {
        return minRegionY;
    }

    public int getMaxRegionX() {
        return maxRegionX;
    }

    public int getMaxRegionY() {
        return maxRegionY;
    }

    public int getMapMinRegionX() {
        return mapMinRegionX;
    }

    public int getMapMinRegionY() {
        return mapMinRegionY;
    }

    public int getMapMaxRegionX() {
        return mapMaxRegionX;
    }

    public int getMapMaxRegionY() {
        return mapMaxRegionY;
    }

    @Override
    public String toString() {
        return "AdjustedRegionWMArea(plane=" + this.getPlane() + ", numberOfPlanes=" + this.getNumberOfPlanes() + ", " +
                "minRegionX=" + this.getMinRegionX() + ", minRegionY=" + this.getMinRegionY() + ", maxRegionX=" + this.getMaxRegionX() + ", maxRegionY=" + this.getMaxRegionY() + ", mapMinRegionX=" + this.getMapMinRegionX() + ", mapMinRegionY=" + this.getMapMinRegionY() + ", mapMaxRegionX=" + this.getMapMaxRegionX() + ", mapMaxRegionY=" + this.getMapMaxRegionY() + ")";
    }

}
