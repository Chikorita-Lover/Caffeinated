package com.chikoritalover.caffeinated.client;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.CaffeinatedClient;
import com.chikoritalover.caffeinated.entity.CivetEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class CivetEntityRenderer extends MobEntityRenderer<CivetEntity, CivetEntityModel<CivetEntity>> {
    private static final Identifier TEXTURE = new Identifier(Caffeinated.MODID, "textures/entity/civet/civet.png");

    public CivetEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CivetEntityModel<>(context.getPart(CaffeinatedClient.MODEL_CIVET_LAYER)), 0.4F);
    }

    public Identifier getTexture(CivetEntity civetEntity) {
        return TEXTURE;
    }
}
