package mgi.types.component.custom;

import com.zenyte.ContentConstants;
import com.zenyte.game.util.AccessMask;
import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.GraphicComponent;
import mgi.types.component.type.LayerComponent;
import mgi.types.component.type.RectangleComponent;
import mgi.types.component.type.TextComponent;

import java.util.ArrayList;

/**
 * @author Tommeh | 26-1-2019 | 13:08
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class ExperienceModeSelection extends ComponentDefinitions {
    public ArrayList<ComponentDefinitions> assemble() {
        int componentId = 0;
        setIf3(true);
        setSize(300, 300);
        setPosition(0, 20);
        setDynamicPosition(1, 0);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new ComponentDefinitions(); //1
        component.setIf3(true);
        component.setParentId(0);
        component.setSize(300, 280);
        component.setPosition(0, 0);
        component.setDynamicPosition(1, 1);
        component.setOnLoadListener(new Object[]{227, -2147483645, "Experience Mode"});
        add(componentId++, component);
        component = new TextComponent("Select an experience mode", FONT_BOLD, CENTER, CENTER);
        component.setPosition(128, 62);
        component.setSize(184, 30);
        add(componentId++, component);
        component = new LayerComponent(); //3
        component.setPosition(144, 92);
        component.setSize(150, 158);
        component.setOnLoadListener(new Object[]{712, -2147483645, 0});
        add(componentId++, component);
        component = new RectangleComponent("#170801", 166, true);
        component.setParentId(3);
        component.setDynamicSize(1, 1);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(3);
        component.setDynamicPosition(0, 1);
        component.setDynamicSize(1, 1);
        component.setPosition(2, 0);
        component.setSize(5, 4);
        add(componentId++, component);
        final String[] xpModes = new String[]{"50x Combat<br>25x Skilling", "10x Combat<br>10x Skilling", "5x Combat" +
                "<br>5x Skilling"};
        final String[] ops = new String[]{"50x Combat & 25x Skilling", "10x Combat & 10x Skilling", "5x Combat & 5x " +
                "Skilling"};
        for (int i = 0; i < xpModes.length; i++) {
            component = new GraphicComponent((i + 1) % 2 == 0 ? 897 : 297);
            component.setParentId(5);
            component.setSpriteTiling(true);
            component.setDynamicSize(1, 0);
            component.setDynamicPosition(1, 0);
            component.setPosition(0, i * 50);
            component.setSize(6, 50);
            component.setClickMask(AccessMask.CLICK_OP1);
            component.setOpBase("<col=ff981f>" + ops[i] + "</col>");
            component.setOption(0, "Select");
            component.setOnMouseOverListener(new Object[]{44, -2147483645, 1040});
            component.setOnMouseLeaveListener(new Object[]{44, -2147483645, (i + 1) % 2 == 0 ? 897 : 297});
            add(componentId++, component);
            component = new TextComponent(xpModes[i], 495, CENTER, CENTER);
            component.setParentId(5);
            component.setDynamicSize(1, 0);
            component.setDynamicPosition(1, 0);
            component.setPosition(0, i == 0 ? 3 : i == 1 ? 50 : 100);
            component.setSize(0, 50);
            component.setLineHeight(18);
            add(componentId++, component);
        }
        component = new TextComponent("If you choose choose the 5x or 10x combat & skilling mode, you can always talk" +
                " to the " + ContentConstants.SERVER_NAME + " guide in Edgeville to change it to the easier 50x combat & 25x skilling one.",
                "ffffff", 494, CENTER, CENTER);
        component.setPosition(120, 250);
        component.setSize(270, 50);
        add(componentId++, component);
        component = new GraphicComponent(222);
        component.setPosition(300, 108);
        component.setSize(92, 106);
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
