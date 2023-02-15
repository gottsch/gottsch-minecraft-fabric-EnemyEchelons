/*
 * This file is part of  Enemy Echelons.
 * Copyright (c) 2022, Mark Gottschling (gottsch)
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
package mod.gottsch.fabric.eechelons.core.setup;

import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import mod.gottsch.fabric.eechelons.EEchelons;
import mod.gottsch.fabric.eechelons.core.config.ClientConfig;
import mod.gottsch.fabric.eechelons.core.config.CommonConfig;
import mod.gottsch.fabric.eechelons.core.config.EchelonsHolder;
import mod.gottsch.fabric.eechelons.core.config.ServerConfig;
import mod.gottsch.fabric.eechelons.core.echelon.EchelonManager;
import mod.gottsch.fabric.eechelons.core.event.AttackEntityHandler;
import mod.gottsch.fabric.eechelons.core.event.ClientEntityWorldEvents;
import mod.gottsch.fabric.eechelons.core.event.ServerEntityWorldEvents;
import mod.gottsch.fabric.eechelons.core.network.EEchelonsNetwork;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieEntity;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 
 * @author Mark Gottschling on Jul 24, 2022
 *
 */
public class Registration {
	private static final String ECHELONS_CONFIG_VERSION = "1.18.2-v1";
	public static EchelonsHolder holder;

	/**
	 *
	 */
	public static void register() {

		// TODO research and install better config, like Cloth or owo
		// load client config
		ClientConfig.register(EEchelons.MODID + "-client-config");
		// load common config
		CommonConfig.register(EEchelons.MODID + "-common-config");
		// setup rolling file appender
		CommonConfig.instance.addRollingFileAppender(EEchelons.MODID);
		// load server config
		ServerConfig.register(EEchelons.MODID + "-server-config");

		// create default server config
		createEchelonsConfig();
		// load echelons config
		holder = loadEchelonsConfig();
		EEchelons.LOGGER.info("loaded echelons -> {}", holder.echelons);
		// load echelons
		EchelonManager.build();

		// events
		ClientEntityEvents.ENTITY_LOAD.register(new ClientEntityWorldEvents());
		ServerEntityEvents.ENTITY_LOAD.register(new ServerEntityWorldEvents());
		AttackEntityCallback.EVENT.register(new AttackEntityHandler());

		// networking
		EEchelonsNetwork.registerC2S();

	}

	/**
	 *
	 */
	private static void createEchelonsConfig() {
		String fileName = "eechelons-echelons" + "-" + ECHELONS_CONFIG_VERSION + ".toml";
		Path path = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), fileName);
		File config = path.toFile();
		if (!config.exists()) {
			try {
				FileUtils.copyInputStreamToFile(
						Objects.requireNonNull(EEchelons.class.getClassLoader().getResourceAsStream(fileName)),
						config);
			} catch (IOException e) {
				EEchelons.LOGGER.error("Error creating default config for " + fileName);
			}
		}
	}

	private static EchelonsHolder loadEchelonsConfig() {
//		manifest = readResourcesFromStream(
//				Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream("data/" + modID +"/" + META_FOLDER + "/manifest.json")), ResourceManifest.class);

		String fileName = "eechelons-echelons" + "-" + ECHELONS_CONFIG_VERSION + ".toml";
		Path path = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), fileName);
		File config = path.toFile();

		final var tomlMapper = new TomlMapper();
		final EchelonsHolder data;
		try {
			data = tomlMapper.readValue(config, EchelonsHolder.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		EEchelons.LOGGER.debug("holder -> {}", data);
		return data;
	}
}
