package com.zenyte.game.content.tombsofamascut.raid;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.tombsofamascut.lobby.TOALobbyParty;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;

import java.util.*;

/**
 * @author Savions
 */
public class TOAScoreBoardInterface extends Interface {

    @Override
    protected void attach() {

    }

    @Override
    protected void build() {

    }

    @Override
    public void open(Player player) {
        super.open(player);
        TOARaidParty party = (TOARaidParty) player.getTOAManager().getRaidParty();
        if (party != null) {
            player.getPacketDispatcher().sendComponentText(GameInterface.TOA_SCOREBOARD, 58, party.getPartySettings().getRaidLevel());
            player.getPacketDispatcher().sendComponentText(GameInterface.TOA_SCOREBOARD, 59, party.getPartySettings().getActiveInvocations());
            player.getPacketDispatcher().sendComponentText(GameInterface.TOA_SCOREBOARD, 66, TOARaidArea.formatTime(party.getTotalChallengeTime()));
            player.getPacketDispatcher().sendComponentText(GameInterface.TOA_SCOREBOARD, 67, TOARaidArea.formatTime(party.getTotalTime()));
            sendEncounterTime(player, 9, EncounterType.WARDENS_SECOND_ROOM);
            sendEncounterTime(player, 14, EncounterType.CRONDIS_BOSS);
            sendEncounterTime(player, 11, EncounterType.APMEKEN_BOSS);
            sendEncounterTime(player, 16, EncounterType.HET_BOSS);
            sendEncounterTime(player, 18, EncounterType.SCABARIS_BOSS);
            sendEncounterTime(player, 25, EncounterType.CRONDIS_PUZZLE);
            sendEncounterTime(player, 28, EncounterType.APMEKEN_PUZZLE);
            sendEncounterTime(player, 30, EncounterType.HET_PUZZLE);
            sendEncounterTime(player, 72, EncounterType.SCABARIS_PUZZLE);
            final Map<Integer, Player> playerMap = new TreeMap<>(Collections.reverseOrder());
            for (Player p : party.getPlayers()) {
                if (p != null) {
                    final int damageDone = p.getTOAManager().getDamageDone();
                    playerMap.put(damageDone, player);
                }
            }
            int index = 0;
            final Collection<Player> players = playerMap.values();
            for (Player p : players) {
                final Object[] args = new Object[9];
                args[0] = p.getName();
                args[1] = p.getTOAManager().getDamageDone();
                args[2] = p.getTOAManager().getDamageTaken();
                args[3] = p.getTOAManager().getIndividualDeaths();
                final ScoreBoardTitleType title = getPlayerTitle(p, players);
                args[4] = title == null ? -1 : title.getStructId();
                args[5] = -1;
                args[6] = index;
                args[7] = players.size();
                args[8] = index == 0 ? 1 : 0;
                index++;
                player.getPacketDispatcher().sendClientScript(6590, args);
            }
        }
    }

    private void sendEncounterTime(Player player, int componentId, EncounterType encounterType) {
        TOARaidParty party = (TOARaidParty) player.getTOAManager().getRaidParty();
        for (ChallengeResult result : party.getChallengeResults()) {
            if (result != null && encounterType.equals(result.getEncounterType())) {
                player.getPacketDispatcher().sendComponentText(GameInterface.TOA_SCOREBOARD, componentId, TOARaidArea.formatTime(result.getTime()));
                return;
            }
        }
    }

    private ScoreBoardTitleType getPlayerTitle(final Player p, final Collection<Player> players) {
        final List<ScoreBoardTitleType> potentialTitles = getPotentialPlayerTitles(p, players);
        return potentialTitles.get(potentialTitles.size() > 1 ? Utils.random(potentialTitles.size() - 1) : 0);
    }

    private List<ScoreBoardTitleType> getPotentialPlayerTitles(final Player player, final Collection<Player> players) {
        final List<ScoreBoardTitleType> potentialTitles = new ArrayList<>();
        if (players.size() < 2) {
            potentialTitles.add(ScoreBoardTitleType.BRAWLER);
            potentialTitles.add(ScoreBoardTitleType.SORCERER);
            potentialTitles.add(ScoreBoardTitleType.ARCHER);
            potentialTitles.add(ScoreBoardTitleType.THE_CLUTCH);
            return potentialTitles;
        } else {
            final Player[] playerArray = players.toArray(new Player[0]);
            int highestLoadOut = 0;
            int lowestLoadOut = Integer.MAX_VALUE;
            int lowestDamageDone = Integer.MAX_VALUE;
            int damageDone = 0;
            int damageTaken = 0;
            int maxDeaths = 0;
            for (int i = 0; i < players.size(); i++) {
                final Player p = playerArray[i];
                final int currentLoadOut = getLoadOut(p);
                highestLoadOut = Math.max(highestLoadOut, currentLoadOut);
                lowestLoadOut = Math.min(lowestLoadOut, currentLoadOut);
                lowestDamageDone = Math.min(lowestDamageDone, p.getTOAManager().getDamageDone());
                damageDone = Math.max(damageDone, p.getTOAManager().getDamageDone());
                damageTaken = Math.max(damageTaken, p.getTOAManager().getDamageTaken());
                maxDeaths = Math.max(maxDeaths, p.getTOAManager().getIndividualDeaths());
            }
            final int playerLoadOut = getLoadOut(player);
            if (playerLoadOut >= highestLoadOut) {
                potentialTitles.add(ScoreBoardTitleType.THE_SHOWOFF);
            }
            if (playerLoadOut <= lowestLoadOut) {
                potentialTitles.add(ScoreBoardTitleType.THE_PEASANT);
            }
            if (player.getTOAManager().getDamageTaken() >= damageTaken) {
                potentialTitles.add(ScoreBoardTitleType.THE_TANK);
            }
            if (player.getTOAManager().getDamageDone() >= damageDone) {
                potentialTitles.add(ScoreBoardTitleType.CARRY);
            }
            if (maxDeaths > 0 && player.getTOAManager().getIndividualDeaths() >= maxDeaths) {
                potentialTitles.add(ScoreBoardTitleType.ANCHOR);
            }
            if (player.getTOAManager().getDamageDone() <= lowestDamageDone) {
                potentialTitles.add(ScoreBoardTitleType.LEECH);
            } else {
                potentialTitles.add(ScoreBoardTitleType.BRAWLER);
                potentialTitles.add(ScoreBoardTitleType.SORCERER);
                potentialTitles.add(ScoreBoardTitleType.ARCHER);
            }
        }
        return potentialTitles;
    }

    private int getLoadOut(final Player p) {
        int loadOut = 0;
        for (Item item : p.getInventory().getContainer().getItems().values()) {
            if (item != null) {
                loadOut += item.getSellPrice();
            }
        }
        for (Item item : p.getEquipment().getContainer().getItems().values()) {
            if (item != null) {
                loadOut += item.getSellPrice();
            }
        }
        return loadOut;
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.TOA_SCOREBOARD;
    }

    enum ScoreBoardTitleType {

        LEECH(391),
        THE_SWIMMER(392),
        THE_SPECIALIST(393),
        THE_PICKY_EATER(394),
        THE_PEASANT(395),
        THE_SHOWOFF(396),
        THE_TANK(397), //took most damage
        THE_BALLER(398),
        THE_ENTOMOPHOBE(401),
        THE_MOTH(402),
        CARRY(403), //done most damage
        ANCHOR(406), //died the most
        THE_GLUTTON(407),
        SORCERER(408),
        ARCHER(409),
        BRAWLER(411),
        THE_CLUTCH(412),
        ;

        private final int structId;

        ScoreBoardTitleType(int structId) {
            this.structId = structId;
        }

        public final int getStructId() { return structId; }
    }
}
