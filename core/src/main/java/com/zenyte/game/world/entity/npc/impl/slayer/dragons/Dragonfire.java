package com.zenyte.game.world.entity.npc.impl.slayer.dragons;

import com.google.common.collect.ImmutableList;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 30/10/2018 17:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Dragonfire {

    public static abstract class DragonfireBuilder extends Dragonfire {
        public DragonfireBuilder(final DragonfireType type, final int maximumDamage, final List<DragonfireProtection> protection) {
            this(type, maximumDamage, protection, DEFAULT_MSG_BUILDER);
        }

        public DragonfireBuilder(final DragonfireType type, final int maximumDamage, final List<DragonfireProtection> protection, final MessageBuilder messageBuilder) {
            super(type, maximumDamage, protection, messageBuilder);
        }

        @Override
        public abstract int getDamage();

        public float getAccumulativeTier() {
            float accumulativeTier = 0;
            for (final DragonfireProtection protection : affectedProtections) {
                accumulativeTier += protection.protectionTier;
            }
            return accumulativeTier;
        }
    }

    public Dragonfire(final DragonfireType type, final int maximumDamage, final List<DragonfireProtection> protection) {
        this(type, maximumDamage, protection, DEFAULT_MSG_BUILDER);
    }

    public Dragonfire(final DragonfireType type, final int maximumDamage, final List<DragonfireProtection> protection, final MessageBuilder messageBuilder) {
        this.maximumDamage = maximumDamage;
        this.type = type;
        this.protection = protection;
        this.messageBuilder = messageBuilder;
        filter();
    }

    /**
     * Filters the protections list by removing the ones that do not affect the dragonfire type,
     * and sorts the list in a descending order depending on the protection tier.
     * Creates an immutable list of the sorted protections.
     */
    private void filter() {
        final ArrayList<DragonfireProtection> list = new ArrayList<DragonfireProtection>(3);
        for (final DragonfireProtection protection : this.protection) {
            if (ArrayUtils.contains(type.acceptableProtections, protection)) {
                list.add(protection);
            }
        }
        list.sort((c1, c2) -> Float.compare(c2.protectionTier, c1.protectionTier));
        affectedProtections = ImmutableList.copyOf(list);
    }

    private final int maximumDamage;
    protected final DragonfireType type;
    final List<DragonfireProtection> protection;
    List<DragonfireProtection> affectedProtections;
    int reducedDamage = -1;
    String protectionMessage;
    final MessageBuilder messageBuilder;

    public void backfire(@NotNull final NPC target, Player player, final int delay, final int damage) {
        WorldTasksManager.scheduleOrExecute(() -> target.applyHit(new Hit(damage, HitType.REGULAR)), delay);
    }

    /**
     * Gets the maximum damage inflictable by the dragonfire after applying all available dragonfire protections.
     * @return maximum damage inflictable.
     */
    public int getDamage() {
        if (reducedDamage != -1) {
            return reducedDamage;
        }
        float accumulativeTier = 0;
        for (final DragonfireProtection protection : affectedProtections) {
            accumulativeTier += protection.protectionTier;
        }
        reducedDamage = (int) (accumulativeTier >= type.fullProtectionTier ? 0 : (maximumDamage * (1 - (accumulativeTier / type.fullProtectionTier))));
        return reducedDamage;
    }

    /**
     * Gets the message sent to the player when dragonfire damage is inflicted. Supports custom messagebuilder due to
     * all of the inconsistencies.
     * @return dragonfire message.
     */
    public final String getMessage() {
        if (protectionMessage != null) {
            return protectionMessage;
        }
        final StringBuilder builder = new StringBuilder(100);
        messageBuilder.build(affectedProtections, (float) getDamage() / maximumDamage, builder);
        return protectionMessage = builder.toString();
    }

    /**
     * The default dragonfire message builder; requires inputting the name of the attack through formatting.
     */
    private static final MessageBuilder DEFAULT_MSG_BUILDER = (protections, percentage, builder) -> {
        if (protections.isEmpty()) {
            builder.append("You are horribly burnt by the %s.");
        } else {
            builder.append("Your ");
            builder.append(protections.get(0).protectionName);
            if (percentage == 0) {
                builder.append(" fully");
            } else if (percentage >= 0.75F) {
                builder.append(" slightly");
            }
            builder.append(" protects you from the %s.");
        }
    };


    /**
     * Messagebuilder to allow custom dragonfire messages.
     */
    @FunctionalInterface
    public interface MessageBuilder {
        void build(List<DragonfireProtection> protections, float percentage, final StringBuilder builder);
    }

    public int getMaximumDamage() {
        return maximumDamage;
    }
}
