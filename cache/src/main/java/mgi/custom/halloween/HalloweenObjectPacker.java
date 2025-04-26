package mgi.custom.halloween;

import mgi.types.config.ObjectDefinitions;

import java.util.Arrays;

import static mgi.custom.halloween.HalloweenObject.*;

public class HalloweenObjectPacker {

    private static final String[] nullOps = new String[] {
            null, null, null, null, null
    };

    public void pack() {
        DEAD_EXPLORER.builder().options(nullOps).build().pack();
        TRAPDOOR.builder().build().pack();
        BOAT.builder().build().pack();
        DEAD_TREE.builder().options(nullOps).build().pack();
        System.err.println(Arrays.toString(ObjectDefinitions.get(TWISTED_BUSH.getOriginalObject()).getOptions()));
        TWISTED_BUSH.builder().options(new String[] {null, null, null, null, "Search"}).name("Bush").build().pack();
        DEAD_WILLOW.builder().options(nullOps).build().pack();
        PUMP_AND_DRAIN.builder().options(nullOps).build().pack();
        DEAD_YEW.builder().options(nullOps).build().pack();
        STRANGE_FLOOR.builder().build().pack();
        System.err.println(Arrays.toString(ObjectDefinitions.get(FIRE_WALL.getOriginalObject()).getOptions()));
        FIRE_WALL.builder().options(new String[] {"Go-through", null, null, null, null}).build().pack();
        STEPPING_STONES.builder().build().pack();
        DRAYNOR_MANOR_LEFT_DOOR.builder().build().pack();
        DRAYNOR_MANOR_RIGHT_DOOR.builder().build().pack();
        System.err.println(Arrays.toString(ObjectDefinitions.get(BLOODY_AXE.getOriginalObject()).getOptions()));
        BLOODY_AXE.builder().name("Bloody axe").options(new String[] { "Look-at", null, null, null, null }).build().pack();
        System.err.println(Arrays.toString(ObjectDefinitions.get(SKELETON.getOriginalObject()).getOptions()));
        SKELETON.builder().options(new String[] {null, null, null, null, "Search"}).build().pack();
        TORCH.builder().options(nullOps).build().pack();
        CELL_DOOR.builder().build().pack();
        PIANO.builder().options(nullOps).build().pack();
        RANGE.builder().options(nullOps).build().pack();
        WEB.builder().build().pack();
        BARRIER.builder().build().pack();
        System.err.println(Arrays.toString(ObjectDefinitions.get(PILE_OF_RUBBLE.getOriginalObject()).getOptions()));
        PILE_OF_RUBBLE.builder().name("Pile of rubble").options(new String[] {"Jump-over", null, null, null, null}).build().pack();
        CHEST.builder().build().pack();
        OPENED_CHEST.builder().options(nullOps).build().pack();
    }

}
