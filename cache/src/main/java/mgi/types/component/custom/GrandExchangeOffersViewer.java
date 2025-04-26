package mgi.types.component.custom;

import com.zenyte.game.util.AccessMask;
import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.*;

import java.util.ArrayList;

/**
 * @author Tommeh | 16/08/2019 | 16:25
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class GrandExchangeOffersViewer extends ComponentDefinitions {
    public ArrayList<ComponentDefinitions> assemble(final int interfaceId) {
        int componentId = 0;
        setIf3(true); //universe
        setDynamicSize(1, 1);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new ComponentDefinitions(); //main_component
        component.setIf3(true);
        component.setParentId(0);
        component.setSize(488, 330);
        component.setOnLoadListener(new Object[] {10800});
        add(componentId++, component);
        component = new LayerComponent(); //layout
        component.setParentId(1);
        component.setDynamicSize(1, 1);
        component.setDynamicPosition(1, 1);
        add(componentId++, component);
        component = new GraphicComponent(314);
        component.setParentId(1);
        component.setPosition(5, 40);
        component.setSize(478, 36);
        component.setDynamicPosition(0, 2);
        component.setSpriteTiling(true);
        add(componentId++, component);
        component = new LayerComponent(); //3
        component.setParentId(1);
        component.setPosition(7, 7);
        component.setSize(57, 20);
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Exchange");
        component.setOnLoadListener(new Object[] {540, -2147483645, "Exchange"});
        add(componentId++, component);
        component = new LayerComponent(); //4
        component.setParentId(1);
        component.setPosition(14, 13);
        component.setSize(40, 36);
        component.setDynamicPosition(0, 2);
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Search for item");
        add(componentId++, component);
        component = new GraphicComponent(1120);
        component.setParentId(5);
        component.setSize(40, 36);
        add(componentId++, component);
        component = new GraphicComponent(1121);
        component.setParentId(5);
        component.setSize(40, 36);
        component.setOnTimerListener(new Object[] {811, -2147483645, -1, 0, 100, 250});
        add(componentId++, component);
        component = new GraphicComponent(-1);
        component.setParentId(5);
        component.setPosition(2, 2);
        component.setSize(36, 32);
        component.setBorderType(1);
        component.setShadowColor("#333333");
        add(componentId++, component);
        component = new GraphicComponent(1113);
        component.setParentId(5);
        component.setSize(20, 18);
        add(componentId++, component);
        component = new LayerComponent(); //10
        component.setParentId(1);
        component.setPosition(54, 8);
        component.setSize(425, 45);
        component.setDynamicPosition(0, 2);
        add(componentId++, component);
        component = new TextComponent("-", 496, 1, 1); //8
        component.setParentId(10);
        component.setPosition(0, 5);
        component.setSize(140, 35);
        add(componentId++, component);
        component = new LineComponent("ff981f", 45); //8
        component.setParentId(10);
        component.setPosition(144, 22);
        component.setSize(1, 0);
        add(componentId++, component);
        component = new GraphicComponent(1112);
        component.setParentId(10);
        component.setSize(25, 23);
        component.setPosition(160, 3);
        add(componentId++, component);
        component = new TextComponent("GE", 495);
        component.setParentId(10);
        component.setPosition(165, 27);
        component.setSize(20, 20);
        add(componentId++, component);
        component = new TextComponent("-", "#ffffff", 495, 1, 1); //8
        component.setParentId(10);
        component.setPosition(186, 6);
        component.setSize(95, 35);
        add(componentId++, component);
        component = new GraphicComponent(699); //button
        component.setParentId(10);
        component.setSize(17, 17);
        component.setPosition(359, 3);
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Select");
        component.setOnClickListener(new Object[] {10806, 1});
        add(componentId++, component);
        component = new GraphicComponent(697); //button
        component.setParentId(10);
        component.setSize(17, 17);
        component.setPosition(359, 24);
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Select");
        component.setOnClickListener(new Object[] {10806, 0});
        add(componentId++, component);
        component = new TextComponent("Buying", 495);
        component.setParentId(10);
        component.setPosition(382, 4);
        component.setSize(50, 20);
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Select");
        component.setOnClickListener(new Object[] {10806, 1});
        add(componentId++, component);
        component = new TextComponent("Selling", 495);
        component.setParentId(10);
        component.setPosition(382, 25);
        component.setSize(50, 20);
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Select");
        component.setOnClickListener(new Object[] {10806, 0});
        add(componentId++, component);
        component = new LayerComponent(); //20
        component.setHidden(true);
        component.setParentId(1);
        component.setPosition(8, 36);
        component.setSize(472, 103);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new RectangleComponent("#000000", 100, false);
        component.setParentId(20);
        component.setPosition(0, 0);
        component.setSize(0, 0);
        component.setDynamicSize(1, 1);
        add(componentId++, component);
        component = new RectangleComponent("#ff981f", 200, false);
        component.setParentId(20);
        component.setPosition(1, 1);
        component.setSize(2, 2);
        component.setDynamicSize(1, 1);
        add(componentId++, component);
        component = new RectangleComponent("#4E453A", 0, true);
        component.setParentId(20);
        component.setPosition(2, 2);
        component.setSize(4, 4);
        component.setDynamicSize(1, 1);
        add(componentId++, component);
        component = new RectangleComponent("#000000", 100, false); //collumn row
        component.setParentId(20);
        component.setPosition(0, 0);
        component.setSize(0, 22);
        component.setDynamicSize(1, 0);
        add(componentId++, component);
        component = new RectangleComponent("#ff981f", 200, false);
        component.setParentId(20);
        component.setPosition(1, 1);
        component.setSize(2, 20);
        component.setDynamicSize(1, 0);
        add(componentId++, component);
        component = new TextComponent("Quantity", "ffffff", 494, 1, 1);
        component.setParentId(20);
        component.setPosition(2, 1);
        component.setSize(50, 20);
        add(componentId++, component);
        component = new TextComponent("Price", "ffffff", 494, 1, 1);
        component.setParentId(20);
        component.setPosition(96, 1);
        component.setSize(50, 20);
        add(componentId++, component);
        component = new TextComponent("Seller", "ffffff", 494, 1, 1);
        component.setParentId(20);
        component.setPosition(210, 1);
        component.setSize(50, 20);
        add(componentId++, component);
        component = new LayerComponent(); //sort quantity
        component.setParentId(20);
        component.setPosition(49, 9);
        component.setSize(11, 4);
        component.setOnLoadListener(new Object[] {10807, -2147483645, 0, 1, "Sort by quantity"});
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Sort by quantity");
        add(componentId++, component);
        component = new LayerComponent(); //sort price
        component.setParentId(20);
        component.setPosition(135, 9);
        component.setSize(11, 4);
        component.setOnLoadListener(new Object[] {10807, -2147483645, 2, 3, "Sort by price"});
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Sort by price");
        add(componentId++, component);
        component = new LayerComponent(); //sort name
        component.setParentId(20);
        component.setPosition(251, 9);
        component.setSize(11, 4);
        component.setOnLoadListener(new Object[] {10807, -2147483645, 4, 5, "Sort by name"});
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Sort by name");
        add(componentId++, component);
        component = new LayerComponent(); //32 scrolllayer
        component.setParentId(20);
        component.setPosition(1, 23);
        component.setSize(470, 25);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new LayerComponent();// 33 scrollbar
        component.setParentId(20);
        component.setPosition(2, 23);
        component.setSize(16, 25);
        component.setDynamicSize(0, 1);
        component.setDynamicPosition(2, 0);
        add(componentId++, component);
        component = new TextComponent("Search for an item and what type of offers you're looking for.", "ffffff", 495, 1, 1);
        component.setHidden(true);
        component.setParentId(1);
        component.setPosition(8, 36);
        component.setSize(469, 101);
        component.setDynamicSize(0, 1);
        add(componentId++, component);
        component = new LayerComponent(); //35 tooltip
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
