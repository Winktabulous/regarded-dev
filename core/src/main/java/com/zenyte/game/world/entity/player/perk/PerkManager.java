package com.zenyte.game.world.entity.player.perk;

import com.zenyte.game.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommeh | 3-1-2019 | 19:54
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class PerkManager {
    private static final Logger log = LoggerFactory.getLogger(PerkManager.class);
    private final transient Player player;
    private Map<PerkWrapper, Perk> perks;

    public PerkManager(final Player player) {
        this.player = player;
        perks = new HashMap<>();
    }

    public void initialize(final PerkManager manager) {
        if (manager != null && manager.perks != null) {
            perks = manager.perks;
        }
    }

    private Perk getPerk(final PerkWrapper wrapper) {
        final Class<? extends Perk> parent = wrapper.getPerk();
        try {
            return parent.getDeclaredConstructor().newInstance();
        } catch (final Exception e) {
            log.error("", e);
        }
        throw new RuntimeException("Failure constructing perk.");
    }

    public boolean unlock(final PerkWrapper wrapper) {
        final Perk perk = getPerk(wrapper);
        if (perks.containsKey(wrapper)) {
            player.sendMessage("You already have the <col=00080>" + perk.getName() + "</col> perk unlocked.");
            return false;
        }
        perks.put(PerkWrapper.get(perk.getClass()), perk);
        player.sendMessage("You have unlocked the <col=00080>" + perk.getName() + "</col> perk!");
        return true;
    }

    public Perk get(final PerkWrapper wrapper) {
        return perks.get(wrapper);
    }

    public void consume(final PerkWrapper wrapper) {
        final Perk perk = getPerk(wrapper);
        perk.consume(player);
    }

    public boolean isValid(final PerkWrapper wrapper) {
        if (perks.containsKey(wrapper)) {
            final Perk perk = getPerk(wrapper);
            return perk.valid();
        }
        return false;
    }

    public boolean ifValidConsume(final PerkWrapper wrapper) {
        if (perks.containsKey(wrapper)) {
            final Perk perk = getPerk(wrapper);
            if (perk.valid()) {
                perk.consume(player);
                return true;
            }
        }
        return false;
    }

    public Player getPlayer() {
        return player;
    }

    public Map<PerkWrapper, Perk> getPerks() {
        return perks;
    }
}
