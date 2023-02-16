/*
 * This file is part of  Enemy Echelons.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.fabric.eechelons.core.integration;

import mod.gottsch.fabric.eechelons.core.config.ClientConfig;
import net.fabricmc.loader.api.FabricLoader;

/**
 * 
 * @author Mark Gottschling on Aug 1, 2022
 *
 */
public class WailaIntegration {
	private static boolean jadeLoaded = false;
	private static boolean wthitLoaded = false;
	
	public static void init() {
		if (FabricLoader.getInstance().isModLoaded("jade")) {
			jadeLoaded = true;
		}
		else if (FabricLoader.getInstance().isModLoaded("wthit")) {
			wthitLoaded = true;
		}
	}

	public static boolean isEnabled() {
		return ClientConfig.enableWailaIntegration
				&& (jadeLoaded || wthitLoaded);
	}
	
	public static boolean isJadeLoaded() {
		return jadeLoaded;
	}

	public static boolean isWthitLoaded() {
		return wthitLoaded;
	}

}
