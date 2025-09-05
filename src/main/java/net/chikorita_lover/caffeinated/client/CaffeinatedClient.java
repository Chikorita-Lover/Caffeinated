package net.chikorita_lover.caffeinated.client;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedEntityTypes;
import net.chikorita_lover.caffeinated.registry.CaffeinatedParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class CaffeinatedClient implements ClientModInitializer {
    public static EntityModelLayer MODEL_CIVET_LAYER = new EntityModelLayer(Caffeinated.of("civet"), "main");

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(CaffeinatedBlocks.COFFEE_SHRUB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CaffeinatedBlocks.FLOWERING_COFFEE_SHRUB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CaffeinatedBlocks.POTTED_COFFEE_SHRUB, RenderLayer.getCutout());

        EntityModelLayerRegistry.registerModelLayer(MODEL_CIVET_LAYER, CivetEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(CaffeinatedEntityTypes.CIVET, CivetEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(CaffeinatedParticleTypes.COFFEE_POP, BubblePopParticle.Factory::new);
    }
}
