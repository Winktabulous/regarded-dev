package com.zenyte.game.content.chambersofxeric.map;

import com.zenyte.game.content.chambersofxeric.room.*;
import mgi.utilities.StringFormatUtil;

/**
 * @author Kris | 12. nov 2017 : 23:40.34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum RaidRoom {

    FLOOR_END_DOWNSTAIRS(644, 0, FloorEdgeRoom.class, '¤', null),
    FLOOR_START_UPSTAIRS(712, 0, FloorEdgeRoom.class, '#', null),
    RAID_START(648, 0, EntranceRoom.class, '#', null),
    SMALL_SCAVENGER_RUNT(652, 0, SmallScavengerRoom.class, 'C', null),
    LIZARDMEN_SHAMAN(656, 0, LizardmanShamanRoom.class, 'C', "Shamans"),
    VASA_NISTIRIO(660, 0, VasaNistirioRoom.class, 'B', "Vasa"),
    VANGUARD(664, 0, VanguardRoom.class, 'B', "Vanguards"),
    ICE_DEMON(668, 0, IceDemonRoom.class, 'P', "Ice Demon"),
    CREATURE_KEEPER(672, 0, CreatureKeeperRoom.class, 'P', "Thieving"),
    RESOURCES_A(680, 0, ResourcesRoom.class, 'S', null),
    LARGE_SCAVENGER_BEAST(652, 1, LargeScavengerRoom.class, 'C', null),
    DARK_ALTAR_ROOM(656, 1, DarkAltarRoom.class, 'C', "Mystics"),
    TEKTON(660, 1, TektonRoom.class, 'B', "Tekton"),
    MUTTADILES(664, 1, MuttadileRoom.class, 'B', "Muttadiles"),
    DEATHLY_ROOM(668, 1, DeathlyRoom.class, 'P', "Tightrope"),
    RESOURCES_B(680, 1, ResourcesRoom.class, 'S', null),
    GUARDIANS(656, 2, GuardiansRoom.class, 'C', "Guardians"),
    VESPULA(660, 2, VespulaRoom.class, 'B', "Vespula"),
    CRAB_PUZZLE(668, 2, CrabPuzzleRoom.class, 'P', "Crabs");

    public static final RaidRoom[] values = values();
    /**
     * The static map's y coordinate of the chunk. The x is always one of the three - 408, 412 & 416, based on the exit location of the room.
     */
    private final int staticChunkY;
    /**
     * The plane of the room on the static map.
     */
    private final int height;
    /**
     * The parent room class type.
     */
    private final Class<? extends RaidArea> parentClass;
    /**
     * The formatted name of the room.
     */
    private final String formattedName;
    /**
     * The character constant of the room, defining its classification type.
     */
    private final char typeChar;

    /**
     * The name that runelite gives to a raid room.
     */
    private final String runeliteNaming;

    RaidRoom(final int staticChunkY, final int height, final Class<? extends RaidArea> parentClass, final char typeChar, final String runeliteNaming) {
        this.staticChunkY = staticChunkY;
        this.height = height;
        this.parentClass = parentClass;
        this.formattedName = StringFormatUtil.formatString(toString());
        this.typeChar = typeChar;
        this.runeliteNaming = runeliteNaming;
    }

    public int getStaticChunkY() {
        return staticChunkY;
    }

    public int getHeight() {
        return height;
    }

    public Class<? extends RaidArea> getParentClass() {
        return parentClass;
    }

    public String getFormattedName() {
        return formattedName;
    }

    public char getTypeChar() {
        return typeChar;
    }

    public String getRuneliteNaming() {
        return runeliteNaming;
    }

}
