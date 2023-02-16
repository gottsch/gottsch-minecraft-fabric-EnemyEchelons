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

import mod.gottsch.fabric.eechelons.core.config.ClientConfig;
import mod.gottsch.fabric.eechelons.core.data.ILevelSupport;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

/**
 * This class was derived from Champions by TheIllusiveC4
 * The minecraft code was taken from GameRenderer.
 * @see <a href="https://github.com/TheIllusiveC4/Champions">Champions</a>
 *
 */
public class MouseUtil {
	public static Optional<LivingEntity> getMouseOverEchelonMob(MinecraftClient mc, float partialTicks) {
		Entity player = mc.getCameraEntity();
		if (player != null) {
			if (mc.world != null) {
				double range = ClientConfig.hudRange;
				HitResult rayTraceResult = player.raycast(range, partialTicks, false);
				Vec3d vec3d = player.getCameraPosVec(partialTicks);
				double distance = rayTraceResult.squaredDistanceTo(mc.player);
				Vec3d vec3d2 = player.getRotationVec(1.0F);
				Vec3d vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
				Box box = player.getBoundingBox().stretch(vec3d2.multiply(range)).expand(1.0D, 1.0D, 1.0D);
				EntityHitResult entityRayTraceResult =
						ProjectileUtil.raycast(player, vec3d, vec3d3, box, (e) -> !e.isSpectator() && e.canHit(), distance);

				if (entityRayTraceResult != null) {
					Entity hoverEntity = entityRayTraceResult.getEntity();
					if(hoverEntity instanceof ILevelSupport) {
						return Optional.of((LivingEntity)hoverEntity);
					}
				}
			}
		}
		return Optional.empty();
	}
}
