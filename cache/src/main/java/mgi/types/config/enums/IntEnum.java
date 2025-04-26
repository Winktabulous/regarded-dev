package mgi.types.config.enums;

import com.google.errorprone.annotations.Immutable;
import it.unimi.dsi.fastutil.ints.Int2IntAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntMaps;
import mgi.utilities.ByteBuffer;

import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Consumer;

/**
 * @author Kris | 20/11/2018 11:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 *
 * <p>A primitive int representation of an enum held in the cache. We use type-specific enums because the language
 * restrictions of generics cause a lot of unnecessary garbage.</p>
 */
@Immutable
@SuppressWarnings({"unused", "WeakerAccess"})
public final class IntEnum extends AbstractEnum<Integer> {
    private final transient Int2IntMap intMap;
    private final transient Int2IntMap reverseIntMap;
    private final transient int defaultInt;
    private final transient boolean containsOverlappingValues;

    public IntEnum(final int id, final char keyType, final char valType, final int defaultValue, final Map<Integer, ?> map) {
        super(id, keyType, valType, defaultValue, map);
        defaultInt = defaultValue;
        Int2IntMap intMap = new Int2IntAVLTreeMap();
        Int2IntMap reverseIntMap = new Int2IntAVLTreeMap();
        this.containsOverlappingValues = populate(map, intMap, reverseIntMap);
        this.intMap = Int2IntMaps.unmodifiable(intMap);
        this.reverseIntMap = containsOverlappingValues ? null : Int2IntMaps.unmodifiable(reverseIntMap);
    }

    @Override
    boolean populate(final Map<Integer, ?> map, final Map<Integer, Integer> populatedMap, final Map<Integer, Integer> reversePopulatedMap) {
        boolean overlappingValues = false;
        int key;
        int value;
        for (final Map.Entry<Integer, ?> entry : map.entrySet()) {
            key = entry.getKey();
            value = (Integer) entry.getValue();
            if (!overlappingValues && reversePopulatedMap.containsKey(value)) {
                overlappingValues = true;
            }
            populatedMap.put(key, value);
            if (!overlappingValues) {
                reversePopulatedMap.put(value, key);
            }
        }
        return overlappingValues;
    }

    /**
     * {@inheritDoc}
     * @deprecated use {@link IntEnum#defaultInt()} instead to prevent autoboxing & unboxing.
     */
    @Override
    @Deprecated
    public Integer defaultValue() {
        return defaultValue;
    }

    /**
     * @return the default return value of the enum in the primtive form.
     */
    public int defaultInt() {
        return defaultInt;
    }

    /**
     * @deprecated Use {@link IntEnum#getValues()} instead to prevent autoboxing & unboxing.
     * @return a map containing the enum values.
     * @throws IllegalArgumentException if the map is empty.
     */
    @Override
    @Deprecated
    public Map<Integer, Integer> map() {
        return intMap;
    }

    /**
     * Allocates a new buffer of size {@link IntEnum#encodedSize()}, encodes it with the enum's data.
     * @return a new bytebuffer containing the enum in encoded form.
     */
    @Override
    public ByteBuffer encode() {
        final ByteBuffer buffer = new ByteBuffer(encodedSize());
        buffer.writeByte(1);
        buffer.writeByte(keyType);
        buffer.writeByte(2);
        buffer.writeByte(valType);
        if (defaultInt != 0) {
            buffer.writeByte(4);
            buffer.writeInt(defaultInt);
        }
        if (intMap != null) {
            buffer.writeByte(6);
            buffer.writeShort(intMap.size());
            for (final Int2IntMap.Entry entry : intMap.int2IntEntrySet()) {
                buffer.writeInt(entry.getIntValue());
                buffer.writeInt(entry.getIntValue());
            }
        }
        buffer.writeByte(0);
        return buffer;
    }

    @Override
    int encodedSize() {
        int size = 5;
        if (defaultInt != 0) {
            size += 5;
        }
        if (intMap != null) {
            size += 3 + (intMap.size() * 8);
        }
        return size;
    }

    /**
     * {@inheritDoc}
     * @deprecated use {@link IntEnum#getReverseValues()} instead to prevent autoboxing & unboxing.
     */
    @Override
    @Deprecated
    public Map<Integer, Integer> reverseMap() {
        return reverseIntMap;
    }

    /**
     * @return the values of the enum in a primitive int2int map.
     * @throws RuntimeException if the enum is empty.
     */
    public Int2IntMap getValues() {
        return intMap;
    }

    /**
     * @return the values of the enum in a primitive int2int map in reverse.
     * @throws RuntimeException if the enum is empty.
     * @throws IllegalArgumentException if the enum contains overlapping values.
     */
    public Int2IntMap getReverseValues() {
        if (reverseIntMap == null) {
            throw new IllegalArgumentException("Enum contains overlapping values.");
        }
        return reverseIntMap;
    }

    /**
     * @param key the key of the pair.
     * @return gets the value of the pair, or the {@link IntEnum#defaultInt} on absence.
     * @throws RuntimeException if the enum is empty.
     */
    public int getValueOrDefault(final int key) {
        if (!getValues().containsKey(key)) {
            return defaultInt;
        }
        return intMap.get(key);
    }

    /**
     * @param key the key of the map pair.
     * @return an OptionalInt containing the value of the map pair, or empty representation if absent.
     * @throws RuntimeException if the enum is empty.
     */
    public OptionalInt getValue(final int key) {
        if (!getValues().containsKey(key)) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(intMap.get(key));
    }

    /**
     * @param value the value of the map pair.
     * @return an OptionalInt containing the key of the map pair, or empty representation if absent.
     * @throws RuntimeException if the enum is empty.
     * @throws IllegalArgumentException if the enum contains overlapping values.
     */
    public OptionalInt getKey(final int value) {
        if (!getReverseValues().containsKey(value)) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(reverseIntMap.get(value));
    }

    /**
     * Iterates over all of the map pairings, accepting the consumer on every pair.
     *
     * @param consumer the entry consumer.
     * @throws RuntimeException if the enum is empty.
     */
    public void forEach(final Consumer<Int2IntMap.Entry> consumer) {
        for (final Int2IntMap.Entry entry : getValues().int2IntEntrySet()) {
            consumer.accept(entry);
        }
    }

    @Override
    public String toString() {
        return "IntEnum{" +
                "intMap=" + intMap +
                ", defaultInt=" + defaultInt +
                ", id=" + id +
                ", size=" + size +
                ", keyType=" + keyType +
                ", valType=" + valType +
                '}';
    }

    public boolean containsOverlappingValues() {
        return containsOverlappingValues;
    }
}
