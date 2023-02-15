package mod.gottsch.fabric.eechelons.core.event;

import mod.gottsch.fabric.eechelons.EEchelons;
import mod.gottsch.fabric.eechelons.core.data.ILevelSupport;
import mod.gottsch.fabric.eechelons.core.echelon.EchelonManager;
import mod.gottsch.fabric.eechelons.core.network.EEchelonsNetwork;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Created by Mark Gottshling 2/13/2023
 */
public class ServerEntityWorldEvents implements ServerEntityEvents.Load {

    @Override
    public void onLoad(Entity entity, ServerWorld world) {
        // check if HostileEntity as all Hostiles will be ILevelSupport by mixins.
        if (entity instanceof HostileEntity) {
            if (!EchelonManager.isValidEntity(entity)) {
                return;
            }

            int data = ((ILevelSupport) entity).getLevel();
            if (data < 0) {
                EEchelons.LOGGER.debug("modifying entity attribs");
                EchelonManager.applyModifications(entity);
                // TEMP
                ((ILevelSupport) entity).setLevel(5);
            }
        }

        // TEMP Testing
//        else if (entity instanceof PlayerEntity) {
//            EEchelons.LOGGER.debug("player joined");
//            if (ServerPlayNetworking.canSend((ServerPlayerEntity) entity, EEchelonsNetwork.LEVEL_RESPONSE_ID)) {
//                EEchelons.LOGGER.debug("player can send");
//            }
//            else {
//                EEchelons.LOGGER.debug("player can not send");
//            }
////            (ServerPlayerEntity) entity, EEchelonsNetwork.LEVEL_REQUEST_ID,
////            ((ServerPlayerEntity) entity).networkHandler.sendPacket(
//////                    ServerPlayNetworking.send(
////                    new CustomPayloadS2CPacket(EEchelonsNetwork.LEVEL_REQUEST_ID, PacketByteBufs.empty()));
////            ServerSidePacketRegistry.INSTANCE.sendToPlayer((ServerPlayerEntity) entity, EEchelonsNetwork.LEVEL_REQUEST_ID, PacketByteBufs.empty());
//            ServerPlayNetworking.send((ServerPlayerEntity) entity, EEchelonsNetwork.LEVEL_REQUEST_ID, PacketByteBufs.empty());
//        }
    }
}
