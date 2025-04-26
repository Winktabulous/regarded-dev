package com.zenyte.game.world.entity.player.dailychallenge;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.GameSetting;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.DailyChallenge;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.entity.player.dailychallenge.reward.ChallengeReward;
import com.zenyte.game.world.entity.player.dailychallenge.reward.RewardType;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Tommeh | 02/05/2019 | 22:36
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DailyChallengeManager {

    private final transient Player player;
    Map<DailyChallenge, ChallengeProgress> challengeProgression;

    public DailyChallengeManager(final Player player) {
        this.player = player;
        this.challengeProgression = new Object2ObjectLinkedOpenHashMap<>(3);
    }

    public void update(final DailyChallenge challenge, final int progress) {
        final ChallengeProgress challengeProgress = challengeProgression.get(challenge);
        if (challengeProgress == null || challengeProgress.isCompleted()) {
            return;
        }
        challengeProgress.progress(progress);
        check(challenge);
    }

    public void update(final DailyChallenge challenge) {
        update(challenge, 1);
    }

    private void check(final DailyChallenge challenge) {
        final ChallengeProgress progress = challengeProgression.get(challenge);
        if (progress == null) {
            return;
        }
        if (progress.getProgress() == challenge.getLength()) {
            player.sendMessage("<col=ce8500><shad=000000>Daily Challenge complete: " + challenge.getName() + "! Return to the Challenge Headmaster in Edgeville to claim your reward!");
        } else {
            if (player.getNumericAttribute(GameSetting.DAILY_CHALLENGE_NOTIFICATIONS.toString()).intValue() == 1) {
                player.sendMessage("<col=ce8500><shad=000000>Daily Challenge updated: " + progress);
            }
        }
    }

    public void assignChallenge(final DailyChallenge challenge) {
        final int amountToRemove = Math.max(0, (challengeProgression.size() + 1) - getMaximumChallenges());
        if (amountToRemove > 0) {
            for (int i = 0; i < amountToRemove; i++) {
                final DailyChallenge removable = getRemovableChallenge();
                if (removable != null) {
                    challengeProgression.remove(removable);
                }
            }
        }
        challengeProgression.put(challenge, new ChallengeProgress(challenge));
        player.sendMessage("<img=13><col=ce8500><shad=000000>New Daily Challenge: " + challengeProgression.get(challenge));
    }

    private DailyChallenge getRemovableChallenge() {
        final Iterator<Map.Entry<DailyChallenge, ChallengeProgress>> iterator = challengeProgression.entrySet().iterator();
        if (iterator.hasNext()) {
            return iterator.next().getKey();
        }
        return null;
    }

    public DailyChallenge getRandomChallenge() {
        final ChallengeCategory category = ChallengeCategory.SKILLING;
        int count = 50;
        DailyChallenge challenge = null;
        while (--count > 0 && challenge == null) {
            final ChallengeDetails information = category.getDetails(player);
            for (final DailyChallenge c : ChallengeWrapper.challenges.values()) {
                if (challengeProgression.containsKey(c)) {
                    continue;
                }
                if (!c.getCategory().equals(category) || !c.getDifficulty().equals(information.getDifficulty())) {
                    continue;
                }
                if (c.getCategory().equals(ChallengeCategory.SKILLING) && ((SkillingChallenge) c).getSkill() != (int) information.getAdditionalInformation()[0]) {
                    continue;
                }
                challenge = c;
            }
        }
        return challenge;
    }

    public DailyChallenge getChallenge(final int index) {
        final Set<Map.Entry<DailyChallenge, ChallengeProgress>> set = challengeProgression.entrySet();
        final Object[] array = set.toArray();
        if (index >= array.length) {
            return null;
        }
        final Map.Entry<DailyChallenge, ChallengeProgress> entry = (Map.Entry<DailyChallenge, ChallengeProgress>) array[index];
        return entry.getKey();
    }

    public ChallengeProgress getProgress(final DailyChallenge challenge) {
        return challengeProgression.get(challenge);
    }

    public boolean claim(final DailyChallenge challenge) {
        final ChallengeProgress progress = challengeProgression.get(challenge);
        if (progress == null || !progress.isCompleted()) {
            return false;
        }
        if (!player.getBooleanTemporaryAttribute("daily_challenge_claimable")) {
            player.sendMessage("I should talk to Challenge Headmaster to claim this.");
            return false;
        }
        final ChallengeReward[] rewards = challenge.getRewards();
        int space = 1;
        for (final ChallengeReward reward : rewards) {
            if (reward.getType().equals(RewardType.ITEM)) {
                space += 1;
            }
        }
        if (!player.getInventory().checkSpace(space)) {
            player.sendMessage("Not enough space in inventory to claim the challenge.");
            return false;
        }
        player.sendMessage("<col=ce8500><shad=000000>Challenge redeemed: " + challenge.getName());
        for (final ChallengeReward reward : rewards) {
            reward.apply(player);
        }

        Item coins = new Item(995, challenge.getDifficulty().getCoins());
        player.getInventory().addItem(coins);
        player.sendMessage("<col=ce8500><shad=000000>You have been awarded with " + coins.getAmount() + " " + coins.getName() + "!");

        challengeProgression.remove(challenge);
        return true;
    }

    public void notifyUnclaimedChallenges() {
        int unclaimedChallenges = 0;
        for (final Map.Entry<DailyChallenge, ChallengeProgress> entry : challengeProgression.entrySet()) {
            final ChallengeProgress progress = entry.getValue();
            if (progress.isCompleted() && !progress.isClaimed()) {
                unclaimedChallenges++;
            }
        }
        if (unclaimedChallenges > 0) {
            player.sendMessage("<col=ce8500>You seem to have one or more completed challenges. Return to the Challenge Headmaster in Edgeville to claim your rewards!");
        }
    }

    private int getMaximumChallenges() {
        if (player.getMemberRank().equalToOrGreaterThan(MemberRank.LEGENDARY)) {
            return 5;
        } else if (player.getMemberRank().equalToOrGreaterThan(MemberRank.EXPANSION)) {
            return 4;
        }
        return 3;
    }

    public Map<DailyChallenge, ChallengeProgress> getChallengeProgression() {
        return challengeProgression;
    }

    public void setChallengeProgression(Map<DailyChallenge, ChallengeProgress> challengeProgression) {
        this.challengeProgression = challengeProgression;
    }
}
