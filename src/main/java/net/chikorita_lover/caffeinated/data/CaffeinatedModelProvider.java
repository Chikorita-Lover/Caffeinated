package net.chikorita_lover.caffeinated.data;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

public class CaffeinatedModelProvider extends FabricModelProvider {
    public CaffeinatedModelProvider(FabricDataOutput output) {
        super(output);
    }

    private static void registerCoffeeShrub(BlockStateModelGenerator blockStateModelGenerator, Block crop, Block pottedCrop, Property<Integer> ageProperty, int... ageTextureIndices) {
        if (ageProperty.getValues().size() != ageTextureIndices.length) {
            throw new IllegalArgumentException();
        } else {
            Int2ObjectMap<Identifier> int2ObjectMap = new Int2ObjectOpenHashMap<>();
            BlockStateVariantMap blockStateVariantMap = BlockStateVariantMap.create(ageProperty).register((integer) -> {
                int i = ageTextureIndices[integer];
                Identifier identifier = int2ObjectMap.computeIfAbsent(i, (j) -> blockStateModelGenerator.createSubModel(crop, "_stage" + i, Models.CROSS, TextureMap::cross));
                return BlockStateVariant.create().put(VariantSettings.MODEL, identifier);
            });
            blockStateModelGenerator.registerItemModel(crop.asItem());
            blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(crop).coordinate(blockStateVariantMap));
        }

        TextureMap textureMap = TextureMap.plant(TextureMap.getSubId(crop, "_stage1"));
        Identifier identifier = BlockStateModelGenerator.TintType.NOT_TINTED.getFlowerPotCrossModel().upload(pottedCrop, textureMap, blockStateModelGenerator.modelCollector);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(pottedCrop, identifier));
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        generator.registerSimpleCubeAll(CaffeinatedBlocks.COFFEE_BEAN_BLOCK);
        generator.registerSimpleCubeAll(CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
        generator.registerSingleton(CaffeinatedBlocks.COFFEE_BERRY_CRATE, new TextureMap().put(TextureKey.SIDE, Caffeinated.of("block/coffee_berry_crate_side")).put(TextureKey.TOP, Caffeinated.of("block/coffee_berry_crate_top")).put(TextureKey.BOTTOM, Identifier.of("farmersdelight", "block/crate_bottom")), Models.CUBE_BOTTOM_TOP);

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(CaffeinatedBlocks.COFFEE_CAULDRON).coordinate(BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL1.upload(CaffeinatedBlocks.COFFEE_CAULDRON, "_level1", TextureMap.cauldron(Caffeinated.of("block/coffee")), generator.modelCollector))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL2.upload(CaffeinatedBlocks.COFFEE_CAULDRON, "_level2", TextureMap.cauldron(Caffeinated.of("block/coffee")), generator.modelCollector))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_FULL.upload(CaffeinatedBlocks.COFFEE_CAULDRON, "_full", TextureMap.cauldron(Caffeinated.of("block/coffee")), generator.modelCollector)))));

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON).coordinate(BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL1.upload(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, "_level1", TextureMap.cauldron(Caffeinated.of("block/ground_coffee_cauldron")), generator.modelCollector))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL2.upload(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, "_level2", TextureMap.cauldron(Caffeinated.of("block/ground_coffee_cauldron")), generator.modelCollector))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_FULL.upload(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, "_full", TextureMap.cauldron(Caffeinated.of("block/ground_coffee_cauldron")), generator.modelCollector)))));

        registerCoffeeShrub(generator, CaffeinatedBlocks.COFFEE_SHRUB, CaffeinatedBlocks.POTTED_COFFEE_SHRUB, Properties.AGE_3, 0, 0, 1, 1);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(CaffeinatedItems.COFFEE_BERRIES, Models.GENERATED);
        itemModelGenerator.register(CaffeinatedItems.COFFEE_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(CaffeinatedItems.TIRAMISU, Models.GENERATED);
        itemModelGenerator.register(CaffeinatedItems.GROUND_COFFEE, Models.GENERATED);
        itemModelGenerator.register(CaffeinatedItems.JAVA_BANNER_PATTERN, Models.GENERATED);
    }
}
