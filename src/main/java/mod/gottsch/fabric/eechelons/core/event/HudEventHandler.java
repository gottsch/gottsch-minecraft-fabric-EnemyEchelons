/*
 * This file is part of  Enemy Echelons.
 * Copyright (c) 2022, Mark Gottschling (gottsch)
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
package mod.gottsch.fabric.eechelons.core.event;

import mod.gottsch.fabric.eechelons.EEchelons;
import mod.gottsch.fabric.eechelons.core.client.HudUtil;
import mod.gottsch.fabric.eechelons.core.client.MouseUtil;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.Optional;

/**
 * This class was derived from Champions by TheIllusiveC4
 * @see <a href="https://github.com/TheIllusiveC4/Champions">Champions</a>
 *
 */
public class HudEventHandler implements HudRenderCallback {

	// NOTE these are only used to check where the offset is set to, so other integrations can move if they overlap
	// these are NOT used in the actual echelons rendering of background and level text 
	public static int startX = 0;
	public static int startY = 0;

	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta) {

		if (EEchelons.CONFIG.showHud() && EEchelons.CONFIG.showClientHud()) {
			MinecraftClient mc = MinecraftClient.getInstance();

			Optional<LivingEntity> livingEntity;

			if (EEchelons.CONFIG.hudRangeEnabled()) {
				// NOTE mc.cameraEntity.raycast() does not work for Entities - only returns Blocks
				livingEntity = MouseUtil.getMouseOverEchelonMob(mc, tickDelta);
			} else {
				HitResult hitResult = mc.crosshairTarget;
				if (hitResult.getType() == HitResult.Type.ENTITY
						&& ((EntityHitResult)hitResult).getEntity() instanceof LivingEntity) {
					livingEntity = Optional.of((LivingEntity)((EntityHitResult)hitResult).getEntity());
				} else {
					livingEntity = Optional.empty();
				}
			}

			livingEntity.ifPresent(entity -> {
				HudUtil.renderLevelBar(drawContext, entity);
			});
		}
	}
}
