package com.zenyte.game.content.tombsofamascut;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractTOAManager {



    public AbstractTOAManager(Player player) {
    }

    public static AbstractTOAManager getForPlayer(Player player) {
        try {
            Class<?> clazz = Class.forName("com.zenyte.game.content.tombsofamascut.TOAManager");
            Constructor<?> c = clazz.getConstructor(Player.class);
            return (AbstractTOAManager) c.newInstance(player);
        }catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
               InvocationTargetException ignored) {
            return new AbstractTOAManager(player) {
                @Override
                public void onLogin() {
                    super.onLogin();
                }

                @Override
                public void triggerTOAFailure(boolean b) {

                }

                @Override
                public int getCurrentPoints() {
                    return 0;
                }

                @Override
                public void setCurrentPoints(int currentPoints) {

                }

                @Override
                public void refreshHudStates() {

                }

                @Override
                public void sendEmptyPartyList() {

                }

                @Override
                protected Object getCurrentEncounter() {
                    return null;
                }

                @Override
                public Object getViewingParty() {
                    return null;
                }

                @Override
                public void setViewingParty(Object toaRaidParty) {

                }

                @Override
                public Object getCurrentParty() {
                    return null;
                }

                @Override
                public void setCurrentParty(Object toaRaidParty) {

                }

                @Override
                public Object getAppliedParty() {
                    return null;
                }

                @Override
                public void setAppliedParty(Object toaRaidParty) {

                }

                @Override
                public boolean needsAbandonRequest() {
                    return false;
                }

                @Override
                public void startAbandonDialogue(String s, Runnable confirmRunnable) {

                }

                @Override
                public boolean enter(boolean b, Object firstEncounter) {
                    return false;
                }

                @Override
                public void refreshPathLevel(int i) {

                }

                @Override
                public void setCanClaimSupplies(boolean b) {

                }

                @Override
                public void removeTOAItems() {

                }

                @Override
                public void setToaPlayerLogoutState(Object toaPlayerLogoutState) {

                }

                @Override
                public void setCurrentEncounter(Object currentEncounter) {

                }

                @Override
                public int getIndividualDeaths() {
                    return 0;
                }

                @Override
                public void setIndividualDeaths(int i) {

                }

                @Override
                public Container getSuppliesContainer() {
                    return null;
                }

                @Override
                public void withdrawSpecificSupplies(int slotId, int option) {

                }

                @Override
                public Container getRewardContainer() {
                    return null;
                }

                @Override
                public void setSuppliesContainer(Container suppliesContainer) {

                }

                @Override
                public boolean isCanClaimSupplies() {
                    return false;
                }


                @Override
                public Integer getCurrentInterfaceTab() {
                    return 0;
                }

                @Override
                public void setCurrentInterfaceTab(int currentInterfaceTab) {

                }

                @Override
                public int getViewingValue() {
                    return 0;
                }

                @Override
                public void setViewingValue(int viewingValue) {

                }

                @Override
                public Object getPartySettings() {
                    return null;
                }

                @Override
                public void setPartySettings(Object partySettings) {

                }

                @Override
                protected void updatePreset(int[] preset, int index) {

                }

                @Override
                public int[] getInvocationPreset(int currentPresetSlot) {
                    return new int[0];
                }

                @Override
                public boolean viewingManagementInterface(String leaderDisplayName) {
                    return false;
                }

                @Override
                public void toggleInvocation(Object value, Player player) {

                }

                @Override
                public void clearInvocationPreset(int slotId) {

                }

                @Override
                public void saveInvocationPreset(int currentPresetSlot) {

                }

                @Override
                public boolean isPresetEmpty(int currentPresetSlot) {
                    return false;
                }

                @Override
                protected int getPresetBaseVarId(int index) {
                    return 0;
                }

                @Override
                public void enterRaid() {

                }

                @Override
                public boolean storeSupply(int i, Item item) {
                    return false;
                }

                @Override
                public void withdrawSupplies(int i) {

                }

                @Override
                public void reSupply() {

                }

                @Override
                public void resetSessionAttributes() {

                }

                @Override
                public void sendRaidLevel() {

                }

                @Override
                public void refreshHudPlayers() {

                }

                @Override
                public void refreshTimer() {

                }

                @Override
                public void startLeaveDialogue() {

                }

                @Override
                public void initialize(AbstractTOAManager toaManager) {

                }

                @Override
                public Object getToaPlayerLogoutState() {
                    return null;
                }

                @Override
                public void advanceRaid(boolean b) {

                }

                @Override
                public void leaveTombs(String s) {

                }

                @Override
                public Object getRaidParty() {
                    return null;
                }

                @Override
                public void setRaidParty(Object toaRaidParty) {

                }
            };
        }
    }

    public void onLogin() {

    }

    public int damageDone;
    public int damageTaken;
    public int points;


    public int getDamageDone() { return damageDone; }

    public void setDamageDone(int damageDone) { this.damageDone = damageDone; }

    public int getDamageTaken() { return damageTaken; }

    public void setDamageTaken(int damageTaken) { this.damageTaken = damageTaken; }


    public void setRewardContainer(Container container) {

    }

    public abstract void triggerTOAFailure(boolean b);

    public abstract int getCurrentPoints();

    public abstract void setCurrentPoints(int currentPoints);

    public void sendHud() {

    }

    public abstract void refreshHudStates();

    public abstract void sendEmptyPartyList();

    protected abstract Object getCurrentEncounter();

    public abstract Object getViewingParty();

    public abstract void setViewingParty(Object toaRaidParty);

    public abstract Object getCurrentParty();

    public abstract void setCurrentParty(Object toaRaidParty);

    public abstract Object getAppliedParty();

    public abstract void setAppliedParty(Object toaRaidParty);

    public abstract boolean needsAbandonRequest();

    public abstract void startAbandonDialogue(String s, Runnable confirmRunnable);

    public abstract boolean enter(boolean b, Object firstEncounter);

    public abstract void refreshPathLevel(int i);

    public abstract void setCanClaimSupplies(boolean b);

    public abstract void removeTOAItems();

    public abstract void setToaPlayerLogoutState(Object toaPlayerLogoutState);

    public abstract void setCurrentEncounter(Object currentEncounter);

    public abstract int getIndividualDeaths();

    public abstract void setIndividualDeaths(int i);

    public abstract Container getSuppliesContainer();

    public abstract void withdrawSpecificSupplies(int slotId, int option);

    public abstract Container getRewardContainer();

    public abstract void setSuppliesContainer(Container suppliesContainer);

    public abstract boolean isCanClaimSupplies();

    public abstract Integer getCurrentInterfaceTab();

    public abstract void setCurrentInterfaceTab(int currentInterfaceTab);

    public abstract int getViewingValue();

    public abstract void setViewingValue(int viewingValue);
    public abstract Object getPartySettings();

    public abstract void setPartySettings(Object partySettings);

    protected abstract void updatePreset(int[] preset, int index);

    public abstract int[] getInvocationPreset(int currentPresetSlot);

    public abstract boolean viewingManagementInterface(String leaderDisplayName);

    public abstract void toggleInvocation(Object value, Player player);

    public abstract void clearInvocationPreset(int slotId);

    public abstract void saveInvocationPreset(int currentPresetSlot);

    public abstract boolean isPresetEmpty(int currentPresetSlot);

    protected abstract int getPresetBaseVarId(int index);

    public abstract void enterRaid();

    public abstract boolean storeSupply(int i, Item item);

    public abstract void withdrawSupplies(int i);

    public abstract void reSupply();

    public abstract void resetSessionAttributes();

    public abstract void sendRaidLevel();

    public abstract void refreshHudPlayers();

    public abstract void refreshTimer();

    public abstract void startLeaveDialogue();

    public abstract void initialize(AbstractTOAManager toaManager);

    public abstract Object getToaPlayerLogoutState();

    public abstract void advanceRaid(boolean b);

    public abstract void leaveTombs(String s);

    public abstract Object getRaidParty();

    public abstract void setRaidParty(Object toaRaidParty);
}
