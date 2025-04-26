package mgi.types.config.enums;

import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;

import java.util.Map;

/**
 * @author Kris | 20/11/2018 11:40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 *
 * <p>A skeleton for enums in cache, containing the basic methods for fields that all enums have.
 * Specific methods are absent to refrain from autoboxing & unboxing due to the main type being int,
 * which cannot be generalized due to language restrictions.</p>
 */
public interface Enum<V> extends Definitions {

    /**
     * @return the id of the enum in the cache.
     */
    int getId();

    /**
     * @return the amount of pairs the enum contains.
     */
    int getSize();

    /**
     * Each char represents to a specific type that's defined at {@link EnumDefinitions#TYPE_MAP}.
     * @return the key type of the enum.
     */
    char getKeyType();

    /**
     * Each char represents to a specific type that's defined at {@link EnumDefinitions#TYPE_MAP}.
     * @return the key type of the enum.
     */
    char getValType();

    /**
     * @return the default return value of the enum, used in the absence of a pair.
     */
    V defaultValue();

    /**
     * A map containing all of the pairs defined by the enum. The key is always an integer.
     * @return map of {@param <Map#Entry#Key> of Integer} & {@param Map#Entry#Value of either Integer or String}.
     */
    Map<Integer, V> map();

    /**
     * The returned map will always be null if the enum contains overlapping values.
     * @return A reverse representation of {@link #map()}.
     */
    Map<V, Integer> reverseMap();

    /**
     * @deprecated The enums are never constructed directly, thus do not require loading - this is done by the parent
     * class {@link EnumDefinitions}.
     */
    @Override
    @Deprecated
    default void load() {

    }

    /**
     * @deprecated The enums are never constructed directly, thus do not require decoding - this is done by the parent
     * class {@link EnumDefinitions}.
     */
    @Override
    @Deprecated
    default void decode(final ByteBuffer buffer) {

    }

    /**
     * @deprecated The enums are never constructed directly, thus do not require decoding - this is done by the parent
     * class {@link EnumDefinitions}.
     */
    @Override
    @Deprecated
    default void decode(final ByteBuffer buffer, final int opcode) {

    }

}
