package mgi.types.component.custom;

import mgi.types.component.ComponentDefinitions;
import mgi.types.component.type.GraphicComponent;
import mgi.types.component.type.LayerComponent;
import mgi.types.component.type.RectangleComponent;
import mgi.types.component.type.TextComponent;

import java.util.ArrayList;

/**
 * @author Tommeh | 15 jul. 2018 | 15:08:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class TeleportMenu extends ComponentDefinitions {
	public ArrayList<ComponentDefinitions> assemble() {
		int componentId = 0;
		setIf3(true);
		setSize(24, 300);
		setDynamicSize(1, 0);
		setPosition(0, 20);
		setDynamicPosition(1, 0);
		setChildren(new ArrayList<>());
		ComponentDefinitions component = new ComponentDefinitions();
		component.setIf3(true);
		component.setDynamicSize(1, 1);
		component.setPosition(0, 0);
		component.setDynamicPosition(1, 1);
		component.setParentId(0);
		component.setOnLoadListener(new Object[] {10000});
		add(componentId++, component);
		/** Categories **/
		component = new RectangleComponent("#000000", 100, false);
		component.setPosition(9, 62);
		component.setParentId(0);
		component.setSize(120, 229);
		add(componentId++, component);
		component = new RectangleComponent("#4E453A", 0, true); //3
		component.setParentId(0);
		component.setPosition(11, 64);
		component.setSize(117, 225);
		add(componentId++, component);
		// content layer
		component = new LayerComponent(); //4
		component.setParentId(0);
		component.setPosition(11, 64);
		component.setSize(117, 225);
		add(componentId++, component);
		// scrollbar
		component = new LayerComponent();
		component.setParentId(0);
		component.setPosition(111, 64);
		component.setSize(16, 225);
		add(componentId++, component);
		component = new RectangleComponent("#ff981f", 200, false);
		component.setPosition(10, 63);
		component.setParentId(0);
		component.setSize(118, 227);
		add(componentId++, component);
		/** Teleports **/
		component = new RectangleComponent("#000000", 100, false);
		component.setPosition(132, 62);
		component.setParentId(0);
		component.setSize(224, 229);
		add(componentId++, component);
		component = new RectangleComponent("#ff981f", 200, false);
		component.setPosition(133, 63);
		component.setParentId(0);
		component.setSize(222, 227);
		add(componentId++, component);
		component = new RectangleComponent("#4E453A", 0, true);
		component.setParentId(0);
		component.setPosition(134, 64);
		component.setSize(203, 225);
		add(componentId++, component);
		component = new RectangleComponent("#ff981f", 225, false);
		component.setPosition(133, 63);
		component.setParentId(0);
		component.setSize(205, 227);
		add(componentId++, component);
		component = new LayerComponent(); //10
		component.setPosition(134, 64);
		component.setSize(203, 225);
		component.setParentId(0);
		add(componentId++, component);
		component = new LayerComponent(); //14
		component.setParentId(11);
		component.setPosition(134, 64);
		component.setSize(203, 225);
		add(componentId++, component);
		component = new LayerComponent();
		component.setParentId(0);
		component.setPosition(338, 64);
		component.setSize(16, 225);
		add(componentId++, component);
		/** Favorites **/
		component = new RectangleComponent("#000000", 100, false);
		component.setPosition(359, 62);
		component.setParentId(0);
		component.setSize(121, 229);
		add(componentId++, component);
		component = new RectangleComponent("#ff981f", 200, false);
		component.setPosition(360, 63);
		component.setParentId(0);
		component.setSize(119, 227);
		add(componentId++, component);
		component = new RectangleComponent("#4E453A", 0, true);
		component.setParentId(0);
		component.setPosition(361, 64);
		component.setSize(117, 225);
		add(componentId++, component);
		// main layer
		component = new LayerComponent();
		component.setParentId(0);
		component.setPosition(361, 64);
		component.setSize(117, 225);
		add(componentId++, component);
		// scrollbar
		component = new LayerComponent();
		component.setParentId(0);
		component.setPosition(361, 64);
		component.setSize(16, 225);
		add(componentId++, component);
		/**
		 * Categories
		 */
		component = new RectangleComponent("#000000", 100, false);
		component.setPosition(9, 37);
		component.setParentId(0);
		component.setSize(120, 22);
		add(componentId++, component);
		component = new RectangleComponent("#ff981f", 200, false);
		component.setPosition(10, 38);
		component.setParentId(0);
		component.setSize(118, 20);
		add(componentId++, component);
		component = new RectangleComponent("#4E453A", 0, true);
		component.setParentId(0);
		component.setPosition(11, 39);
		component.setSize(116, 18);
		add(componentId++, component);
		/**
		 * Teleports
		 */
		component = new RectangleComponent("#000000", 100, false);
		component.setPosition(132, 37);
		component.setParentId(0);
		component.setSize(224, 22);
		add(componentId++, component);
		component = new RectangleComponent("#ff981f", 200, false);
		component.setPosition(133, 38);
		component.setParentId(0);
		component.setSize(222, 20);
		add(componentId++, component);
		component = new RectangleComponent("#4E453A", 0, true);
		component.setParentId(0);
		component.setPosition(134, 39);
		component.setSize(219, 18);
		add(componentId++, component);
		/**
		 * Favourites
		 */
		component = new RectangleComponent("#000000", 100, false);
		component.setPosition(359, 37);
		component.setParentId(0);
		component.setSize(121, 22);
		add(componentId++, component);
		component = new RectangleComponent("#ff981f", 200, false);
		component.setPosition(360, 38);
		component.setParentId(0);
		component.setSize(119, 20);
		add(componentId++, component);
		component = new RectangleComponent("#4E453A", 0, true);
		component.setParentId(0);
		component.setPosition(361, 39);
		component.setSize(117, 18);
		add(componentId++, component);
		component = new GraphicComponent(1131);
		component.setPosition(379, 59);
		component.setSize(16, 16);
		add(componentId++, component);
		component = new TextComponent("Categories", FONT_BOLD, CENTER, CENTER);
		component.setParentId(0);
		component.setPosition(9, 37);
		component.setSize(120, 22);
		add(componentId++, component);
		component = new TextComponent("Teleports", FONT_BOLD, CENTER, CENTER);
		component.setParentId(0);
		component.setPosition(132, 37);
		component.setSize(224, 22);
		add(componentId++, component);
		component = new TextComponent("Favorites", FONT_BOLD, CENTER, CENTER);
		component.setParentId(0);
		component.setPosition(359, 37);
		component.setSize(121, 22);
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
