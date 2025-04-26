package mgi.types.component.custom;

import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.LayerComponent;
import mgi.types.component.type.RectangleComponent;
import mgi.types.component.type.TextComponent;

import java.util.ArrayList;

/**
 * @author Tommeh | 03/06/2019 | 15:07
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class TournamentViewer extends ComponentDefinitions {
    public ArrayList<ComponentDefinitions> assemble(final int interfaceId) {
        int componentId = 0;
        setIf3(true);
        setSize(300, 334);
        setDynamicPosition(1, 1);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new ComponentDefinitions(); //1
        component.setIf3(true);
        component.setParentId(0);
        component.setSize(300, 334);
        component.setDynamicPosition(1, 1);
        component.setOnLoadListener(new Object[]{227, -2147483645, "Tournament Viewer"});
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(0);
        component.setPosition(13, 43);
        component.setSize(273, 209);
        component.setOnTimerListener(new Object[]{714, -2147483645});
        add(componentId++, component);
        component = new RectangleComponent("#170801", 166, true);
        component.setParentId(2);
        component.setPosition(0, 0);
        component.setSize(273, 209);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(2);
        component.setDynamicPosition(0, 1);
        component.setDynamicSize(1, 1);
        component.setPosition(2, 0);
        component.setSize(20, 4);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(2);
        component.setDynamicPosition(2, 1);
        component.setDynamicSize(0, 1);
        component.setPosition(2, 0);
        component.setSize(16, 4);
        add(componentId++, component);
        component = new TextComponent("Players remaining:", 494, LEFT, CENTER);
        component.setParentId(0);
        component.setDynamicPosition(0, 2);
        component.setPosition(7, 55);
        component.setSize(110, 20);
        add(componentId++, component);
        component = new TextComponent("Round:", 494, LEFT, CENTER);
        component.setParentId(0);
        component.setDynamicPosition(0, 2);
        component.setPosition(7, 42);
        component.setSize(50, 20);
        add(componentId++, component);
        component = new TextComponent("0", "#ffffff", 494, LEFT, CENTER);
        component.setParentId(0);
        component.setDynamicPosition(2, 2);
        component.setDynamicSize(1, 0);
        component.setPosition(0, 55);
        component.setSize(90, 20);
        add(componentId++, component);
        component = new TextComponent("0", "#ffffff", 494, LEFT, CENTER);
        component.setParentId(0);
        component.setDynamicPosition(2, 2);
        component.setDynamicSize(1, 0);
        component.setPosition(0, 42);
        component.setSize(90, 20);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(0);
        component.setDynamicPosition(1, 2);
        component.setPosition(0, 19);
        component.setSize(100, 20);
        component.setOnLoadListener(new Object[]{1262, -2147483645});
        add(componentId++, component);
        component = new TextComponent("There are currently no fights to spectate.", "#ffffff", 495, CENTER, CENTER);
        component.setParentId(2);
        component.setHidden(true);
        component.setDynamicSize(1, 1);
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
