package mgi.types.component.custom;

import com.zenyte.game.util.AccessMask;
import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.GraphicComponent;
import mgi.types.component.type.LayerComponent;
import mgi.types.component.type.RectangleComponent;
import mgi.types.component.type.TextComponent;

import java.util.ArrayList;

/**
 * @author Tommeh | 27 mei 2018 | 02:11:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class GameNoticeboard extends ComponentDefinitions {
    public ArrayList<ComponentDefinitions> assemble(final int interfaceId) {
        int componentId = 0;
        setIf3(true);
        setSize(190, 261);
        setChildren(new ArrayList<>());
        ComponentDefinitions component = new TextComponent("Game Noticeboard", 496, CENTER, CENTER);
        component.setParentId(0);
        component.setPosition(27, 4);
        component.setSize(130, 18);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(0);
        component.setPosition(3, 26);
        component.setSize(184, 229);
        component.setOnLoadListener(new Object[]{712, -2147483645, 0});
        add(componentId++, component);
        component = new RectangleComponent("#170801", 166, true);
        component.setParentId(2);
        component.setPosition(0, 0);
        component.setSize(184, 229);
        add(componentId++, component);
        component = new LayerComponent(); //4
        component.setParentId(2);
        component.setPosition(0, 0);
        component.setSize(164, 229);
        component.setScrollHeight(405 + 46 + 16 + 15);
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(2);
        component.setPosition(0, 0);
        component.setSize(164, 229);
        component.setOnScrollWheelListener(new Object[]{36, interfaceId << 16 | componentId + 1,
                interfaceId << 16 | componentId, -2147483646});
        add(componentId++, component);
        component = new LayerComponent();
        component.setParentId(0);
        component.setPosition(166, 31);
        component.setSize(16, 218);
        component.setOnLoadListener(new Object[]{30, -2147483645, interfaceId << 16 | componentId - 1});
        component.setOnScrollWheelListener(new Object[]{36, interfaceId << 16 | componentId,
                interfaceId << 16 | componentId - 1});
        add(componentId++, component);
        component = new TextComponent("Server Information", 496, 1, 0);
        component.setParentId(4);
        component.setPosition(7, 9);
        component.setSize(160, 10);
        add(componentId++, component);
        final int serverNodes = 5;
        for (int i = 0; i < serverNodes; i++) {
            component = new TextComponent("component: " + (componentId + 1), 494, 1, 0);
            if (i == 0 || i == 2) {
                if (i == 0) {
                    component.setOnLoadListener(new Object[]{3504, ((701 << 16) | 8), ((701 << 16) | 42)});
                    component.setOnVarTransmitListener(new Object[]{3504, ((701 << 16) | 8), ((701 << 16) | 42)});
                    component.setVarTransmitTriggers(new int[]{3502, 3508});
                } else {
                    component.setOnVarTransmitListener(new Object[]{3502, ((701 << 16) | 8), ((701 << 16) | 9),
                            ((701 << 16) | 10)});
                    component.setVarTransmitTriggers(new int[]{3502, 3503, 3509});
                }
            } else if (i == 1) {
                //staff online
                component.setOpBase("Online staff");
                component.setOption(0, "<col=ff981f>View</col>");
                component.setClickMask(AccessMask.CLICK_OP1);
                component.setOnVarTransmitListener(new Object[]{3502, ((701 << 16) | 8), ((701 << 16) | 9),
                        ((701 << 16) | 10)});
                component.setVarTransmitTriggers(new int[]{3502, 3508});
                component.setOnMouseOverListener(new Object[]{45, -2147483645, Integer.parseInt("ffffff", 16)});
                component.setOnMouseLeaveListener(new Object[]{45, -2147483645, Integer.parseInt("ff981f", 16)});
            } else if (i == 3) {
                //p.getPacketDispatcher().sendClientScript(3501, ((701 << 16) | 11), ((701 << 16) | 15));
                component.setOnLoadListener(new Object[]{3500, ((701 << 16) | 11), ((701 << 16) | 16),
                        ((701 << 16) | 31), ((701 << 16) | 32), 1});
            }
            component.setParentId(4);
            component.setPosition(7, 25 + (i * 15));
            component.setSize(160, 10);
            add(componentId++, component);
        }
        component = new TextComponent("Player Information", 496, 1, 0);
        component.setParentId(4);
        component.setPosition(7, 39 + (serverNodes * 15));
        component.setSize(160, 10);
        add(componentId++, component);
        final int playerNodes = 13;
        for (int i = 0; i < playerNodes; i++) {
            component = new TextComponent("component: " + (componentId + 1), 494, 1, 0);
            component.setParentId(4);
            component.setPosition(7, (55 + (serverNodes * 15)) + (i * 15));
            component.setSize(160, 10);
            if (i == 0) {
                //2fa
                component.setColor("ff0000");
                component.setOpBase("2FA");
                //component.setOption(0, "<col=ff981f>Setup</col>");
                //component.setClickMask(AccessMask.CLICK_OP1);
                component.setOnVarTransmitListener(new Object[]{3503, -2147483645, 3505});
                component.setVarTransmitTriggers(new int[]{3505});
                component.setOnMouseOverListener(new Object[]{45, -2147483645, Integer.parseInt("ffffff", 16)});
                component.setOnMouseLeaveListener(new Object[]{45, -2147483645, Integer.parseInt("ff0000", 16)});
            } else if (i == 10 || i == 11 || i == 12) {
                //game settings
                final String text = i == 10 ? "Game Settings" : i == 11 ? "Drop Viewer" : "Daily Challenges";
                component.setPosition(22, (55 + (serverNodes * 15)) + (i * 15));
                component.setOption(0, "<col=ff981f>Open</col>");
                component.setClickMask(AccessMask.CLICK_OP1);
                component.setText(text);
                component.setOpBase(text);
                component.setOnMouseOverListener(new Object[]{45, -2147483645, Integer.parseInt("ffffff", 16)});
                component.setOnMouseLeaveListener(new Object[]{45, -2147483645, Integer.parseInt("ff981f", 16)});
            }
            add(componentId++, component);
        }
        component = new GraphicComponent(2505);
        component.setParentId(4);
        component.setPosition(7, 263 + 15);
        component.setSize(13, 13);
        add(componentId++, component);
        component = new GraphicComponent(1113);
        component.setParentId(4);
        component.setPosition(5, 276 + 15);
        component.setSize(18, 16);
        add(componentId++, component);
        component = new GraphicComponent(2506);
        component.setParentId(4);
        component.setPosition(7, 293 + 15);
        component.setSize(13, 13);
        add(componentId++, component);
        int yOffset = 0;
        component = new TextComponent("Boosts", 496, 1, 0);
        component.setParentId(4);
        component.setPosition(7, (69 + (serverNodes * 15)) + (playerNodes * 15));
        yOffset += 16;
        component.setSize(160, 10);
        add(componentId++, component);
        final int timerNodes = 2;
        for (int i = 0; i < timerNodes; i++) {
            component = new TextComponent("component: " + (componentId + 1), 494, 1, 0);
            component.setParentId(4);
            component.setTooltip(i == 0 ? "50% Bonus experience boost" : "25% Extra points in Chambers of Xeric");
            component.setPosition(7, 15 + (69 + (serverNodes * 15)) + (playerNodes * 15) + (i * 15));
            yOffset += 15;
            component.setSize(160, 10);
            add(componentId++, component);
        }
        yOffset += 16;
        component = new TextComponent("Useful Links", 496, 1, 0);
        component.setParentId(4);
        component.setPosition(7, yOffset + (69 + (serverNodes * 15)) + (playerNodes * 15));
        component.setSize(160, 10);
        add(componentId++, component);
        final int linksNodes = 4;
        for (int i = 0; i < linksNodes; i++) {
            final String text = i == 0 ? "Website" : i == 1 ? "Forums" : i == 2 ? "Discord" : "Store";
            component = new TextComponent(text, 494, 1, 0);
            component.setParentId(4);
            component.setPosition(24, yOffset + (85 + (serverNodes * 15)) + (playerNodes * 15) + (i * 15));
            component.setSize(160, 10);
            component.setOpBase(text);
            component.setOption(0, "<col=ff981f>Visit</col>");
            component.setClickMask(AccessMask.CLICK_OP1);
            component.setOnMouseOverListener(new Object[]{45, -2147483645, Integer.parseInt("ffffff", 16)});
            component.setOnMouseLeaveListener(new Object[]{45, -2147483645, Integer.parseInt("ff981f", 16)});
            add(componentId++, component);
            component = new GraphicComponent(2501 + i, 0);
            component.setParentId(4);
            component.setPosition(7, yOffset + (82 + (serverNodes * 15)) + (playerNodes * 15) + (i * 15));
            component.setSize(13, 13);
            add(componentId++, component);
        }
        //p.getPacketDispatcher().sendClientScript(3504, ((701 << 16) | 8), ((701 << 16) | 42));
        component = new LayerComponent();//Tooltip | Component id is used in script 3504
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
