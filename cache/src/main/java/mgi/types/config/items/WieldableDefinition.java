package mgi.types.config.items;

import com.zenyte.game.world.entity.masks.RenderAnimation;

public final class WieldableDefinition {

    private boolean twoHanded;
    private int blockAnimation;
    private int standAnimation = RenderAnimation.STAND;
    private int walkAnimation = RenderAnimation.WALK;
    private int runAnimation = RenderAnimation.RUN;
    private int standTurnAnimation = RenderAnimation.STAND_TURN;
    private int rotate90Animation = RenderAnimation.ROTATE90;
    private int rotate180Animation = RenderAnimation.ROTATE180;
    private int rotate270Animation = RenderAnimation.ROTATE270;
    private int accurateAnimation;
    private int aggressiveAnimation;
    private int controlledAnimation;
    private int defensiveAnimation;
    private int attackSpeed;
    private int interfaceVarbit;
    private int normalAttackDistance;
    private int longAttackDistance;

    public boolean isTwoHanded() {
        return twoHanded;
    }

    public void setTwoHanded(boolean twoHanded) {
        this.twoHanded = twoHanded;
    }

    public int getBlockAnimation() {
        return blockAnimation;
    }

    public void setBlockAnimation(int blockAnimation) {
        this.blockAnimation = blockAnimation;
    }

    public int getStandAnimation() {
        return standAnimation;
    }

    public void setStandAnimation(int standAnimation) {
        this.standAnimation = standAnimation;
    }

    public int getWalkAnimation() {
        return walkAnimation;
    }

    public void setWalkAnimation(int walkAnimation) {
        this.walkAnimation = walkAnimation;
    }

    public int getRunAnimation() {
        return runAnimation;
    }

    public void setRunAnimation(int runAnimation) {
        this.runAnimation = runAnimation;
    }

    public int getStandTurnAnimation() {
        return standTurnAnimation;
    }

    public void setStandTurnAnimation(int standTurnAnimation) {
        this.standTurnAnimation = standTurnAnimation;
    }

    public int getRotate90Animation() {
        return rotate90Animation;
    }

    public void setRotate90Animation(int rotate90Animation) {
        this.rotate90Animation = rotate90Animation;
    }

    public int getRotate180Animation() {
        return rotate180Animation;
    }

    public void setRotate180Animation(int rotate180Animation) {
        this.rotate180Animation = rotate180Animation;
    }

    public int getRotate270Animation() {
        return rotate270Animation;
    }

    public void setRotate270Animation(int rotate270Animation) {
        this.rotate270Animation = rotate270Animation;
    }

    public int getAccurateAnimation() {
        return accurateAnimation;
    }

    public void setAccurateAnimation(int accurateAnimation) {
        this.accurateAnimation = accurateAnimation;
    }

    public int getAggressiveAnimation() {
        return aggressiveAnimation;
    }

    public void setAggressiveAnimation(int aggressiveAnimation) {
        this.aggressiveAnimation = aggressiveAnimation;
    }

    public int getControlledAnimation() {
        return controlledAnimation;
    }

    public void setControlledAnimation(int controlledAnimation) {
        this.controlledAnimation = controlledAnimation;
    }

    public int getDefensiveAnimation() {
        return defensiveAnimation;
    }

    public void setDefensiveAnimation(int defensiveAnimation) {
        this.defensiveAnimation = defensiveAnimation;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public int getInterfaceVarbit() {
        return interfaceVarbit;
    }

    public void setInterfaceVarbit(int interfaceVarbit) {
        this.interfaceVarbit = interfaceVarbit;
    }

    public int getNormalAttackDistance() {
        return normalAttackDistance;
    }

    public void setNormalAttackDistance(int normalAttackDistance) {
        this.normalAttackDistance = normalAttackDistance;
    }

    public int getLongAttackDistance() {
        return longAttackDistance;
    }

    public void setLongAttackDistance(int longAttackDistance) {
        this.longAttackDistance = longAttackDistance;
    }

    public WieldableDefinition copy() {
        final WieldableDefinition copy = new WieldableDefinition();
        copy.setTwoHanded(twoHanded);
        copy.setAttackSpeed(attackSpeed);
        copy.setAccurateAnimation(accurateAnimation);
        copy.setAggressiveAnimation(aggressiveAnimation);
        copy.setBlockAnimation(blockAnimation);
        copy.setControlledAnimation(controlledAnimation);
        copy.setDefensiveAnimation(defensiveAnimation);
        copy.setRunAnimation(runAnimation);
        copy.setStandAnimation(standAnimation);
        copy.setWalkAnimation(walkAnimation);
        copy.setStandTurnAnimation(standTurnAnimation);
        copy.setRotate90Animation(rotate90Animation);
        copy.setRotate180Animation(rotate180Animation);
        copy.setRotate270Animation(rotate270Animation);
        copy.setInterfaceVarbit(interfaceVarbit);
        copy.setNormalAttackDistance(normalAttackDistance);
        copy.setLongAttackDistance(longAttackDistance);
        return copy;
    }
}
