package net.chikorita_lover.caffeinated.data;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.block.CauldronCampfireBlock;
import net.chikorita_lover.caffeinated.block.TiramisuBlock;
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

import java.util.Optional;

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

    private static void registerTiramisu(BlockStateModelGenerator generator) {
        generator.registerItemModel(CaffeinatedBlocks.TIRAMISU.asItem());
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(CaffeinatedBlocks.TIRAMISU).coordinate(BlockStateVariantMap.create(TiramisuBlock.SLICES).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(CaffeinatedBlocks.TIRAMISU, "_slice3"))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(CaffeinatedBlocks.TIRAMISU, "_slice2"))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(CaffeinatedBlocks.TIRAMISU, "_slice1"))).register(4, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(CaffeinatedBlocks.TIRAMISU)))));
    }

    private static void registerCauldronCampfire(BlockStateModelGenerator blockStateModelGenerator, Block cauldronCampfire, Block campfire) {
        Identifier identifier = Caffeinated.of("block/cauldron_campfire_off");
        Identifier identifier2 = new Model(Optional.of(Caffeinated.of("block/template_cauldron_campfire")), Optional.empty(), TextureKey.FIRE, TextureKey.LIT_LOG).upload(cauldronCampfire, TextureMap.campfire(campfire), blockStateModelGenerator.modelCollector);
        Identifier identifier3 = Caffeinated.of("block/cauldron_campfire_off_filled");
        Identifier identifier4 = new Model(Optional.of(Caffeinated.of("block/template_cauldron_campfire_filled")), Optional.empty(), TextureKey.FIRE, TextureKey.LIT_LOG).upload(ModelIds.getBlockSubModelId(cauldronCampfire, "_filled"), TextureMap.campfire(campfire), blockStateModelGenerator.modelCollector);
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(cauldronCampfire).coordinate(BlockStateModelGenerator.createSouthDefaultHorizontalRotationStates()).coordinate(BlockStateVariantMap.create(Properties.LIT, CauldronCampfireBlock.FILLED).register(false, false, BlockStateVariant.create().put(VariantSettings.MODEL, identifier)).register(true, false, BlockStateVariant.create().put(VariantSettings.MODEL, identifier2)).register(false, true, BlockStateVariant.create().put(VariantSettings.MODEL, identifier3)).register(true, true, BlockStateVariant.create().put(VariantSettings.MODEL, identifier4))));
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        generator.registerSimpleCubeAll(CaffeinatedBlocks.COFFEE_BEAN_BLOCK);
        generator.registerSimpleCubeAll(CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
        generator.registerSingleton(CaffeinatedBlocks.COFFEE_BERRY_CRATE, new TextureMap().put(TextureKey.SIDE, Caffeinated.of("block/coffee_berry_crate_side")).put(TextureKey.TOP, Caffeinated.of("block/coffee_berry_crate_top")).put(TextureKey.BOTTOM, Identifier.of("farmersdelight", "block/crate_bottom")), Models.CUBE_BOTTOM_TOP);

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(CaffeinatedBlocks.COFFEE_CAULDRON).coordinate(BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL1.upload(CaffeinatedBlocks.COFFEE_CAULDRON, "_level1", TextureMap.cauldron(Caffeinated.of("block/coffee")), generator.modelCollector))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL2.upload(CaffeinatedBlocks.COFFEE_CAULDRON, "_level2", TextureMap.cauldron(Caffeinated.of("block/coffee")), generator.modelCollector))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_FULL.upload(CaffeinatedBlocks.COFFEE_CAULDRON, "_full", TextureMap.cauldron(Caffeinated.of("block/coffee")), generator.modelCollector)))));

        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON).coordinate(BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL1.upload(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, "_level1", TextureMap.cauldron(Caffeinated.of("block/ground_coffee_cauldron")), generator.modelCollector))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL2.upload(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, "_level2", TextureMap.cauldron(Caffeinated.of("block/ground_coffee_cauldron")), generator.modelCollector))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_FULL.upload(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, "_full", TextureMap.cauldron(Caffeinated.of("block/ground_coffee_cauldron")), generator.modelCollector)))));

        registerCoffeeShrub(generator, CaffeinatedBlocks.COFFEE_SHRUB, CaffeinatedBlocks.POTTED_COFFEE_SHRUB, Properties.AGE_3, 0, 0, 1, 1);

        registerTiramisu(generator);

        for (Block campfireBlock : CauldronCampfireBlock.CAMPFIRE_TO_CAULDRON_CAMPFIRE.keySet()) {
            registerCauldronCampfire(generator, CauldronCampfireBlock.CAMPFIRE_TO_CAULDRON_CAMPFIRE.get(campfireBlock), campfireBlock);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(CaffeinatedItems.COFFEE_BOTTLE, Models.GENERATED);
        generator.register(CaffeinatedItems.LATTE_COFFEE_BOTTLE, Models.GENERATED);
        generator.register(CaffeinatedItems.CAFE_MIEL_COFFEE_BOTTLE, Models.GENERATED);
        generator.register(CaffeinatedItems.TIRAMISU_SLICE, Models.GENERATED);
        generator.register(CaffeinatedItems.COFFEE_BEANS, Models.GENERATED);
        generator.register(CaffeinatedItems.GROUND_COFFEE, Models.GENERATED);
        generator.register(CaffeinatedItems.JAVA_BANNER_PATTERN, Models.GENERATED);
        generator.register(CaffeinatedItems.CIVET_SPAWN_EGG, Models.item("template_spawn_egg"));
    }
}
