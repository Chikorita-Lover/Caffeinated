package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.block.CoffeeCauldronBlock;
import com.chikoritalover.caffeinated.block.CoffeeShrubBlock;
import com.chikoritalover.caffeinated.block.FloweringCoffeeShrubBlock;
import com.chikoritalover.caffeinated.block.GroundCoffeeCauldronBlock;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class CaffeinatedBlocks {
    public static final Block COFFEE_SHRUB = register("coffee_shrub", new CoffeeShrubBlock(AbstractBlock.Settings.create().ticksRandomly().noCollision().sounds(BlockSoundGroup.AZALEA)));
    public static final Block FLOWERING_COFFEE_SHRUB = register("flowering_coffee_shrub", new FloweringCoffeeShrubBlock(AbstractBlock.Settings.create().ticksRandomly().noCollision().sounds(BlockSoundGroup.FLOWERING_AZALEA)));
    public static final Block POTTED_COFFEE_SHRUB = register("potted_coffee_shrub", new FlowerPotBlock(COFFEE_SHRUB, AbstractBlock.Settings.create().breakInstantly().nonOpaque()));
    public static final Block COFFEE_BEAN_BLOCK = registerBlockWithItem("coffee_bean_block", new Block(AbstractBlock.Settings.create().mapColor(MapColor.SPRUCE_BROWN).sounds(CaffeinatedBlockSoundGroup.COFFEE_BEAN_BLOCK).strength(1.8F, 3.0F)));
    public static final Block GROUND_COFFEE_BLOCK = registerBlockWithItem("ground_coffee_block", new SandBlock(8473899, AbstractBlock.Settings.create().mapColor(MapColor.SPRUCE_BROWN).strength(0.5F).sounds(CaffeinatedBlockSoundGroup.GROUND_COFFEE_BLOCK)));
    public static final Block COFFEE_BERRY_CRATE = registerBlockWithItem("coffee_berry_crate", new Block(AbstractBlock.Settings.create().mapColor(MapColor.DARK_CRIMSON).sounds(BlockSoundGroup.WOOD).strength(2.0F, 3.0F)));
    public static final Block COFFEE_CAULDRON = register("coffee_cauldron", new CoffeeCauldronBlock(AbstractBlock.Settings.copy(Blocks.CAULDRON), CaffeinatedCauldronBehavior.COFFEE_CAULDRON_BEHAVIOR));
    public static final Block GROUND_COFFEE_CAULDRON = register("ground_coffee_cauldron", new GroundCoffeeCauldronBlock(AbstractBlock.Settings.copy(Blocks.CAULDRON).ticksRandomly()));

    public static Block registerBlockWithItem(String id, Block block) {
        Block block2 = register(id, block);
        CaffeinatedItems.register(block2);
        return block2;
    }

    public static Block register(String id, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(Caffeinated.MODID, id), block);
    }

    public static void register() {
        registerFlammableBlocks();
    }

    public static void registerFlammableBlocks() {
        FlammableBlockRegistry.getDefaultInstance().add(COFFEE_SHRUB, 60, 15);
        FlammableBlockRegistry.getDefaultInstance().add(FLOWERING_COFFEE_SHRUB, 60, 15);
        FlammableBlockRegistry.getDefaultInstance().add(COFFEE_BEAN_BLOCK, 20, 30);
    }
}
