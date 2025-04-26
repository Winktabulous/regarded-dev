package com.zenyte.game.world.entity.npc.impl.motherlode;

import com.zenyte.game.content.minigame.motherlode.MotherlodeArea;
import com.zenyte.game.content.minigame.motherlode.Paydirt;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

import java.util.List;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class PaydirtNPC extends NPC {
    private static final Location END_TILE = new Location(3748, 5660, 0);
    private final Player player;
    private final List<Item> paydirt;

    public PaydirtNPC(final int id, final Location tile, final Direction facing, final int radius, final Player player, final List<Item> paydirt) {
        super(id, tile, facing, radius);
        this.player = player;
        this.paydirt = paydirt;
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (getY() == 5660) {
            if (!player.isNulled()) {
                this.paydirt.forEach(item -> {
                    final int id = item.getNumericAttribute("paydirt ore id").intValue();
                    if (id == 0) {
                        Paydirt.generate(player).ifPresent(pd -> {
                            player.getPaydirt().add(pd.getId());
                            player.getSkills().addXp(SkillConstants.MINING, pd.getXp());
                        });
                    } else {
                        player.getPaydirt().add(id);
                        Paydirt.get(id).ifPresent(paydirt -> player.getSkills().addXp(SkillConstants.MINING, paydirt.getXp()));
                    }
                });
                player.getVarManager().sendBit(5558, player.getPaydirt().size());
                final int maxPaydirt = (player.getBooleanAttribute("motherlode_sack_upgrade") ? 162 : 81);
                final int paydirtAmt = player.getPaydirt().size();
                if (paydirtAmt + 28 >= maxPaydirt) player.sendMessage("Some ore is ready to be collected from the sack. It's getting full.");
                 else player.sendMessage("Some ore is ready to be collected from the sack.");
            }
            finish();
        }
        if ((MotherlodeArea.WATER_WHEELS.get(true) || MotherlodeArea.WATER_WHEELS.get(false))) addWalkSteps(END_TILE.getX(), END_TILE.getY(), -1, false);
         else resetWalkSteps();
    }

    /**
     * Calculation for paydirt message upon entry into the sack.
     */
    public Player getPlayer() {
        return player;
    }

    public List<Item> getPaydirt() {
        return paydirt;
    }
}
