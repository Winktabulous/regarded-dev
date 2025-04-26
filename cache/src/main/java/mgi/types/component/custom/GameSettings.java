package mgi.types.component.custom;

import com.zenyte.game.util.AccessMask;
import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.GraphicComponent;
import mgi.types.component.type.LayerComponent;
import mgi.types.component.type.RectangleComponent;
import mgi.types.component.type.TextComponent;

import java.util.ArrayList;

/**
 * @author Tommeh | 26-4-2019 | 23:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class GameSettings extends ComponentDefinitions {
    public ArrayList<ComponentDefinitions> assemble(final int interfaceId) {
        int componentId = 0;
        setIf3(true);
        setSize(190, 261);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new TextComponent("Game Settings", 496, CENTER, CENTER);
        component.setParentId(0);
        component.setPosition(27, 4);
        component.setSize(130, 18);
        add(componentId++, component);
        component = new GraphicComponent(2505);
        component.setParentId(0);
        component.setPosition(28, 6);
        component.setSize(13, 13);
        add(componentId++, component);
        component = new LayerComponent(); //3
        component.setParentId(0);
        component.setPosition(3, 26);
        component.setSize(184, 229);
        component.setOnLoadListener(new Object[]{712, -2147483645, 0});
        add(componentId++, component);
        component = new GraphicComponent(831);
        component.setParentId(0);
        component.setPosition(166, 5);
        component.setSize(16, 16);
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Close");
        component.setOnMouseOverListener(new Object[]{44, -2147483645, 832});
        component.setOnMouseLeaveListener(new Object[]{44, -2147483645, 831});
        component.setOnOpListener(new Object[]{29});
        add(componentId++, component);
        component = new RectangleComponent("#170801", 166, true);
        component.setParentId(3);
        component.setPosition(0, 0);
        component.setSize(185, 229);
        add(componentId++, component);
        component = new LayerComponent(); //6 scrolllayer
        component.setParentId(0);
        component.setPosition(9, 32);
        component.setSize(171, 216);
        add(componentId++, component);
        component = new LayerComponent(); //7 scrollbar
        component.setParentId(0);
        component.setPosition(166, 31);
        component.setSize(16, 218);
        add(componentId++, component);
        component = new LayerComponent(); //8 used for tooltips
        component.setParentId(0);
        component.setPosition(0, 0);
        component.setSize(1, 1);
        add(componentId++, component);
        final ArrayList<ComponentDefinitions> list = new ArrayList<>();
        list.add(this);
        for (final ComponentDefinitions c : getChildren()) {
            if (c == null) {
                continue;
            }
            list.add(c);
        }
        return list;
    }

}
