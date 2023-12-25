package com.chikoritalover.caffeinated.client;

import com.chikoritalover.caffeinated.block.entity.CauldronCampfireBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class CauldronCampfireBlockEntityRenderer implements BlockEntityRenderer<CauldronCampfireBlockEntity>  {
    @Override
    public void render(CauldronCampfireBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

    }
}
