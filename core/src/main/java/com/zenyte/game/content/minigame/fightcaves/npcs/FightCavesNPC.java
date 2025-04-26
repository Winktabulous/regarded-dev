package com.zenyte.game.content.minigame.fightcaves.npcs;

import com.zenyte.game.content.minigame.fightcaves.FightCaves;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.npc.NPC;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Kris | 8. nov 2017 : 21:38.57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public abstract class FightCavesNPC extends NPC {

	public enum TzHaarNPC {
		/* Primary monsters encountered. */
		TZ_KIH(TzKih.class, 3116), //Sound effects: Strike: 291, block: 296, death: 295
		TZ_KEK(TzKek.class, 3118), //Sound effects: Strike: 595, block: 597, death: 596
		TOK_XIL(TokXil.class, 3121), //Sound effects: Strike: Melee: 598, Ranged: 601, block: 600: death: 599
		YT_MEJ_KOT(YtMejKot.class, 3123), KET_ZEK(KetZek.class, 3125), //Sound effects: Strike: 598(magic)
		TZ_TOK_JAD(TzTokJad.class, 3127), 
		/* Secondary monsters encountered, needs to be separated from the primary. */
		TZ_KIH_ALT(TzKih.class, 3117), TZ_KEK_ALT(TzKek.class, 3119), TZ_KEK_SPLIT(SplitTzKek.class, 3120), TOK_XIL_ALT(TokXil.class, 3122), YT_MEJ_KOT_ALT(YtMejKot.class, 3124), KET_ZEK_ALT(KetZek.class, 3126), YT_HUR_KOT(YtHurKot.class, 3128);
		//Sound effects: Strike: 608, block: 610, death: 609
		private static final TzHaarNPC[] allValues = values();
		private static final Int2ObjectOpenHashMap<TzHaarNPC> map = new Int2ObjectOpenHashMap<>(allValues.length);

		static {
			for (final FightCavesNPC.TzHaarNPC tzhaar : allValues) {
				map.put(tzhaar.id, tzhaar);
			}
		}

		private static final TzHaarNPC[] values = new TzHaarNPC[] {TZ_KIH, TZ_KEK, TOK_XIL, YT_MEJ_KOT, KET_ZEK, TZ_TOK_JAD};
		private final Class<? extends FightCavesNPC> clazz;
		private final int id;
		private final int waveId = (int) Math.pow(2, ordinal() + 1) - 1;
		private static final Class<?>[] arguments = new Class<?>[] {TzHaarNPC.class, Location.class, FightCaves.class};

		public static boolean isAlternative(final NPC npc) {
			final FightCavesNPC.TzHaarNPC constant = map.get(npc.getId());
			return constant != null && constant.toString().endsWith("ALT");
		}

		public FightCavesNPC instantiate(final Location tile, final FightCaves caves) {
			try {
				return clazz.getDeclaredConstructor(arguments).newInstance(this, tile, caves);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public static void populate(final List<TzHaarNPC> list, final int wave) {
			float remaining = wave;
			float multiplier;
			while (remaining >= 1) {
				TzHaarNPC npc;
				for (final FightCavesNPC.TzHaarNPC value : values) {
					npc = value;
					multiplier = remaining / npc.waveId;
					if (multiplier > 2) continue;
					remaining = Math.round((multiplier - 1) * npc.waveId);
					list.add(npc);
					break;
				}
			}
			replaceAlternatives(list);
		}

		private static void replaceAlternatives(final List<TzHaarNPC> list) {
			if (list.size() <= 1) return;
			final FightCavesNPC.TzHaarNPC first = list.get(0);
			final FightCavesNPC.TzHaarNPC second = list.get(1);
			if (first == second) {
				list.set(1, map.get(first.id + 1));
			}
		}

		TzHaarNPC(Class<? extends FightCavesNPC> clazz, int id) {
			this.clazz = clazz;
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	FightCavesNPC(TzHaarNPC npc, final Location tile, final FightCaves caves) {
		super(npc.id, tile, true);
		this.caves = caves;
		this.setMaxDistance(200);
		this.setForceAggressive(true);
		caves.getMonsters().add(this);
		possibleTargets.add(caves.getPlayer());
	}

	final FightCaves caves;

	protected void playSound(@NotNull final SoundEffect sound) {
		caves.getPlayer().getPacketDispatcher().sendSoundEffect(sound);
	}

	@Override
	public void onFinish(final Entity source) {
		super.onFinish(source);
		caves.checkWave(this);
	}

	@Override
	public List<Entity> getPossibleTargets(final EntityType type) {
		return possibleTargets;
	}
}
