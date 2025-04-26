package mgi.types.component.type;

import mgi.types.component.ComponentDefinitions;

/**
 * @author Tommeh | 26 jul. 2018 | 21:36:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class LineComponent extends ComponentDefinitions {

    public LineComponent(final String color, final int lineWidth) {
        super();
        this.type = 9;
        this.isIf3 = true;
        this.lineWidth = lineWidth;
        setColor(color);
    }

}
