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
package mod.gottsch.fabric.eechelons.core.network;

import mod.gottsch.fabric.eechelons.EEchelons;
import mod.gottsch.fabric.eechelons.core.data.ILevelSupport;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;

/**
 * Created by Mark Gottschling on 2/14/2023
 */
public class LevelResponsePacketToClient {
    /**
     * Received on the client
     * @param client
     * @param handler
     * @param buf
     * @param responseSender
     */
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
//        EEchelons.LOGGER.debug("client received packet");
        int id = buf.readInt();
        int level = buf.readInt();

        client.execute(() -> {
            // get the entity by uuid
            Entity entity = handler.getWorld().getEntityById(id);
            if (entity != null && entity instanceof ILevelSupport) {
//                EEchelons.LOGGER.debug("setting entity -> {} to level -> {}", entity.getDisplayName().asString(), level);
                ((ILevelSupport)entity).setLevel(level);
            }
        });
    }
}
