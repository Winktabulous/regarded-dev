package com.zenyte.game.content.grandexchange;

import com.near_reality.tools.logging.GameLogMessage;
import com.near_reality.tools.logging.GameLogger;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import com.zenyte.utils.DefaultLogger;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import kotlinx.datetime.Instant;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.zenyte.game.content.grandexchange.GrandExchange.OFFER_TIMEOUT_DELAY;

/**
 * @author Kris | 13/01/2019 15:46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
class GrandExchangeOfferExecutor {
    private static final Logger transactionLogger = LoggerFactory.getLogger("Grand Exchange Transactions Logger");

    static void refresh(final ExchangeOffer offer) {
        check(offer);
    }

    private static void check(final ExchangeOffer offer) {
        final List<ExchangeOffer> matchingOffers = getMatchingOffers(offer);
        if (matchingOffers.isEmpty()) return;
        final ExchangeType type = offer.getType();
        sort(type, matchingOffers);
        if (type.equals(ExchangeType.BUYING)) {
            processPurchaseOffer(offer, matchingOffers);
        } else {
            processSellOffer(offer, matchingOffers);
        }
    }

    private static void processPurchaseOffer(final ExchangeOffer buyOffer, final List<ExchangeOffer> matchingOffers) {
        final int originalAmount = buyOffer.getRemainder();
        int amount = originalAmount;
        final int id = buyOffer.getItem().getId();
        for (int i = matchingOffers.size() - 1; i >= 0; i--) {
            final ExchangeOffer sellOffer = matchingOffers.get(i);
            if (sellOffer == null) {
                continue;
            }
            final int offerAmount = Math.min(amount, sellOffer.getRemainder());
            if (offerAmount == 0) {
                continue;
            }
            amount -= offerAmount;
            final int exchangePrice = sellOffer.getPrice();
            sellOffer.setAmount(sellOffer.getAmount() + offerAmount);
            sellOffer.refreshUpdateTime();
            buyOffer.refreshUpdateTime();
            final ContainerResult result = sellOffer.getContainer().add(new Item(995, (offerAmount * exchangePrice)));
            GrandExchangePriceManager.post(id, offerAmount, exchangePrice);
            sellOffer.setTotalPrice(sellOffer.getTotalPrice() + result.getSucceededAmount());
            WorldTasksManager.schedule(sellOffer::updateAndInform);
            buyOffer.setAmount(buyOffer.getAmount() + offerAmount);
            buyOffer.getContainer().add(new Item(id, offerAmount));
            final int returnedAmount = (offerAmount * buyOffer.getPrice()) - result.getSucceededAmount();
            if (returnedAmount > 0) {
                buyOffer.getContainer().add(new Item(995, returnedAmount));
            }
            buyOffer.setTotalPrice(buyOffer.getTotalPrice() + result.getSucceededAmount());
            try {
                transactionLogger.info(StringFormatUtil.formatString(buyOffer.getUsername()) + " bought " + offerAmount + " x " + ItemDefinitions.getOrThrow(id).getName() + "(" + id + ") for " + StringFormatUtil.format(exchangePrice) + " each from " + StringFormatUtil.formatString(sellOffer.getUsername()) + " for a total of " + StringFormatUtil.format(offerAmount * exchangePrice) + ". Offer progress: " + buyOffer.getAmount() + "/" + buyOffer.getItem().getAmount() + ".");
                GameLogger.log(Level.INFO, () -> new GameLogMessage.GrandExchangeTransaction.Purchase(
                        Instant.Companion.now(),
                        buyOffer.getUsername(),
                        sellOffer.getUsername(),
                        new Item(id, offerAmount),
                        exchangePrice
                ));
            } catch (Exception e) {
                DefaultLogger.logger.error("", e);
            }
            if (amount <= 0) {
                break;
            }
        }
        if (originalAmount == amount) {
            return;
        }
        WorldTasksManager.schedule(buyOffer::updateAndInform);
    }

    private static void processSellOffer(final ExchangeOffer sellOffer, final List<ExchangeOffer> matchingOffers) {
        final int originalAmount = sellOffer.getRemainder();
        int amount = originalAmount;
        final int id = sellOffer.getItem().getId();
        for (int i = matchingOffers.size() - 1; i >= 0; i--) {
            final ExchangeOffer buyOffer = matchingOffers.get(i);
            if (buyOffer == null) {
                continue;
            }
            final int offerAmount = Math.min(amount, buyOffer.getRemainder());
            if (offerAmount == 0) {
                continue;
            }
            amount -= offerAmount;
            final int exchangePrice = buyOffer.getPrice();
            sellOffer.refreshUpdateTime();
            buyOffer.refreshUpdateTime();
            sellOffer.setAmount(sellOffer.getAmount() + offerAmount);
            final ContainerResult result = sellOffer.getContainer().add(new Item(995, (offerAmount * exchangePrice)));
            GrandExchangePriceManager.post(id, offerAmount, exchangePrice);
            sellOffer.setTotalPrice(sellOffer.getTotalPrice() + result.getSucceededAmount());
            WorldTasksManager.schedule(buyOffer::updateAndInform);
            buyOffer.setAmount(buyOffer.getAmount() + offerAmount);
            buyOffer.getContainer().add(new Item(id, offerAmount));
            final int returnedAmount = (offerAmount * buyOffer.getPrice()) - result.getSucceededAmount();
            if (returnedAmount > 0) {
                buyOffer.getContainer().add(new Item(995, returnedAmount));
            }
            buyOffer.setTotalPrice(buyOffer.getTotalPrice() + result.getSucceededAmount());
            try {
                transactionLogger.info(StringFormatUtil.formatString(sellOffer.getUsername()) + " sold " + offerAmount + " x " + ItemDefinitions.getOrThrow(id).getName() + "(" + id + ") for " + StringFormatUtil.format(exchangePrice) + " each to " + StringFormatUtil.formatString(buyOffer.getUsername()) + " for a total of " + StringFormatUtil.format(offerAmount * exchangePrice) + ". Offer progress: " + sellOffer.getAmount() + "/" + sellOffer.getItem().getAmount() + ".");
                GameLogger.log(Level.INFO, () -> new GameLogMessage.GrandExchangeTransaction.Sell(
                        Instant.Companion.now(),
                        sellOffer.getUsername(),
                        buyOffer.getUsername(),
                        new Item(id, offerAmount),
                        exchangePrice
                ));
            } catch (Exception e) {
                DefaultLogger.logger.error("", e);
            }
            if (amount <= 0) {
                break;
            }
        }
        if (amount == originalAmount) {
            return;
        }
        WorldTasksManager.schedule(sellOffer::updateAndInform);
    }

    private static void sort(final ExchangeType type, final List<ExchangeOffer> matchingOffers) {
        if (matchingOffers.isEmpty()) return;
        if (type.equals(ExchangeType.BUYING)) {
            matchingOffers.sort((a, b) -> {
                final int offset = Integer.compare(b.getPrice(), a.getPrice());
                return offset == 0 ? Long.compare(b.getTime(), a.getTime()) : offset;
            });
        } else {
            matchingOffers.sort((a, b) -> {
                final int offset = Integer.compare(a.getPrice(), b.getPrice());
                return offset == 0 ? Long.compare(b.getTime(), a.getTime()) : offset;
            });
        }
    }

    private static List<ExchangeOffer> getMatchingOffers(final ExchangeOffer offer) {
        final Map<String, Int2ObjectOpenHashMap<ExchangeOffer>> offers = GrandExchangeHandler.getAllOffers();
        final ArrayList<ExchangeOffer> matchingOffers = new ArrayList<ExchangeOffer>();
        final Iterator<Map.Entry<String, Int2ObjectOpenHashMap<ExchangeOffer>>> iterator = offers.entrySet().iterator();
        final int id = offer.getItem().getId();
        final ExchangeType type = offer.getType();
        final int price = offer.getPrice();
        final long lowestAcceptableTime = System.currentTimeMillis() - OFFER_TIMEOUT_DELAY;
        while (iterator.hasNext()) {
            final Map.Entry<String, Int2ObjectOpenHashMap<ExchangeOffer>> next = iterator.next();
            final String username = next.getKey();
            if (username.equals(offer.getUsername())) {
                continue;
            }
            final Int2ObjectOpenHashMap<ExchangeOffer> pendingOffers = next.getValue();
            pendingOffers.values().forEach(o -> {
                if (o == null || o.isAborted() || o.isCancelled()) {
                    return;
                }
                final Item item = o.getItem();
                if (item == null || item.getId() != id || type.equals(o.getType())) {
                    return;
                }
                if (type.equals(ExchangeType.BUYING) ? (price < o.getPrice()) : (price > o.getPrice())) return;
                if (o.getLastUpdateTime() < lowestAcceptableTime) {
                    return;
                }
                matchingOffers.add(o);
            });
        }
        return matchingOffers;
    }

}
