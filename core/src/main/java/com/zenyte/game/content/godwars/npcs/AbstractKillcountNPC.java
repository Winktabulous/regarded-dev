package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.content.godwars.instance.GodwarsInstance;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combatdefs.AggressionType;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.area.godwars.GodwarsDungeonArea;
import com.zenyte.game.world.region.area.wilderness.WildernessGodwarsDungeon;

public abstract class AbstractKillcountNPC extends NPC {

    private transient GodType type;
    private boolean isInGodwars;
    private boolean isInWildernessGodwars;

    public AbstractKillcountNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        if (tile == null) return;
        this.isInWildernessGodwars = WildernessGodwarsDungeon.polygon.contains(tile);
        this.isInGodwars = GodwarsDungeonArea.polygon.contains(tile)
                || isInWildernessGodwars
                || GlobalAreaManager.getArea(tile) instanceof GodwarsInstance;

        if (isInGodwars) {
            setTargetType(EntityType.BOTH);
        }
        this.type = (id >= 2233 && id <= 2249 && id != 2244) ? GodType.BANDOS
                : (id >= 2209 && id <= 2214 && id != 2212) ? GodType.SARADOMIN
                : (id >= 3133 && id <= 3141 || id == 3159 || id == 3160) ? GodType.ZAMORAK
                : (id == NpcId.SPIRITUAL_RANGER_11291 || id == NpcId.SPIRITUAL_MAGE_11292 || id == NpcId.SPIRITUAL_WARRIOR_11290 || id == NpcId.BLOOD_REAVER)
                ? GodType.ANCIENT
                : GodType.ARMADYL;
    }

    public GodType type() {
        return type;
    }

    @Override
    public void setCombatDefinitions(final NPCCombatDefinitions definitions) {
        super.setCombatDefinitions(definitions);
        if (!isInGodwars) {
            return;
        }
        if (!(this instanceof Bloodveld)) {
            combatDefinitions.setAggressionType(AggressionType.AGGRESSIVE);
        }
    }

    @Override
    public boolean isTolerable() {
        if (!isInGodwars) {
            return super.isTolerable();
        }
        return false;
    }


    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (isInWildernessGodwars || !isInGodwars || !(source instanceof final Player player)) {
            return;
        }
        type().addKillcount(player, getKillCountAmount(type, this.getId()));
    }


    @Override
    public boolean isAcceptableTarget(final Entity target) {
        if (!isInGodwars) {
            return super.isAcceptableTarget(target);
        }
        if (target instanceof Player) {
            return !type().isWieldingProtectiveItem((Player) target);
        }
        if (!(target instanceof SpawnableKillcountNPC)) return false;
        return type() != ((SpawnableKillcountNPC) target).type();
    }

    public final void setInGodwars(boolean inGodwars) {
        isInGodwars = inGodwars;
    }

    private static int getKillCountAmount(GodType type, int npcID) {
        return switch (type) {
            case BANDOS, ZAMORAK, SARADOMIN, ARMADYL -> 1;
            case ANCIENT -> switch (npcID) {
                case NpcId.SPIRITUAL_MAGE_11292 -> 5 + (Utils.randomBoolean(4) ? 1 : 0);
                case NpcId.SPIRITUAL_RANGER_11291, NpcId.SPIRITUAL_WARRIOR_11290 -> 2;
                case NpcId.BLOOD_REAVER -> 3;
                case NpcId.BLOOD_REAVER_11294 -> 3 + (Utils.randomBoolean(5) ? 1 : 0);
                default -> 1;
            };
        };
    }
}
