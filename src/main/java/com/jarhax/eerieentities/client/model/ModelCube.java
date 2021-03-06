package com.jarhax.eerieentities.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCube extends ModelBase {
    
    private final ModelRenderer body;
    
    public ModelCube() {
        
        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
    }
    
    @Override
    public void render (Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.translate(0.0F, 1.0F, 0.0F);
        this.body.render(scale);
    }
}