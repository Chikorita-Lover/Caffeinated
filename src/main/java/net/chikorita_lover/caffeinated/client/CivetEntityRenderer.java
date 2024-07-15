package net.chikorita_lover.caffeinated.client;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.entity.CivetEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class CivetEntityRenderer extends MobEntityRenderer<CivetEntity, CivetEntityModel<CivetEntity>> {
    private static final Identifier TEXTURE = Caffeinated.of("textures/entity/civet/civet.png");

    public CivetEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CivetEntityModel<>(context.getPart(CaffeinatedClient.MODEL_CIVET_LAYER)), 0.4F);
    }

    @Override
    public Identifier getTexture(CivetEntity civetEntity) {
        return TEXTURE;
    }
}
