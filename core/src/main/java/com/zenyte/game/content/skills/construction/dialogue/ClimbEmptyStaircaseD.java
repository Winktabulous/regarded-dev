package com.zenyte.game.content.skills.construction.dialogue;

import com.zenyte.game.content.skills.construction.FurnitureData;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.constants.Furniture;
import com.zenyte.game.content.skills.construction.constants.FurnitureSpace;
import com.zenyte.game.content.skills.construction.constants.RoomType;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;

public final class ClimbEmptyStaircaseD extends Dialogue {

	public ClimbEmptyStaircaseD(Player player, final RoomReference room, final boolean upstairs, final WorldObject object) {
		super(player);
		this.room = room;
		this.upstairs = upstairs;
		this.object = object;
	}
	
	private final RoomReference room;
	private final boolean upstairs;
	private final WorldObject object;
	
	@Override
	public void buildDialogue() {
		if (upstairs) {
			plain("These stairs do not lead anywhere.<br>Do you want to build a room at the top?");
			options(TITLE, "Skill hall", "Quest hall", "Cancel")
			.onOptionOne(() -> {
				finish();
				final RoomReference ref = new RoomReference(RoomType.SKILL_HALL_DS, room.getX(), room.getY(), 2, room.getRotation());
				final int slot = getFurnitureSlot(FurnitureSpace.SKILL_HALL_STAIRCASE);
				final Furniture furniture = FurnitureSpace.SKILL_HALL_STAIRCASE_DS.getFurnitures()[slot];
				ref.getFurnitureData().add(new FurnitureData(FurnitureSpace.SKILL_HALL_STAIRCASE_DS, furniture, new Location(object.getXInChunk(), object.getYInChunk(), 2), object.getType(), object.getRotation()));
				player.getConstruction().createRoom(ref);
			})
			.onOptionTwo(() -> {
				finish();
				final RoomReference ref = new RoomReference(RoomType.QUEST_HALL_DS, room.getX(), room.getY(), 2, room.getRotation());
				final int slot = getFurnitureSlot(FurnitureSpace.SKILL_HALL_STAIRCASE);
				final Furniture furniture = FurnitureSpace.QUEST_HALL_STAIRCASE_DS.getFurnitures()[slot];
				ref.getFurnitureData().add(new FurnitureData(FurnitureSpace.QUEST_HALL_STAIRCASE_DS, furniture, new Location(object.getXInChunk(), object.getYInChunk(), 2), object.getType(), object.getRotation()));
				player.getConstruction().createRoom(ref);
			})
			.onOptionThree(() -> finish());
		} else {
			plain(object.getName().equals("Dungeon entrance") ? "This dungeon entrance does not lead anywhere.<br>Do you want to build a room at the bottom?" 
					: "These stairs do not lead anywhere.<br>Do you want to build a room at the bottom?");
			options(TITLE, "Skill hall", "Quest hall", "Dungeon stairs room", "Cancel")
			.onOptionOne(() -> {
				finish();
				final RoomReference ref = new RoomReference(RoomType.SKILL_HALL, room.getX(), room.getY(), 0, room.getRotation());
				int slot = getFurnitureSlot(FurnitureSpace.SKILL_HALL_STAIRCASE_DS);
				if (slot == -1)
					slot = 0;
				final Furniture furniture = FurnitureSpace.SKILL_HALL_STAIRCASE.getFurnitures()[slot];
				ref.getFurnitureData().add(new FurnitureData(FurnitureSpace.SKILL_HALL_STAIRCASE, furniture, new Location(object.getXInChunk(), object.getYInChunk(), 0), object.getType(), object.getRotation()));
				player.getConstruction().createRoom(ref);
			})
			.onOptionTwo(() -> {
				finish();
				final RoomReference ref = new RoomReference(RoomType.QUEST_HALL, room.getX(), room.getY(), 0, room.getRotation());
				int slot = getFurnitureSlot(FurnitureSpace.SKILL_HALL_STAIRCASE_DS);
				if (slot == -1)
					slot = 0;
				final Furniture furniture = FurnitureSpace.QUEST_HALL_STAIRCASE.getFurnitures()[slot];
				ref.getFurnitureData().add(new FurnitureData(FurnitureSpace.QUEST_HALL_STAIRCASE, furniture, new Location(object.getXInChunk(), object.getYInChunk(), 0), object.getType(), object.getRotation()));
				player.getConstruction().createRoom(ref);
			})
			.onOptionThree(() -> {
				finish();
				final RoomReference ref = new RoomReference(RoomType.DUNGEON_STAIRS_ROOM, room.getX(), room.getY(), 0, room.getRotation());
				int slot = getFurnitureSlot(FurnitureSpace.QUEST_HALL_STAIRCASE_DS);
				if (slot == -1)
					slot = 0;
				final Furniture furniture = FurnitureSpace.QUEST_HALL_STAIRCASE.getFurnitures()[slot];
				ref.getFurnitureData().add(new FurnitureData(FurnitureSpace.QUEST_HALL_STAIRCASE, furniture, new Location(object.getXInChunk(), object.getYInChunk(), 0), object.getType(), object.getRotation()));
				player.getConstruction().createRoom(ref);	
			})
			.onOptionFour(() -> finish());
		}
	}
	
	private final int getFurnitureSlot(FurnitureSpace space) {
		for (int slot = 0; slot < space.getFurnitures().length; slot++) {
			final Furniture furn = space.getFurnitures()[slot];
			if (furn.getObjectId() == object.getId())
				return slot;
		}
		return -1;
	}

}
