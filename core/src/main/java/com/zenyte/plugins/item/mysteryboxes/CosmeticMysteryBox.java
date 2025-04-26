package com.zenyte.plugins.item.mysteryboxes;

import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import com.zenyte.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import com.zenyte.plugins.interfaces.MysteryBoxInterface;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * @author Kris | 03/09/2019 02:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CosmeticMysteryBox extends ItemPlugin {

    public static int totalWeight;
    public static MysteryItem[] rewards;

    public static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();
    public static ObjectArrayList<DropViewerEntry> toEntries() {
        if(entries.size() == 0) {
            calculateEntries();
        }
        return entries;
    }

    private static void calculateEntries() {
        for (final MysteryItem reward : rewards) {
            OtherDropViewerEntry entry = new OtherDropViewerEntry(reward.getId(), reward.getMinAmount(), reward.getMaxAmount(), reward.getWeight(), totalWeight, "");
            entries.add(entry);
        }
    }

    @Override
    public void handle() {
        bind("Open", (player, item, container, slotId) -> MysteryBoxInterface.openBox(player, item.getId(), rewards, totalWeight, null));
        bind("Quick-Open", (player, item, container, slotId) -> MysteryBoxInterface.openBoxQuick(player, item.getId(), rewards, totalWeight, null));

        rewards = new MysteryItem[] {
                // Common/Uncommon
                new MysteryItem(20002, 1000), // dscim orn kit
                new MysteryItem(20062, 1000), // torture orn kit
                new MysteryItem(22246, 1000), // anguish orn kit
                new MysteryItem(20065, 1000), // occult orn kit
                new MysteryItem(12802, 1000), // ward kit
                new MysteryItem(12800, 1000), // dpick kit
                new MysteryItem(20143, 1000), // ddef kit
                new MysteryItem(19991, 1000), // bucket helm
                new MysteryItem(20059, 1000), // bucket helm (g)
                new MysteryItem(6666, 1000), // flippers
                new MysteryItem(13655, 1000), // Gnome child hat
                new MysteryItem(12337, 1000), // Sagacious specs
                new MysteryItem(20053, 1000), // half moon specs
                new MysteryItem(19970, 1000), // dark bow tie
                new MysteryItem(12245, 1000), // beanie
                new MysteryItem(23285, 1000), // mole slippers
                new MysteryItem(23288, 1000), // frog slippers
                new MysteryItem(23291, 1000), // bear feet
                new MysteryItem(23294, 1000), // demon feet
                new MysteryItem(23300, 1000), // shoulder parrot
                new MysteryItem(23357, 1000), // rain-bow
                new MysteryItem(23360, 1000), // ham joint
                new MysteryItem(23389, 1000), // manacles
                new MysteryItem(23413, 1000), // climbing boots (g)
                new MysteryItem(23227, 1000), // rune def kit
                new MysteryItem(23232, 1000), // obby maul kit
                new MysteryItem(23237, 1000), // b neck kit

                // Rare
                new MysteryItem(3057, 500), // mime mask
                new MysteryItem(3058, 500), // mime top
                new MysteryItem(3059, 500), // mime legs
                new MysteryItem(3060, 500), // mime gloves
                new MysteryItem(3061, 500), // mime boots
                new MysteryItem(6184, 500), // prince tunic
                new MysteryItem(6185, 500), // prince legs
                new MysteryItem(6186, 500), // princess top
                new MysteryItem(6187, 500), // princess skirt
                new MysteryItem(7592, 500), // zombie shirt
                new MysteryItem(7593, 500), // zombie legs
                new MysteryItem(7594, 500), // zombie mask
                new MysteryItem(7595, 500), // zombie gloves
                new MysteryItem(7596, 500), // zombie boots
                new MysteryItem(12887, 500), // santa mask
                new MysteryItem(12888, 500), // santa jacket
                new MysteryItem(12889, 500), // santa legs
                new MysteryItem(12890, 500), // santa gloves
                new MysteryItem(12891, 500), // santa boots
                new MysteryItem(11021, 500), // chicken head
                new MysteryItem(11019, 500), // chicken feet
                new MysteryItem(11020, 500), // chicken wings
                new MysteryItem(11022, 500), // chicken legs
                new MysteryItem(6654, 500), // camo top
                new MysteryItem(6655, 500), // camo bottoms
                new MysteryItem(6656, 500), // camo helmet
                new MysteryItem(20838, 500).announce(), // corrupted helm
                new MysteryItem(20840, 500).announce(), // corrupted plate
                new MysteryItem(20842, 500).announce(), // corrupted legs
                new MysteryItem(20844, 500).announce(), // corrupted skirt
                new MysteryItem(20846, 500).announce(), // corrupted shield
                new MysteryItem(9921, 250), // skeleton boots
                new MysteryItem(9922, 250), // skeleton gloves
                new MysteryItem(9923, 250), // skeleton leggings
                new MysteryItem(9924, 250), // skeleton shirt
                new MysteryItem(9925, 250), // skeleton mask
                new MysteryItem(13317, 500).announce(), // deadman chest
                new MysteryItem(13318, 500).announce(), // deadman legs
                new MysteryItem(13319, 500).announce(), // deadman cape
                new MysteryItem(13283, 250), // gravedigger mask
                new MysteryItem(13284, 250), // gravedigger top
                new MysteryItem(13285, 250), // gravedigger legs
                new MysteryItem(13286, 250), // gravedigger boots
                new MysteryItem(13287, 250), // gravedigger gloves
                new MysteryItem(20773, 250), // banshee mask
                new MysteryItem(20775, 250), // banshee top
                new MysteryItem(20777, 250), // banshee robe
                new MysteryItem(8971, 100).announce(), // phasmatys flag
                new MysteryItem(1419, 125), // scythe
                new MysteryItem(1037, 125).announce(), // bunny ears

                // Evil Chicken = 50
                new MysteryItem(20433, 125), // evil chicken feet
                new MysteryItem(20436, 125).announce(), // evil chicken wings
                new MysteryItem(20439, 125).announce(), // evil chicken head
                new MysteryItem(20442, 125).announce(), // evil chicken legs

                new MysteryItem(23185, 75).announce(), // ring of 3a

                // Custom Partyhats = 25
                new MysteryItem(32066, 100).announce(), // pink phat
                new MysteryItem(32068, 100).announce(), // orange phat

                // Extremely Rare
                new MysteryItem(21859, 50).announce(), // wise old man santa
                new MysteryItem(13344, 50).announce(), // inverted santa
                new MysteryItem(13343, 50).announce(), // black santa
                new MysteryItem(11847, 50).announce(), // black h'ween
                new MysteryItem(5607, 50).announce(), // grain
                new MysteryItem(25500, 50).announce(), // cursed banana
                new MysteryItem(24525, 50).announce(), // cat ears
                new MysteryItem(24527, 50).announce(), // hell-cat ears
                new MysteryItem(4565, 50).announce(), // easter basket
                new MysteryItem(26649, 50).announce(), // skis
                new MysteryItem(8871, 50).announce(), // zanik crate
                new MysteryItem(22840, 50).announce(), // golden tench

                // Uber Rare
                new MysteryItem(11863, 25).announce(), // rainbow phat
                new MysteryItem(11862, 25).announce(), // black phat
        };

        totalWeight += MysteryItem.calculateTotalWeight(rewards);
    }

    @Override
    public int[] getItems() {
        return new int[] {CustomItemId.COSMETIC_MYSTERY_BOX};
    }
}
