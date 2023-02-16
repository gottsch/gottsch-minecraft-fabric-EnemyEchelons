/*
 * This file is part of  Enemy Echelons.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
import mod.gottsch.fabric.eechelons.core.data.ILevelSupport;
import mod.gottsch.fabric.eechelons.core.echelon.EchelonManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Created by Mark Gottshling 2-13-2023
 */
public class ServerEntityWorldEvents implements ServerEntityEvents.Load {

    @Override
    public void onLoad(Entity entity, ServerWorld world) {
        // check if HostileEntity as all Hostiles will be ILevelSupport by mixins.
        if (entity instanceof ILevelSupport) {
            if (!EchelonManager.isValidEntity(entity)) {
                return;
            }

            int data = ((ILevelSupport) entity).getLevel();
            if (data < 0) {
                EEchelons.LOGGER.debug("modifying entity attribs");
                EchelonManager.applyModifications(entity);
            }
        }
    }
}
