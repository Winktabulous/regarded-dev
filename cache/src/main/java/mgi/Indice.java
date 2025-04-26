package mgi;

/**
 * @author Kris | 9. veebr 2018 : 2:55.04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum Indice {

    SKELETON(0),
    SKIN(1),
    UNDERLAY_DEFINITIONS(2, 1),
    IDENTITY_KIT_DEFINITIONS(2, 3),
    OVERLAY_DEFINITIONS(2, 4),
    INVENTORY_SIZE_DEFINITIONS(2, 5),
    OBJECT_DEFINITIONS(2, 6),
    MAPPED_VALUES(2, 8),
    NPC_DEFINITIONS(2, 9),
    ITEM_DEFINITIONS(2, 10),
    PARAMETERS(2, 11),
    ANIMATION_DEFINITIONS(2, 12),
    GRAPHICS_DEFINITIONS(2, 13),
    VARBIT_DEFINITIONS(2, 14),
    HITBAR_DEFINITIONS(2, 33),
    VARC_STR(2, 15),
    VARP_DEFINITIONS(2, 16),
    VARC_INT(2, 19),
    HITMARK(2, 32),
    HITBAR(2, 33),
    STRUCT(2, 34),
    AREA(2, 35),
    INTERFACE_DEFINITIONS(3),
    PRIMARY_SOUND_EFFECTS(4),
    MAPS(5),
    PRIMARY_MUSIC(6),
    MODELS(7),
    SPRITES(8),
    TEXTURES(9),
    HUFFMAN(10),
    SECONDARY_MUSIC(11),
    STRUCTS(12),
    FONTS(13),
    SECONDARY_SOUND_EFFECTS(14),
    TERTIARY_SOUND_EFFECTS(15),
    WORLD_MAP(16),
    LOADING(17);

    private final int indice, archive;

    Indice(final int indice, final int archive) {
        this.indice = indice;
        this.archive = archive;
    }

    Indice(final int indice) {
        this(indice, -1);
    }

    public int getIndice() {
        return indice;
    }

    public int getArchive() {
        return archive;
    }

}
