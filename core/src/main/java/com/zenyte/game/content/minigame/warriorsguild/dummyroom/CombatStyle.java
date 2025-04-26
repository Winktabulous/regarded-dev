package com.zenyte.game.content.minigame.warriorsguild.dummyroom;

import static com.zenyte.game.content.minigame.warriorsguild.dummyroom.DummyStyle.*;

/**
 * @author Kris | 16. dets 2017 : 15:50.11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
enum CombatStyle {

	VARBIT_0(new StyleDefinition(ACCURATE, CRUSH), new StyleDefinition(AGGRESSIVE, CRUSH), new StyleDefinition(DEFENSIVE, CRUSH)),
	VARBIT_1(new StyleDefinition(ACCURATE, SLASH), new StyleDefinition(AGGRESSIVE, SLASH), new StyleDefinition(AGGRESSIVE, CRUSH), new StyleDefinition(DEFENSIVE, SLASH)),
	VARBIT_2(new StyleDefinition(ACCURATE, CRUSH), new StyleDefinition(AGGRESSIVE, CRUSH), new StyleDefinition(DEFENSIVE, CRUSH)),
	VARBIT_3(),
	VARBIT_4(new StyleDefinition(ACCURATE, SLASH), new StyleDefinition(AGGRESSIVE, SLASH), new StyleDefinition(CONTROLLED, STAB), new StyleDefinition(DEFENSIVE, SLASH)),
	VARBIT_5(),
	VARBIT_6(new StyleDefinition(AGGRESSIVE, SLASH)),
	VARBIT_7(),
	VARBIT_8(null, new StyleDefinition(AGGRESSIVE, CRUSH)),
	VARBIT_9(new StyleDefinition(ACCURATE, SLASH), new StyleDefinition(AGGRESSIVE, SLASH), new StyleDefinition(CONTROLLED, STAB), new StyleDefinition(DEFENSIVE, SLASH)),
	VARBIT_10(new StyleDefinition(ACCURATE, SLASH), new StyleDefinition(AGGRESSIVE, SLASH), new StyleDefinition(AGGRESSIVE, CRUSH), new StyleDefinition(DEFENSIVE, SLASH)),
	VARBIT_11(new StyleDefinition(ACCURATE, STAB), new StyleDefinition(AGGRESSIVE, STAB), new StyleDefinition(AGGRESSIVE, CRUSH), new StyleDefinition(DEFENSIVE, STAB)),
	VARBIT_12(new StyleDefinition(CONTROLLED, STAB), new StyleDefinition(AGGRESSIVE, SLASH), new StyleDefinition(DEFENSIVE, STAB)),
	VARBIT_13(new StyleDefinition(ACCURATE, CRUSH), new StyleDefinition(AGGRESSIVE, CRUSH), new StyleDefinition(DEFENSIVE, CRUSH)),
	VARBIT_14(new StyleDefinition(ACCURATE, SLASH), new StyleDefinition(AGGRESSIVE, STAB), new StyleDefinition(AGGRESSIVE, CRUSH), new StyleDefinition(DEFENSIVE, SLASH)),
	VARBIT_15(new StyleDefinition(CONTROLLED, STAB), new StyleDefinition(CONTROLLED, SLASH), new StyleDefinition(CONTROLLED, CRUSH), new StyleDefinition(DEFENSIVE, STAB)),
	VARBIT_16(new StyleDefinition(ACCURATE, CRUSH), new StyleDefinition(AGGRESSIVE, CRUSH), new StyleDefinition(CONTROLLED, STAB), new StyleDefinition(DEFENSIVE, CRUSH)),
	VARBIT_17(new StyleDefinition(ACCURATE, STAB), new StyleDefinition(AGGRESSIVE, STAB), new StyleDefinition(AGGRESSIVE, SLASH), new StyleDefinition(DEFENSIVE, STAB)),
	VARBIT_18(new StyleDefinition(ACCURATE, CRUSH), new StyleDefinition(AGGRESSIVE, CRUSH), new StyleDefinition(DEFENSIVE, CRUSH)),
	VARBIT_19(),
	VARBIT_20(new StyleDefinition(ACCURATE, SLASH), new StyleDefinition(CONTROLLED, SLASH), new StyleDefinition(DEFENSIVE, SLASH)),
    VARBIT_21(new StyleDefinition(ACCURATE, STAB), new StyleDefinition(AGGRESSIVE, SLASH), new StyleDefinition(DEFENSIVE, CRUSH)),
    VARBIT_22(new StyleDefinition(ACCURATE, SLASH), new StyleDefinition(AGGRESSIVE, SLASH), new StyleDefinition(AGGRESSIVE, CRUSH), new StyleDefinition(DEFENSIVE, SLASH)),
    VARBIT_23(),
    VARBIT_24(new StyleDefinition(ACCURATE, STAB), new StyleDefinition(AGGRESSIVE, SLASH), new StyleDefinition(CONTROLLED, CRUSH), new StyleDefinition(DEFENSIVE, STAB)),
    VARBIT_25(new StyleDefinition(CONTROLLED, STAB), new StyleDefinition(AGGRESSIVE, SLASH), new StyleDefinition(DEFENSIVE, STAB)),
    VARBIT_26(new StyleDefinition(AGGRESSIVE, CRUSH), new StyleDefinition(AGGRESSIVE, CRUSH), new StyleDefinition(AGGRESSIVE, CRUSH)),
    VARBIT_27(new StyleDefinition(ACCURATE, CRUSH));

    public static final CombatStyle[] VALUES = values();

    private final StyleDefinition[] styles;

    CombatStyle(final StyleDefinition... styles) {
        this.styles = styles;
    }

    public StyleDefinition[] getStyles() {
        return styles;
    }

}
