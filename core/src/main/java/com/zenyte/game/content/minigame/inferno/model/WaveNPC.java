package com.zenyte.game.content.minigame.inferno.model;

import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.content.minigame.inferno.npc.impl.*;
import com.zenyte.game.content.minigame.inferno.npc.impl.zuk.TzKalZuk;

/**
 * @author Tommeh | 29/11/2019 | 20:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public enum WaveNPC {
    JAL_NIB(JalNib.class, 7691),
    JAL_MEJRAH(JalMejRah.class, 7692),
    JAL_AK(JalAk.class, 7693),
    JAL_AKREK_MEJ(JalAkRekMej.class, 7694),
    JAL_AKREK_XIL(JalAkRekXil.class, 7695),
    JAL_AKREK_KET(JalAkRekKet.class, 7696),
    JAL_IMKOT(JalImKot.class, 7697),
    JAL_XIL(JalXil.class, 7698),
    JAL_ZEK(JalZek.class, 7699),
    JALTOK_JAD(JalTokJad.class, 7700),
    TZKAL_ZUK(TzKalZuk.class, 7706);

    private final Class<? extends InfernoNPC> clazz;
    private final int baseNPC;

    WaveNPC(Class<? extends InfernoNPC> clazz, int baseNPC) {
        this.clazz = clazz;
        this.baseNPC = baseNPC;
    }

    public Class<? extends InfernoNPC> getClazz() {
        return clazz;
    }

    public int getBaseNPC() {
        return baseNPC;
    }
}
