package com.zenyte.game.content.skills.construction;

import com.google.gson.annotations.Expose;
import com.zenyte.game.content.skills.construction.constants.Furniture;
import com.zenyte.game.content.skills.construction.constants.FurnitureSpace;
import com.zenyte.game.world.entity.Location;

public class FurnitureData {

    @Expose

    private FurnitureSpace space;
    @Expose

    private Furniture furniture;
    @Expose

    private Location location;
    @Expose

    private int type, rotation;

    public FurnitureData(FurnitureSpace space, Furniture furniture, Location location, int type, int rotation) {
        this.space = space;
        this.furniture = furniture;
        this.location = location;
        this.type = type;
        this.rotation = rotation;
    }

    public FurnitureSpace getSpace() {
        return space;
    }

    public void setSpace(FurnitureSpace space) {
        this.space = space;
    }

    public Furniture getFurniture() {
        return furniture;
    }

    public void setFurniture(Furniture furniture) {
        this.furniture = furniture;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

}
