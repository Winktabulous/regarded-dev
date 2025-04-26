package com.zenyte.game.world.entity.player.container;

import com.zenyte.game.item.Item;

/**
 * @author Kris | 11. sept 2018 : 20:41:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class MultiResult {
	public MultiResult(final ContainerState state, final ContainerResult[] results) {
		this.state = state;
		this.results = results;
		for (int i = 0; i < results.length; i++) {
			final ContainerResult res = results[i];
			if (res.getResult() != RequestResult.SUCCESS) {
				result = RequestResult.FAILURE;
				return;
			}
		}
		result = RequestResult.SUCCESS;
	}

	private final ContainerState state;
	private final ContainerResult[] results;
	/**
	 * The payload for the batch request will only be either
	 * {@code RequestResult#SUCCESS} or {@code RequestResult#FAILURE}
	 */
	private final RequestResult result;

	public void onFailure(final ContainerFailure runnable) {
		for (final ContainerResult result : results) {
			final Item item = result.getItem();
			final int succeededAmount = result.getSucceededAmount();
			if (result.getResult() == RequestResult.SUCCESS || item.getAmount() == succeededAmount) {
				continue;
			}
			runnable.execute(new Item(item.getId(), item.getAmount() - succeededAmount));
		}
	}

	public ContainerState getState() {
		return state;
	}

	public RequestResult getResult() {
		return result;
	}
}
