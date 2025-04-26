package mgi.types.component.type;

import mgi.types.component.ComponentDefinitions;

/**
 * @author Tommeh | 26-1-2019 | 15:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class ModelComponent extends ComponentDefinitions {

    public ModelComponent(final int modelId, final int zoom) {
        super();
        this.type = 6;
        this.isIf3 = true;
        this.modelId = modelId;
        this.modelZoom = zoom;
    }
    
}
