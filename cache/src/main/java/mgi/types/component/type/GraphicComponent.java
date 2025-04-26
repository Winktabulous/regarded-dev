package mgi.types.component.type;

import mgi.types.component.ComponentDefinitions;

/**
 * @author Tommeh | 2 jun. 2018 | 15:08:23
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class GraphicComponent extends ComponentDefinitions {

    public GraphicComponent(final int spriteId, final int opacity) {
        super();
        this.type = 5;
        this.isIf3 = true;
        this.spriteId = spriteId;
        this.opacity = opacity;
    }

    public GraphicComponent(final int spriteId) {
        this(spriteId, 0);
    }

}
