package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.block.CoffeeCauldronBlock;
import com.chikoritalover.caffeinated.block.CoffeeShrubBlock;
import com.chikoritalover.caffeinated.block.FloweringCoffeeShrubBlock;
import com.chikoritalover.caffeinated.block.GroundCoffeeCauldronBlock;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final Block COFFEE_BEAN_BLOCK = new Block(AbstractBlock.Settings.of(Material.WOOD, MapColor.SPRUCE_BROWN).sounds(ModBlockSoundGroup.COFFEE_BEAN_BLOCK).strength(1.8F, 3.0F));
    public static final Block GROUND_COFFEE_BLOCK = new SandBlock(8473899, AbstractBlock.Settings.of(Material.AGGREGATE, MapColor.SPRUCE_BROWN).strength(0.5F).sounds(ModBlockSoundGroup.GROUND_COFFEE_BLOCK));

    public static final Block COFFEE_CAULDRON = new CoffeeCauldronBlock(AbstractBlock.Settings.copy(Blocks.CAULDRON), ModCauldronBehavior.COFFEE_CAULDRON_BEHAVIOR);
    public static final Block GROUND_COFFEE_CAULDRON = new GroundCoffeeCauldronBlock(AbstractBlock.Settings.copy(Blocks.CAULDRON).ticksRandomly(), ModCauldronBehavior.GROUND_COFFEE_CAULDRON_BEHAVIOR);

    public static final Block COFFEE_SHRUB = new CoffeeShrubBlock(AbstractBlock.Settings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.AZALEA));
    public static final Block FLOWERING_COFFEE_SHRUB = new FloweringCoffeeShrubBlock(AbstractBlock.Settings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.FLOWERING_AZALEA));

    public static void register() {
        Registry.register(Registry.BLOCK, new Identifier(Caffeinated.MODID, "coffee_bean_block"), COFFEE_BEAN_BLOCK);
        Registry.register(Registry.BLOCK, new Identifier(Caffeinated.MODID, "ground_coffee_block"), GROUND_COFFEE_BLOCK);

        Registry.register(Registry.BLOCK, new Identifier(Caffeinated.MODID, "coffee_cauldron"), COFFEE_CAULDRON);
        Registry.register(Registry.BLOCK, new Identifier(Caffeinated.MODID, "ground_coffee_cauldron"), GROUND_COFFEE_CAULDRON);

        Registry.register(Registry.BLOCK, new Identifier(Caffeinated.MODID, "coffee_shrub"), COFFEE_SHRUB);
        Registry.register(Registry.BLOCK, new Identifier(Caffeinated.MODID, "flowering_coffee_shrub"), FLOWERING_COFFEE_SHRUB);
    }

    public static void registerFlammableBlocks() {
        FlammableBlockRegistry.getDefaultInstance().add(COFFEE_BEAN_BLOCK, 20, 30);

        FlammableBlockRegistry.getDefaultInstance().add(COFFEE_SHRUB, 60, 15);
        FlammableBlockRegistry.getDefaultInstance().add(FLOWERING_COFFEE_SHRUB, 60, 15);
    }
}
