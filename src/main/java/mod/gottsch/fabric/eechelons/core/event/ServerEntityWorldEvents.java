package mod.gottsch.fabric.eechelons.core.event;

import mod.gottsch.fabric.eechelons.EEchelons;
import mod.gottsch.fabric.eechelons.core.data.ILevelSupport;
import mod.gottsch.fabric.eechelons.core.echelon.EchelonManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
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
            }
        }
    }
}
