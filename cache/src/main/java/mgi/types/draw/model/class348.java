package mgi.types.draw.model;

import mgi.tools.jagcached.cache.Archive;
import mgi.types.config.draw.SpriteBuffer;

public class class348 {
    public static boolean method6766(Archive var0, int var1) {
        byte[] var2 = var0.takeFileFlat(var1);
        if (var2 == null) {
            return false;
        } else {
            SpriteBuffer.SpriteBuffer_decode(var2);
            return true;
        }
    }
}
