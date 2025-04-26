package mgi.custom.christmas;

/**
 * @author Kris | 24/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ChristmasObjectPacker {

    private static final String[] nullOps = new String[] {
            null, null, null, null, null
    };

    public void pack() {
        ChristmasObject.CAVE_ENTRANCE.builder().options(nullOps).build().pack();
        ChristmasObject.HOLE.builder().options(nullOps).build().pack();
        ChristmasObject.HOLLOW_LOG.builder().options(nullOps).build().pack();
        ChristmasObject.ALT_HOLLOW_LOG.builder().options(nullOps).build().pack();
        ChristmasObject.CHEST.builder().options(nullOps).build().pack();
        ChristmasObject.TUNNEL.builder().options(nullOps).build().pack();
        ChristmasObject.SNOW_DRIFT.builder().options(nullOps).build().pack();
    }

}
