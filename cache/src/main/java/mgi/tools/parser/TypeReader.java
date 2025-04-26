package mgi.tools.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moandjiezana.toml.Toml;
import mgi.tools.parser.readers.AnimationReader;
import mgi.tools.parser.readers.ComponentReader;
import mgi.tools.parser.readers.EnumReader;
import mgi.tools.parser.readers.GraphicsReader;
import mgi.tools.parser.readers.ItemReader;
import mgi.tools.parser.readers.NPCReader;
import mgi.tools.parser.readers.ObjectReader;
import mgi.tools.parser.readers.SpriteReader;
import mgi.tools.parser.readers.StructReader;
import mgi.tools.parser.readers.VarbitReader;
import mgi.types.Definitions;
import mgi.types.config.SpotAnimationDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tommeh | 22/01/2020 | 18:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public interface TypeReader {
    TypeReader[] readers = {new ItemReader(), new NPCReader(), new ObjectReader(), new EnumReader(),
            new StructReader(), new AnimationReader(), new SpriteReader(), new ComponentReader(),
            new GraphicsReader(), new VarbitReader()};

    default ArrayList<Definitions> read(final Map<String, Object> properties) throws NoSuchFieldException, IllegalAccessException, CloneNotSupportedException, InstantiationException {
        return new ArrayList<>();
    }

    default ArrayList<Definitions> read(final Toml toml) throws NoSuchFieldException, IllegalAccessException, CloneNotSupportedException {
        return new ArrayList<>();
    }

    default List<Definitions> read(final ObjectMapper mapper, final JsonNode tree) throws NoSuchFieldException, IllegalAccessException, CloneNotSupportedException {
        return new ArrayList<>();
    }

    String getType();

    static void setFields(final Definitions definitions, final Map<String, Object> properties) throws IllegalAccessException, NoSuchFieldException {
        final Class<? extends Definitions> clazz = definitions.getClass();
        for (final Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            final TypeProperty property = TypeProperty.get(field.getName());
            if (property == null) {
                continue;
            }
            if (definitions instanceof SpotAnimationDefinition && property.equals(TypeProperty.REPLACEMENT_COLOURS)) {
                continue;
            }
            final String identifier = property.getIdentifier();
            if (!properties.containsKey(identifier)) {
                continue;
            }
            if (property.equals(TypeProperty.DEFAULT_INT) || property.equals(TypeProperty.DEFAULT_STRING)) {
                final Object value = properties.get(identifier);
                if (value instanceof Long) {
                    final Field f = clazz.getDeclaredField(TypeProperty.DEFAULT_INT.getField());
                    f.setAccessible(true);
                    f.set(definitions, ((Long) value).intValue());
                } else if (value instanceof Integer) {
                    final Field f = clazz.getDeclaredField(TypeProperty.DEFAULT_INT.getField());
                    f.setAccessible(true);
                    f.set(definitions, value);
                } else {
                    final Field f = clazz.getDeclaredField(TypeProperty.DEFAULT_STRING.getField());
                    f.setAccessible(true);
                    f.set(definitions, value);
                }
                continue;
            }
            if (field.getType() == String.class) {
                field.set(definitions, properties.get(identifier));
            } else if (field.getType() == int.class) {
                field.set(definitions, properties.get(identifier));
            } else if (field.getType() == boolean.class) {
                field.set(definitions, properties.get(identifier));
            } else if (field.getType() == int[].class) {
                final ArrayList<Integer> list = (ArrayList<Integer>) properties.get(identifier);
                field.set(definitions, list.stream().mapToInt(i -> i).toArray());
            } else if (field.getType() == short[].class) {
                final ArrayList<Integer> list = (ArrayList<Integer>) properties.get(identifier);
                final short[] array = new short[list.size()];
                for (int index = 0; index < array.length; index++) {
                    array[index] = list.get(index).shortValue();
                }
                field.set(definitions, array);
            } else if (field.getType() == String[].class) {
                final ArrayList<String> list = (ArrayList<String>) properties.get(identifier);
                if ((property.equals(TypeProperty.OPTIONS_ITEM) || property.equals(TypeProperty.OPTIONS_NPC_OBJECT) || property.equals(TypeProperty.FILTERED_OPTIONS)) && list.isEmpty()) {
                    field.set(definitions, new String[5]);
                } else {
                    final String[] array = new String[5];
                    for (int index = 0; index < array.length; index++) {
                        final String option = list.get(index);
                        array[index] = option.isEmpty() ? null : option;
                    }
                    field.set(definitions, array);
                }
            } else {
                //new Exception("Unhandled field type: " + field.getType() + " for def " + definitions).printStackTrace();
            }
        }
    }
}
