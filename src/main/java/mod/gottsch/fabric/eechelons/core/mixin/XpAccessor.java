package mod.gottsch.fabric.eechelons.core.mixin;

import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobEntity.class)
public interface XpAccessor {

        @Accessor
        int getExperiencePoints();

        @Accessor("experiencePoints")
        public void setExperiencePoints(int experiencePoints);
}


