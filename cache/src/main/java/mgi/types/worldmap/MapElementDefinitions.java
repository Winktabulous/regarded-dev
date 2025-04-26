package mgi.types.worldmap;

import com.zenyte.CacheManager;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;

import java.util.Arrays;

/**
 * @author Tommeh | 6-12-2018 | 23:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class MapElementDefinitions implements Definitions {

    private static MapElementDefinitions[] definitions;

    private int id;
    private int spriteId;
    private int field3306;//always -1
    private String text;
    private int colour;
    private int textSize;
    private String[] options;
    private String optionName;
    private int[] field3312;//always null
    private int field3313;//always 2147483647
    private int field3314;//always 2147483647
    private int field3315;//always -2147483648
    private int field3316;//always -2147483648
    private int horizontalAlignment;
    private int verticalAlignment;
    private int[] field3307;//always null
    private byte[] field3320;//always null
    //Used for getting string value from enum 1713
    private int tooltipId;

    public int getGroupId() {
        return id << 8 | 10;//10 = index of the string, Open is first so it's 10; effectively 10 + index.
    }

    @Override
    public void load() {
        final Cache cache = CacheManager.getCache();
        final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
        final Group labels = configs.findGroupByID(GroupType.MAP_LABELS);
        definitions = new MapElementDefinitions[labels.getHighestFileId()];
        for (int id = 0; id < labels.getHighestFileId(); id++) {
            final File file = labels.findFileByID(id);
            if (file == null) {
                continue;
            }
            final ByteBuffer buffer = file.getData();
            if (buffer == null) {
                continue;
            }
            definitions[id] = new MapElementDefinitions(id, buffer);
        }
    }

    public static MapElementDefinitions get(final int id) {
        return definitions[id];
    }

    public MapElementDefinitions(final int id, final ByteBuffer buffer) {
        this.id = id;
        this.tooltipId = -1;
        this.spriteId = -1;
        this.field3306 = -1;
        this.textSize = 0;
        this.horizontalAlignment = 1;
        this.verticalAlignment = 1;
        this.options = new String[5];
        this.field3313 = Integer.MAX_VALUE;
        this.field3314 = Integer.MAX_VALUE;
        this.field3315 = Integer.MIN_VALUE;
        this.field3316 = Integer.MIN_VALUE;
        decode(buffer);
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
        int var3;
        int var4;
        switch (opcode) {
            case 1:
                spriteId = buffer.readBigSmart();
                return;
            case 2:
                field3306 = buffer.readBigSmart();
                return;
            case 3:
                text = buffer.readString();
                return;
            case 4:
                colour = buffer.readMedium();
                return;
            case 5:
                buffer.readMedium();
                return;
            case 6:
                textSize = buffer.readUnsignedByte();
                return;
            case 7:
                var3 = buffer.readUnsignedByte();
                return;
            case 8:
                buffer.readByte();
                return;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                options[opcode - 10] = buffer.readString();
                return;
            case 15:
                var3 = buffer.readUnsignedByte();
                field3312 = new int[var3 * 2];
                for (int index = 0; index < var3 * 2; index++) {
                    field3312[index] = buffer.readShort();
                }
                buffer.readInt();
                var4 = buffer.readUnsignedByte();
                field3307 = new int[var4];
                for (int index = 0; index < field3307.length; index++) {
                    field3307[index] = buffer.readInt();
                }
                field3320 = new byte[var3];
                for (int index = 0; index < var3; index++) {
                    field3320[index] = buffer.readByte();
                }
                return;
            case 17:
                optionName = buffer.readString();
                return;
            case 18:
                buffer.readBigSmart();
                return;
            case 19:
                tooltipId = buffer.readUnsignedShort();
                return;
            case 21:
            case 22:
                buffer.readInt();
                return;
            case 23:
                buffer.readByte();
                buffer.readByte();
                buffer.readByte();
                return;
            case 24:
                buffer.readShort();
                buffer.readShort();
                return;
            case 25:
                buffer.readBigSmart();
                return;
            case 28:
                buffer.readByte();
                return;
            case 29:
                horizontalAlignment = buffer.readUnsignedByte();
                return;
            case 30:
                verticalAlignment = buffer.readUnsignedByte();
                return;
        }
    }

    @Override
    public ByteBuffer encode() {
        final ByteBuffer buffer = new ByteBuffer(4096);
        buffer.writeByte(1);
        buffer.writeBigSmart(spriteId);
        buffer.writeByte(2);
        buffer.writeBigSmart(field3306);
        if (text != null) {
            buffer.writeByte(3);
            buffer.writeString(text);
        }
        buffer.writeByte(4);
        buffer.writeMedium(colour);
        buffer.writeByte(5);
        buffer.writeMedium(-1);
        buffer.writeByte(6);
        buffer.writeByte(textSize);
        buffer.writeByte(7);
        buffer.writeByte(-1);
        buffer.writeByte(8);
        buffer.writeByte(-1);
        for (int opcode = 10; opcode <= 14; opcode++) {
            if (options[opcode - 10] != null) {
                buffer.writeByte(opcode);
                buffer.writeString(options[opcode - 10]);
            }
        }
        /*
        buffer.writeByte(15);
        if (field3312 != null) {
            buffer.writeByte((field3312.length / 2));
            for (int index = 0; index < field3312.length / 2; index++) {
                buffer.writeShort(field3312[index]);
            }
        }
        buffer.putInt(-1);
        if (field3307 != null) {
            buffer.writeByte(field3307.length);
            for (int index = 0; index < field3307.length; index++) {
                buffer.putInt(field3307[index]);
            }
        }

        if (field3320 != null) {
            for (int index = 0; index < field3312.length; index++) {
                buffer.put(field3320[index]);
            }
        }*/
        if (optionName != null) {
            buffer.writeByte(17);
            buffer.writeString(optionName);
        }
        buffer.writeByte(18);
        buffer.writeBigSmart(-1);
        buffer.writeByte(19);
        buffer.writeShort(tooltipId);
        buffer.writeByte(21);
        buffer.writeInt(-1);
        buffer.writeByte(23);
        buffer.writeByte(-1);
        buffer.writeByte(-1);
        buffer.writeByte(-1);
        buffer.writeByte(24);
        buffer.writeShort(-1);
        buffer.writeShort(-1);
        buffer.writeByte(25);
        buffer.writeBigSmart(-1);
        buffer.writeByte(28);
        buffer.writeByte(-1);
        buffer.writeByte(29);
        buffer.writeByte(horizontalAlignment);
        buffer.writeByte(30);
        buffer.writeByte(verticalAlignment);
        buffer.writeByte(0);
        return buffer;
    }

    public MapElementDefinitions() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpriteId() {
        return spriteId;
    }

    public void setSpriteId(int spriteId) {
        this.spriteId = spriteId;
    }

    public int getField3306() {
        return field3306;
    }

    public void setField3306(int field3306) {
        this.field3306 = field3306;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int[] getField3312() {
        return field3312;
    }

    public void setField3312(int[] field3312) {
        this.field3312 = field3312;
    }

    public int getField3313() {
        return field3313;
    }

    public void setField3313(int field3313) {
        this.field3313 = field3313;
    }

    public int getField3314() {
        return field3314;
    }

    public void setField3314(int field3314) {
        this.field3314 = field3314;
    }

    public int getField3315() {
        return field3315;
    }

    public void setField3315(int field3315) {
        this.field3315 = field3315;
    }

    public int getField3316() {
        return field3316;
    }

    public void setField3316(int field3316) {
        this.field3316 = field3316;
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public int[] getField3307() {
        return field3307;
    }

    public void setField3307(int[] field3307) {
        this.field3307 = field3307;
    }

    public byte[] getField3320() {
        return field3320;
    }

    public void setField3320(byte[] field3320) {
        this.field3320 = field3320;
    }

    public int getTooltipId() {
        return tooltipId;
    }

    public void setTooltipId(int tooltipId) {
        this.tooltipId = tooltipId;
    }

    @Override
    public String toString() {
        return "MapElementDefinitions(id=" + this.getId() + ", spriteId=" + this.getSpriteId() + ", field3306=" + this.getField3306() + ", text=" + this.getText() + ", colour=" + this.getColour() + ", textSize=" + this.getTextSize() + ", options=" + Arrays.deepToString(this.getOptions()) + ", optionName=" + this.getOptionName() + ", field3312=" + Arrays.toString(this.getField3312()) + ", field3313=" + this.getField3313() + ", field3314=" + this.getField3314() + ", field3315=" + this.getField3315() + ", field3316=" + this.getField3316() + ", horizontalAlignment=" + this.getHorizontalAlignment() + ", verticalAlignment=" + this.getVerticalAlignment() + ", field3307=" + Arrays.toString(this.getField3307()) + ", field3320=" + Arrays.toString(this.getField3320()) + ", tooltipId=" + this.getTooltipId() + ")";
    }
}
