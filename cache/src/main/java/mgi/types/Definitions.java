package mgi.types;

import mgi.types.clientscript.ClientScriptDefinitions;
import mgi.types.component.ComponentDefinitions;
import mgi.types.config.*;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.identitykit.IdentityKitDefinitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.types.draw.sprite.SpriteGroupDefinitions;
import mgi.types.skeleton.SkeletonDefinitions;
import mgi.types.worldmap.MapElementDefinitions;
import mgi.utilities.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kris | 6. apr 2018 : 19:21.33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface Definitions {

    static void loadDefinitions(final Class<?>[] definitions) {
        for (final Class<?> clazz : definitions) {
            try {
                final Object instance = clazz.getDeclaredConstructor().newInstance();
                if (instance instanceof Definitions) {
                    ((Definitions) instance).load();
                }
            } catch (final Exception e) {
                logger.error("", e);
            }
        }
    }

    static Runnable load(final Class<?> clazz) {
        return () -> {
            try {
                final Object instance = clazz.getDeclaredConstructor().newInstance();
                if (instance instanceof Definitions) {
                    ((Definitions) instance).load();
                }
            } catch (Exception e) {
                logger.error("", e);
            }
        };
    }

    void load();

    default void decode(final ByteBuffer buffer) {
    }

    default void decode(final ByteBuffer buffer, final int opcode) {
    }

    default ByteBuffer encode() {
        return null;
    }

    default void pack() {
    }

    Logger logger = LoggerFactory.getLogger("Definitions");

    Class<?>[] highPriorityDefinitions = new Class<?>[]{
            ObjectDefinitions.class,
            NPCDefinitions.class,
            ItemDefinitions.class,
            AnimationDefinitions.class
    };

    Class<?>[] lowPriorityDefinitions = new Class<?>[]{
            EnumDefinitions.class,
            SpotAnimationDefinition.class,
            IdentityKitDefinitions.class,
            InventoryDefinitions.class,
            OverlayDefinitions.class,
            ParamDefinitions.class,
            ClientScriptDefinitions.class,
            SkeletonDefinitions.class,
            UnderlayDefinitions.class,
            VarbitDefinitions.class,
            ComponentDefinitions.class,
            HitbarDefinitions.class,
            MapElementDefinitions.class,
            StructDefinitions.class,
            SpriteGroupDefinitions.class,
            DBTableDefinition.class,
            DBRowDefinition.class
    };

}
