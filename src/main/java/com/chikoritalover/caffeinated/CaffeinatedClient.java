package com.chikoritalover.caffeinated;

import com.chikoritalover.caffeinated.registry.ModBlocks;
import com.chikoritalover.caffeinated.registry.ModParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.particle.BubblePopParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class CaffeinatedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COFFEE_SHRUB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FLOWERING_COFFEE_SHRUB, RenderLayer.getCutout());

        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new Identifier(Caffeinated.MODID, "particle/coffee_pop_0"));
            registry.register(new Identifier(Caffeinated.MODID, "particle/coffee_pop_1"));
            registry.register(new Identifier(Caffeinated.MODID, "particle/coffee_pop_2"));
            registry.register(new Identifier(Caffeinated.MODID, "particle/coffee_pop_3"));
            registry.register(new Identifier(Caffeinated.MODID, "particle/coffee_pop_4"));
        }));

        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.COFFEE_POP, BubblePopParticle.Factory::new);

    }
}
