package net.chikorita_lover.caffeinated.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class CivetEntityModel<T extends Entity> extends AnimalModel<T> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart upperTail;
    private final ModelPart lowerTail;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;

    public CivetEntityModel(ModelPart root) {
        super(true, 9.5F, 3.0F);
        this.head = root.getChild(EntityModelPartNames.HEAD);
        this.body = root.getChild(EntityModelPartNames.BODY);
        this.upperTail = root.getChild("upper_tail");
        this.lowerTail = this.upperTail.getChild("lower_tail");
        this.leftHindLeg = root.getChild(EntityModelPartNames.LEFT_HIND_LEG);
        this.rightHindLeg = root.getChild(EntityModelPartNames.RIGHT_HIND_LEG);
        this.leftFrontLeg = root.getChild(EntityModelPartNames.LEFT_FRONT_LEG);
        this.rightFrontLeg = root.getChild(EntityModelPartNames.RIGHT_FRONT_LEG);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-2.5F, -2.0F, -4.0F, 5.0F, 4.0F, 5.0F, new Dilation(0.0F)).uv(0, 21).cuboid(-1.0F, 0.0F, -6.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)).uv(0, 9).cuboid(-3.5F, -3.0F, -1.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)).uv(0, 9).mirrored().cuboid(1.5F, -3.0F, -1.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 17.5F, -7.0F));

        modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(20, 0).cuboid(-2.5F, -6.0F, -3.5F, 5.0F, 13.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 17.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        ModelPartData upper_tail = modelPartData.addChild("upper_tail", ModelPartBuilder.create().uv(38, 0).cuboid(-1.0F, -0.25F, -0.5F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 18.25F, 7.0F, 1.0472F, 0.0F, 0.0F));

        upper_tail.addChild("lower_tail", ModelPartBuilder.create().uv(46, 0).cuboid(-1.0F, 0.0F, 0.0F, 2.0F, 10.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 7.75F, -0.5F, 0.5236F, 0.0F, 0.0F));

        modelPartData.addChild(EntityModelPartNames.LEFT_HIND_LEG, ModelPartBuilder.create().uv(8, 9).mirrored().cuboid(-1.0F, -0.5F, -0.9F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(1.6F, 19.5F, 5.0F));

        modelPartData.addChild(EntityModelPartNames.RIGHT_HIND_LEG, ModelPartBuilder.create().uv(8, 9).cuboid(-1.0F, -0.5F, -0.9F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.6F, 19.5F, 5.0F));

        modelPartData.addChild(EntityModelPartNames.LEFT_FRONT_LEG, ModelPartBuilder.create().uv(0, 12).mirrored().cuboid(-1.0F, -0.5F, -0.9F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(1.7F, 17.3F, -5.0F));

        modelPartData.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, ModelPartBuilder.create().uv(0, 12).cuboid(-1.0F, -0.5F, -0.9F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.7F, 17.3F, -5.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body, this.upperTail, this.leftHindLeg, this.rightHindLeg, this.leftFrontLeg, this.rightFrontLeg);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * ((float) Math.PI / 180);
        this.head.yaw = headYaw * ((float) Math.PI / 180);
        this.rightHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4f * limbDistance;
        this.leftHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.rightFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.leftFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.lowerTail.pitch = 0.52359878F + 0.31415927F * MathHelper.cos(limbAngle) * limbDistance;
    }

    @Override
    public void animateModel(T entity, float limbAngle, float limbDistance, float tickDelta) {
        this.upperTail.pitch = 0.9F;
        if (entity.isInSneakingPose()) {
            this.upperTail.pitch = 1.5707964F;
            this.lowerTail.pitch = 1.5707964F;
        } else if (entity.isSprinting()) {
            this.upperTail.pitch = 1.5707964F;
            this.lowerTail.pitch = 1.5707964F;
        }
    }
}