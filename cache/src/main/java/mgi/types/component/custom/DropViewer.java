package mgi.types.component.custom;

import com.zenyte.game.util.AccessMask;
import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.GraphicComponent;
import mgi.types.component.type.LayerComponent;
import mgi.types.component.type.RectangleComponent;
import mgi.types.component.type.TextComponent;

import java.util.ArrayList;

/**
 * @author Tommeh | 19-11-2018 | 19:29
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class DropViewer extends ComponentDefinitions {

    public ArrayList<ComponentDefinitions> assemble(final int interfaceId) {
        int componentId = 0;
        setIf3(true); //universe
        setDynamicSize(1, 1);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new ComponentDefinitions(); //main
        component.setIf3(true);
        component.setParentId(0);
        component.setSize(488, 330);
        component.setOnLoadListener(new Object[]{10102, interfaceId << 16 | 30, interfaceId << 16 | 15,
                interfaceId << 16 | 32});
        add(componentId++, component);
        component = new LayerComponent(); //layout
        component.setParentId(1);
        component.setDynamicSize(1, 1);
        component.setDynamicPosition(1, 1);
        add(componentId++, component);
        component = new LayerComponent(); //left top 3
        component.setParentId(1);
        component.setPosition(8, 36);
        component.setSize(137, 45);
        add(componentId++, component);
        component = new RectangleComponent("#000000", 100, false);
        component.setParentId(3);
        component.setPosition(0, 0);
        component.setSize(137, 45);
        add(componentId++, component);
        component = new RectangleComponent("#ff981f", 200, false);
        component.setParentId(3);
        component.setPosition(1, 1);
        component.setSize(135, 43);
        add(componentId++, component);
        component = new RectangleComponent("#4E453A", 0, true);
        component.setParentId(3);
        component.setPosition(2, 2);
        component.setSize(133, 41);
        add(componentId++, component);
        component = new GraphicComponent(699); //button
        component.setParentId(3);
        component.setSize(17, 17);
        component.setPosition(6, 25);
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Select");
        component.setOnClickListener(new Object[]{10118, 1});
        add(componentId++, component);
        component = new GraphicComponent(697); //button
        component.setParentId(3);
        component.setSize(17, 17);
        component.setPosition(86, 25);
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Select");
        component.setOnClickListener(new Object[]{10118, 0});
        add(componentId++, component);
        component = new TextComponent("NPC", "ffffff", 495, CENTER, CENTER);
        component.setParentId(3);
        component.setSize(27, 16);
        component.setPosition(30, 26);
        add(componentId++, component);
        component = new TextComponent("Item", "ffffff", 495, CENTER, CENTER); //9
        component.setParentId(3);
        component.setSize(27, 16);
        component.setPosition(106, 26);
        add(componentId++, component);
        ////end left top
        component = new LayerComponent(); //left bottom 10
        component.setParentId(1);
        component.setPosition(8, 83);
        component.setSize(137, 94);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new RectangleComponent("#000000", 100, false);
        component.setParentId(11);
        component.setPosition(0, 0);
        component.setSize(137, 0);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new RectangleComponent("#ff981f", 200, false);
        component.setParentId(11);
        component.setPosition(1, 1);
        component.setSize(135, 2);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new RectangleComponent("#4E453A", 0, true);
        component.setParentId(11);
        component.setPosition(2, 2);
        component.setSize(133, 4);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new LayerComponent(); //14 scrolllayer
        component.setParentId(1);
        component.setPosition(8, 85);
        component.setSize(119, 100);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new LayerComponent(); //15 scrollbar
        component.setParentId(1);
        component.setPosition(127, 85);
        component.setSize(16, 99);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new LayerComponent(); //16
        component.setParentId(1);
        component.setPosition(147, 36);
        component.setSize(332, 47);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new RectangleComponent("#000000", 100, false);
        component.setParentId(17);
        component.setPosition(0, 0);
        component.setSize(332, 0);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new RectangleComponent("#ff981f", 200, false);
        component.setParentId(17);
        component.setPosition(1, 1);
        component.setSize(330, 2);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new RectangleComponent("#4E453A", 0, true);
        component.setParentId(17);
        component.setPosition(2, 2);
        component.setSize(328, 4);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new RectangleComponent("#000000", 100, false); //collumn row 20
        component.setParentId(17);
        component.setPosition(0, 0);
        component.setSize(332, 22);
        add(componentId++, component);
        component = new RectangleComponent("#ff981f", 200, false);
        component.setParentId(17);
        component.setPosition(1, 1);
        component.setSize(330, 20);
        add(componentId++, component);
        component = new TextComponent("", "ffffff", 494, CENTER, CENTER); //text = Item/NPC
        component.setParentId(17);
        component.setSize(27, 16);
        component.setPosition(105, 4);
        add(componentId++, component);
        component = new TextComponent("Quantity", "ffffff", 494, CENTER, CENTER);
        component.setParentId(17);
        component.setSize(46, 16);
        component.setPosition(194, 4);
        add(componentId++, component);
        component = new TextComponent("Rarity", "ffffff", 494, CENTER, CENTER); //24
        component.setParentId(17);
        component.setSize(41, 16);
        component.setPosition(262, 4);
        add(componentId++, component);
        component = new LayerComponent(); //26 scrolllayer
        component.setParentId(1);
        component.setPosition(147, 58);
        component.setSize(332, 71);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new LayerComponent();// 27 scrollbar
        component.setParentId(1);
        component.setPosition(461, 59);
        component.setSize(16, 72);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new LayerComponent();// 19 search input
        component.setParentId(1);
        component.setPosition(12, 40);
        component.setSize(129, 20);
        component.setOnLoadListener(new Object[]{10110, -2147483645});
        add(componentId++, component);
        component = new RectangleComponent("#675f55", true);// sprite bg
        component.setParentId(28);
        component.setPosition(0, 0);
        component.setSize(129, 20);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(28);
        component.setPosition(4, 4);
        component.setSize(124, 14);
        component.setOnClickListener(new Object[]{1905, 10223631});
        add(componentId++, component);
        component = new TextComponent("", "ffffff", 494, CENTER, CENTER); //response
        component.setParentId(17);
        component.setHidden(true);
        component.setSize(325, 0);
        component.setPosition(5, 0);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new GraphicComponent(942, 0); //23
        component.setParentId(17);
        component.setSize(18, 18);
        component.setPosition(300, 1);
        component.setOption(0, "<col=ff981f>Switch to</col>");
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOnClickListener(new Object[]{10116, interfaceId << 16 | 32});
        component.setOnMouseOverListener(new Object[]{273, -2147483645, 100});
        component.setOnMouseLeaveListener(new Object[]{273, -2147483645, 0});
        add(componentId++, component);
        component = new GraphicComponent(1113); // 24
        component.setParentId(1);
        component.setOpacity(100);
        component.setPosition(123, 41);
        component.setSize(18, 16);
        component.setOption(0, "Search...");
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOnClickListener(new Object[]{10117});
        component.setOnMouseOverListener(new Object[]{273, -2147483645, 0});
        component.setOnMouseLeaveListener(new Object[]{273, -2147483645, 100});
        add(componentId++, component);
        component = new LayerComponent(); //tooltip
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
