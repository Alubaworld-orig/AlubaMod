package com.aluba.pvp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("alubapvp")
public class AlubaRenderer {
    public AlubaRenderer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        // Рендерим только на этапе текста (чтобы было поверх всего)
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getInstance();
        MatrixStack ms = event.getMatrixStack();
        int x = mc.getMainWindow().getScaledWidth() / 2;
        int y = mc.getMainWindow().getScaledHeight() / 2;

        // 1. РИСУЕМ ПРИЦЕЛ
        AbstractGui.fill(ms, x - 1, y - 1, x + 1, y + 1, 0xFFFF0000); // Центр
        AbstractGui.fill(ms, x - 4, y, x - 2, y + 1, 0xFFFF0000);     // Лево
        AbstractGui.fill(ms, x + 3, y, x + 5, y + 1, 0xFFFF0000);     // Право

        // 2. СТАТИСТИКА ВРАГА (TARGET HUD)
        if (mc.objectMouseOver instanceof EntityRayTraceResult) {
            Entity entity = ((EntityRayTraceResult) mc.objectMouseOver).getEntity();
            if (entity instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) entity;
                
                String name = target.getName().getString();
                String hp = String.format("%.1f HP", target.getHealth());
                
                // Рисуем темную подложку под инфу
                AbstractGui.fill(ms, x + 10, y - 20, x + 80, y + 5, 0x88000000);
                
                // Пишем Имя и HP
                mc.fontRenderer.drawString(ms, name, x + 15, y - 15, 0xFFFFFF);
                mc.fontRenderer.drawString(ms, hp, x + 15, y - 5, 0xFF0000);
                
                // Полоска здоровья
                float healthWidth = (target.getHealth() / target.getMaxHealth()) * 60;
                AbstractGui.fill(ms, x + 15, y + 2, x + 15 + (int)healthWidth, y + 4, 0xFF0000);
            }
        }
    }
          }
