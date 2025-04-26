package com.zenyte.game.content.chambersofxeric.greatolm;

/**
 * @author Kris | 14. jaan 2018 : 1:46.35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * A base interface for any combat script that the Olm can use.
 */
public interface OlmCombatScript {

    /**
     * Executes the body of the script.
     *
     * @param olm the Olm boss who is executing the script.
     */
    void handle(final GreatOlm olm);

}
