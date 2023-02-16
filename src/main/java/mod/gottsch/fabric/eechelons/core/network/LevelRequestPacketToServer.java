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
