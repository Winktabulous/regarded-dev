package mgi.types.worldmap;

/**
 * @author Tommeh | 4-12-2018 | 20:13
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class WorldMapGameObject {
    private int id;
    private int type;
    private int rotation;

    public WorldMapGameObject(int id, int type, int rotation) {
        this.id = id;
        this.type = type;
        this.rotation = rotation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "WorldMapGameObject(id=" + this.getId() + ", type=" + this.getType() + ", rotation=" + this.getRotation() + ")";
    }

}
