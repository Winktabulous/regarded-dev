package mgi.types.component.custom;

import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.GraphicComponent;
import mgi.types.component.type.LayerComponent;
import mgi.types.component.type.RectangleComponent;
import mgi.types.component.type.TextComponent;

import java.util.ArrayList;

/**
 * @author Tommeh | 05/05/2019 | 15:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class DailyChallengesOverview extends ComponentDefinitions {
    public ArrayList<ComponentDefinitions> assemble(final int interfaceId) {
        int componentId = 0;
        setIf3(true);
        setSize(24, 300);
        setDynamicSize(1, 0);
        setPosition(0, 20);
        setDynamicPosition(1, 0);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new ComponentDefinitions();
        component.setIf3(true);
        component.setParentId(0);
        component.setDynamicSize(1, 1);
        component.setPosition(0, 0);
        component.setDynamicPosition(1, 1);
        component.setOnLoadListener(new Object[]{10300});
        add(componentId++, component);
        component = new LayerComponent(); //2
        component.setParentId(0);
        component.setPosition(13, 43);
        component.setSize(137, 242);
        component.setOnLoadListener(new Object[]{712, -2147483645, 0});
        add(componentId++, component);
        component = new RectangleComponent("#170801", 166, true);
        component.setParentId(2);
        component.setPosition(0, 0);
        component.setSize(137, 242);
        add(componentId++, component);
        component = new LayerComponent(); //4 challenges
        component.setParentId(0);
        component.setPosition(19, 49);
        component.setSize(125, 230);
        add(componentId++, component);
        component = new TextComponent("-", 645, CENTER, CENTER);
        component.setParentId(0);
        component.setPosition(192, 35);
        component.setSize(285, 52);
        add(componentId++, component);
        component = new GraphicComponent(-1);
        component.setParentId(0);
        component.setPosition(161, 48);
        component.setSize(30, 30);
        add(componentId++, component);
        component = new LayerComponent(); //7
        component.setParentId(0);
        component.setPosition(160, 93);
        component.setSize(315, 45);
        component.setOnLoadListener(new Object[]{712, -2147483645, 0});
        add(componentId++, component);
        component = new RectangleComponent("#170801", 166, true);
        component.setParentId(7);
        component.setPosition(0, 0);
        component.setSize(315, 44);
        add(componentId++, component);
        component = new TextComponent("<col=ff981f>Category:<col> -", "#ffffff", 496, CENTER, LEFT);
        component.setParentId(7);
        component.setPosition(10, 12);
        component.setSize(145, 20);
        add(componentId++, component);
        component = new TextComponent("<col=ff981f>Difficulty:</col> -", "#ffffff", 496, CENTER, LEFT);
        component.setParentId(7);
        component.setPosition(172, 12);
        component.setSize(125, 20);
        add(componentId++, component);
        component = new LayerComponent(); //11
        component.setParentId(0);
        component.setPosition(160, 143);
        component.setSize(315, 81);
        component.setOnLoadListener(new Object[]{712, -2147483645, 0});
        add(componentId++, component);
        component = new RectangleComponent("#170801", 166, true);
        component.setParentId(11);
        component.setPosition(0, 0);
        component.setSize(315, 81);
        add(componentId++, component);
        component = new TextComponent("Progress", 496, CENTER, CENTER);
        component.setParentId(11);
        component.setPosition(120, 8);
        component.setSize(75, 30);
        add(componentId++, component);
        component = new LayerComponent(); //14
        component.setParentId(11);
        component.setPosition(15, 39);
        component.setSize(284, 26);
        component.setOnLoadListener(new Object[]{991, -2147483645, 0});
        add(componentId++, component);
        component = new RectangleComponent("#453933", 0, true);
        component.setParentId(14);
        component.setPosition(3, 3);
        component.setSize(278, 19);
        add(componentId++, component);
        component = new RectangleComponent("#2ea315", 0, true);
        component.setParentId(14);
        component.setPosition(3, 3);
        component.setSize(0, 19);
        add(componentId++, component);
        component = new TextComponent("-", "#ffffff", 494, CENTER, CENTER);
        component.setParentId(14);
        component.setPosition(101, 5);
        component.setSize(80, 17);
        add(componentId++, component);
        component = new LayerComponent(); //18
        component.setParentId(0);
        component.setPosition(160, 229);
        component.setSize(215, 54);
        component.setOnLoadListener(new Object[]{712, -2147483645, 0});
        add(componentId++, component);
        component = new RectangleComponent("#170801", 166, true);
        component.setParentId(18);
        component.setPosition(0, 0);
        component.setSize(215, 54);
        add(componentId++, component);
        component = new TextComponent("Rewards:", 496, CENTER, LEFT);
        component.setParentId(18);
        component.setPosition(14, 17);
        component.setSize(63, 20);
        add(componentId++, component);
        component = new LayerComponent(); //21
        component.setParentId(18);
        component.setPosition(0, 0);
        component.setSize(215, 54);
        add(componentId++, component);
        component = new LayerComponent(); //22
        component.setParentId(0);
        component.setPosition(397, 241);
        component.setSize(60, 30);
        add(componentId++, component);
        final ArrayList<ComponentDefinitions> list = new ArrayList<ComponentDefinitions>();
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
