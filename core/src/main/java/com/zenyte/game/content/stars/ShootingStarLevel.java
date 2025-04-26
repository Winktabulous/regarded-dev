package com.zenyte.game.content.stars;

/**
 * @author Andys1814
 */
public enum ShootingStarLevel {
    ONE(1, 41229, 1_200, 10),
    TWO(2, 41228, 700, 20),
    THREE(3, 41227, 430, 30),
    FOUR(4, 41226, 250, 40),
    FIVE(5, 41225, 175, 50),
    SIX(6, 41224, 80, 60),
    SEVEN(7, 41223, 40, 70),
    EIGHT(8, 41021, 40, 80),
    NINE(9, 41020, 15, 90);
//    ONE(1, 41229, 2, 10),
//    TWO(2, 41228, 2, 20),
//    THREE(3, 41227, 2, 30),
//    FOUR(4, 41226, 2, 40),
//    FIVE(5, 41225, 2, 50),
//    SIX(6, 41224, 2, 60),
//    SEVEN(7, 41223, 2, 70),
//    EIGHT(8, 41021, 2, 80),
//    NINE(9, 41020, 15, 90);

    private final int numeric;

    private final int objectId;

    private final int stardust;

    private final int levelRequirement;

    ShootingStarLevel(int numeric, int objectId, int stardust, int levelRequirement) {
        this.numeric = numeric;
        this.objectId = objectId;
        this.stardust = stardust;
        this.levelRequirement = levelRequirement;
    }

    public ShootingStarLevel getNextLevel() {
        switch (this) {
            case NINE: return EIGHT;
            case EIGHT: return SEVEN;
            case SEVEN: return SIX;
            case SIX: return FIVE;
            case FIVE: return FOUR;
            case FOUR: return THREE;
            case THREE: return TWO;
            case TWO: return ONE;
            case ONE: throw new IllegalStateException("Exception while transforming star: can not compute next level for ONE.");
            default: throw new IllegalStateException("Exception while transforming star: unhandled level.");
        }
    }

    public int getNumeric() {
        return numeric;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getStardust() {
        return stardust;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }
}
