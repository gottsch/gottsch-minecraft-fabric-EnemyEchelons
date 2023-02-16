/*
 * This file is part of  Enemy Echelons.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
 *
 * Enemy Echelons is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Enemy Echelons is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Enemy Echelons.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.fabric.eechelons.core.client;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import mod.gottsch.fabric.eechelons.EEchelons;
import mod.gottsch.fabric.eechelons.core.config.ClientConfig;
import mod.gottsch.fabric.eechelons.core.data.ILevelSupport;
import mod.gottsch.fabric.eechelons.core.event.HudEventHandler;
import mod.gottsch.fabric.eechelons.core.integration.WailaIntegration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

/**
 * This class was derived from Champions by TheIllusiveC4
 * @see <a href="https://github.com/TheIllusiveC4/Champions">Champions</a>
 *
 */
public class HudUtil {
	private static final int WAILA_INTEGRATION_XOFFSET = -80;
	private static final int CHAMPIONS_INTEGRATION_XOFFSET = -124;
	
	private static final int HUD_OFFSET_WIDTH = 32;
	private static final int HUD_OFFSET_HEIGHT = 4;
	private static final Identifier HUD_BG = new Identifier(EEchelons.MODID, "textures/gui/echelon_hud_bg.png");
	private static final Identifier HUD_DARK_BG = new Identifier(EEchelons.MODID, "textures/gui/echelon_hud_dark_bg.png");
	
	/**
	 * 
	 * @param matrixStack
	 * @param livingEntity
	 * @return
	 */
	public static boolean renderLevelBar(MatrixStack matrixStack, final LivingEntity livingEntity) {

		int level = ((ILevelSupport)livingEntity).getLevel();

		if (level > -1) {
			MinecraftClient client = MinecraftClient.getInstance();
			int i = client.getWindow().getScaledWidth();
			// middle of the screen
			int centerWidth = i / 2 - HUD_OFFSET_WIDTH;
			int centerHeight = HUD_OFFSET_HEIGHT;
			
			int xOffset = ClientConfig.hudXOffset;
			int yOffset = ClientConfig.hudYOffset;

			RenderSystem.defaultBlendFunc();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.enableBlend();
			RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
			RenderSystem.setShaderTexture(0, ClientConfig.useDarkHud ? HUD_DARK_BG : HUD_BG); // GUI_BAR_TEXTURES);

			/*
			 * only recalc offsets for integration if the config offsets are still default values
			 */
			int integrationXOffset = 0;
			int integrationYOffset = 0;
			if (xOffset == 0 && yOffset == 0) {
//				if (ChampionsIntegration.isEnabled()) {
//					integrationXOffset = CHAMPIONS_INTEGRATION_XOFFSET;
//				}
			 if (WailaIntegration.isEnabled()) {
					integrationXOffset = WAILA_INTEGRATION_XOFFSET;
				}
			}

			// update static variable in the event handler
			HudEventHandler.startX = xOffset + centerWidth + integrationXOffset;
			HudEventHandler.startY = yOffset + 1 + integrationYOffset;

			// 0 = startx, 0 = starty, 64 = endx, 20 = endy, 64 = width of image, 20 = height of image
			DrawableHelper.drawTexture(matrixStack, xOffset + centerWidth + integrationXOffset, yOffset + centerHeight + integrationYOffset, 0, 0, 64, 20, 64, 20);
			// display the level text
			String text = "Level " + level;
//			client.font.drawShadow(matrixStack, text,
//					xOffset + (float) (i / 2 - client.font.width(text) / 2) + integrationXOffset,
//					yOffset + (float) (centerHeight  + client.font.lineHeight - 3) + integrationYOffset, Color.WHITE.getRGB());
			int textWidth = client.textRenderer.getWidth(text);
			int fontHeight = client.textRenderer.fontHeight;
			int xPos = i / 2 - textWidth / 2;
			int yPos = centerHeight + fontHeight -3;
			client.textRenderer.drawWithShadow(matrixStack, text,
					(float)xPos + xOffset + integrationXOffset,
					(float)yPos + yOffset + integrationYOffset, Color.WHITE.getRGB());

			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.disableBlend();
		}

		return true;
	}
}
