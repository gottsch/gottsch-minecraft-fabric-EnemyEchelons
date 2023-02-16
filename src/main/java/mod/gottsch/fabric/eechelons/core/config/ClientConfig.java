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
import mod.gottsch.fabric.gottschcore.config.CoreSimpleConfig;
import mod.gottsch.fabric.gottschcore.config.SimpleConfig;

/**
 * Created by Mark Gottschling on 2/12/2023
 */
public class ClientConfig extends AbstractSimpleConfig {
    public static Integer hudXOffset;
    public static Integer hudYOffset;
    public static Integer hudRange;
    public static Boolean useDarkHud;
    public static boolean enableWailaIntegration;

    public static ClientConfig instance = new ClientConfig();


    private ClientConfig() {}

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
                        .append("# Range: -1000 to 1000\n")
                        .append("# Default: 0\n")
                        .append("hudXOffset=0\n")
                        .append("# Range: -1000 to 1000\n")
                        .append("# Default: 0\n")
                        .append("hudYOffset=0\n")
                        .append("# The distance that the HUD can be seen from (in blocks).\n")
                        .append("# Range: 0 to 100\n")
                        .append("# Default: 50\n")
                        .append("hudRange=50\n")
                        .append("# Use dark theme HUD.\n")
                        .append("# Default: true\n")
                        .append("useDarkHud=true\n")
                        .append("# Moves the Enemy Echelons HUD beside (to the left) of the WAILA HUD.\n")
                        .append("# This setting is ignored if hudXOffset or hudYOffset are set (ie. not 0).\n")
                        .append("# Default: true\n")
                        .append("enableWailaIntegration=true\n");
                return builder.toString();
            }
        };
    }

    /**
     *
     * @param filename
     */
    public static void register(String filename) {
        SimpleConfig config = SimpleConfig.of(filename).provider(instance.getProvider()).request();
        // load any additional config properties
        hudXOffset = config.getOrDefault("hudXOffset", 0);
        hudYOffset = config.getOrDefault("hudYOffset", 0);
        hudRange = config.getOrDefault("hudRange", 50);
        useDarkHud = config.getOrDefault("useDarkHud", true);
        enableWailaIntegration = config.getOrDefault("enableWailaIntegration", true);
    }


}
