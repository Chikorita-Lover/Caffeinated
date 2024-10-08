package net.chikorita_lover.caffeinated.registry;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public class CaffeinatedTradeOffers {
    public static void register() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 3, factories -> {
            factories.add(new TradeOffers.BuyItemFactory(CaffeinatedItems.COFFEE_BERRIES, 8, 12, 20));
        });
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 5, factories -> {
            factories.add(new TradeOffers.SellItemFactory(CaffeinatedItems.TIRAMISU, 3, 1, 12, 30));
        });
        TradeOfferHelper.registerWanderingTraderOffers(1, factories -> {
            factories.add(new TradeOffers.SellItemFactory(CaffeinatedItems.COFFEE_BEANS, 1, 1, 12, 1));
        });
    }
}
