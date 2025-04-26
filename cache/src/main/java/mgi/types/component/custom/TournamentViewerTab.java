package mgi.types.component.custom;

import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.LayerComponent;
import mgi.types.component.type.RectangleComponent;
import mgi.types.component.type.TextComponent;

import java.util.ArrayList;

/**
 * @author Tommeh | 31/05/2019 | 15:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class TournamentViewerTab extends ComponentDefinitions {
    public ArrayList<ComponentDefinitions> assemble(final int interfaceId) {
        int componentId = 0;
        setIf3(true);
        setDynamicPosition(1, 1);
        setDynamicSize(1, 1);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new TextComponent("Tournament Viewer", 496, CENTER, CENTER);
        component.setParentId(0);
        component.setDynamicPosition(1, 0);
        component.setDynamicSize(1, 0);
        component.setSize(0, 20);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(0);
        component.setDynamicPosition(1, 0);
        component.setPosition(0, 20);
        component.setSize(190, 169);
        component.setOnTimerListener(new Object[]{714, -2147483645});
        add(componentId++, component);
        component = new RectangleComponent("#170801", 166, true);
        component.setParentId(2);
        component.setPosition(0, 0);
        component.setSize(190, 169);
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
        component.setPosition(0, 45);
        component.setSize(110, 20);
        add(componentId++, component);
        component = new TextComponent("Round:", 494, LEFT, CENTER);
        component.setParentId(0);
        component.setDynamicPosition(0, 2);
        component.setPosition(0, 32);
        component.setSize(50, 20);
        add(componentId++, component);
        component = new TextComponent("0", "#ffffff", 494, LEFT, CENTER);
        component.setParentId(0);
        component.setDynamicPosition(2, 2);
        component.setDynamicSize(1, 0);
        component.setPosition(0, 45);
        component.setSize(90, 20);
        add(componentId++, component);
        component = new TextComponent("0", "#ffffff", 494, LEFT, CENTER);
        component.setParentId(0);
        component.setDynamicPosition(2, 2);
        component.setDynamicSize(1, 0);
        component.setPosition(0, 32);
        component.setSize(90, 20);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(0);
        component.setDynamicPosition(1, 2);
        component.setPosition(0, 12);
        component.setSize(100, 20);
        component.setOnLoadListener(new Object[]{1262, -2147483645});
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
