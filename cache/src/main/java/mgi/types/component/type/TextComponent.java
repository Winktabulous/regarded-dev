package mgi.types.component.type;

import mgi.types.component.ComponentDefinitions;

/**
 * @author Tommeh | 26 mei 2018 | 12:37:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class TextComponent extends ComponentDefinitions {

    public static final int LEFT = 0,
            CENTER = 1,
            RIGHT = 2;
    public static final int FONT_SMALL = 494,
            FONT_REGULAR = 495,
            FONT_BOLD = 496,
            FONT_LARGE_STYLE = 497;

    public TextComponent(final String text, final String color, final int font, final int verticalAlignment,
                         final int horizontalAlignment) {
        super();
        this.type = 4;
        this.isIf3 = true;
        this.textShadowed = true;
        this.text = text;
        this.font = font;
        this.yAllignment = verticalAlignment;
        this.xAllignment = horizontalAlignment;
        setColor(color);
    }

    public TextComponent(final String text, final int font, final int verticalAlignment,
                         final int horizontalAlignment) {
        this(text, "ff981f", font, verticalAlignment, horizontalAlignment);
    }

    public TextComponent(final String text, final int font) {
        this(text, font, 0, 0);
    }

}
