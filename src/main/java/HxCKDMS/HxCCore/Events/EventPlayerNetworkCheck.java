package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.lib.References;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.network.NetHandlerPlayServer;

import java.util.*;

@SuppressWarnings("unused")
public class EventPlayerNetworkCheck implements EventListener {
    public static Set<UUID> hasPlayerMod = new HashSet<>();

    @SubscribeEvent
    public void NetworkChannelRegistration(FMLNetworkEvent.CustomPacketRegistrationEvent event) {
        if (event.operation.equals("REGISTER") && event.registrations.contains(References.PACKET_CHANNEL_NAME) && event.handler instanceof NetHandlerPlayServer)
            hasPlayerMod.add(((NetHandlerPlayServer) event.handler).playerEntity.getUniqueID());
    }
}
