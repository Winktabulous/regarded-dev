package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.content.follower.Pet;
import com.zenyte.game.content.follower.impl.SkillingPet;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;

import java.util.*;

import static com.zenyte.game.content.follower.impl.SkillingPet.*;

/**
 * @author Tommeh | 27-3-2019 | 14:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class OreOnRockGolemPetNPCAction implements ItemOnNPCAction {
    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        final OreOnRockGolemPetNPCAction.RockGolemCombination combination = RockGolemCombination.get(item.getId());
        if (combination == null) {
            return;
        }
        if (player.getFollower() == null || player.getFollower().getPet() == null || !SkillingPet.isRockGolem(player.getFollower().getPet())) {
            return;
        }
        final Pet currentPet = player.getFollower().getPet();
        if (currentPet.petId() == combination.getPet().petId()) {
            player.sendMessage("The appearance of your pet is already set on " + combination + ".");
            return;
        }
        player.getInventory().deleteItem(item);
        player.setPetId(combination.getPet().getPetId());
        player.getFollower().setTransformation(combination.getPet().getPetId());
    }

    @Override
    public Object[] getItems() {
        final ArrayList<Object> list = new ArrayList<Object>();
        list.addAll(RockGolemCombination.MAP.keySet());
        return list.toArray(new Object[list.size()]);
    }

    @Override
    public Object[] getObjects() {
        return SKILLING_PETS_BY_SKILL.get(SkillConstants.MINING).stream().map(SkillingPet::getPetId).toArray();
    }


    private enum RockGolemCombination {
        DEFAULT(ROCK_GOLEM_DEFAULT, 1480), TIN(ROCK_GOLEM_TIN, 438), COPPER(ROCK_GOLEM_COPPER, 436), IRON(ROCK_GOLEM_IRON, 440), BLURITE(ROCK_GOLEM_BLURITE, 668), SILVER(ROCK_GOLEM_SILVER, 442), COAL(ROCK_GOLEM_COAL, 453), GOLD(ROCK_GOLEM_GOLD, 444), MITHRIL(ROCK_GOLEM_MITHRIL, 447), GRANITE(ROCK_GOLEM_GRANITE, 6979, 6981, 6983), ADAMANTITE(ROCK_GOLEM_ADAMANTITE, 449), RUNITE(ROCK_GOLEM_RUNITE, 451), AMETHYST(ROCK_GOLEM_AMETHYST, 21347), LOVAKITE(ROCK_GOLEM_LOVAKITE, 13356), ELEMENTAL(ROCK_GOLEM_ELEMENTAL, 2892), DAEYALT(ROCK_GOLEMD_DAEYALT, 9632);
        private final SkillingPet pet;
        private final int[] ores;
        private static final Set<RockGolemCombination> ALL = EnumSet.allOf(RockGolemCombination.class);
        private static final Map<Integer, RockGolemCombination> MAP = new HashMap<>();

        RockGolemCombination(final SkillingPet pet, final int... ores) {
            this.pet = pet;
            this.ores = ores;
        }

        public static RockGolemCombination get(final int ore) {
            return MAP.get(ore);
        }

        static {
            for (final OreOnRockGolemPetNPCAction.RockGolemCombination combination : ALL) {
                for (final int ore : combination.getOres()) {
                    MAP.put(ore, combination);
                }
            }
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        public SkillingPet getPet() {
            return pet;
        }

        public int[] getOres() {
            return ores;
        }
    }
}
