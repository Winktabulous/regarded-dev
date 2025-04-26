package com.zenyte.game.world.object;

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary;
import com.zenyte.game.content.minigame.blastfurnace.BlastFurnaceArea;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FillContainer extends Action {

    private final WorldObject object;

    private final Item item;

    private FillCombinations combo;

    private int count = 0;

    private static final SoundEffect sound = new SoundEffect(2609, 0, 0);

    public FillContainer(final WorldObject object, final Item item) {
        this.object = object;
        this.item = item;
    }

    @Override
    public boolean start() {
        combo = FillCombinations.getDef(WorldObjectUtils.getObjectNameOfPlayer(object, player).toLowerCase());
        if (!check()) {
            return false;
        }
        return check();
    }

    @Override
    public boolean process() {
        return check();
    }

    @Override
    public int processWithDelay() {
        reward();
        return 0;
    }

    @Override
    public void stop() {
    }

    public static boolean tryAction(final Player player, final WorldObject object, final Item item) {
        final String name = object.getName().toLowerCase();
        if (FillCombinations.combos.containsKey(name)) {
            if (FillCombinations.getDef(name).getEmpty().contains(item.getId())) {
                player.getActionManager().setAction(new FillContainer(object, item));
                return true;
            }
        }
        return false;
    }

    private void reward() {
        final String name = combo == FillCombinations.FLOUR_BIN ? WorldObjectUtils.getObjectNameOfPlayer(object, player) : object.getName();
        player.getInventory().deleteItem(item);
        player.getInventory().addItem(FillCombinations.getReward(name.toLowerCase(), item.getId()), 1);
        player.getPacketDispatcher().sendSoundEffect(sound);
        if (combo.getAnim() != null) {
            player.setAnimation(combo.getAnim());
        }
        if (combo.getMessage().isEmpty()) {
            if (combo == FillCombinations.FLOUR_BIN) {
                player.addAttribute("flourbin", player.getNumericAttribute("flourbin").intValue() - 1);
                if (player.getNumericAttribute("flourbin").intValue() > 0) {
                    player.sendFilteredMessage("You fill the pot with flour from the bin.");
                } else {
                    player.sendFilteredMessage("You fill the pot with the last of the flour from the bin.");
                    player.getVarManager().sendBit(5325, 0);
                }
            }

            if (combo == FillCombinations.FILL_WATER) {
                if (item.getName().startsWith("Watering can")) {
                    if (object.getName().toLowerCase().contains("well")) {
                        player.getDialogueManager().start(new PlainChat(player, "If I drop my watering can down there" +
                                " I don't think I'm likely to get it back."));
                        return;
                    }
                }
                if (item.getId() == 1925) {
                    player.getAchievementDiaries().update(FremennikDiary.FILL_A_BUCKET);
                    player.getAchievementDiaries().update(FaladorDiary.FILL_A_BUCKET);
                }
                player.sendFilteredMessage("You fill the " + (item.getId() == 20800 ? "gourd vial" : item.getName().replaceAll(" .*", "").toLowerCase()) + " from the " + object.getName().toLowerCase() + ".");
            }
            return;
        } else {
            player.sendMessage(combo.getMessage());
        }

        count++;
    }

    private boolean checkObject() {
        return World.getRegion(object.getRegionId()).containsObject(object.getId(), object.getType(), object);
    }

    private boolean check() {
        if (player.isDead() || player.isFinished() || !checkObject())
            return false;

        if (!player.getInventory().containsItem(item) || combo == null)
            return false;

        if (combo == FillCombinations.FLOUR_BIN && player.getNumericAttribute("flourbin").intValue() == 0)
            return false;

        return !combo.isAutofill() || count == 0;
    }

    public enum FillCombinations {

        FILL_WATER("", Animation.GRAB,
                new Integer[][] { { 229, 1923, 1925, 1935, 20800, 5331, 5333, 5334, 5335, 5336, 5337, 5338, 5339, 1831, 1829, 1827, 1825 },
                        { 227, 1921, 1929, 1937, 20801, 5340, 5340, 5340, 5340, 5340, 5340, 5340, 5340, 1823, 1823, 1823, 1823 } },
                "well", "fountain", "sink", "geyser", "water barrel", "waterpump", "pump and drain", "tap"),
        FLOUR_BIN("", new Animation(832), new Integer[][] { { 1931 }, { 1933 } }, "flour bin"),
        FILL_MILK("You milk the cow.", new Animation(2305), new Integer[][] { { 1925 }, { 1927 } }, "dairy cow"),
        SPADE_COKE("You take a spadeful of coke.", BlastFurnaceArea.TAKE_COKE, new Integer[][] { { 952 }, { 6448 } }, false, "coke"),
        ;

        private final String message;
        private final Animation anim;
        private final Integer[][] containers;
        private final boolean autofill;
        private final String[] names;

        public static final FillCombinations[] VALUES = values();

        public static Map<String, FillCombinations> combos = new HashMap<String, FillCombinations>();

        static {
            for (final FillCombinations entry : FillCombinations.VALUES) {
                for (final String name : entry.getNames()) {
                    combos.put(name, entry);
                }
            }
        }

        FillCombinations(final String message, final Animation anim, final Integer[][] containers, final String... names) {
            this(message, anim, containers, true, names);
        }

        FillCombinations(final String message, final Animation anim, final Integer[][] containers, final boolean autofill, final String... names) {
            this.message = message;
            this.anim = anim;
            this.containers = containers;
            this.autofill = autofill;
            this.names = names;
        }


        public List<Integer> getEmpty() {
            return Arrays.asList(containers[0]);
        }

        public static FillCombinations getDef(final String name) {
            return combos.get(name);
        }

        public static int getReward(final String name, final int id) {
            final FillCombinations combo = combos.get(name);
            if (combo == null) {
                return -1;
            }
            for (int i = 0; i < combo.getContainers()[0].length; i++) {
                if (combo.getContainers()[0][i] == id) {
                    return combo.getContainers()[1][i];
                }
            }
            return -1;
        }

        public String getMessage() {
            return message;
        }

        public Animation getAnim() {
            return anim;
        }

        public Integer[][] getContainers() {
            return containers;
        }

        public boolean isAutofill() {
            return autofill;
        }

        public String[] getNames() {
            return names;
        }
    }
}
