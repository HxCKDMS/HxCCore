package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.lib.References;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.network.NetHandlerPlayServer;

import java.util.*;

public class EventPlayerNetworkCheck implements EventListener {
    public static Set<UUID> hasPlayerMod = new HashSet<>();

    @SubscribeEvent
    public void NetworkChnnelRegistration(FMLNetworkEvent.CustomPacketRegistrationEvent event) {
        if(event.operation.equals("REGISTER") && event.registrations.contains(References.PACKET_CHANNEL_NAME) && event.handler instanceof NetHandlerPlayServer)
            hasPlayerMod.add(((NetHandlerPlayServer) event.handler).playerEntity.getUniqueID());
    }
}
