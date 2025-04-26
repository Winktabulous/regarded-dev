package mgi.types.component.custom;

import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.GraphicComponent;
import mgi.types.component.type.LayerComponent;
import mgi.types.component.type.RectangleComponent;
import mgi.types.component.type.TextComponent;

import java.util.ArrayList;

/**
 * @author Tommeh | 18/05/2019 | 15:42
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class TournamentInformation extends ComponentDefinitions {
    public ArrayList<ComponentDefinitions> assemble(final int interfaceId) {
        int componentId = 0;
        setIf3(true);
        setDynamicSize(1, 1);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new LayerComponent();
        component.setParentId(0);
        component.setDynamicSize(1, 1);
        component.setDynamicPosition(1, 1);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(1);
        component.setDynamicPosition(2, 0);
        component.setPosition(5, 5);
        component.setSize(135, 95);
        // component.setOnTimerListener(new Object[] { 10403 });
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(2);
        component.setDynamicPosition(1, 0);
        component.setSize(135, 95);
        add(componentId++, component);
        component = new GraphicComponent(297, 100);
        component.setParentId(3);
        component.setDynamicPosition(1, 1);
        component.setDynamicSize(0, 1);
        component.setSize(135, 0);
        add(componentId++, component);
        component = new RectangleComponent("#383023", false);
        component.setParentId(3);
        component.setDynamicPosition(1, 1);
        component.setDynamicSize(0, 1);
        component.setSize(135, 0);
        add(componentId++, component);
        component = new RectangleComponent("#5a5245", false);
        component.setParentId(3);
        component.setDynamicPosition(1, 1);
        component.setSize(133, 93);
        add(componentId++, component);
        component = new TextComponent("Tournament", 496, CENTER, CENTER);
        component.setParentId(3);
        component.setSize(134, 20);
        component.setPosition(0, 2);
        add(componentId++, component);
        component = new TextComponent("Preset: Dharok's", "#ffffff", 494, CENTER, LEFT);
        component.setParentId(3);
        component.setSize(124, 10);
        component.setPosition(5, 23);
        add(componentId++, component);
        component = new TextComponent("Players: -", "#ffffff", 494, CENTER, LEFT);
        component.setParentId(3);
        component.setSize(124, 10);
        component.setPosition(5, 35);
        component.setOnVarTransmitListener(new Object[]{10401, -2147483645, 3611});
        add(componentId++, component);
        component = new TextComponent("Round: -", "#ffffff", 494, CENTER, LEFT);
        component.setParentId(3);
        component.setHidden(false);
        component.setSize(124, 10);
        component.setPosition(5, 47);
        component.setOnVarTransmitListener(new Object[]{10402, -2147483645, 3612});
        add(componentId++, component);
        component = new TextComponent("Time left:", "#ffffff", 494, CENTER, LEFT);
        component.setParentId(3);
        component.setSize(124, 10);
        component.setPosition(5, 59);
        add(componentId++, component);
        component = new TextComponent("-", 496, CENTER, CENTER);
        component.setParentId(3);
        component.setSize(124, 30);
        component.setPosition(5, 67);
        component.setOnLoadListener(new Object[]{10403, -2147483645});
        //component.setOnVarTransmitListener(new Object[] { 10402, 3612 });
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
