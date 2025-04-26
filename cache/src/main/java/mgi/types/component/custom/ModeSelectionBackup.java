package mgi.types.component.custom;

import com.zenyte.game.util.AccessMask;
import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.*;

import java.util.ArrayList;

/**
 * @author Tommeh | 26-1-2019 | 13:08
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class ModeSelectionBackup extends ComponentDefinitions {
    public ArrayList<ComponentDefinitions> assemble() {
        int componentId = 0;
        setIf3(true);
        setSize(24, 300);
        setDynamicSize(1, 0);
        setPosition(0, 20);
        setDynamicPosition(1, 0);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new ComponentDefinitions(); //1
        component.setIf3(true);
        component.setParentId(0);
        component.setDynamicSize(1, 1);
        component.setPosition(0, 0);
        component.setDynamicPosition(1, 1);
        component.setOnLoadListener(new Object[]{227, -2147483645, "Mode Selection"});
        add(componentId++, component);
        component = new GraphicComponent(535);
        component.setPosition(470, 26);
        component.setSize(27, 22);
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOption(0, "Close");
        component.setOnMouseOverListener(new Object[]{44, -2147483645, 536});
        component.setOnMouseLeaveListener(new Object[]{44, -2147483645, 535});
        add(componentId++, component);
        component = new LayerComponent(); // 3
        component.setPosition(50, 73);
        component.setSize(150, 22);
        add(componentId++, component);
        component = new RectangleComponent("000000", false);
        component.setParentId(3);
        component.setSize(150, 22);
        add(componentId++, component);
        component = new RectangleComponent("ff981f", 150, false);
        component.setParentId(3);
        component.setPosition(1, 1);
        component.setSize(148, 20);
        add(componentId++, component);
        component = new TextComponent("Game Mode", FONT_BOLD, CENTER, CENTER);
        component.setParentId(3);
        component.setSize(150, 24);
        add(componentId++, component);
        component = new LayerComponent(); // 7
        component.setPosition(50, 100);
        component.setSize(150, 184);
        add(componentId++, component);
        component = new RectangleComponent("000000", false);
        component.setParentId(7);
        component.setSize(150, 184);
        add(componentId++, component);
        component = new RectangleComponent("ff981f", 200, false);
        component.setParentId(7);
        component.setPosition(1, 1);
        component.setSize(148, 182);
        add(componentId++, component);
        final String[] gameModes = new String[]{"Regular", "Standard Iron Man", "Hardcore Iron Man", "Ultimate Iron " +
                "Man"};
        for (int i = 0; i < 4; i++) {
            component = new RectangleComponent((i + 1) % 2 == 0 ? "#574E43" : "#4E453A", true);
            component.setParentId(7);
            component.setPosition(2, 2 + (i * 45));
            component.setSize(146, 45);
            add(componentId++, component);
            component = new TextComponent(gameModes[i], FONT_BOLD, CENTER, CENTER);
            component.setParentId(7);
            component.setPosition(2, 2 + (i * 45));
            component.setSize(109, 50);
            add(componentId++, component);
            component = new GraphicComponent(697);
            component.setParentId(7);
            component.setSize(19, 19);
            component.setPosition(115, 15 + (i * 45));
            component.setClickMask(AccessMask.CLICK_OP1);
            component.setOption(0, "<col=ff981f>Choose</col>");
            component.setOpBase(gameModes[i]);
            add(componentId++, component);
        }
        component = new LayerComponent(); // 22
        component.setPosition(230, 73);
        component.setSize(150, 22);
        add(componentId++, component);
        component = new RectangleComponent("000000", false);
        component.setParentId(22);
        component.setSize(150, 22);
        add(componentId++, component);
        component = new RectangleComponent("ff981f", 150, false);
        component.setParentId(22);
        component.setPosition(1, 1);
        component.setSize(148, 20);
        add(componentId++, component);
        component = new TextComponent("XP Mode", FONT_BOLD, CENTER, CENTER); //11
        component.setParentId(22);
        component.setSize(150, 24);
        add(componentId++, component);
        component = new LayerComponent(); // 26
        component.setPosition(230, 100);
        component.setSize(150, 94);
        add(componentId++, component);
        component = new RectangleComponent("000000", false);
        component.setParentId(26);
        component.setSize(150, 94);
        add(componentId++, component);
        component = new RectangleComponent("ff981f", 200, false);
        component.setParentId(26);
        component.setPosition(1, 1);
        component.setSize(148, 92);
        add(componentId++, component);
        final String[] xpModes = new String[]{"1x rate", "2x rate"};
        for (int i = 0; i < 2; i++) {
            component = new RectangleComponent((i + 1) % 2 == 0 ? "#574E43" : "#4E453A", true);
            component.setParentId(26);
            component.setPosition(2, 2 + (i * 45));
            component.setSize(146, 45);
            add(componentId++, component);
            component = new TextComponent(xpModes[i], FONT_BOLD, CENTER, CENTER);
            component.setParentId(26);
            component.setPosition(2, 2 + (i * 45));
            component.setSize(109, 50);
            add(componentId++, component);
            component = new GraphicComponent(697);
            component.setParentId(26);
            component.setSize(19, 19);
            component.setPosition(115, 15 + (i * 45));
            component.setClickMask(AccessMask.CLICK_OP1);
            component.setOption(0, "<col=ff981f>Choose</col>");
            component.setOpBase(xpModes[i]);
            add(componentId++, component);
        }
        component = new ModelComponent(11039, 500); //helm
        component.setRotationX(534);
        component.setRotationZ(92);
        component.setPosition(450, 191);
        component.setSize(32, 32);
        add(componentId++, component);
        component = new ModelComponent(11041, 600); //body
        component.setRotationX(514);
        component.setRotationZ(1983);
        component.setPosition(426, 122);
        component.setSize(32, 32);
        add(componentId++, component);
        component = new ModelComponent(11044, 600); //legs
        component.setRotationX(367);
        component.setRotationY(1964);
        component.setPosition(409, 241);
        component.setSize(32, 32);
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
