package com.zenyte.game.content.area.prifddinas.zalcano.formation;


import java.util.Optional;

public interface ZalcanoRockFormationHandler {

    /**
     * A set amount of formations
     *
     * @return - here is a set amount of formations
     */
    ZalcanoRockFormation[] formations();

    /**
     * The last active formation
     * @return -
     */
    int lastActiveFormation();

    Optional<ZalcanoRockFormation> getActiveFormation();

    void deactivateLast();

    void deactivate(int index);

    void activateFormation(int index);

    void damagePlayers();

    void switchActivateFormation();

    void depleteAllFormations();

    void deactivateAllFormations();


}
