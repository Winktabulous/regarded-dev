package com.zenyte.game.content.treasuretrails.clues;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.treasuretrails.ClueLevel;
import com.zenyte.game.content.treasuretrails.TreasureTrailType;
import com.zenyte.game.content.treasuretrails.challenges.ClueChallenge;
import com.zenyte.game.content.treasuretrails.challenges.PuzzleRequest;
import com.zenyte.game.content.treasuretrails.challenges.TalkChallengeRequest;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.zenyte.game.content.treasuretrails.ClueLevel.HARD;
import static com.zenyte.game.content.treasuretrails.ClueLevel.MEDIUM;

/**
 * Contains all the cipherable treasure trails. Cipher TT is effectively just
 * the name of the NPC having each character swapped out to a random available
 * character.
 * 
 * @author Kris | 29. march 2018 : 19:54.27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum CipherClue implements Clue {

	BMJ_UIF_LFCBC_TFMMFS(MEDIUM, ChallengeScroll.ALI_THE_KEBAB_SELLER, 3536),
	ECRVCKP_MJCNGF(MEDIUM, ChallengeScroll.CAPTAIN_KHALED, 6971, 6972),
	//GBJSZ_RVFFO(HARD, null, 1161, 1842),//PUZZLE BOX
	GUHCHO(MEDIUM, ChallengeScroll.DREZEL, 9636),
	//HCKTA_IQFHCVJGT(HARD, ChallengeScroll.FAIRY_GODFATHER, 1840, 5837),
	//OVEXON(HARD, ChallengeScroll.ELUNED, 889, 5304),
	QSPGFTTPS_HSBDLMFCPOF(MEDIUM, ChallengeScroll.PROFESSOR_GRACKLEBONE, 7048),
	USBJCPSO(MEDIUM, ChallengeScroll.TRAIBORN, 5081),
	UZZU_MUJHRKYYKJ(HARD, ChallengeScroll.OTTO_GODBLESSED_2, 2914, 2915),
	VTYR_APCNTGLW(HARD, ChallengeScroll.KING_PERCIVAL, 4058), ZCZL(MEDIUM, ChallengeScroll.ADAM, 311),
	ZHLUG_ROG_PDQ(HARD, ChallengeScroll.WEIRD_OLD_MAN, 954),
	ZSBKDO_ZODO(HARD, null, 601, 602, 3389, 4814, 4816);

	//PUZZLE BOX
	private static final CipherClue[] values = values();
	private final String cipher;
	private final ClueLevel level;
	private final ChallengeScroll scroll;
	private final ClueChallenge challenge;
	private final int[] npcIds;
	public static final Int2ObjectMap<List<CipherClue>> npcMap = new Int2ObjectOpenHashMap<>();

	static {
		for (final CipherClue value : values) {
			for (final int npc : value.npcIds) {
				npcMap.computeIfAbsent(npc, a -> new ArrayList<>()).add(value);
			}
		}
	}

	CipherClue(final ClueLevel level, final ChallengeScroll scroll, final int... npcIds) {
		this.level = level;
		this.cipher = name().replaceAll("_", " ");
		this.scroll = scroll;
		this.challenge = scroll == null ? new PuzzleRequest(npcIds) : new TalkChallengeRequest(scroll, npcIds);
		this.npcIds = npcIds;
	}

	@Override
	public void view(@NotNull Player player, @NotNull Item item) {
		player.getTemporaryAttributes().put("Clue scroll item", item);
		GameInterface.CLUE_SCROLL.open(player);
	}

	@Override
	public TreasureTrailType getType() {
		return TreasureTrailType.CIPHER;
	}

	@Override
	public String getEnumName() {
		return toString();
	}

	@Override
	public String getText() {
		return "The cipher reveals who to speak to next: " + cipher;
	}

	@Override
	public ClueChallenge getChallenge() {
		return challenge;
	}

	@NotNull
	@Override
	public ClueLevel level() {
		return level;
	}

	public String getCipher() {
		return cipher;
	}

	public ClueLevel getLevel() {
		return level;
	}

	public ChallengeScroll getScroll() {
		return scroll;
	}
}
