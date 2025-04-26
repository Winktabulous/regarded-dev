package com.zenyte.game.content.skills.farming.actions;

import com.zenyte.game.content.skills.farming.*;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.utils.Articles;

import java.util.Optional;

import static com.zenyte.game.content.skills.farming.PatchFlag.WATCHED_OVER;
import static com.zenyte.game.content.skills.farming.PatchState.*;

/**
 * @author Kris | 07/02/2019 15:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Inspecting extends Action {
    public Inspecting(final FarmingSpot spot) {
        this.spot = spot;
    }

    private final FarmingSpot spot;

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        final StringBuilder builder = new StringBuilder();
        final String name = spot.getPatch().getType().getSanitizedName();
        final FarmingProduct product = spot.getProduct();
        final PatchType type = spot.getPatch().getType();
        if (type == PatchType.GRAPEVINE_PATCH) {
            builder.append("This is a grapevine patch. ");
            final int value = spot.getValue();
            if (value == 0) {
                builder.append("The patch is empty.");
            } else if (value == 1) {
                builder.append("The patch has been treated with saltpetre.");
            }
            final PatchState state = spot.getState();
            if (state == GROWING || state == WATERED || state == HEALTH_CHECK) {
                builder.append("The patch has something growing in it.");
            }
            player.sendMessage(builder.toString());
            player.sendMessage("These patches are automatically protected.");
            return -1;
        }
        builder.append("This is ").append(Articles.prepend(name)).append(". ");
        if (type != PatchType.HESPORI_PATCH) {
            final Optional<PatchFlag> compost = spot.getCompostFlag();
            if (compost.isPresent()) {
                builder.append("The soil has been treated with ").append(compost.get().toString().toLowerCase()).append(". ");
            } else {
                builder.append("The soil has not been treated. ");
            }
        }
        if (product == FarmingProduct.WEEDS) {
            if (spot.getValue() == 3) {
                builder.append("The patch is empty and weeded.");
            } else {
                builder.append("The patch needs weeding.");
            }
        } else if (product != FarmingProduct.SCARECROW) {
            final PatchState state = product.getState(spot.getValue());
            if (state == GROWN || state == REGAINING_PRODUCE || state == HEALTH_CHECK) {
                if (product == FarmingProduct.HESPORI & spot.getValue() == 8) {
                    builder.append("The plant has been killed and can now be dug up.");
                } else {
                    builder.append("The patch is fully grown.");
                }
            } else if (state == DISEASED) {
                builder.append("The patch is diseased and needs attending to before it dies.");
            } else if (state == DEAD) {
                builder.append("The patch has become infected by disease and has died.");
            } else if (state == GROWING || state == WATERED) {
                builder.append("The patch has something growing in it.");
            } else if (state == STUMP) {
                builder.append("The patch has the remains of a tree stump in it.");
            }
        }
        player.sendMessage(builder.toString());
        if (spot.containsFlag(WATCHED_OVER)) {
            player.sendMessage("A nearby gardener is looking after this patch for you.");
        }
        return -1;
    }
}
