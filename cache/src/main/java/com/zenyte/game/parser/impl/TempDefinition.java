package com.zenyte.game.parser.impl;

public class TempDefinition {

    private int id;
    private int attackAnimation = -1;
    private int blockAnimation = -1;
    private int deathAnimation = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAttackAnimation() {
        return attackAnimation;
    }

    public void setAttackAnimation(int attackAnimation) {
        this.attackAnimation = attackAnimation;
    }

    public int getBlockAnimation() {
        return blockAnimation;
    }

    public void setBlockAnimation(int blockAnimation) {
        this.blockAnimation = blockAnimation;
    }

    public int getDeathAnimation() {
        return deathAnimation;
    }

    public void setDeathAnimation(int deathAnimation) {
        this.deathAnimation = deathAnimation;
    }

}
