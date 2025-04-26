package com.zenyte.game.content.skills.construction;

import com.google.gson.annotations.Expose;
import com.zenyte.game.content.skills.construction.constants.Furniture;
import com.zenyte.game.content.skills.construction.constants.FurnitureSpace;
import com.zenyte.game.content.skills.construction.constants.RoomType;

import java.util.ArrayList;
import java.util.List;

public class RoomReference {

	@Expose
    private RoomType room;

    @Expose
    private int x, y, plane, rotation;

	@Expose
    private List<FurnitureData> furnitureData;

	public RoomReference(RoomType room, int positionX, int positionY, int plane, int rotation) {
		this.room = room;
		this.x = positionX;
		this.y = positionY;
		this.plane = plane;
		this.rotation = rotation;
		furnitureData = new ArrayList<FurnitureData>();
	}
	
	public FurnitureData getFurniture(Furniture furniture) {
		for (FurnitureData data : furnitureData) {
			if (data.getFurniture() == furniture)
				return data;
		}
		return null;
	}
	
	public FurnitureData getFurniture(FurnitureSpace space) {
		for (FurnitureData data : furnitureData) {
			if (data.getSpace() == space)
				return data;
		}
		return null;
	}
	
	public FurnitureData getFurniture(final String... furniture) {
		for (FurnitureData data : furnitureData) {
			for (String furn : furniture) {
				if (data.getFurniture().name().contains(furn))
					return data;
			}
		}
		return null;
	}
	
	public FurnitureData getStaircase() {
		for (FurnitureData data : furnitureData) {
			final Furniture furn = data.getFurniture();
			if (furn.ordinal() >= Furniture.OAK_STAIRCASE_DS.ordinal()
					&& furn.ordinal() <= Furniture.MARBLE_SPIRAL.ordinal())
				return data;
		}
		return null;
    }

    public FurnitureData getCarpet() {
        for (FurnitureData data : furnitureData) {
            final Furniture furn = data.getFurniture();
            if (furn == Furniture.RUG || furn == Furniture.BROWN_RUG || furn == Furniture.OPULENT_RUG)
                return data;
        }
        return null;
    }

    public RoomType getRoom() {
        return room;
    }

    public void setRoom(RoomType room) {
        this.room = room;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPlane() {
        return plane;
    }

    public void setPlane(int plane) {
        this.plane = plane;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public List<FurnitureData> getFurnitureData() {
        return furnitureData;
    }

    public void setFurnitureData(List<FurnitureData> furnitureData) {
        this.furnitureData = furnitureData;
    }

}