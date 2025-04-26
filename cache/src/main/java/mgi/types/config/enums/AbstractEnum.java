package mgi.types.config.enums;

import com.google.errorprone.annotations.Immutable;

import java.util.Map;

/**
 * @author Kris | 20/11/2018 11:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 *
 * <p>An abstract skeleton class for the enums held in cache, containing the default variables that are inherited by
 * both enum types available.</p>
 */
@Immutable
abstract class AbstractEnum<V> implements Enum<V> {

    /**
     * The constructor for enums.
     * @param id the id of the enum as represented by the cache.
     * @param keyType the keyType character for the enum. {@link EnumDefinitions#TYPE_MAP} for mappings.
     * @param valType the valType character for the enum.{@link EnumDefinitions#TYPE_MAP} for mappings.
     * @param defaultValue the default value of the enum, either in the form of an Integer or a String.
     * @param map a wildcard map containing the pairs for the enum.
     */
    AbstractEnum(final int id, final char keyType, final char valType, final V defaultValue,
                 final Map<Integer, ?> map) {
        this.id = id;
        this.size = map == null ? 0 : map.size();
        this.keyType = keyType;
        this.valType = valType;
        this.defaultValue = defaultValue;
    }

    protected final int id;
    protected final int size;
    protected final char keyType;
    protected final char valType;
    protected final V defaultValue;

    /**
     * Defines whether the enum contains overlapping values or not.
     */
    /*boolean overlappingValues;*/
    abstract boolean containsOverlappingValues();

    /**
     * An abstract method used for populating the type-specific maps with the wildcard map.
     * @param map a wildcard map containing the pairs for the enum.
     * @return whether or not the {@param map} contains overlapping values.
     */
    abstract boolean populate(final Map<Integer, ?> map, final Map<Integer, V> populatedMap,
                           final Map<V, Integer> reversePopulatedMap);

    /**
     * Verifies that the enum is not empty.
     * @throws RuntimeException if the enum is empty.
     */
    void verifyPresence() {
        if (size == 0) {
            throw new RuntimeException("Enum is empty.");
        }
    }

    /**
     * @return the amount of bytes required for the buffer to encode the enum in its entirety.
     */
    abstract int encodedSize();

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public char getKeyType() {
        return keyType;
    }

    public char getValType() {
        return valType;
    }

    public V getDefaultValue() {
        return defaultValue;
    }

}
