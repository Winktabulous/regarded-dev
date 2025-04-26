package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.skills.farming.FarmingSpot;
import com.zenyte.game.content.skills.farming.PatchFlag;
import com.zenyte.game.content.skills.farming.PatchState;
import com.zenyte.game.content.skills.farming.PatchType;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

/**
 * @author Kris | 03/02/2019 18:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Saturating extends Action {
    private static final Animation SATURATION_ANIMATION = new Animation(2283);
    private static final SoundEffect SATURATION_SOUND = new SoundEffect(2427, 0, 0);
    private static final Item BUCKET = new Item(1925);
    public static final PatchState[] allowedStates = new PatchState[] {PatchState.WEEDS, PatchState.GROWING, PatchState.WATERED, PatchState.DISEASED};

    public Saturating(final FarmingSpot spot, final Item item) {
        this.spot = spot;
        this.item = item;
    }

    private final FarmingSpot spot;
    private final Item item;

    @Override
    public boolean start() {
        final PatchState state = spot.getState();
        if (!ArrayUtils.contains(allowedStates, state)) {
            player.sendMessage("Composting isn't going to make it get any bigger.");
            return false;
        }
        if (spot.getPatch().getType() == PatchType.HESPORI_PATCH) {
            player.sendMessage("The patch won't benefit from that kind of treatment.");
            return false;
        }
        final Optional<PatchFlag> compost = spot.getCompostFlag();
        if (compost.isPresent()) {
            final PatchFlag flag = getCompostType(item);
            assert flag != null;
            //We allow re-saturating plants with better compost types.
            if (flag.ordinal() <= compost.get().ordinal()) {
                player.sendMessage("This " + spot.getPatch().getType().getSanitizedName() + " has already been treated with " + compost.get().toString().toLowerCase() + ".");
                return false;
            }
        }
        if (item.getId() == 22994 || item.getId() == 22997) {
            if ((item.getCharges() & 65535) == 0) {
                player.sendMessage("Your bottomless compost bucket hasn't got any compost in it!");
                return false;
            }
        }
        player.setAnimation(SATURATION_ANIMATION);
        player.getPacketDispatcher().sendSoundEffect(SATURATION_SOUND);
        delay(3);
        player.lock(3);
        return true;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public void stop() {
        this.delay(3);
    }

    @Override
    public int processWithDelay() {
        final PatchFlag flag = getCompostType(item);
        assert flag != null;
        player.getSkills().addXp(SkillConstants.FARMING, flag == PatchFlag.COMPOST ? 18 : flag == PatchFlag.SUPERCOMPOST ? 26 : 36);
        if (item.getId() == 22994 || item.getId() == 22997) {
            item.setCharges((item.getCharges() >> 16) << 16 | ((item.getCharges() & 65535) - 1));
            if ((item.getCharges() & 65535) == 0) {
                item.setCharges(0);
                item.setId(22994);
                player.getInventory().refreshAll();
            }
        } else {
            player.getInventory().deleteItem(item);
            player.getInventory().addItem(BUCKET);
        }
        player.sendFilteredMessage("You treat the " + spot.getPatch().getType().getSanitizedName() + " with " + flag.name().toLowerCase() + ".");
        //Remove existing spot flags.
        spot.getFlags().removeIf(f -> (f == PatchFlag.COMPOST || f == PatchFlag.SUPERCOMPOST || f == PatchFlag.ULTRACOMPOST));
        spot.setFlag(flag);
        return -1;
    }

    private final PatchFlag getCompostType(final Item item) {
        final int id = item.getId();
        if (id == 22994 || id == 22997) {
            final int type = (item.getCharges() >> 16);
            return type == 0 ? PatchFlag.COMPOST : type == 1 ? PatchFlag.SUPERCOMPOST : PatchFlag.ULTRACOMPOST;
        }
        return id == 6032 ? PatchFlag.COMPOST : id == 6034 ? PatchFlag.SUPERCOMPOST : id == 21483 ? PatchFlag.ULTRACOMPOST : null;
    }
}
