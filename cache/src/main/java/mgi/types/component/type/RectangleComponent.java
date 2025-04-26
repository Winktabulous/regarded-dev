package mgi.types.component.type;

import mgi.types.component.ComponentDefinitions;

/**
 * @author Tommeh | 2 jun. 2018 | 15:12:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class RectangleComponent extends ComponentDefinitions {

    public RectangleComponent(final String color, final int opacity, final boolean filled) {
        super();
        this.type = 3;
        this.isIf3 = true;
        this.opacity = opacity;
        this.filled = filled;
        setColor(color);
    }

    public RectangleComponent(final String color, final boolean filled) {
        this(color, 0, filled);
    }

    public RectangleComponent(final String color) {
        this(color, 0, true);
    }

    public RectangleComponent() {
        this("", 0, true);
    }

}
