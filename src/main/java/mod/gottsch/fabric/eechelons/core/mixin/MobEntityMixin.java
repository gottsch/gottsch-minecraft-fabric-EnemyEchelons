/*
 * This file is part of  Mage Flame.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
 *
 * Mage Flame is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mage Flame is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Mage Flame.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.fabric.eechelons.core.mixin;

import mod.gottsch.fabric.eechelons.core.data.ILevelSupport;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Mark Gottschling on 2/13/2023
 */
@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements ILevelSupport {

    // required constructor
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    // NOTE use this to shadow methods that you don't want to implement
//    @Shadow
//    public abstract Iterable<ItemStack> getArmorItems();

    // the level determined by the echelon
    private int level = -1;

    // save the level when other data is saved
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"), cancellable = false)
    private void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("level", getLevel());
    }

    // read the level when other data is loaded
    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"), cancellable = false)
    private void readNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("level")) {
            setLevel(nbt.getInt("level"));
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
