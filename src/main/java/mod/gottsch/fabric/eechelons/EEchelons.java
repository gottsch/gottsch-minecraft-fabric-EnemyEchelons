/*
 * This file is part of  Enemy Echelons.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
 *
 * All rights reserved.
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
package mod.gottsch.fabric.eechelons;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import mod.gottsch.fabric.eechelons.core.config.ClientConfig;
import mod.gottsch.fabric.eechelons.core.config.CommonConfig;
import mod.gottsch.fabric.eechelons.core.config.ServerConfig;
import mod.gottsch.fabric.eechelons.core.setup.Registration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Many thanks go out to TheIllusiveC4 as the EchelonConfig
 * loading code was derived from Champions.
 *
 * @author Mark Gottschling on Jul 24, 2022
 * @see <a href="https://github.com/TheIllusiveC4/Champions">Champions</a>
 */
public class EEchelons implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(EEchelons.MODID);

    public static final String MODID = "eechelons";

    @Override
    public void onInitialize() {
        Registration.register();


        // register the server config
//		ModLoadingContext.get().registerConfig(Type.CLIENT, Config.CLIENT_SPEC);
//		ModLoadingContext.get().registerConfig(Type.COMMON, Config.COMMON_SPEC);
//		ModLoadingContext.get().registerConfig(Type.SERVER, Config.SERVER_SPEC);
        // create the default config


        // register the setup method for mod loading
//		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//		// register 'ModSetup::init' to be called at mod setup time (server and client)
//		modEventBus.addListener(CommonSetup::init);
//		modEventBus.addListener(this::config);
//

//		EEchelonsNetwork.register();
//		ChampionsIntegration.init();
//		WailaIntegration.init();
    }


}
