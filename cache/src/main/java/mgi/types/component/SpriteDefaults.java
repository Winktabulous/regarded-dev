package mgi.types.component;

import mgi.utilities.ByteBuffer;

public class SpriteDefaults {

    public int compass = -1;
    public int field4621 = -1;
    public int mapScenes = -1;
    public int headIconsPk = -1;
    public int field4616 = -1;
    public int field4617 = -1;
    public int field4620 = -1;
    public int field4619 = -1;
    public int field4622 = -1;
    public int field4613 = -1;
    public int field4624 = -1;

    public static SpriteDefaults decode(ByteBuffer var3) {
        SpriteDefaults defaults = new SpriteDefaults();
        defaults.decodeThis(var3);
        return defaults;
    }

    private void decodeThis(ByteBuffer var3) {
        while(true) {
            int var4 = var3.readUnsignedByte();
            if (var4 == 0) {
                return;
            }

            switch (var4) {
                case 1:
                    var3.readMedium();
                    break;
                case 2:
                    this.compass = var3.readNullableLargeSmart();
                    this.field4621 = var3.readNullableLargeSmart();
                    this.mapScenes = var3.readNullableLargeSmart();
                    this.headIconsPk = var3.readNullableLargeSmart();
                    this.field4616 = var3.readNullableLargeSmart();
                    this.field4617 = var3.readNullableLargeSmart();
                    this.field4620 = var3.readNullableLargeSmart();
                    this.field4619 = var3.readNullableLargeSmart();
                    this.field4622 = var3.readNullableLargeSmart();
                    this.field4613 = var3.readNullableLargeSmart();
                    this.field4624 = var3.readNullableLargeSmart();
            }
        }
    }
}
