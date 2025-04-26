package mgi.types.worldmap;


import mgi.utilities.ByteBuffer;

/**
 * @author Tommeh | 2-12-2018 | 19:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface WorldMapType {

    void decode(final ByteBuffer buffer);

    void encode(final ByteBuffer buffer);

}
