package mgi.types.component.custom;

import com.zenyte.game.util.AccessMask;
import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.GraphicComponent;
import mgi.types.component.type.LayerComponent;
import mgi.types.component.type.TextComponent;

import java.util.ArrayList;

/**
 * @author Tommeh | 26/05/2019 | 16:19
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class TournamentPresetsManager extends ComponentDefinitions {
    public ArrayList<ComponentDefinitions> assemble(final int interfaceId) {
        int componentId = 0;
        setIf3(true);
        setSize(480, 320);
        setDynamicPosition(1, 1);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new ComponentDefinitions();
        component.setIf3(true);
        component.setParentId(0);
        component.setDynamicSize(1, 1);
        component.setDynamicPosition(1, 1);
        component.setOnLoadListener(new Object[]{10500});
        add(componentId++, component);
        component = new LayerComponent(); //presets button
        component.setHidden(true);
        component.setParentId(0);
        component.setSize(60, 20);
        component.setPosition(7, 7);
        component.setClickMask(AccessMask.CLICK_OP1);
        component.setOnLoadListener(new Object[]{10503, -2147483645});
        add(componentId++, component);
        component = new LayerComponent();
        component.setHidden(true);
        component.setParentId(0);
        component.setDynamicSize(1, 1);
        component.setDynamicPosition(1, 2);
        component.setSize(20, 50);
        component.setPosition(0, 10);
        add(componentId++, component);
        component = new LayerComponent(); //INVENTORY 4
        component.setParentId(3);
        component.setSize(160, 246);
        add(componentId++, component);
        component = new GraphicComponent(897);
        component.setParentId(4);
        component.setSpriteTiling(true);
        component.setDynamicSize(1, 1);
        component.setDynamicPosition(1, 1);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(4);
        component.setDynamicSize(1, 1);
        component.setDynamicPosition(1, 1);
        component.setOnLoadListener(new Object[]{714, -2147483645});
        add(componentId++, component);
        component = new LayerComponent(); //EQUIPMENT 7
        component.setParentId(3);
        //component.setDynamicPosition(0, 1);
        component.setSize(140, 200);
        component.setPosition(175, 40);
        add(componentId++, component);
        component = new TextComponent("preset_name", 496, CENTER, CENTER);
        component.setParentId(3);
        component.setSize(140, 36);
        component.setPosition(176, 0);
        add(componentId++, component);
        component = new GraphicComponent(172);
        component.setParentId(7);
        component.setDynamicSize(0, 1);
        component.setDynamicPosition(1, 1);
        component.setSize(36, 72);
        add(componentId++, component);
        component = new GraphicComponent(173);
        component.setParentId(7);
        component.setDynamicSize(1, 0);
        component.setDynamicPosition(1, 0);
        component.setSize(92, 36);
        component.setPosition(0, 42);
        component = new GraphicComponent(173);
        component.setParentId(7);
        component.setDynamicSize(1, 0);
        component.setDynamicPosition(1, 0);
        component.setSize(72, 36);
        component.setPosition(0, 81);
        add(componentId++, component);
        component = new GraphicComponent(172);
        component.setParentId(7);
        component.setSize(36, 46);
        component.setPosition(0, 118);
        add(componentId++, component);
        component = new GraphicComponent(172);
        component.setParentId(7);
        component.setDynamicPosition(2, 0);
        component.setSize(36, 46);
        component.setPosition(0, 118);
        add(componentId++, component);
        component = new LayerComponent(); //helm
        component.setParentId(7);
        component.setDynamicPosition(1, 0);
        component.setSize(36, 36);
        add(componentId++, component);
        component = new LayerComponent(); //cape
        component.setParentId(7);
        component.setPosition(10, 43);
        component.setSize(36, 36);
        add(componentId++, component);
        component = new LayerComponent(); //amulet
        component.setParentId(7);
        component.setDynamicPosition(1, 0);
        component.setPosition(0, 43);
        component.setSize(36, 36);
        add(componentId++, component);
        component = new LayerComponent(); //weapon
        component.setParentId(7);
        component.setPosition(0, 82);
        component.setSize(36, 36);
        add(componentId++, component);
        component = new LayerComponent(); //body
        component.setParentId(7);
        component.setDynamicPosition(1, 0);
        component.setPosition(0, 82);
        component.setSize(36, 36);
        add(componentId++, component);
        component = new LayerComponent(); //shield
        component.setParentId(7);
        component.setDynamicPosition(2, 0);
        component.setPosition(0, 82);
        component.setSize(36, 36);
        add(componentId++, component);
        component = new LayerComponent(); //legs
        component.setParentId(7);
        component.setDynamicPosition(1, 0);
        component.setPosition(0, 122);
        component.setSize(36, 36);
        add(componentId++, component);
        component = new LayerComponent(); //hands
        component.setParentId(7);
        component.setDynamicPosition(0, 2);
        component.setSize(36, 36);
        add(componentId++, component);
        component = new LayerComponent(); //feet
        component.setParentId(7);
        component.setDynamicPosition(1, 2);
        component.setSize(36, 36);
        add(componentId++, component);
        component = new LayerComponent(); //ring
        component.setParentId(7);
        component.setDynamicPosition(2, 2);
        component.setSize(36, 36);
        add(componentId++, component);
        component = new LayerComponent(); //ammo
        component.setParentId(7);
        component.setDynamicPosition(2, 0);
        component.setPosition(10, 43);
        component.setSize(36, 36);
        add(componentId++, component);
        component = new LayerComponent(); //SKILLS 24
        component.setParentId(3);
        component.setSize(125, 128);
        component.setPosition(332, 0);
        add(componentId++, component);
        //2
        component = new LayerComponent();
        component.setParentId(24);
        component.setSize(62, 32);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(24);
        component.setSize(62, 32);
        component.setPosition(63, 0);
        add(componentId++, component);
        //4
        component = new LayerComponent();
        component.setParentId(24);
        component.setSize(62, 32);
        component.setPosition(0, 32);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(24);
        component.setSize(62, 32);
        component.setPosition(63, 32);
        add(componentId++, component);
        //6
        component = new LayerComponent();
        component.setParentId(24);
        component.setSize(62, 32);
        component.setPosition(0, 64);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(24);
        component.setSize(62, 32);
        component.setPosition(63, 64);
        add(componentId++, component);
        //7
        component = new LayerComponent();
        component.setParentId(24);
        component.setSize(62, 32);
        component.setPosition(0, 96);
        add(componentId++, component);
        component = new LayerComponent(); //32
        component.setParentId(3);
        component.setSize(125, 40);
        component.setPosition(332, 136);
        component.setOnLoadListener(new Object[]{714, -2147483645});
        add(componentId++, component);
        component = new GraphicComponent(897);
        component.setParentId(32);
        component.setSpriteTiling(true);
        component.setDynamicSize(1, 1);
        component.setDynamicPosition(1, 1);
        add(componentId++, component);
        component = new GraphicComponent(780);
        component.setParentId(32);
        component.setSize(33, 36);
        component.setPosition(5, 2);
        add(componentId++, component);
        component = new TextComponent("Normals", 496, CENTER, CENTER);
        component.setParentId(32);
        component.setSize(86, 28);
        component.setPosition(38, 6);
        add(componentId++, component);
        component = new LayerComponent(); //new page 36
        component.setHidden(false);
        component.setParentId(0);
        component.setDynamicSize(1, 1);
        component.setDynamicPosition(1, 2);
        component.setSize(20, 50);
        component.setPosition(0, 10);
        add(componentId++, component);
        component = new LayerComponent(); // presets list 36
        component.setParentId(36);
        component.setSize(120, 180);
        component.setOnLoadListener(new Object[]{714, -2147483645});
        add(componentId++, component);
        component = new GraphicComponent(897);
        component.setParentId(37);
        component.setSpriteTiling(true);
        component.setDynamicSize(1, 1);
        component.setDynamicPosition(1, 1);
        add(componentId++, component);
        component = new LayerComponent(); //scrolllayer 38
        component.setParentId(37);
        component.setDynamicPosition(0, 1);
        component.setDynamicSize(1, 1);
        component.setSize(20, 4);
        component.setPosition(2, 0);
        add(componentId++, component);
        component = new LayerComponent(); //scrollbar
        component.setParentId(37);
        component.setDynamicPosition(2, 1);
        component.setDynamicSize(0, 1);
        component.setSize(16, 4);
        component.setPosition(2, 0);
        add(componentId++, component);
        component = new LayerComponent(); // items 41
        component.setParentId(36);
        component.setDynamicSize(0, 1);
        component.setSize(335, 40);
        component.setPosition(125, 0);
        component.setOnLoadListener(new Object[]{714, -2147483645});
        add(componentId++, component);
        component = new GraphicComponent(897); //38
        component.setParentId(41);
        component.setSpriteTiling(true);
        component.setDynamicSize(1, 1);
        component.setDynamicPosition(1, 1);
        add(componentId++, component);
        component = new LayerComponent(); //39
        component.setParentId(41);
        component.setDynamicPosition(0, 1);
        component.setDynamicSize(1, 1);
        component.setSize(20, 4);
        component.setPosition(2, 0);
        add(componentId++, component);
        component = new LayerComponent(); //40
        component.setParentId(41);
        component.setDynamicPosition(2, 1);
        component.setDynamicSize(0, 1);
        component.setSize(16, 4);
        component.setPosition(2, 0);
        add(componentId++, component);
        component = new LayerComponent(); //search button
        component.setParentId(36);
        component.setDynamicPosition(1, 2);
        component.setSize(150, 36);
        component.setPosition(58, 0);
        add(componentId++, component);
        component = new LayerComponent(); //view
        component.setParentId(36);
        component.setSize(58, 25);
        component.setPosition(0, 185);
        add(componentId++, component);
        component = new LayerComponent(); //add
        component.setParentId(36);
        component.setSize(58, 25);
        component.setPosition(62, 185);
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
