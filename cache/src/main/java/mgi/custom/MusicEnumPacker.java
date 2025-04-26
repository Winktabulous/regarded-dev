package mgi.custom;

import mgi.types.config.enums.EnumDefinitions;

/**
 * @author Kris | 09/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MusicEnumPacker {
    public void pack() {
        EnumDefinitions enumDef;
        for (int enumId : new int[] {812, 817}) {
            enumDef = EnumDefinitions.get(enumId);
            enumDef.getValues().put(enumDef.getLargestIntKey() + 1, "Silent Knight");
            enumDef.getValues().put(enumDef.getLargestIntKey() + 2, "Smorgasbord");
            enumDef.getValues().put(enumDef.getLargestIntKey() + 3, "Lazy Wabbit");
            enumDef.pack();
        }
        enumDef = EnumDefinitions.get(818);
        enumDef.getValues().put(enumDef.getLargestIntKey() + 1, enumDef.getLargestIntKey() + 2);
        enumDef.getValues().put(enumDef.getLargestIntKey() + 2, enumDef.getLargestIntKey() + 3);
        enumDef.getValues().put(enumDef.getLargestIntKey() + 3, enumDef.getLargestIntKey() + 4);
        enumDef.pack();
        enumDef = EnumDefinitions.get(819);
        enumDef.getValues().put(enumDef.getLargestIntKey() + 1, enumDef.getValues().get(1));
        enumDef.getValues().put(enumDef.getLargestIntKey() + 2, enumDef.getValues().get(1));
        enumDef.getValues().put(enumDef.getLargestIntKey() + 3, enumDef.getValues().get(1));
        enumDef.pack();
    }
}
