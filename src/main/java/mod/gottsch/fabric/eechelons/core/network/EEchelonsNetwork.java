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
