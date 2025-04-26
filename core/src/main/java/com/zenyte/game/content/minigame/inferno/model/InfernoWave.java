package com.zenyte.game.content.minigame.inferno.model;

import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.CollectionUtils;

import static com.zenyte.game.content.minigame.inferno.model.WaveNPC.*;

/**
 * @author Kris | 16. apr 2018 : 15:13.01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum InfernoWave {
	WAVE_1(1, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH)), WAVE_2(2, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2)), WAVE_3(3, new WaveEntry(JAL_NIB, 6)), WAVE_4(4, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK)), WAVE_5(5, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_AK)), WAVE_6(6, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_AK)), WAVE_7(7, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK, 2)), WAVE_8(8, new WaveEntry(JAL_NIB, 6)), WAVE_9(9, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_IMKOT)), WAVE_10(10, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_IMKOT)), WAVE_11(11, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_IMKOT)), WAVE_12(12, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_IMKOT)), WAVE_13(13, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_IMKOT)), WAVE_14(14, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_IMKOT)), WAVE_15(15, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK, 2), new WaveEntry(JAL_IMKOT)), WAVE_16(16, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_IMKOT, 2)), WAVE_17(17, new WaveEntry(JAL_NIB, 6)), WAVE_18(18, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_XIL)), WAVE_19(19, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_XIL)), WAVE_20(20, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_XIL)), WAVE_21(21, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_XIL)), WAVE_22(22, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_XIL)), WAVE_23(23, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_XIL)), WAVE_24(24, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK, 2), new WaveEntry(JAL_XIL)), WAVE_25(25, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL)), WAVE_26(26, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL)), WAVE_27(27, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL)), WAVE_28(28, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL)), WAVE_29(29, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_AK), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL)), WAVE_30(30, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_AK), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL)), WAVE_31(31, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK, 2), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL)), WAVE_32(32, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_IMKOT, 2), new WaveEntry(JAL_XIL)), WAVE_33(33, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_XIL, 2)), WAVE_34(34, new WaveEntry(JAL_NIB, 6)), WAVE_35(35, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_ZEK)), WAVE_36(36, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_ZEK)), WAVE_37(37, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_ZEK)), WAVE_38(38, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_ZEK)), WAVE_39(39, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_ZEK)), WAVE_40(40, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_ZEK)), WAVE_41(41, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK, 2), new WaveEntry(JAL_ZEK)), WAVE_42(42, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_ZEK)), WAVE_43(43, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_ZEK)), WAVE_44(44, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_ZEK)), WAVE_45(45, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_ZEK)), WAVE_46(46, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_AK), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_ZEK)), WAVE_47(47, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_AK), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_ZEK)), WAVE_48(48, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK, 2), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_ZEK)), WAVE_49(49, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_IMKOT, 2), new WaveEntry(JAL_ZEK)), WAVE_50(50, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_51(51, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_52(52, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_53(53, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_54(54, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_AK), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_55(55, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_AK), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_56(56, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK, 2), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_57(57, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_58(58, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_59(59, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_60(60, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_61(61, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH), new WaveEntry(JAL_AK), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_62(62, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_MEJRAH, 2), new WaveEntry(JAL_AK), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_63(63, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_AK, 2), new WaveEntry(JAL_IMKOT), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_64(64, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_IMKOT, 2), new WaveEntry(JAL_XIL), new WaveEntry(JAL_ZEK)), WAVE_65(65, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_XIL, 2), new WaveEntry(JAL_ZEK)), WAVE_66(66, new WaveEntry(JAL_NIB, 3), new WaveEntry(JAL_ZEK, 2)), WAVE_67(67, new WaveEntry(JALTOK_JAD)), WAVE_68(68, new WaveEntry(JALTOK_JAD, 3)), WAVE_69(69, new WaveEntry(TZKAL_ZUK));
	private static final InfernoWave[] values = values();
	private static final Int2ObjectOpenHashMap<InfernoWave> map;

	static {
		CollectionUtils.populateMap(values, map = new Int2ObjectOpenHashMap<>(values.length), InfernoWave::getWave);
	}

	private final int wave;
	private final WaveEntry[] entries;

	InfernoWave(final int wave, final WaveEntry... entries) {
		this.wave = wave;
		this.entries = entries;
	}

	public static InfernoWave get(final int wave) {
		return map.get(wave);
	}

	public static InfernoWave[] getValues() {
		return values;
	}

	public InfernoWave increment() {
		if (this.equals(WAVE_69)) {
			throw new IllegalArgumentException("Cannot increment the wave on the last wave.");
		}
		return values[ordinal() + 1];
	}

	public boolean contains(final Class<? extends InfernoNPC> npc) {
		for (final WaveEntry entry : entries) {
			if (entry.getNpc().getClazz().equals(npc)) {
				return true;
			}
		}
		return false;
	}

	public int getWave() {
		return wave;
	}

	public WaveEntry[] getEntries() {
		return entries;
	}
}
