package mod.gottsch.fabric.eechelons.core.event;

import mod.gottsch.fabric.eechelons.EEchelons;
import mod.gottsch.fabric.eechelons.core.data.ILevelSupport;
import mod.gottsch.fabric.eechelons.core.echelon.EchelonManager;
import mod.gottsch.fabric.eechelons.core.network.EEchelonsNetwork;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.network.PacketByteBuf;

/**
 * Created by Mark Gottschling on 2/14/2023
 */
public class ClientEntityWorldEvents implements ClientEntityEvents.Load {

    @Override
    public void onLoad(Entity entity, ClientWorld world) {
        // check if HostileEntity as all Hostiles will be ILevelSupport by mixins.
        if (entity instanceof HostileEntity) {
            if (!EchelonManager.isValidEntity(entity)) {
                return;
            }

            if (((ILevelSupport) entity).getLevel() == -1) {
//                EEchelons.LOGGER.debug("entity -> {} joined client, needs level update.", entity.getDisplayName().asString());
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeInt(entity.getId());
                buf.writeUuid(entity.getUuid());
                ClientPlayNetworking.send(EEchelonsNetwork.LEVEL_REQUEST_ID, buf);
            }
        }
    }
}
