package mgi.types.worldmap;

import mgi.utilities.ByteBuffer;

/**
 * @author Tommeh | 2-12-2018 | 19:43
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class AdjustedChunkWMArea implements WorldMapType {
    private int plane;
    private int numberOfPlanes;
    private int regionX;
    private int regionY;
    private int mapRegionX;
    private int mapRegionY;
    private int fromChunkXInRegion;
    private int fromChunkYInRegion;
    private int toChunkXInRegion;
    private int toChunkYInRegion;
    private int fromChunkXInMapRegion;
    private int fromChunkYInMapRegion;
    private int toChunkXInMapRegion;
    private int toChunkYInMapRegion;

    @Override
    public void decode(final ByteBuffer buffer) {
        plane = buffer.readUnsignedByte();
        numberOfPlanes = buffer.readUnsignedByte();
        regionX = buffer.readUnsignedShort();
        fromChunkXInRegion = buffer.readUnsignedByte();
        toChunkXInRegion = buffer.readUnsignedByte();
        regionY = buffer.readUnsignedShort();
        fromChunkYInRegion = buffer.readUnsignedByte();
        toChunkYInRegion = buffer.readUnsignedByte();
        mapRegionX = buffer.readUnsignedShort();
        fromChunkXInMapRegion = buffer.readUnsignedByte();
        toChunkXInMapRegion = buffer.readUnsignedByte();
        mapRegionY = buffer.readUnsignedShort();
        fromChunkYInMapRegion = buffer.readUnsignedByte();
        toChunkYInMapRegion = buffer.readUnsignedByte();
    }

    @Override
    public void encode(final ByteBuffer buffer) {
        buffer.writeByte(2);
        buffer.writeByte(plane);
        buffer.writeByte(numberOfPlanes);
        buffer.writeShort(regionX);
        buffer.writeByte(fromChunkXInRegion);
        buffer.writeByte(toChunkXInRegion);
        buffer.writeShort(regionY);
        buffer.writeByte(fromChunkYInRegion);
        buffer.writeByte(toChunkYInRegion);
        buffer.writeShort(mapRegionX);
        buffer.writeByte(fromChunkXInMapRegion);
        buffer.writeByte(toChunkXInMapRegion);
        buffer.writeShort(mapRegionY);
        buffer.writeByte(fromChunkYInMapRegion);
        buffer.writeByte(toChunkYInMapRegion);
    }

    public int getPlane() {
        return plane;
    }

    public int getNumberOfPlanes() {
        return numberOfPlanes;
    }

    public int getRegionX() {
        return regionX;
    }

    public int getRegionY() {
        return regionY;
    }

    public int getMapRegionX() {
        return mapRegionX;
    }

    public int getMapRegionY() {
        return mapRegionY;
    }

    public int getFromChunkXInRegion() {
        return fromChunkXInRegion;
    }

    public int getFromChunkYInRegion() {
        return fromChunkYInRegion;
    }

    public int getToChunkXInRegion() {
        return toChunkXInRegion;
    }

    public int getToChunkYInRegion() {
        return toChunkYInRegion;
    }

    public int getFromChunkXInMapRegion() {
        return fromChunkXInMapRegion;
    }

    public int getFromChunkYInMapRegion() {
        return fromChunkYInMapRegion;
    }

    public int getToChunkXInMapRegion() {
        return toChunkXInMapRegion;
    }

    public int getToChunkYInMapRegion() {
        return toChunkYInMapRegion;
    }

    @Override
    public String toString() {
        return "AdjustedChunkWMArea(plane=" + this.getPlane() + ", numberOfPlanes=" + this.getNumberOfPlanes() + ", " +
                "regionX=" + this.getRegionX() + ", regionY=" + this.getRegionY() + ", mapRegionX=" + this.getMapRegionX() + ", mapRegionY=" + this.getMapRegionY() + ", fromChunkXInRegion=" + this.getFromChunkXInRegion() + ", fromChunkYInRegion=" + this.getFromChunkYInRegion() + ", toChunkXInRegion=" + this.getToChunkXInRegion() + ", toChunkYInRegion=" + this.getToChunkYInRegion() + ", fromChunkXInMapRegion=" + this.getFromChunkXInMapRegion() + ", fromChunkYInMapRegion=" + this.getFromChunkYInMapRegion() + ", toChunkXInMapRegion=" + this.getToChunkXInMapRegion() + ", toChunkYInMapRegion=" + this.getToChunkYInMapRegion() + ")";
    }

}
