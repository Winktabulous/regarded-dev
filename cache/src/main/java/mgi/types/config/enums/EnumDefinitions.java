package mgi.types.config.enums;

import com.google.common.collect.ImmutableMap;
import com.zenyte.CacheManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.types.config.ObjectDefinitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.ByteBuffer;
import net.runelite.cache.util.ScriptVarType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author Kris | 3. march 2018 : 16:32.44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class EnumDefinitions implements Definitions, Cloneable {
    private static final Logger log = LoggerFactory.getLogger(EnumDefinitions.class);
    public static final ImmutableMap<Character, String> TYPE_MAP = ImmutableMap.<Character, String>builder().put('A', "seq").put('i', "int").put('1', "boolean").put('s', "string").put('v', "inv").put('z', "char").put('O', "namedobj").put('M', "midi").put('K', "idkit").put('o', "obj").put('n', "npc").put('c', "coordgrid").put('S', "stat").put('m', "model").put('d', "graphic").put('J', "struct").put('f', "fontmetrics").put('I', "component").put('k', "chatchar").put('g', "enum").put('l', "location").build();
    public static final ImmutableMap<String, Character> REVERSE_TYPE_MAP = ImmutableMap.<String, Character>builder().putAll(TYPE_MAP.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey))).build();
    private static Int2ObjectOpenHashMap<Enum> map;
    public static Int2ObjectOpenHashMap<EnumDefinitions> definitions = new Int2ObjectOpenHashMap<>();

    public static IntEnum getIntEnum(final int id) {
        final mgi.types.config.enums.Enum e = map.get(id);
        if (!(e instanceof IntEnum)) {
            throw new RuntimeException("Enum isn't an instanceof IntEnum!");
        }
        return (IntEnum) e;
    }

    public static StringEnum getStringEnum(final int id) {
        final mgi.types.config.enums.Enum e = map.get(id);
        if (!(e instanceof StringEnum)) {
            throw new RuntimeException("Enum isn't an instanceof StringEnum!");
        }
        return (StringEnum) e;
    }

    public static StringEnumLC getStringEnumLowercase(final int id) {
        final mgi.types.config.enums.Enum e = map.get(id);
        if (!(e instanceof StringEnum)) {
            throw new RuntimeException("Enum isn't an instanceof StringEnum!");
        }
        return new StringEnumLC((StringEnum) e);
    }

    public static IntEnum getIntEnumOrNull(final int id) {
        final mgi.types.config.enums.Enum e = map.get(id);
        if (!(e instanceof IntEnum)) {
            return null;
        }
        return (IntEnum) e;
    }

    public static StringEnum getStringEnumOrNull(final int id) {
        final mgi.types.config.enums.Enum e = map.get(id);
        if (!(e instanceof StringEnum)) {
            return null;
        }
        return (StringEnum) e;
    }

    public static Enum<?> getEnum(final int id) {
        return map.get(id);
    }

    @Override
    public void load() {
        final Cache cache = CacheManager.getCache();
        load(cache);
    }

    public void load(Cache cache) {
        final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
        final Group enums = configs.findGroupByID(GroupType.ENUM);
        definitions = new Int2ObjectOpenHashMap<>();
        map = new Int2ObjectOpenHashMap<>(enums.getHighestFileId());
        for (int id = 0; id < enums.getHighestFileId(); id++) {
            final File file = enums.findFileByID(id);
            if (file == null) {
                continue;
            }
            final ByteBuffer buffer = file.getData();
            if (buffer == null) {
                continue;
            }
            definitions.put(id, new EnumDefinitions(id, buffer));
        }
    }


    private int id;
    private int size;
    private char keyType;
    private char valueType;
    private ScriptVarType keyTypeExt;
    private ScriptVarType valueTypeExt;

    private String defaultString;
    private int defaultInt;
    private Map<Integer, Object> values;
    private transient int largestIntValue = -1;
    private transient int largestIntKey = -1;

    private EnumDefinitions(final int id, final ByteBuffer buffer) {
        this.id = id;
        defaultString = "null";
        decode(buffer);
        if (values == null) {
            return;
        }
        Enum anEnum;
        if (valueType == 's') {
            anEnum = new StringEnum(id, keyType, valueType, defaultString, values);
        } else {
            anEnum = new IntEnum(id, keyType, valueType, defaultInt, values);
        }
        map.put(id, anEnum);
    }

    public EnumDefinitions clone() throws CloneNotSupportedException {
        return (EnumDefinitions) super.clone();
    }

    public void setKeyType(final String keyType) {
        final Character type = REVERSE_TYPE_MAP.get(keyType);
        if (type == null) {
            throw new RuntimeException("Unable to find a matching type for " + keyType + ".");
        }
        this.keyType = type;
    }

    public void setValueType(final String valueType) {
        final Character type = REVERSE_TYPE_MAP.get(valueType);
        if (type == null) {
            throw new RuntimeException("Unable to find a matching type for " + valueType + ".");
        }
        this.valueType = type;
    }

    public int getLargestIntValue() {
        if (largestIntValue != -1) {
            return largestIntValue;
        }
        int largestValue = 0;
        final Map<Integer, Object> values = this.values;
        if (values != null) {
            final Iterator<Map.Entry<Integer, Object>> iterator = values.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<Integer, Object> next = iterator.next();
                if (next.getValue() instanceof Integer) {
                    final int intValue = (int) next.getValue();
                    if (intValue > largestValue) {
                        largestValue = intValue;
                    }
                }
            }
        }
        largestIntValue = largestValue;
        return largestIntValue;
    }

    public int getLargestIntKey() {
        if (largestIntKey != -1) {
            return largestIntKey;
        }
        int largestKey = 0;
        final Map<Integer, Object> values = this.values;
        if (values != null) {
            final Iterator<Map.Entry<Integer, Object>> iterator = values.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<Integer, Object> next = iterator.next();
                if (next.getKey() instanceof Integer) {
                    final Integer intKey = next.getKey();
                    if (intKey > largestKey) {
                        largestKey = intKey;
                    }
                }
            }
        }
        largestIntKey = largestKey;
        return largestIntKey;
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
        switch (opcode) {
        case 1: 
            keyType = (char) buffer.readUnsignedByte();
            return;
        case 2: 
            valueType = (char) buffer.readUnsignedByte();
            return;
        case 3: 
            defaultString = buffer.readString();
            return;
        case 4: 
            defaultInt = buffer.readInt();
            return;
        case 5: 
            size = buffer.readUnsignedShort();
            values = new TreeMap<>();
            for (int index = 0; index < size; ++index) {
                final int key = buffer.readInt();
                final String value = buffer.readString();
                values.put(key, value);
            }
            return;
        case 6: 
            size = buffer.readUnsignedShort();
            values = new TreeMap<>();
            for (int index = 0; index < size; ++index) {
                final int key = buffer.readInt();
                final int value = buffer.readInt();
                values.put(key, value);
            }
            return;
        }
    }

    public static EnumDefinitions get(final int id) {
        return definitions.get(id);
    }

    public static Optional<EnumDefinitions> getOptional(final int id) {
        if (get(id) == null) {
            return Optional.empty();
        }
        return Optional.of(get(id));
    }

    public int getKeyForValue(final Object value) {
        for (final Integer key : values.keySet()) {
            if (values.get(key).equals(value)) {
                return key;
            }
        }
        return -1;
    }

    public int getKeyForStringValue(final String value) {
        for (final Integer key : values.keySet()) {
            if (values.get(key).toString().equalsIgnoreCase(value)) {
                return key;
            }
        }
        return -1;
    }

    public int getSize() {
        if (values == null) {
            return 0;
        }
        return values.size();
    }

    public int getIntValue(final int key) {
        if (values == null) {
            return defaultInt;
        }
        final Object value = values.get(key);
        if (value == null || !(value instanceof Integer)) {
            return defaultInt;
        }
        return (Integer) value;
    }

    public int getIntValueOrDefault(final int key, final int defaultValue) {
        if (values == null) {
            return defaultValue;
        }
        final Object value = values.get(key);
        if (value == null || !(value instanceof Integer)) {
            return defaultValue;
        }
        return (Integer) value;
    }

    public String getStringValue(final int key) {
        if (values == null) {
            return defaultString;
        }
        final Object value = values.get(key);
        if (value == null || !(value instanceof String)) {
            return defaultString;
        }
        return (String) value;
    }

    public Optional<String> getString(final int key) {
        if (values == null) {
            return Optional.ofNullable(defaultString);
        }
        final Object value = values.get(key);
        if (value == null || !(value instanceof String)) {
            return Optional.ofNullable(defaultString);
        }
        return Optional.of((String) value);
    }

    public static final String getPrettyValue(final char type, final Object value) {
        if (type == 's') {
            return (String) value;
        }
        final int intVal = (int) value;
        if (intVal == -1) {
            return "null";
        }
        switch (type) {
            case 'I':
                return (intVal >> 16) + ":" + (intVal & 65535);
            case 'c':
                final int x = intVal >> 14 & 16383;
                final int y = intVal & 16383;
                final int z = intVal >> 28;
                return x + "_" + y + "_" + z;
            case 'o':
            case 'O':
                ItemDefinitions defs = ItemDefinitions.get(intVal);
                if(defs == null) {
                    return "null_" + intVal;
                } else return defs.getName().toLowerCase().replaceAll(" ", "_") + "_" + intVal;

            case 'l':
                ObjectDefinitions defs2 = ObjectDefinitions.get(intVal);
                if(defs2 == null) {
                    return "null_" + intVal;
                } else return defs2.getName().toLowerCase().replaceAll(" ", "_") + "_" + intVal;

            default:
                return Integer.toString(intVal);
        }
    }

    @Override
    public ByteBuffer encode() {
        final ByteBuffer buffer = new ByteBuffer(4096 * 8);
        buffer.writeByte(1);
        buffer.writeByte(keyType);
        buffer.writeByte(2);
        buffer.writeByte(valueType);
        if (valueType == 's') {
            buffer.writeByte(3);
            buffer.writeString(defaultString);
            if (values != null && !values.isEmpty()) {
                buffer.writeByte(5);
                buffer.writeShort(values.size());
                for (final Map.Entry<Integer, Object> entry : values.entrySet()) {
                    buffer.writeInt(entry.getKey());
                    buffer.writeString(entry.getValue().toString());
                }
            }
        } else {
            if (defaultInt != 0) {
                buffer.writeByte(4);
                buffer.writeInt(defaultInt);
            }
            if (values != null && !values.isEmpty()) {
                buffer.writeByte(6);
                buffer.writeShort(values.size());
                for (final Map.Entry<Integer, Object> entry : values.entrySet()) {
                    buffer.writeInt(entry.getKey());
                    buffer.writeInt(Double.valueOf(entry.getValue().toString()).intValue());
                }
            }
        }
        buffer.writeByte(0);
        return buffer;
    }

    @Override
    public void pack() {
        CacheManager.getCache().getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.ENUM).addFile(new File(id, encode()));
    }

    public EnumDefinitions() {
    }

    public EnumDefinitions(int id, ScriptVarType keyTypeExt, ScriptVarType valueTypeExt) {
        this.id = id;
        this.keyTypeExt = keyTypeExt;
        this.valueTypeExt = valueTypeExt;
        this.keyType = keyTypeExt.getKeyChar();
        this.valueType = valueTypeExt.getKeyChar();
        this.values = new TreeMap<>();
    }

    public static EnumDefinitions create(int newId, ScriptVarType key, ScriptVarType val) {
        EnumDefinitions newDef = new EnumDefinitions(newId, key, val);
        newDef.defaultInt = -1;
        newDef.defaultString = "";
        return newDef;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getKeyType() {
        return keyType;
    }

    public char getValueType() {
        return valueType;
    }

    public String getDefaultString() {
        return defaultString;
    }

    public void setDefaultString(String defaultString) {
        this.defaultString = defaultString;
    }

    public int getDefaultInt() {
        return defaultInt;
    }

    public void setDefaultInt(int defaultInt) {
        this.defaultInt = defaultInt;
    }

    public Map<Integer, Object> getValues() {
        return values;
    }

    public void setValues(Map<Integer, Object> values) {
        this.values = values;
    }


    public void setValuesTyped(HashMap<Integer, Object> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "EnumDefinitions(id=" + this.getId() + ", size=" + this.getSize() + ", keyType=" + this.getKeyType() + ", valueType=" + this.getValueType() + ", defaultString=" + this.getDefaultString() + ", defaultInt=" + this.getDefaultInt() + ", values=" + this.getValues() + ", largestIntValue=" + this.getLargestIntValue() + ", largestIntKey=" + this.getLargestIntKey() + ")";
    }
}
