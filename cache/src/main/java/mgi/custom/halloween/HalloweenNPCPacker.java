package mgi.custom.halloween;

import static mgi.custom.halloween.HalloweenNPC.*;
/**
 * @author Kris | 03/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class HalloweenNPCPacker {

    private static final String[] nullOps = new String[] {
            null, null, null, null, null
    };

    public void pack() {
        JONAS_SHORTSWORD.builder().name("Jonas").options(nullOps).models(new int[] {52524, 52526}).chatModels(null).originalColours(new short[] {17467}).replacementColours(new short[] {18626}).build().pack();
        JONAS_LONGSWORD.builder().name("Jonas").options(nullOps).models(new int[] {52524, 52525}).chatModels(null).originalColours(new short[] {17467}).replacementColours(new short[] {18626}).build().pack();
        JONAS_UNMASKED.builder().name("Jonas").options(nullOps).models(new int[] {52527, 52525}).chatModels(null).originalColours(new short[] {17467}).replacementColours(new short[] {18626}).build().pack();
        GRIM_REAPER_BASE.builder().name("Grim Reaper").options(new String[]{"Talk-to", null, null, null, null}).models(new int[] {52530, 52529}).chatModels(new int[] {52528}).standAnimation(7749).walkAnimation(-1).rotate90Animation(-1)
                .rotate180Animation(-1).rotate270Animation(-1).size(2).varp(3620).transmogrifiedIds(new int[] {10029, -1}).build().pack();
        GRIM_REAPER.builder().name("Grim Reaper").options(new String[]{"Talk-to", null, null, null, null}).models(new int[] {52530, 52529}).chatModels(new int[] {52528}).standAnimation(7749).walkAnimation(-1).rotate90Animation(-1)
                .rotate180Animation(-1).rotate270Animation(-1).size(2).build().pack();
        SHILOP.builder().build().pack();
        CROW.builder().build().pack();
        GHOST.builder().name("Ghost").options(new String[] {"Talk-to", null, null, null, null}).build().pack();
    }

}
