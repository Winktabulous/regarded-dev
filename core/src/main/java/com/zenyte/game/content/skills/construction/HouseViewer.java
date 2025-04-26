package com.zenyte.game.content.skills.construction;

import com.zenyte.game.content.skills.construction.constants.RoomType;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.dynamicregion.CoordinateUtilities;
import mgi.types.config.enums.EnumDefinitions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SW corner value = 6.
 * The value at which the table begins is the start value.
 * For example, if I start at 4th square on the second
 * row from the bottom, the base value would be 18.
 * Once the base value has reached value 38,
 * it will then start again from 6 until it reaches
 * the value it began at.
 * @author Kris | 23. nov 2017 : 2:11.06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class HouseViewer {

	private static final int INTERFACE_ID = 422;
	private static final int ROOM_SCRIPT = 1376;
	private static final int GRID_SCRIPT = 1382;
	
	/**
	 * Grid base values, from 5x5 to 9x9. Excluding other values
	 * because our construction was never designed to support them.
	 */
	private static final int[][] GRIDS = new int[][] {
			new int[] { 32770, 65539, 9 },
			new int[] { 16385, 65539, -1 },
			new int[] { 16385, 81925, -1 },
			new int[] { 16385, 98310, -1 },
			new int[] { 32770, 131072, -1 },
	};
	
	public HouseViewer(final Player player) {
		this.player = player;
	}
		
	/**
	 * The owner of the house.
	 */
	private final transient Player player;
	
	/**
	 * The reference we're editing (moving, rotating, deleting).
	 */
	private transient RoomReference room;
	
	/**
	 * The component id of the room.
	 */
	private transient int roomComponent;
	
	/**
	 * The temporary rotation. 
	 * Cannot access directly as players can cancel it.
	 */
	private transient int rotation;

	/**
	 * The plane we're viewing at the moment.
	 */
	private transient int plane;
	
	/**
	 * Size of the grid.
	 */
	private transient int size;
	
	/**
	 * Index of the room we're modifying.
	 */
	private transient int index;
	
	/**
	 * Whether to unlock the display button or not.
	 */
	private transient boolean displayDungeon;
	
	/**
	 * Whether to unlock the upper floor button or not.
	 */
	private transient boolean displayUpperFloor;
	
	/**
	 * A map containing the bits used for the object in a room.
	 */
	private final transient List<Integer> bits = new ArrayList<Integer>();
	
	public void openHouseViewer() {
		player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
		plane = player.getPlane();
			size = 9;
			for (final RoomReference room : player.getConstruction().getReferences()) {
				if (room.getPlane() == 0) {
					displayDungeon = true;
				} else if (room.getPlane() == 2) {
					displayUpperFloor = true;
				}
			}
		displayDungeon = true;
		displayUpperFloor = true;
		player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, INTERFACE_ID);
		final int size = player.getConstruction().getReferences().size();
		int count = 1;
		final Iterator<RoomReference> it = player.getConstruction().getReferences().iterator();
		while(it.hasNext()) {
			final RoomReference ref = it.next();
			final int settings = ((ref.getY() * 8) + ref.getX()) | ref.getPlane() << 6 | ref.getRotation() << 8 | ref.getRoom().getInterfaceSlot() << 10 | getRoomContents(ref);
			player.getPacketDispatcher().sendClientScript(ROOM_SCRIPT, count++, ref.getRoom().getEnumMap(), 0, settings, getAdditionalRoomContents(ref));
		}
		final int xGrid = GRIDS[this.size - 5][0] | (displayDungeon ? 0 : 1) << 28;
		final int yGrid = GRIDS[this.size - 5][1] | 1 << 28 | (displayUpperFloor ? 1 : 0) << 29;
		player.getVarManager().sendBit(5330, 0);
		player.getVarManager().sendBit(5329, roomComponent);
		player.getVarManager().sendBit(5333, 0);
		if (room != null) {
			player.getVarManager().sendBit(5333, room.getRoom().getInterfaceSlot());
		}
		player.getPacketDispatcher().sendClientScript(GRID_SCRIPT, size, xGrid, yGrid, plane);
		player.getPacketDispatcher().sendComponentSettings(422, 5, 0, 242, AccessMask.CLICK_OP1, AccessMask.CLICK_OP6);
	}
	
	public void handleInterface(final int interfaceId, final int componentId, final int slotId, final int itemId, final int option) {
		if (componentId > 5 && componentId < 39) {
			player.getTemporaryAttributes().remove("houseviewerMove");
			room = player.getConstruction().getReferences().get(componentId - 6);
			index = GRIDS[size - 5][2] + (82 * room.getPlane()) + (room.getY() * 9) + room.getX() + (2 - room.getPlane());
			player.getVarManager().sendBit(5330, 0);
			player.getVarManager().sendBit(5329, roomComponent = (componentId - 5));
			player.getVarManager().sendBit(5332, 0);
			player.getVarManager().sendBit(5333, room.getRoom().getInterfaceSlot());
		} else if (componentId == 58) {//move
			player.getTemporaryAttributes().put("houseviewerMove", true);
		} else if (componentId == 59) {//rotate
			rotation = room.getRotation();
			player.getVarManager().sendBit(5330, index);
			player.getVarManager().sendBit(5332, 1);
			player.getVarManager().sendBit(5331, rotation);
		} else if (componentId == 60 || componentId == 61) {
			rotate(componentId == 60);
		} else if (componentId == 62) {
			player.getConstruction().getReferences().remove(room);
			for (int i = 5329; i < 5335; i++) {
				player.getVarManager().sendBit(i, 0);
			}
			reopen();
			//openHouseViewer();
		} else if (componentId == 63) {
			player.getVarManager().sendBit(5332, 0);
			player.getVarManager().sendBit(5330, 0);
		} else if (componentId == 64) {
			player.getVarManager().sendBit(5332, 0);
			if (room.getRotation() == rotation) {
				player.getVarManager().sendBit(5330, 0);
				return;
			}
			final int roomRot = room.getRotation();
			int rot = 3;
			if (roomRot == 0) {
				rot = rotation;
			} else if (roomRot == 1) {
				if (rotation != 0) {
					rot = 1;
				}
			} else if (roomRot == 2) {
				rot = (rotation + 2) & 0x3;
			} else if (roomRot == 3) {
				if (rotation == 0) {
					rot = 1;
				}
			}
			final int rota = rot;
			room.setRotation(rotation);
			room.getFurnitureData().forEach(r -> {
				//final int x, final int y, final int mapRotation, int sizeX, int sizeY, final int objectRotation
				final int[] coords = CoordinateUtilities.translate(r.getLocation().getX(), r.getLocation().getY(), rota);
				r.getLocation().setLocation(coords[0], coords[1], r.getLocation().getPlane());
			});
			reopen();
		} else if (componentId == 5) {
			int slot = slotId + 1;
			final int plane = slot > 170 ? 2 : slot > 80 ? 1 : 0;
			int x = 0;
			int y = 0;
			slot -= (size == 5 ? 9 : 0) + (plane * 80);
			y = (slot - 1) / size;
			x = slot - (y * size) - plane;
			y+= player.getConstruction().getYardOffset();
			x+= player.getConstruction().getYardOffset();
			final Object moving = player.getTemporaryAttributes().remove("houseviewerMove");
			if (x == 0 || y == 0 || x == 8 || y == 8) {
				player.sendMessage("You cannot " + (moving == null ? "build" : "move") + " rooms on the edge of the map.");
				return;
			}
			if (moving != null) {
				room.setX(x);
				room.setY(y);
				index = GRIDS[size - 5][2] + (82 * room.getPlane()) + (room.getY() * 9) + room.getX() + 1;
				
				reopen();
				return;
			}
			player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 212);
			player.getTemporaryAttributes().put("houseviewerRoomAdd", slotId);
		}
	}
	
	private void reopen() {
		player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
		player.getConstruction().enterHouse(true, player.getConstruction().getRelationalSpawnTile());
		WorldTasksManager.schedule(() -> openHouseViewer(), 3);
	}
	
	private void rotate(final boolean clockwise) {
		if (clockwise) {
			if (++rotation == 4) {
				rotation = 0;
			}
		} else {
			if (--rotation == -1) {
				rotation = 3;
			}
		}
	}
	
	public void addRoom(final int componentId, final int slot) {
		final int slotId = (int) player.getTemporaryAttributes().get("houseviewerRoomAdd");
		RoomType room = RoomType.getRoomBySlot(slot);
		final int plane = slotId > 170 ? 2 : slotId > 80 ? 1 : 0;
		if (plane == 0) {
			if (!(room == RoomType.DUNGEON_CORRIDOR || room == RoomType.DUNGEON_JUNCTION || room == RoomType.OUBLIETTE || room == RoomType.DUNGEON_STAIRS_ROOM)) {
				openHouseViewer();
				player.sendMessage("You cannot build a " + room.toString().toLowerCase() + " in dungeon.");
				return;
			}
		} else if (plane == 1) {
			if (room == RoomType.DUNGEON_CORRIDOR || room == RoomType.DUNGEON_JUNCTION || room == RoomType.OUBLIETTE || room == RoomType.DUNGEON_STAIRS_ROOM) {
				openHouseViewer();
				player.sendMessage("You cannot build a " + room.toString().toLowerCase() + " on the surface.");
				return;
			}
		} else if (plane == 2) {
			if (room == RoomType.DUNGEON_CORRIDOR || room == RoomType.DUNGEON_JUNCTION || room == RoomType.OUBLIETTE || room == RoomType.DUNGEON_STAIRS_ROOM) {
				openHouseViewer();
				player.sendMessage("You cannot build a " + room.toString().toLowerCase() + " on the surface.");
				return;
			}
			if (room == RoomType.SKILL_HALL) {
				room = RoomType.SKILL_HALL_DS;
			} else if (room == RoomType.QUEST_HALL) {
				room = RoomType.QUEST_HALL_DS;
			}
		}
		int coordinateSlot = slotId + 1;
		int x = 0;
		int y = 0;
		coordinateSlot -= (size == 5 ? 9 : 0) + (plane * 80);
		y = (coordinateSlot - 1) / size;
		x = coordinateSlot - (y * size) - plane;
		y+= player.getConstruction().getYardOffset();
		x+= player.getConstruction().getYardOffset();
		player.getConstruction().getReferences().add(new RoomReference(room, x, y, plane, 0));
		this.plane = plane;
		reopen();
	}
	
	/**
	 * Gets the value of the contents of the room,
	 * used for hovers in the house viewer.
	 * @return bitpacked value of contents.
	 */
	private int getRoomContents(final RoomReference room) {
		int mask = 0;
		bits.clear();
		for (final FurnitureData furn : room.getFurnitureData()) {
			addBits(room.getRoom().getEnumMap(), furn.getFurniture().getItemId());
		}
		for (final Integer bit : bits) {
			mask |= 1 << bit;
		}
		return mask;
	}
	
	private int getAdditionalRoomContents(final RoomReference room) {
		int bits = 0;
		for (final FurnitureData data : room.getFurnitureData()) {
			final int item = data.getFurniture().getItemId();
			if (item == 21213 || item == 20652) {
				bits |= 1 << 15;
			} else if (item == 20634) {
				bits |= 1 << 18;
			}
		}
		return bits;
	}
	
	private void addBits(final int scriptId, final int itemId) {
		final EnumDefinitions map = EnumDefinitions.get(scriptId);
		final long value = map.getKeyForValue(itemId);
		if (value == 16385) {
			bits.add(15);
		} else if (value == 16386) {
			bits.add(16);
		} else if (value == 16387) {
			bits.add(15);
			bits.add(16);
		} else if (value == 16388) {
			bits.add(17);
		} else if (value == 16389) {
			bits.add(15);
			bits.add(17);
		} else if (value == 16390) {
			bits.add(16);
			bits.add(17);
		} else if (value == 16391) {
			bits.add(15);
			bits.add(16);
			bits.add(17);
		} else if (value == 32769) {
			bits.add(18);
		} else if (value == 32770) {
			bits.add(19);
		} else if (value == 32771) {
			bits.add(18);
			bits.add(19);
		} else if (value == 32772) {
			bits.add(20);
		} else if (value == 32773) {
			bits.add(18);
			bits.add(20);
		} else if (value == 32774) {
			bits.add(19);
			bits.add(20);
		} else if (value == 32775) {
			bits.add(18);
			bits.add(19);
			bits.add(20);
		} else if (value == 49153) {
			bits.add(21);
		} else if (value == 49154) {
			bits.add(22);
		} else if (value == 49155) {
			bits.add(21);
			bits.add(22);
		} else if (value == 49156) {
			bits.add(23);
		} else if (value == 49157) {
			bits.add(21);
			bits.add(23);
		} else if (value == 49158) {
			bits.add(22);
			bits.add(23);
		} else if (value == 49159) {
			bits.add(21);
			bits.add(22);
			bits.add(23);
		} else if (value == 65537) {
			bits.add(24);
		} else if (value == 65538) {
			bits.add(25);
		} else if (value == 65539) {
			bits.add(24);
			bits.add(25);
		} else if (value == 81921) {
			bits.add(26);
		} else if (value == 81922) {
			bits.add(27);
		} else if (value == 81923) {
			bits.add(26);
			bits.add(27);
		} else if (value == 98305) {
			bits.add(28);
		} else if (value == 98306) {
			bits.add(29);
		} else if (value == 98307) {
			bits.add(28);
			bits.add(29);
		} else if (value == 114689) {
			bits.add(30);
		} else if (value == 114690) {
			bits.add(31);
		} else if (value == 114691) {
			bits.add(30);
			bits.add(31);
		}
	}
	
}
