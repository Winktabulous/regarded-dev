package com.zenyte.game.world.entity.player.container;

import com.zenyte.game.item.Item;

/**
 * @author Kris | 3. mai 2018 : 19:22:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ContainerResult {

    private final Item item;
    private final ContainerState state;
    private int succeededAmount;
    private RequestResult result;
    public ContainerResult(final Item item, final ContainerState state) {
        this.item = item == null ? null : new Item(item.getId(), item.getAmount(), item.getAttributesCopy());
        this.state = state;
    }

    public void onFailure(final ContainerFailure runnable) {
        if (result == RequestResult.SUCCESS || item.getAmount() == succeededAmount) {
            return;
        }
        runnable.execute(new Item(item.getId(), item.getAmount() - succeededAmount, item.getAttributesCopy()));
    }

    public boolean isFailure() {
        return !result.equals(RequestResult.SUCCESS) || item.getAmount() != succeededAmount;
	}

    @Override
    public String toString() {
        String builder = "State: " + state.toString() + ", " +
                "Item: " + item.toString() + ", " +
                "Succeeded amount: " + succeededAmount + ", " +
                "Result: " + result.toString() + ".";
        return builder;
    }

    public Item getItem() {
        return item;
    }

    public ContainerState getState() {
        return state;
    }

    public int getSucceededAmount() {
        return succeededAmount;
    }

    public void setSucceededAmount(int succeededAmount) {
        this.succeededAmount = succeededAmount;
    }

    public RequestResult getResult() {
        return result;
    }

    public void setResult(RequestResult result) {
        this.result = result;
    }

    public ContainerResult markFailure() {
        this.result = RequestResult.FAILURE;
        return this;
    }

}
