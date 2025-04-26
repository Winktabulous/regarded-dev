package com.near_reality.game.content

import com.zenyte.game.item._Item

object CollectionLogRewards {


    val rewards = mutableListOf<CollectionLogReward>()
    init {
        rewards.addAll(
            arrayOf(
                // Abyssal Sire: 5 unsired (13273)
                CollectionLogReward(476, arrayOf(_Item(13273, 5))),
                // Alchemical Hydra: 10m GP, 5x slayer task picker scrolls, 1x mystery box (995, 32157, 6199)
                CollectionLogReward(539, arrayOf(gen(995, 10.toM()), gen(32157, 5), gen(6199, 1))),
                // Barrows (move to minigames): 5m GP, 10x Barrows Totems (995, 32168)
                CollectionLogReward(477, arrayOf(gen(995, 5.toM()), gen(32168, 10))),
                // Bryophyta: 500k GP, 3x mossy keys (995, 22374)
                CollectionLogReward(478, arrayOf(gen(995, 500_000), gen(22374, 3))),
                // Callisto & Artio: 5m GP, 1x pet boosters (995, 32152)
                CollectionLogReward(479, arrayOf(gen(995, 5.toM()), gen(32152, 1))),
                // Cerberus: 5m GP, 5x slayer task picker scrolls (995, 32157)
                CollectionLogReward(480, arrayOf(gen(995, 5.toM()), gen(32157, 5))),
                // Chaos Elemental: 2.5m GP (995)
                CollectionLogReward(481, arrayOf(gen(995, 2_500_000))),
                // Chaos Fanatic: 2.5m GP (995)
                CollectionLogReward(482, arrayOf(gen(995, 2_500_000))),
                // Commander Zilyana: 5m GP, 2x mystery box (995, 6199)
                CollectionLogReward(483, arrayOf(gen(995, 5.toM()), gen(6199, 2))),
                // Corporeal Beast: 2x mystery box, $10 bond (6199, 30051)
                CollectionLogReward(484, arrayOf(gen(6199, 2), gen(32070, 1))),
                // Crazy Archaeologist: 2.5m GP (995)
                CollectionLogReward(485, arrayOf(gen(995, 2_500_000))),
                // Dagannoth Kings: Imbue Scroll x1 (11681)
                CollectionLogReward(486, arrayOf(gen(11681, 1))),
                // Duke Sucellus:
                CollectionLogReward(10500, arrayOf(gen(995, 5.toM()), gen(6199, 2))),
                // Vardorvis:
                CollectionLogReward(10501, arrayOf(gen(995, 5.toM()), gen(6199, 2))),
                // Tormented Demons:
                CollectionLogReward(10502, arrayOf(gen(995, 10.toM()), gen(6199, 2))),
                // Arraxor
                CollectionLogReward(10503, arrayOf(gen(995, 15.toM()), gen(6199, 3))),
                // Fight Caves (move to minigames): 50k Tokkul, 1x fire cape (6529, 6570)
                CollectionLogReward(500, arrayOf(gen(6529, 50_000), gen(6570, 1))),
                // The Gauntlet (move to minigames): 1x gauntlet booster, 1x mystery box (32153, 6199)
                CollectionLogReward(605, arrayOf(gen(32153, 1), gen(6199, 1))),
                // Ganodermic Beast: $50 bond, ultimate mystery box, 1x ganodermic booster (13190, 32165, 32150)
                CollectionLogReward(10300, arrayOf(gen(13190, 1), gen(32165, 1), gen(32150, 1))),
                // General Graardor: 5m GP, 2x mystery box (995, 6199)
                CollectionLogReward(487, arrayOf(gen(995, 5.toM()), gen(6199, 2))),
                // Giant Mole: 3x crystal key (989)
                CollectionLogReward(488, arrayOf(gen(989, 3))),
                // Grotesque Guardians: 3x slayer boosters, 3x slayer task picker scrolls (32151, 32157)
                CollectionLogReward(489, arrayOf(gen(32151, 3), gen(32157, 3))),
                // Hespori: 100x clean toadflax, 100x clean snapdragon, 100x clean torstol (2999, 3001, 270)
                CollectionLogReward(541, arrayOf(gen(2999, 100), gen(3001, 100), gen(270, 100))),
                // The Inferno (move to minigames): 100k tokkul, 1x infernal cape (6529, 21295)
                CollectionLogReward(499, arrayOf(gen(6529, 100_000), gen(21295, 1))),
                // Kalphite Queen: 5m GP, 1x Mystery Box (995, 6199)
                CollectionLogReward(490, arrayOf(gen(995, 5.toM()), gen(6199, 1))),
                // King Black Dragon: 5m gp, 1x Mystery Box (995, 6199)
                CollectionLogReward(491, arrayOf(gen(995, 5.toM()), gen(6199, 1))),
                // Kraken: 1x slayer task picker scroll, 1x slayer task reset scroll, 1x mystery box (32157, 32158)
                CollectionLogReward(492, arrayOf(gen(32157, 1), gen(32158, 1), gen(6199, 1))),
                // Kree'arra: 5m GP, 2x mystery box (995, 6199)
                CollectionLogReward(493, arrayOf(gen(995, 5.toM()), gen(6199, 2))),
                // K'ril Tsutsaroth: 5m GP, 2x mystery box (995, 6199)
                CollectionLogReward(494, arrayOf(gen(995, 5.toM()), gen(6199, 2))),
                // Nex: 1x nex booster, torva themed primordial boot kit (32167, N/A)
                CollectionLogReward(3769, arrayOf(gen(32167, 1), gen(31352, 1))),
                // The Nightmare: 3x mystery box, $10 bond (6199, 30051)
                CollectionLogReward(1263, arrayOf(gen(6199, 3), gen(32070, 1))),
                // Obor: 3x giant keys (20754)
                CollectionLogReward(495, arrayOf(gen(20754, 3))),
                // Phantom Muspah: 5m cash, 1x venator shard (995, 27614)
                CollectionLogReward(4455, arrayOf(gen(995, 5.toM()), gen(27614, 1))),
                // Rise of the Six: 10m cash, 2x mystery box (or make a strange old man pet, would be cool/funny) (995, 6199)
                CollectionLogReward(10321, arrayOf(gen(995, 10.toM()), gen(6199, 2))),
                // Sarachnis: 5m gp, 1x Mystery Box (995, 6199)
                CollectionLogReward(601, arrayOf(gen(995, 5.toM()), gen(6199, 1))),
                // Scorpia: 5m GP, 1x pet boosters (995, 32152)
                CollectionLogReward(496, arrayOf(gen(995, 5.toM()), gen(32152, 1))),
                // Skotizo: 5m GP, 5x dark totems (995, 19685)
                CollectionLogReward(497, arrayOf(gen(995, 5.toM()), gen(19685, 1))),
                // Thermonuclear Smoke Devil: 5m gp, 1x Mystery Box (995, 6199)
                CollectionLogReward(498, arrayOf(gen(995, 5.toM()), gen(6199, 1))),
                // Vanstrom Klause: 5m GP (995)
                CollectionLogReward(10322, arrayOf(gen(995, 5.toM()))),
                // Venenatis and Spindel: 5m GP, 1x pet boosters (995, 32152)
                CollectionLogReward(501, arrayOf(gen(995, 5.toM()), gen(32152, 1))),
                // Vet'ion and Calvar'ion: 5m GP, 1x pet boosters (995, 32152)
                CollectionLogReward(502, arrayOf(gen(995, 5.toM()), gen(32152, 1))),
                // Vorkath: 5m GP, 100x superior bones (995, 22124)
                CollectionLogReward(503, arrayOf(gen(995, 5.toM()), gen(22124, 100))),
                // Wintertodt (move to minigames): 2m GP, 5x supply crates (995, 20703)
                CollectionLogReward(504, arrayOf(gen(995, 2.toM()), gen(20703, 5))),
                // Zalcano: 2x mystery box, 1x pet boosters (6199, 32152)
                CollectionLogReward(604, arrayOf(gen(6199, 2), gen(32152, 1))),
                // Zulrah: 2x mystery box, 10m gp
                CollectionLogReward(505, arrayOf(gen(6199, 2), gen(995, 10.toM()))),

                // RAIDS
                // Chambers of Xeric: $25 scroll, ancestral themed eternal boot kit (32071, N/A)
                CollectionLogReward(507, arrayOf(gen(32071, 1), gen(31351, 1))),
                // Theater of Blood: $50 scroll, 1x ultimate mystery box, 1x ToB booster (32072, 32156)
                CollectionLogReward(506, arrayOf(gen(32072, 1), gen(32165, 1), gen(32156, 1))),
                // Tombs of Amascut: $25 scroll, masori themed pegasian boot kit (32071, N/A)
                CollectionLogReward(4378, arrayOf(gen(32071, 1), gen(31350, 1))),

                // CLUES
                // Beginner Treasure Trails: 5m GP (995)
                CollectionLogReward(593, arrayOf(gen(995, 5.toM()))),
                // Easy Treasure Trails: 10m GP (995)
                CollectionLogReward(508, arrayOf(gen(995, 10.toM()))),
                // Medium Treasure Trails: 10m GP, 1x clue booster (995, 32155)
                CollectionLogReward(509, arrayOf(gen(995, 10.toM()), gen(32155, 1))),
                // Hard Treasure Trails: 20m GP, 1x clue booster (995, 32155)
                CollectionLogReward(510, arrayOf(gen(995, 20.toM()), gen(32155, 1))),
                // Elite Treasure Trails: 25m GP, 1x clue booster (995, 32155)
                CollectionLogReward(511, arrayOf(gen(995, 25.toM()), gen(32155, 1))),
                // Master Treasure Trails: 30m gp, 1x clue booster (995, 32155)
                CollectionLogReward(512, arrayOf(gen(995, 30.toM()), gen(32155, 1))),
                // Hard Treasure Trails (Rare):40m gp, 1x clue booster (995, 32155)
                CollectionLogReward(2869, arrayOf(gen(995, 40.toM()), gen(32155, 1))),
                // Elite Treasure Trails (Rare): 45m gp, 1x clue booster (995, 32155)
                CollectionLogReward(2870, arrayOf(gen(995, 45.toM()), gen(32155, 1))),
                // Master Treasure Trails (Rare): 50m gp, 1x clue booster (995, 32155)
                CollectionLogReward(2871, arrayOf(gen(995, 50.toM()), gen(32155, 1))),

                // MINIGAMES
                // Pest Control: 2.5m GP (995)
                CollectionLogReward(518, arrayOf(gen(995, 2_500_000))),
                // Rogues Den: 2.5m GP (995)
                CollectionLogReward(522, arrayOf(gen(995, 2_500_000))),

                // OTHER
                // Aerial Fishing: 5m cash, 1x mystery box (995, 6199)
                CollectionLogReward(540, arrayOf(gen(995, 5.toM()), gen(6199, 1))),
                // All Pets: $100 bond (30017)
                CollectionLogReward(535, arrayOf(gen(32073, 1))),
                // Chaos Druids: 1x larrans key booster (32149)
                CollectionLogReward(533, arrayOf(gen(32149, 1))),
                // Cyclopes: dragon defender ornament kit (20143)
                CollectionLogReward(532, arrayOf(gen(20143, 1))),
                // Glough's Experiments: 5m GP, 2x mystery box (995, 6199)
                CollectionLogReward(526, arrayOf(gen(995, 5.toM()), gen(6199, 2))),
                // Motherlode Mine: dragon pickaxe ornament kit (12800)
                CollectionLogReward(530, arrayOf(gen(12800, 1))),
                // Revenants: 10m GP, 1x mystery box, 1x revenant booster (995, 6199, 32166)
                CollectionLogReward(525, arrayOf(gen(995, 10.toM()), gen(6199, 1), gen(32166, 1))),
                // Rooftop Agility: 3x graceful dye (2710)
                CollectionLogReward(547, arrayOf(gen(2710, 3))),
                // Shooting Stars: 500x stardust (25527)
                CollectionLogReward(2858, arrayOf(gen(25527, 500))),
                // Skilling Pets: 25m GP, 5x tome of experience (995, 30215)
                CollectionLogReward(529, arrayOf(gen(995, 25.toM()), gen(30215, 5))),
                // Slayer: 5x slayer task picker scrolls, 10x slayer task skip scrolls, 3x mystery box (32157, 32158, 6199)
                CollectionLogReward(527, arrayOf(gen(32157, 5), gen(32158, 10), gen(6199, 3))),
                // Tzhaar: 5m GP, 5x crystal keys (995, 989)
                CollectionLogReward(536, arrayOf(gen(995, 5.toM()), gen(989, 5))),
                // Miscellaneous: 3x mystery box (6199)
                CollectionLogReward(534, arrayOf(gen(6199, 3))),
                )
        )
    }

    fun gen(id: Int, q: Int) = _Item(id, q)

    @JvmStatic fun getRewardSet(struct: Int) : CollectionLogRewardSet {
        return rewards.find{ it.struct == struct }?.toSet() ?: CollectionLogRewardSet()
    }
}

private fun Int.toM(): Int {
    return this * 1_000_000

}
