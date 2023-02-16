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
//        buf.release();
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
