package com.chikoritalover.caffeinated.registry;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public class CaffeinatedTradeOffers {
    public static void register() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 3, factories -> {
            factories.add(new TradeOffers.BuyForOneEmeraldFactory(CaffeinatedItems.COFFEE_BERRIES, 14, 12, 20));
        });
        TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
            factories.add(new TradeOffers.SellItemFactory(CaffeinatedItems.COFFEE_BERRIES, 1, 1, 12, 1));
        });
    }
}
