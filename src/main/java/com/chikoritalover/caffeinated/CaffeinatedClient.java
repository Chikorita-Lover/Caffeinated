package com.chikoritalover.caffeinated;

import com.chikoritalover.caffeinated.block.CauldronCampfireBlock;
import com.chikoritalover.caffeinated.block.entity.CauldronCampfireBlockEntity;
import com.chikoritalover.caffeinated.registry.CaffeinatedBlocks;
import com.chikoritalover.caffeinated.registry.CaffeinatedParticleTypes;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Objects;

public class CaffeinatedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(CaffeinatedBlocks.COFFEE_SHRUB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CaffeinatedBlocks.FLOWERING_COFFEE_SHRUB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CaffeinatedBlocks.POTTED_COFFEE_SHRUB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CaffeinatedBlocks.CAULDRON_CAMPFIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CaffeinatedBlocks.SOUL_CAULDRON_CAMPFIRE, RenderLayer.getCutout());

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world == null || pos == null || world.getBlockEntity(pos) == null) {
                return BiomeColors.getWaterColor(world, pos);
            }
            return ((CauldronCampfireBlockEntity) world.getBlockEntity(pos)).getColor();
        }, CauldronCampfireBlock.CAMPFIRE_TO_CAULDRON_CAMPFIRE.values().toArray(new Block[0]));

        ParticleFactoryRegistry.getInstance().register(CaffeinatedParticleTypes.COFFEE_POP, BubblePopParticle.Factory::new);

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            Item item = stack.getItem();
            if (!stack.isFood() || !Objects.equals(Registries.ITEM.getId(item).getNamespace(), Caffeinated.MODID)) {
                return;
            }
            List<Pair<StatusEffectInstance, Float>> statusEffects = item.getFoodComponent().getStatusEffects();
            List<StatusEffectInstance> list = Lists.newArrayList();
            for (Pair<StatusEffectInstance, Float> statusEffect : statusEffects) {
                if (statusEffect.getSecond() == 1.0F) {
                    list.add(statusEffect.getFirst());
                }
            }
            if (!statusEffects.isEmpty()) {
                PotionUtil.buildTooltip(list, lines, 1.0F);
            }
        });
    }
}
