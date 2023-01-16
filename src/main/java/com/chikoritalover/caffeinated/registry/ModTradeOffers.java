package com.chikoritalover.caffeinated.registry;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public class ModTradeOffers {
    public static void register() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 3, factories -> {
            factories.add(new TradeOffers.BuyForOneEmeraldFactory(ModItems.COFFEE_BERRIES, 8, 12, 20));
        });
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 5, factories -> {
            factories.add(new TradeOffers.SellItemFactory(ModItems.TIRAMISU, 3, 1, 12, 30));
        });
        TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
            factories.add(new TradeOffers.SellItemFactory(ModItems.COFFEE_BEANS, 1, 1, 12, 1));
        });
    }
}
