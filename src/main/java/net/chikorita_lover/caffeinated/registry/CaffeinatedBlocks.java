package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.block.*;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ColorCode;
import net.minecraft.util.DyeColor;

public class CaffeinatedBlocks {
    public static final Block COFFEE_SHRUB = register("coffee_shrub", new CoffeeShrubBlock(AbstractBlock.Settings.create().ticksRandomly().noCollision().sounds(BlockSoundGroup.AZALEA)));
    public static final Block FLOWERING_COFFEE_SHRUB = register("flowering_coffee_shrub", new FloweringCoffeeShrubBlock(AbstractBlock.Settings.create().ticksRandomly().noCollision().sounds(BlockSoundGroup.FLOWERING_AZALEA)));
    public static final Block POTTED_COFFEE_SHRUB = register("potted_coffee_shrub", new FlowerPotBlock(COFFEE_SHRUB, AbstractBlock.Settings.create().breakInstantly().nonOpaque()));
    public static final Block COFFEE_BEAN_BLOCK = registerBlockWithItem("coffee_bean_block", new Block(AbstractBlock.Settings.create().mapColor(MapColor.SPRUCE_BROWN).sounds(CaffeinatedBlockSoundGroup.COFFEE_BEAN_BLOCK).strength(1.8F, 3.0F)));
    public static final Block GROUND_COFFEE_BLOCK = registerBlockWithItem("ground_coffee_block", new ColoredFallingBlock(new ColorCode(0x814D2B), AbstractBlock.Settings.create().mapColor(MapColor.SPRUCE_BROWN).strength(0.5F).sounds(CaffeinatedBlockSoundGroup.GROUND_COFFEE_BLOCK)));
    public static final Block COFFEE_STAINED_WOOL = registerBlockWithItem("coffee_stained_wool", new Block(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL).mapColor(MapColor.OFF_WHITE)));
    public static final Block COFFEE_STAINED_CARPET = registerBlockWithItem("coffee_stained_carpet", new DyedCarpetBlock(DyeColor.WHITE, AbstractBlock.Settings.copy(Blocks.WHITE_CARPET).mapColor(MapColor.OFF_WHITE)));
    public static final Block COFFEE_BERRY_CRATE = registerBlockWithItem("coffee_berry_crate", new Block(AbstractBlock.Settings.create().mapColor(MapColor.DARK_CRIMSON).sounds(BlockSoundGroup.WOOD).strength(2.0F, 3.0F)));
    public static final Block COFFEE_CAULDRON = register("coffee_cauldron", new CoffeeCauldronBlock(AbstractBlock.Settings.copy(Blocks.CAULDRON)));
    public static final Block GROUND_COFFEE_CAULDRON = register("ground_coffee_cauldron", new CoffeeCauldronBlock(AbstractBlock.Settings.copy(Blocks.CAULDRON)));

    public static final Block CAULDRON_CAMPFIRE = register("cauldron_campfire", createCauldronCampfire(Blocks.CAMPFIRE, ParticleTypes.FLAME));
    public static final Block SOUL_CAULDRON_CAMPFIRE = register("soul_cauldron_campfire", createCauldronCampfire(Blocks.SOUL_CAMPFIRE, ParticleTypes.SOUL_FIRE_FLAME));

    public static final Block TIRAMISU = register("tiramisu", new TiramisuBlock(AbstractBlock.Settings.copy(Blocks.CAKE)));

    private static CauldronCampfireBlock createCauldronCampfire(Block campfireBlock, SimpleParticleType particle) {
        Block cauldronBlock = Blocks.CAULDRON;
        return new CauldronCampfireBlock(campfireBlock, particle, AbstractBlock.Settings.copy(campfireBlock).sounds(cauldronBlock.getDefaultState().getSoundGroup()));
    }

    private static Block registerBlockWithItem(String id, Block block) {
        register(id, block);
        Registry.register(Registries.ITEM, Caffeinated.of(id), new BlockItem(block, new Item.Settings()));
        return block;
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registries.BLOCK, Caffeinated.of(id), block);
    }

    public static void register() {
        registerFlammableBlocks();
    }

    private static void registerFlammableBlocks() {
        FlammableBlockRegistry.getDefaultInstance().add(COFFEE_SHRUB, 60, 15);
        FlammableBlockRegistry.getDefaultInstance().add(FLOWERING_COFFEE_SHRUB, 60, 15);
        FlammableBlockRegistry.getDefaultInstance().add(COFFEE_BEAN_BLOCK, 20, 30);
        FlammableBlockRegistry.getDefaultInstance().add(COFFEE_STAINED_WOOL, 30, 60);
        FlammableBlockRegistry.getDefaultInstance().add(COFFEE_STAINED_CARPET, 60, 20);
    }
}
