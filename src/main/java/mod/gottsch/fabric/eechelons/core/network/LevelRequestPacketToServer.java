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
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

/**
 * Created by Mark Gottschling on 2/14/2023
 */
public class LevelRequestPacketToServer {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int id = buf.readInt();
        UUID uuid = buf.readUuid();

        if (player.getWorld() == null) {
            return;
        }

        Entity entity = player.getWorld().getEntity(uuid);
        if (entity == null) {
            entity = player.getWorld().getEntityById(id);
        }

        if (entity != null) {
            int level = ((ILevelSupport)entity).getLevel();
            // send a packet back
//            EEchelons.LOGGER.debug("entityId -> {} exists on server with level -> {}", id, level);

            if (ServerPlayNetworking.canSend(player, EEchelonsNetwork.LEVEL_RESPONSE_ID)) {
                // send the level back to the client
                PacketByteBuf clientBuf = PacketByteBufs.create();
                clientBuf.writeInt(entity.getId());
                clientBuf.writeInt(level);

                ServerPlayNetworking.send(player, EEchelonsNetwork.LEVEL_RESPONSE_ID, clientBuf);
            } else {
                EEchelons.LOGGER.warn("cannot send packet to player on channel -> {}", EEchelonsNetwork.LEVEL_RESPONSE_ID);
            }
        }
//        });
    }

}
