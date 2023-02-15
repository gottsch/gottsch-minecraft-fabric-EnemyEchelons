/*
 * This file is part of  Enemy Echelons.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.fabric.eechelons.core.config;

import mod.gottsch.fabric.gottschcore.config.AbstractSimpleConfig;
import mod.gottsch.fabric.gottschcore.config.SimpleConfig;

/**
 * Created by Mark Gottschling on 2/12/2023
 */
public class ServerConfig extends AbstractSimpleConfig {

    public static Boolean showHud;

    public static ServerConfig instance = new ServerConfig();

    private ServerConfig() {}

    @Override
    public SimpleConfig.DefaultConfig getProvider() {
        return new SimpleConfig.DefaultConfig() {
            @Override
            public String get(String namespace) {
                // TODO add config properties string here
                StringBuilder builder = new StringBuilder()
                        .append("###############################\n")
                        .append("# HUD Properties\n")
                        .append("###############################\n")
                        .append("# Enable HUD display.\n")
                        .append("showHud=true\n");

                return builder.toString();
            }
        };
    }

    public static void register(String filename) {
        SimpleConfig config = SimpleConfig.of(filename).provider(instance.getProvider()).request();
        showHud = config.getOrDefault("showHud", true);
    }
}
