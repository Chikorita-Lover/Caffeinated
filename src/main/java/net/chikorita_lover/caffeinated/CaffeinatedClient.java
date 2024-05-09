package net.chikorita_lover.caffeinated;

import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.client.render.RenderLayer;

public class CaffeinatedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(CaffeinatedBlocks.COFFEE_SHRUB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CaffeinatedBlocks.FLOWERING_COFFEE_SHRUB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CaffeinatedBlocks.POTTED_COFFEE_SHRUB, RenderLayer.getCutout());

        ParticleFactoryRegistry.getInstance().register(CaffeinatedParticleTypes.COFFEE_POP, BubblePopParticle.Factory::new);
    }
}
