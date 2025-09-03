package net.chikorita_lover.caffeinated;

import net.chikorita_lover.caffeinated.data.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CaffeinatedDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(CaffeinatedAdvancementProvider::new);
        pack.addProvider(CaffeinatedBlockTagProvider::new);
        pack.addProvider(CaffeinatedItemTagProvider::new);
        pack.addProvider(CaffeinatedLootTableProvider::new);
        pack.addProvider(CaffeinatedModelProvider::new);
        pack.addProvider(CaffeinatedRecipeProvider::new);
    }
}
