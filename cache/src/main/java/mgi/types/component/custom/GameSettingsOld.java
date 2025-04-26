package mgi.types.component.custom;

import com.zenyte.game.util.AccessMask;
import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.GraphicComponent;
import mgi.types.component.type.LayerComponent;
import mgi.types.component.type.RectangleComponent;
import mgi.types.component.type.TextComponent;

import java.util.ArrayList;

/**
 * @author Tommeh | 18-1-2019 | 19:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class GameSettingsOld extends ComponentDefinitions {
    public ArrayList<ComponentDefinitions> assemble(final int interfaceId) {
        int componentId = 0;
        setIf3(true);
        setSize(190, 261);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new LayerComponent();
        component.setParentId(0);
        component.setPosition(1, 1);
        component.setSize(188, 258);
        component.setOnLoadListener(new Object[]{991, -2147483645, 26});
        add(componentId++, component);
        component = new GraphicComponent(897);
        component.setParentId(1);
        component.setSpriteTiling(true);
        component.setXAllignment(0);
        component.setYAllignment(0);
        component.setDynamicSize(1, 1);
        add(componentId++, component);
        component = new TextComponent("Game Settings", FONT_BOLD, LEFT, CENTER);
        component.setParentId(1);
        component.setPosition(22, 4);
        component.setSize(120, 16);
        add(componentId++, component);
        component = new GraphicComponent(831);
        component.setPosition(168, 5);
        component.setSize(16, 16);
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Close");
        component.setOnMouseOverListener(new Object[]{44, -2147483645, 832});
        component.setOnMouseLeaveListener(new Object[]{44, -2147483645, 831});
        component.setOnOpListener(new Object[]{29});
        add(componentId++, component);
        component = new LayerComponent(); //5
        component.setParentId(0);
        component.setPosition(1, 25);
        component.setSize(188, 231);
        component.setScrollHeight(450);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(5);
        component.setPosition(0, 0);
        component.setSize(188, 200);
        component.setOnScrollWheelListener(new Object[]{36, interfaceId << 16 | componentId + 1,
                interfaceId << 16 | componentId, -2147483646});
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(0);
        component.setPosition(168, 28);
        component.setSize(16, 227);
        component.setOnLoadListener(new Object[]{30, -2147483645, interfaceId << 16 | componentId - 1});
        component.setOnScrollWheelListener(new Object[]{36, interfaceId << 16 | componentId,
                interfaceId << 16 | componentId - 1});
        add(componentId++, component);
        component = new GraphicComponent(988);
        component.setParentId(1);
        component.setSize(32, 255);
        component.setPosition(149, 0);
        add(componentId++, component);
        /* component = new GraphicComponent(987);
        component.setParentId(1);
        component.setSize(160, 32);
        component.setPosition(0, 10);
        add(componentId++, component);*/
        final int settingNodes = 7;
        final String[] settings = new String[]{"Level-up dialogues", "Rare drop broadcasts", "Level-99 broadcasts",
                "Maxed player broadcasts", "200 million XP broadcasts", "Pet broadcasts", "Hardcore Ironman death " +
                "broadcasts"};
        for (int i = 0; i < settingNodes; i++) {
            final String text = settings[i];
            component = new TextComponent(text, FONT_REGULAR, CENTER, LEFT);
            component.setParentId(5);
            component.setSize(125, 27);
            component.setPosition(6, 5 + (i * 35));
            add(componentId++, component);
            component = new GraphicComponent(987);
            component.setParentId(5);
            component.setSize(160, 32);
            component.setPosition(2, 21 + (i * 35));
            add(componentId++, component);
            if (i == 0) {
                //input box
                component = new RectangleComponent("#423c3c");
                component.setParentId(5);
                component.setSize(21, 21);
                component.setPosition(137, 7 + (i * 35));
                add(componentId++, component);
                component = new RectangleComponent("#999999", false);
                component.setParentId(5);
                component.setSize(22, 22);
                component.setPosition(136, 6 + (i * 35));
                add(componentId++, component);
                component = new TextComponent("10", "#b3b3b3", FONT_BOLD, CENTER, CENTER);
                component.setParentId(5);
                component.setSize(21, 24);
                component.setPosition(137, 6 + (i * 35));
                component.setClickMask(AccessMask.CLICK_OP1);
                component.setOption(0, "Modify");
                component.setOnMouseOverListener(new Object[]{45, -2147483645, Integer.parseInt("ffffff", 16)});
                component.setOnMouseLeaveListener(new Object[]{45, -2147483645, Integer.parseInt("b3b3b3", 16)});
                add(componentId++, component);
            } else {
                //toggle
                component = new GraphicComponent(698);
                component.setParentId(5);
                component.setSize(19, 19);
                component.setPosition(137, 8 + (i * 35));
                component.setClickMask(AccessMask.CLICK_OP1);
                component.setOpBase(text);
                component.setOption(0, "<col=ff981f>Toggle</col>");
                add(componentId++, component);
            }
        }
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
