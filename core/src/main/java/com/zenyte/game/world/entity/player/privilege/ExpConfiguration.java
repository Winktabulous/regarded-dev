package com.zenyte.game.world.entity.player.privilege;

public class ExpConfiguration {
        private int skillingExpModifier;
        private int combatModifier;
        private int dropRateIncrease;

        public int getSkillingExpModifier() {
            return skillingExpModifier;
        }

        public int getCombatModifier() {
            return combatModifier;
        }

        public int getDropRateIncrease() {
            return dropRateIncrease;
        }

        public ExpConfiguration(int combatModifier, int skillingExpModifier, int dropRateIncrease) {
            this.skillingExpModifier = skillingExpModifier;
            this.combatModifier = combatModifier;
            this.dropRateIncrease = dropRateIncrease;
        }

        public boolean matches(int combatModifier, int expModifier) {
            return this.combatModifier == combatModifier && this.skillingExpModifier == expModifier;
        }

        public String getString() {
            return combatModifier+"x Combat, "+skillingExpModifier+"x Skilling & "+dropRateIncrease+"% Drop Rate";
        }

    @Override
    public String toString() {
        return "ExpConfiguration{" +
                "skillingExpModifier=" + skillingExpModifier +
                ", combatModifier=" + combatModifier +
                ", dropRateIncrease=" + dropRateIncrease +
                '}';
    }
}