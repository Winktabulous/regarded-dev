package com.zenyte.game.world.entity.pathfinding.strategy;

import com.zenyte.game.world.entity.pathfinding.RouteStrategy;
import com.zenyte.game.world.object.WorldObject;

public class ObjectStrategy extends RouteStrategy {

    /**
     * Contains object pos x.
     */
    private final int x;
    /**
     * Contains object pos y.
     */
    private final int y;
    /**
     * Contains object route type.
     */
    private final int routeType;
    /**
     * Contains object type.
     */
    private final int type;
    /**
     * Contains object rotation.
     */
    private final int rotation;
    /**
     * Contains object size X.
     */
    private final int sizeX;
    /**
     * Contains object size Y.
     */
    private final int sizeY;
    /**
     * Contains block flag.
     */
    private int accessBlockFlag;

    public ObjectStrategy(final WorldObject object) {
        this(object, 0);
    }
    
    public ObjectStrategy(final WorldObject object, final int distance) {
        this(object, distance, object.getDefinitions().getAccessBlockFlag());
    }

    public ObjectStrategy(final WorldObject object, final int distance, final int accessBlockFlag) {
        super(distance);
        x = object.getX();
        y = object.getY();
        routeType = getType(object);
        type = object.getType();
        rotation = object.getRotation();
        sizeX = rotation == 0 || rotation == 2 ? object.getDefinitions().getSizeX() : object.getDefinitions().getSizeY();
        sizeY = rotation == 0 || rotation == 2 ? object.getDefinitions().getSizeY() : object.getDefinitions().getSizeX();
        this.accessBlockFlag = accessBlockFlag;
        if (rotation != 0) {
			this.accessBlockFlag = ((accessBlockFlag << rotation) & 0xF) + (accessBlockFlag >> (4 - rotation));
		}
    }

    @Override
    public boolean canExit(final int currentX, final int currentY, final int sizeXY, final int[][] clip, final int clipBaseX, final int clipBaseY) {
        switch (routeType) {
            case 0:
                return RouteStrategy.checkWallInteract(clip, currentX - clipBaseX, currentY - clipBaseY,
                        sizeXY + getDistance(), x - clipBaseX, y - clipBaseY, type, rotation);
            case 1:
                return RouteStrategy.checkWallDecorationInteract(clip, currentX - clipBaseX,
                        currentY - clipBaseY, sizeXY + getDistance(), x - clipBaseX,
                        y - clipBaseY, type, rotation);
            case 2:
                return RouteStrategy.checkFilledRectangularInteract(clip, currentX - clipBaseX,
                        currentY - clipBaseY, sizeXY + getDistance(), sizeXY + getDistance(),
                        x - clipBaseX, y - clipBaseY, sizeX + getDistance(), sizeY + getDistance(),
                        accessBlockFlag);
            case 3:
                return currentX == x && currentY == y;
        }
        return false;
    }

    @Override
    public int getApproxDestinationX() {
        return x;
    }

    @Override
    public int getApproxDestinationY() {
        return y;
    }

    @Override
    public int getApproxDestinationSizeX() {
        return sizeX;
    }

    @Override
    public int getApproxDestinationSizeY() {
        return sizeY;
    }

    private int getType(final WorldObject object) {
        final int type = object.getType();
        if ((type >= 0 && type <= 3) || type == 9) {
			return 0; // wall
		} else if (type < 9) {
			return 1; // deco
		} else if (type == 10 || type == 11 || type == 22) {
			return 2; // ground
		}
		else {
			return 3; // misc
		}
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ObjectStrategy)) {
			return false;
		}
        final ObjectStrategy strategy = (ObjectStrategy) other;
        return x == strategy.x && y == strategy.y && routeType == strategy.routeType && type == strategy.type
                && rotation == strategy.rotation && sizeX == strategy.sizeX && sizeY == strategy.sizeY
                && accessBlockFlag == strategy.accessBlockFlag;
    }

}
