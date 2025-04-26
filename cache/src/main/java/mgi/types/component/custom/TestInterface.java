package mgi.types.component.custom;

import com.zenyte.game.util.AccessMask;
import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.GraphicComponent;
import mgi.types.component.type.LayerComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author Tommeh | 3 jun. 2018 | 18:59:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class TestInterface extends ComponentDefinitions {
    private static final Logger log = LoggerFactory.getLogger(TestInterface.class);

    public void buildInterface(final int interfaceId) {
        int componentId = 0;
        setSize(765, 503);
        setPosition(138, 86);
        setDynamicSize(1, 1);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new ComponentDefinitions();
        component.setDynamicPosition(1, 1);
        component.setDynamicSize(1, 1);
        component.setOnLoadListener(new Object[]{227, -2147483645, "Test Interface"});
        component.setParentId(0);
        add(componentId++, component);
        component = new GraphicComponent(170, 0);
        component.setPosition(200, 116);
        component.setSize(36, 36);
        component.setActions(new String[]{"Test button"});
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOnMouseOverListener(new Object[]{273, interfaceId << 16 | 3, 75});
        component.setOnMouseLeaveListener(new Object[]{273, interfaceId << 16 | 3, 0});
        component.setOnOpListener(new Object[]{292, -2147483645, 179, 170, -2147483644});
        add(componentId++, component);
        component = new GraphicComponent(1656, 0);
        component.setPosition(207, 123);
        component.setSize(22, 22);
        add(componentId++, component);
        component = new LayerComponent();
        component.setPosition(200, 200);
        component.setSize(36, 36);
        component.setActions(new String[]{"Test button"});
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOnLoadListener(new Object[]{92, -2147483645});
        component.setOnMouseOverListener(new Object[]{94, -2147483645});
        component.setOnMouseLeaveListener(new Object[]{92, -2147483645});
        component.setOnMouseRepeatListener(new Object[]{38, -2147483645, interfaceId << 16 | 6, "Level 1<br>Thick " +
                "Skin<br>Increases your defence by 5%.", 25, 186});
        add(componentId++, component);
        component = new GraphicComponent(1656, 0);
        component.setPosition(207, 207);
        component.setSize(22, 22);
        add(componentId++, component);
        component = new LayerComponent();
        component.setSize(1, 1);
        component.setParentId(0);
        add(componentId++, component);
    }

}
