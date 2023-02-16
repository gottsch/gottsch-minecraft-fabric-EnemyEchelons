/*
 * This file is part of  Enemy Echelons.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.fabric.eechelons.core.network;

import mod.gottsch.fabric.eechelons.EEchelons;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

/**
 * Created by Mark Gottschling on 2/13/2023
 */
public class EEchelonsNetwork {
    public static final Identifier LEVEL_REQUEST_ID = new Identifier(EEchelons.MODID, "level_request");
    public static final Identifier LEVEL_RESPONSE_ID = new Identifier(EEchelons.MODID, "level_response");
    // register on the server side
    public static void registerC2S() {
        ServerPlayNetworking.registerGlobalReceiver(EEchelonsNetwork.LEVEL_REQUEST_ID, LevelRequestPacketToServer::receive);
    }

    // register on the client side
    public static void registerS2C() {
        ClientPlayNetworking.registerGlobalReceiver(EEchelonsNetwork.LEVEL_RESPONSE_ID, LevelResponsePacketToClient::receive);
    }
}
