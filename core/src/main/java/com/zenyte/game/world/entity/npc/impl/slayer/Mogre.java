package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.TimeUnit;

import java.util.Optional;

/**
 * @author Kris | 10/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Mogre extends NPC implements Spawnable {
    private static final int TOTAL_WEIGHT;
    private String username;
    private static final MogreQuote[] quotes = new MogreQuote[] {new MogreQuote(11, new ForceTalk("Human hit me on the head!")), new MogreQuote(20, new ForceTalk("Human scare all da fishies!")), new MogreQuote(23, new ForceTalk("I get you!")), new MogreQuote(18, new ForceTalk("I smack you good!")), new MogreQuote(25, new ForceTalk("Smash stupid human!")), new MogreQuote(18, new ForceTalk("Tasty human!")), new MogreQuote(3, new ForceTalk("Da boom-boom kill all da fishies!"))};

    static {
        int weight = 0;
        for (final Mogre.MogreQuote quote : quotes) {
            weight += quote.weight;
        }
        TOTAL_WEIGHT = weight;
    }

    private Optional<ForceTalk> getPseudoRandomForceTalk() {
        final int roll = Utils.random(TOTAL_WEIGHT);
        int current = 0;
        for (final Mogre.MogreQuote quote : quotes) {
            if ((current += quote.weight) >= roll) {
                return Optional.of(quote.message);
            }
        }
        return Optional.empty();
    }

    public Mogre(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.spawned = true;
    }

    private int ticks = 0;

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAttackable(final Entity e) {
        if (username == null || e instanceof NPC) {
            return true;
        }
        if (!((Player) e).getUsername().equalsIgnoreCase(username)) {
            ((Player) e).sendMessage("You can't kill someone else's mogre.");
            return false;
        }
        return true;
    }

    @Override
    public NPC spawn() {
        getPseudoRandomForceTalk().ifPresent(this::setForceTalk);
        return super.spawn();
    }

    @Override
    public void onFinish(final Entity source) {
        super.onFinish(source);
        if (username == null) {
            return;
        }
        World.getPlayer(username).ifPresent(player -> player.getTemporaryAttributes().remove("Is mogre spawned"));
    }

    @Override
    public void processNPC() {
        if (getAttackingDelay() < System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(5)) {
            if (++ticks >= 100) {
                finish();
                return;
            }
        } else {
            ticks = 0;
        }
        super.processNPC();
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 2592;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    private static class MogreQuote {
        private final int weight;
        private final ForceTalk message;

        public MogreQuote(int weight, ForceTalk message) {
            this.weight = weight;
            this.message = message;
        }

        public int getWeight() {
            return weight;
        }

        public ForceTalk getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "Mogre.MogreQuote(weight=" + this.getWeight() + ", message=" + this.getMessage() + ")";
        }

        @Override
        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof Mogre.MogreQuote)) return false;
            final Mogre.MogreQuote other = (Mogre.MogreQuote) o;
            if (!other.canEqual(this)) return false;
            if (this.getWeight() != other.getWeight()) return false;
            final Object this$message = this.getMessage();
            final Object other$message = other.getMessage();
            return this$message == null ? other$message == null : this$message.equals(other$message);
        }

        protected boolean canEqual(final Object other) {
            return other instanceof Mogre.MogreQuote;
        }

        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + this.getWeight();
            final Object $message = this.getMessage();
            result = result * PRIME + ($message == null ? 43 : $message.hashCode());
            return result;
        }
    }
}
