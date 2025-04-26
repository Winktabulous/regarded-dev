package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.content.skills.prayer.actions.Bones;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kris | 23/06/2019 12:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BoneGrinding extends Action {
    private static final Animation turn = new Animation(1648);
    private static final Animation deposit = new Animation(1649);
    private static final Animation withdraw = new Animation(1650);


    enum Bonemeal {
        REGULAR_BONEMEAL(Bones.BONES, 4255), BAT_BONEMEAL(Bones.BAT_BONES, 4256), BIG_BONEMEAL(Bones.BIG_BONES, 4257), BURNT_BONEMEAL(Bones.BURNT_BONES, 4258), BURNT_JOGRE_BONEMEAL(Bones.BURNT_JOGRE_BONES, 4259), BABYDRAGON_BONEMEAL(Bones.BABYDRAGON_BONES, 4260), DRAGON_BONEMEAL(Bones.DRAGON_BONES, 4261), WOLF_BONEMEAL(Bones.WOLF_BONES, 4262), MONKEY_BONEMEAL(Bones.MONKEY_BONES, 4267), JOGRE_BONEMEAL(Bones.JOGRE_BONES, 4271), ZOGRE_BONEMEAL(Bones.ZOGRE_BONES, 4852), FAYRG_BONEMEAL(Bones.FAYRG_BONES, 4853), RAURG_BONEMEAL(Bones.RAURG_BONES, 4854), OURG_BONEMEAL(Bones.OURG_BONES, 4855), SHAIKAHAN_BONEMEAL(Bones.SHAIKAHAN_BONES, 5615), DAGANNOTH_BONEMEAL(Bones.DAGANNOTH_BONES, 6728), WYVERN_BONEMEAL(Bones.WYVERN_BONES, 6810), LAVA_DRAGON_BONEMEAL(Bones.LAVA_DRAGON_BONES, 11922), SUPERIOR_DRAGON_BONEMEAL(Bones.SUPERIOR_DRAGON_BONES, 22116), WYRM_BONEMEAL(Bones.WYRM_BONES, 22754), DRAKE_BONEMEAL(Bones.DRAKE_BONES, 22756), HYDRA_BONEMEAL(Bones.HYDRA_BONES, 22758);
        static final Bonemeal[] values = values();
        private final Bones bones;
        private final int bonemeal;

        Bonemeal(Bones bones, int bonemeal) {
            this.bones = bones;
            this.bonemeal = bonemeal;
        }

        public Bones getBones() {
            return bones;
        }

        public int getBonemeal() {
            return bonemeal;
        }
    }

    BoneGrinding(@NotNull final Stage stage, @Nullable final Item bones) {
        this.stage = stage;
        this.bones = bones;
    }

    private Stage stage;
    private Item bones;


    enum Stage {
        ADDING_BONES, GRINDING, COLLECTING
    }

    @Override
    public boolean start() {
        final int status = player.getNumericAttribute("ectofuntus bone status").intValue();
        final int boneId = player.getNumericAttribute("ectofuntus grinded bone").intValue();
        switch (stage) {
        case ADDING_BONES: 
            if (status == 1) {
                player.sendMessage("You need to grind the bones already inside the grinder first.");
                return false;
            } else if (boneId != 0) {
                player.sendMessage("You need to collect the ground bones from the bin first.");
                return false;
            }
            break;
        case GRINDING: 
            if (status == 0 && boneId != 0) {
                player.sendMessage("You need to collect the ground bones from the bin first.");
                return false;
            }
            if (status == 0) {
                player.sendMessage("You have to add some bones in the loader first.");
                return false;
            }
            break;
        case COLLECTING: 
            if (status == 1) {
                player.sendMessage("You need to grind the bones already inside the grinder first.");
                return false;
            }
            if (boneId == 0) {
                player.sendMessage("There's no bonemeal in the bin right now.");
                return false;
            }
            break;
        }
        if (bones == null) {
            final int id = player.getNumericAttribute("ectofuntus grinded bone").intValue();
            if (id == 0) {
                return false;
            }
            bones = new Item(id);
        }
        return true;
    }

    @Override
    public boolean process() {
        switch (stage) {
        case ADDING_BONES: 
            if (!player.getInventory().containsItem(bones)) {
                return false;
            }
            break;
        case GRINDING: 
            if (player.getNumericAttribute("ectofuntus bone status").intValue() != 1) {
                return false;
            }
            break;
        case COLLECTING: 
            if (player.getNumericAttribute("ectofuntus grinded bone").intValue() == 0) {
                return false;
            }
            if (!player.getInventory().containsItem(1931, 1)) {
                player.sendMessage("You need an empty pot to collect the bonemeal.");
                return false;
            }
            break;
        }
        return true;
    }

    @Override
    public int processWithDelay() {
        final BoneGrinding action = this;
        final BoneGrinding.Stage stage = this.stage;
        this.stage = stage == Stage.ADDING_BONES ? Stage.GRINDING : stage == Stage.GRINDING ? Stage.COLLECTING : Stage.ADDING_BONES;
        switch (stage) {
        case ADDING_BONES: 
            player.setFaceLocation(new Location(3660, 3525, 1));
            player.setAnimation(deposit);
            player.getInventory().deleteItem(bones);
            player.addAttribute("ectofuntus bone status", 1);
            player.addAttribute("ectofuntus grinded bone", bones.getId());
            WorldTasksManager.schedule(() -> {
                if (player.getActionManager().getAction() != action) {
                    return;
                }
                player.addWalkSteps(3659, 3524);
            }, 3);
            return 4;
        case GRINDING: 
            player.setFaceLocation(new Location(3659, 3525, 1));
            player.setAnimation(turn);
            player.getAttributes().remove("ectofuntus bone status");
            WorldTasksManager.schedule(() -> {
                if (player.getActionManager().getAction() != action) {
                    return;
                }
                player.addWalkSteps(3658, 3524);
            }, 3);
            return 4;
        case COLLECTING: 
            final int boneId = player.getNumericAttribute("ectofuntus grinded bone").intValue();
            final BoneGrinding.Bonemeal bonemeal = CollectionUtils.findMatching(Bonemeal.values, meal -> CollectionUtils.findMatching(meal.bones.getItems(), b -> b.getId() == boneId) != null);
            if (bonemeal == null) {
                return -1;
            }
            player.setFaceLocation(new Location(3658, 3525, 1));
            player.setAnimation(withdraw);
            player.getInventory().deleteItem(1931, 1);
            player.getInventory().addOrDrop(new Item(bonemeal.getBonemeal()));
            player.getAttributes().remove("ectofuntus grinded bone");
            WorldTasksManager.schedule(() -> {
                if (player.getActionManager().getAction() != action) {
                    return;
                }
                player.addWalkSteps(3660, 3524);
            }, 3);
            return 4;
        }
        return -1;
    }
}
