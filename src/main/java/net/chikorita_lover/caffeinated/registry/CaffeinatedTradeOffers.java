package net.chikorita_lover.caffeinated.registry;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.village.TradeOffers;

public class CaffeinatedTradeOffers {
    public static void register() {
        TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
            factories.add(new TradeOffers.SellItemFactory(CaffeinatedItems.COFFEE_BERRIES, 3, 1, 8, 1));
        });
        TradeOfferHelper.registerRebalancedWanderingTraderOffers(builder -> {
            builder.addOffersToPool(TradeOfferHelper.WanderingTraderOffersBuilder.BUY_ITEMS_POOL, new TradeOffers.BuyItemFactory(CaffeinatedItems.COFFEE_BOTTLE, 1, 2, 1, 3));
        });
        TradeOfferHelper.registerRebalancedWanderingTraderOffers(builder -> {
            builder.addOffersToPool(TradeOfferHelper.WanderingTraderOffersBuilder.SELL_COMMON_ITEMS_POOL, new TradeOffers.SellItemFactory(CaffeinatedItems.COFFEE_BERRIES, 3, 1, 8, 1));
        });
    }
}
